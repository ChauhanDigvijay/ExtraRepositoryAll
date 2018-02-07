//
//  InCommUserPaymentAccountDetails.swift
//  InCommSDK
//
//  Created by vThink on 8/16/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommUserPaymentAccountDetails{
    public var firstName:String!
    public var lastName:String!
    public var streetAddress1:String!
    public var streetAddress2:String?
    public var city:String!
    public var stateProvince:String!
    public var country:String!
    public var zipPostalCode:String!
    public var creditCardExpirationMonth:String!
    public var creditCardNumber:String!
    public var creditCardVerificationCode:String!
    public var creditCardTypeCode:String!
    public var creditCardExpirationYear:String!
    public var userId:Int32!
    public var paymentGatewayToken:String?
    public var middleName:String?
    public var id:Int32?
    public var clientIpAddress:String?
    public var name:String?
    
    public init(firstName: String!, lastName: String!, streetAddress1: String!, streetAddress2: String?, city: String!, stateProvince: String!, country: String!, zipPostalCode:String!, creditCardExpirationMonth: String!, creditCardNumber: String!, creditCardVerificationCode:String!, creditCardTypeCode:String!, creditCardExpirationYear:String!, userId:Int32!){
        
        self.firstName                  = firstName
        self.lastName                   = lastName
        self.streetAddress1             = streetAddress1
        self.streetAddress2             = streetAddress2
        self.city                       = city
        self.stateProvince              = stateProvince
        self.country                    = country
        self.zipPostalCode              = zipPostalCode
        self.creditCardExpirationMonth  = creditCardExpirationMonth
        self.creditCardNumber           = creditCardNumber
        self.creditCardVerificationCode = creditCardVerificationCode
        self.creditCardTypeCode         = creditCardTypeCode
        self.creditCardExpirationYear   = creditCardExpirationYear
        self.userId                     = userId
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict = InCommJSONDictionary()
        jsonDict["FirstName"] = firstName as AnyObject?
        jsonDict["LastName"]  = lastName as AnyObject?
        jsonDict["StreetAddress1"] = streetAddress1 as AnyObject?
        jsonDict["StreetAddress2"] = streetAddress2 as AnyObject?
        jsonDict["City"] = city as AnyObject?
        jsonDict["StateProvince"] = stateProvince as AnyObject?
        jsonDict["Country"] = country as AnyObject?
        jsonDict["ZipPostalCode"] = zipPostalCode as AnyObject?
        jsonDict["CreditCardExpirationMonth"] = creditCardExpirationMonth as AnyObject?
        jsonDict["CreditCardNumber"] = creditCardNumber as AnyObject?
        jsonDict["CreditCardVerificationCode"] = creditCardVerificationCode as AnyObject?
        jsonDict["CreditCardTypeCode"] = creditCardTypeCode as AnyObject?
        jsonDict["CreditCardExpirationYear"] = creditCardExpirationYear as AnyObject?
        jsonDict["UserId"]          = userId != nil ? NSNumber(value: userId! as Int32) : nil
        jsonDict["PaymentGatewayToken"] = paymentGatewayToken as AnyObject?
        jsonDict["MiddleName"] = middleName as AnyObject?
        jsonDict["Id"]         = id as AnyObject?
        jsonDict["ClientIpAddress"] = clientIpAddress as AnyObject?
        jsonDict["Name"] = name as AnyObject?
        return jsonDict
    }
}
