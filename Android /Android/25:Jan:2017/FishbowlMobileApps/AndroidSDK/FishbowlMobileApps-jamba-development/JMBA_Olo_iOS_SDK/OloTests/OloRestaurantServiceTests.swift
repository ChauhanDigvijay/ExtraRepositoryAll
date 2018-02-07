//
//  OloRestaurantTests.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import XCTest
@testable import Olo

class OloRestaurantServiceTests: OloTestCase {

    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }

    func testRestaurants() {
        let expectation = self.expectationWithDescription("OLOService")
        OloRestaurantService.restaurants { (response, error) -> Void in
            if error != nil {
                XCTAssert(false, "Request failed")
                print(error!.localizedDescription)
            } else {
                XCTAssert(response.count > 0, "Got restaurants!")
                for restaurant in response {
                    XCTAssert(restaurant.zip.isEmpty == false, "Restaurant got zip code")
                }
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }
    
    // Currently expecting 0 restaurants
    func testRestaurantsNearSanLuisObispo() {
        let expectation = self.expectationWithDescription("OLOService")
        
        let latitude = 35.268275
        let longitude = -120.669934
        let radius: Double = 20
        let limit = 10
        
        OloRestaurantService.restaurantsNear(latitude, longitude: longitude, radius: radius, limit: limit) { (response, error) -> Void in
            if error != nil {
                XCTAssert(false, "Request failed")
                print(error!.localizedDescription)
            } else {
                XCTAssert(response.count == 0, "Got restaurants!")
                for restaurant in response {
                    XCTAssert(restaurant.zip.isEmpty == false, "Restaurant got zip code")
                }
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(5.0, handler: nil)
    }

    // Currently expecting ~6 restaurants
    func testRestaurantsNearSanFrancisco() {
        let expectation = self.expectationWithDescription("OLOService")
        
        let latitude = 37.774929
        let longitude = -122.419416
        let radius: Double = 2
        let limit = 10
        
        OloRestaurantService.restaurantsNear(latitude, longitude: longitude, radius: radius, limit: limit) { (response, error) -> Void in
            if error != nil {
                XCTAssert(false, "Request failed")
                print(error!.localizedDescription)
            } else {
                XCTAssert(response.count > 0, "Got restaurants!")
                for restaurant in response {
                    XCTAssert(restaurant.zip.isEmpty == false, "Restaurant got zip code")
                    XCTAssertEqual(restaurant.city, "San Francisco", "City is San Francisco")
                }
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(5.0, handler: nil)
    }
    
    func testRestaurantCalendar() {
        let expectation = self.expectationWithDescription("OLOService")
        OloRestaurantService.restaurants { (restaurants, error) -> Void in
            if error != nil {
                XCTAssert(false, "Request failed")
                print(error!.localizedDescription)
                expectation.fulfill()
            } else {
                XCTAssert(restaurants.count > 0, "Did not get any restaurants")
                if restaurants.count > 0 {
                    let restaurant = restaurants[0]
                    let dateToday = NSDate()
                    let toDate = NSDate(timeIntervalSinceNow: 6 * 24 * 60 * 60)
                    OloRestaurantService.restaurantCalendar(restaurant.id, fromDate:dateToday , toDate: toDate) { (restaurantCalendars, error) -> Void in
                        if error != nil {
                            XCTAssert(false, "Request failed")
                            print(error!.localizedDescription)
                        } else {
                            XCTAssert(restaurantCalendars.count > 0, "Did not get calendar for first restaurant")
                            for restaurantCalendar in restaurantCalendars {
                                XCTAssertFalse(restaurantCalendar.type.isEmpty, "type should not be empty")
                                XCTAssert(restaurantCalendar.ranges.count > 0, "ranges should have count > 0")
                                for timeRange in restaurantCalendar.ranges {
                                    XCTAssertFalse(timeRange.start.isEmpty, "start should not be empty")
                                    XCTAssertFalse(timeRange.end.isEmpty, "end should not be empty")
                                    XCTAssertFalse(timeRange.weekday.isEmpty, "weekday should not be empty")
                                }
                            }
                        }
                        expectation.fulfill()
                    }
                }
            }
        }
        self.waitForExpectationsWithTimeout(20, handler: nil)
    }
    
    func testRestaurantByID() {
        let expectation = self.expectationWithDescription("OLOService")
        OloRestaurantService.restaurants { (response, error) -> Void in
            XCTAssert(response.count > 0, "Got restaurants!")
            for restaurant in response {
                XCTAssert(restaurant.zip.isEmpty == false, "Restaurant got zip code")
            }
            let firstRestaurant = response.first!
            OloRestaurantService.restaurant(firstRestaurant.id, callback: { (restaurant, error) -> Void in
                XCTAssertEqual(firstRestaurant.id, restaurant!.id, "Restaurant Id matches")
                XCTAssertEqual(firstRestaurant.storeCode, restaurant!.storeCode, "Restaurant ExtRef matches")
                XCTAssertEqual(firstRestaurant.name, restaurant!.name, "Restaurant Id matches")
                expectation.fulfill()
            })
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }
    
    func testRestaurantByStoreNumber() {
        let expectation = self.expectationWithDescription("OLOService")
        OloRestaurantService.restaurants { (response, error) -> Void in
            XCTAssert(response.count > 0, "Got restaurants!")
            for restaurant in response {
                XCTAssert(restaurant.zip.isEmpty == false, "Restaurant got zip code")
            }
            let firstRestaurant = response.first!
            OloRestaurantService.restaurant(firstRestaurant.storeCode, callback: { (restaurant, error) -> Void in
                XCTAssertEqual(firstRestaurant.id, restaurant!.id, "Restaurant Id matches")
                XCTAssertEqual(firstRestaurant.storeCode, restaurant!.storeCode, "Restaurant ExtRef matches")
                XCTAssertEqual(firstRestaurant.name, restaurant!.name, "Restaurant Id matches")
                expectation.fulfill()
            })
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }

    func testRestaurantNotFound() {
        let expectation = self.expectationWithDescription("OLOService")
        OloRestaurantService.restaurant("INVALID_CODE", callback: { (restaurant, error) -> Void in
            XCTAssertNotNil(error, "Error was returned")
            expectation.fulfill()
        })
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }

}
