//
//  OloBasketSubmitBodyTestCases.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
@testable import Olo

class OloBasketSubmitBodyTestCases: OloTestCase {

    func testSerializeAsJSONDictionary() {
        var basketSubmitBody = OloBasketSubmitBody()
        basketSubmitBody.userType = "user"
        basketSubmitBody.authToken = "at"
        basketSubmitBody.firstName = "First"
        basketSubmitBody.lastName = "Last"
        basketSubmitBody.emailAddress = "email"
        basketSubmitBody.contactNumber = "111"
        basketSubmitBody.reference = "ref"
        //
        basketSubmitBody.billingMethod = "cash"
        basketSubmitBody.billingAccountId = "acc"
        basketSubmitBody.cardNumber = "cardno"
        basketSubmitBody.expiryYear = "2020"
        basketSubmitBody.expiryMonth = "12"
        basketSubmitBody.cvv = "cvv1"
        basketSubmitBody.zip = "xy123"
        basketSubmitBody.saveOnFile = "true"
        //
        basketSubmitBody.orderRef = "ordref"
        //Test
        let dict = basketSubmitBody.serializeAsJSONDictionary()
        XCTAssertEqual((dict["usertype"] as! String), "user", "usertype should be equal")
        XCTAssertEqual((dict["authtoken"] as! String), "at", "authtoken should be equal")
        XCTAssertEqual((dict["firstname"] as! String), "First", "firstname should be equal")
        XCTAssertEqual((dict["lastname"] as! String), "Last", "lastname should be equal")
        XCTAssertEqual((dict["emailaddress"] as! String), "email", "email should be equal")
        XCTAssertEqual((dict["contactnumber"] as! String), "111", "contactnumber should be equal")
        XCTAssertEqual((dict["reference"] as! String), "ref", "reference should be equal")
        //
        XCTAssertEqual((dict["billingmethod"] as! String), "cash", "billingmethod should be equal")
        XCTAssertEqual((dict["billingaccountid"] as! String), "acc", "billingaccountid should be equal")
        XCTAssertEqual((dict["cardnumber"] as! String), "cardno", "cardnumber should be equal")
        XCTAssertEqual((dict["expiryyear"] as! String), "2020", "expiryyear should be equal")
        XCTAssertEqual((dict["expirymonth"] as! String), "12", "expirymonth should be equal")
        XCTAssertEqual((dict["cvv"] as! String), "cvv1", "cvv should be equal")
        XCTAssertEqual((dict["zip"] as! String), "xy123", "zip should be equal")
        XCTAssertEqual((dict["saveonfile"] as! String), "true", "zip should be equal")
        XCTAssertEqual((dict["orderref"] as! String), "ordref", "orderref should be equal")
    }
    
    func testValidateValues() {
        var basketSubmitBody = OloBasketSubmitBody()
        //1
        basketSubmitBody.billingMethod = "cash"
        basketSubmitBody.userType = "xyz"
        var error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //2
        basketSubmitBody.userType = "user"
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //3
        basketSubmitBody.userType = "user"
        basketSubmitBody.authToken = "at"
        error = basketSubmitBody.validateValues()
        XCTAssertNil(error, "error should be nil")
        //4
        basketSubmitBody = OloBasketSubmitBody()
        basketSubmitBody.billingMethod = "cash"
        basketSubmitBody.userType = "guest"
        //Setup Values
        basketSubmitBody.firstName = "First"
        basketSubmitBody.lastName = "Last"
        basketSubmitBody.emailAddress = "email"
        basketSubmitBody.contactNumber = "111"
        basketSubmitBody.reference = "ref"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNil(error, "error should be nil")
        //5-1
        //Setup Values
        basketSubmitBody.firstName = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //5-2
        //Setup Values
        basketSubmitBody.firstName = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //6-1
        //Setup Values
        basketSubmitBody.firstName = "First"
        basketSubmitBody.lastName = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //6-2
        //Setup Values
        basketSubmitBody.lastName = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //7-1
        //Setup Values
        basketSubmitBody.lastName = "Last"
        basketSubmitBody.emailAddress = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //7-2
        //Setup Values
        basketSubmitBody.emailAddress = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //8-1
        //Setup Values
        basketSubmitBody.emailAddress = "email"
        basketSubmitBody.contactNumber = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //8-2
        //Setup Values
        basketSubmitBody.contactNumber = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //9-1
        //Setup Values
        basketSubmitBody.contactNumber = "111"
        basketSubmitBody.reference = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //9-2
        //Setup Values
        basketSubmitBody.reference = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //9-3
        //Setup Values
        basketSubmitBody.reference = "ref"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNil(error, "error should be nil")
        //10-1
        basketSubmitBody.billingMethod = "billingaccount"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //10-2
        basketSubmitBody.billingAccountId = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //10-3
        basketSubmitBody.billingAccountId = "acc"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNil(error, "error should be nil")
        //11
        basketSubmitBody.billingAccountId = nil
        basketSubmitBody.billingMethod = "creditcard"
        basketSubmitBody.cardNumber = "cardno"
        basketSubmitBody.expiryYear = "2020"
        basketSubmitBody.expiryMonth = "12"
        basketSubmitBody.cvv = "cvv1"
        basketSubmitBody.zip = "xy123"
        basketSubmitBody.saveOnFile = "true"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNil(error, "error should be nil")
        //12-1
        basketSubmitBody.cardNumber = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //12-2
        basketSubmitBody.cardNumber = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //13-1
        basketSubmitBody.cardNumber = "cardno"
        basketSubmitBody.expiryYear = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //13-2
        basketSubmitBody.expiryYear = "1899"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //13-3
        basketSubmitBody.expiryYear = "2100"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //14-1
        basketSubmitBody.expiryYear = "2020"
        basketSubmitBody.expiryMonth = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //14-2
        basketSubmitBody.expiryMonth = "0"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //14-3
        basketSubmitBody.expiryMonth = "13"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //15-1
        basketSubmitBody.expiryMonth = "12"
        basketSubmitBody.cvv = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //15-2
        basketSubmitBody.cvv = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //16-1
        basketSubmitBody.cvv = "cvv1"
        basketSubmitBody.zip = nil
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //16-2
        basketSubmitBody.zip = ""
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //17-1
        basketSubmitBody.zip = "xy123"
        basketSubmitBody.saveOnFile = "abc"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNotNil(error, "error should be not nil")
        //17-2
        basketSubmitBody.saveOnFile = "false"
        //Test
        error = basketSubmitBody.validateValues()
        XCTAssertNil(error, "error should be nil")
    }

}
