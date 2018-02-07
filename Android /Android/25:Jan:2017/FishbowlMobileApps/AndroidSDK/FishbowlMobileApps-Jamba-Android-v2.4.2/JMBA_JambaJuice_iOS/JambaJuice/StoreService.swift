//
//  StoreService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/25/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import OloSDK
import SpendGoSDK
import HDK
import MapKit
import SwiftyJSON

enum StoreSearchResultType: Int {
    case allStores
    case orderAhead
}

typealias StoreCallback = (_ stores: StoreList, _ location: CLLocation?, _ error: NSError?) -> Void
typealias StoreScheduleCallback = (_ schedule: StoreSchedule?, _ error: NSError?) -> Void
typealias StoreMenuCallback = (_ storeMenu: StoreMenu?, _ error: NSError?) -> Void
typealias StoreProductModifiersCallback = (_ modifiers: [StoreMenuProductModifier], _ error: NSError?) -> Void
typealias StoreErrorCallback = (_ error: NSError?) -> Void

class StoreService: NSObject {
    
    /// Find stores near location name
    class func findStoresByLocationName(_ locationName: String, searchType: StoreSearchResultType, callback: @escaping StoreCallback) {
        log.verbose("Staring store search: \(locationName)")
        AnalyticsService.trackEvent("search", action: "store_search", label: locationName)
        LocationService.locationForString(locationName) { (location, error) in
            assertMainThread()
            if error != nil {
                callback([], location, error)
                return
            }
            self.findStoresByLocation(location!, searchType: searchType) { (stores, error) -> Void in
                callback(stores, location, error)
                let count = stores.count 
                AnalyticsService.trackEvent("search", action: count == 0 ? "store_search_no_results" : "store_search_results", label: "\(locationName) (\(count))")
            }
        }
    }
    
    /// Find stores near user location
    class func findStoresByUserLocation(_ searchType: StoreSearchResultType, callback: @escaping StoreCallback) {
        log.verbose("Staring store search near user location")
        AnalyticsService.trackEvent("search", action: "store_search", label: "user_location")
        LocationService.sharedInstance.getUserLocation { (location, error) in
            assertMainThread()
            if error != nil {
                callback([], location, error)
                return
            }
            // Location would be nil if user has declined access to location services
            if location == nil {
                callback([], location, error)
                return
            }
            self.findStoresByLocation(location!, searchType: searchType) { (stores, error) -> Void in
                callback(stores, location, error)
                
                let count = stores.count 
                AnalyticsService.trackEvent("search", action: count == 0 ? "store_search_no_results" : "store_search_results", label: "user_location (\(count))")
            }
        }
    }
    
    /// Load a store schedule for the given dates
    /// Ignore non-business type hours
    class func storeSchedule(_ storeId: Int64, fromDate: Date, toDate: Date, callback: @escaping StoreScheduleCallback) {
        OloRestaurantService.restaurantCalendar(storeId, fromDate: fromDate, toDate: toDate) { (calendars, error) in
            if error != nil {
                callback(nil, error)
                return
            }
            
            // Olo API returns two calendar types: business and delivery
            // We only need the first business calendar
            let schedule = calendars.filter { $0.type == "business" }.map { StoreSchedule(restaurantCalendar: $0) }.first
            callback(schedule, nil)
        }
    }
    
    /// Load a store schedule for an entire week (7 days from now)
    class func storeSchedule(_ storeId: Int64, callback: @escaping StoreScheduleCallback) {
        let todayDate = Date()
        let dateAfterWeek = Date(timeIntervalSinceNow: TimeInterval(6 * StoreSchedule.secondsPerDay))
        storeSchedule(storeId, fromDate: todayDate, toDate: dateAfterWeek, callback: callback)
    }
    
