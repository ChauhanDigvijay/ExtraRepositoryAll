//
//  SpendGoRewardService.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

public typealias SpendGoRewardSummaryCallback = (summary: SpendGoRewardSummary?, error: NSError?) -> Void

public class SpendGoRewardService {
    
    public class func rewardSummary(spendGoId: String, callback: SpendGoRewardSummaryCallback) {
        let parameters = [
            "spendgo_id": spendGoId
        ]
        SpendGoService.post("/rewardsAndOffers", parameters: parameters, needsAuthToken: true) { (response, error) -> Void in
            if error != nil {
                callback(summary: nil, error: error)
                return
            }
            callback(summary: SpendGoRewardSummary(json: response), error: nil)
        }
    }

}
