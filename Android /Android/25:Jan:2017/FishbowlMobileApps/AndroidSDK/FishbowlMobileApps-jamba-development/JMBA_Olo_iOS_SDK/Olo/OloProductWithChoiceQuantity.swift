//
//  OloProductWithChoiceQuantity.swift
//  Olo
//
//  Created by vThink on 22/11/16.
//  Copyright © 2016 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloProductWithChoiceQuantity {
    
    public var productId: Int64
    public var quantity: Int64
    public var choices: [OloProductChoiceQuantity]
    public var specialInstructions: String?
    public var recipient: String?
    
    public init() {
        productId = 0
        quantity = 0
        choices = []
//        choiceCustomFields = []
    }
    
    public func serializeAsJSONDictionary() -> OloJSONDictionary {
        var jsonDict = OloJSONDictionary()
        jsonDict["productid"]           = NSNumber(longLong:productId)
        jsonDict["quantity"]            = NSNumber(longLong:quantity)
        jsonDict["choices"]             = choices.map({ $0.serializeAsJSONDictionary() })
        jsonDict["specialinstructions"] = specialInstructions
        jsonDict["recipient"]           = recipient
//        jsonDict["choicecustomfields"]  = choiceCustomFields.map { $0.serializeAsJSONDictionary() }
        return jsonDict
    }
    
}