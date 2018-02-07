//
//  OloBasketTests.swift
//  Olo
//
//  Created by Taha Samad on 5/5/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import SwiftyJSON
@testable import Olo

class OloBasketTests: OloTestCase {

    func testInitWithJSON() {
        let dict = OloBasketTests.helperBasketDictionaryWithIndex(1)
        let json = JSON(dict)
        let basket = OloBasket(json: json)
        OloBasketTests.helperCompareBasketWithDictionary(basket, dictionary: dict)
    }
    
    //MARK: Helper

    class func helperBasketDictionaryWithIndex(index: UInt64) -> [String: AnyObject] {
        let i = Int(index) + 1
        let cost = 1.5 + Double(i)
        var dict: [String: AnyObject] = [
            "id": "id\(index)",
            "earliestreadytime": "earliestreadytime\(index)",
            "subtotal": cost,
            "salestax": cost * 0.5,
            "tip": cost * 0.25,
            "coupondiscount": cost * 0.5,
            "total": cost * 1.25,
            "vendorid": i * 2,
            "deliverymode": "deliverymode\(index)",
            "products": self.helperBasketProductDictionaryArrayWithCount(3)
        ]
        if index % 2 == 0 {
            dict["timewanted"] = "timewanted\(index)"
            dict["vendoronline"] = false
        }
        else {
            dict["vendoronline"] = true
        }
        return dict
    }
    
    class func helperBasketProductDictionaryArrayWithCount(count: UInt64) -> [[String: AnyObject]] {
        var dictArray = [[String: AnyObject]]()
        for i in 0..<count {
            dictArray.append(self.helperBasketProductDictionaryWithIndex(i))
        }
        return dictArray
    }

    class func helperBasketProductDictionaryWithIndex(index: UInt64) -> [String: AnyObject] {
        let i = Int(index) + 1
        let cost = 1.5 + Double(i)
        let dict: [String: AnyObject] = [
            "id":  i,
            "productId": i * 2,
            "name": "name\(index)",
            "quantity": i * 3,
            "basecost": cost,
            "totalcost": cost * 2,
            "specialinstructions": "specialinstructions\(index)",
            "choices": self.helperBasketChoiceDictionaryArrayWithCount(3)
        ]
        return dict
    }
    
    class func helperBasketChoiceDictionaryArrayWithCount(count: UInt64) -> [[String: AnyObject]] {
        var dictArray = [[String: AnyObject]]()
        for i in 0..<count {
            dictArray.append(self.helperBasketChoiceDictionaryWithIndex(i))
        }
        return dictArray
    }
    
    class func helperBasketChoiceDictionaryWithIndex(index: UInt64) -> [String: AnyObject] {
        let i = Int(index) + 1
        let cost = 1.5 + Double(i)
        let dict: [String: AnyObject] = [
            "id":  i,
            "optionid": i * 2,
            "name": "name\(index)",
            "metric": i * 3,
            "indent": i * 4,
            "cost": cost,
            "customfields": self.helperChoiceCustomFieldValueDictionaryArrayWithCount(3)
        ]
        return dict
    }
    
    class func helperChoiceCustomFieldValueDictionaryArrayWithCount(count: UInt64) -> [[String: AnyObject]] {
        var dictArray = [[String: AnyObject]]()
        for i in 0..<count {
            dictArray.append(self.helperChoiceCustomFieldValueDictionaryWithIndex(i))
        }
        return dictArray
    }
    
    class func helperChoiceCustomFieldValueDictionaryWithIndex(index: UInt64) -> [String: AnyObject] {
        let i = Int(index) + 1
        let dict: [String: AnyObject] = [
            "fieldid": i,
            "value": "value\(index)"
        ]
        return dict
    }
    
