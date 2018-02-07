//
//  Basket.swift
//  JambaJuice
//
//  Created by Taha Samad on 27/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct Basket {

    // These default values are just there so
    // that we can use update method for proper population.
    var basketId: String = ""
//    var timeWanted: String?
//    var earliestReadyTime: String = ""
    var subTotal: Double = 0
    var salesTax: Double = 0
    var total: Double = 0
    var tip: Double = 0
    var discount: Double = 0
    var couponDiscount: Double = 0
    var restaurantId: Int64 = 0
    var products: [BasketProduct] = []
    var appliedRewards: [Reward] = []

    init(oloBasket: OloBasket) {
        basketId          = oloBasket.id
//        timeWanted        = oloBasket.timeWanted
//        earliestReadyTime = oloBasket.earliestReadyTime
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

}
