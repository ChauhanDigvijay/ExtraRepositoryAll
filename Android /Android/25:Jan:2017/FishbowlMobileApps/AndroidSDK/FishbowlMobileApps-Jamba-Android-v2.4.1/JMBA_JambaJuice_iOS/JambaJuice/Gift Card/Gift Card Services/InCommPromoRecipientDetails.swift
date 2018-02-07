//
//  InCommPromoRecipientDetails.swift
//  JambaJuice
//
//  Created by VT010 on 10/25/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommPromoRecipientDetails {
    public var firstName: String
    public var lastName: String
    public var country: String?
    public var emailAddress: String?

    
    public init(firstName: String, lastName: String, emailAddress: String,country:String ) {
        self.firstName = firstName
        self.lastName  = lastName
        self.emailAddress = emailAddress
        self.country = country
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict                     = InCommJSONDictionary()
        jsonDict["FirstName"]            = firstName as AnyObject?
        jsonDict["LastName"]             = lastName as AnyObject?
        jsonDict["Country"]              = country as AnyObject?
        jsonDict["EmailAddress"]         = emailAddress as AnyObject?
        return jsonDict
    }
}
