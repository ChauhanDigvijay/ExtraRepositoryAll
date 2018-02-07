//
//  StoreLocatorServiceTests.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/25/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import XCTest
import CoreLocation

class StoreLocatorServiceTests: XCTestCase {

    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }

    func testZipcodeReverseGeolocation() {
        let lat = 35.282752
        let lon = -120.659616
        let location = CLLocation(latitude: lat, longitude: lon)

        let expectation = self.expectationWithDescription("JambaJuice")
        LocationService.zipcodeForLocation(location, callback: { (zipcode, error) -> Void in
            XCTAssertNil(error, "Error is nil")
            XCTAssertEqual(zipcode, "93401", "San Luis Obispo")
            expectation.fulfill()
        })
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }
    
}
