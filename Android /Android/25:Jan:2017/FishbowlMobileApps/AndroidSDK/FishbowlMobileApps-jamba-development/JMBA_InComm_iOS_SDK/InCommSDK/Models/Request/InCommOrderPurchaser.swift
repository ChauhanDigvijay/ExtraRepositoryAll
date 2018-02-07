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
        jsonDict["Country"]                  = country
        jsonDict["EmailAddress"]             = emailAddress
        jsonDict["FirstName"]                = firstName
        jsonDict["LastName"]                 = lastName
        //
        jsonDict["City"]                     = city
        jsonDict["CompanyName"]              = companyName
        jsonDict["MobilePhoneNumber"]        = mobilePhoneNumber
        jsonDict["PhoneNumber"]              = phoneNumber
        jsonDict["SendDeliveryEmailAlert"]   = sendDeliveryEmailAlert
        jsonDict["SendDeliveryTextAlert"]    = sendDeliveryTextAlert
        jsonDict["SendViewEmailAlert"]       = sendViewEmailAlert
        jsonDict["SendViewTextAlert"]        = sendViewTextAlert
        jsonDict["StateProvince"]            = stateProvince
        jsonDict["StreetAddress1"]           = streetAddress1
        jsonDict["StreetAddress2"]           = streetAddress2
        jsonDict["SuppressReceiptEmail"]     = suppressReceiptEmail
        jsonDict["UserId"]                   = userId != nil ? NSNumber(int: userId!) : nil
        jsonDict["ZipPostalCode"]            = zipPostalCode
        return jsonDict
    }
    
}

