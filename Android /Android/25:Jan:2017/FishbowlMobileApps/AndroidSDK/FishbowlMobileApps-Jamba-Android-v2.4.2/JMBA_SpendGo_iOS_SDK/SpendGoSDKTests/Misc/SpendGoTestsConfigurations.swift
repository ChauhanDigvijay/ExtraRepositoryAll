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
    
    fileprivate(set) var SpendGoAPIKey: String
    fileprivate(set) var SpendGoBaseURL: String
    fileprivate(set) var SpendGoUserEmailAddress: String
    fileprivate(set) var SpendGoUserPassword: String
    fileprivate(set) var SpendGoUserPhoneNumber: String
    fileprivate(set) var SpendGoUserId: String
    fileprivate(set) var SpendGoSigningKey: String
    
    override fileprivate init() {
        let bundle = Bundle(for: type(of: self))
        let path = bundle.path(forResource: "Info", ofType: "plist")
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
