//
//  OloUserServiceTests.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import XCTest
@testable import Olo

class OloUserServiceTests: OloTestCase {

    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
//    func testForgotPassword() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        OloUserService.forgotPassword(emailAddress) { (error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//            }
//            expectation.fulfill()
//        }
//        self.waitForExpectationsWithTimeout(5.0, handler: nil)
//    }

//    func testChangePassword() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        let newPassword = password + "4"
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//            }
//            else {
//                XCTAssert(user != nil, "user should have been returned")
//                if user != nil {
//                    OloUserService.changePassword(password, newPassword: newPassword) { (error) -> Void in
//                        if error != nil {
//                            print(error!.localizedDescription)
//                            XCTAssert(false, "Request failed")
//                        }
//                        else {
//                            OloSessionService.authenticateUser(emailAddress, password: newPassword, basketId: nil) { (user, error) -> Void in
//                                if error != nil {
//                                    print(error!.localizedDescription)
//                                    XCTAssert(false, "Request failed")
//                                }
//                                else {
//                                    //Need to revert password
//                                    OloUserService.changePassword(newPassword, newPassword: password) { (error) -> Void in
//                                        if error != nil {
//                                            print(error!.localizedDescription)
//                                            XCTAssert(false, "Request failed")
//                                        }
//                                        else {
//                                            expectation.fulfill()
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(10.0, handler: nil)
//    }

//    func testUserInformation() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        let firstName = shareOloSDKTestsConfigurations.OloUserFirstName
//        let lastName = shareOloSDKTestsConfigurations.OloUserLastName
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//            }
//            else {
//                XCTAssert(user != nil, "user should have been returned")
//                if user != nil {
//                    OloUserService.userInformation() { (user, error) -> Void in
//                        if error != nil {
//                            print(error!.localizedDescription)
//                            XCTAssert(false, "Request failed")
//                        }
//                        else {
//                            XCTAssert(user != nil, "User not returned")
//                            if user != nil {
//                                XCTAssertEqual(user!.firstName, firstName, "firstName should be equal")
//                                XCTAssertEqual(user!.lastName, lastName, "lastName should be equal")
//                                XCTAssertEqual(user!.emailAddress, emailAddress, "emailAddress should be equal")
//                                XCTAssertTrue(user!.authToken != nil, "authToken should be present")
//                            }
//                        }
//                        expectation.fulfill()
//                    }
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(5.0, handler: nil)
//    }

//    func testUpdateUserInformation() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        let firstName = shareOloSDKTestsConfigurations.OloUserFirstName
//        let lastName = shareOloSDKTestsConfigurations.OloUserLastName
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//                expectation.fulfill()
//                return
//            }
//            XCTAssert(user != nil, "user should have been returned")
//            if user == nil {
//                XCTAssert(false, "Request failed")
//                expectation.fulfill()
//                return
//            }
//                
//            var newUser = user!
//            newUser.firstName = lastName
//            newUser.lastName = firstName
//            OloUserService.updateUserInformation(newUser) { (user, Error) -> Void in
//                if error != nil {
//                    print(error!.localizedDescription)
//                    XCTAssert(false, "Request failed")
//                    expectation.fulfill()
//                    return
//                }
//                XCTAssert(user != nil, "User not returned")
//                if user == nil {
//                    XCTAssert(false, "Request failed")
//                    expectation.fulfill()
//                    return
//                }
//                XCTAssertEqual(user!.firstName, lastName, "firstName should be equal")
//                XCTAssertEqual(user!.lastName, firstName, "lastName should be equal")
//                //Revert Changes
//                var newUser = user!
//                newUser.firstName = firstName
//                newUser.lastName = lastName
//                OloUserService.updateUserInformation(newUser) { (user, Error) -> Void in
//                    if error != nil {
//                        print(error!.localizedDescription)
//                        XCTAssert(false, "Request failed")
//                        expectation.fulfill()
//                        return
//                    }
//                    XCTAssert(user != nil, "User not returned")
//                    if user != nil {
//                        XCTAssertEqual(user!.firstName, firstName, "firstName should be equal")
//                        XCTAssertEqual(user!.lastName, lastName, "lastName should be equal")
//                    }
//                    expectation.fulfill()
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(10.0, handler: nil)
//    }

//    func testRecentOrders() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//            }
//            else {
//                XCTAssert(user != nil, "user should have been returned")
//                if user != nil {
//                    OloUserService.recentOrders() { (orderStatusArray, error) -> Void in
//                        if error != nil {
//                            print(error!.localizedDescription)
//                            XCTAssert(false, "Request failed")
//                        }
//                        expectation.fulfill()
//                    }
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(5.0, handler: nil)
//    }

    //Generated Using:http://names.igopaygo.com/credit_card
    //4556892528054091, CVV2, 445, 02/2019
//    func testAddCreditCard() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        //Credit Card Info
//        let creditCardNumber = shareOloSDKTestsConfigurations.OloUserCreditCardNumber
//        let expiryMonth = shareOloSDKTestsConfigurations.OloUserCreditCardExpiryMonth
//        let expiryYear = shareOloSDKTestsConfigurations.OloUserCreditCardExpiryYear
//        //
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//            }
//            else {
//                XCTAssert(user != nil, "user should have been returned")
//                if user != nil {
//                    OloUserService.addCreditCard(creditCardNumber, expiryMonth: expiryMonth, expiryYear: expiryYear, cvv: "445", zip: "54000") { (error) -> Void in
//                        if error != nil {
//                            print(error!.localizedDescription)
//                            XCTAssert(false, "Request failed")
//                        }
//                        expectation.fulfill()
//                    }
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(5.0, handler: nil)
//    }

//    func testDeleteCreditCard() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//            }
//            else {
//                XCTAssert(user != nil, "user should have been returned")
//                if user != nil {
//                    OloUserService.deleteCreditCard() { (error) -> Void in
//                        if error != nil {
//                            print(error!.localizedDescription)
//                            XCTAssert(false, "Request failed")
//                        }
//                        expectation.fulfill()
//                    }
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(5.0, handler: nil)
//    }

//    func testContactDetails() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        let phoneNumber = shareOloSDKTestsConfigurations.OloUserPhoneNumber
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//                expectation.fulfill()
//            }
//            else if user == nil {
//                XCTAssert(false, "user should have been returned")
//                expectation.fulfill()
//            }
//            else {
//                OloUserService.contactDetails { (contactDetail, error) -> Void in
//                    if error != nil {
//                        print(error!.localizedDescription)
//                        XCTAssert(false, "Request failed")
//                        expectation.fulfill()
//                    }
//                    else if contactDetail == nil {
//                        XCTAssert(false, "contactDetail should have been returned")
//                        expectation.fulfill()
//                    }
//                    else {
//                        XCTAssertTrue(!contactDetail!.isEmpty, "contact detail should not be empty")
//                        OloUserService.updateContactDetails(phoneNumber) { (phoneNumber, error) -> Void in
//                            if error != nil {
//                                print(error!.localizedDescription)
//                                XCTAssert(false, "Request failed")
//                                expectation.fulfill()
//                            }
//                            else if contactDetail == nil {
//                                XCTAssert(false, "contactDetail should have been returned")
//                                expectation.fulfill()
//                            }
//                            else {
//                                XCTAssertTrue(!contactDetail!.isEmpty, "contact detail should not be empty")
//                                expectation.fulfill()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(5.0, handler: nil)
//    }

}
