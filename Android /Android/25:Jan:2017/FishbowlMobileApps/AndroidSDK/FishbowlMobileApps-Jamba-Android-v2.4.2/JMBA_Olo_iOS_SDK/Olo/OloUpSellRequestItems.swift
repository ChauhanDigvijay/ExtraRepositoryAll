//
//  OloUpSellRequestItems.swift
//  Olo
//
//  Created by VT010 on 10/20/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation

public struct OloUpSellRequestItems {
public var items: [OloUpSellRequestItem]

public init() {
    items = []
}
    
public func serializeAsJSONDictionary() -> OloJSONDictionary {
    var jsonDict = OloJSONDictionary()
    jsonDict["items"]  = items.map({$0.serializeAsJSONDictionary()})
    return jsonDict
}
}

