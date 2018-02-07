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
    var thumbImageUrl: String
    var orderImageUrl: String
    var featured: Bool
    
    // For Recently Ordered Product
    var lastOrderedTime: NSDate?
    
    // Products mapped to StoreMenuProduct
    // Will be nil if there is no basket, store has no menu or menu does not contain the product
    var storeMenuProduct: StoreMenuProduct? {
        get {
            let storeMenu = BasketService.sharedBasket?.store.storeMenu
            return storeMenu?[chainProductId]
        }
    }
    
    init(parseObject: PFObject) {
        chainProductId = (parseObject["productId"] as! NSNumber).longLongValue
        name           = parseObject["name"] as! String
        ingredients    = (parseObject["ingredients"] as? String ?? "").stringByUnescapingXML()
        imageUrl       = parseObject["imageUrl"] as? String ?? ""
        thumbImageUrl  = parseObject["thumbImageUrl"] as? String ?? ""
        orderImageUrl  = parseObject["orderImageUrl"] as? String ?? ""
        featured       = parseObject["featured"] as? Bool ?? false
    }
    
}

// MARK: Equatable

func == (lhs: Product, rhs: Product) -> Bool {
    return lhs.chainProductId == rhs.chainProductId
}
