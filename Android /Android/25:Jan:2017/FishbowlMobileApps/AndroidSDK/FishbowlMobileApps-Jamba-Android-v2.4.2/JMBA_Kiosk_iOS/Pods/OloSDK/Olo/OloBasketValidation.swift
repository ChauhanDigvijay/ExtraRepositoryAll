//
//  OloBasketValidation.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloBasketValidation {

    public var basketId: String
    public var tax: Double
    public var subTotal: Double
    public var total: Double
    
    public init(json: JSON) {
        basketId = json["basketid"].stringValue
        tax      = json["tax"].doubleValue
        subTotal = json["subtotal"].doubleValue
        total    = json["total"].doubleValue
    }
    
}
