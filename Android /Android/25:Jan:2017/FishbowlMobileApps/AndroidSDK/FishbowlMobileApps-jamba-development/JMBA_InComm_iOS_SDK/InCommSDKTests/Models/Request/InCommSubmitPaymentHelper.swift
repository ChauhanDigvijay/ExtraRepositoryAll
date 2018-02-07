//
//  InCommSubmitPaymentHelper.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/19/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK

class InCommSubmitPaymentHelper {
    
    class func createSubmitPaymentWithDiffrentiatingNumber(num: Int) -> InCommSubmitPayment {
        //Payment
        let amount = Double(num) + 0.1
        let month = UInt16(num)
        let year = UInt16(2020 + num)
        let firstName = "FN\(num)"
        let lastName = "LN\(num)"
        let streetAddress1 = "\(num)"
        let city = "City\(num)"
        let state = "State\(num)"
        let country = "CN\(num)"
        let zipPostalCode = "Zip\(num)"
        let creditCardNumber = "CCN\(num)"
        let creditCardVerificationCode = "CVV\(num)"
        var payment = InCommSubmitPayment(amount: amount, orderPaymentMethod: .CreditCard, firstName: firstName, lastName: lastName, streetAddress1: streetAddress1, city: city, stateProvince: state, country: country, zipPostalCode: zipPostalCode, creditCardNumber: creditCardNumber, creditCardVerificationCode: creditCardVerificationCode, creditCardExpirationMonth: month, creditCardExpirationYear: year, creditCardType: "VISA")
        payment.paymentAccountId = 10 + Int32(num)
        let randomBoolPayment = arc4random() % 2 == 0
        payment.paymentReceived = randomBoolPayment
        payment.streetAddress2 = "StreetAddr2-\(num)"
        payment.vestaOrgId = "VOId\(num)"
        payment.vestaWebSessionId = "VWSId\(num)"
        var mobileDevice = InCommOrderPaymentMobileDevice()
        mobileDevice.id = "MDId\(num)"
        mobileDevice.latitude = Double(num) + 0.2
        mobileDevice.longitude = Double(num) + 0.3
        payment.mobileDevice = mobileDevice
        return payment
    }
    
    class func testSubmitPayment(submitPayment: InCommSubmitPayment) {
        testSubmitPaymentJSONDict(submitPayment.serializeAsJSONDictionary(), submitPayment: submitPayment)
    }
    
    class func testSubmitPaymentJSONDict(submitPaymentJSONDict: InCommJSONDictionary, submitPayment: InCommSubmitPayment) {
        
        XCTAssertEqual(submitPayment.amount, submitPaymentJSONDict["Amount"] as? Double, "Amount should have been same")
        XCTAssertEqual(submitPayment.orderPaymentMethod?.rawValue, submitPaymentJSONDict["OrderPaymentMethod"] as? String, "OrderPaymentMethod should have been same")
        XCTAssertEqual(submitPayment.firstName, submitPaymentJSONDict["FirstName"] as? String, "FirstName should have been same")
        XCTAssertEqual(submitPayment.lastName, submitPaymentJSONDict["LastName"] as? String, "LastName should have been same")
        XCTAssertEqual(submitPayment.streetAddress1, submitPaymentJSONDict["StreetAddress1"] as? String, "StreetAddress1 should have been same")
        XCTAssertEqual(submitPayment.city, submitPaymentJSONDict["City"] as? String, "City should have been same")
        XCTAssertEqual(submitPayment.stateProvince, submitPaymentJSONDict["StateProvince"] as?String, "StateProvince should have been same")
        XCTAssertEqual(submitPayment.country, submitPaymentJSONDict["Country"] as? String, "Country should have been same")
        XCTAssertEqual(submitPayment.zipPostalCode, submitPaymentJSONDict["ZipPostalCode"] as? String, "ZipPostalCode should have been same")
        XCTAssertEqual(submitPayment.creditCardNumber, submitPaymentJSONDict["CreditCardNumber"] as? String, "CreditCardNumber should have been same")
        XCTAssertEqual(submitPayment.creditCardVerificationCode, submitPaymentJSONDict["CreditCardVerificationCode"] as? String, "CreditCardVerificationCode should have been same")
        XCTAssertEqual(submitPayment.creditCardExpirationMonth!, (submitPaymentJSONDict["CreditCardExpirationMonth"] as? NSNumber)?.unsignedShortValue, "CreditCardExpirationMonth should have been same")
        XCTAssertEqual(submitPayment.creditCardExpirationYear!, (submitPaymentJSONDict["CreditCardExpirationYear"] as? NSNumber)?.unsignedShortValue, "CreditCardExpirationYear should have been same")
        XCTAssertEqual(submitPayment.creditCardType!, submitPaymentJSONDict["CreditCardType"] as? String, "CreditCardType should have been same")
        XCTAssertEqual(submitPayment.creditCardType!,submitPaymentJSONDict["CreditCardType"] as? String, "CreditCardType should have been same")
        XCTAssertEqual(submitPayment.paymentAccountId!, (submitPaymentJSONDict["PaymentAccountId"] as? NSNumber)?.intValue, "PaymentAccountId should have been same")
        XCTAssertEqual(submitPayment.paymentReceived!, submitPaymentJSONDict["PaymentReceived"] as? Bool, "PaymentReceived should have been same")
        XCTAssertEqual(submitPayment.streetAddress2!, submitPaymentJSONDict["StreetAddress2"] as? String, "StreetAddress2 should have been same")
        XCTAssertEqual(submitPayment.vestaOrgId!, submitPaymentJSONDict["VestaOrgId"] as? String, "VestaOrgId should have been same")
        XCTAssertEqual(submitPayment.vestaWebSessionId!, submitPaymentJSONDict["VestaWebSessionId"] as? String, "VestaWebSessionId should have been same")
        //Mobile Device
        XCTAssertNotNil(submitPaymentJSONDict["MobileDevice"] as? InCommJSONDictionary, "MobileDevice should have been present")
        let mobileDeviceJSONDict = submitPaymentJSONDict["MobileDevice"] as? InCommJSONDictionary
        XCTAssertEqual(submitPayment.mobileDevice?.id, mobileDeviceJSONDict?["Id"] as? String, "Id should have been same")
        XCTAssertEqual(submitPayment.mobileDevice?.latitude, mobileDeviceJSONDict?["Latitude"] as? Double, "Latitude should have been same")
        XCTAssertEqual(submitPayment.mobileDevice?.longitude, mobileDeviceJSONDict?["Longitude"] as? Double, "Longitude should have been same")
        //
    }
}
