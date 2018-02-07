//
//  FishbowlCustomerModalClass.swift
//  JambaJuice
//
//  Created by VT016 on 09/02/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation

public struct FishbowlCustomerModalClass{
    
    var firstName: String
    var lastName: String
    var email: String
    var phone: String
    var smsOptIn: String
    var emailOptIn: String
    var addressStreet: String
    var addressCity: String
    var addressZipCode: String
    var favoriteStore: String
    var dob: String
    var gender: String
    var username: String
    var deviceId: String
    var sendWelcomeEmail: String    = ""
    var loginID: String
    var storeCode: String
    var pushToken:String            = ""
    var pushOptIn:String
    
    public init(firstName:String, lastName:String, email:String, phone:String, smsOptIn: String, emailOptIn:String, addressStreet:String, addressCity:String, addressZipCode:String, favoriteStore:String, dob: String, gender:String, username:String, deviceId:String, loginID:String, storeCode:String, pushToken:String, pushOptIn:String){
        
        self.firstName      = firstName
        self.lastName       = lastName
        self.email          = email
        self.phone          = phone
        self.smsOptIn       = smsOptIn
        self.emailOptIn     = emailOptIn
        self.addressStreet  = addressStreet
        self.addressCity    = addressCity
        self.addressZipCode = addressZipCode
        self.favoriteStore  = favoriteStore
        self.dob            = dob
        self.gender         = gender
        self.username       = username
        self.deviceId       = deviceId
        self.loginID        = loginID
        self.storeCode      = storeCode
        self.pushToken      = pushToken
        self.pushOptIn      = pushOptIn
    }
    
    public func serializeAsJSONDictionary() -> FishbowlJSONDictionary{
        var jsonDict = FishbowlJSONDictionary()
        jsonDict["firstName"]       = firstName as AnyObject?
        jsonDict["lastName"]        = lastName as AnyObject?
        jsonDict["email"]           = email as AnyObject?
        jsonDict["phone"]           = phone as AnyObject?
        jsonDict["smsOptIn"]        = smsOptIn as AnyObject?
        jsonDict["emailOptIn"]      = emailOptIn as AnyObject?
        jsonDict["addressStreet"]   = addressStreet as AnyObject?
        jsonDict["addressCity"]     = addressCity as AnyObject?
        jsonDict["addressZipCode"]  = addressZipCode as AnyObject?
        jsonDict["favoriteStore"]   = favoriteStore as AnyObject?
        jsonDict["dob"]             = dob as AnyObject?
        jsonDict["gender"]          = gender as AnyObject?
        jsonDict["username"]        = username as AnyObject?
        jsonDict["deviceId"]        = deviceId as AnyObject?
        jsonDict["loginID"]         = loginID as AnyObject?
        jsonDict["storeCode"]       = storeCode as AnyObject?
        jsonDict["pushToken"]       = pushToken as AnyObject?
        jsonDict["pushOptIn"]       = pushOptIn as AnyObject?
        return jsonDict
    }
}
