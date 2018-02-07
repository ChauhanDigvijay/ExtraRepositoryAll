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
    
    public init (cardId: Int32, cardPin: String? = nil, brandId: String? = nil, amount: Double, purchaser: InCommOrderPurchaser, payment: InCommSubmitPayment?, paymentWithId: InCommSubmitPaymentWithId?) {
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
        jsonDict["CardId"]                = NSNumber(int: cardId)
        jsonDict["Amount"]                = amount
        jsonDict["IsTestMode"]            = isTestMode
        jsonDict["Purchaser"]             = purchaser.serializeAsJSONDictionary()
        
        if paymentWithId != nil{
            jsonDict["Payment"]           = paymentWithId!.serializeAsJSONDictionary()
        }
        else{
        jsonDict["Payment"]               = payment!.serializeAsJSONDictionary()
        }
        //
        
        jsonDict["BrandId"]               = brandId
        jsonDict["CardPin"]               = cardPin
        return jsonDict
    }
    
}