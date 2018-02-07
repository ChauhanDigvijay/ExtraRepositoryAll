//
//  SpendGoUser.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias SpendGoUserKeyValuePair = (key: String, value: String)

public struct SpendGoUser {
    
    public var id: String? //Optional only so we can create SpendGo User with out providing it
    public var phoneNumber: String
    public var smsOptIn: Bool
    public var emailAddress: String?
    public var emailOptIn: Bool?
    public var firstName: String?
    public var lastName: String?
    public var dateOfBirth: String?
    public var gender: String?
    public var maritalStatus: String?
    public var streetAddress: String?
    public var city: String?
    public var state: String?
    public var zip: String?
    public var additionalInfo: [SpendGoUserKeyValuePair]?
    public var favoriteStore: SpendGoStore? //Optional only so we can create SpendGoUser with specifying it.

    public init(json: JSON) {
        id                      = json["spendgo_id"].string
        phoneNumber             = json["phone"].stringValue
        smsOptIn                = json["sms_opt_in"].boolValue
        emailAddress            = json["email"].string
        emailOptIn              = json["email_opt_in"].bool
        firstName               = json["first_name"].string
        lastName                = json["last_name"].string
        dateOfBirth             = json["dob"].string
        gender                  = json["gender"].string
        maritalStatus           = json["marital_status"].string
        streetAddress           = json["street"].string
        city                    = json["city"].string
        state                   = json["state"].string
        zip                     = json["zip"].string
        if json["favorite_store"] != JSON.null {
            favoriteStore       = SpendGoStore(json: json["favorite_store"])    
        }
        let additionalInfoArray = json["addtl_info"].array
        if (additionalInfoArray != nil) {
            additionalInfo      = additionalInfoArray!.map { (key: $0["name"].stringValue, value: $0["value"].stringValue) }
        }
    }
    
    public init(emailAddress: String, firstName: String, lastName: String, phoneNumber: String, enrollForEmailUpdates: Bool, enrollForTextUpdates: Bool, dateOfBirth: String) {
        self.emailAddress = emailAddress
        self.firstName = firstName
        self.lastName = lastName
        self.phoneNumber = phoneNumber
        self.emailOptIn = enrollForEmailUpdates
        self.smsOptIn = enrollForTextUpdates
        self.dateOfBirth = dateOfBirth
    }
    
    public func serializeAsJSONDictionary() -> SpendGoJSONDictonary {
        var jsonDict = SpendGoJSONDictonary()
        jsonDict["spendgo_id"] = id as AnyObject?
        jsonDict["phone"] = phoneNumber as AnyObject?
        jsonDict["sms_opt_in"] = smsOptIn as AnyObject?
        jsonDict["email"] = emailAddress as AnyObject?
        jsonDict["email_opt_in"] = emailOptIn as AnyObject?
        jsonDict["first_name"] = firstName as AnyObject?
        jsonDict["last_name"] = lastName as AnyObject?
        jsonDict["dob"] = dateOfBirth as AnyObject?
        jsonDict["gender"] = gender as AnyObject?
        jsonDict["marital_status"] = maritalStatus as AnyObject?
        jsonDict["street"] = streetAddress as AnyObject?
        jsonDict["city"] = city as AnyObject?
        jsonDict["state"] = state as AnyObject?
        jsonDict["zip"] = zip as AnyObject?
        if favoriteStore != nil {
            jsonDict["favorite_store"] = favoriteStore!.serializeAsJSONDictionary() as AnyObject?
        }
        if additionalInfo != nil {
            var additionalInfoDictArray = [[String : String]]()
            for (key, value) in additionalInfo! {
                additionalInfoDictArray.append(["name": key, "value": value])
            }
            jsonDict["addtl_info"] = additionalInfoDictArray as AnyObject?
        }
        return jsonDict
    }

}
