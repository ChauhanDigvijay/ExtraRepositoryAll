//
//  OloLoyaltyScheme.swift
//  Olo
//
//  Created by Eneko Alonso on 6/29/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloLoyaltyScheme {

    public var id: Int64
    public var label: String
    public var name: String
    public var membership: OloLoyaltyMembership?
    
    public init(json: JSON) {
        id         = json["id"].int64Value
        label      = json["label"].stringValue
        name       = json["name"].stringValue
        if json["membership"] != JSON.null {
            membership = OloLoyaltyMembership(json: json["membership"])
        }
    }
    
}
