//
//  InCommUserProfileDetails.swift
//  InCommSDK
//
//  Created by VT010 on 11/19/16.
//  Copyright © 2016 Hathway. All rights reserved.
//

import Foundation

public struct InCommUserProfileDetails{
    
    public var firstName:String
    public var lastName:String
    public var emailAddress:String
    public var id:Int32
    
    public init(firstName:String, lastName:String, emailAddress:String, id:Int32){
        self.firstName      = firstName
        self.lastName       = lastName
        self.emailAddress   = emailAddress
        self.id = id
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict = InCommJSONDictionary()
        jsonDict["FirstName"]           = firstName as AnyObject?
        jsonDict["LastName"]            = lastName as AnyObject?
        jsonDict["EmailAddress"]        = emailAddress as AnyObject?
        jsonDict["Id"]                  = NSNumber.init(value: id as Int32)
        jsonDict["IsActive"]            = true as AnyObject?
        return jsonDict
    }
}
