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

enum StoreSearchResultType {
    case AllStores
    case OrderAhead
}

typealias StoreCallback = (stores: StoreList, location: CLLocation?, error: NSError?) -> Void
typealias StoreScheduleCallback = (schedule: StoreSchedule?, error: NSError?) -> Void
typealias StoreMenuCallback = (storeMenu: StoreMenu?, error: NSError?) -> Void
typealias StoreProductModifiersCallback = (modifiers: [StoreMenuProductModifier], error: NSError?) -> Void
typealias StoreErrorCallback = (error: NSError?) -> Void

class StoreService: NSObject {

    /// Find stores near location name
    class func findStoresByLocationName(locationName: String, searchType: StoreSearchResultType, callback: StoreCallback) {
        log.verbose("Staring store search: \(locationName)")
        AnalyticsService.trackEvent("search", action: "store_search", label: locationName)
        LocationService.locationForString(locationName) { (location, error) in
            assertMainThread()
            if error != nil {
                callback(stores: [], location: location, error: error)
                return
            }
            self.findStoresByLocation(location!, searchType: searchType) { (stores, error) -> Void in
                callback(stores: stores, location: location, error: error)
                let count = stores.count ?? 0
                AnalyticsService.trackEvent("search", action: count == 0 ? "store_search_no_results" : "store_search_results", label: "\(locationName) (\(count))")
            }
        }
    }

    /// Find stores near user location
    class func findStoresByUserLocation(searchType: StoreSearchResultType, callback: StoreCallback) {
        log.verbose("Staring store search near user location")
        AnalyticsService.trackEvent("search", action: "store_search", label: "user_location")
        LocationService.sharedInstance.getUserLocation { (location, error) in
            assertMainThread()
            if error != nil {
                callback(stores: [], location: location, error: error)
                return
            }
            // Location would be nil if user has declined access to location services
            if location == nil {
                callback(stores: [], location: location, error: error)
                return
            }
            self.findStoresByLocation(location!, searchType: searchType) { (stores, error) -> Void in
                callback(stores: stores, location: location, error: error)
                
                let count = stores.count ?? 0
                AnalyticsService.trackEvent("search", action: count == 0 ? "store_search_no_results" : "store_search_results", label: "user_location (\(count))")
            }
        }
    }

    /// Load a store schedule for the given dates
    /// Ignore non-business type hours
    class func storeSchedule(storeId: Int64, fromDate: NSDate, toDate: NSDate, callback: StoreScheduleCallback) {
        OloRestaurantService.restaurantCalendar(storeId, fromDate: fromDate, toDate: toDate) { (calendars, error) in
            if error != nil {
                callback(schedule: nil, error: error)
                return
            }
            
            // Olo API returns two calendar types: business and delivery
            // We only need the first business calendar
            let schedule = calendars.filter { $0.type == "business" }.map { StoreSchedule(restaurantCalendar: $0) }.first
            callback(schedule: schedule, error: nil)
        }
    }

    /// Load a store schedule for an entire week (7 days from now)
    class func storeSchedule(storeId: Int64, callback: StoreScheduleCallback) {
        let todayDate = NSDate()
        let dateAfterWeek = NSDate(timeIntervalSinceNow: NSTimeInterval(6 * StoreSchedule.secondsPerDay))
        storeSchedule(storeId, fromDate: todayDate, toDate: dateAfterWeek, callback: callback)
    }
    
    /// Given a location with coordinates, find nearby stores using Olo and SpendGo APIs
    private class func findStoresByLocation(location: CLLocation, searchType: StoreSearchResultType, callback: (stores: StoreList, error: NSError?) -> Void) {
        assertMainThread()
        log.verbose("Searching stores near location: \(location)")
        let group = dispatch_group_create()

        var oloRestaurantMap = Dictionary<String, OloRestaurant>()
        var spendGoStores: [SpendGoStore] = []
        var oloError: NSError?
        var spendGoError: NSError?
        
        let searchRadiusInMiles = Constants.storeLocatorSearchRadiusInMiles
        let searchResultsLimit = Constants.storeLocatorNumberOfResults
        
        var searchlocation = location
        if !CLLocationCoordinate2DIsValid(searchlocation.coordinate) {
            searchlocation = Constants.defaultStoreLocationPoints
        }

        // Find nearby stores in Olo service
        dispatch_group_enter(group)
        OloRestaurantService.restaurantsNear(searchlocation.coordinate.latitude, longitude: searchlocation.coordinate.longitude, radius: searchRadiusInMiles, limit: searchResultsLimit) { (restaurants, error) in
            assertMainThread()
            let restaurants = restaurants
            if error != nil {
                oloError = error
                log.error("Olo store search failed: \(error?.localizedDescription)")
                dispatch_group_leave(group)
                return
            }
 
            
            let availableRestaurants = restaurants.filter { $0.isAvailable }
            log.verbose("Olo Stores: \(availableRestaurants.count)")
            // Map restaurants by Store Code
            
            // Here we map all restaurant in olo restaurant map 
            for restaurant in restaurants {
                oloRestaurantMap[restaurant.storeCode] = restaurant
            }
            
            dispatch_group_leave(group)
        }

