//
//  SpendGoRewardServiceTests.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 5/12/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import SpendGoSDK
@testable import SpendGoSDK

class SpendGoRewardServiceTests: SpendGoTestCase {

    func testRewardSummary() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        let password = sharedSpendGoSDKTestsConfigurations.SpendGoUserPassword
        let expectation = self.expectationWithDescription("SpendGoService")
        SpendGoSessionService.signIn(emailAddress, password: password) { (error) -> Void in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            SpendGoRewardService.rewardSummary(SpendGoSessionService.spendGoId!) { (spendGoRewardSummary, error) -> Void in
                if error != nil {
                    print(error)
                    XCTAssert(false, "Request failed")
                }
                else {
                    //TODO:
                    print(spendGoRewardSummary)
                }
                expectation.fulfill()
            }
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }

/// Reward by phone number nly not available yet
//    func testRewardPhoneNumberOnly() {
//        var expectation = self.expectationWithDescription("SpendGoService")
//        SpendGoRewardService.rewardSummary("8052861458") { (spendGoRewardSummary, error) in
//            if error != nil {
//                println(error!.localizedDescription)
//                println((error!.userInfo as! [String: AnyObject])[SpendGoErrorTechnicalDescriptionKey])
//                XCTAssert(false, "Request failed")
//            } else {
//                // TODO:
//                println(spendGoRewardSummary)
//            }
//            expectation.fulfill()
//        }
//        self.waitForExpectationsWithTimeout(10, handler: nil)
//    }

}