    fileprivate class func findStoresByLocation(_ location: CLLocation, searchType: StoreSearchResultType, callback: @escaping (_ stores: StoreList, _ error: NSError?) -> Void) {
        
        log.verbose("Searching stores near location: \(location)")
        var oloStores:[OloRestaurant] = []
        
        let searchRadiusInMiles = Constants.storeLocatorSearchRadiusInMiles
        let searchResultsLimit = Constants.storeLocatorNumberOfResults
        
        var searchlocation = location
        if !CLLocationCoordinate2DIsValid(searchlocation.coordinate) {
            searchlocation = Constants.defaultStoreLocationPoints
        }
        
        
        OloRestaurantService.restaurantsNear(searchlocation.coordinate.latitude, longitude: searchlocation.coordinate.longitude, radius: searchRadiusInMiles, limit: searchResultsLimit) { (restaurants, error) in
            if error != nil {
                log.error("Olo store search failed: \(String(describing: error?.localizedDescription))")
                callback([], error)
                return
            }
            oloStores = restaurants
            
            var stores = StoreList()
            for oloStore in oloStores {
                let store = Store(oloRestaurant: oloStore)
                stores.append(store)
            }
            
            // Calculate distance to all stores and sort list by distance (sortInPlace is crashing, not idea why)
            var sortedStores:[Store] = stores.sorted {
                let distA = $0.distanceToLocation(location) ?? Double.infinity
                let distB = $1.distanceToLocation(location) ?? Double.infinity
                return distA < distB
            }
            
            // Search results type
            if searchType == StoreSearchResultType.orderAhead {
                sortedStores = sortedStores.filter { $0.supportsOrderAhead }
            }
            
            // Truncate array to limit the number of results
            if sortedStores.count > Constants.storeLocatorNumberOfResults {
                sortedStores = Array(sortedStores[0..<Constants.storeLocatorNumberOfResults])
            }
            
            log.verbose("Final stores found: \(sortedStores.count)")
            callback(sortedStores, nil)
        }
    }
    
    
    /// Retrieve product menu for a given store
    class func menuForStore(_ store: Store, callback: @escaping StoreMenuCallback) {
        if store.storeMenu != nil {
            callback(store.storeMenu, nil)
            return
        }
        
        if store.restaurantId == nil{
            callback(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Store does not support order ahead"]))
            return
        }
        
        OloMenuService.restaurantMenu(store.restaurantId!) { (oloCategories, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            var storeMenu: StoreMenu = [:];
            for oloCategory in oloCategories {
                for oloProduct in oloCategory.products {
                    let storeMenuProduct = StoreMenuProduct(productId: oloProduct.id, chainProductId: oloProduct.chainProductId, cost: oloProduct.cost)
                    storeMenu[oloProduct.chainProductId] = storeMenuProduct
                }
            }
            callback(storeMenu, nil)
        }
    }
    
    /// Retrieve modifiers for a given product
    class func modifiersForProduct(_ product: Product, callback: @escaping StoreProductModifiersCallback) {
        if product.storeMenuProduct == nil {
            callback([], NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Product not available for order ahead"]))
            return
        }
        OloMenuService.productModifiers(product.storeMenuProduct!.productId) { (oloModifiers, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let modifiers = oloModifiers.map { StoreMenuProductModifier(oloModifier: $0) }
            callback(modifiers, nil)
        }
    }
    
    /// Start a new order for a given store
    /// Store will be modified by this call, with the store menu loaded
    class func startNewOrder(_ store: Store, callback: @escaping StoreErrorCallback) {
        menuForStore(store) { (storeMenu, error) -> Void in
            if error != nil {
                callback(error)
                return
            }
            if storeMenu == nil {
                callback(NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Failed to retrieve store menu"]))
                return
            }
            BasketService.createBasket(store) { (basket, error) -> Void in
                if error != nil {
                    callback(error)
                    return
                }
                // Since we have basket, we can now safely store the menu
                store.storeMenu = storeMenu!
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.OrderStarted.rawValue), object: self)
                callback(nil)
            }
        }
    }
    
    class func storeByStoreCode(_ storeCode: String, callback: @escaping StoreCallback) {
        // See if we can find a restaurant for this store
        OloRestaurantService.restaurant(storeCode) { (oloRestaurant, error) -> Void in
            if error != nil {
                callback([], nil, error)
                return
            }
            let store = Store(oloRestaurant: oloRestaurant!)
            callback([store], nil, nil)
        }
    }
    
    class func storeByRestaurantId(_ restaurantId: Int64, callback: @escaping StoreCallback) {
        // See if we can find a restaurant by id
        OloRestaurantService.restaurant(restaurantId) { (restaurant, error) in
            if error != nil {
                callback([], nil, error)
                return
            }
            let store = Store(oloRestaurant: restaurant!)
            callback([store], nil, nil)
        }
    }
}
