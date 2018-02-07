//
//  ProductService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/12/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//


import UIKit
import Parse

private let productEntityName = "Product"
private let productCategoryEntityName = "ProductCategory"
private let productFamilyEntityName = "ProductFamily"

typealias ProductListCallback = (ProductList, NSError?) -> Void
typealias ProductMenuCallback = (ProductFamilyList, NSError?) -> Void

class ProductService: NSObject {
    
    // Featured products from local cache
    class func featuredProducts(callback: ProductListCallback) {
        UIApplication.inBackground {
            loadProductsIfNeeded()
            let query = PFQuery(className: productEntityName)
            query.whereKey("featured", equalTo: true)
            query.orderByAscending("order")
            query.fromLocalDatastore()
            do {
                let products = try query.findObjects()
                UIApplication.inMainThread {
                    callback(products.map { Product(parseObject: $0) }, nil)
                }
            } catch {
                UIApplication.inMainThread {
                    callback([], nil) // Treat as no featured products
                }
            }
        }
    }

    // Load all product families
    class func productFamilies(callback: ProductMenuCallback) {
        UIApplication.inBackground {
            loadProductsIfNeeded()
            let query = PFQuery(className: productFamilyEntityName)
            query.orderByAscending("order")
            query.fromLocalDatastore()
            do {
                let objects = try query.findObjects()
                let families: ProductFamilyList = objects.map { object in
                    var family = ProductFamily(parseObject: object)
                    family.categories = self.categoriesForFamily(family)
                    return family
                }
                UIApplication.inMainThread {
                    callback(families, nil)
                }
            } catch {
                UIApplication.inMainThread {
                    callback([], nil) // Treat as no families
                }
            }
        }
    }
    
    // Load products for a single category
    class func productsForCategory(category: ProductCategory, callback: ProductListCallback) {
        UIApplication.inBackground {
            let query = PFQuery(className: productEntityName)
            query.whereKey("category", equalTo: PFObject(withoutDataWithClassName: productCategoryEntityName, objectId: category.id))
            query.orderByAscending("order")
            query.fromLocalDatastore()
            do {
                let products = try query.findObjects()
                UIApplication.inMainThread {
                    callback(products.map { Product(parseObject: $0) }, nil)
                }
            } catch {
                UIApplication.inMainThread {
                    callback([], nil) // Treat as no featured products
                }
            }
        }
    }

    // Force downloading products from remote
    class func forceLoadProductsInBackground() {
        UIApplication.inBackground {
            loadProductsFromRemote()
        }
    }

    class func recentlyOrderedProducts(callback: ProductListCallback) {
        UIApplication.inBackground {
            do {
                let recentlyOrderedProducts = try self.recentlyOrderedProductsInternal()
                if recentlyOrderedProducts.count == 0 {
                    UIApplication.inMainThread {
                        callback([], nil)
                    }
                    return
                }

                let chainProductIds = recentlyOrderedProducts.map { $0.chainProductId }
                var products = try self.productWithChainProductIds(chainProductIds)
                products = products.filter { chainProductIds.contains($0.chainProductId) }
                
                // check & filter the recent ordered product available in the current store menu
                if let store = CurrentStoreService.sharedInstance.currentStore {
                    if let storeMenu = store.storeMenu  {
                        let chainProductIds = Array(storeMenu.keys)
                        products = products.filter({ (product) -> Bool in
                            return chainProductIds.contains(product.chainProductId)
                        })
                    } else {
                        products = []
                    }
                }else {
                    products = []
                }
                UIApplication.inMainThread {
                    callback(products, nil)
                }
            } catch {
                UIApplication.inMainThread {
                    callback([], nil) // Treat as no recent products
                }
            }
        }
    }

