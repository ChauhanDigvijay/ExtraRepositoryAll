//
//  InCommSubmitPaymentWithId.swift
//  InCommSDK
//
//  Created by vThink on 8/29/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation

public struct InCommSubmitPaymentWithId{
    
    public var amount:Double
    public var paymentAccountId: Int32
    public var vestaOrgId: String
    public var vestaWebSessionId: String
    
    public init(amount:Double!, paymentAccountId:Int32!,vestaOrgId:String,vestaWebSessionId:String){
        self.amount = amount
        self.paymentAccountId = paymentAccountId
        self.vestaOrgId = vestaOrgId
        self.vestaWebSessionId = vestaWebSessionId
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict = InCommJSONDictionary()
        
        jsonDict["Amount"] = amount as AnyObject?
        jsonDict["PaymentAccountId"] = NSNumber(value: paymentAccountId as Int32)
        jsonDict["VestaOrgId"]                 = vestaOrgId as AnyObject?
        jsonDict["VestaWebSessionId"]          = vestaWebSessionId as AnyObject?
        jsonDict["PaymentReceived"]            = false
        
        return jsonDict
    }
}
