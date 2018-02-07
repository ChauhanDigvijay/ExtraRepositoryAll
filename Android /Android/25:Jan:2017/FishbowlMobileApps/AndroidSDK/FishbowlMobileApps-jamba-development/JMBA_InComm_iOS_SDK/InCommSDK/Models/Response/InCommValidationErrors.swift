//
//  InCommValidationErrors.swift
//  InCommSDK
//
//  Created by vThink on 9/2/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommValidationErrors{
    public var creditCardNumber:[String]
    public var orderPaymentAmount:[String]
    public var streetAddress1:[String]
    public var stateProvinceCode:[String]
    public var creditCardExpirationYear:[String]
    public var creditCardExpirationMonth:[String]
    public init(json:JSON){
        creditCardNumber = json["CreditCardNumber"].arrayValue.map{($0.string)!}
        orderPaymentAmount = json["Order.Payment.Amount"].arrayValue.map{($0.string)!}
        streetAddress1 = json["StreetAddress1"].arrayValue.map{($0.string)!}
        stateProvinceCode = json["StateProvinceCode"].arrayValue.map{($0.string)!}
        creditCardExpirationYear = json["CreditCardExpirationYear"].arrayValue.map{($0.string)!}
        creditCardExpirationMonth = json["CreditCardExpirationMonth"].arrayValue.map{($0.string)!}
    }
}
