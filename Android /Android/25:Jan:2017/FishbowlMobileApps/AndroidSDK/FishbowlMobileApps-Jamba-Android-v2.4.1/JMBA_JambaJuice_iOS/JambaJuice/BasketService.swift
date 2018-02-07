//
//  BasketService.swift
//  JambaJuice
//
//  Created by Taha Samad on 27/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK
import InCommSDK
import UIKit

typealias BasketCallback = (_ basket: Basket?, _ error: NSError?) -> Void
typealias BasketErrorCallback = (_ error: NSError?) -> Void
typealias PlaceOrderCallback = (_ orderStatus: OrderStatus?, _ error: NSError?) -> Void
typealias BasketRewardsCallback = (_ rewards: [Reward], _ error: NSError?) -> Void
//typealias DeleteBasketCallback = (status: NSString) -> Void

class BasketService: NSObject {
    
    /// Current basket
    fileprivate(set) static var sharedBasket: Basket? {
        didSet {
            NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.SharedBasketUpdated.rawValue), object: self)
        }
    }
    
    static var lastOrderStatus: OrderStatus?
    
    /// Create new basket for a given Olo restaurant
    class func createBasket(_ store: Store, callback: @escaping BasketCallback) {
        if store.restaurantId == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Restaurant does not support Order Ahead"]))
            return
        }
        
        // Create new basket for the selected restaurant
        OloBasketService.createBasket(store.restaurantId!) { (oloBasket, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            // Add reference to store in new basket
            self.sharedBasket = Basket(store: store, oloBasket: oloBasket!)
            callback(self.sharedBasket, nil)
            AnalyticsService.trackEvent("order_ahead", action: "create_basket")
        }
    }
    
    class func itemsInBasket() -> Int64 {
        if sharedBasket == nil {
            return 0
        }
        var existingQuantity: Int64 = 0
        for product in sharedBasket!.products {
            existingQuantity += Int64(product.quantity)
        }
        return existingQuantity
    }
    
    //add product with options quantity
    class func addProductWithOptionQuantity(_ product: Product, quantity: Int64, options: [OloProductChoiceQuantity], specialInstructions: String?, callback: @escaping BasketCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        
        let existingQuantity: Int64 = itemsInBasket()
        
        if existingQuantity + quantity >  Constants.oloItemLimit {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Can not add more than \(Constants.oloItemLimit) items in your cart per order."]))
            return
        }
        
        if product.storeMenuProduct == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Product not available at selected store."]))
            return
        }
        
        var productWithChoiceQuantitys:[OloProductWithChoiceQuantity] = []
        
        var productWithChoiceQuantity = OloProductWithChoiceQuantity()
        productWithChoiceQuantity.productId = product.storeMenuProduct!.productId
        productWithChoiceQuantity.quantity = quantity
        productWithChoiceQuantity.specialInstructions = specialInstructions
        productWithChoiceQuantity.choices = options
        
        productWithChoiceQuantitys.append(productWithChoiceQuantity)
        
        OloBasketService.addProductsWithChoiceQuantity(sharedBasket!.id, newBasketProducts: productWithChoiceQuantitys) { (oloBasket, error) in
            if error != nil {
                callback(nil, error)
                return
            } else if oloBasket != nil && oloBasket!.errors.count > 0 {
                let addProductError = NSError(domain: "Error", code: 400, userInfo: [NSLocalizedDescriptionKey:oloBasket!.errors[0].message])
                callback(nil, addProductError)
                return
            }
            self.sharedBasket!.updateBasketAndNotify((oloBasket?.basket)!)
            callback(self.sharedBasket, nil)
            AnalyticsService.trackEvent("order_ahead", action: "add_product", label: product.name, value: Int(quantity))
            if productWithChoiceQuantity.specialInstructions != nil && productWithChoiceQuantity.specialInstructions!.isEmpty == false {
                AnalyticsService.trackEvent("order_ahead", action: "special_instructions", label: productWithChoiceQuantity.specialInstructions!)
            }
            for option in options {
                AnalyticsService.trackEvent("order_ahead", action: "add_boost", label: String(option.choiceId))
            }
        }
    }
    
    
    /// Add a given Product to the existing basket
    class func addProduct(_ product: Product, quantity: Int64, options: [String], specialInstructions: String?, callback: @escaping BasketCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        
        let existingQuantity: Int64 = itemsInBasket()
        
        if existingQuantity + quantity >  Constants.oloItemLimit {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Can not add more than \(Constants.oloItemLimit) items in your cart per order."]))
            return
        }
        
        if product.storeMenuProduct == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Product not available at selected store."]))
            return
        }
        
        var productWithChoiceQuantity = OloProductWithChoiceQuantity()
        productWithChoiceQuantity.productId = product.storeMenuProduct!.productId
        productWithChoiceQuantity.quantity = quantity
        productWithChoiceQuantity.specialInstructions = specialInstructions
        
        
        var newBasketProduct = OloNewBasketProduct()
        newBasketProduct.productId = product.storeMenuProduct!.productId
        newBasketProduct.quantity = quantity
        newBasketProduct.options = options.joined(separator: ",")
        newBasketProduct.specialInstructions = specialInstructions
        
        OloBasketService.addProduct(sharedBasket!.id, newBasketProduct: newBasketProduct) { (oloBasket, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(self.sharedBasket, nil)
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
    class func removeProduct(_ basketProduct: BasketProduct, callback: @escaping BasketCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        
        OloBasketService.removeProduct(sharedBasket!.id, basketProductId: basketProduct.id) { (oloBasket, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(self.sharedBasket, nil)
            AnalyticsService.trackEvent("order_ahead", action: "remove_product", label: basketProduct.name, value: Int(basketProduct.quantity))
            for option in basketProduct.choices {
                AnalyticsService.trackEvent("order_ahead", action: "remove_boost", label: "\(option.optionId)")
            }
        }
    }
    
    class func updateTimeWanted(_ callback: @escaping BasketCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        
        // If Time Wanted is not set, remove any previously times set in the server
        if sharedBasket!.timeWanted == nil {
            OloBasketService.deleteTimeWanted(sharedBasket!.id) { (oloBasket, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                self.sharedBasket!.updateBasketAndNotify(oloBasket!)
                callback(self.sharedBasket, nil)
                AnalyticsService.trackEvent("order_ahead", action: "pickup_time", label: "ASAP")
            }
            return
        }
        
        // Get Date components for time wanted
        let date = sharedBasket!.timeWanted!.dateFromOloDateTimeString()!
        let day = Int64(date.dayOfMonthInGregorianCalendar())
        let month = Int64(date.monthOfYearInGregorianCalendar())
        let year = Int64(date.yearInGregorianCalendar())
        let hour = Int64(date.hourOfDayInGregorianCalendar())
        let min = Int64(date.minOfHourInGregorianCalendar())
        
        OloBasketService.updateTimeWanted(sharedBasket!.id, isManualFire: false, year: year, month: month, day: day, hour: hour, minute: min) { (oloBasket, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(self.sharedBasket, nil)
            
            let day = date.isTodayInGregorianCalendar() ? "today" : "tomorrow"
            let time = String(format: "%02d:%02d", hour, min)
            AnalyticsService.trackEvent("order_ahead", action: "pickup_time", label: "\(day)::\(time)")
        }
    }
    
    class func validateBasket(_ callback: @escaping BasketCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        OloBasketService.validate(sharedBasket!.id) { (result, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            callback(self.sharedBasket, nil)
        }
    }
    
    class func updateTimeWantedAndValidate(_ callback: @escaping BasketErrorCallback) {
        updateTimeWanted { (basket, error) -> Void in
            if error != nil {
                callback(error)
                return
            }
            self.validateBasket { (basket, error) -> Void in
                callback(error)
            }
        }
    }
    
    class func placeOrderWithCreditCard(_ cardNumber: String, securityCode: String, zip: String, expiryMonth: String, expiryYear: String, savePaymentInfo: Bool, callback:@escaping PlaceOrderCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        var submitBody = OloBasketSubmitBody()
        if UserService.sharedUser != nil {
            submitBody.userType = "user"
            submitBody.contactNumber = UserService.sharedUser!.phoneNumber
        }
        else {
            submitBody.userType = "guest"
            submitBody.firstName = UserService.guestUserInfo!.firstName
            submitBody.lastName = UserService.guestUserInfo!.lastName
            submitBody.emailAddress = UserService.guestUserInfo!.emailAddress
            submitBody.reference = UserService.guestUserInfo!.reference
            submitBody.contactNumber = UserService.guestUserInfo!.phoneNumber
        }
        submitBody.billingMethod = "creditcard"
        submitBody.cardNumber = cardNumber
        submitBody.cvv = securityCode
        submitBody.zip = zip
        submitBody.expiryMonth = expiryMonth
        submitBody.expiryYear = expiryYear
        submitBody.saveOnFile = savePaymentInfo ? "true" : "false"
        placeOrderInternal(submitBody, callback: callback)
    }
    
    //Mark: Place order with gift card
    class func placeOrderWithGiftCard(_ giftCard: InCommUserGiftCard, callback:@escaping PlaceOrderCallback){
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        if UserService.sharedUser == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"User not logged in."]))
            return
        }
        var submitBody = OloBasketSubmitBody()
        submitBody.userType = "user"
        submitBody.billingMethod = "storedvalue"
        submitBody.billingSchemeId = getOloBillingSchemeIdForGiftCard()
        var billingField = OloBillingField()
        billingField.name = "number"
        billingField.value = giftCard.cardNumber
        submitBody.billingFields.append(billingField)
        
        billingField = OloBillingField()
        billingField.name = "pin"
        billingField.value = giftCard.cardPin!
        submitBody.billingFields.append(billingField)
        
        //submitBody.saveOnFile = "false"
        
        placeOrderInternal(submitBody, callback: callback)
    }
    
    class func getOloBillingSchemeIdForGiftCard() -> Int64 {
        for oloBillingSchemes in (BasketService.sharedBasket?.billingSchemes)! {
            if oloBillingSchemes.type == "giftcard" {
                return oloBillingSchemes.id
            }
        }
        return 0
    }
    
    class func placeOrderWithBillingAccount(_ billingAccount: BillingAccount, callback:@escaping PlaceOrderCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        if UserService.sharedUser == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"User not logged in."]))
            return
        }
        var submitBody = OloBasketSubmitBody()
        submitBody.userType = "user"
        submitBody.billingMethod = "billingaccount"
        submitBody.billingAccountId = "\(billingAccount.accountId)"
        placeOrderInternal(submitBody, callback: callback)
    }
    
    
    //MARK: - Place order with gift card
    fileprivate class func placeOrderInternal(_ submitBody: OloBasketSubmitBody, callback: @escaping PlaceOrderCallback) {
        validateBasket { (basket, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            OloBasketService.submit(self.sharedBasket!.id, basketSubmitBody: submitBody) { (status, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                var orderStatus = OrderStatus(oloOrderStatus: status!)
                orderStatus.store = self.sharedBasket!.store
                ProductService.updateRecentlyOrderedProducts(self.sharedBasket!)
                
                // Track analytics
                AnalyticsService.trackPurchase(orderStatus, products: self.sharedBasket!.products)
                AnalyticsService.trackEvent("order_ahead", action: "submit_order", label: "basket_size_\(self.sharedBasket!.products.count)")
                
                // Push notification segmentation
                if let _ = SettingsManager.setting("first_purchase") as? Bool {
                    // NotificationService.removeTag("first_purchase")
                } else {
                    // NotificationService.addTag("first_purchase")
                    SettingsManager.setSetting("first_purchase", value: true as AnyObject)
                }
                
                // Audit log
                AuditService.trackOrder(self.sharedBasket!, orderStatus: orderStatus)
                
                //                self.sharedBasket = nil
                self.deleteAndCreateNewBasket(nil)
                callback(orderStatus, nil)
            }
        }
    }
    //MARK: -
    
    /// Clear the entire basket
    /* class func deleteBasket() {
     self.createBasket(CurrentStoreService.sharedInstance.currentStore!) { (basket, error) -> Void in
     if(basket==nil || error != nil){
     
     }
     else{
     sharedBasket=nil
     sharedBasket=basket;
     }
     }
     // sharedBasket = nil
     AnalyticsService.trackEvent("order_ahead", action: "delete_basket")
     }*/
    
    /// Apply promotion code
    /// If rewards or promotions were already applied, remove them first
    class func applyPromotionCode(_ promotionCode: String, callback: @escaping BasketCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        
        // Verify if basket has already a reward, if so, remove it and try again
        if sharedBasket!.appliedRewards.count > 0 {
            removeRewards { (basket, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                self.applyPromotionCode(promotionCode, callback: callback)
            }
            return
        }
        
        // Verify if basket has a promotion applied, if so, remove it and try again
        if sharedBasket!.promotionCode != nil{
            removePromotionCode { (basket, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                self.applyPromotionCode(promotionCode, callback: callback)
            }
            return
        }
        
        // Apply promotion code
        OloBasketService.applyCoupon(sharedBasket!.id, couponCode: promotionCode) { (oloBasket, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            // Remove shared basket offer id as nil when applied an offer
            sharedBasket?.offerId = nil
            // Set promotion code before notification is fired
            self.sharedBasket!.promotionCode = promotionCode
            // Set fbOfferApplied basket id
            self.sharedBasket!.fbOfferAppliedBasketId = oloBasket!.id
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(self.sharedBasket, nil)
            AnalyticsService.trackEvent("order_ahead", action: "apply_coupon", label: promotionCode)
        }
    }
    
    /// Remove applied prmotion code
    class func removePromotionCode(_ callback: @escaping BasketCallback) {
        if sharedBasket?.promotionCode == nil {
            // If no promotion was applied, return no error
            callback(sharedBasket, nil)
            return
        }
        
        OloBasketService.deleteCoupon(sharedBasket!.id) { (oloBasket, error) -> Void in
            if error != nil {
                if error!.code == 9{
                    OloBasketService.basketWithBasketId(sharedBasket!.id, callback: { (basket, error) in
                        if error != nil{
                            callback(nil, error)
                            return
                        }else{
                            let promotionCode = self.sharedBasket!.promotionCode
                            self.sharedBasket!.promotionCode = nil
                            // Remove offer id
                            self.sharedBasket!.offerId = nil
                            self.sharedBasket!.updateBasketAndNotify(basket!)
                            callback(self.sharedBasket, nil)
                            AnalyticsService.trackEvent("order_ahead", action: "remove_coupon", label: promotionCode)
                        }
                    })
                }else{
                    callback(nil, error)
                    return
                }
            } else{
                //Set promotion code before notif is fired
                let promotionCode = self.sharedBasket!.promotionCode
                self.sharedBasket!.promotionCode = nil
                // Remove offer id
                self.sharedBasket!.offerId = nil
                
                self.sharedBasket!.updateBasketAndNotify(oloBasket!)
                callback(self.sharedBasket, nil)
                AnalyticsService.trackEvent("order_ahead", action: "remove_coupon", label: promotionCode)
            }
        }
    }
    
    /// Retrieve available rewards for the current basket / restaurant
    /// We need to ensure loyalty accounts are linked, which we do by posting the phonenumber to loyaltyschemes
    ///  1. Get loyatly schemes, if membership is null, post phone number and get loyalty schemes
    ///  2. Get rewards available for that membership id
    class func availableRewards(_ callback: @escaping BasketRewardsCallback) {
        let basketId = sharedBasket?.id
        if basketId == nil {
            callback([], NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        
        let userPhone = UserService.sharedUser?.phoneNumber
        if userPhone == nil {
            callback([], NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"User phone number not found"]))
            return
        }
        
        OloBasketService.loyaltySchemes(basketId!) { (loyaltySchemes, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            
            // Get Jamba Juice scheme
            let jambaJuiceScheme = loyaltySchemes.filter{ $0.name == "Jamba Insider Rewards" }.first
            if jambaJuiceScheme == nil {
                callback([], nil)  // No rewards available for the user, might be an error
                return
            }
            
            // Check if user is member yet, if not, make it a member and try again
            let membershipId = jambaJuiceScheme?.membership?.id
            if membershipId == nil {
                OloBasketService.configureLoyaltySchemes(basketId!, schemeId: jambaJuiceScheme!.id, phoneNumber: userPhone!) { (error) -> Void in
                    if error != nil {
                        callback([], error)
                        return
                    }
                    // Try again
                    self.availableRewards(callback)
                }
                // Nothing to do, let the callback roll again
                return
            }
            
            OloBasketService.loyaltyRewards(self.sharedBasket!.id, membershipId: membershipId!) { (loyaltyRewards, error) -> Void in
                if error != nil {
                    callback([], error)
                    return
                }
                let rewards = loyaltyRewards.map { Reward(oloReward: $0) }
                callback(rewards, nil)
            }
        }
    }
    
    /// Apply new reward to basket
    /// If rewards or promotions were already applied, remove them
    class func applyReward(_ reward: Reward, callback: @escaping BasketCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        if reward.reference == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Missing reward reference"]))
            return
        }
        if reward.membershipId == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Missing reward membership Id"]))
            return
        }
        
        // Verify if basket has already a reward, if so, remove it and try again
        if sharedBasket!.appliedRewards.count > 0 {
            removeRewards { (basket, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                self.applyReward(reward, callback: callback)
            }
            return
        }
        
        // Verify if basket has a promotion applied, if so, remove it and try again
        if sharedBasket!.promotionCode != nil {
            removePromotionCode { (basket, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                self.applyReward(reward, callback: callback)
            }
            return
        }
        
        // Add reward to basket
        OloBasketService.applyReward(sharedBasket!.id, membershipId: reward.membershipId!, rewardReference: reward.reference!) { (basket, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            self.sharedBasket!.promotionCode = nil
            self.sharedBasket!.offerId = nil
            self.sharedBasket!.updateBasketAndNotify(basket!)
            callback(self.sharedBasket, nil)
            AnalyticsService.trackEvent("order_ahead", action: "apply_reward", label: reward.name)
        }
    }
    
    /// Remove any applied rewards
    class func removeRewards(_ callback: @escaping BasketCallback) {
        let reward = sharedBasket?.appliedRewards.first
        if reward == nil {
            // If no rewards available, no error
            callback(self.sharedBasket, nil)
            return
        }
        if reward!.rewardId == nil {
            
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Missing reward Id"]))
            return
        }
        OloBasketService.removeReward(sharedBasket!.id, rewardId: reward!.rewardId!) { (basket, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            // Log before removing next, if any
            AnalyticsService.trackEvent("order_ahead", action: "remove_reward", label: reward!.name)
            
            if basket!.appliedRewards.count > 0 {
                self.removeRewards(callback)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(basket!)
            callback(self.sharedBasket, nil)
        }
    }
    
    class func billingSchemes(_ callback: @escaping BasketCallback) {
        if sharedBasket == nil {
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Basket not found."]))
            return
        }
        OloBasketService.billingSchemes(sharedBasket!.id) { (oloBillingSchemes, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            self.sharedBasket!.billingSchemes = oloBillingSchemes.map { oloBillingScheme in BillingScheme(oloBillingScheme: oloBillingScheme) }
            callback(self.sharedBasket, nil)
        }
    }
    
    class func orderAgain(_ orderStatus: OrderStatus, callback: @escaping BasketCallback) {
        // See if we can find a restaurant for this store
        if orderStatus.store == nil {
            StoreService.storeByStoreCode(orderStatus.vendorExtRef) { (stores, location, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                if stores.count == 0 {
                    callback(nil, NSError(description: "Unable to retrieve store information"))
                    return
                }
                let store = stores[0]
                self.orderAgainUsingStore(store, orderStatus: orderStatus, callback: callback)
            }
        }
        else {
            self.orderAgainUsingStore(orderStatus.store!, orderStatus: orderStatus, callback: callback)
        }
    }
    
    fileprivate class func orderAgainUsingStore(_ store: Store, orderStatus: OrderStatus, callback: @escaping BasketCallback) {
        StoreService.menuForStore(store) { (storeMenu, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            if storeMenu == nil {
                callback(nil, NSError(description: "Failed to retrieve store menu"))
                return
            }
            OloBasketService.createFromOrderStatusId(orderStatus.id) { (oloBasket, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                // Add reference to store in new basket
                self.sharedBasket = Basket(store: store, oloBasket: oloBasket!)
                // Since we have basket, we can now safely store the menu
                store.storeMenu = storeMenu!
                NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.OrderStarted.rawValue), object: self)
                callback(self.sharedBasket, nil)
                AnalyticsService.trackEvent("order_ahead", action: "order_again", label: orderStatus.orderRef)
            }
        }
    }
    
    //MARK:- Reorder from favourite order
    class func reOrderFavouriteOrder(_ order: FavouriteOrder, callback: @escaping BasketCallback) {
        // load store menu
        StoreService.menuForStore(order.store!) { (storeMenu, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            if storeMenu == nil {
                callback(nil, NSError(description: "Failed to retrieve store menu"))
                return
            }
            
            OloBasketService.createFromFave(order.id, callback: { (basket, error) in
                if error != nil {
                    callback(nil, error)
                    return
                }
                // Add reference to store in new basket
                self.sharedBasket = Basket(store: order.store!, oloBasket: basket!)
                // Since we have basket, we can now safely store the menu
                order.store!.storeMenu = storeMenu!
                NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.OrderStarted.rawValue), object: self)
                callback(self.sharedBasket, nil)
                
            })
            
        }
    }
    
    class func deleteBasket() {
        sharedBasket = nil
        AnalyticsService.trackEvent("order_ahead", action: "delete_basket")
    }
    
    // delete and create new basket based on current choosen store. if current store not available then delete only basket.
    class func deleteAndCreateNewBasket(_ completion: ((_ status: Bool) -> Void)?) {
        //self.deleteBasket()
        let store = CurrentStoreService.sharedInstance.currentStore;
        if(store != nil){
            self.createBasket(CurrentStoreService.sharedInstance.currentStore!) { (basket, error) -> Void in
                if(basket == nil || error != nil){
                    if(completion != nil){
                        completion!(false)
                        // create basket failed
                    }
                }else{
                    if(completion != nil){
                        completion!(true)
                        // create basket success
                    }
                }
            }
        }
    }
    
    // Create basket for current store
    class func createBasketForCurrentStore(_ store: Store!,oloBasket:OloBasket){
        self.sharedBasket = Basket(store: store,oloBasket: oloBasket)
    }
    
    //MARK: validate basket
    class func validateBasketForRewardAndOffer() -> Bool{
        if let basket = BasketService.sharedBasket{
            if basket.appliedRewards.count > 0 || basket.offerId != nil || basket.promotionCode != nil{
                return true
            }else{
                return false
            }
        }else{
            return false
        }
    }
    
    //MARK: Validate Applied Reward
    class func validateAppliedReward() -> Bool{
        if let basket = BasketService.sharedBasket{
            if basket.appliedRewards.count > 0{
                return true
            }else{
                return false
            }
        }else{
            return false
        }
    }
    
    //MARK: Validate Applied Offer
    class func validateAppliedOffer() -> Bool{
        if let basket = BasketService.sharedBasket{
            if basket.offerId != nil || basket.promotionCode != nil{
                return true
            }else{
                return false
            }
        }else{
            return false
        }
    }
    
    class func basketDiscountValidation() -> Bool{
        if let basket = BasketService.sharedBasket{
            if basket.offerId != nil{
                return true
            }else{
                return false
            }
        }else{
            return false
        }
    }
    
    //MARK:- Get Delivery Address
    class func getDeliveryAddress() -> (String,Bool) {
        if let basket = BasketService.sharedBasket{
            if let deliveryAddress = basket.deliveryAddress {
                if deliveryAddress.zipcode != "" {
                    var address = ""
                    let cityZip = [deliveryAddress.city, deliveryAddress.zipcode].joined(separator: ", ")
                    address = "\(address)\(deliveryAddress.streetaddress)\n\(cityZip)"
                    return (address, true)
                }
            }
        }
        return ("Please enter Delivery Address",false)
    }
    
    //MARK:- Delivery Address
    class func addDeliveryAddress(_ deliveryAddress: OloDeliveryAddress,callback:@escaping OloBasketCallback) {
        OloBasketService.saveDeliveryaddress(deliveryAddress, basketId: sharedBasket!.id) { (basket, error) in
            if error != nil {
                callback(nil,error)
            } else {
                self.sharedBasket!.updateBasketAndNotify(basket!)
//                self.changeDeliveryMode(sharedBasket!.deliveryMode, callback: { (basket, error) in
                    callback(basket, nil)
//                })
//                AnalyticsService.trackEvent("order_ahead", action: "create_basket")
            }
        }
    }
    
    class func removeDeliveryAddress(_ id:String, callback:@escaping BasketErrorCallback) {
        OloUserService.deleteSavedDeliveryaddressList(id) { (error) in
            if error != nil {
                callback(error)
            } else {
                callback(nil)
            }
        }
    }
    
    class func changeDeliveryMode(_ deliveryMode: String,callback:@escaping BasketCallback) {
        OloBasketService.changeDeliveryMode(deliveryMode, basketId: sharedBasket!.id) { (basket, error) in
            if error != nil {
                callback(nil,error)
            } else {
                self.sharedBasket!.updateBasketAndNotify(basket!)
                BasketService.sharedBasket?.deliveryMode = deliveryMode
                BasketService.sharedBasket?.selectedPickupTime = nil
                callback(nil, error)
                //                AnalyticsService.trackEvent("order_ahead", action: "create_basket")
            }
        }
    }
    
}
