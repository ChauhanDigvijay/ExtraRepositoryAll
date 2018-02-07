//
//  InCommSubmitPayment.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommSubmitPayment {

    public var amount: Double
    public var city: String?
    public var country: String?
    public var creditCardExpirationMonth: UInt16?
    public var creditCardExpirationYear: UInt16?
    public var creditCardNumber: String?
    public var creditCardVerificationCode: String?
    public var firstName: String?
    public var lastName: String?
    public var paymentAccountId: Int32?
    public var paymentReceived: Bool?
    public var stateProvince: String?
    public var streetAddress1: String?
    public var streetAddress2: String?
    public var vestaOrgId: String?
    public var vestaWebSessionId: String?
    public var zipPostalCode: String?
    public var mobileDevice: InCommOrderPaymentMobileDevice?
    public var creditCardType: String?
    public var orderPaymentMethod: InCommOrderPaymentMethod?
    
    public init(amount: Double, orderPaymentMethod: InCommOrderPaymentMethod = .CreditCard, firstName: String? = nil, lastName: String? = nil, streetAddress1: String? = nil, streetAddress2: String? = nil, city: String? = nil, stateProvince: String? = nil, country: String? = nil, zipPostalCode: String? = nil, creditCardNumber: String? = nil, creditCardVerificationCode: String? = nil, creditCardExpirationMonth: UInt16? = nil, creditCardExpirationYear: UInt16? = nil, creditCardType: String?) {
        self.amount = amount
        self.orderPaymentMethod = orderPaymentMethod
        if orderPaymentMethod == .CreditCard {
            assert(firstName != nil, "firstName should be provided for payment method")
            assert(lastName != nil, "lastName should be provided for payment method")
            assert(streetAddress1 != nil, "streetAddress1 should be provided for payment method")
            assert(city != nil, "city should be provided for payment method")
            assert(stateProvince != nil, "stateProvince should be provided for payment method")
            assert(country != nil, "country should be provided for payment method")
            assert(zipPostalCode != nil, "zipPostalCode should be provided for payment method")
            assert(creditCardNumber != nil, "creditCardNumber should be provided for payment method")
            assert(creditCardVerificationCode != nil, "creditCardVerificationCode should be provided for payment method")
            assert(creditCardExpirationMonth != nil, "creditCardExpirationMonth should be provided for payment method")
            assert(creditCardExpirationYear != nil, "creditCardExpirationYear should be provided for payment method")
            assert(creditCardType != nil, "creditCardType should be provided for payment method")
        }
        self.firstName = firstName
        self.lastName = lastName
        self.streetAddress1 = streetAddress1
        self.streetAddress2 = streetAddress2
        self.city = city
        self.stateProvince = stateProvince
        self.country = country
        self.zipPostalCode = zipPostalCode
        self.creditCardNumber = creditCardNumber
        self.creditCardVerificationCode = creditCardVerificationCode
        self.creditCardExpirationMonth = creditCardExpirationMonth
        self.creditCardExpirationYear = creditCardExpirationYear
        self.creditCardType = creditCardType
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict                           = InCommJSONDictionary()
        jsonDict["Amount"]                     = amount as AnyObject?
        jsonDict["City"]                       = city as AnyObject?
        jsonDict["Country"]                    = country as AnyObject?
        jsonDict["CreditCardExpirationMonth"]  = creditCardExpirationMonth != nil ? NSNumber(value: creditCardExpirationMonth! as UInt16) : nil
        jsonDict["CreditCardExpirationYear"]   = creditCardExpirationYear != nil ? NSNumber(value: creditCardExpirationYear! as UInt16) : nil
        jsonDict["CreditCardNumber"]           = creditCardNumber as AnyObject?
        jsonDict["CreditCardVerificationCode"] = creditCardVerificationCode as AnyObject?
        jsonDict["FirstName"]                  = firstName as AnyObject?
        jsonDict["LastName"]                   = lastName as AnyObject?
        jsonDict["PaymentAccountId"]           = paymentAccountId != nil ? NSNumber(value: paymentAccountId! as Int32) : nil
        jsonDict["PaymentReceived"]            = false
        jsonDict["StateProvince"]              = stateProvince as AnyObject?
        jsonDict["StreetAddress1"]             = streetAddress1 as AnyObject?
        jsonDict["StreetAddress2"]             = streetAddress2 as AnyObject?
        jsonDict["VestaOrgId"]                 = vestaOrgId as AnyObject?
        jsonDict["VestaWebSessionId"]          = vestaWebSessionId as AnyObject?
        jsonDict["ZipPostalCode"]              = zipPostalCode as AnyObject?
        jsonDict["MobileDevice"]               = mobileDevice?.serializeAsJSONDictionary() as AnyObject?
        jsonDict["CreditCardType"]             = creditCardType as AnyObject?
        jsonDict["OrderPaymentMethod"]         = orderPaymentMethod?.rawValue as AnyObject?
        return jsonDict
    }
}
