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
        jsonDict["productid"]           = NSNumber(value: productId as Int64)
        jsonDict["quantity"]            = NSNumber(value: quantity as Int64)
        jsonDict["choices"]             = choices.map({ $0.serializeAsJSONDictionary() })
        jsonDict["specialinstructions"] = specialInstructions as AnyObject?
        jsonDict["recipient"]           = recipient as AnyObject?
//        jsonDict["choicecustomfields"]  = choiceCustomFields.map { $0.serializeAsJSONDictionary() }
        return jsonDict
    }
    
}
