//
//  OloOrderStatusTests.swift
//  Olo
//
//  Created by Taha Samad on 4/28/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import SwiftyJSON
@testable import Olo

class OloOrderStatusTests: OloTestCase {
    
    func testInitWithJSON() {
        let dict = self.helperOrderStatusDictionaryWithIndex(1)
        let json = JSON(dict)
        let orderStatus = OloOrderStatus(json: json)
        self.helperCompareOrderStatusWithDictionary(orderStatus, dictionary: dict)
    }
    
    //MARK: Helper
    
    func helperOrderStatusDictionaryWithIndex(index: UInt64) -> [String: AnyObject] {
        let total = (1.5 * 3.0) + Double(index)
        let subTotal = (1.5 * 2.0) + Double(index)
        let salesTax = 1.5
        let dict: [String: AnyObject] = [
            "id": "id\(index)",
            "timeplaced": "timeplaced\(index)",
            "vendorname": "vendorname\(index)",
            "status": "status\(index)",
            "total": total,
            "subtotal": subTotal,
            "salestax": salesTax,
            "orderref": "orderref\(index)",
            "readytime": "readytime\(index)",
            "vendorextref": "vendorextref\(index)",
            "deliverymode": "deliverymode\(index)",
            "products": self.helperOrderStatusProductDictionaryArrayWithCount(3)
        ]
        return dict
    }
    
    func helperOrderStatusProductDictionaryArrayWithCount(count: UInt64) -> [[String: AnyObject]] {
        var dictArray = [[String: AnyObject]]()
        for i in 0..<count {
            dictArray.append(self.helperOrderStatusProductDictionaryWithIndex(i))
        }
        return dictArray
    }
    
    func helperOrderStatusProductDictionaryWithIndex(index: UInt64) -> [String: AnyObject] {
        let i = Int(index) + 1
        let cost = 1.5 + Double(i)
        let dict: [String: AnyObject] = [
            "name":  "name\(index)",
            "quantity": i,
            "totalcost": cost,
            "specialinstructions": "specialinstructions\(index)"
        ]
        return dict
    }
    
    func helperCompareOrderStatusWithDictionary(orderStatus: OloOrderStatus, dictionary:[String:AnyObject]) {
        XCTAssertEqual(orderStatus.id, (dictionary["id"] as! String), "id should be equal")
        XCTAssertEqual(orderStatus.timePlaced, (dictionary["timeplaced"] as! String), "timePlaced should be equal")
        XCTAssertEqual(orderStatus.vendorName, (dictionary["vendorname"] as! String), "vendorName should be equal")
        XCTAssertEqual(orderStatus.status, (dictionary["status"] as! String), "status should be equal")
        XCTAssertEqual(orderStatus.total, (dictionary["total"] as! Double), "total should be equal")
        XCTAssertEqual(orderStatus.subTotal, (dictionary["subtotal"] as! Double), "subTotal should be equal")
        XCTAssertEqual(orderStatus.salesTax, (dictionary["salestax"] as! Double), "salesTax should be equal")
        XCTAssertEqual(orderStatus.orderRef, (dictionary["orderref"] as! String), "orderRef should be equal")
        XCTAssertEqual(orderStatus.readyTime, (dictionary["readytime"] as! String), "readyTime should be equal")
        XCTAssertEqual(orderStatus.vendorExtRef, (dictionary["vendorextref"] as! String), "vendorExtRef should be equal")
        XCTAssertEqual(orderStatus.deliveryMode, (dictionary["deliverymode"] as! String), "deliveryMode should be equal")
        let arrayOfOrderStatusProductDict = dictionary["products"] as! [[String:AnyObject]]
        var i = 0
        for product in orderStatus.products {
            self.helperCompareOrderStatusProductWithDictionary(product, dictionary: arrayOfOrderStatusProductDict[i])
            ++i
        }
    }
    
    func helperCompareOrderStatusProductWithDictionary(product: OloOrderStatusProduct, dictionary:[String:AnyObject]) {
        XCTAssertEqual(product.name, (dictionary["name"] as! String), "name should be equal")
        XCTAssertEqual(product.quantity, (dictionary["quantity"] as! Int), "quantity should be equal")
        XCTAssertEqual(product.totalCost, (dictionary["totalcost"] as! Double), "totalCost should be equal")
        XCTAssertEqual(product.specialInstructions, (dictionary["specialinstructions"] as! String), "specialInstructions should be equal")
    }
}
