//
//  SpendGoStoreServiceTests.swift
//  SpendGoSDK
//
//  Created by Eneko Alonso on 5/13/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import UIKit
import XCTest
@testable import SpendGoSDK

class SpendGoStoreServiceTests: SpendGoTestCase {

    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }

    func testNearestStores() {
        let expectation = self.expectationWithDescription("SpendGoService")
        SpendGoStoreService.nearestStores("93401", distance: 10) { (stores, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                print((error!.userInfo as! [String: AnyObject])[SpendGoErrorTechnicalDescriptionKey])
                XCTAssert(false, "Request failed")
            }
            else {
                //TODO: Test Store Data
                print(stores)
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }

}
