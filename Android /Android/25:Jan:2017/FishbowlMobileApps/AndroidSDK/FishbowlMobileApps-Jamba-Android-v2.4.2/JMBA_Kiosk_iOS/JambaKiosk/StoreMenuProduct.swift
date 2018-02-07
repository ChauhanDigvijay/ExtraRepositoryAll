//
//  StoreMenuProduct.swift
//  JambaJuice
//
//  Created by Taha Samad on 02/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

/// Dictionary of StoreMenuProduct by Product.chainProductId key
typealias StoreMenu = [Int64: StoreMenuProduct]

/// Holds state for product modifiers being loaded
class StoreMenuProduct {

    var productId: Int64       // Olo Product Id (used on basket)
    var chainProductId: Int64  // Jamba Juice product Id from global product menu
    var cost: Double
    //
    var productTypeAndSizeTopLevelModifier: StoreMenuProductModifier?
    var productModifiers: [StoreMenuProductModifier] = []
    var hasPopulatedModifiers = false

    init(productId: Int64, chainProductId: Int64, cost: Double) {
        self.productId = productId
        self.chainProductId = chainProductId
        self.cost = cost
    }

    func setModifiers(storeMenuModifiers: [StoreMenuProductModifier]) {
        hasPopulatedModifiers = true
        productTypeAndSizeTopLevelModifier = nil
        productModifiers = []

        for storeMenuModifier in storeMenuModifiers {
            if storeMenuModifier.isTopLevelTypeOrSizeModifier() {
                productTypeAndSizeTopLevelModifier = storeMenuModifier
            } else {
                let unnesterModifiers = storeMenuModifier.returnModifiersByRemovingNeedlessNesting()
                productModifiers += unnesterModifiers
            }
        }
    }

}
