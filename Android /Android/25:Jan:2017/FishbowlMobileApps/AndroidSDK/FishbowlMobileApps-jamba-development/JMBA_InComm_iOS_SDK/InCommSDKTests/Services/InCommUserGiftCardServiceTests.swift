//
//  InCommUserGiftCardServiceTests.swift
//  InCommSDK
//
//  Created by vThink on 8/12/16.
//  Copyright © 2016 Int32. All rights reserved.
//

import Foundation
import InCommSDK
import XCTest

class InCommUserGiftCardServiceTests: InCommTestCase{
    
    let responseTimeHandler:NSTimeInterval     = 10
    let userId:Int32                           = 263558
    let sampleGiftCardId:Int32                 = 42599571
    let brandId                                = "711763"
    
    
    
    func testForGetUserCards(){
          let expectation = self.expectationWithDescription("InComm Service's Tests")

    InCommUserGiftCardService.getGiftCards(userId) { (userGiftCards, error) in
        XCTAssertNotNil(userGiftCards, "data should not be nil")
        XCTAssertNil(error, "error should be nil")

            if error != nil{
                print(error!.localizedDescription)
                return
            }
         expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)

    }
    
    func testForGetUserGiftCard(){
         let expectation = self.expectationWithDescription("InComm Service's Tests")
        
        InCommUserGiftCardService.getGiftCard(userId, cardId: sampleGiftCardId) { (userGiftCard, error) in
          
            XCTAssertNotNil(userGiftCard, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)

    }
    
    func testUserGiftCardBalance(){
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        
        InCommUserGiftCardService.getGiftCardBalance(userId, cardId:sampleGiftCardId) { (giftCardBalance, error) in
            XCTAssertNotNil(giftCardBalance, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testAssocaiateGiftCardToUser(){
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        
        let cardNumber = "710000sample00181"
        let cardPin = "1414"
        
        InCommUserGiftCardService.associateGiftCard(userId, brandId: brandId, cardNumber:cardNumber , cardPin: cardPin) { (userGiftCard, error) in
    
            XCTAssertNotNil(userGiftCard, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    
    func testCreateAutoReload(){
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        let startdate = NSDate()
        let endDate = "2016-08-31".dateFromInCommFormatString()
        let giftCardId: Int32 = 45883592
        let paymentAccountId: Int32 = 74989
        let autoReload = InCommAutoReload(amount: 10, endsOn: endDate, giftCardId: giftCardId, minimumBalance: 0, numberOfOccurancesRemaining: 0, paymentAccountId: paymentAccountId, startsOn: startdate, reloadFrequencyId: InCommReloadFrequencyType.Monthly)
    
        
        InCommUserGiftCardService.createAutoReload(userId, cardId: sampleGiftCardId, autoReload: autoReload) { (autoReloadSavable, error) in
        
            XCTAssertNotNil(autoReloadSavable, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testDeletGiftCard(){
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        
        let cardId: Int32 = 45883592
        
        InCommUserGiftCardService.deleteGiftCard(userId, cardId: cardId) { (error) in
            XCTAssertNotNil(error, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testGetAutoReload(){
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        
        let autoRelaodId:Int32 = 8954
        let cardId:Int32 = 45464464
        
        InCommUserGiftCardService.getAutoReload(userId, cardId: cardId , autoReloadId: autoRelaodId) { (autoReloadSavable, error) in
            XCTAssertNotNil(autoReloadSavable, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testGiftCardTransactionHistory(){
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        
        InCommUserGiftCardService.getGiftCardTransactionHistory(userId, cardId: sampleGiftCardId) { (giftCardTransactionHistory, error) in
            XCTAssertNotNil(giftCardTransactionHistory, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testDeleteAutoReload(){
         let expectation = self.expectationWithDescription("InComm Service's Tests")
        
        let cardId: Int32       = 45883592
        let autoReloadId: Int32 = 8964
        
        InCommUserGiftCardService.deleteAutoReload(userId, cardId: cardId, autoReloadId: autoReloadId) { (error) in
          //  XCTAssertNotNil(error, "data should not be nil")
         //   XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                 expectation.fulfill()
                return
            }
              expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testUpdateAutoReloadStatus(){
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        let cardId: Int32       = 42599571
        let autoReloadId: Int32 = 8854
        let active = false
        InCommUserGiftCardService.updateAutoReloadStatus(userId, cardId: cardId, autoReloadId: autoReloadId, active: active) { (error) in
//            XCTAssertNotNil(error, "data should not be nil")
//            XCTAssertNil(error, "error should be nil")
            if error != nil{
                print(error!.localizedDescription)
                XCTAssert(false, "Unable to auto reload the card")
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
    
    func testUpdateUserGiftCardName(){
        
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        let cardId: Int32       =  458696
        let cardName = "Gift to sister"
        
        InCommUserGiftCardService.updateUserGiftCardName(userId, cardId: cardId, cardName: cardName) { (userGiftCard, error) in
            XCTAssertNotNil(error, "data should not be nil")
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