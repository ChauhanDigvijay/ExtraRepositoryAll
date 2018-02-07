//
//  OrderStatusProduct.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/24/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct OrderStatusProduct {
    
    var name: String
    var quantity: Int
    var totalCost: Double
    var specialInstructions: String
    var choices:[OloBasketChoice]
    
    init(oloOrderStatusProduct: OloOrderStatusProduct) {
        name                = oloOrderStatusProduct.name.capitalized
        quantity            = oloOrderStatusProduct.quantity
        totalCost           = oloOrderStatusProduct.totalCost
        specialInstructions = oloOrderStatusProduct.specialInstructions
        choices             = oloOrderStatusProduct.choices
    }
    
    init(oloBasket: OloBasketProduct) {
        name                = oloBasket.name.capitalized
        quantity            = oloBasket.quantity
        totalCost           = oloBasket.totalCost
        specialInstructions = oloBasket.specialInstructions
        choices             = oloBasket.choices
    }

}
