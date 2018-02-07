//
//  OloRestaurantService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

public typealias OloRestaurantList = [OloRestaurant]
public typealias OloRestaurantServiceCallback = (_ restaurant: OloRestaurant?, _ error: NSError?) -> Void
public typealias OloRestaurantListServiceCallback = (_ restaurants: OloRestaurantList, _ error: NSError?) -> Void
public typealias OloRestaurantSchedule = [OloRestaurantCalendar]
public typealias OloRestaurantCalendarCallback = (_ schedule: OloRestaurantSchedule, _ error: NSError?) -> Void

open class OloRestaurantService {

    /// Retrieve a complete list of participating restaurants
    /// - parameter callback:    Returns a list of OloRestaurant objects or error if failed
    open class func restaurants(_ callback: @escaping OloRestaurantListServiceCallback) {
        OloService.get("/restaurants") { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let restaurants = response["restaurants"].arrayValue.map { OloRestaurant(json: $0) }
            callback(restaurants, nil)
        }
    }

    /// Retrieve a single restaurant by its restaurant Id
    /// - parameter restaurantId:    Int64  Id for the Olo restaurant
    /// - parameter callback:        Returns a single OloRestaurant or error if failed
    open class func restaurant(_ restaurantId: Int64, callback: @escaping OloRestaurantServiceCallback) {
        OloService.get("/restaurants/\(restaurantId)") { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let restaurant = OloRestaurant(json: response)
            callback(restaurant, nil)
        }
    }

    /// Retrieve a single restaurant by its restaurant extRef code (Store Number)
    /// - parameter storeNumber:     String  Store Number (extRef) for the Olo restaurant
    /// - parameter callback:        Returns a single OloRestaurant or error if failed
    open class func restaurant(_ storeNumber: String, callback: @escaping OloRestaurantServiceCallback) {
        OloService.get("/restaurants/byref/\(storeNumber)") { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let restaurant = OloRestaurant(json: response["restaurants"].arrayValue.first!)
            callback(restaurant, nil)
        }
    }
    
    /// Store locator: find restaurants near a given location
    ///
    /// - parameter latitude:    Coordinate with the latitude
    /// - parameter longitude:   Coordinate with the longitude
    /// - parameter radius:      Radius in miles to search stores
    /// - parameter limit:       Maximum number of results
    /// - parameter callback:    Returns a list of OloRestaurant objects or error if failed
    open class func restaurantsNear(_ latitude: Double, longitude: Double, radius: Double, limit: Int, callback: @escaping OloRestaurantListServiceCallback) {
        let parameters: [String : AnyObject] = [
            "lat": latitude as AnyObject,
            "long": longitude as AnyObject,
            "radius": radius as AnyObject,
            "limit": limit as AnyObject
        ]
        OloService.get("/restaurants/near", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let restaurants = response["restaurants"].arrayValue.map { OloRestaurant(json: $0) }
            callback(restaurants, nil)
        }
    }
    
    open class func restaurantCalendar(_ restaurantId: Int64, fromDate: Date?, toDate: Date?, callback:@escaping OloRestaurantCalendarCallback) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyyMMdd"
        let fromString = dateFormatter.string(from: fromDate != nil ? fromDate! : Date())
        let toString = dateFormatter.string(from: toDate != nil ? toDate! : Date())
        let parameters: [String : AnyObject] = [
            "from": fromString as AnyObject,
            "to": toString as AnyObject
        ]
        OloService.get("/restaurants/\(restaurantId)/calendars", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let schedule = response["calendar"].arrayValue.map { OloRestaurantCalendar(json: $0) }
            callback(schedule, nil)
        }
    }

}
