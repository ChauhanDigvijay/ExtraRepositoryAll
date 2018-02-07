//
//  OloOrderStatus.swift
//  Olo
//
//  Created by Taha Samad on 4/28/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloOrderStatus {
    
    public var id: String
    public var timePlaced: String
    public var vendorName: String
    public var status: String
    public var total: Double
    public var subTotal: Double
    public var salesTax: Double
    public var orderRef: String
    public var readyTime: String
    public var vendorExtRef: String
    public var deliveryMode: String
    public var deliveryAddress: OloDeliveryAddress?
    public var products: [OloOrderStatusProduct]
     public var isEditable: Bool
    public init(json: JSON) {
        id              = json["id"].stringValue
        timePlaced      = json["timeplaced"].stringValue
        vendorName      = json["vendorname"].stringValue
        status          = json["status"].stringValue
        total           = json["total"].doubleValue
        subTotal        = json["subtotal"].doubleValue
        salesTax        = json["salestax"].doubleValue
        orderRef        = json["orderref"].stringValue
        readyTime       = json["readytime"].stringValue
        vendorExtRef    = json["vendorextref"].stringValue
        deliveryMode    = json["deliverymode"].stringValue
        products        = json["products"].arrayValue.map { item in OloOrderStatusProduct(json: item) }
        deliveryAddress = OloDeliveryAddress(json: json["deliveryaddress"])
        
         isEditable      = json["iseditable"].boolValue
    }
    
}
