//
//  OloUpsellRequestItem.swift
//  Olo
//
//  Created by VT016 on 30/06/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation

public struct OloUpSellRequestItem {
    public var id: Int64
    public var quantity: Int64
    
    public init() {
        id = 0
        quantity = 0
    }
    
    public func serializeAsJSONDictionary() -> OloJSONDictionary {
        var jsonDict = OloJSONDictionary()
        jsonDict["id"]                  = NSNumber(value: id as Int64)
        jsonDict["quantity"]            = NSNumber(value: quantity as Int64)
        return jsonDict
    }
}
