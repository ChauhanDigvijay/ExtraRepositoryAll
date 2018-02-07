//
//  OloSavedDeliverAdrdress.swift
//  Olo
//
//  Created by VT016 on 09/03/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON


public struct OloSavedDeliverAdrdress {
    
    public var id                : String
    public var status            : String
    public var drivername        : String
    public var driverphonenumber : String
    public var deliveryservice   : String

    public init(json: JSON) {
        id                  = json["id"].stringValue
        status              = json["status"].stringValue
        drivername          = json["drivername"].stringValue
        driverphonenumber   = json["driverphonenumber"].stringValue
        deliveryservice     = json["deliveryservice"].stringValue
    }
}
