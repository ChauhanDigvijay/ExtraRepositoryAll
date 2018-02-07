//
//  OloOrderStatusProduct.swift
//  Olo
//
//  Created by Taha Samad on 4/28/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloOrderStatusProduct {
    
    public var name: String
    public var quantity: Int
    public var totalCost: Double
    public var specialInstructions: String
    public var choices: [OloBasketChoice]
    
    public init(json: JSON) {
        name                = json["name"].stringValue
        quantity            = json["quantity"].intValue
        totalCost           = json["totalcost"].doubleValue
        specialInstructions = json["specialinstructions"].stringValue
        choices             = json["choices"].arrayValue.map { item in OloBasketChoice(json: item) }
    }
    
}
