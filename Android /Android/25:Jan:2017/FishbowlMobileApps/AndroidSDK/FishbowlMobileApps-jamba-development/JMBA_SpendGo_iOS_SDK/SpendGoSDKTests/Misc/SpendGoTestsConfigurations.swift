//
//  SpendGoTestsConfigurations.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 6/19/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

class SpendGoSDKTestsConfigurations : NSObject {
    
    static let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations()
    
    private(set) var SpendGoAPIKey: String
    private(set) var SpendGoBaseURL: String
    private(set) var SpendGoUserEmailAddress: String
    private(set) var SpendGoUserPassword: String
    private(set) var SpendGoUserPhoneNumber: String
    private(set) var SpendGoUserId: String
    private(set) var SpendGoSigningKey: String
    
    override private init() {
        let bundle = NSBundle(forClass: self.dynamicType)
        let path = bundle.pathForResource("Info", ofType: "plist")
        let infoDict = NSDictionary(contentsOfFile: path!)!
        let SpendGoSDKTestsConfigurationsDict = infoDict["SpendGoSDKTestsConfigurations"] as! NSDictionary
        SpendGoAPIKey = SpendGoSDKTestsConfigurationsDict["SpendGoAPIKey"] as! String
        SpendGoBaseURL = SpendGoSDKTestsConfigurationsDict["SpendGoBaseURL"] as! String
        SpendGoUserEmailAddress = SpendGoSDKTestsConfigurationsDict["SpendGoUserEmailAddress"] as! String
        SpendGoUserPassword = SpendGoSDKTestsConfigurationsDict["SpendGoUserPassword"] as! String
        SpendGoUserPhoneNumber = SpendGoSDKTestsConfigurationsDict["SpendGoUserPhoneNumber"] as! String
        SpendGoUserId = SpendGoSDKTestsConfigurationsDict["SpendGoUserId"] as! String
        SpendGoSigningKey = SpendGoSDKTestsConfigurationsDict["SpendGoSigningKey"] as! String
    }
    
}
