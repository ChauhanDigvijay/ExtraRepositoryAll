//
//  InCommBrand.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/17/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommBrand {

    //NOTE: THIS CLASS ONLY INCLUDES NEEDED PROPS. FOR FULL LIST SEE DOCUMENTATION
    public var id: String
    public var cardImages: [InCommBrandCardImage]
    public var creditCardTypesAndImages: [InCommCreditCardTypeAndImage]
    public var supportsVariableAmountDenominations: Bool
    //
    public var variableAmountDenominationMaximumValue: Double?
    public var variableAmountDenominationMinimumValue: Double?
    public var billingCountries: [InCommBillingCountries]
    public var billingStates: [InCommBillingStates]
    public var quantities: [Int32]

    public init(json: JSON) {
        id                                     = json["Id"].stringValue
        cardImages                             = json["CardImages"].arrayValue.map { InCommBrandCardImage(json: $0) }
        creditCardTypesAndImages               = json["CreditCardTypes"].arrayValue.map { InCommCreditCardTypeAndImage(json: $0) }
        supportsVariableAmountDenominations    = json["SupportsVariableAmountDenominations"].boolValue
        //
        variableAmountDenominationMaximumValue = json["VariableAmountDenominationMaximumValue"].double
        variableAmountDenominationMinimumValue = json["VariableAmountDenominationMinimumValue"].double
        
        billingCountries                       = json["BillingCountries"].arrayValue.map{ InCommBillingCountries(json: $0)}
        billingStates                          = json["BillingStates"].arrayValue.map{InCommBillingStates(json: $0)}
        quantities                             = json["Quantities"].arrayValue.map{($0.int32)!}
    }
    
}