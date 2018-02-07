//
//  SpendGoStoreService.swift
//  SpendGoSDK
//
//  Created by Eneko Alonso on 5/13/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias SpendGoNearestStoresCallback = ([SpendGoStore], NSError?) -> Void

open class SpendGoStoreService {
   
    open class func nearestStores(_ zip: String, distance: Double, callback: @escaping SpendGoNearestStoresCallback) {
        let parameters: SpendGoJSONDictonary = [
            "zip": zip as AnyObject,
            "distance": distance as AnyObject
        ]
        SpendGoService.post("/nearestStores", parameters: parameters, needsAuthToken: false) { (response, error) -> Void in
            if error != nil {
                callback([], error)
            }
            else {
                let stores = response.arrayValue.map { item in SpendGoStore(json: item) }
                callback(stores, nil)
            }
        }
    }
    
}
