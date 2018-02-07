//
//  OloBasket.swift
//  Olo
//
//  Created by Taha Samad on 5/5/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloBasket {
    
    public var id: String
    public var timeWanted: String?
    public var earliestReadyTime: String
    public var subTotal: Double
    public var salesTax: Double
    public var total: Double
    public var tip: Double
    public var discount: Double
    public var couponDiscount: Double
    public var vendorId: Int64
    public var vendorOnline: Bool
    public var deliveryMode: String
    public var deliveryAddress: OloDeliveryAddress
    public var products: [OloBasketProduct]
    public var appliedRewards: [OloLoyaltyReward]
    public var contactNumber: String
    public var customerhandoffcharge: Double
    public var leadTimeEstimateMinutes: Double
    
    public init(json: JSON) {
        id                = json["id"].stringValue
        timeWanted        = json["timewanted"].string
        earliestReadyTime = json["earliestreadytime"].stringValue
        subTotal          = json["subtotal"].doubleValue
        salesTax          = json["salestax"].doubleValue
        total             = json["total"].doubleValue
        tip               = json["tip"].doubleValue
        discount          = json["discount"].doubleValue
        couponDiscount    = json["coupondiscount"].doubleValue
        vendorId          = json["vendorid"].int64Value
        vendorOnline      = json["vendoronline"].boolValue
        deliveryMode      = json["deliverymode"].stringValue
        deliveryAddress   = OloDeliveryAddress(json: json["deliveryaddress"])
        products          = json["products"].arrayValue.map { OloBasketProduct(json: $0) }
        appliedRewards    = json["appliedrewards"].arrayValue.map { OloLoyaltyReward(json: $0) }
        contactNumber     = json["contactnumber"].stringValue
        customerhandoffcharge   = json["customerhandoffcharge"].doubleValue
        leadTimeEstimateMinutes = json["leadtimeestimateminutes"].doubleValue
    }
    
}
