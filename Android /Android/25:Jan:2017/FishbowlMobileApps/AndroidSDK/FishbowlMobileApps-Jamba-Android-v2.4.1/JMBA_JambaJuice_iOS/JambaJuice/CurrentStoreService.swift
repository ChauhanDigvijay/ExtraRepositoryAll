//
//  CurrentStoreService.swift
//  JambaJuice
//
//  Created by vThink on 2/16/16.
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
typealias StartNewOrderCallBack =  (_ status:NSString?,_ error:NSError?) -> Void

// Menu Callback
typealias StoreMenuCallBack =  (_ storeMenuBasket:StoreMenuBasket?,_ error:NSError?) -> Void

// OloRestaurantDetails Callback
typealias OloRestaurantCallaback = (_ store:Store, _ error:NSError?) -> Void

// Parse Ads Callback
typealias AdProductListCallback = (AdProductsList, NSError?) -> Void

class CurrentStoreService{
    
    // Product family list
    fileprivate(set) var productTree: ProductFamilyList = []
    
    // Instance shared instance for the class
    static let sharedInstance = CurrentStoreService()
    
    // Current store assign the change store in this current store variable
    fileprivate(set) var currentStore: Store?
    
    // Product list array for store based feature products
    fileprivate(set) var storeBasedFeatureProducts: ProductList = []
    
