//
//  OloTestCase.swift
//  Olo
//
//  Created by Taha Samad on 15/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import XCGLogger
@testable import Olo

class OloTestCase: XCTestCase {

    override func setUp() {
        super.setUp()
        let sharedOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
        let logger = XCGLogger.defaultInstance()
        logger.setup(.Error, showLogIdentifier: true, showFunctionName: false, showThreadName: true, showLogLevel: true, showFileNames: true, showLineNumbers: true, showDate: true, writeToFile: nil, fileLogLevel: nil)
        OloService.configurationForService(sharedOloSDKTestsConfigurations.OloBaseURL, APIKey: sharedOloSDKTestsConfigurations.OloAPIKey, logger: logger)
    }

}
