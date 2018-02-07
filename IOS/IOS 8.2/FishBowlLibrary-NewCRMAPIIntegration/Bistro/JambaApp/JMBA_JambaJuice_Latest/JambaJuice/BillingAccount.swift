//
//  BillingAccount.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/23/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct BillingAccount {
    
    var accountId: Int64
    var accountType: String
    var cardType: String
    var cardSuffix: String
    var desc: String
    var expiration: String
    
    init(oloBillingAccount: OloBillingAccount) {
        accountId   = oloBillingAccount.accountId
        accountType = oloBillingAccount.accountType
        cardType    = oloBillingAccount.cardType
        cardSuffix  = oloBillingAccount.cardSuffix
        desc        = oloBillingAccount.desc
        expiration  = oloBillingAccount.expiration
    }
}
