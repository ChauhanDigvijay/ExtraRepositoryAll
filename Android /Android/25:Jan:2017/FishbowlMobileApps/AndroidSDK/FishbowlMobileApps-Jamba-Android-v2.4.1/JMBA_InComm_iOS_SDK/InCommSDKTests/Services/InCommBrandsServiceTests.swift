//
//  InCommBrandsServiceTests.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l > r
  default:
    return rhs < lhs
  }
}


class InCommBrandsServiceTests: InCommTestCase {
    
    func testBrands() {
        let expectation = self.expectation(description: "InComm Service's Tests")
        let sharedConfig = InCommSDKTestsConfigurations.sharedInCommSDKTestsConfigurations
        let brandId = sharedConfig.InCommTestBrandId
        InCommBrandsService.brands { (brands, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            XCTAssert(brands.count > 0, "brands should have been returned")
            var hasMyBrandId = false
            for brand in brands {
                XCTAssert(!brand.id.isEmpty, "Brand Id should be present")
                if brand.id == brandId {
                    hasMyBrandId = true
                }
            }
            XCTAssert(hasMyBrandId, "My brand id should have been present")
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }
    
    func testBrandWithId() {
        let expectation = self.expectation(description: "InComm Service's Tests")
        let sharedConfig = InCommSDKTestsConfigurations.sharedInCommSDKTestsConfigurations
        let brandId = sharedConfig.InCommTestBrandId
        InCommBrandsService.brandWithId(brandId) { (brand, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            XCTAssert(brand != nil, "brand should have been returned")
            XCTAssertEqual(brand!.id, brandId, "Brand Id should be same")
            XCTAssert(brand!.cardImages.count > 0, "Card Images should be present")
            for cardImage in brand!.cardImages {
                XCTAssertFalse(cardImage.imageCode.isEmpty, "Image Code should have been present")
                XCTAssertFalse(cardImage.imageFileName.isEmpty, "imageFileName should have been present")
                XCTAssertFalse(cardImage.imageUrl.isEmpty, "imageUrl should have been present")
                XCTAssert(cardImage.sortOrder > 0, "sortOrder should have been non-zero")
                XCTAssertFalse(cardImage.thumbnailImageUrl.isEmpty, "thumbnailImageUrl should have been present")
                //imageType tested implicitly.
            }
            XCTAssert(brand!.creditCardTypesAndImages.count > 0, "creditCardTypesAndImages should be present")
            for creditCardTypeAndImage in brand!.creditCardTypesAndImages {
                XCTAssertFalse(creditCardTypeAndImage.thumbnailImageUrl.isEmpty, "thumbnailImageUrl should have been present")
                //creditCardType tested implicitly
            }
            XCTAssertTrue(brand!.supportsVariableAmountDenominations, "supportsVariableAmountDenominations should have been true")
            if brand!.supportsVariableAmountDenominations {
                XCTAssertNotNil(brand!.variableAmountDenominationMaximumValue, "variableAmountDenominationMaximumValue should have been present")
                XCTAssertNotNil(brand!.variableAmountDenominationMinimumValue, "variableAmountDenominationMinimumValue should have been present")
                XCTAssert(brand!.variableAmountDenominationMaximumValue > 0, "variableAmountDenominationMaximumValue should have been non-zero")
            }
            expectation.fulfill()
        }
        self.waitForExpectations(timeout: 10, handler: nil)
    }
    
}
