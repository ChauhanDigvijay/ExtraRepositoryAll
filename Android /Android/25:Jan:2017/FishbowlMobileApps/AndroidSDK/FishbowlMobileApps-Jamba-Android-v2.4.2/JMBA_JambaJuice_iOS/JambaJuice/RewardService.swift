//
//  RewardService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/19/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SpendGoSDK

typealias RewardsCallback = (_ rewards: RewardSummary?, _ error: NSError?) -> Void

class RewardService {

    fileprivate(set) var rewardSummary:RewardSummary?
    
    static let sharedInstance=RewardService();
    
    class func loadRewards(_ callback: @escaping RewardsCallback) {
        if SpendGoSessionService.spendGoId == nil{
            return callback(nil,NSError.init(description: "User is not authenticated."))
        }
        SpendGoRewardService.rewardSummary(SpendGoSessionService.spendGoId!, callback: { (summary, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            callback(RewardSummary(spendGoRewardSummary: summary!), nil)
        })
    }
    
    func updateRewardSummary(_ rewardSummary:RewardSummary?){
        self.rewardSummary = rewardSummary
    }
}
