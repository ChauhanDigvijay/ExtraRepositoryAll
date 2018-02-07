//
//  OloDeliveryAddressList.swift
//  Olo
//
//  Created by VT016 on 10/03/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloDeliveryAddressList {
    
    public var deliverAddress: [OloDeliveryAddress]
    
    //User Delivery Address
    public init(json: JSON) {
        deliverAddress = json["deliveries"].map({ OloDeliveryAddress(json: $0.1) })
    }
    
}