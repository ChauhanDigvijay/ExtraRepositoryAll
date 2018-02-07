//
//  OloDeliveryAddressList.swift
//  Olo
//
//  Created by VT016 on 09/03/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloSavedDeliverAddressList {
    
    public var savedDeliverAddress: [OloSavedDeliverAdrdress]
    
    public init(json: JSON) {
        savedDeliverAddress = json["deliveries"].map({ OloSavedDeliverAdrdress(json: $0.1) })
    }
}
