//
//  SpendGoStore.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct SpendGoStore {
    
    public var id: String
    public var storeCode: String
    public var name: String
    public var streetAddress: String
    public var city: String
    public var state: String
    public var zip: String
    public var phoneNumber: String
    public var latitude: Double
    public var longitude: Double
    
    public init(json: JSON) {
        id            = json["id"].stringValue
        storeCode     = json["code"].stringValue
        name          = json["name"].stringValue
        streetAddress = json["street"].stringValue
        city          = json["city"].stringValue
        state         = json["state"].stringValue
        zip           = json["zip"].stringValue
        phoneNumber   = json["phone"].stringValue
        latitude      = json["latitude"].doubleValue
        longitude     = json["longitude"].doubleValue
    }

    public func serializeAsJSONDictionary() -> SpendGoJSONDictonary {
        var jsonDict = SpendGoJSONDictonary()
        jsonDict["id"] = id
        jsonDict["name"] = name
        jsonDict["street"] = streetAddress
        jsonDict["city"] = city
        jsonDict["state"] = state
        jsonDict["zip"] = zip
        return jsonDict
    }
    
}
