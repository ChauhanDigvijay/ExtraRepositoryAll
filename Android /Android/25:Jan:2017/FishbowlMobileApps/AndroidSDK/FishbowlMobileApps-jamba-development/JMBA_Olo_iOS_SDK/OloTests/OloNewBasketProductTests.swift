//
//  OloNewBasketProductTests.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
@testable import Olo

class OloNewBasketProductTests: OloTestCase {
    
    func testSerializationAsJSONDictionary() {
        //Setup
        var newBasketProduct = OloNewBasketProduct()
        newBasketProduct.productId = 1
        newBasketProduct.quantity = 100
        newBasketProduct.options = "a,b"
        var batchProductChoices = [OloBatchProductChoice]()
        for i in 0..<2 {
            var batchProductChoice = OloBatchProductChoice()
            batchProductChoice.choiceId = i + 1
            var customFields: [OloChoiceCustomFieldValue] = []
            for j in 0..<2 {
                let fieldId = i * 100 + j
                let choiceCustomFieldValue = OloChoiceCustomFieldValue(json: ["fieldid": fieldId, "value":"value\(fieldId)"])
                customFields.append(choiceCustomFieldValue)
            }
            batchProductChoice.customFields = customFields
            batchProductChoices.append(batchProductChoice)
        }
        newBasketProduct.choiceCustomFields = batchProductChoices
        //Test
        let jsonDict = newBasketProduct.serializeAsJSONDictionary()
        XCTAssertEqual((jsonDict["productid"] as! Int), Int(newBasketProduct.productId), "productId should be equal")
        XCTAssertEqual((jsonDict["quantity"] as! Int), Int(newBasketProduct.quantity), "quantity should be equal")
        XCTAssertEqual((jsonDict["options"] as! String), newBasketProduct.options, "quantity should be equal")
        XCTAssertNil((jsonDict["recipient"] as? String), "recipient should be nil")
        XCTAssertNil(jsonDict["specialinstructions"] as? String, "specialinstructions should be nil")
        XCTAssertTrue((jsonDict["choicecustomfields"] as! [[String:AnyObject]]).count == 2, "count should be 2")
        var i = 0
        for batchProductChoice in jsonDict["choicecustomfields"] as! [[String:AnyObject]] {
            XCTAssertEqual((batchProductChoice["choiceid"] as! Int), i + 1, "choiceid should be same")
            XCTAssertTrue((batchProductChoice["customfields"] as! [[String:AnyObject]]).count == 2, "count should be same")
            var j = 0
            for customfield in batchProductChoice["customfields"] as! [[String:AnyObject]] {
                XCTAssertEqual((customfield["fieldid"] as! Int), i * 100 + j, "fieldid should be same")
                XCTAssertEqual((customfield["value"] as! String), "value\(i * 100 + j)", "value should be same")
                ++j
            }
            ++i
        }
        //Setup
        newBasketProduct.recipient = "test user"
        newBasketProduct.specialInstructions = "instructions are of no use"
        //Test
        let jsonDict2 = newBasketProduct.serializeAsJSONDictionary()
        XCTAssertEqual((jsonDict2["recipient"] as! String), "test user","recipient should be equal")
        XCTAssertEqual((jsonDict2["specialinstructions"] as! String), "instructions are of no use", "specialinstructions should be equal")
    }
    
}
