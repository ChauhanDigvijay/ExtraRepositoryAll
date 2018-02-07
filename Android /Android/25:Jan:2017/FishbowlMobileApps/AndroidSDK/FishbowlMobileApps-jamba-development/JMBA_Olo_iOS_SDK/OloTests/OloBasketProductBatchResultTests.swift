//
//  OloBasketProductBatchResultTests.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import SwiftyJSON
@testable import Olo

class OloBasketProductBatchResultTests: OloTestCase {
    
    func testInitWithJSON() {
        var dict: [String: AnyObject] = [:]
        dict["basket"] = OloBasketTests.helperBasketDictionaryWithIndex(1)
        var errorsDictArray: [[String: AnyObject]] = []
        for i in 0..<2 {
            var errorDict: [String: AnyObject] = [:]
            errorDict["productid"] = i + 1
            errorDict["message"] = "message\(i + 1)"
            errorsDictArray.append(errorDict)
        }
        dict["errors"] = errorsDictArray
        let json = JSON(dict)
        let basketProductBatchResult = OloBasketProductBatchResult(json: json)
        OloBasketTests.helperCompareBasketWithDictionary(basketProductBatchResult.basket, dictionary: dict["basket"] as! [String: AnyObject])
        XCTAssertTrue(basketProductBatchResult.errors.count == 2, "count should be 2")
        var i = 0
        for error in basketProductBatchResult.errors {
            let errDict = (dict["errors"] as! [[String:AnyObject]])[i]
            XCTAssertEqual(Int(error.productId), (errDict["productid"] as! Int), "peoductId should be equal")
            XCTAssertEqual(error.message, (errDict["message"] as! String), "message should be equal")
            ++i
        }
    }
}
