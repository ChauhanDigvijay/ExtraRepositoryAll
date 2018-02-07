//
//  RewardService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/19/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SpendGoSDK

typealias RewardsCallback = (rewards: RewardSummary?, error: NSError?) -> Void

class RewardService {

    class func loadRewards(callback: RewardsCallback) {
        SpendGoRewardService.rewardSummary(SpendGoSessionService.spendGoId!, callback: { (summary, error) -> Void in
            if error != nil {
                callback(rewards: nil, error: error)
                return
            }
            callback(rewards: RewardSummary(spendGoRewardSummary: summary!), error: nil)
        })
    }
    
}
