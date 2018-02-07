//
//  OloLoyaltyReward.swift
//  Olo
//
//  Created by Eneko Alonso on 6/30/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloLoyaltyReward {

    public var type: String
    public var applied: Bool
    public var label: String
    public var value: Double
    public var reference: String
    public var rewardId: Int64?
    public var membershipId: Int64

    public init(json: JSON) {
        type         = json["type"].stringValue
        applied      = json["applied"].boolValue
        label        = json["label"].stringValue
        value        = json["value"].doubleValue
        reference    = json["reference"].stringValue
        membershipId = json["membershipid"].int64Value
        rewardId     = json["rewardid"].int64
    }
    
}
