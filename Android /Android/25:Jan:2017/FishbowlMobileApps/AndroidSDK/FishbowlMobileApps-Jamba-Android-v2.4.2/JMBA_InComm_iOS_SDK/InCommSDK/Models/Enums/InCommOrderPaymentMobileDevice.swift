//
//  InCommOrderPaymentMobileDevice.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public struct InCommOrderPaymentMobileDevice {

    public var id: String?
    public var latitude: Double?
    public var longitude: Double?
    
    public init() {
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict          = InCommJSONDictionary()
        jsonDict["Id"]        = id as AnyObject?
        jsonDict["Latitude"]  = latitude as AnyObject?
        jsonDict["Longitude"] = longitude as AnyObject?
        return jsonDict
    }
    
}