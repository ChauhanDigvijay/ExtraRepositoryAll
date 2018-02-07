//
//  ProductCategory.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/17/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

typealias ProductCategoryList = [ProductCategory]

struct ProductCategory: Equatable {

    var productCategoryId: String
    var name: String
    var tagline: String?
    var desc: String?
    var imageUrl: String
    var products: ProductList

    init(parseObject: PFObject) {
        productCategoryId = parseObject.objectId!
        name = parseObject["name"] as? String ?? ""
        tagline = parseObject["tagLine"] as? String
        desc = parseObject["desc"] as? String
        imageUrl = parseObject["imageUrl"] as? String ?? ""
        products = []
    }

}


// MARK: Equatable

func == (lhs: ProductCategory, rhs: ProductCategory) -> Bool {
    return lhs.name == rhs.name
}
