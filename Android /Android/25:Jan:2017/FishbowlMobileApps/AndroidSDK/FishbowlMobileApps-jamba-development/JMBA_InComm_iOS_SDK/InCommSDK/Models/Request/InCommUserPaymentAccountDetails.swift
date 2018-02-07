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
    public var orderPaymentMethod:InCommOrderPaymentMethod!
    
    public init(firstName: String!, lastName: String!, streetAddress1: String!, streetAddress2: String?, city: String!, stateProvince: String!, country: String!, zipPostalCode:String!, creditCardExpirationMonth: String!, creditCardNumber: String!, creditCardVerificationCode:String!, creditCardTypeCode:String!, creditCardExpirationYear:String!, orderPaymentMethod:InCommOrderPaymentMethod!){
        
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
        self.orderPaymentMethod         = orderPaymentMethod
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict = InCommJSONDictionary()
        
        jsonDict["FirstName"] = firstName
        jsonDict["LastName"]  = lastName
        jsonDict["StreetAddress1"] = streetAddress1
        jsonDict["StreetAddress2"] = streetAddress2
        jsonDict["City"] = city
        jsonDict["StateProvince"] = stateProvince
        jsonDict["Country"] = country
        jsonDict["ZipPostalCode"] = zipPostalCode
        jsonDict["CreditCardExpirationMonth"] = creditCardExpirationMonth
        jsonDict["CreditCardNumber"] = creditCardNumber
        jsonDict["CreditCardVerificationCode"] = creditCardVerificationCode
        jsonDict["CreditCardTypeCode"] = creditCardTypeCode
        jsonDict["CreditCardExpirationYear"] = creditCardExpirationYear
        jsonDict["OrderPaymentMethod"] = orderPaymentMethod.rawValue

        return jsonDict
    }
}