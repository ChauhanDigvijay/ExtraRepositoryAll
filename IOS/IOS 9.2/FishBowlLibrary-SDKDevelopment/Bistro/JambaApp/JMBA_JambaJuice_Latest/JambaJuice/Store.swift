//
//  Store.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/6/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK
import SpendGoSDK
import CoreLocation
import Parse
import HDK

typealias StoreList = [Store]

/// Store class holds state across multiple user interactions (start new order, download store menu, product modifiers, etc)
class Store: Equatable, CustomStringConvertible {
    
    static let parseClassName = "Store"
    
   
    var storeCode: String?
    var name: String
    var street: String
    var city: String
    var state: String
    var zip: String
    var phone: String
    var latitude: Double
    var longitude: Double
    
    // Support OrderAhead for olo response
    var supportsOnlineOrdering:Bool = true
    var isAvailable:Bool  = true
    
    // For Olo restaurants only
    var supportsOrderAhead: Bool {
        get {
            return (supportsOnlineOrdering && isAvailable && restaurantId != nil)
        }
    }
    
    var restaurantId: Int64?
    var supportsSpecialInstructions: Bool?
    var storeSchedule: StoreSchedule?
    
    // For Olo restaurants, when user has started an order
    var storeMenu: StoreMenu?
    
    init(spendGoStore: SpendGoStore) {
        
        // Assume SpendGo API returns StoreCode embedded on store name and that store code is always 4 digit
        // StoreCode field not available in all APIs that return stores
        storeCode = spendGoStore.name.substringToIndex(spendGoStore.name.startIndex.advancedBy(4))
        name = spendGoStore.name.substringFromIndex(spendGoStore.name.startIndex.advancedBy(5))
        
        street = spendGoStore.streetAddress
        city = spendGoStore.city
        state = spendGoStore.state
        zip  = spendGoStore.zip
        phone = spendGoStore.phoneNumber
        latitude = spendGoStore.latitude
        longitude = spendGoStore.longitude
    }
    
    //Needed for create from Order
    init(oloRestaurant: OloRestaurant) {
        //id SpendGo Store Id is not present on Olo Stores. But does not effect our particular use case.
        storeCode = oloRestaurant.storeCode
       // storeCode = nil
        // Remove the store name from name
        name = oloRestaurant.name.substringFromIndex(oloRestaurant.name.startIndex.advancedBy(oloRestaurant.storeName.characters.count+1))
        street = oloRestaurant.streetAddress
        city = oloRestaurant.city
        state = oloRestaurant.state
        zip  = oloRestaurant.zip
        phone = oloRestaurant.telephone
        latitude = oloRestaurant.latitude
        longitude = oloRestaurant.longitude
        restaurantId = oloRestaurant.id
        supportsSpecialInstructions = oloRestaurant.supportsSpecialInstructions
        
        // Validate for support order ahead required field
        isAvailable = oloRestaurant.isAvailable
        supportsOnlineOrdering = oloRestaurant.supportsOnlineOrdering
    }
    
    init(parseObject: PFObject) {
        storeCode    = parseObject["storeCode"] as? String
        name         = parseObject["name"] as! String
        street       = parseObject["street"] as! String
        city         = parseObject["city"] as! String
        state        = parseObject["state"] as! String
        zip          = parseObject["zip"] as! String
        phone        = parseObject["phone"] as! String
        latitude     = (parseObject["latitude"] as? NSNumber)?.doubleValue ?? 0
        longitude    = (parseObject["longitude"] as? NSNumber)?.doubleValue ?? 0
        restaurantId = (parseObject["restaurantId"] as? NSNumber)?.longLongValue
        
        // Tp check support order ahead
        if parseObject["isAvailable"] != nil{
            isAvailable = parseObject["isAvailable"] as! Bool
        }
        else{
            isAvailable = true
        }
        if parseObject["supportsOnlineOrdering"] != nil{
            supportsOnlineOrdering  = parseObject["supportsOnlineOrdering"] as! Bool
        }
        else{
            supportsOnlineOrdering = true
        }
    }
    
