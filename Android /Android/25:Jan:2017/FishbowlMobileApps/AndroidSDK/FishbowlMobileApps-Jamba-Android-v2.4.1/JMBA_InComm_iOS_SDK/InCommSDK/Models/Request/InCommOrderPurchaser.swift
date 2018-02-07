//
//  InCommOrderPurchaser.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommOrderPurchaser {

    public var country: String
    public var emailAddress: String
    public var firstName: String
    public var lastName: String
    //
    public var city: String?
    public var companyName: String?
    public var mobilePhoneNumber: String?
    public var phoneNumber: String?
    public var sendDeliveryEmailAlert: Bool?
    public var sendDeliveryTextAlert: Bool?
    public var sendViewEmailAlert: Bool?
    public var sendViewTextAlert: Bool?
    public var stateProvince: String?
    public var streetAddress1: String?
    public var streetAddress2: String?
    public var suppressReceiptEmail: Bool?
    public var userId: Int32?
    public var zipPostalCode: String?
    
    public init(firstName: String, lastName: String, emailAddress: String, country: String) {
        self.firstName    = firstName
        self.lastName     = lastName
        self.emailAddress = emailAddress
        self.country      = country
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict                         = InCommJSONDictionary()
        jsonDict["Country"]                  = country as AnyObject?
        jsonDict["EmailAddress"]             = emailAddress as AnyObject?
        jsonDict["FirstName"]                = firstName as AnyObject?
        jsonDict["LastName"]                 = lastName as AnyObject?
        //
        jsonDict["City"]                     = city as AnyObject?
        jsonDict["CompanyName"]              = companyName as AnyObject?
        jsonDict["MobilePhoneNumber"]        = mobilePhoneNumber as AnyObject?
        jsonDict["PhoneNumber"]              = phoneNumber as AnyObject?
        // Add default values
        jsonDict["SendDeliveryEmailAlert"]   = false
        jsonDict["SendDeliveryTextAlert"]    = false
        jsonDict["SendViewEmailAlert"]       = false
        jsonDict["SendViewTextAlert"]        = false
        //
        jsonDict["StateProvince"]            = stateProvince as AnyObject?
        jsonDict["StreetAddress1"]           = streetAddress1 as AnyObject?
        jsonDict["StreetAddress2"]           = streetAddress2 as AnyObject?
        // Add Default Values
        jsonDict["SuppressReceiptEmail"]     = false
        //
        jsonDict["UserId"]                   = userId != nil ? NSNumber(value: userId! as Int32) : nil
        jsonDict["ZipPostalCode"]            = zipPostalCode as AnyObject?
        return jsonDict
    }
    
}