    // restaurent list array for olo stores
    fileprivate(set) var oloRestaurentList: OloRestaurantList = []
    
    
    // Start new order is used to get menu and create a new basket for given store
    func startNewOrder(_ store:Store! , callback:@escaping StartNewOrderCallBack){

        // Assign store,menu,basket,featured products,produttree attributes in one structure
        var tempStoreMenuBasket = StoreMenuBasket()
        // Get menu for given store
        self.menuForStore(store) { (storeMenuBasket, error) in
            if error != nil{
                return callback("Failure", error)
            }
            if storeMenuBasket == nil{
                return callback("Failure", NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Unable to retrieve store menu"]))
            }
            
            // Override an tempStoreMenuBasket object with storeMenuBasket object
            tempStoreMenuBasket = storeMenuBasket!
            
            // Create basket for given store
            OloBasketService.createBasket(store.restaurantId!) { (oloBasket, error) -> Void in
                if error != nil {
                    return callback("Failure", error)
                }
                
                tempStoreMenuBasket.oloBasket = oloBasket!
                self.currentStoreMenuBasketUpdation(tempStoreMenuBasket)
                return callback("Success" , nil)
            }
        }
    }
    
    // Empty the current store and assign the selecte store
    func resetStore(_ store:Store){
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
    }
    
    // Load store based product tree
    func loadProductTree(_ chainProductIds: [Int64]) -> ProductFamilyList {
        loadProductsIfNeeded()
        return productFamiliesWithCategories(chainProductIds)
    }
    
    // Load products from parse
    func loadProductsIfNeeded() {
        // Check if it is time to download products and save to local data store
        if let lastPullDate = SettingsManager.setting(.LastProductUpdate) as? Date {
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
            let _ = PFObject.unpinAllObjectsInBackground(withName:productEntityName) // Ignore if it fails
            log.verbose("Downloaded \(products.count) products from Parse.com")
            let _ = PFObject.pinAll(inBackground:products, withName: productEntityName)
            SettingsManager.setSetting(.LastProductUpdate, value: Date() as AnyObject)
        } catch {
            log.error("Failed to load products from remote")
            CurrentStoreService.callAppError()
            
        }
    }
    
    //load ads from the local parse server
    func loadAds(_ callback: AdProductListCallback) {
        let query = PFQuery(className: AdClass.parseClassName)
        query.fromLocalDatastore()
        if let result = try? query.findObjects() {
            if result.count > 0 {
                var adslist =  AdClass(parseObject: result[0])
                //combine all addetails in Adclass
                let ads:[AdDetailList] = result.map({ object in
                    let ad = AdClass(parseObject: object)
                    let ads = self.adDetailsForAd(ad)
                    return ads
                })
                adslist.adsDetailList = ads.reduce([], +)
                callback(adslist, nil)
                return
            }
            callback(AdClass.init(), NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"failed to load products"]))
        } else {
            callback(AdClass.init(), NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"failed to load products"]))
        }
    }

    // Get menu families and categories
    func productFamiliesWithCategories(_ chainProductIds: [Int64]) -> ProductFamilyList {
        let query = PFQuery(className: productFamilyEntityName)
        query.order(byAscending: "order")
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
    func categoriesForFamilyWithProducts(_ family: ProductFamily, chainProductIds: [Int64]) -> ProductCategoryList {
        let query = PFQuery(className: productCategoryEntityName)
        query.whereKey("family", equalTo: PFObject(withoutDataWithClassName: productFamilyEntityName, objectId: family.id))
        query.order(byAscending: "order")
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
    func productsForCategory(_ category: ProductCategory, chainProductIds: [Int64]) -> ProductList {
        let query = PFQuery(className: productEntityName)
        query.whereKey("category", equalTo: PFObject(withoutDataWithClassName: productCategoryEntityName, objectId: category.id))
        query.order(byAscending: "order")
        query.fromLocalDatastore()
        if let objects = try? query.findObjects() {
            let products = objects.map { Product(parseObject: $0) }
            return products.filter { chainProductIds.contains($0.chainProductId) }
        }
        return []
    }
    
    // Get menu for given store
    func menuForStore(_ store:Store!,callback:@escaping StoreMenuCallBack){
        StoreService.menuForStore(store) { (storeMenu, error) -> Void in
            if error != nil  {
                return callback(nil,error)
            }
            
            UIApplication.inMainThread({
                ProductService.featuredProducts { (products, error) in
                    if error != nil {
                        CurrentStoreService.callAppError()
                        log.error("Error: no products loaded!")
                        return callback(nil,error)
                    }
                    
                    // Assign response of store menu in store object
                    store.storeMenu = storeMenu
                    var storeMenuBasket = StoreMenuBasket()
                    storeMenuBasket.store = store
                    
                    // Construct store based featured products and product tree and assign in storeMenuBasket object
                    let chainProductIds = Array(store.storeMenu!.keys)
                    storeMenuBasket.storeBasedFeatureProducts = products.filter { chainProductIds.contains($0.chainProductId) }
                    storeMenuBasket.productTree = self.loadProductTree(chainProductIds)
                    return callback(storeMenuBasket,nil)
                }
            })
        }
    }
    
    // Update currentstore service shared instance by assign storeMenuBasket object
    func currentStoreMenuBasketUpdation(_ storeMenuBasket:StoreMenuBasket!){
        self.currentStore = storeMenuBasket.store
        self.storeBasedFeatureProducts = storeMenuBasket.storeBasedFeatureProducts
        self.productTree = storeMenuBasket.productTree
        
        // Notification for current store change
        NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.CurrentStoreChanged.rawValue), object: nil);
        
        // Assign the current store for logged in user for session management
        if let user = UserService.sharedUser{
            user.currentStore = self.currentStore
            UserService.updateCurrentStore()
        }
        
        // Override shared basket object with current store and oloBasket
        BasketService.createBasketForCurrentStore( self.currentStore, oloBasket: storeMenuBasket.oloBasket!)
    }
    
    
    // Get olo restaurant details for store code
    func oloRestauarantDetailsForStoreCode(_ store:Store,callback:@escaping OloRestaurantCallaback){
        StoreService.storeByStoreCode(store.storeCode) { (stores, location, error) in
            if error != nil{
                return callback(store, error)
            }
            else if let storeFromOlo = stores.first {
                store.updateOloRestaurantProperties(storeFromOlo)
                if store.supportsOrderAhead == false{
                    return callback(store, NSError.init(description: "Store does not support order ahead"))
                }
                return callback(store, nil)
            }
            else{
                return callback(store, NSError.init(description: "Store does not support order ahead"))
            }
        }
    }
    
    //map AdDetail with the AdClass
    fileprivate func adDetailsForAd(_ ad: AdClass) -> AdDetailList {
        let query = PFQuery(className: AdDetailClass.parseClassName)
        query.whereKey(AdClass.parseClassName, equalTo: PFObject(withoutDataWithClassName: AdClass.parseClassName, objectId: ad.id))
        query.whereKey(AdDetailClass.parseStatusKey, equalTo: true)
        query.order(byAscending: AdDetailClass.parseOrderKey)
        query.fromLocalDatastore()
        do {
            let objects = try query.findObjects()
            let adDetails = objects.map { AdDetailClass(parseObject: $0) }
            return adDetails
        } catch {
            return []
        }
    }
    
    //MARK:- get olo restaurent
    func getOloRestaurentsFromApi(_ callback:@escaping (_ error: NSError?) -> Void) {
        OloRestaurantService.restaurants { (restaurants, error) in
            if error != nil {
                callback(error)
            } else {
                self.oloRestaurentList = restaurants
                callback(nil)
            }
        }
    }
    
    // Get product category List
    func getCategoryList(_ categoryId: String) -> ProductCategoryList {
        for family in productTree{
            for category in family.categories {
                if category.id == categoryId {
                    return [category]
                }
            }
        }
        return []
    }
}
