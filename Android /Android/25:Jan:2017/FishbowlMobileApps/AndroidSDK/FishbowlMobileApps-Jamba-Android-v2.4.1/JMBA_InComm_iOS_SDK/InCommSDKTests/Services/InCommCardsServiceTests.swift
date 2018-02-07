//
//  InCommCardsServiceTests.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK

class InCommCardsServiceTests: InCommTestCase {

    func testCardsWithNumber() {
        let expectation = self.expectation(description: "InComm Service's Tests")
        let sharedConfig = InCommSDKTestsConfigurations.sharedInCommSDKTestsConfigurations
        let brandId = sharedConfig.InCommTestBrandId
        let cardNumber = sharedConfig.InCommTestCardNumber
        //TODO: getLatestBalance = true fails with server error at times.
        InCommCardsService.card(brandId, cardNumber: cardNumber, getLatestBalance: false) { (card, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            XCTAssert(card != nil, "Card should have been returned")
            XCTAssertEqual(card!.cardId, sharedConfig.InCommTestCardId, "Card Id should have been same.")
            XCTAssertEqual(card!.cardNumber, cardNumber, "Card Number should have been same")
            //TODO:Test cardPin/pin
            XCTAssert(card!.balance > 0, "Balance should be non-zero")
            XCTAssertFalse(card!.barcodeImageUrl.isEmpty, "Barcode URL should have been present")
            XCTAssertEqual(card!.brandId, brandId, "Brand Id should have been same")
            XCTAssertFalse(card!.brandName.isEmpty, "Brand Name should have been present")
            XCTAssertFalse(card!.imageUrl.isEmpty, "Image URL should have been present")
            XCTAssert(card!.initialBalance > 0, "Initial Balance should be non-zero")
            XCTAssertTrue(card!.isTestMode, "Test Mode should be true")
            XCTAssert(card!.lastModifiedDate != nil, "lastModifiedDate should have been present")
            XCTAssertEqual(card!.messageFrom, sharedConfig.InCommTestMessageFrom, "Message From should have been present")
            XCTAssertEqual(card!.messageTo, sharedConfig.InCommTestMessageTo, "Message From should have been present")
            XCTAssertEqual(card!.messageText, sharedConfig.InCommTestMessageText, "Message From should have been present")
            XCTAssertFalse(card!.termsAndConditions.isEmpty, "Term And Conditions should have been present")
            XCTAssertFalse(card!.thumbnailImageUrl.isEmpty, "Thumbnail Image URL should have been present")
            XCTAssertFalse(card!.usageInstructions.isEmpty, "Usage Instructions should have been present")
            //TODO:Test balanceDate
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }
    
    func testCardsWithId() {
        let expectation = self.expectation(description: "InComm Service's Tests")
        let sharedConfig = InCommSDKTestsConfigurations.sharedInCommSDKTestsConfigurations
        let brandId = sharedConfig.InCommTestBrandId
        let cardId = sharedConfig.InCommTestCardId
        
        InCommCardsService.card(Int32(cardId), getLatestBalance: false) { (card, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            XCTAssert(card != nil, "Card should have been returned")
            XCTAssertEqual(card!.cardNumber, sharedConfig.InCommTestCardNumber, "Card Number should have been same.")
            XCTAssertEqual(card!.cardId, cardId, "Card Id should have been same.")
            //TODO:Test cardPin/pin
            XCTAssert(card!.balance > 0, "Balance should be non-zero")
            XCTAssertFalse(card!.barcodeImageUrl.isEmpty, "Barcode URL should have been present")
            XCTAssertEqual(card!.brandId, brandId, "Brand Id should have been same")
            XCTAssertFalse(card!.brandName.isEmpty, "Brand Name should have been present")
            XCTAssertFalse(card!.imageUrl.isEmpty, "Image URL should have been present")
            XCTAssert(card!.initialBalance > 0, "Initial Balance should be non-zero")
            XCTAssertTrue(card!.isTestMode, "Test Mode should be true")
            XCTAssert(card!.lastModifiedDate != nil, "lastModifiedDate should have been present")
            XCTAssertEqual(card!.messageFrom, sharedConfig.InCommTestMessageFrom, "Message From should have been present")
            XCTAssertEqual(card!.messageTo, sharedConfig.InCommTestMessageTo, "Message From should have been present")
            XCTAssertEqual(card!.messageText, sharedConfig.InCommTestMessageText, "Message From should have been present")
            XCTAssertFalse(card!.termsAndConditions.isEmpty, "Term And Conditions should have been present")
            XCTAssertFalse(card!.thumbnailImageUrl.isEmpty, "Thumbnail Image URL should have been present")
            XCTAssertFalse(card!.usageInstructions.isEmpty, "Usage Instructions should have been present")
            //TODO:Test balanceDate
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }

}
