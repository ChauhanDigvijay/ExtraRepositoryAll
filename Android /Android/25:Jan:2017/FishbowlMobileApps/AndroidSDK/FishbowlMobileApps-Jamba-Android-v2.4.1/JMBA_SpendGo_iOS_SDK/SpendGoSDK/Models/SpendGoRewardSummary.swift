//
//  SpendGoRewardSummary.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct SpendGoRewardSummary {
        
    public var pointTotal: Int64
    public var spendThreshold: Int64
    public var rewardCount: Int64
    public var rewardList: [SpendGoReward]
    
    public init(json: JSON) {
        pointTotal     = json["point_total"].int64Value
        spendThreshold = json["spend_threshold"].int64Value
        rewardCount    = json["rewards_count"].int64Value
        rewardList     = json["rewards_list"].arrayValue.map { item in SpendGoReward(json: item) }
    }
    
}
