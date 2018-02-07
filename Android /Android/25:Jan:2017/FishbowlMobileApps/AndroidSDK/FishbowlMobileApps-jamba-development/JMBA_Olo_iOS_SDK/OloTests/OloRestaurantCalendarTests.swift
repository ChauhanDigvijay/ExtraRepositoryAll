//
//  OloRestaurantCalendarTests.swift
//  Olo
//
//  Created by Taha Samad on 5/15/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//
import Foundation
import SwiftyJSON
import XCTest
@testable import Olo

class OloRestaurantCalendarTests: OloTestCase {
    
    func testInitWithJSON() {
        let dict =   [
            "type": "type",
            "ranges": [
                [
                    "start": "20150101",
                    "end": "20150102",
                    "weekday": "Mon"
                ],
                [
                    "start": "20150102",
                    "end": "20150103",
                    "weekday": "Tue"
                ]
            ]
        ]
        let json = JSON(dict)
        let restaurantCalendar = OloRestaurantCalendar(json: json)
        XCTAssertEqual(restaurantCalendar.type, "type", "type should be equal")
        XCTAssertEqual(restaurantCalendar.ranges.count, 2, "ranges.count should be equal to 2")
        if restaurantCalendar.ranges.count == 2 {
            XCTAssertEqual(restaurantCalendar.ranges[0].start, "20150101", "[0].start should be equal")
            XCTAssertEqual(restaurantCalendar.ranges[0].end, "20150102", "[0].end should be equal")
            XCTAssertEqual(restaurantCalendar.ranges[0].weekday, "Mon", "[0].weekeday should be equal")
            XCTAssertEqual(restaurantCalendar.ranges[1].start, "20150102", "[1].start should be equal")
            XCTAssertEqual(restaurantCalendar.ranges[1].end, "20150103", "[1].end should be equal")
            XCTAssertEqual(restaurantCalendar.ranges[1].weekday, "Tue", "[1].weekeday should be equal")
        }
    }
}
