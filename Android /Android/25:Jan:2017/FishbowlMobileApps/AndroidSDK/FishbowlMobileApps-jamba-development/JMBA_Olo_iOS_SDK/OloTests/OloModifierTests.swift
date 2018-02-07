//
//  OloModifierTests.swift
//  JambaJuice
//
//  Created by Taha Samad on 23/04/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import XCTest
import SwiftyJSON
@testable import Olo

class OloModifierTests: OloTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testInitWithJSON() -> Void {
        let dict = self.helperModifierDictionaryWithIndex(1, generateOptions: true)
        let json = JSON(dict)
        let modifier = OloModifier(json: json)
        self.helperCompareModifierWithDictionary(modifier, dictionary: dict)
    }

    //MARK: Helper
    
    func helperModifierDictionaryArrayWithCount(count: UInt64, generateOptions: Bool) -> [[String: AnyObject]] {
        var dictArray = [[String: AnyObject]]()
        for i in 0..<count {
            dictArray.append(self.helperModifierDictionaryWithIndex(i, generateOptions: generateOptions))
        }
        return dictArray
    }
    
    func helperModifierDictionaryWithIndex(index: UInt64, generateOptions: Bool) -> [String: AnyObject] {
        let i = Int(index) + 1
        let dict: [String: AnyObject] = [
            "id": i,
            "description": "description\(index)",
            "mandatory": (index % 2 == 0),
            "options": generateOptions ? self.helperOptionDictionaryArrayWithCount(3): [],
            "parentchoiceid": "parentchoiceid\(index)",
            "minselects": "minselects\(index)",
            "maxselects": "maxselects\(index)"
        ]
        return dict
    }
    
    func helperOptionDictionaryArrayWithCount(count: UInt64) -> [[String: AnyObject]] {
        var dictArray = [[String: AnyObject]]()
        for i in 0..<count {
            dictArray.append(self.helperOptionDictionaryWithIndex(i))
        }
        return dictArray
    }
    
    func helperOptionDictionaryWithIndex(index: UInt64) -> [String: AnyObject] {
        let i = Int(index) + 1
        let cost = 1.5 + Double(i)
        let dict: [String: AnyObject] = [
            "id":  i,
            "name": "name\(index)",
            "isdefault": (index % 2 == 0),
            "cost": cost,
            "children": (index % 2 == 1),
            "modifiers": self.helperModifierDictionaryArrayWithCount(1, generateOptions: false),
            "fields": self.helperCustomFieldDictionaryArrayWithCount(3)
        ]
        return dict
    }
    
    func helperCustomFieldDictionaryArrayWithCount(count: UInt64) -> Array<[String:AnyObject]> {
        var dictArray = Array<[String:AnyObject]>()
        for i in 0..<count {
            dictArray.append(self.helperCustomFieldDictionaryWithIndex(i))
        }
        return dictArray
    }
    
    func helperCustomFieldDictionaryWithIndex(index: UInt64) -> [String:AnyObject] {
        let i = Int(index) + 1
        let dict: [String:AnyObject] = [
            "id":  i,
            "label": "label\(index)",
            "isrequired": (index % 2 == 0),
            "maxlength": i
        ]
        return dict
    }
    
    func helperCompareModifierWithDictionary(modifier: OloModifier, dictionary:[String:AnyObject]) {
        XCTAssertEqual(modifier.id, Int64(dictionary["id"] as! Int), "id should be equal")
        XCTAssertEqual(modifier.desc, (dictionary["description"] as! String), "description should be equal")
        XCTAssertEqual(modifier.mandatory, (dictionary["mandatory"] as! Bool), "mandatory should be equal")
        XCTAssertEqual(modifier.parentChoiceId, (dictionary["parentchoiceid"] as! String), "parentChoiceId should be equal")
        XCTAssertEqual(modifier.minSelects, (dictionary["minselects"] as! String), "minSelects should be equal")
        XCTAssertEqual(modifier.maxSelects, (dictionary["maxselects"] as! String), "maxSelects should be equal")
        var i = 0
        let arrayOfOptionsDict = dictionary["options"] as! [[String : AnyObject]]
        for option in modifier.options {
            self.helperCompareOptionWithDictionary(option, dictionary: arrayOfOptionsDict[i])
            ++i
        }
    }
    
    func helperCompareOptionWithDictionary(option: OloOption, dictionary:[String:AnyObject]) {
        XCTAssertEqual(option.id, Int64(dictionary["id"] as! Int), "id should be equal")
        XCTAssertEqual(option.name, (dictionary["name"] as! String), "name should be equal")
        XCTAssertEqual(option.isDefault, (dictionary["isdefault"] as! Bool), "isDefault should be equal")
        XCTAssertEqual(option.cost, (dictionary["cost"] as! Double), "cost should be equal")
        XCTAssertEqual(option.children, (dictionary["children"] as! Bool), "children should be equal")
        var i = 0
        let arrayOfModifiersDict = dictionary["modifiers"] as! Array<[String:AnyObject]>
        for modifier in option.modifiers {
            self.helperCompareModifierWithDictionary(modifier, dictionary: arrayOfModifiersDict[i])
            ++i
        }
        i = 0
        let arrayOfCustomFieldsDict = dictionary["fields"] as! Array<[String:AnyObject]>
        for field in option.fields {
            self.helperCompareCustomFieldWithDictionary(field, dictionary: arrayOfCustomFieldsDict[i])
            ++i
        }
    }
    
    func helperCompareCustomFieldWithDictionary(field: OloCustomField, dictionary:[String: AnyObject]) {
        XCTAssertEqual(field.id, Int64(dictionary["id"] as! Int), "id should be equal")
        XCTAssertEqual(field.label, (dictionary["label"] as! String), "label should be equal")
        XCTAssertEqual(field.isRequired, (dictionary["isrequired"] as! Bool), "isRequired should be equal")
        XCTAssertEqual(field.maxLength, (dictionary["maxlength"] as! Int), "maxLength should be equal")
    }

}
