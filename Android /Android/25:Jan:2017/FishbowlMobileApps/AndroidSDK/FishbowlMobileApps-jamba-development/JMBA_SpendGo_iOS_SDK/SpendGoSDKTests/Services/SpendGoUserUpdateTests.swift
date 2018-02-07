//
//  SpendGoUserUpdateTests.swift
//  SpendGoSDK
//
//  Created by Eneko Alonso on 6/21/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import UIKit
import XCTest
@testable import SpendGoSDK

class SpendGoUserUpdateTests: XCTestCase {

    var emailAddress: String!
    var password: String!
    var spendGoId: String!
    
    override func setUp() {
        super.setUp()
        let sharedSpendGoSDKTestsConfigurations = SpendGoSDKTestsConfigurations.sharedSpendGoSDKTestsConfigurations
        emailAddress = sharedSpendGoSDKTestsConfigurations.SpendGoUserEmailAddress
        password = sharedSpendGoSDKTestsConfigurations.SpendGoUserPassword
        spendGoId = sharedSpendGoSDKTestsConfigurations.SpendGoUserId
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }

    func testUpdateUserDetails() {
        let firstName = "John-\(random() % 10000)"
        let lastName = "Doe-\(random() % 10000)"
        let dateOfBirth = NSDate(timeIntervalSince1970: NSTimeInterval(random() % 1000000000))
        
        let expectation = self.expectationWithDescription("SpendGoService")
        SpendGoSessionService.signIn(emailAddress, password: password) { error in
            if error != nil {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            SpendGoUserService.updateMember(firstName, lastName: lastName, dateOfBirth: dateOfBirth, emailOptIn: false, smsOptIn: false) { (error) -> Void in
                XCTAssertNil(error, "Error should have been nil")
                if error != nil {
                    print(error)
                    XCTAssert(false, "Request failed")
                    expectation.fulfill()
                    return
                }
                SpendGoUserService.getMemberById(SpendGoSessionService.spendGoId!) { (user, error) -> Void in
                    if error != nil {
                        print(error)
                        XCTAssert(false, "Request failed")
                        expectation.fulfill()
                        return
                    }
                    XCTAssertEqual(user!.firstName!, firstName, "First name has been updated")
                    XCTAssertEqual(user!.lastName!, lastName, "Last name has been updated")
                    // SpendGo API failing to update DOB
                    //                    XCTAssertEqual(user!.dateOfBirth!, SpendGoUserService.userDOBString(dateOfBirth), "DOB has been updated")
                    expectation.fulfill()
                }
            }
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }

    func testUpdateUserFavoriteStore() {
        let expectation = self.expectationWithDescription("SpendGoService")
        SpendGoStoreService.nearestStores("93401", distance: 20) { (stores, error) -> Void in
            if error != nil || stores.count == 0 {
                print(error)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            // Pick random store from the list
            let store = stores[abs(random()) % stores.count]
        
            SpendGoSessionService.signIn(self.emailAddress, password: self.password) { error in
                if error != nil {
                    print(error)
                    XCTAssert(false, "Request failed")
                    expectation.fulfill()
                    return
                }
                let storeId = Int64(Int(store.id)!)
                SpendGoUserService.updateMemberFavoriteStore(storeId) { error in
                    XCTAssertNil(error, "Error should have been nil")
                    if error != nil {
                        print(error)
                        XCTAssert(false, "Request failed")
                        expectation.fulfill()
                        return
                    }
                    SpendGoUserService.getMemberById(SpendGoSessionService.spendGoId!) { (user, error) -> Void in
                        if error != nil {
                            print(error)
                            XCTAssert(false, "Request failed")
                            expectation.fulfill()
                            return
                        }
                        XCTAssertEqual(user!.favoriteStore!.id, store.id, "Store Id  has been updated")
                        expectation.fulfill()
                    }
                }
            }
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }
    
//    func testUpdateMemberPassword() {
//        let newPassword = "newpassword"
//        let expectation = self.expectationWithDescription("SpendGoService")
//        SpendGoSessionService.signIn(emailAddress, password: password) { error in
//            if error != nil {
//                print(error)
//                XCTAssert(false, "Request failed")
//                expectation.fulfill()
//                return
//            }
//            SpendGoUserService.updateMemberPassword(newPassword) { error in
//                XCTAssertNil(error, "Error should have been nil")
//                if error != nil {
//                    print(error)
//                    XCTAssert(false, "Request failed")
//                    expectation.fulfill()
//                    return
//                }
//                SpendGoSessionService.signIn(self.emailAddress, password: newPassword) { error in
//                    if error != nil {
//                        print(error)
//                        XCTAssert(false, "Request failed")
//                        expectation.fulfill()
//                        return
//                    }
//                    // Set password back to original
//                    SpendGoUserService.updateMemberPassword(self.password) { error in
//                        XCTAssertNil(error, "Error should have been nil")
//                        if error != nil {
//                            print(error)
//                            XCTAssert(false, "Request failed")
//                            expectation.fulfill()
//                            return
//                        }
//                        expectation.fulfill()
//                    }
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(10, handler: nil)
//    }

}
