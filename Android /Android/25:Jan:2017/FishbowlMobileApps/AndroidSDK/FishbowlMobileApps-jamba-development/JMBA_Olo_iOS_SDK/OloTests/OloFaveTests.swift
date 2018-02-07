//
//  OloFaveTests.swift
//  Olo
//
//  Created by Taha Samad on 06/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import SwiftyJSON
@testable import Olo

class OloFaveTests: OloTestCase {

    func testInitWithJSON() {
        let dict =   [
            "id": 1,
            "name": "name1",
            "vendorid": 2,
            "vendorname": "vendorname1",
            "disabled": true,
            "online": false
        ]
        let json = JSON(dict)
        let fave = OloFave(json: json)
        XCTAssertEqual(fave.id, Int64(dict["id"] as! Int), "id should be equal")
        XCTAssertEqual(fave.name, (dict["name"] as! String), "name should be equal")
        XCTAssertEqual(fave.vendorId, Int64(dict["vendorid"] as! Int), "vendorId should be equal")
        XCTAssertEqual(fave.vendorName, (dict["vendorname"] as! String), "vendorName should be equal")
        XCTAssertEqual(fave.disabled, (dict["disabled"] as! Bool), "disabled should be equal")
        XCTAssertEqual(fave.online, (dict["online"] as! Bool), "online should be equal")
    }
}