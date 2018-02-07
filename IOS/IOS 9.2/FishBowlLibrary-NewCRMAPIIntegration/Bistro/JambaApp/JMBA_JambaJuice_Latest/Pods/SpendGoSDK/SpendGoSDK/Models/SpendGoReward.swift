//
//  SpendGoReward.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct SpendGoReward {
    
    public var type: String
    public var rewardTitle: String
    public var desc: String
    public var quantity: Int

    public init(json: JSON) {
        type        = json["type"].stringValue
        rewardTitle = json["reward_title"].stringValue
        desc        = json["description"].stringValue
        quantity    = json["quantity"].intValue
    }
    
}
