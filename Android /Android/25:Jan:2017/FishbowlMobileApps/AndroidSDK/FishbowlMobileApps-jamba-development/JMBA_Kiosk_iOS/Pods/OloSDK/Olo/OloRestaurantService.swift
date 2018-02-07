//
//  OloRestaurantService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

public typealias OloRestaurantList = [OloRestaurant]
public typealias OloRestaurantServiceCallback = (restaurant: OloRestaurant?, error: NSError?) -> Void
public typealias OloRestaurantListServiceCallback = (restaurants: OloRestaurantList, error: NSError?) -> Void
public typealias OloRestaurantSchedule = [OloRestaurantCalendar]
public typealias OloRestaurantCalendarCallback = (schedule: OloRestaurantSchedule, error: NSError?) -> Void

public class OloRestaurantService {

    /// Retrieve a complete list of participating restaurants
    /// - parameter callback:    Returns a list of OloRestaurant objects or error if failed
    public class func restaurants(callback: OloRestaurantListServiceCallback) {
        OloService.get("/restaurants") { (response, error) -> Void in
            if error != nil {
                callback(restaurants: [], error: error)
                return
            }
            let restaurants = response["restaurants"].arrayValue.map { OloRestaurant(json: $0) }
            callback(restaurants: restaurants, error: nil)
        }
    }

    /// Retrieve a single restaurant by its restaurant Id
    /// - parameter restaurantId:    Int64  Id for the Olo restaurant
    /// - parameter callback:        Returns a single OloRestaurant or error if failed
    public class func restaurant(restaurantId: Int64, callback: OloRestaurantServiceCallback) {
        OloService.get("/restaurants/\(restaurantId)") { (response, error) -> Void in
            if error != nil {
                callback(restaurant: nil, error: error)
                return
            }
            let restaurant = OloRestaurant(json: response)
            callback(restaurant: restaurant, error: nil)
        }
    }

    /// Retrieve a single restaurant by its restaurant extRef code (Store Number)
    /// - parameter storeNumber:     String  Store Number (extRef) for the Olo restaurant
    /// - parameter callback:        Returns a single OloRestaurant or error if failed
    public class func restaurant(storeNumber: String, callback: OloRestaurantServiceCallback) {
        OloService.get("/restaurants/byref/\(storeNumber)") { (response, error) -> Void in
            if error != nil {
                callback(restaurant: nil, error: error)
                return
            }
            let restaurant = OloRestaurant(json: response["restaurants"].arrayValue.first!)
            callback(restaurant: restaurant, error: nil)
        }
    }
    
    /// Store locator: find restaurants near a given location
    ///
    /// - parameter latitude:    Coordinate with the latitude
    /// - parameter longitude:   Coordinate with the longitude
    /// - parameter radius:      Radius in miles to search stores
    /// - parameter limit:       Maximum number of results
    /// - parameter callback:    Returns a list of OloRestaurant objects or error if failed
    public class func restaurantsNear(latitude: Double, longitude: Double, radius: Double, limit: Int, callback: OloRestaurantListServiceCallback) {
        let parameters: [String : AnyObject] = [
            "lat": latitude,
            "long": longitude,
            "radius": radius,
            "limit": limit
        ]
        OloService.get("/restaurants/near", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(restaurants: [], error: error)
                return
            }
            let restaurants = response["restaurants"].arrayValue.map { OloRestaurant(json: $0) }
            callback(restaurants: restaurants, error: nil)
        }
    }
    
    public class func restaurantCalendar(restaurantId: Int64, fromDate: NSDate?, toDate: NSDate?, callback:OloRestaurantCalendarCallback) {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "yyyyMMdd"
        let fromString = dateFormatter.stringFromDate(fromDate != nil ? fromDate! : NSDate())
        let toString = dateFormatter.stringFromDate(toDate != nil ? toDate! : NSDate())
        let parameters: [String : AnyObject] = [
            "from": fromString,
            "to": toString
        ]
        OloService.get("/restaurants/\(restaurantId)/calendars", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(schedule: [], error: error)
                return
            }
            let schedule = response["calendar"].arrayValue.map { OloRestaurantCalendar(json: $0) }
            callback(schedule: schedule, error: nil)
        }
    }

}
