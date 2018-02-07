//
//  OloBasketSubmitBody.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloBasketSubmitBody {
    
    public var userType: String
    public var authToken: String?
    public var firstName: String?
    public var lastName: String?
    public var emailAddress: String?
    public var contactNumber: String?
    public var reference: String?
    
    public var billingMethod: String
    public var billingAccountId: String?
    public var cardNumber: String?
    public var expiryYear: String?
    public var expiryMonth: String?
    public var cvv: String?
    public var zip: String?
    public var saveOnFile: String?
    
    public var orderRef: String?
    
    public init() {
        userType = ""
        billingMethod = ""
    }
    
    public func validateValues() -> NSError? {
        if userType != "user" && userType != "guest" {
            //Invalid User Type
            return OloUtils.error("Invalid userType", code: 0)
        }
        if userType == "user" && (authToken == nil || authToken!.isEmpty) {
            //Auth token is required for user
            return OloUtils.error("AuthToken not found for userType = user", code: 0)
        }
        else if userType == "guest" {
            //Guest
            if firstName == nil || firstName!.isEmpty {
                return OloUtils.error("First name is necessary for userType = guest", code: 0)
            }
            else if lastName == nil || lastName!.isEmpty {
                return OloUtils.error("Last name is necessary for userType = guest", code: 0)
            }
            else if emailAddress == nil || emailAddress!.isEmpty {
                return OloUtils.error("Email address is necessary for userType = guest", code: 0)
            }
            else if contactNumber == nil || contactNumber!.isEmpty {
                return OloUtils.error("Contact phone number is necessary for userType = guest", code: 0)
            }
            else if reference == nil || reference!.isEmpty {
                return OloUtils.error("Reference is necessary for userType = guest", code: 0)
            }
        }
        if billingMethod != "billingaccount" && billingMethod != "cash" && billingMethod != "creditcard" {
            //Invalid Billing Method
            return OloUtils.error("Invalid billing method", code: 0)
        }
        if billingMethod == "billingaccount" && (billingAccountId == nil || billingAccountId!.isEmpty) {
            //Billing account Id is required.
            return OloUtils.error("Billing Account Id is requried for billing method 'billingaccount'", code: 0)
        }
        else if billingMethod == "creditcard" {
            //Credit Card
            if cardNumber == nil || cardNumber!.isEmpty {
                return OloUtils.error("Card number is requried for billing method 'creditcard'.", code: 0)
            }
            else if expiryYear == nil || Int(expiryYear!) < 1900 || Int(expiryYear!) > 2099 {
                return OloUtils.error("Expiry year is requried for billing method 'creditcard'.", code: 0)
            }
            else if expiryMonth == nil || Int(expiryMonth!) < 1 || Int(expiryMonth!) > 12 {
                return OloUtils.error("Expiry month is requried for billing method 'creditcard'.", code: 0)
            }
            else if cvv == nil || cvv!.isEmpty {
                return OloUtils.error("CVV is requried for billing method 'creditcard'.", code: 0)
            }
            else if zip == nil || zip!.isEmpty {
                return OloUtils.error("Zip is requried for billing method 'creditcard'.", code: 0)
            }
            else if saveOnFile == nil || (saveOnFile! != "true" && saveOnFile! != "false") {
                return OloUtils.error("Save On File is requried for billing method 'creditcard'.", code: 0)
            }
        }
        return nil
    }

    public func serializeAsJSONDictionary() -> OloJSONDictionary {
        var jsonDict = OloJSONDictionary()
        jsonDict["usertype"] = userType
        jsonDict["authtoken"] = authToken
        jsonDict["firstname"] = firstName
        jsonDict["lastname"] = lastName
        jsonDict["emailaddress"] = emailAddress
        jsonDict["contactnumber"] = contactNumber
        jsonDict["reference"] = reference
        jsonDict["billingmethod"] = billingMethod
        jsonDict["billingaccountid"] = billingAccountId
        jsonDict["cardnumber"] = cardNumber
        jsonDict["expiryyear"] = expiryYear
        jsonDict["expirymonth"] = expiryMonth
        jsonDict["cvv"] = cvv
        jsonDict["zip"] = zip
        jsonDict["saveonfile"] = saveOnFile
        jsonDict["orderref"] = orderRef
        return jsonDict
    }
    
}
