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
typealias ProductListAndAdListCallback = (ProductList,AdProductsList, NSError?) -> Void

class ProductService: NSObject {
    
    // Featured products from local cache
    class func featuredProducts(_ callback: @escaping ProductListCallback) {
        UIApplication.inBackground {
            loadProductsIfNeeded()
            let query = PFQuery(className: productEntityName)
            query.whereKey("featured", equalTo: true)
            query.order(byAscending: "order")
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
    class func productFamilies(_ callback: @escaping ProductMenuCallback) {
        UIApplication.inBackground {
            loadProductsIfNeeded()
            let query = PFQuery(className: productFamilyEntityName)
            query.order(byAscending: "order")
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
    class func productsForCategory(_ category: ProductCategory, callback: @escaping ProductListCallback) {
        UIApplication.inBackground {
            let query = PFQuery(className: productEntityName)
            query.whereKey("category", equalTo: PFObject(withoutDataWithClassName: productCategoryEntityName, objectId: category.id))
            query.order(byAscending: "order")
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

    class func recentlyOrderedProducts(_ callback: @escaping ProductListCallback) {
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
            let _ = PFObject.unpinAllObjectsInBackground(withName:RecentlyOrderedProduct.parseClassName) // Ignore if it fails
            UIApplication.inMainThread {
                NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.RecentlyOrderedProductsUpdated.rawValue), object: self)
            }
        }
    }

    class func updateRecentlyOrderedProducts(_ basket: Basket) {
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
                recentProducts.append(contentsOf: chainProductIdsFromBasket.map { RecentlyOrderedProduct(chainProductId: $0, lastOrderedTime: NSDate() as Date) } )
               
                // Sort by most recent first and truncate if needed
                recentProducts.sort { $0.lastOrderedTime.compare($1.lastOrderedTime) == .orderedDescending }
                if recentProducts.count > Constants.recentlyOrderedProductCount {
                    recentProducts = Array(recentProducts[0..<Constants.recentlyOrderedProductCount])
                }

                // Remove old recent products and save new list
                let _ = PFObject.unpinAllObjectsInBackground(withName:RecentlyOrderedProduct.parseClassName) // Ignore if it fails
                try PFObject.pinAll(recentProducts.map { $0.serializeAsParseObject() }, withName: RecentlyOrderedProduct.parseClassName)

                UIApplication.inMainThread {
                    NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.RecentlyOrderedProductsUpdated.rawValue), object: self)
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
    fileprivate class func categoriesForFamily(_ family: ProductFamily) -> ProductCategoryList {
        let query = PFQuery(className: productCategoryEntityName)
        query.whereKey("family", equalTo: PFObject(withoutDataWithClassName: productFamilyEntityName, objectId: family.id))
        query.order(byAscending: "order")
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
    fileprivate class func loadProductsIfNeeded() {
        // Check if it is time to download products and save to local data store
        if let lastPullDate = SettingsManager.setting(.LastProductUpdate) as? Date {
            if abs(lastPullDate.timeIntervalSinceNow) < Constants.parseRemoteSyncPeriod {
                return
            }
        }

        // Load products from remote
        loadProductsFromRemote()
        updateAds()
    }

    // Download products from Parse.com and store in Parse local datastore
    // Sync method: Call from background thread
    fileprivate class func loadProductsFromRemote() {
        do {
            log.verbose("Downloading products from Parse.com ...")
            let query = PFQuery(className: productEntityName)
            query.whereKey("published", equalTo: true)
            query.includeKey("category")
            query.includeKey("family")
            let products = try query.findObjects()
            let _ = try? PFObject.unpinAllObjects(withName: productEntityName) // Ignore if it fails
            log.verbose("Downloaded \(products.count) products from Parse.com")
            try PFObject.pinAll(products, withName: productEntityName)
            SettingsManager.setSetting(.LastProductUpdate, value: Date() as AnyObject)
        } catch {
            log.error("Failed to load products from remote")
        }
    }

    // Sync method: Call from background thread
    fileprivate class func recentlyOrderedProductsInternal() throws -> RecentlyOrderedProductList {
        let query = PFQuery(className: RecentlyOrderedProduct.parseClassName)
        query.fromLocalDatastore()
        query.order(byDescending: RecentlyOrderedProduct.parseOrderKey)
        query.limit = RecentlyOrderedProduct.recentOrderLimit
        
        let parseRecentlyOrderedProducts = try query.findObjects()
        let recentlyOrderedProducts = parseRecentlyOrderedProducts.map { RecentlyOrderedProduct(parseObject: $0) }
        return recentlyOrderedProducts
    }

    // Sync method: Call from background thread
    fileprivate class func productWithChainProductIds(_ chainProductIds: [Int64]) throws -> ProductList {
        return try allProducts().filter { chainProductIds.contains($0.chainProductId) }
    }

    // Sync method: Call from background thread
    fileprivate class func allProducts() throws -> ProductList {
        let query = PFQuery(className: productEntityName)
        query.fromLocalDatastore()
        let parseProducts = try query.findObjects()
        return parseProducts.map { Product(parseObject: $0) }
    }
    
    //get a product detail by product Id
    class func getParticularProduct(_ productId: Int64) -> ProductList {
        let query = PFQuery(className: productEntityName)
        query.fromLocalDatastore()
        if let parseProducts = try? query.findObjects() {
            let productArray = parseProducts.map { Product(parseObject: $0) }
            return productArray.filter{ $0.chainProductId == productId }
        }
        return []
    }
    
    //download ads from the parse server
    class func updateAds() {
        UIApplication.inBackground {
            do {
                //update Addetail from the parse
                var query = PFQuery(className: AdDetailClass.parseClassName)
                query.includeKey(AdClass.parseClassName)
                var result = try query.findObjects()
                let _ = PFObject.unpinAllObjectsInBackground(withName:AdDetailClass.parseClassName) // Ignore if it fails
                let _ = PFObject.pinAll(inBackground: result, withName: AdDetailClass.parseClassName)
                //update Ad from the parse
                query = PFQuery(className: AdClass.parseClassName)
                result = try query.findObjects()
                let _ = PFObject.unpinAllObjectsInBackground(withName:AdClass.parseClassName) // Ignore if it fails
                let _ = PFObject.pinAll(inBackground:result, withName: AdClass.parseClassName)
            } catch {
                log.error("Failed to download Ads")
            }
        }
    }
    
    //check what is the ads & feature products available
    class func recentlyOrderedProductsAndAds(_ callback: @escaping ProductListAndAdListCallback) {
        UIApplication.inBackground {
            do {
                let recentlyOrderedProducts = try self.recentlyOrderedProductsInternal()
                if recentlyOrderedProducts.count == 0 {
                    //get Ads
                    var adslist:AdProductsList = AdClass.init()
                    let query = PFQuery(className: AdClass.parseClassName)
                    query.fromLocalDatastore()
                    if let result = try? query.findObjects() {
                        //check the result is exist or not
                        if result.count > 0 {
                            adslist =  AdClass(parseObject: result[0])
                            //Combine all AdDetail array to single array and place it in Adclass
                            let ads:[AdDetailList] = result.map({ object in
                                let ad = AdClass(parseObject: object)
                                let ads = self.adDetailsForAd(ad)
                                return ads
                            })
                            adslist.adsDetailList = ads.reduce([], +)
                        }
                    }
                    UIApplication.inMainThread {
                        callback([], adslist, nil)
                    }
                    return
                }
                
                //map chainproductid with the products
                let chainProductIds = recentlyOrderedProducts.map { $0.chainProductId }
                var productsList = try self.productWithChainProductIds(chainProductIds)
                productsList = productsList.filter { chainProductIds.contains($0.chainProductId) }
                //check the recent order comes in Descending order
                var products:ProductList = []
                for recentProduct in recentlyOrderedProducts {
                    for product in productsList {
                        if product.chainProductId == recentProduct.chainProductId {
                            products.append(product)
                            break
                        }
                    }
                }
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
                
                //get Ads
                var adslist:AdProductsList = AdClass.init()
                let query = PFQuery(className: AdClass.parseClassName)
                query.fromLocalDatastore()
                if let result = try? query.findObjects() {
                    if result.count > 0 {
                        adslist =  AdClass(parseObject: result[0])
                        let ads:[AdDetailList] = result.map({ object in
                            let ad = AdClass(parseObject: object)
                            let ads = self.adDetailsForAd(ad)
                            return ads
                        })
                        adslist.adsDetailList = ads.reduce([], +)
                    }
                }
                UIApplication.inMainThread {
                    callback(products, adslist, nil)
                }
            } catch {
                UIApplication.inMainThread {
                    callback([], AdClass.init(), nil) // Treat as no recent products
                }
            }
        }
    }
    
    //map AdDetail with the AdClass
    fileprivate class func adDetailsForAd(_ ad: AdClass) -> AdDetailList {
        let query = PFQuery(className: AdDetailClass.parseClassName)
        query.whereKey(AdClass.parseClassName, equalTo: PFObject(withoutDataWithClassName: AdClass.parseClassName, objectId: ad.id))
        query.whereKey(AdDetailClass.parseStatusKey, equalTo: true)
        query.order(byAscending: AdDetailClass.parseOrderKey)
        query.fromLocalDatastore()
        do {
            let objects = try query.findObjects()
            let adDetails:AdDetailList = objects.map { AdDetailClass(parseObject: $0) }
            return adDetails
        } catch {
            return []
        }
    }

}
