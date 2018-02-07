//
//  InCommSDKTestsConfigurations.swift
//  InCommSDK
//
//  Created by Taha Samad on 6/08/2015.
//  Copyright (c) 2015 Fishbowl, Inc. All rights reserved.
//

import Foundation
import InCommSDK

class InCommSDKTestsConfigurations : NSObject {
    
    static let sharedInCommSDKTestsConfigurations = InCommSDKTestsConfigurations()
    
    fileprivate(set) var InCommBaseURL: String
    fileprivate(set) var InCommClientId: String
    fileprivate(set) var InCommApiKey: String
    fileprivate(set) var InCommUserEmailAddress: String
    fileprivate(set) var InCommUserCreditCardNumber: String
    fileprivate(set) var InCommUserCreditCardExpiryMonth: UInt16
    fileprivate(set) var InCommUserCreditCardExpiryYear: UInt16
    fileprivate(set) var InCommUserCreditCardCVV: String
    fileprivate(set) var InCommUserCreditCardZip: String
    fileprivate(set) var InCommUserCreditCardType: String
    fileprivate(set) var InCommNoFundsCollected: Bool
    fileprivate(set) var InCommTestCardId: Int32
    fileprivate(set) var InCommTestCardNumber: String
    fileprivate(set) var InCommTestCardPin: String
    fileprivate(set) var InCommTestBrandId: String
    fileprivate(set) var InCommTestMessageFrom: String
    fileprivate(set) var InCommTestMessageTo: String
    fileprivate(set) var InCommTestMessageText: String
    
    override fileprivate init() {
        
        let inCommAPISecreteKey = "UKGvqTkD6qxkSIWAvuzWTkMe8is+WJ8sAaWUehkHqwE="
        let inCommAPISandBoxURL = "https://api.giftango.com/enterpriseapi/v1"
        let inCommClientId      = "jambajuicemobileapplicationtest"
        let inCommSampleSpendGoUserId      = "3634177"
        
        InCommBaseURL = inCommAPISandBoxURL
        InCommClientId = inCommClientId
        InCommApiKey = InCommUserService.getUserInCommAuthToken(inCommSampleSpendGoUserId, secretKey: inCommAPISecreteKey)
     
        
        
        let bundle = Bundle(for: type(of: self))
        let path = bundle.path(forResource: "Info", ofType: "plist")
        let infoDict = NSDictionary(contentsOfFile: path!)!
        let InCommSDKTestsConfigurationsDict = infoDict["InCommSDKTestsConfigurations"] as! NSDictionary
   //     InCommBaseURL = InCommSDKTestsConfigurationsDict["InCommBaseURL"] as! String
   //     InCommClientId = InCommSDKTestsConfigurationsDict["InCommClientId"] as! String
   //     InCommApiKey = InCommSDKTestsConfigurationsDict["InCommApiKey"] as! String
        InCommUserEmailAddress = InCommSDKTestsConfigurationsDict["InCommUserEmailAddress"] as! String
        InCommUserCreditCardNumber = InCommSDKTestsConfigurationsDict["InCommUserCreditCardNumber"] as! String
        InCommUserCreditCardExpiryMonth = (InCommSDKTestsConfigurationsDict["InCommUserCreditCardExpiryMonth"] as! NSNumber).uint16Value
        InCommUserCreditCardExpiryYear = (InCommSDKTestsConfigurationsDict["InCommUserCreditCardExpiryYear"] as! NSNumber).uint16Value
        InCommUserCreditCardCVV = InCommSDKTestsConfigurationsDict["InCommUserCreditCardCVV"] as! String
        InCommUserCreditCardZip = InCommSDKTestsConfigurationsDict["InCommUserCreditCardZip"] as! String
        InCommUserCreditCardType = InCommSDKTestsConfigurationsDict["InCommUserCreditCardType"] as! String
        InCommNoFundsCollected = InCommSDKTestsConfigurationsDict["InCommNoFundsCollected"] as! Bool
        InCommTestCardId = (InCommSDKTestsConfigurationsDict["InCommTestCardId"] as! NSNumber).int32Value
        InCommTestCardNumber = InCommSDKTestsConfigurationsDict["InCommTestCardNumber"] as! String
        InCommTestCardPin = InCommSDKTestsConfigurationsDict["InCommTestCardPin"] as! String
        InCommTestBrandId = InCommSDKTestsConfigurationsDict["InCommTestBrandId"] as! String
        InCommTestMessageFrom = InCommSDKTestsConfigurationsDict["InCommTestMessageFrom"] as! String
        InCommTestMessageTo = InCommSDKTestsConfigurationsDict["InCommTestMessageTo"] as! String
        InCommTestMessageText = InCommSDKTestsConfigurationsDict["InCommTestMessageText"] as! String 
    }
    
}
