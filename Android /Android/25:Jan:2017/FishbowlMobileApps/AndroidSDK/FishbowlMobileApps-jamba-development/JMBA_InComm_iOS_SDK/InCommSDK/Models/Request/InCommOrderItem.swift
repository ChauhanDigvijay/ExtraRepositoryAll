//
//  InCommOrderItem.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/17/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommOrderItem {
    public var amount: Double
    public var brandId: String
    public var quantity: UInt32
    //
    public var embossText: String?
    public var embossTextId: String?
    public var expirationDate: NSDate?
    public var imageCode: String?
    public var messageFrom: String?
    public var messageText: String?
    public var messageTo: String?
    public var productId: Int32?
    public var productName: String?
    public var hideAmount:Bool?

    public init(brandId: String, amount: Double, quantity: UInt32) {
        self.amount   = amount
        self.brandId  = brandId
        self.quantity = quantity
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict               = InCommJSONDictionary()
        jsonDict["Amount"]         = amount
        jsonDict["BrandId"]        = brandId
        jsonDict["Quantity"]       = NSNumber(unsignedInt: quantity)
        //
        jsonDict["EmbossText"]     = embossText
        jsonDict["EmbossTextId"]   = embossTextId
        jsonDict["ExpirationDate"] = expirationDate?.InCommDateTimeFormatString()
        jsonDict["ImageCode"]      = imageCode
        jsonDict["MessageFrom"]    = messageFrom
        jsonDict["MessageText"]    = messageText
        jsonDict["MessageTo"]      = messageTo
        jsonDict["ProductId"]      = productId != nil ? NSNumber(int: productId!) : nil
        jsonDict["ProductName"]    = productName
        jsonDict["HideAmount"]     = hideAmount
        return jsonDict
    }
    
}