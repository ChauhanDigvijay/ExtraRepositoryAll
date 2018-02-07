//
//  OloLoyaltyMembership.swift
//  Olo
//
//  Created by Eneko Alonso on 6/29/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloLoyaltyMembership {

    public var id: Int64
    public var desc: String
    public var membershipNumber: String

    public init(json: JSON) {
        id = json["id"].int64Value
        desc = json["description"].stringValue
        membershipNumber = json["membershipnumber"].stringValue
    }
    
}
