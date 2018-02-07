//
//  OloBasketProduct.swift
//  Olo
//
//  Created by Taha Samad on 5/5/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloBasketProduct {
    
    public var id: Int64
    public var productId: Int64
    public var name: String
    public var quantity: Int
    public var baseCost: Double
    public var totalCost: Double
    public var specialInstructions: String
    public var choices: [OloBasketChoice]
    
    public init(json: JSON) {
        id                  = json["id"].int64Value
        productId           = json["productId"].int64Value
        name                = json["name"].stringValue
        quantity            = json["quantity"].intValue
        baseCost            = json["basecost"].doubleValue
        totalCost           = json["totalcost"].doubleValue
        specialInstructions = json["specialinstructions"].stringValue
        choices             = json["choices"].arrayValue.map { item in OloBasketChoice(json: item) }
    }
    
}
