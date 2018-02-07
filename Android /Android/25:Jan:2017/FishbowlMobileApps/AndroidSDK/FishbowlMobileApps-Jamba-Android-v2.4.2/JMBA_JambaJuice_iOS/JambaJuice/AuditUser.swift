//
//  AuditUser.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 8/31/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

struct AuditUser {
    
    static let parseClassName = "AuditUser"
    
    fileprivate var user: User
    fileprivate var action: String
    
    init(user: User, action: String) {
        self.user = user
        self.action = action
    }
    
    func serializeAsParseObject() -> PFObject {
        let parseObject = PFObject(className: AuditUser.parseClassName)
        parseObject["id"]          = user.id 
        parseObject["email"]       = user.emailAddress ?? NSNull()
        parseObject["phone"]       = user.phoneNumber 
        parseObject["firstName"]   = user.firstName ?? NSNull()
        parseObject["lastName"]    = user.lastName ?? NSNull()
        parseObject["dateOfBirth"] = user.dateOfBirth ?? NSNull()
        parseObject["smsOptIn"]    = user.smsOptIn 
        parseObject["emailOptIn"]  = user.emailOptIn 
        parseObject["action"]      = action
        return parseObject
    }
    
}
