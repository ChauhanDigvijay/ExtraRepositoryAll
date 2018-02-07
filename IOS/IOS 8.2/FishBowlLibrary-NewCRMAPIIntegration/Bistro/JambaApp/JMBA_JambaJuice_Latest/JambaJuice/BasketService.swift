//
//  BasketService.swift
//  JambaJuice
//
//  Created by Taha Samad on 27/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK
import UIKit

typealias BasketCallback = (basket: Basket?, error: NSError?) -> Void
typealias BasketErrorCallback = (error: NSError?) -> Void
typealias PlaceOrderCallback = (orderStatus: OrderStatus?, error: NSError?) -> Void
typealias BasketRewardsCallback = (rewards: [Reward], error: NSError?) -> Void
//typealias DeleteBasketCallback = (status: NSString) -> Void

class BasketService: NSObject {
    
    /// Current basket
    private(set) static var sharedBasket: Basket? {
        didSet {
            NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.SharedBasketUpdated.rawValue, object: self)
        }
    }
    
    static var lastOrderStatus: OrderStatus?

    /// Create new basket for a given Olo restaurant
    class func createBasket(store: Store, callback: BasketCallback) {
        if store.restaurantId == nil {
            callback(basket: nil, error: NSError(description: "Restaurant does not support Order Ahead"))
            return
        }
        
        // Create new basket for the selected restaurant
        OloBasketService.createBasket(store.restaurantId!) { (oloBasket, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            // Add reference to store in new basket
            self.sharedBasket = Basket(store: store, oloBasket: oloBasket!)
            callback(basket: self.sharedBasket, error: nil)
            AnalyticsService.trackEvent("order_ahead", action: "create_basket")
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("CREATE_BASKET");
            }

        }
    }

    class func itemsInBasket() -> Int64 {
        if sharedBasket == nil {
            return 0
        }
        var existingQuantity: Int64 = 0
        for product in sharedBasket!.products {
            existingQuantity += product.quantity
        }
        return existingQuantity
    }
    
    /// Add a given Product to the existing basket
    class func addProduct(product: Product, quantity: Int64, options: [String], specialInstructions: String?, callback: BasketCallback) {
        if sharedBasket == nil {
            callback(basket: nil, error: NSError(description: "Basket not found."))
            return
        }
        
        let existingQuantity: Int64 = itemsInBasket()
        
        if existingQuantity + quantity > 10 {
            callback(basket: nil, error: NSError(description: "Can not add more than 10 items in your cart per order."))
            return
        }

        if product.storeMenuProduct == nil {
            callback(basket: nil, error: NSError(description: "Product not available at selected store."))
            return
        }
        
        var newBasketProduct = OloNewBasketProduct()
        newBasketProduct.productId = product.storeMenuProduct!.productId
        newBasketProduct.quantity = quantity
        newBasketProduct.options = options.joinWithSeparator(",")
        newBasketProduct.specialInstructions = specialInstructions
        
        OloBasketService.addProduct(sharedBasket!.id, newBasketProduct: newBasketProduct) { (oloBasket, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(basket: self.sharedBasket, error: nil)
            AnalyticsService.trackEvent("order_ahead", action: "add_product", label: product.name, value: Int(quantity))
            if newBasketProduct.specialInstructions != nil && newBasketProduct.specialInstructions!.isEmpty == false {
                AnalyticsService.trackEvent("order_ahead", action: "special_instructions", label: newBasketProduct.specialInstructions!)
            }
            for option in options {
                AnalyticsService.trackEvent("order_ahead", action: "add_boost", label: option)
            }
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            
            if(arrBoostCount.count>0)
            {
                productName = arrBoostCount.componentsJoinedByString(",")
                productID = nil;
                isAppEvent = true
                clpAnalyticsService.sharedInstance.clpTrackScreenView("ADD_BOOST");
                arrBoostCount.removeAllObjects()
            }
            }
        }
        
            }
    /// Remove a product from the existing basket
    class func removeProduct(basketProduct: BasketProduct, callback: BasketCallback) {
        if sharedBasket == nil {
            callback(basket: nil, error: NSError(description: "Basket not found."))
            return
        }

        OloBasketService.removeProduct(sharedBasket!.id, basketProductId: basketProduct.id) { (oloBasket, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(basket: self.sharedBasket, error: nil)
            AnalyticsService.trackEvent("order_ahead", action: "remove_product", label: basketProduct.name, value: Int(basketProduct.quantity))
            for option in basketProduct.choices {
                AnalyticsService.trackEvent("order_ahead", action: "remove_boost", label: "\(option.optionId)")
            }
            
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            if(arrBoostCount.count>0)
            {
                productName = arrBoostCount.componentsJoinedByString(",")
                productID = nil;
                isAppEvent = true
                clpAnalyticsService.sharedInstance.clpTrackScreenView("REMOVE_BOOST");
                arrBoostCount.removeAllObjects()
            }
            }
        }
    }

    class func updateTimeWanted(callback: BasketCallback) {
        if sharedBasket == nil {
            callback(basket: nil, error: NSError(description: "Basket not found."))
            return
        }
        
        // If Time Wanted is not set, remove any previously times set in the server
        if sharedBasket!.timeWanted == nil {
            OloBasketService.deleteTimeWanted(sharedBasket!.id) { (oloBasket, error) -> Void in
                if error != nil {
                    callback(basket: nil, error: error)
                    return
                }
                self.sharedBasket!.updateBasketAndNotify(oloBasket!)
                callback(basket: self.sharedBasket, error: nil)
                AnalyticsService.trackEvent("order_ahead", action: "pickup_time", label: "ASAP")
                pickTime = "ASAP"
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
                callback(basket: nil, error: error)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(basket: self.sharedBasket, error: nil)

            let day = date.isTodayInGregorianCalendar() ? "today" : "tomorrow"
            let time = String(format: "%02d:%02d", hour, min)
            AnalyticsService.trackEvent("order_ahead", action: "pickup_time", label: "\(day)::\(time)")
            pickTime = "\(day)::\(time)"
        }
    }
    
    class func validateBasket(callback: BasketCallback) {
        if sharedBasket == nil {
            callback(basket: nil, error: NSError(description: "Basket not found."))
            return
        }
        OloBasketService.validate(sharedBasket!.id) { (result, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            callback(basket: self.sharedBasket, error: nil)
        }
    }
    
    class func updateTimeWantedAndValidate(callback: BasketErrorCallback) {
        updateTimeWanted { (basket, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            self.validateBasket { (basket, error) -> Void in
                callback(error: error)
            }
        }
    }

    class func placeOrderWithCreditCard(cardNumber: String, securityCode: String, zip: String, expiryMonth: String, expiryYear: String, savePaymentInfo: Bool, callback: PlaceOrderCallback) {
        if sharedBasket == nil {
            callback(orderStatus: nil, error: NSError(description: "Basket not found."))
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

    class func placeOrderWithBillingAccount(billingAccount: BillingAccount, callback: PlaceOrderCallback) {
        if sharedBasket == nil {
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
        submitBody.billingAccountId = "\(billingAccount.accountId)"
        placeOrderInternal(submitBody, callback: callback)
    }
    
    private class func placeOrderInternal(submitBody: OloBasketSubmitBody, callback: PlaceOrderCallback) {
        validateBasket { (basket, error) -> Void in
            if error != nil {
                callback(orderStatus: nil, error: error)
                return
            }
            OloBasketService.submit(self.sharedBasket!.id, basketSubmitBody: submitBody) { (status, error) -> Void in
                if error != nil {
                    callback(orderStatus: nil, error: error)
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
                    SettingsManager.setSetting("first_purchase", value: true)
                }

                // Audit log
                AuditService.trackOrder(self.sharedBasket!, orderStatus: orderStatus)

//                self.sharedBasket = nil
                self.deleteAndCreateNewBasket(nil)
                callback(orderStatus: orderStatus, error: nil)
            }
        }
    }
    
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
    class func applyPromotionCode(promotionCode: String, callback: BasketCallback) {
        if sharedBasket == nil {
            callback(basket: nil, error: NSError(description: "Basket not found."))
            return
        }
        
        // Verify if basket has already a reward, if so, remove it and try again
        if sharedBasket!.appliedRewards.count > 0 {
            removeRewards { (basket, error) -> Void in
                if error != nil {
                    callback(basket: nil, error: error)
                    return
                }
                self.applyPromotionCode(promotionCode, callback: callback)
            }
            return
        }
        
        // Verify if basket has a promotion applied, if so, remove it and try again
        if sharedBasket!.promotionCode != nil {
            removePromotionCode { (basket, error) -> Void in
                if error != nil {
                    callback(basket: nil, error: error)
                    return
                }
                self.applyPromotionCode(promotionCode, callback: callback)
            }
            return
        }

        // Apply promotion code
        OloBasketService.applyCoupon(sharedBasket!.id, couponCode: promotionCode) { (oloBasket, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            //Set promotion code before notif is fired
            self.sharedBasket!.promotionCode = promotionCode
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(basket: self.sharedBasket, error: nil)
            AnalyticsService.trackEvent("order_ahead", action: "apply_coupon", label: promotionCode)
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("APPLY_COUPAN");
            }

        }
    }
    
    /// Remove applied prmotion code
    class func removePromotionCode(callback: BasketCallback) {
        if sharedBasket?.promotionCode == nil {
            // If no promotion was applied, return no error
            callback(basket: sharedBasket, error: nil)
            return
        }
        
        OloBasketService.deleteCoupon(sharedBasket!.id) { (oloBasket, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            //Set promotion code before notif is fired
            let promotionCode = self.sharedBasket!.promotionCode
            self.sharedBasket!.promotionCode = nil
            self.sharedBasket!.updateBasketAndNotify(oloBasket!)
            callback(basket: self.sharedBasket, error: nil)
            AnalyticsService.trackEvent("order_ahead", action: "remove_coupon", label: promotionCode)
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("REMOVE_COUPON");
            }

        }
    }
    
    /// Retrieve available rewards for the current basket / restaurant
    /// We need to ensure loyalty accounts are linked, which we do by posting the phonenumber to loyaltyschemes
    ///  1. Get loyatly schemes, if membership is null, post phone number and get loyalty schemes
    ///  2. Get rewards available for that membership id
    class func availableRewards(callback: BasketRewardsCallback) {
        let basketId = sharedBasket?.id
        if basketId == nil {
            callback(rewards: [], error: NSError(description: "Basket not found."))
            return
        }

        let userPhone = UserService.sharedUser?.phoneNumber
        if userPhone == nil {
            callback(rewards: [], error: NSError(description: "User phone number not found"))
            return
        }
        
        OloBasketService.loyaltySchemes(basketId!) { (loyaltySchemes, error) -> Void in
            if error != nil {
                callback(rewards: [], error: error)
                return
            }
            
            // Get Jamba Juice scheme
            let jambaJuiceScheme = loyaltySchemes.filter{ $0.name == "Jamba Insider Rewards" }.first
            if jambaJuiceScheme == nil {
                callback(rewards: [], error: nil)  // No rewards available for the user, might be an error
                return
            }

            // Check if user is member yet, if not, make it a member and try again
            let membershipId = jambaJuiceScheme?.membership?.id
            if membershipId == nil {
                OloBasketService.configureLoyaltySchemes(basketId!, schemeId: jambaJuiceScheme!.id, phoneNumber: userPhone!) { (error) -> Void in
                    if error != nil {
                        callback(rewards: [], error: error)
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
                    callback(rewards: [], error: error)
                    return
                }
                let rewards = loyaltyRewards.map { Reward(oloReward: $0) }
                callback(rewards: rewards, error: nil)
            }
        }
    }

    /// Apply new reward to basket
    /// If rewards or promotions were already applied, remove them
    class func applyReward(reward: Reward, callback: BasketCallback) {
        if sharedBasket == nil {
            callback(basket: nil, error: NSError(description: "Basket not found."))
            return
        }
        if reward.reference == nil {
            callback(basket: nil, error: NSError(description: "Missing reward reference"))
            return
        }
        if reward.membershipId == nil {
            callback(basket: nil, error: NSError(description: "Missing reward membership Id"))
            return
        }
        
        // Verify if basket has already a reward, if so, remove it and try again
        if sharedBasket!.appliedRewards.count > 0 {
            removeRewards { (basket, error) -> Void in
                if error != nil {
                    callback(basket: nil, error: error)
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
                    callback(basket: nil, error: error)
                    return
                }
                self.applyReward(reward, callback: callback)
            }
            return
        }
        
        // Add reward to basket
        OloBasketService.applyReward(sharedBasket!.id, membershipId: reward.membershipId!, rewardReference: reward.reference!) { (basket, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(basket!)
            callback(basket: self.sharedBasket, error: nil)
            AnalyticsService.trackEvent("order_ahead", action: "apply_reward", label: reward.name)
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("APPLY_REWARD");
            }

        }
    }
    
    /// Remove any applied rewards
    class func removeRewards(callback: BasketCallback) {
        let reward = sharedBasket?.appliedRewards.first
        if reward == nil {
            // If no rewards available, no error
            callback(basket: self.sharedBasket, error: nil)
            return
        }
        if reward!.rewardId == nil {
            callback(basket: nil, error: NSError(description: "Missing reward Id"))
            return
        }
        OloBasketService.removeReward(sharedBasket!.id, rewardId: reward!.rewardId!) { (basket, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            // Log before removing next, if any
            AnalyticsService.trackEvent("order_ahead", action: "remove_reward", label: reward!.name)
            
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("REMOVE_REWARD");
            }

            if basket!.appliedRewards.count > 0 {
                self.removeRewards(callback)
                return
            }
            self.sharedBasket!.updateBasketAndNotify(basket!)
            callback(basket: self.sharedBasket, error: nil)
        }
    }
    
    class func billingSchemes(callback: BasketCallback) {
        if sharedBasket == nil {
            callback(basket: nil, error: NSError(description: "Basket not found."))
            return
        }
        OloBasketService.billingSchemes(sharedBasket!.id) { (oloBillingSchemes, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            self.sharedBasket!.billingSchemes = oloBillingSchemes.map { oloBillingScheme in BillingScheme(oloBillingScheme: oloBillingScheme) }
            callback(basket: self.sharedBasket, error: nil)
        }
    }
    
    class func orderAgain(orderStatus: OrderStatus, callback: BasketCallback) {
        // See if we can find a restaurant for this store
        if orderStatus.store == nil {
            StoreService.storeByStoreCode(orderStatus.vendorExtRef) { (stores, location, error) -> Void in
                if error != nil {
                    callback(basket: nil, error: error)
                    return
                }
                if stores.count == 0 {
                    callback(basket: nil, error: NSError(description: "Unable to retrieve store information"))
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
    
    private class func orderAgainUsingStore(store: Store, orderStatus: OrderStatus, callback: BasketCallback) {
        StoreService.menuForStore(store) { (storeMenu, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            if storeMenu == nil {
                callback(basket: nil, error: NSError(description: "Failed to retrieve store menu"))
                return
            }
            OloBasketService.createFromOrderStatusId(orderStatus.id) { (oloBasket, error) -> Void in
                if error != nil {
                    callback(basket: nil, error: error)
                    return
                }
                // Add reference to store in new basket
                self.sharedBasket = Basket(store: store, oloBasket: oloBasket!)
                // Since we have basket, we can now safely store the menu
                store.storeMenu = storeMenu!
                NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.OrderStarted.rawValue, object: self)
                callback(basket: self.sharedBasket, error: nil)
                AnalyticsService.trackEvent("order_ahead", action: "order_again", label: orderStatus.orderRef)
                if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                {
                    clpAnalyticsService.sharedInstance.clpTrackScreenView("ORDER_AGAIN");
                }

            }
        }
    }
    
    class func deleteBasket() {
        sharedBasket = nil
        AnalyticsService.trackEvent("order_ahead", action: "delete_basket")
        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
        {
        clpAnalyticsService.sharedInstance.clpTrackScreenView("BASKET_DELETE");
        }
    }
    
    // delete and create new basket based on current choosen store. if current store not available then delete only basket.
    class func deleteAndCreateNewBasket(completion: ((status: Bool) -> Void)?) {
        //self.deleteBasket()
        let store = CurrentStoreService.sharedInstance.currentStore;
        if(store != nil){
            self.createBasket(CurrentStoreService.sharedInstance.currentStore!) { (basket, error) -> Void in
                if(basket == nil || error != nil){
                    if(completion != nil){
                        completion!(status: false)
                        // create basket failed
                    }
                }else{
                    if(completion != nil){
                        completion!(status: true)
                        // create basket success
                    }
                }
            }
        }
    }
    
    // Create basket for current store
    class func createBasketForCurrentStore(store: Store!,oloBasket:OloBasket){
        self.sharedBasket = Basket(store: store,oloBasket: oloBasket)
    }
}
