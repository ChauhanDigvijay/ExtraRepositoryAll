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
    public var expirationDate: Date?
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
        jsonDict["Amount"]         = amount as AnyObject?
        jsonDict["BrandId"]        = brandId as AnyObject?
        jsonDict["Quantity"]       = NSNumber(value: quantity as UInt32)
        //
        jsonDict["EmbossText"]     = embossText as AnyObject?
        jsonDict["EmbossTextId"]   = embossTextId as AnyObject?
        jsonDict["ExpirationDate"] = expirationDate?.InCommDateTimeFormatString() as AnyObject?
        jsonDict["ImageCode"]      = imageCode as AnyObject?
        jsonDict["MessageFrom"]    = messageFrom as AnyObject?
        jsonDict["MessageText"]    = messageText as AnyObject?
        jsonDict["MessageTo"]      = messageTo as AnyObject?
        jsonDict["ProductId"]      = productId != nil ? NSNumber(value: productId! as Int32) : nil
        jsonDict["ProductName"]    = productName as AnyObject?
        jsonDict["HideAmount"]     = hideAmount as AnyObject?
        return jsonDict
    }
    
}
