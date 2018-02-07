//
//  SpendGoRewardService.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

public typealias SpendGoRewardSummaryCallback = (_ summary: SpendGoRewardSummary?, _ error: NSError?) -> Void

open class SpendGoRewardService {
    
    open class func rewardSummary(_ spendGoId: String, callback: @escaping SpendGoRewardSummaryCallback) {
        let parameters = [
            "spendgo_id": spendGoId
        ]
        SpendGoService.post("/rewardsAndOffers", parameters: parameters as SpendGoJSONDictonary, needsAuthToken: true) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            callback(SpendGoRewardSummary(json: response), nil)
        }
    }

}