        // Find nearby stores in SpendGo service
        dispatch_group_enter(group)
        LocationService.zipcodeForLocation(location) { (zipcode, error) in
            assertMainThread()
            if error != nil {
                spendGoError = error
                log.error("Zipcode geolocation failed: \(error?.localizedDescription)")
                dispatch_group_leave(group)
                return
            }

            SpendGoStoreService.nearestStores(zipcode, distance: searchRadiusInMiles) { (stores, error) in
                let stores = stores
                if error != nil {
                    spendGoError = error
                    log.error("Olo store search failed: \(error?.localizedDescription)")
                    dispatch_group_leave(group)
                    return
                }
                
                log.verbose("SpendGo Stores: \(stores.count)")
                spendGoStores = stores
                dispatch_group_leave(group)
            }
        }

        // Once both searches have returned, combine results
        dispatch_group_notify(group, dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0)) {
            UIApplication.inMainThread {
                assertMainThread()
                if oloError != nil {
                    callback(stores: [], error: oloError)
                    return
                }
                if spendGoError != nil {
                    callback(stores: [], error: spendGoError)
                    return
                }

                // Map Olo Restaurants to SpendGo Stores
                var stores = StoreList()
                for spendGoStore in spendGoStores {
                    let store = Store(spendGoStore: spendGoStore)
                    if let restaurant = oloRestaurantMap[store.storeCode] {
                        store.updateOloRestaurantProperties(restaurant)
                    }
                    stores.append(store)
                    
                }
                
                // Calculate distance to all stores and sort list by distance (sortInPlace is crashing, not idea why)
                var sortedStores = stores.sort {
                    let distA = $0.distanceToLocation(location) ?? Double.infinity
                    let distB = $1.distanceToLocation(location) ?? Double.infinity
                    return distA < distB
                }

                // Search results type
                if searchType == .OrderAhead {
                    sortedStores = sortedStores.filter { $0.supportsOrderAhead }
                }
                
                // Truncate array to limit the number of results
                if sortedStores.count > Constants.storeLocatorNumberOfResults {
                    sortedStores = Array(sortedStores[0..<Constants.storeLocatorNumberOfResults])
                }
                
                assertMainThread()
                log.verbose("Final stores found: \(sortedStores.count)")
                callback(stores: sortedStores, error: nil)
            }
        }
    }

    /// Retrieve product menu for a given store
    class func menuForStore(store: Store, callback: StoreMenuCallback) {
        if store.storeMenu != nil {
            callback(storeMenu: store.storeMenu, error: nil)
            return
        }
        
        if store.restaurantId == nil{
            callback(storeMenu: nil, error: NSError.init(description: "Store does not support order ahead"))
            return
        }
        
        OloMenuService.restaurantMenu(store.restaurantId!) { (oloCategories, error) -> Void in
            if error != nil {
                callback(storeMenu: nil, error: error)
                return
            }
            var storeMenu: StoreMenu = [:];
            for oloCategory in oloCategories {
                for oloProduct in oloCategory.products {
                    let storeMenuProduct = StoreMenuProduct(productId: oloProduct.id, chainProductId: oloProduct.chainProductId, cost: oloProduct.cost)
                    storeMenu[oloProduct.chainProductId] = storeMenuProduct
                }
            }
            callback(storeMenu: storeMenu, error: nil)
        }
    }

    /// Retrieve modifiers for a given product
    class func modifiersForProduct(product: Product, callback: StoreProductModifiersCallback) {
        if product.storeMenuProduct == nil {
            callback(modifiers: [], error: NSError(description: "Product not available for order ahead"))
            return
        }
        OloMenuService.productModifiers(product.storeMenuProduct!.productId) { (oloModifiers, error) -> Void in
            if error != nil {
                callback(modifiers: [], error: error)
                return
            }
            let modifiers = oloModifiers.map { StoreMenuProductModifier(oloModifier: $0) }
            callback(modifiers: modifiers, error: nil)
        }
    }
    
    /// Start a new order for a given store
    /// Store will be modified by this call, with the store menu loaded
    class func startNewOrder(store: Store, callback: StoreErrorCallback) {
        menuForStore(store) { (storeMenu, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            if storeMenu == nil {
                callback(error: NSError(description: "Failed to retrieve store menu"))
                return
            }
            BasketService.createBasket(store) { (basket, error) -> Void in
                if error != nil {
                    callback(error: error)
                    return
                }
                // Since we have basket, we can now safely store the menu
                store.storeMenu = storeMenu!
                NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.OrderStarted.rawValue, object: self)
                callback(error: nil)
            }
        }
    }

    class func storeByStoreCode(storeCode: String, callback: StoreCallback) {
        // See if we can find a restaurant for this store
        OloRestaurantService.restaurant(storeCode) { (oloRestaurant, error) -> Void in
            if error != nil {
                callback(stores: [], location: nil, error: error)
                return
            }
            let store = Store(oloRestaurant: oloRestaurant!)
            callback(stores: [store], location: nil, error: nil)
        }
    }
}
