//
//  BillingAccount.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/23/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct UserSavedCreditCard {

    var accountId: Int64
    var cardType: String
    var cardSuffix: String
    var expiration: String
    var cardholderName: String? {
        return UserService.sharedUser?.fullName
    }

    init(accountId: Int64, cardType: String, cardSuffix: String, expiration: String) {
        self.accountId  = accountId
        self.cardType   = cardType
        self.cardSuffix = cardSuffix
        self.expiration = expiration
    }

    init(oloBillingAccount: OloBillingAccount) {
        accountId   = oloBillingAccount.accountId
        cardType    = oloBillingAccount.cardType
        cardSuffix  = oloBillingAccount.cardSuffix
        expiration  = oloBillingAccount.expiration
    }

}
