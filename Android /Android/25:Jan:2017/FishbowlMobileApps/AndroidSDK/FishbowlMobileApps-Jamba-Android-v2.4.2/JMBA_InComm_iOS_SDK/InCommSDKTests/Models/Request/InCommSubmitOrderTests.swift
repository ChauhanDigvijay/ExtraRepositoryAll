//
//  InCommSubmitOrderTests.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/19/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK

class InCommSubmitOrderTests: XCTestCase {
    
    func testSubmitOrderSerialization() {
        //Purchaser
        let purchaser = InCommOrderPurchaserHelper.createOrderPurchaserWithDiffrentiatingNumber(1)
        //Recipient
        let recipient = InCommOrderRecipientDetailsHelper.createOrderRecipientDetailsWithDiffrentiatingNumber(2)
        //Payment
        let payment = InCommSubmitPaymentHelper.createSubmitPaymentWithDiffrentiatingNumber(4)
        //Order
        let num = 5
        let randomBoolOrder = arc4random() % 2 == 0
        var order = InCommSubmitOrder(purchaser: purchaser, recipients: [recipient], payment: payment, paymentWithId: nil)
        order.delayActivation = randomBoolOrder
        order.description = "Desc\(num)"
        order.discountAmount = Double(num) + 0.1
        order.discountId = 10 + Int32(num)
        order.fulfillImmediately = !randomBoolOrder
        order.id = "OrderId\(num)"
        order.purchaseOrderFilename = "POF\(num)"
        order.purchaseOrderNumber = "PON\(num)"
        //Serialize
        let orderJSONDict = order.serializeAsJSONDictionary()
        //Test
        //Purchaser
        let purchaserJSONDict = orderJSONDict["Purchaser"] as? InCommJSONDictionary
        XCTAssertNotNil(purchaserJSONDict, "Purchase JSONDict should have been present")
        InCommOrderPurchaserHelper.testOrderPurchaserJSONDict(purchaserJSONDict!, orderPurchaser: purchaser)
        //Recipients
        let recipientJSONDictArray = orderJSONDict["Recipients"] as? [InCommJSONDictionary]
        XCTAssertNotNil(recipientJSONDictArray, "Recipient JSONDict array should have been present")
        XCTAssert(recipientJSONDictArray!.count == 1, "Recipient JSONDict array should have count = 1")
        InCommOrderRecipientDetailsHelper.testOrderRecipientDetailsJSONDict(recipientJSONDictArray![0], orderRecipientDetails: recipient)
        //Payment
        let paymentJSONDict = orderJSONDict["Payment"] as? InCommJSONDictionary
        XCTAssertNotNil(paymentJSONDict, "Payment JSONDict should have been present")
        InCommSubmitPaymentHelper.testSubmitPaymentJSONDict(paymentJSONDict!, submitPayment: payment)
        //Order
        XCTAssertEqual(order.delayActivation, orderJSONDict["DelayActivation"] as? Bool, "DelayActivation should have been the same")
        XCTAssertEqual(order.description, orderJSONDict["Description"] as? String, "Description should have been the same")
        XCTAssertEqual(order.discountAmount, orderJSONDict["DiscountAmount"] as? Double, "DiscountAmount should have been the same")
        XCTAssertEqual(order.discountId, (orderJSONDict["DiscountId"] as? NSNumber)?.int32Value, "DiscountId should have been the same")
        XCTAssertEqual(order.fulfillImmediately, orderJSONDict["FulfillImmediately"] as? Bool, "FulfillImmediately should have been the same")
        XCTAssertEqual(order.id, orderJSONDict["Id"] as? String, "Id should have been the same")
        XCTAssertEqual(order.purchaseOrderFilename, orderJSONDict["PurchaseOrderFilename"] as? String, "PurchaseOrderFilename should have been the same")
        XCTAssertEqual(order.purchaseOrderNumber, orderJSONDict["PurchaseOrderNumber"] as? String, "PurchaseOrderNumber should have been the same")
    }
    
}
