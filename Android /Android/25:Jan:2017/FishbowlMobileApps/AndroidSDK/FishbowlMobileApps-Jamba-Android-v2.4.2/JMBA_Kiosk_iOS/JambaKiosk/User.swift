//
//  User.swift
//  JambaJuice
//
//  Created by Taha Samad on 07/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK
import SpendGoSDK
import Parse
import HDK

struct User: Equatable {

    var userId: String
    var emailAddress: String
    var phoneNumber: String
    var firstName: String
    var lastName: String

    var fullName: String? {
        return "\(firstName) \(lastName)"
    }

    // Store both SpendGo and Olo Auth Tokens (Sprint 7)
    var spendgoAuthToken: String?
    var oloAuthToken: String?

    init(spendGoUser: SpendGoUser, spendGoAuthToken: String) {
        userId           = spendGoUser.id!
        spendgoAuthToken = spendGoAuthToken
        phoneNumber      = spendGoUser.phoneNumber
        emailAddress     = spendGoUser.emailAddress ?? ""
        firstName        = spendGoUser.firstName ?? ""
        lastName         = spendGoUser.lastName ?? ""
    }

}

// MARK: Equatable

func == (lhs: User, rhs: User) -> Bool {
    return lhs.phoneNumber == rhs.phoneNumber
}
