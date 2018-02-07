//
//  OloNewBasketProduct.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

public struct OloNewBasketProduct {
    
    public var productId: Int64
    public var quantity: Int64
    public var options: String
    public var specialInstructions: String?
    public var recipient: String?
    public var choiceCustomFields: [OloBatchProductChoice]
    
    public init() {
        productId = 0
        quantity = 0
        options = ""
        choiceCustomFields = []
    }
    
    public func serializeAsJSONDictionary() -> OloJSONDictionary {
        var jsonDict = OloJSONDictionary()
        jsonDict["productid"]           = NSNumber(value: productId as Int64)
        jsonDict["quantity"]            = NSNumber(value: quantity as Int64)
        jsonDict["options"]             = options as AnyObject?
        jsonDict["specialinstructions"] = specialInstructions as AnyObject?
        jsonDict["recipient"]           = recipient as AnyObject?
        jsonDict["choicecustomfields"]  = choiceCustomFields.map { $0.serializeAsJSONDictionary() }
        return jsonDict
    }
    
}