    class func helperCompareBasketWithDictionary(basket: OloBasket, dictionary:[String:AnyObject]) {
        XCTAssertEqual(basket.id, (dictionary["id"] as! String), "id should be equal")
        XCTAssertEqual(basket.earliestReadyTime, (dictionary["earliestreadytime"] as! String), "earliestReadyTime should be equal")
        XCTAssertEqual(basket.subTotal, (dictionary["subtotal"] as! Double), "subtotal should be equal")
        XCTAssertEqual(basket.salesTax, (dictionary["salestax"] as! Double), "salesTax should be equal")
        XCTAssertEqual(basket.tip, (dictionary["tip"] as! Double), "tip should be equal")
        XCTAssertEqual(basket.couponDiscount, (dictionary["coupondiscount"] as! Double), "couponDiscount should be equal")
        XCTAssertEqual(basket.total, (dictionary["total"] as! Double), "total should be equal")
        XCTAssertEqual(basket.vendorId, Int64(dictionary["vendorid"] as! Int), "vendorId should be equal")
        XCTAssertEqual(basket.deliveryMode, (dictionary["deliverymode"] as! String), "deliveryMode should be equal")
        let arrayOfBasketProductDicts = dictionary["products"] as! [[String : AnyObject]]
        XCTAssertEqual(basket.products.count, arrayOfBasketProductDicts.count, "count should be equal")
        var i = 0
        for basketProduct in basket.products {
            self.helperCompareBasketProductWithDictionary(basketProduct, dictionary: arrayOfBasketProductDicts[i])
            ++i
        }
        XCTAssertEqual(basket.vendorOnline, (dictionary["vendoronline"] as! Bool), "vendoOnline should eb equal")
        XCTAssertTrue(basket.timeWanted == dictionary["timewanted"] as? String, "timeWanted should be equal")
    }
    
    class func helperCompareBasketProductWithDictionary(basketProduct: OloBasketProduct, dictionary:[String:AnyObject]) {
        XCTAssertEqual(basketProduct.id, Int64(dictionary["id"] as! Int), "id should be equal")
        XCTAssertEqual(basketProduct.productId, Int64(dictionary["productId"] as! Int), "productId should be equal")
        XCTAssertEqual(basketProduct.name, (dictionary["name"] as! String), "name should be equal")
        XCTAssertEqual(basketProduct.quantity, (dictionary["quantity"] as! Int), "quantity should be equal")
        XCTAssertEqual(basketProduct.baseCost, (dictionary["basecost"] as! Double), "baseCost should be equal")
        XCTAssertEqual(basketProduct.totalCost, (dictionary["totalcost"] as! Double), "totalCost should be equal")
        XCTAssertEqual(basketProduct.specialInstructions, (dictionary["specialinstructions"] as! String), "specialInstructions should be equal")
        let arrayOfBasketChoiceDicts = dictionary["choices"] as! [[String : AnyObject]]
        XCTAssertEqual(basketProduct.choices.count, arrayOfBasketChoiceDicts.count, "count should be equal")
        var i = 0
        for basketChoice in basketProduct.choices {
            self.helperCompareBasketChoiceWithDictionary(basketChoice, dictionary: arrayOfBasketChoiceDicts[i])
            ++i
        }
    }
    
    class func helperCompareBasketChoiceWithDictionary(basketChoice: OloBasketChoice, dictionary:[String:AnyObject]) {
        XCTAssertEqual(basketChoice.id, Int64(dictionary["id"] as! Int), "id should be equal")
        XCTAssertEqual(basketChoice.optionId, Int64(dictionary["optionid"] as! Int), "optionId should be equal")
        XCTAssertEqual(basketChoice.name, (dictionary["name"] as! String), "name should be equal")
        XCTAssertEqual(basketChoice.metric, Int64(dictionary["metric"] as! Int), "metric should be equal")
        XCTAssertEqual(basketChoice.indent, Int64(dictionary["indent"] as! Int), "indent should be equal")
        XCTAssertEqual(basketChoice.cost, (dictionary["cost"] as! Double), "cost should be equal")
        let arrayOfChoiceCustomFieldValuesDict = dictionary["customfields"] as! [[String : AnyObject]]
        XCTAssertEqual(basketChoice.customFields.count, arrayOfChoiceCustomFieldValuesDict.count, "count should be equal")
        var i = 0
        for choiceCustomFieldValue in basketChoice.customFields {
            self.helperChoiceCustomFieldValueWithDictionary(choiceCustomFieldValue, dictionary: arrayOfChoiceCustomFieldValuesDict[i])
            ++i
        }
    }
        
    class func helperChoiceCustomFieldValueWithDictionary(customField: OloChoiceCustomFieldValue, dictionary:[String:AnyObject]) {
        XCTAssertEqual(customField.fieldId, Int64(dictionary["fieldid"] as! Int), "fieldId should be equal")
        XCTAssertEqual(customField.value, (dictionary["value"] as! String), "value should be equal")
    }
    
}