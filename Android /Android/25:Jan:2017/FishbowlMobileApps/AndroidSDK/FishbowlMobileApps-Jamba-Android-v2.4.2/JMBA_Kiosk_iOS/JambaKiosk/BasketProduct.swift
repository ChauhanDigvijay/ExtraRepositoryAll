//
//  BasketProduct.swift
//  JambaJuice
//
//  Created by Taha Samad on 27/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct BasketProduct {

    var basketProductId: Int64
    var productId: Int64
    var name: String
    var quantity: Int
    var baseCost: Double
    var totalCost: Double
    var choices: [BasketChoice]

    init(oloBasketProduct: OloBasketProduct) {
        basketProductId = oloBasketProduct.id
        productId       = oloBasketProduct.productId
        name            = oloBasketProduct.name
        quantity        = oloBasketProduct.quantity
        baseCost        = oloBasketProduct.baseCost
        totalCost       = oloBasketProduct.totalCost
        choices         = oloBasketProduct.choices.map { BasketChoice(oloBasketChoice: $0) }
    }

}
