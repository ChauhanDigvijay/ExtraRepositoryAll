//
//  RewardSummary.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/19/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SpendGoSDK

struct RewardSummary {

    var points: Int
    var threshold: Int
    var rewardCount: Int
    var rewards: [Reward]
    
    init () {
        points = 0
        threshold = 0
        rewardCount = 0
        rewards = []
    }
    
    init(spendGoRewardSummary: SpendGoRewardSummary) {
        points      = Int(spendGoRewardSummary.pointTotal)
        threshold   = Int(spendGoRewardSummary.spendThreshold)
        rewardCount = Int(spendGoRewardSummary.rewardCount)
        rewards     = spendGoRewardSummary.rewardList.map { Reward(spendGoReward: $0) }
    }
    
}
