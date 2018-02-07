//
//  CurrentStoreService.swift
//  JambaJuice
//
//  Created by Sridhar R on 2/16/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import MapKit
import OloSDK
import Parse


private let productEntityName = "Product"
private let productCategoryEntityName = "ProductCategory"
private let productFamilyEntityName = "ProductFamily"


// StartNewOrder Callback
typealias StartNewOrderCallBack =  (status:NSString?,error:NSError?) -> Void

// Menu Callback
typealias StoreMenuCallBack =  (storeMenuBasket:StoreMenuBasket?,error:NSError?) -> Void

// OloRestaurantDetails Callback
typealias OloRestaurantCallaback = (store:Store, error:NSError?) -> Void

class CurrentStoreService{
    
    // Product family list
    private(set) var productTree: ProductFamilyList = []
    
    // Instance shared instance for the class
    static let sharedInstance = CurrentStoreService()
    
    // Current store assign the change store in this current store variable
    private(set) var currentStore: Store?
    
    // Product list array for store based feature products
    private(set) var storeBasedFeatureProducts: ProductList = []
    
    
    // Start new order is used to get menu and create a new basket for given store
    func startNewOrder(store:Store! , callback:StartNewOrderCallBack){

        // Assign store,menu,basket,featured products,produttree attributes in one structure
        var tempStoreMenuBasket = StoreMenuBasket()
        // Get menu for given store
        self.menuForStore(store) { (storeMenuBasket, error) in
            if error != nil{
                return callback(status: "Failure", error: error)
            }
            if storeMenuBasket == nil{
                return callback(status: "Failure", error: NSError.init(description: "Unable to retrieve store menu"))
            }
            
            // Override an tempStoreMenuBasket object with storeMenuBasket object
            tempStoreMenuBasket = storeMenuBasket!
            
            // Create basket for given store
            OloBasketService.createBasket(store.restaurantId!) { (oloBasket, error) -> Void in
                if error != nil {
                    return callback(status: "Failure", error: error)
                }
                
                tempStoreMenuBasket.oloBasket = oloBasket!
                self.currentStoreMenuBasketUpdation(tempStoreMenuBasket)
                return callback(status: "Success" , error: nil)
            }
        }
    }
    
    // Empty the current store and assign the selecte store
    func resetStore(store:Store){
        self.currentStore=store;
        if let user = UserService.sharedUser{
            user.currentStore = store
            UserService.updateCurrentStore()
        }
    }
    
    // Delete currentstore
    func deleteCurrentStore(){
        self.currentStore=nil;
        if let user = UserService.sharedUser{
            user.currentStore = nil
            UserService.updateCurrentStore()
        }
    }
    
    
    class func callAppError()
    {
        // type method implementation goes here
        clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
    }
    
    // Load store based product tree
    func loadProductTree(chainProductIds: [Int64]) -> ProductFamilyList {
        loadProductsIfNeeded()
        return productFamiliesWithCategories(chainProductIds)
    }
    
    // Load products from parse
    func loadProductsIfNeeded() {
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
    func loadProductsFromRemote() {
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
            CurrentStoreService.callAppError()
            
        }
    }
    
    // Get menu families and categories
    func productFamiliesWithCategories(chainProductIds: [Int64]) -> ProductFamilyList {
        let query = PFQuery(className: productFamilyEntityName)
        query.orderByAscending("order")
        query.fromLocalDatastore()
        if let objects = try? query.findObjects() {
            let families: ProductFamilyList = objects.map {
                var family = ProductFamily(parseObject: $0)
                family.categories = self.categoriesForFamilyWithProducts(family, chainProductIds: chainProductIds)
                return family
            }
            return families.filter { $0.categories.count > 0 }
        }
        return []
    }
    
