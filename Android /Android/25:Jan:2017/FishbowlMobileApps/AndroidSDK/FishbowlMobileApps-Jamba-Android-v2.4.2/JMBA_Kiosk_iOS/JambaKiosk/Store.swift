//
//  Store.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/6/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK
import CoreLocation
import Parse

typealias StoreList = [Store]

/// Store class holds state across multiple user interactions (start new order, download store menu, product modifiers, etc)
class Store: Equatable, CustomStringConvertible {

    static let parseClassName = "Store"

    var spendGoStoreId: String? //SpendGo Store Id
    var storeCode: String
    var name: String
    var street: String
    var city: String
    var state: String
    var zip: String
    var phone: String
    var latitude: Double
    var longitude: Double

    // Olo properties
    var restaurantId: Int64?
    var supportsOrderAhead: Bool
    var supportsSpecialInstructions: Bool
    var isAvailable: Bool

    // For Olo restaurants, when user has started an order
    var storeMenu: StoreMenu?

    //Needed for create from Order
    init(oloRestaurant: OloRestaurant) {
        //id SpendGo Store Id is not present on Olo Stores. But does not effect our particular use case.
        storeCode = oloRestaurant.storeCode
        name = oloRestaurant.name
        street = oloRestaurant.streetAddress
        city = oloRestaurant.city
        state = oloRestaurant.state
        zip  = oloRestaurant.zip
        phone = oloRestaurant.telephone
        latitude = oloRestaurant.latitude
        longitude = oloRestaurant.longitude
        restaurantId = oloRestaurant.id
        supportsOrderAhead = oloRestaurant.supportsOrderAhead
        supportsSpecialInstructions = oloRestaurant.supportsSpecialInstructions
        isAvailable = oloRestaurant.isAvailable
    }

    var description: String {
        get {
            let dict: [String:AnyObject?] = [
                "id":           spendGoStoreId,
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

    func updateOloRestaurantProperties(store: Store) {
        restaurantId = store.restaurantId
        supportsSpecialInstructions = store.supportsSpecialInstructions
    }

    func updateOloRestaurantProperties(oloRestaurant: OloRestaurant) {
        restaurantId = oloRestaurant.id
        supportsSpecialInstructions = oloRestaurant.supportsSpecialInstructions
    }

}


// MARK: Equatable

func == (lhs: Store, rhs: Store) -> Bool {
    return lhs.storeCode == rhs.storeCode
}
