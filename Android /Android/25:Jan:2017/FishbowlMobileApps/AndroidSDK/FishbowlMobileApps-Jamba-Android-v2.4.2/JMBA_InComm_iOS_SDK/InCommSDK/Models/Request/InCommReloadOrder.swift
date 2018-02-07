//
//  InCommReloadOrder.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommReloadOrder {
    
    public var cardId: Int32
    public var amount: Double
    public var isTestMode: Bool
    public var purchaser: InCommOrderPurchaser
    public var payment: InCommSubmitPayment?
    public var paymentWithId: InCommSubmitPaymentWithId?
    //
    public var brandId: String?
    public var cardPin: String?
    
    public init (cardId: Int32, cardPin: String?, brandId: String?, amount: Double, purchaser: InCommOrderPurchaser, payment: InCommSubmitPayment?, paymentWithId: InCommSubmitPaymentWithId?) {
        self.cardId = cardId
        self.cardPin = cardPin
        self.brandId = brandId
        self.amount = amount
        self.isTestMode = InCommService.configuration.testMode
        self.purchaser = purchaser
        self.payment   = payment
        self.paymentWithId = paymentWithId
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict = InCommJSONDictionary()
        jsonDict["CardId"]                = NSNumber(value: cardId as Int32)
        jsonDict["Amount"]                = amount as AnyObject?
        jsonDict["IsTestMode"]            = isTestMode as AnyObject?
        jsonDict["Purchaser"]             = purchaser.serializeAsJSONDictionary() as AnyObject?
        
        if paymentWithId != nil{
            jsonDict["Payment"]           = paymentWithId!.serializeAsJSONDictionary() as AnyObject?
        }
        else{
        jsonDict["Payment"]               = payment!.serializeAsJSONDictionary() as AnyObject?
        }
        //
        
        jsonDict["BrandId"]               = brandId as AnyObject?
        jsonDict["CardPin"]               = cardPin as AnyObject?
        return jsonDict
    }
    
}
