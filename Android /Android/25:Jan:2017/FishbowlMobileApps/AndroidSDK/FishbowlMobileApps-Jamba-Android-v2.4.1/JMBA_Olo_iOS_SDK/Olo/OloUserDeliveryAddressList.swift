//
//  OloUserDeliveryAddressList.swift
//  Olo
//
//  Created by VT016 on 13/03/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloUserDeliveryAddressList {
    
    public var deliverAddress: [OloDeliveryAddress]
    
    //User Delivery Address
    public init(json: JSON) {
        deliverAddress = json["deliveryaddresses"].map({ OloDeliveryAddress(json: $0.1) })
    }
    
}