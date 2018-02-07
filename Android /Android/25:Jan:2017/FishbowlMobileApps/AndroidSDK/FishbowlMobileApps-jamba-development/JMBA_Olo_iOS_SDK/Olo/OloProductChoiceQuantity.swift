//
//  OloProductChoiceQuantity.swift
//  Olo
//
//  Created by vThink on 22/11/16.
//  Copyright © 2016 Hathway, Inc. All rights reserved.
//

import Foundation

public struct OloProductChoiceQuantity {
    
    public var choiceId: Int64
    public var quantity: Int
    
    public init() {
        choiceId = 0
        quantity = 0
    }
    
    public func serializeAsJSONDictionary() -> OloJSONDictionary {
        var jsonDict = OloJSONDictionary()
        jsonDict["choiceid"]    = NSNumber(longLong: choiceId)
        jsonDict["quantity"]    = quantity
        return jsonDict
    }
    
}