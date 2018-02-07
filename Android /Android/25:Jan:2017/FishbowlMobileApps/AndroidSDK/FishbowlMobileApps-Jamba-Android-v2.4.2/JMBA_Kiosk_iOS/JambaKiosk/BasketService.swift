//
//  BasketService.swift
//  JambaJuice
//
//  Created by Taha Samad on 27/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

typealias BasketErrorCallback = (error: NSError?) -> Void
typealias PlaceOrderCallback = (orderStatus: OrderStatus?, error: NSError?) -> Void

class BasketService {

    static let sharedInstance = BasketService()
    static let addProductNotificationName = "AddBasketProductNotification"

    private(set) var currentBasket: Basket?

    func updateBasket(basket: Basket) {
        currentBasket = basket
    }

    /// Create new basket for a given Olo restaurant
    func createBasket(callback: BasketErrorCallback) {
        guard let restaurantId = StoreService.sharedInstance.currentStore?.restaurantId else {
            return
        }
        // Create new basket for the selected restaurant
        OloBasketService.createBasket(restaurantId) { (oloBasket, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            // Add reference to store in new basket
            self.currentBasket = Basket(oloBasket: oloBasket!)
            callback(error: nil)
            AnalyticsService.trackEvent("order_ahead", action: "create_basket")
        }
    }

    func numberOfItemsInBasket() -> Int64 {
        guard let basket = currentBasket else {
            return 0
        }
        return basket.products.map { $0.quantity }.reduce(0, combine: +)
    }

    /// Add a given Product to the existing basket
    func addProduct(product: Product, quantity: Int64, options: [String], specialInstructions: String?, callback: BasketErrorCallback) {
        guard let basket = currentBasket else {
            callback(error: NSError(description: "Basket not found."))
            return
        }

        let existingQuantity = numberOfItemsInBasket()
        if existingQuantity + quantity > 10 {
            callback(error: NSError(description: "Can not add more than 10 items in your cart per order."))
            return
        }

        if product.storeMenuProduct == nil {
            callback(error: NSError(description: "Product not available at selected store."))
            return
        }

        var newBasketProduct = OloNewBasketProduct()
        newBasketProduct.productId = product.storeMenuProduct!.productId
        newBasketProduct.quantity = quantity
        newBasketProduct.options = options.joinWithSeparator(",")
        newBasketProduct.specialInstructions = specialInstructions
        OloBasketService.addProduct(basket.basketId, newBasketProduct: newBasketProduct) { (oloBasket, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            self.currentBasket = Basket(oloBasket: oloBasket!)
            callback(error: nil)
            NSNotificationCenter.defaultCenter().postNotificationName(BasketService.addProductNotificationName, object: nil)

            AnalyticsService.trackEvent("order_ahead", action: "add_product", label: product.name, value: Int(quantity))
            if newBasketProduct.specialInstructions != nil && newBasketProduct.specialInstructions!.isEmpty == false {
                AnalyticsService.trackEvent("order_ahead", action: "special_instructions", label: newBasketProduct.specialInstructions!)
            }
            for option in options {
                AnalyticsService.trackEvent("order_ahead", action: "add_boost", label: option)
            }
        }
    }

    /// Remove a product from the existing basket
    func removeProduct(basketProduct: BasketProduct, callback: BasketErrorCallback) {
        guard let basket = currentBasket else {
            callback(error: NSError(description: "Basket not found."))
            return
        }

        OloBasketService.removeProduct(basket.basketId, basketProductId: basketProduct.basketProductId) { (oloBasket, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            self.currentBasket = Basket(oloBasket: oloBasket!)
            callback(error: nil)

            AnalyticsService.trackEvent("order_ahead", action: "remove_product", label: basketProduct.name, value: Int(basketProduct.quantity))
            for option in basketProduct.choices {
                AnalyticsService.trackEvent("order_ahead", action: "remove_boost", label: "\(option.optionId)")
            }
        }
    }

    func validateBasket(callback: BasketErrorCallback) {
        guard let basket = currentBasket else {
            callback(error: NSError(description: "Basket not found."))
            return
        }
        OloBasketService.validate(basket.basketId) { (result, error) -> Void in
            callback(error: error)
        }
    }

    func placeOrderWithSwipedCreditCard(creditCard: SwipedCreditCard, receiptEmailAddress: String?, callback: PlaceOrderCallback) {
        if currentBasket == nil {
            callback(orderStatus: nil, error: NSError(description: "Basket not found."))
            return
        }

        var submitBody = OloBasketSubmitBody()
        submitBody.userType = "guest"
        submitBody.firstName = creditCard.firstName
        submitBody.lastName = creditCard.lastName
        submitBody.emailAddress = receiptEmailAddress ?? "eneko@wearehathway.com" // Constants.defaultEmailAddress
        submitBody.contactNumber = "8052861458" // Constants.defaultPhoneNumber
        submitBody.reference = NSUUID().UUIDString
        submitBody.billingMethod = "creditcard"
        submitBody.cardNumber = creditCard.cardNumber
        submitBody.cvv = creditCard.cvv
        submitBody.zip = creditCard.zipcode
        submitBody.expiryMonth = creditCard.expirationMonth
        submitBody.expiryYear = creditCard.expirationYear.characters.count == 2 ? "20\(creditCard.expirationYear)" : creditCard.expirationYear
        submitBody.saveOnFile = "false"
        placeOrder(submitBody, callback: callback)
    }

    func placeOrderWithSavedCreditCard(userCard: UserSavedCreditCard, callback: PlaceOrderCallback) {
        if currentBasket == nil {
            callback(orderStatus: nil, error: NSError(description: "Basket not found."))
            return
        }
        if UserService.sharedUser == nil {
            callback(orderStatus: nil, error: NSError(description: "User not logged in."))
            return
        }
        var submitBody = OloBasketSubmitBody()
        submitBody.userType = "user"
        submitBody.billingMethod = "billingaccount"
        submitBody.billingAccountId = "\(userCard.accountId)"
        placeOrder(submitBody, callback: callback)
    }

    private func placeOrder(submitBody: OloBasketSubmitBody, callback: PlaceOrderCallback) {
        validateBasket { error in
            if error != nil {
                callback(orderStatus: nil, error: error)
                return
            }
            guard let basketId = self.currentBasket?.basketId else {
                callback(orderStatus: nil, error: NSError(description: "Basket not found."))
                return
            }
            OloBasketService.submit(basketId, basketSubmitBody: submitBody) { (status, error) -> Void in
                if error != nil {
                    callback(orderStatus: nil, error: error)
                    return
                }
                var orderStatus = OrderStatus(oloOrderStatus: status!)
                orderStatus.store = StoreService.sharedInstance.currentStore

                // Track analytics
                AnalyticsService.trackPurchase(orderStatus, products: self.currentBasket!.products)
                AnalyticsService.trackEvent("order_ahead", action: "submit_order", label: "basket_size_\(self.currentBasket!.products.count)")

                // Audit log
                AuditService.trackOrder(self.currentBasket!, orderStatus: orderStatus)

                self.currentBasket = nil
                callback(orderStatus: orderStatus, error: nil)
            }
        }
    }

    /// Clear the entire basket
    func deleteBasket() {
        currentBasket = nil
        AnalyticsService.trackEvent("order_ahead", action: "delete_basket")
    }

}
