//
//  OloBasketValidationTests.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import SwiftyJSON
@testable import Olo

class OloBasketValidationTests: OloTestCase {
    
    func testInitWithJSON() {
        let dict:[String: AnyObject] = ["basketid": "ide" as AnyObject, "tax":0.5 as AnyObject, "subtotal":1.75 as AnyObject, "total":2.25 as AnyObject]
        let json = JSON(dict)
        let basketValidation = OloBasketValidation(json: json)
        XCTAssertEqual(basketValidation.basketId, "ide", "basketId should be equal")
        XCTAssertEqual(basketValidation.tax, 0.5, "tax should be equal")
        XCTAssertEqual(basketValidation.subTotal, 1.75, "subtotal should be equal")
        XCTAssertEqual(basketValidation.total, 2.25, "total should be equal")
    }
}
