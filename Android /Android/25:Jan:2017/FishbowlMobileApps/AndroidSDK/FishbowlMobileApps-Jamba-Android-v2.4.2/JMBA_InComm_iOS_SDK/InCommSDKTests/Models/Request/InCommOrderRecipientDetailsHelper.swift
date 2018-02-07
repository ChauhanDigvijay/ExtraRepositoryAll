//
//  InCommOrderRecipientDetailsHelper.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/19/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK

class InCommOrderRecipientDetailsHelper {
    
    class func createOrderRecipientDetailsWithDiffrentiatingNumber(_ num: Int) -> InCommOrderRecipientDetails {
        //Item
        let item = InCommOrderItemHelper.createOrderItemWithDiffrentiatingNumber(num + 1)
        //Recipient
        var recipient = InCommOrderRecipientDetails(firstName: "FN\(num)", lastName: "LN\(num)", emailAddress: "email\(num)@email.com", items: [item])
        recipient.city = "City\(num)"
        recipient.company = "Company\(num)"
        recipient.country = "CN\(num)"
        recipient.deliverOn = Date()
        recipient.deliveryLanguage = "Lang\(num)"
        let randomOrderRecipientDetails = arc4random() % 2 == 0
        recipient.disableEmailDelivery = randomOrderRecipientDetails
        recipient.mobilePhoneNumber = "MobilePhone\(num)"
        recipient.phoneNumber = "Phone\(num)"
        recipient.stateProvince = "State\(num)"
        recipient.streetAddress1 = "StreetAddr1-\(num)"
        recipient.streetAddress2 = "StreetAddr2-\(num)"
        recipient.zipPostalCode = "Zip\(num)"
        recipient.shippingMethod = .Email
        return recipient
    }
    
    class func testOrderRecipientDetails(_ orderRecipientDetails: InCommOrderRecipientDetails) {
        testOrderRecipientDetailsJSONDict(orderRecipientDetails.serializeAsJSONDictionary(), orderRecipientDetails: orderRecipientDetails)
    }
    
    class func testOrderRecipientDetailsJSONDict(_ orderRecipientDetailsJSONDict: InCommJSONDictionary, orderRecipientDetails: InCommOrderRecipientDetails) {
        XCTAssertEqual(orderRecipientDetails.firstName, orderRecipientDetailsJSONDict["FirstName"] as? String, "FirstName should have been same")
        XCTAssertEqual(orderRecipientDetails.lastName, orderRecipientDetailsJSONDict["LastName"] as? String, "LastName should have been same")
        XCTAssertEqual(orderRecipientDetails.emailAddress, orderRecipientDetailsJSONDict["EmailAddress"] as? String, "EmailAddress should have been same")
        XCTAssertEqual(orderRecipientDetails.city, orderRecipientDetailsJSONDict["City"] as? String, "City should have been same")
        XCTAssertEqual(orderRecipientDetails.company, orderRecipientDetailsJSONDict["Company"] as? String, "Company should have been same")
        XCTAssertEqual(orderRecipientDetails.country, orderRecipientDetailsJSONDict["Country"] as? String, "Country should have been same")
        XCTAssertEqual(orderRecipientDetails.deliverOn?.InCommDateTimeFormatString(), orderRecipientDetailsJSONDict["DeliverOn"] as? String, "Country should have been same")
        XCTAssertEqual(orderRecipientDetails.deliveryLanguage, orderRecipientDetailsJSONDict["DeliveryLanguage"] as? String, "DeliveryLanguage should have been same")
        XCTAssertEqual(orderRecipientDetails.disableEmailDelivery, orderRecipientDetailsJSONDict["DisableEmailDelivery"] as? Bool, "DisableEmailDelivery should have been same")
        XCTAssertEqual(orderRecipientDetails.mobilePhoneNumber, orderRecipientDetailsJSONDict["MobilePhoneNumber"] as? String, "MobilePhoneNumber should have been same")
        XCTAssertEqual(orderRecipientDetails.phoneNumber, orderRecipientDetailsJSONDict["PhoneNumber"] as? String, "PhoneNumber should have been same")
        XCTAssertEqual(orderRecipientDetails.stateProvince, orderRecipientDetailsJSONDict["StateProvince"] as? String, "StateProvince should have been same")
        XCTAssertEqual(orderRecipientDetails.streetAddress1, orderRecipientDetailsJSONDict["StreetAddress1"] as? String, "StreetAddress1 should have been same")
        XCTAssertEqual(orderRecipientDetails.streetAddress2, orderRecipientDetailsJSONDict["StreetAddress2"] as? String, "StreetAddress2 should have been same")
        XCTAssertEqual(orderRecipientDetails.zipPostalCode, orderRecipientDetailsJSONDict["ZipPostalCode"] as? String, "ZipPostalCode should have been same")
        XCTAssertEqual(orderRecipientDetails.shippingMethod?.rawValue, orderRecipientDetailsJSONDict["ShippingMethod"] as? String, "ShippingMethod should have been same")
        //Item
        let itemJSONDicts = orderRecipientDetailsJSONDict["Items"] as! [InCommJSONDictionary]
        XCTAssertEqual(orderRecipientDetails.items.count, itemJSONDicts.count, "Items count should have been same")
        let count = orderRecipientDetails.items.count
        for i in 0..<count {
            let item = orderRecipientDetails.items[i]
            let itemJSONDict = itemJSONDicts[i]
            InCommOrderItemHelper.testOrderItemJSONDict(itemJSONDict, orderItem: item)
        }
    }
}
