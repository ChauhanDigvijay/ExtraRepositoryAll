//
//  Reward.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/19/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SpendGoSDK
import OloSDK

struct Reward {

    // Shared fields (Olo & SpendGo)
    var name: String
    var type: String
    var desc: String
    var quantity: Int
    var value: Double

    // Olo specific fields
    var rewardId: Int64?
    var reference: String?
    var applied: Bool
    var membershipId: Int64?

    init(spendGoReward: SpendGoReward) {
        name = spendGoReward.rewardTitle
        desc = spendGoReward.desc
        quantity = spendGoReward.quantity
        type = spendGoReward.type
        applied = false
        value = 0
    }

    init(oloReward: OloLoyaltyReward) {
        name = oloReward.label
        desc = ""
        quantity = 1
        value = oloReward.value
        type = oloReward.type
        rewardId = oloReward.rewardId
        reference = oloReward.reference
        applied = oloReward.applied
        membershipId = oloReward.membershipId
    }

}
