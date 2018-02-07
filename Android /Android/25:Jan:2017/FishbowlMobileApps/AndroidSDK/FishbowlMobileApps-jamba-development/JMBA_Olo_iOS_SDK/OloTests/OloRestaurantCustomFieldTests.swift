//
//  OloRestaurantCustomFieldTests.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/24/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import XCTest
import SwiftyJSON
@testable import Olo

class OloRestaurantCustomFieldTests: OloTestCase {

    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }

    func testInitWithJSON() -> Void {
        let dictionaries = self.helperRestaurantCustomFieldDictionaryArrayWithCount(3)
        for dictionary in dictionaries {
            let json = JSON(dictionary)
            let restaurantCustomField = OloRestaurantCustomField(json: json)
            self.helperCompareRestaurantCustomFieldWithDictionary(restaurantCustomField, dictionary: dictionary)
        }
    }
    
    //MARK:Helper
    
    func helperRestaurantCustomFieldDictionaryArrayWithCount(count: Int) -> Array<[String:AnyObject]> {
        var dictArray = Array<[String:AnyObject]>()
        for i in 0..<count {
            dictArray.append(self.helperRestaurantCustomFieldDictionaryWithIndex(i))
        }
        return dictArray
    }
    
    func helperRestaurantCustomFieldDictionaryWithIndex(index: Int) -> [String:AnyObject] {
        let dict: [String:AnyObject] = [
            "id": index,
            "label": "label_\(index)",
            "required": (index % 2 == 0),
            "validationregex": "11122233\(index)",
            "qualificationcriteria": "11122233\(index)",
        ]
        return dict
    }
    
    func helperCompareRestaurantCustomFieldWithDictionary(restaurantCustomField: OloRestaurantCustomField, dictionary:[String: AnyObject]) {
        XCTAssertEqual(restaurantCustomField.id, Int64(dictionary["id"] as! Int), "id should be equal")
        XCTAssertEqual(restaurantCustomField.label, (dictionary["label"] as! String), "label should be equal")
        XCTAssertEqual(restaurantCustomField.required, (dictionary["required"] as! Bool), "required should be equal")
        XCTAssertEqual(restaurantCustomField.validationRegex, (dictionary["validationregex"] as! String), "validationRegex should be equal")
        XCTAssertEqual(restaurantCustomField.qualificationCriteria, (dictionary["qualificationcriteria"] as! String), "cualificationCriteria should be equal")
    }
    
}
