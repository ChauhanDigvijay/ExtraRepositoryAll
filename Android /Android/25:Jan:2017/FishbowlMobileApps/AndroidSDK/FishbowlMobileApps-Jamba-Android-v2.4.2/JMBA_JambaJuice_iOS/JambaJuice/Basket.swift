//
//  Basket.swift
//  JambaJuice
//
//  Created by Taha Samad on 27/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

/// Basket class holds state about current order started by the user
class Basket {
    
    let defaultDeliveryTime:Double = 45.0   //Default delivery Time
    let intervalTime:Double = 15.0
    let maxNoOfDaysForLater:Int = 7
    // These default values are just there so
    // that we can use update method for proper population.
    var id: String = ""
    var timeWanted: String?
    var earliestReadyTime: String = ""
    var subTotal: Double = 0
    var salesTax: Double = 0
    var total: Double = 0
    var tip: Double = 0
    var discount: Double = 0
    var couponDiscount: Double = 0
    var restaurantId: Int64 = 0
    var deliveryMode: String = ""
    var products: [BasketProduct] = []
    var appliedRewards: [Reward] = []

    // Basket must be always associated to a store
    var store: Store
    
    // Promotion code applied to basket
    var promotionCode: String?
    
    var offerId: String?

    // Selected Pick up Time
    var selectedPickupTime: Date?

    // Billing Schemes
    var billingSchemes: [BillingScheme]?
    
    var selectedCustomiseOptionIdsTrack = Set<Int64>()
    
    // Fishbowl offer applied basket id
    var fbOfferAppliedBasketId: String?
    
    var deliveryAddress: OloDeliveryAddress?
    var contactNumber: String = ""
    var customerhandoffcharge: Double = 0.0
    var leadTimeEstimateMinutes: Double = 0.0
    
    // Update basket for upsell groups
    var upSellGroups:[OloUpSellGroup] = []
    
    // Upselll no thanks option for user
    var noThanks:Bool = false


    init(store: Store, oloBasket: OloBasket) {
        self.store = store
        updateBasket(oloBasket)
    }
    
    fileprivate func updateBasket(_ oloBasket: OloBasket) {
        id                      = oloBasket.id
        timeWanted              = oloBasket.timeWanted
        earliestReadyTime       = oloBasket.earliestReadyTime
        subTotal                = oloBasket.subTotal
        salesTax                = oloBasket.salesTax
        total                   = oloBasket.total
        tip                     = oloBasket.tip
        discount                = oloBasket.discount
        couponDiscount          = oloBasket.couponDiscount
        restaurantId            = oloBasket.vendorId
        products                = oloBasket.products.map { BasketProduct(oloBasketProduct: $0) }
        appliedRewards          = oloBasket.appliedRewards.map { Reward(oloReward: $0) }
        deliveryAddress         = oloBasket.deliveryAddress
        contactNumber           = oloBasket.contactNumber
        customerhandoffcharge   = oloBasket.customerhandoffcharge
        leadTimeEstimateMinutes = oloBasket.leadTimeEstimateMinutes 
        deliveryMode            = oloBasket.deliveryMode
    }
    
    func updateBasketAndNotify(_ oloBasket: OloBasket) {
        // remove promotion code when discount is nil ore discount zero
        if oloBasket.id != BasketService.sharedBasket?.fbOfferAppliedBasketId{
            BasketService.sharedBasket?.promotionCode = nil
            BasketService.sharedBasket?.offerId = nil
        }
        updateBasket(oloBasket)
        NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.SharedBasketUpdated.rawValue), object: self)
    }
    
    // Update basket for upsellGroups
    func updateBasketUpSellGroup(_ basketValidation:OloBasketValidation){
        if basketValidation.basketId == BasketService.sharedBasket?.id{
            BasketService.sharedBasket?.upSellGroups = basketValidation.upSellGroups
        }
    }
    
    // Update no thanks for basket
    func updateNoThanksOption(enable:Bool){
        noThanks = enable
    }
    
}
