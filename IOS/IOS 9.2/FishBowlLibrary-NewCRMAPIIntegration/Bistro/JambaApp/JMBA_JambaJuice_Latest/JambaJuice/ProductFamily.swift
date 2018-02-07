//
//  ProductFamily.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/17/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

typealias ProductFamilyList = [ProductFamily]

struct ProductFamily: Equatable {

    var id: String
    var name: String
    var imageUrl: String
    var categories: ProductCategoryList
    
    init(parseObject: PFObject) {
        id = parseObject.objectId!
        name = parseObject["name"] as! String
        imageUrl = parseObject["imageUrl"] as? String ?? ""
        categories = []
    }

}

func == (lhs: ProductFamily, rhs: ProductFamily) -> Bool {
    return lhs.name == rhs.name
}
