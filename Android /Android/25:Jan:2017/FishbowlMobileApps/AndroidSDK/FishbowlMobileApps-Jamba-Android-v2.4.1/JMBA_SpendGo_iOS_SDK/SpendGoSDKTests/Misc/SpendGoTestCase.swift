//
//  SpendGoTestCase.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 6/19/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import XCGLogger
@testable import SpendGoSDK

class SpendGoTestCase: XCTestCase {

    override func setUp() {
        super.setUp()
        let config = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let logger = XCGLogger.defaultInstance()
        logger.setup(.Verbose, showLogIdentifier: true, showFunctionName: false, showThreadName: true, showLogLevel: true, showFileNames: true, showLineNumbers: true, showDate: true, writeToFile: nil, fileLogLevel: nil)
        SpendGoService.configurationForService(config.SpendGoBaseURL, APIKey: config.SpendGoAPIKey, SigningKey: config.SpendGoSigningKey, logger: logger)
    }

}
