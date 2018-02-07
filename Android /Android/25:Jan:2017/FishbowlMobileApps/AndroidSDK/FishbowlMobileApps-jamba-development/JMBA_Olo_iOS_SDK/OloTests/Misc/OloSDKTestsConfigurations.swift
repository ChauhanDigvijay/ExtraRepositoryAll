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
    
    private(set) var OloAPIKey: String
    private(set) var OloBaseURL: String
    private(set) var OloUserEmailAddress: String
    private(set) var OloUserPassword: String
    private(set) var OloUserFirstName: String
    private(set) var OloUserLastName: String
    private(set) var OloUserPhoneNumber: String
    private(set) var OloUserCreditCardNumber: String
    private(set) var OloUserCreditCardExpiryMonth: String
    private(set) var OloUserCreditCardExpiryYear: String
    private(set) var OloUserCreditCardCVV: String
    private(set) var OloUserCreditCardZip: String
    private(set) var OloUserBasketId: String
    private(set) var OloCouponCode: String
    
    override private init() {
        let bundle = NSBundle(forClass: self.dynamicType)
        let path = bundle.pathForResource("Info", ofType: "plist")
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