    // Product categories for a family
    func categoriesForFamilyWithProducts(family: ProductFamily, chainProductIds: [Int64]) -> ProductCategoryList {
        let query = PFQuery(className: productCategoryEntityName)
        query.whereKey("family", equalTo: PFObject(withoutDataWithClassName: productFamilyEntityName, objectId: family.id))
        query.orderByAscending("order")
        query.fromLocalDatastore()
        if let objects = try? query.findObjects() {
            let categories: ProductCategoryList = objects.map {
                var category = ProductCategory(parseObject: $0)
                category.products = self.productsForCategory(category, chainProductIds: chainProductIds)
                return category
            }
            return categories.filter { $0.products.count > 0 }
        }
        return []
    }
    
    // Products for a category
    func productsForCategory(category: ProductCategory, chainProductIds: [Int64]) -> ProductList {
        let query = PFQuery(className: productEntityName)
        query.whereKey("category", equalTo: PFObject(withoutDataWithClassName: productCategoryEntityName, objectId: category.id))
        query.orderByAscending("order")
        query.fromLocalDatastore()
        if let objects = try? query.findObjects() {
            let products = objects.map { Product(parseObject: $0) }
            return products.filter { chainProductIds.contains($0.chainProductId) }
        }
        return []
    }
    
    // Get menu for given store
    func menuForStore(store:Store!,callback:StoreMenuCallBack){
        StoreService.menuForStore(store) { (storeMenu, error) -> Void in
            if error != nil  {
                return callback(storeMenuBasket: nil,error: error)
            }
            
            UIApplication.inMainThread({
                ProductService.featuredProducts { (products, error) in
                    if error != nil {
                        CurrentStoreService.callAppError()
                        log.error("Error: no products loaded!")
                        return callback(storeMenuBasket: nil,error: error)
                    }
                    
                    // Assign response of store menu in store object
                    store.storeMenu = storeMenu
                    var storeMenuBasket = StoreMenuBasket()
                    storeMenuBasket.store = store
                    
                    // Construct store based featured products and product tree and assign in storeMenuBasket object
                    let chainProductIds = Array(store.storeMenu!.keys)
                    storeMenuBasket.storeBasedFeatureProducts = products.filter { chainProductIds.contains($0.chainProductId) }
                    storeMenuBasket.productTree = self.loadProductTree(chainProductIds)
                    return callback(storeMenuBasket: storeMenuBasket,error: nil)
                }
            })
        }
    }
    
    // Update currentstore service shared instance by assign storeMenuBasket object
    func currentStoreMenuBasketUpdation(storeMenuBasket:StoreMenuBasket!){
        self.currentStore = storeMenuBasket.store
        self.storeBasedFeatureProducts = storeMenuBasket.storeBasedFeatureProducts
        self.productTree = storeMenuBasket.productTree
        
        // Notification for current store change
        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.CurrentStoreChanged.rawValue, object: nil);
        
        // Assign the current store for logged in user for session management
        if let user = UserService.sharedUser{
            user.currentStore = self.currentStore
            UserService.updateCurrentStore()
        }
        
        // Override shared basket object with current store and oloBasket
        BasketService.createBasketForCurrentStore( self.currentStore, oloBasket: storeMenuBasket.oloBasket!)
    }
    
    
    // Get olo restaurant details for store code
    func oloRestauarantDetailsForStoreCode(store:Store,callback:OloRestaurantCallaback){
        if store.storeCode == nil{
             return callback(store: store, error: NSError.init(description: "Store does not support order ahead"))
        }
        StoreService.storeByStoreCode(store.storeCode!) { (stores, location, error) in
            if error != nil{
                return callback(store: store, error: error)
            }
            else if let storeFromOlo = stores.first {
                store.updateOloRestaurantProperties(storeFromOlo)
                if store.supportsOrderAhead == false{
                    return callback(store: store, error: NSError.init(description: "Store does not support order ahead"))
                }
                return callback(store: store, error: nil)
            }
            else{
                return callback(store: store, error: NSError.init(description: "Store does not support order ahead"))
            }
        }
    }
    
        
}
