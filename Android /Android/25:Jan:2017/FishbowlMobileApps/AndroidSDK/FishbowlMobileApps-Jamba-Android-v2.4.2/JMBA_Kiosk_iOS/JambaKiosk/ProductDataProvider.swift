//
//  ProductDataProvider.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/11/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

class ProductDataProvider {

    static var productTree: ProductFamilyList = []

    static var productCategories: ProductCategoryList {
        return productTree.map { $0.categories }.flatMap { $0 }
    }

    static var allProducts: ProductList {
        return productCategories.map { $0.products }.flatMap { $0 }
    }

    class func loadProductsForStore(store: Store, complete: Void -> Void) {
        UIApplication.inBackground {
            guard let menu = store.storeMenu else {
                fatalError("Store menu not loaded")
            }
            let chainProductIds = Array(menu.keys)
            productTree = ProductService.loadProductTree(chainProductIds)
            UIApplication.inMainThread {
                complete()
            }
        }
    }

}
