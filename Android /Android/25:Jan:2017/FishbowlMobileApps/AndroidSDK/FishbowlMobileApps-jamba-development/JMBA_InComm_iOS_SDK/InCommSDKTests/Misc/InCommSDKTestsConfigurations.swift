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
    
    private(set) var InCommBaseURL: String
    private(set) var InCommClientId: String
    private(set) var InCommApiKey: String
    private(set) var InCommUserEmailAddress: String
    private(set) var InCommUserCreditCardNumber: String
    private(set) var InCommUserCreditCardExpiryMonth: UInt16
    private(set) var InCommUserCreditCardExpiryYear: UInt16
    private(set) var InCommUserCreditCardCVV: String
    private(set) var InCommUserCreditCardZip: String
    private(set) var InCommUserCreditCardType: String
    private(set) var InCommNoFundsCollected: Bool
    private(set) var InCommTestCardId: Int32
    private(set) var InCommTestCardNumber: String
    private(set) var InCommTestCardPin: String
    private(set) var InCommTestBrandId: String
    private(set) var InCommTestMessageFrom: String
    private(set) var InCommTestMessageTo: String
    private(set) var InCommTestMessageText: String
    
    override private init() {
        
        let inCommAPISecreteKey = "UKGvqTkD6qxkSIWAvuzWTkMe8is+WJ8sAaWUehkHqwE="
        let inCommAPISandBoxURL = "https://api.giftango.com/enterpriseapi/v1"
        let inCommClientId      = "jambajuicemobileapplicationtest"
        let inCommSampleSpendGoUserId      = "3634177"
        
        InCommBaseURL = inCommAPISandBoxURL
        InCommClientId = inCommClientId
        InCommApiKey = InCommUserService.getUserInCommAuthToken(inCommSampleSpendGoUserId, secretKey: inCommAPISecreteKey)
     
        
        
        let bundle = NSBundle(forClass: self.dynamicType)
        let path = bundle.pathForResource("Info", ofType: "plist")
        let infoDict = NSDictionary(contentsOfFile: path!)!
        let InCommSDKTestsConfigurationsDict = infoDict["InCommSDKTestsConfigurations"] as! NSDictionary
   //     InCommBaseURL = InCommSDKTestsConfigurationsDict["InCommBaseURL"] as! String
   //     InCommClientId = InCommSDKTestsConfigurationsDict["InCommClientId"] as! String
   //     InCommApiKey = InCommSDKTestsConfigurationsDict["InCommApiKey"] as! String
        InCommUserEmailAddress = InCommSDKTestsConfigurationsDict["InCommUserEmailAddress"] as! String
        InCommUserCreditCardNumber = InCommSDKTestsConfigurationsDict["InCommUserCreditCardNumber"] as! String
        InCommUserCreditCardExpiryMonth = (InCommSDKTestsConfigurationsDict["InCommUserCreditCardExpiryMonth"] as! NSNumber).unsignedShortValue
        InCommUserCreditCardExpiryYear = (InCommSDKTestsConfigurationsDict["InCommUserCreditCardExpiryYear"] as! NSNumber).unsignedShortValue
        InCommUserCreditCardCVV = InCommSDKTestsConfigurationsDict["InCommUserCreditCardCVV"] as! String
        InCommUserCreditCardZip = InCommSDKTestsConfigurationsDict["InCommUserCreditCardZip"] as! String
        InCommUserCreditCardType = InCommSDKTestsConfigurationsDict["InCommUserCreditCardType"] as! String
        InCommNoFundsCollected = InCommSDKTestsConfigurationsDict["InCommNoFundsCollected"] as! Bool
        InCommTestCardId = (InCommSDKTestsConfigurationsDict["InCommTestCardId"] as! NSNumber).intValue
        InCommTestCardNumber = InCommSDKTestsConfigurationsDict["InCommTestCardNumber"] as! String
        InCommTestCardPin = InCommSDKTestsConfigurationsDict["InCommTestCardPin"] as! String
        InCommTestBrandId = InCommSDKTestsConfigurationsDict["InCommTestBrandId"] as! String
        InCommTestMessageFrom = InCommSDKTestsConfigurationsDict["InCommTestMessageFrom"] as! String
        InCommTestMessageTo = InCommSDKTestsConfigurationsDict["InCommTestMessageTo"] as! String
        InCommTestMessageText = InCommSDKTestsConfigurationsDict["InCommTestMessageText"] as! String 
    }
    
}