//
//  SpendGoUserServiceTests.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 5/11/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import SpendGoSDK
import SwiftyJSON
@testable import SpendGoSDK

class SpendGoUserServiceTests: SpendGoTestCase {
    
    func testAddMember() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let expectation = self.expectation(description: "SpendGoService")
        let dict = [
            "phone":sharedSpendGoSDKTestsConfigurations.SpendGoUserPhoneNumber,
            "email":sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress,
            "sms_opt_in": false,
            "email_opt_in": false
        ] as [String : Any]
        let json = JSON(dict)
        let user = SpendGoUser(json: json)
        
        SpendGoStoreService.nearestStores("93401", distance: 20) { (stores, error) -> Void in
            if error != nil || stores.count == 0 {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            // Pick random store from the list
            let store = stores[abs(random()) % stores.count]
            let storeId = Int64(Int(store.id)!)
            SpendGoSessionService.addMember(user, password: sharedSpendGoSDKTestsConfigurations.SpendGoUserPassword, favoriteStoreId: storeId, sendWelcomeEmail: true, emailValidated: false) { (error, didReturn202) in
                if error != nil {
                    print(error)
                    XCTAssert(false, "Request failed")
                }
                expectation.fulfill()
            }
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }
    
    func testSignIn() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        let password = sharedSpendGoSDKTestsConfigurations.SpendGoUserPassword
        let expectation = self.expectation(description: "SpendGoService")
        SpendGoSessionService.signIn(emailAddress, password: password) { error in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            XCTAssertNotNil(SpendGoSessionService.spendGoId, "SpendGo Id")
            XCTAssertFalse(SpendGoSessionService.spendGoId!.isEmpty, "id should be present");
            XCTAssertNotNil(SpendGoSessionService.authToken, "Authtoken")
            XCTAssertFalse(SpendGoSessionService.authToken!.isEmpty, "authToken should be present");
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }
    
    func testLookupEmail() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        let expectation = self.expectation(description: "SpendGoService")
        SpendGoSessionService.lookupEmail(emailAddress) { (status, error) -> Void in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                return
            }
            XCTAssertTrue(status != nil && status! == "Activated", "account should be activated")
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 5, handler: nil)
    }
    
    func testLookupPhoneNumber() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let phoneNumber = sharedSpendGoSDKTestsConfigurations.SpendGoUserPhoneNumber
        let expectation = self.expectation(description: "SpendGoService")
        SpendGoSessionService.lookupPhoneNumber(phoneNumber) { (status, error) -> Void in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                return
            }
            XCTAssertTrue(status != nil && status! == "Activated", "account should be activated")
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 5, handler: nil)
    }
    
    func testGetByEmailAddress() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        let password = sharedSpendGoSDKTestsConfigurations.SpendGoUserPassword
        let expectation = self.expectation(description: "SpendGoService")
        SpendGoSessionService.signIn(emailAddress, password: password) { (error) -> Void in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            SpendGoUserService.getMemberByEmailAddress(emailAddress) { (user, error) -> Void in
                if error != nil {
                    print(error)
                    XCTAssert(false, "Request failed")
                    return
                }
                self.helperTestUser(user)
                expectation.fulfill()
            }
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }
    
    func testGetByPhoneNumber() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        let password = sharedSpendGoSDKTestsConfigurations.SpendGoUserPassword
        let phoneNumber = sharedSpendGoSDKTestsConfigurations.SpendGoUserPhoneNumber
        let expectation = self.expectation(description: "SpendGoService")
        SpendGoSessionService.signIn(emailAddress, password: password) { (error) -> Void in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            SpendGoUserService.getMemberByPhoneNumber(phoneNumber) { (user, error) -> Void in
                if error != nil {
                    print(error)
                    XCTAssert(false, "Request failed")
                    return
                }
                self.helperTestUser(user)
                expectation.fulfill()
            }
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }

    func testGetBySpendGoId() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        let password = sharedSpendGoSDKTestsConfigurations.SpendGoUserPassword
        let spendGoId = sharedSpendGoSDKTestsConfigurations.SpendGoUserId
        let expectation = self.expectation(description: "SpendGoService")
        SpendGoSessionService.signIn(emailAddress, password: password) { (error) -> Void in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            SpendGoUserService.getMemberById(spendGoId) { (user, error) -> Void in
                if error != nil {
                    print(error)
                    XCTAssert(false, "Request failed")
                    return
                }
                self.helperTestUser(user)
                expectation.fulfill()
            }
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }

    func testForgotPassword() {
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        let expectation = self.expectation(description: "SpendGoService")
        SpendGoUserService.forgotPassword(emailAddress) { error in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }


    //MARK: Helper
    
    fileprivate func helperTestUser(_ user: SpendGoUser?) {
        XCTAssertFalse(user == nil, "user should be not nil")
        if user == nil {
            return
        }

        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        let emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        let phoneNumber = sharedSpendGoSDKTestsConfigurations.SpendGoUserPhoneNumber
        XCTAssertNotNil(user!.emailAddress, "email should be non-nil")
        if user!.emailAddress != nil {
            XCTAssertEqual(user!.emailAddress!, emailAddress, "email should have been equal")
        }
        XCTAssertEqual(user!.phoneNumber, phoneNumber, "phone number should have been equal")
        XCTAssertNotNil(user!.emailOptIn, "emailOptIn should be non-nil")
        if user!.emailOptIn != nil {
            XCTAssertFalse(user!.emailOptIn!, "emailOptIn should be false")
        }
        XCTAssertFalse(user!.smsOptIn, "smsOptIn should be false")
        XCTAssertFalse(user!.favoriteStore == nil, "favoriteStore should be non-nil")
    }
    
}
