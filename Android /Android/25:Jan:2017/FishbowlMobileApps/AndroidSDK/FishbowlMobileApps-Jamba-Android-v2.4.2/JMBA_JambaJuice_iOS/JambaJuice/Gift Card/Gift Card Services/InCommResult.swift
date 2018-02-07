//
//  InCommResult.swift
//  JambaJuice
//
//  Created by VT010 on 10/24/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import SwiftyJSON
import InCommSDK

public struct InCommResult{
    public var payment: InCommPromoOrderPayment
    public var purchaser: InCommOrderPurchaser
    public var recipients: [InCommPromoRecipientDetails]
    
    public init(payment:InCommPromoOrderPayment, purchaser: InCommOrderPurchaser, recipients:[InCommPromoRecipientDetails]){
        self.payment = payment
        self.purchaser = purchaser
        self.recipients = recipients
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary{
        var jsonDict = InCommJSONDictionary()
        jsonDict["Payment"] = payment.serializeAsJSONDictionary() as AnyObject?
        jsonDict["Purchaser"] = purchaser.serializeAsJSONDictionary()
        jsonDict["Recipients"] =  recipients.map {$0.serializeAsJSONDictionary()}
        return jsonDict
    }
}
