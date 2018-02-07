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
    var products: [BasketProduct] = []
    var appliedRewards: [Reward] = []

    // Basket must be always associated to a store
    var store: Store
    
    // Promotion code applied to basket
    var promotionCode: String?
    
    var offerId: String?

    // Selected Pick up Time
    var selectedPickupTime: NSDate?

    // Billing Schemes
    var billingSchemes: [BillingScheme]?
    
    var selectedCustomiseOptionIdsTrack = Set<Int64>()

    init(store: Store, oloBasket: OloBasket) {
        self.store = store
        updateBasket(oloBasket)
    }
    
    private func updateBasket(oloBasket: OloBasket) {
        id                = oloBasket.id
        timeWanted        = oloBasket.timeWanted
        earliestReadyTime = oloBasket.earliestReadyTime
        subTotal          = oloBasket.subTotal
        salesTax          = oloBasket.salesTax
        total             = oloBasket.total
        tip               = oloBasket.tip
        discount          = oloBasket.discount
        couponDiscount    = oloBasket.couponDiscount
        restaurantId      = oloBasket.vendorId
        products          = oloBasket.products.map { BasketProduct(oloBasketProduct: $0) }
        appliedRewards    = oloBasket.appliedRewards.map { Reward(oloReward: $0) }
    }
    
    func updateBasketAndNotify(oloBasket: OloBasket) {
        updateBasket(oloBasket)
        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.SharedBasketUpdated.rawValue, object: self)
    }

}
