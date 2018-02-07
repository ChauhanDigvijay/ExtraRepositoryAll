//
//  OloUser.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/22/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

//  OLO User Response
//
//    {
//        firstname (string): ,
//        lastname (string): ,
//        emailaddress (string): ,
//        authtoken (string): ,
//        contactnumber (string): ,
//        reference (string, optional): can be used to associate an external Id,
//        basketid (string, optional): if specified will link the specified basket to this user
//    }

import UIKit
import SwiftyJSON

public struct OloUser {
   
    public var authToken: String?
    public var firstName: String
    public var lastName: String
    public var emailAddress: String
    public var contactNumber: String?
    public var reference: String?
    public var baskedId: String?
    public var cardSuffix: String?

    public init(json: JSON) {
        authToken      = json["authtoken"].string
        firstName      = json["firstname"].stringValue
        lastName       = json["lastname"].stringValue
        emailAddress   = json["emailaddress"].stringValue
        contactNumber  = json["contactnumber"].string
        reference      = json["reference"].string
        baskedId       = json["basketid"].string
        cardSuffix     = json["cardsuffix"].string
    }
    
    public init(firstName: String, lastName: String, emailAddress: String) {
        self.firstName = firstName
        self.lastName = lastName
        self.emailAddress = emailAddress
    }
    
}
