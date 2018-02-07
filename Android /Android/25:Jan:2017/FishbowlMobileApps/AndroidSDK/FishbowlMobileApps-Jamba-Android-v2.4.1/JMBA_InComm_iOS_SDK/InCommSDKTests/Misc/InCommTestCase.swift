//
//  InCommTestCase.swift
//  InCommSDK
//
//  Created by Taha Samad on 15/05/2015.
//  Copyright (c) 2015 Fishbowl, Inc. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK

class InCommTestCase: XCTestCase {
    
    override func setUp() {
        super.setUp()
        let sharedInCommSDKTestsConfigurations = InCommSDKTestsConfigurations.sharedInCommSDKTestsConfigurations
        InCommService.configurationForService(sharedInCommSDKTestsConfigurations.InCommBaseURL, clientId: sharedInCommSDKTestsConfigurations.InCommClientId, apiKey: sharedInCommSDKTestsConfigurations.InCommApiKey, testMode: true)
    }
    
}
