//
//  InCommPromoOrderPayment.swift
//  JambaJuice
//
//  Created by VT010 on 10/25/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommPromoOrderPayment {
    public var orderPaymentMethod:String
    
    
    public init(orderPaymentMethod:String) {
        self.orderPaymentMethod = orderPaymentMethod
    }
    
    public func serializeAsJSONDictionary() -> ClpJSONDictionary {
        var jsonDict                     = ClpJSONDictionary()
        jsonDict["OrderPaymentMethod"]    = orderPaymentMethod
        return jsonDict
    }
}
