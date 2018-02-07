//
//  OloUserTests.swift
//  JambaJuice
//
//  Created by Taha Samad on 24/04/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import XCTest
import SwiftyJSON
@testable import Olo

class OloUserTests: OloTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testInitWithJSON() -> Void {
        let userDicts = self.helperUserDictionaryArrayWithCount(3)
        for userDict in userDicts {
            let json = JSON(userDict)
            let user = OloUser(json: json)
            self.helperCompareUserWithDictionary(user, dictionary: userDict)
        }
    }

    //MARK:Helper
    
    func helperUserDictionaryArrayWithCount(count: UInt64) -> [[String: AnyObject]] {
        var dictArray = [[String: AnyObject]]()
        for i in 0..<count {
            dictArray.append(self.helperUserDictionaryWithIndex(i))
        }
        return dictArray
    }
    
    func helperUserDictionaryWithIndex(index: UInt64) -> [String: AnyObject] {
        var dict: [String: AnyObject] = [
            "firstname": "firstName\(index)",
            "lastname": "lastName\(index)",
            "emailaddress": "email\(index)@email.com"
        ]
        if (index % 2) == 0 {
            dict["authtoken"] = "authToken\(index)"
            dict["cardsuffix"] = "cardsuffix\(index)"
        }
        if (index % 2) == 1 {
            dict["basketid"] = "basketId\(index)"
            dict["contactnumber"] = "11122233\(index)"
        }
        if (index % 3) == 0 {
            dict["reference"] = "reference\(index)"
        }
        return dict
    }
    
    func helperCompareUserWithDictionary(user: OloUser, dictionary:[String: AnyObject]) {
        XCTAssertEqual(user.firstName, (dictionary["firstname"] as! String), "firstName should be equal")
        XCTAssertEqual(user.lastName, (dictionary["lastname"] as! String), "lastName should be equal")
        XCTAssertEqual(user.emailAddress, (dictionary["emailaddress"] as! String), "emailAddress should be equal")
        let dictAuthToken = dictionary["authtoken"] as? String
        if !((dictAuthToken == nil && user.authToken == nil) || (dictAuthToken! == user.authToken!)) {
            XCTAssert(false, "authToken should be equal")
        }
        let dictCardSuffix = dictionary["cardsuffix"] as? String
        if !((dictCardSuffix == nil && user.cardSuffix == nil) || (dictCardSuffix! == user.cardSuffix!)) {
            XCTAssert(false, "cardSuffix should be equal")
        }
        let dictBasketId = dictionary["basketid"] as? String
        if !((dictBasketId == nil && user.baskedId == nil) || (dictBasketId! == user.baskedId!)) {
            XCTAssert(false, "baskedId should be equal")
        }
        let dictContactNumber = dictionary["contactnumber"] as? String
        if !((dictContactNumber == nil && user.contactNumber == nil) || (dictContactNumber! == user.contactNumber!)) {
            XCTAssert(false, "contactNumber should be equal")
        }
        let dictReference = dictionary["reference"] as? String
        if !((dictReference == nil && user.reference == nil) || (dictReference! == user.reference!)) {
            XCTAssert(false, "reference should be equal")
        }
    }

}
