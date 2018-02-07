//
//  Product.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/12/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

typealias ProductList = [Product]

struct Product: Equatable {

    var chainProductId: Int64 // Maps to OloProduct.chainProductId
    var name: String
    var ingredients: String
    var imageUrl: String

    // Products mapped to StoreMenuProduct
    // Will be nil if there is no basket, store has no menu or menu does not contain the product
    var storeMenuProduct: StoreMenuProduct? {
        get {
            let storeMenu = StoreService.sharedInstance.currentStore?.storeMenu
            return storeMenu?[chainProductId]
        }
    }

    init(parseObject: PFObject) {
        chainProductId = (parseObject["productId"] as? NSNumber)?.longLongValue ?? 0
        name           = parseObject["name"] as? String ?? ""
        ingredients    = (parseObject["ingredients"] as? String ?? "").stringByUnescapingXML()
        imageUrl       = parseObject["kioskImageUrl"] as? String ?? ""
    }

}

// MARK: Equatable

func == (lhs: Product, rhs: Product) -> Bool {
    return lhs.chainProductId == rhs.chainProductId
}
