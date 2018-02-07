//
//  ProductWithCategory.swift
//  JambaJuice
//
//  Created by VT010 on 10/17/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

typealias SearchResultProducts = [ProductWithCategory]

class ProductWithCategory{
    var product:Product
    var category:ProductCategory
    
    init(product:Product, category:ProductCategory){
        self.product = product
        self.category = category
    }
}
