//
//  InCommOrdersServiceTests.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK

class InCommOrdersServiceTests: InCommTestCase {
    
    func testOrderSubmitAndGet() {
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        let sharedConfig = InCommSDKTestsConfigurations.sharedInCommSDKTestsConfigurations
        let emailAddress = sharedConfig.InCommUserEmailAddress
        let brandId = sharedConfig.InCommTestBrandId
        let creditCardNumber = sharedConfig.InCommUserCreditCardNumber
        let zip = sharedConfig.InCommUserCreditCardZip
        let cvv = sharedConfig.InCommUserCreditCardCVV
        let expirationMonth = sharedConfig.InCommUserCreditCardExpiryMonth
        let expirationYear = sharedConfig.InCommUserCreditCardExpiryYear
        let creditCardType = sharedConfig.InCommUserCreditCardType
        //Request Data
        let purchaser = InCommOrderPurchaser(firstName: "JMB1", lastName: "JMB2", emailAddress: emailAddress, country: "US")
        let amount = 11.0
        let quantity: UInt32 = 2
        let item = InCommOrderItem(brandId: brandId, amount: amount, quantity: quantity)
        let recipient = InCommOrderRecipientDetails(firstName: "JMB3", lastName: "JMB4", emailAddress: "r." + emailAddress, items: [item])
        let payment: InCommSubmitPayment!
        if sharedConfig.InCommNoFundsCollected {
            payment = InCommSubmitPayment(amount: amount * Double(quantity), orderPaymentMethod: .CreditCard, creditCardType:creditCardType)
        }
        else {
            payment = InCommSubmitPayment(amount: 10 * Double(quantity), orderPaymentMethod: .CreditCard, firstName: "JMB5", lastName: "JMB6", streetAddress1: "", city: "City1", stateProvince: "", country: "US", zipPostalCode: zip, creditCardNumber: creditCardNumber, creditCardVerificationCode: cvv, creditCardExpirationMonth: expirationMonth, creditCardExpirationYear: expirationYear, creditCardType: creditCardType)
            
        }
        let submitOrder = InCommSubmitOrder(purchaser: purchaser, recipients: [recipient], payment: payment, paymentWithId: nil)
        InCommOrdersService.submitOrder(submitOrder) { (order, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            XCTAssert(order != nil, "Order should have been present")
            XCTAssertFalse(order!.id.isEmpty, "id should have been present")
            XCTAssert(order!.result == .Success, "result should have been Success")
            XCTAssert(order!.submittedOrderItemGiftCards != nil, "submittedOrderItemGiftCards should have been present")
            XCTAssert(order!.submittedOrderItemGiftCards!.count > 0, "submittedOrderItemGiftCards should have count non-zero")
            for orderItemGiftCard in order!.submittedOrderItemGiftCards! {
                XCTAssert(orderItemGiftCard.amount > 0, "amount should be > 0")
                XCTAssertFalse(orderItemGiftCard.barcodeImageUrl.isEmpty, "barcodeImageUrl should be present")
                XCTAssertFalse(orderItemGiftCard.brandName.isEmpty, "brandName should be present")
                XCTAssert(orderItemGiftCard.giftCardId > 0, "giftCardId should > 0")
                XCTAssertFalse(orderItemGiftCard.giftCardImageUrl.isEmpty, "giftCardImageUrl should be present")
                XCTAssertFalse(orderItemGiftCard.giftCardNumber.isEmpty, "giftCardNumber should be present")
                XCTAssert(orderItemGiftCard.giftCardStatus == .Activated, "giftCardStatus should be Activated")
                XCTAssertFalse(orderItemGiftCard.giftCardUrl.isEmpty, "giftCardUrl should be present")
                XCTAssertFalse(orderItemGiftCard.token.isEmpty, "token should be present")
                //TODO: Test pin/cardPin
            }
            if let orderId = order?.id {
                InCommOrdersService.order(order!.id) { (order, error) -> Void in
                    if error != nil {
                        print(error!.localizedDescription)
                        XCTAssert(false, "Request failed")
                        expectation.fulfill()
                        return
                    }
                    XCTAssert(order != nil, "Order should have been present")
                    XCTAssertEqual(orderId, order!.id, "Order ids should have been same")
                    expectation.fulfill()
                }
            }
            else {
                expectation.fulfill()
            }
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }
    
    func testSubmitReloadOrder() {
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        let sharedConfig = InCommSDKTestsConfigurations.sharedInCommSDKTestsConfigurations
        let emailAddress = sharedConfig.InCommUserEmailAddress
        let cardId = sharedConfig.InCommTestCardId
        let cardPin = sharedConfig.InCommTestCardPin
        let brandId = sharedConfig.InCommTestBrandId
        let creditCardNumber = sharedConfig.InCommUserCreditCardNumber
        let zip = sharedConfig.InCommUserCreditCardZip
        let cvv = sharedConfig.InCommUserCreditCardCVV
        let expirationMonth = sharedConfig.InCommUserCreditCardExpiryMonth
        let expirationYear = sharedConfig.InCommUserCreditCardExpiryYear
        let creditCardType = sharedConfig.InCommUserCreditCardType
        //Request Data
        let purchaser = InCommOrderPurchaser(firstName: "JMB1", lastName: "JMB2", emailAddress: emailAddress, country: "US")
        let amount = 11.0
        let payment: InCommSubmitPayment!
        if sharedConfig.InCommNoFundsCollected {
            payment = InCommSubmitPayment(amount: amount, orderPaymentMethod: .NoFundsCollected, creditCardType:creditCardType)
        }
        else {
            payment = InCommSubmitPayment(amount: amount, orderPaymentMethod: .CreditCard, firstName: "JMB5", lastName: "JMB6", streetAddress1: "StreetAddr1", city: "City1", stateProvince: "State1", country: "US", zipPostalCode: zip, creditCardNumber: creditCardNumber, creditCardVerificationCode: cvv, creditCardExpirationMonth: expirationMonth, creditCardExpirationYear: expirationYear, creditCardType: creditCardType)
            
        }
        let reloadOrder = InCommReloadOrder(cardId: cardId, cardPin: cardPin, brandId: brandId, amount: amount, purchaser: purchaser, payment: payment,paymentWithId: nil)
        InCommOrdersService.submitReloadOrder(reloadOrder) { (order, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            XCTAssert(order != nil, "Order should have been present")
            XCTAssertFalse(order!.id.isEmpty, "id should have been present")
            XCTAssert(order!.result == .Success, "result should have been Success")
            XCTAssert(order!.submittedOrderItemGiftCards != nil, "submittedOrderItemGiftCards should have been present")
            //TODO: Not sure if submittedOrderItemGiftCards is [] or nil
            XCTAssert(order!.submittedOrderItemGiftCards == nil, "submittedOrderItemGiftCards should have been nil")
            //TODO: Check cardBalance and cardBalanceDate
            XCTAssert(order!.cardBalance != nil, "cardBalance should have been present")
            XCTAssert(order!.cardBalance > 0, "cardBalance should have been > 0")
            XCTAssert(order!.cardBalanceDate != nil, "cardBalanceDate should have been present")
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }
    
    func testPromoOrder(){
        let expectation = self.expectationWithDescription("InComm Service's Tests")
        let sharedConfig = InCommSDKTestsConfigurations.sharedInCommSDKTestsConfigurations
        let emailAddress = sharedConfig.InCommUserEmailAddress
        let brandId = sharedConfig.InCommTestBrandId
        //Request Data
        let purchaser = InCommOrderPurchaser(firstName: "JMB1", lastName: "JMB2", emailAddress: emailAddress, country: "US")
        let amount = 11.0
        let quantity: UInt32 = 2
        let item = InCommOrderItem(brandId: brandId, amount: amount, quantity: quantity)
        let recipient = InCommOrderRecipientDetails(firstName: "JMB3", lastName: "JMB4", emailAddress: "r." + emailAddress, items: [item])
        let payment: InCommSubmitPayment!
        payment = InCommSubmitPayment(amount: amount * Double(quantity), orderPaymentMethod: .NoFundsCollected, creditCardType: nil)
      
        let submitOrder = InCommSubmitOrder(purchaser: purchaser, recipients: [recipient], payment: payment, paymentWithId: nil)
        InCommOrdersService.submitPromoOrder(submitOrder) {(error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
        }
        self.waitForExpectationsWithTimeout(10, handler: nil)
    }
}