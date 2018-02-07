//
//  InCommSubmitOrder.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/17/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public struct InCommSubmitOrder {
    
    public var purchaser: InCommOrderPurchaser
    public var recipients: [InCommOrderRecipientDetails]
    public var payment: InCommSubmitPayment?
    public var paymentWithId: InCommSubmitPaymentWithId?
    //
    public var delayActivation: Bool?
    public var description: String?
    public var discountAmount: Double?
    public var discountId: Int32?
    public var fulfillImmediately: Bool?
    public var id: String?
    public var purchaseOrderFilename: String?
    public var purchaseOrderNumber: String?
    
    public init (purchaser: InCommOrderPurchaser, recipients: [InCommOrderRecipientDetails], payment: InCommSubmitPayment?, paymentWithId: InCommSubmitPaymentWithId?) {
        self.purchaser = purchaser
        self.recipients = recipients
        self.payment   = payment
        self.paymentWithId = paymentWithId
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict = InCommJSONDictionary()
        jsonDict["Purchaser"]             = purchaser.serializeAsJSONDictionary() as AnyObject?
        jsonDict["Recipients"]            = recipients.map { $0.serializeAsJSONDictionary() }
        
        if paymentWithId != nil{
            jsonDict["Payment"]          = paymentWithId!.serializeAsJSONDictionary() as AnyObject?
        }
        else{
        jsonDict["Payment"]               = payment!.serializeAsJSONDictionary() as AnyObject?
        }
        //
        // Add Default Value
        jsonDict["DelayActivation"]       = false
        //
        
        jsonDict["Description"]           = description as AnyObject?
        jsonDict["DiscountAmount"]        = discountAmount as AnyObject?
        if discountId != nil {
            jsonDict["DiscountId"]        = NSNumber(value: discountId! as Int32)
        }
        // Add Default Value
        jsonDict["FulfillImmediately"]    = false
        //
        
        jsonDict["Id"]                    = id as AnyObject?
        jsonDict["PurchaseOrderFilename"] = purchaseOrderFilename as AnyObject?
        jsonDict["PurchaseOrderNumber"]   = purchaseOrderNumber as AnyObject?
        return jsonDict
    }
    
}
