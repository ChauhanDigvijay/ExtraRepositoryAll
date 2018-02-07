//
//  SignUpUserInfo.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/9/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

struct SignUpUserInfo {
    
    var emailAddress: String?
    var firstName: String?
    var lastName: String?
    var phoneNumber: String?
    var birthdate: Date?
    var enrollForEmailUpdates: Bool = true // SpendGo default is Opt-out
    var enrollForTextUpdates: Bool = false // SpendGo default is Opt-in
    var enrollForPushUpdates: Bool = false // SpendGo default is Opt-in
    var favoriteStoreCode: String?
    var password: String?

}
