//
//  InCommOrderPurchaserHelper.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/19/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import InCommSDK
import XCTest

class InCommOrderPurchaserHelper {
    
    class func createOrderPurchaserWithDiffrentiatingNumber(num: Int) -> InCommOrderPurchaser {
        var purchaser = InCommOrderPurchaser(firstName: "FN\(num)", lastName: "LN\(num)", emailAddress: "email\(num)@email.com", country: "CN\(num)")
        purchaser.city = "City\(num)"
        purchaser.companyName = "Company\(num)"
        purchaser.mobilePhoneNumber = "MobilePhone\(num)"
        purchaser.phoneNumber = "Phone\(num)"
        let randomBoolPurchaser1 = arc4random() % 2 == 0
        purchaser.sendDeliveryEmailAlert = randomBoolPurchaser1
        purchaser.sendDeliveryTextAlert = !randomBoolPurchaser1
        let randomBoolPurchaser2 = arc4random() % 2 == 0
        purchaser.sendViewEmailAlert = randomBoolPurchaser2
        purchaser.sendViewTextAlert = randomBoolPurchaser2
        purchaser.stateProvince = "State\(num)"
        purchaser.streetAddress1 = "StreetAddr1-\(num)"
        purchaser.streetAddress2 = "StreetAddr2-\(num)"
        let randomBoolPurchaser3 = arc4random() % 2 == 0
        purchaser.suppressReceiptEmail = randomBoolPurchaser3
        purchaser.userId = Int32(10) + num
        purchaser.zipPostalCode = "Zip\(num)"
        return purchaser
    }
    
    class func testOrderPurchaser(orderPurchaser: InCommOrderPurchaser) {
        testOrderPurchaserJSONDict(orderPurchaser.serializeAsJSONDictionary(), orderPurchaser: orderPurchaser)
    }
    
    class func testOrderPurchaserJSONDict(orderPurchaserJSONDict: InCommJSONDictionary, orderPurchaser: InCommOrderPurchaser) {
        XCTAssertEqual(orderPurchaser.firstName, orderPurchaserJSONDict["FirstName"] as? String, "FirstName should have been same")
        XCTAssertEqual(orderPurchaser.lastName, orderPurchaserJSONDict["LastName"] as? String, "LastName should have been same")
        XCTAssertEqual(orderPurchaser.emailAddress, orderPurchaserJSONDict["EmailAddress"] as? String, "EmailAddress should have been same")
        XCTAssertEqual(orderPurchaser.country, orderPurchaserJSONDict["Country"] as? String, "Country should have been same")
        XCTAssertEqual(orderPurchaser.city, orderPurchaserJSONDict["City"] as? String, "City should have been same")
        XCTAssertEqual(orderPurchaser.companyName, orderPurchaserJSONDict["CompanyName"] as? String, "CompanyName should have been same")
        XCTAssertEqual(orderPurchaser.mobilePhoneNumber, orderPurchaserJSONDict["MobilePhoneNumber"] as? String, "MobilePhoneNumber should have been same")
        XCTAssertEqual(orderPurchaser.phoneNumber, orderPurchaserJSONDict["PhoneNumber"] as? String, "PhoneNumber should have been same")
        XCTAssertEqual(orderPurchaser.sendDeliveryEmailAlert, orderPurchaserJSONDict["SendDeliveryEmailAlert"] as? Bool, "SendDeliveryEmailAlert should have been same")
        XCTAssertEqual(orderPurchaser.sendDeliveryTextAlert, orderPurchaserJSONDict["SendDeliveryTextAlert"] as? Bool, "SendDeliveryTextAlert should have been same")
        XCTAssertEqual(orderPurchaser.sendViewEmailAlert, orderPurchaserJSONDict["SendViewEmailAlert"] as? Bool, "SendViewEmailAlert should have been same")
        XCTAssertEqual(orderPurchaser.sendViewTextAlert, orderPurchaserJSONDict["SendViewTextAlert"] as? Bool, "SendViewTextAlert should have been same")
        XCTAssertEqual(orderPurchaser.sendViewTextAlert, orderPurchaserJSONDict["SendViewTextAlert"] as? Bool, "SendViewTextAlert should have been same")
        XCTAssertEqual(orderPurchaser.stateProvince, orderPurchaserJSONDict["StateProvince"] as? String, "StateProvince should have been same")
        XCTAssertEqual(orderPurchaser.streetAddress1, orderPurchaserJSONDict["StreetAddress1"] as? String, "StreetAddress1 should have been same")
        XCTAssertEqual(orderPurchaser.streetAddress2, orderPurchaserJSONDict["StreetAddress2"] as? String, "StreetAddress2 should have been same")
        XCTAssertEqual(orderPurchaser.suppressReceiptEmail, orderPurchaserJSONDict["SuppressReceiptEmail"] as? Bool, "SuppressReceiptEmail should have been same")
        XCTAssertEqual(orderPurchaser.userId, (orderPurchaserJSONDict["UserId"] as? NSNumber)?.intValue, "UserId should have been same")
        XCTAssertEqual(orderPurchaser.zipPostalCode, orderPurchaserJSONDict["ZipPostalCode"] as? String, "ZipPostalCode should have been same")
    }
}
