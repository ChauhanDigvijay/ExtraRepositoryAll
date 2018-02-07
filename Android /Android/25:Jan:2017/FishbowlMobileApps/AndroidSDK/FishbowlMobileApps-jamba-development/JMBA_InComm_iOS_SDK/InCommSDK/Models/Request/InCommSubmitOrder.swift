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
        jsonDict["Purchaser"]             = purchaser.serializeAsJSONDictionary()
        jsonDict["Recipients"]            = recipients.map { $0.serializeAsJSONDictionary() }
        
        if paymentWithId != nil{
            jsonDict["Payment"]          = paymentWithId!.serializeAsJSONDictionary()
        }
        else{
        jsonDict["Payment"]               = payment!.serializeAsJSONDictionary()
        }
        //
        
        jsonDict["DelayActivation"]       = delayActivation
        jsonDict["Description"]           = description
        jsonDict["DiscountAmount"]        = discountAmount
        if discountId != nil {
            jsonDict["DiscountId"]        = NSNumber(int: discountId!)
        }
        jsonDict["FulfillImmediately"]    = fulfillImmediately
        jsonDict["Id"]                    = id
        jsonDict["PurchaseOrderFilename"] = purchaseOrderFilename
        jsonDict["PurchaseOrderNumber"]   = purchaseOrderNumber
        return jsonDict
    }
    
}
