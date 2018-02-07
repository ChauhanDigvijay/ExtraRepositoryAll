//
//  InCommUserPaymentAccount.swift
//  InCommSDK
//
//  Created by vThink on 8/11/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommUserPaymentAccount{
    
    public var id: Int32
    public var userId:Int32
    public var name:String?
    public var paymentGatewayToken:String
    public var firstName:String
    public var middleName:String?
    public var lastName:String
    public var streetAddress1:String
    public var streetAddress2:String?
    public var city:String
    public var stateProvince:String
    public var country:String
    public var zipPostalCode:String
    public var creditCardNumber:String
    public var creditCardTypeCode:String
    public var creditCardVerificationCode:String?
    public var creditCardExpirationMonth:UInt16
    public var creditCardExpirationYear:UInt16
    public var clientIpAddress:String?
    
    public init(json: JSON){
        id                         = json["Id"].int32Value
        userId                     = json["UserId"].int32Value
        name                       = json["Name"].string
        paymentGatewayToken        = json["PaymentGatewayToken"].stringValue
        firstName                  = json["FirstName"].stringValue
        middleName                 = json["MiddleName"].string
        lastName                   = json["LastName"].stringValue
        streetAddress1             = json["StreetAddress1"].stringValue
        streetAddress2             = json["StreetAddress2"].string
        city                       = json["City"].stringValue
        stateProvince              = json["StateProvince"].stringValue
        country                    = json["Country"].stringValue
        zipPostalCode              = json["ZipPostalCode"].stringValue
        creditCardNumber           = json["CreditCardNumber"].stringValue
        creditCardTypeCode         =  json["CreditCardTypeCode"].stringValue
        creditCardVerificationCode = json["CreditCardVerificationCode"].string
        creditCardExpirationMonth  = json["CreditCardExpirationMonth"].uInt16Value
        creditCardExpirationYear   = json["CreditCardExpirationYear"].uInt16Value
        clientIpAddress            = json["ClientIpAddress"].stringValue
    }
    
}
