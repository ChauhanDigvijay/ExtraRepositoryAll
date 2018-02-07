//
//  OloSDKTestsConfigurations.swift
//  Olo
//
//  Created by Taha Samad on 15/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

class OloSDKTestsConfigurations : NSObject {
    
    static let sharedOloSDKTestsConfigurations = OloSDKTestsConfigurations()
    
    fileprivate(set) var OloAPIKey: String
    fileprivate(set) var OloBaseURL: String
    fileprivate(set) var OloUserEmailAddress: String
    fileprivate(set) var OloUserPassword: String
    fileprivate(set) var OloUserFirstName: String
    fileprivate(set) var OloUserLastName: String
    fileprivate(set) var OloUserPhoneNumber: String
    fileprivate(set) var OloUserCreditCardNumber: String
    fileprivate(set) var OloUserCreditCardExpiryMonth: String
    fileprivate(set) var OloUserCreditCardExpiryYear: String
    fileprivate(set) var OloUserCreditCardCVV: String
    fileprivate(set) var OloUserCreditCardZip: String
    fileprivate(set) var OloUserBasketId: String
    fileprivate(set) var OloCouponCode: String
    
    override fileprivate init() {
        let bundle = Bundle(for: type(of: self))
        let path = bundle.path(forResource: "Info", ofType: "plist")
        let infoDict = NSDictionary(contentsOfFile: path!)!
        let OloSDKTestsConfigurationsDict = infoDict["OloSDKTestsConfigurations"] as! NSDictionary
        OloAPIKey = OloSDKTestsConfigurationsDict["OloAPIKey"] as! String
        OloBaseURL = OloSDKTestsConfigurationsDict["OloBaseURL"] as! String
        OloUserEmailAddress = OloSDKTestsConfigurationsDict["OloUserEmailAddress"] as! String
        OloUserPassword = OloSDKTestsConfigurationsDict["OloUserPassword"] as! String
        OloUserFirstName = OloSDKTestsConfigurationsDict["OloUserFirstName"] as! String
        OloUserLastName = OloSDKTestsConfigurationsDict["OloUserLastName"] as! String
        OloUserPhoneNumber = OloSDKTestsConfigurationsDict["OloUserPhoneNumber"] as! String
        OloUserCreditCardNumber = OloSDKTestsConfigurationsDict["OloUserCreditCardNumber"] as! String
        OloUserCreditCardExpiryMonth = OloSDKTestsConfigurationsDict["OloUserCreditCardExpiryMonth"] as! String
        OloUserCreditCardExpiryYear = OloSDKTestsConfigurationsDict["OloUserCreditCardExpiryYear"] as! String
        OloUserCreditCardCVV = OloSDKTestsConfigurationsDict["OloUserCreditCardCVV"] as! String
        OloUserCreditCardZip = OloSDKTestsConfigurationsDict["OloUserCreditCardZip"] as! String
        OloUserBasketId = OloSDKTestsConfigurationsDict["OloUserBasketId"] as! String
        OloCouponCode = OloSDKTestsConfigurationsDict["OloCouponCode"] as! String
    }
    
}