    class func deleteAllRecentlyOrderedProducts() {
        UIApplication.inBackground {
            do {
                try PFObject.unpinAllObjectsWithName(RecentlyOrderedProduct.parseClassName)
            } catch {
                // Ignore
            }
            UIApplication.inMainThread {
                NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.RecentlyOrderedProductsUpdated.rawValue, object: self)
            }
        }
    }

    class func updateRecentlyOrderedProducts(basket: Basket) {
        UIApplication.inBackground {
            do {
                if basket.store.storeMenu == nil {
                    return // Cannot map basket products by chainProductId withtout the store menu
                }
                
                // Create a store product map by Olo Product Id based on the store menu (basically reverse the store menu map)
                let storeProductMap = basket.store.storeMenu!.values.reduce([Int64 : StoreMenuProduct]()) { ( dict, element) in
                    var dict = dict
                    dict[element.productId] = element
                    return dict
                }
                
                // Map basket products into a list of chainProductIds, using the store product map
                let chainProductIdsFromBasket = basket.products.flatMap { storeProductMap[$0.productId]?.chainProductId }

                // Load existing recent products
                // Remove recent products that are included in the basket
                // Append new recent products from basket
                var recentProducts = try self.recentlyOrderedProductsInternal()
                recentProducts = recentProducts.filter { chainProductIdsFromBasket.contains($0.chainProductId) == false }
                recentProducts.appendContentsOf(chainProductIdsFromBasket.map { RecentlyOrderedProduct(chainProductId: $0, lastOrderedTime: NSDate()) } )
               
                // Sort by most recent first and truncate if needed
                recentProducts.sortInPlace { $0.lastOrderedTime.compare($1.lastOrderedTime) == .OrderedDescending }
                if recentProducts.count > Constants.recentlyOrderedProductCount {
                    recentProducts = Array(recentProducts[0..<Constants.recentlyOrderedProductCount])
                }

                // Remove old recent products and save new list
                let _ = try? PFObject.unpinAllObjectsWithName(RecentlyOrderedProduct.parseClassName)
                try PFObject.pinAll(recentProducts.map { $0.serializeAsParseObject() }, withName: RecentlyOrderedProduct.parseClassName)

                UIApplication.inMainThread {
                    NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.RecentlyOrderedProductsUpdated.rawValue, object: self)
                }
            } catch {
                // Failed to save recent products
                log.error("Failed to save recent ordered products")
            }
        }
    }



    // MARK: Private methods

    // Product categories for a family
    // Sync method: Call from background thread
    private class func categoriesForFamily(family: ProductFamily) -> ProductCategoryList {
        let query = PFQuery(className: productCategoryEntityName)
        query.whereKey("family", equalTo: PFObject(withoutDataWithClassName: productFamilyEntityName, objectId: family.id))
        query.orderByAscending("order")
        query.fromLocalDatastore()
        do {
            let objects = try query.findObjects()
            let categories = objects.map { ProductCategory(parseObject: $0) }
            return categories
        } catch {
            return []
        }
    }

    
    // Sync method: Call from background thread
    private class func loadProductsIfNeeded() {
        // Check if it is time to download products and save to local data store
        if let lastPullDate = SettingsManager.setting(.LastProductUpdate) as? NSDate {
            if abs(lastPullDate.timeIntervalSinceNow) < Constants.parseRemoteSyncPeriod {
                return
            }
        }

        // Load products from remote
        loadProductsFromRemote()
    }

    // Download products from Parse.com and store in Parse local datastore
    // Sync method: Call from background thread
    private class func loadProductsFromRemote() {
        do {
            log.verbose("Downloading products from Parse.com ...")
            let query = PFQuery(className: productEntityName)
            query.whereKey("published", equalTo: true)
            query.includeKey("category")
            query.includeKey("family")
            let products = try query.findObjects()
            let _ = try? PFObject.unpinAllObjectsWithName(productEntityName) // Ignore if it fails
            log.verbose("Downloaded \(products.count) products from Parse.com")
            try PFObject.pinAll(products, withName: productEntityName)
            SettingsManager.setSetting(.LastProductUpdate, value: NSDate())
        } catch {
            log.error("Failed to load products from remote")
        }
    }

    // Sync method: Call from background thread
    private class func recentlyOrderedProductsInternal() throws -> RecentlyOrderedProductList {
        let query = PFQuery(className: RecentlyOrderedProduct.parseClassName)
        query.fromLocalDatastore()
        query.orderByDescending(RecentlyOrderedProduct.parseOrderKey)
        
        let parseRecentlyOrderedProducts = try query.findObjects()
        let recentlyOrderedProducts = parseRecentlyOrderedProducts.map { RecentlyOrderedProduct(parseObject: $0) }
        return recentlyOrderedProducts
    }

    // Sync method: Call from background thread
    private class func productWithChainProductIds(chainProductIds: [Int64]) throws -> ProductList {
        return try allProducts().filter { chainProductIds.contains($0.chainProductId) }
    }

    // Sync method: Call from background thread
    private class func allProducts() throws -> ProductList {
        let query = PFQuery(className: productEntityName)
        query.fromLocalDatastore()
        let parseProducts = try query.findObjects()
        return parseProducts.map { Product(parseObject: $0) }
    }

}
