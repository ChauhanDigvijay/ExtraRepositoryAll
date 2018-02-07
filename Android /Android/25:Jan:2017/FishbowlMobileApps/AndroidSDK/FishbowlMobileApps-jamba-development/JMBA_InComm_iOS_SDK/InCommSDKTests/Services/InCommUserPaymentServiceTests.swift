//
//  InCommUserPaymentServiceTests.swift
//  InCommSDK
//
//  Created by vThink on 8/16/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import InCommSDK
import XCTest

class InCommUserPaymentServiceTests: InCommTestCase{
  
    let responseTimeHandler:NSTimeInterval     = 60
    let userId:Int32                           = 263558
    let sampleGiftCardId:Int32                 = 42599571
    let brandId                                = "711763"
    
    func testAddPaymentAccount(){
         let expectation = self.expectationWithDescription("InComm Service's Tests")
        
        let userPaymentAccount = InCommUserPaymentAccountDetails(firstName: "Test", lastName: "User", streetAddress1: "Edwin Steet",streetAddress2: "Ave Suite 900", city: "Portland", stateProvince: "OR", country: "US", zipPostalCode: "97204", creditCardExpirationMonth: "09", creditCardNumber: "4111111111111111", creditCardVerificationCode: "321", creditCardTypeCode: "VISA", creditCardExpirationYear: "2015", orderPaymentMethod: InCommOrderPaymentMethod.CreditCard)
        
        
        InCommUserPaymentService.addPaymentAccount(userId, userPaymentAccount: userPaymentAccount) { (userPaymentAccount, error) in
            XCTAssertNotNil(userPaymentAccount, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
            self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testDeletePaymentAccount(){
          let expectation = self.expectationWithDescription("InComm Service's Tests")
        let paymentAccountId:Int32 = 75526
        
        InCommUserPaymentService.deletePaymentAccount(userId, paymentAccountId: paymentAccountId) { (error) in
            if error != nil{
                print(error!.localizedDescription)
                XCTAssertNotNil(error, "data should not be nil")
                expectation.fulfill()
                return
            }
            XCTAssertNil(error, "error should be nil")
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testGetPaymentAccountDetails(){
         let expectation = self.expectationWithDescription("InComm Service's Tests")
        let paymentAccountID: Int32 = 75528
        InCommUserPaymentService.getPaymentAccountDetails(userId, paymentAccountId: paymentAccountID) { (userPaymentAccount, error) in
            XCTAssertNotNil(userPaymentAccount, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                XCTAssertNotNil(error, "data should not be nil")
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testGetPaymentAccountsDetails(){
          let expectation = self.expectationWithDescription("InComm Service's Tests")
        InCommUserPaymentService.getPaymentAccountsDetails(userId) { (userPaymentAccounts, error) in
            XCTAssertNotNil(userPaymentAccounts, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
}