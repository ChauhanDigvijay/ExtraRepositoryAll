//
//  OloSessionServiceTests.swift
//  Olo
//
//  Created by Taha Samad on 4/28/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
@testable import Olo

class OloSessionServiceTests: OloTestCase {

    func testCreateUser() {
        let expectation = self.expectation(description: "OLOService")
        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
        let firstName = shareOloSDKTestsConfigurations.OloUserFirstName
        let lastName = shareOloSDKTestsConfigurations.OloUserLastName
        let contactNumber = shareOloSDKTestsConfigurations.OloUserPhoneNumber
        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
        let password = shareOloSDKTestsConfigurations.OloUserPassword
        OloSessionService.createUser(firstName, lastName: lastName, contactNumber: contactNumber, emailAddress: emailAddress, password: password) { (user, error) -> Void in
            if error != nil {
                //Not 2xx. This can happen in case of (HTTP 400, Olo Error Code:2xx) Case same email repeating.
                if error!.code < 200 || error!.code >= 300 {
                    print(error!.localizedDescription)
                    XCTAssert(false, "Request failed")
                }
            }
            else {
                XCTAssert(user != nil, "Created user not returned")
                if user != nil {
                    XCTAssertEqual(user!.firstName, firstName, "firstName should be equal")
                    XCTAssertEqual(user!.lastName, lastName, "lastName should be equal")
                    XCTAssertEqual(user!.contactNumber!, contactNumber, "contactNumber should be equal")
                    XCTAssertEqual(user!.emailAddress, emailAddress, "emailAddress should be equal")
                    XCTAssertTrue(user!.authToken != nil, "authToken should be present")
                }
            }
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 5.0, handler: nil)
    }
    
    
    func testAuthenticateUser() {
        let expectation = self.expectation(description: "OLOService")
        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
        let password = shareOloSDKTestsConfigurations.OloUserPassword
        let firstName = shareOloSDKTestsConfigurations.OloUserFirstName
        let lastName = shareOloSDKTestsConfigurations.OloUserLastName
        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
            if error != nil {
                //Not 2xx. This can happen in case of (HTTP 400, Olo Error Code:2xx) Case same email repeating.
                if error!.code < 200 || error!.code >= 300 {
                    print(error!.localizedDescription)
                    XCTAssert(false, "Request failed")
                }
            }
            else {
                XCTAssert(user != nil, "Authenticated user not returned")
                if user != nil {
                    XCTAssertEqual(user!.firstName, firstName, "firstName should be equal")
                    XCTAssertEqual(user!.lastName, lastName, "lastName should be equal")
                    XCTAssertEqual(user!.emailAddress, emailAddress, "emailAddress should be equal")
                    XCTAssertTrue(user!.authToken != nil, "authToken should be present")
                }
            }
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 5.0, handler: nil)
        
    }
    
//    func testDeleteAuthenticationToken() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
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
//                OloSessionService.deleteAuthenticationToken()
//                XCTAssertNil(OloSessionService.authToken, "authToken should have cleared")
//                expectation.fulfill()
//            }
//        }
//        self.waitForExpectationsWithTimeout(10.0, handler: nil)
//    }

    
    //This will also test getOrCreate
    //Keeping this function, so in case some body wants to test the
    //SDK code with a valid SpendGo Auth Token.
    func testGetOloAuthTokenFromSpendGoAuthToken() {
        let expectation = self.expectation(description: "OLOService")
        //TODO:Need to figure out how to test this func with proper spend go auth token
        //The Auth Token might expire/change when using one from configurations.
        //Another choice could be to just make a call to spend-go using Alamofire.
        OloSessionService.getOrCreate("vendor", providerToken: "asdfasdfasdfasdf") { (oloAuthToken, error) -> Void in
            print("Error Message:\(String(describing: error?.localizedDescription))")
            print("Error Code:\(String(describing: error?.code))")
            print("Auth Tokken:\(String(describing: oloAuthToken))")
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 10.0, handler: nil)
    }
}