    func serializeAsParseObject() -> PFObject {
        let parseObject = PFObject(className: Store.parseClassName)
        parseObject["storeCode"]    = storeCode ?? NSNull()
        parseObject["name"]         = name
        parseObject["street"]       = street
        parseObject["city"]         = city
        parseObject["state"]        = state
        parseObject["zip"]          = zip
        parseObject["phone"]        = phone
        parseObject["latitude"]     = NSNumber(double: latitude)
        parseObject["longitude"]    = NSNumber(double: longitude)
        parseObject["restaurantId"] = supportsOrderAhead ? NSNumber(longLong: restaurantId!) : NSNull()
        parseObject["isAvailable"] =  isAvailable
        parseObject["supportsOnlineOrdering"] = supportsOnlineOrdering
        return parseObject
    }
    
    var description: String {
        get {
            let dict: [String:AnyObject?] = [
                "storeCode":    storeCode,
                "name":         name,
                "street":       street,
                "city":         city,
                "state":        state,
                "zip":          zip,
                "phone":        phone,
                "latitude":     latitude,
                "longitude":    longitude,
                "restaurantId": NSNumber(longLong: restaurantId ?? 0)
            ]
            return dict.description
        }
    }
    
    // Combine stree, city, state and zip
    var address: String {
        get {
            let cityState = [city, state].joinWithSeparator(", ")
            let cityStateZip = [cityState, zip].joinWithSeparator(" ")
            return "\(street)\n\(cityStateZip)"
        }
    }
    
    // If distance to user is known, returns the street and distance
    // If not, returns only street
    var streetAndDistance: String {
        get {
            assertMainThread()
            if let distance = distanceToUser() {
                let distanceStr = String(format: "%0.1f", distance)
                return "\(street) - \(distanceStr) mi"
            } else {
                return street
            }
        }
    }
    
    // For store search results, show distance to searched location
    func streetAndDistanceToLocation(location: CLLocation) -> String {
        if let distance = distanceToLocation(location) {
            let distanceStr = String(format: "%0.1f", distance)
            return "\(street) - \(distanceStr) mi"
        } else {
            return street
        }
    }
    
    // If distance to user is known, returns the address and distance
    // If not, returns only address
    var addressAndDistance: String {
        get {
            if let distance = distanceToUser() {
                let distanceStr = String(format: "%0.1f", distance)
                return "\(address)\n\n\(distanceStr) miles away"
            } else {
                return address
            }
        }
    }
    
    // Returns the store status (open / closed), if known
    func storeStatus() -> String {
        if let range = storeSchedule?.startAndEndDateTimesForToday() {
            if range.isValid() {
                if NSDate().timeIntervalSince1970 < range.end!.timeIntervalSince1970 {
                    let rangeString = range.rangeString
                    if rangeString != nil {
                        return "Open today " + rangeString!
                    }
                }
                else {
                    return "Closed"
                }
            }
        }
        return "Store hours not available"
    }
    
    /// Distance from store to user
    func distanceToUser() -> Double? {
        assertMainThread()
        if latitude == 0 || longitude == 0 {
            return nil
        }
        
        if let userLocation = LocationService.sharedInstance.userLocation() {
            return distanceToLocation(userLocation)
        }
        
        return nil
    }
    
    /// Distance from store to location
    func distanceToLocation(location: CLLocation) -> Double? {
        assertMainThread()
        if latitude == 0 || longitude == 0 {
            return nil
        }
        
        let storeLocation = CLLocation(latitude: latitude, longitude: longitude)
        let distanceInMeters = LocationService.distanceBetweenLocations(location, locationB: storeLocation)
        return distanceInMeters / Constants.metersPerMile
    }
    
    // Update olo restaurant properties in store object
    func updateOloRestaurantProperties(store: Store) {
        storeCode = store.storeCode
        //storeCode = nil
        name =  store.name
        street = store.street
        city = store.city
        state = store.state
        zip = store.zip
        phone = store.phone
        restaurantId = store.restaurantId
        supportsSpecialInstructions = store.supportsSpecialInstructions
        supportsOnlineOrdering = store.supportsOnlineOrdering
        isAvailable = store.isAvailable
        latitude = store.latitude
        longitude = store.longitude
    }
    
}


// MARK: Equatable

func == (lhs: Store, rhs: Store) -> Bool {
    return lhs.storeCode == rhs.storeCode
}
