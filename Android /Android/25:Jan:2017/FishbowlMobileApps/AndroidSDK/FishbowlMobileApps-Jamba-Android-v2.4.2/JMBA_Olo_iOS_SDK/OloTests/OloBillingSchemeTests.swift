//
//  OloBillingSchemeTests.swift
//  Olo
//
//  Created by Taha Samad on 23/06/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
import SwiftyJSON
@testable import Olo

class OloBillingSchemeTests: OloTestCase {
    
    func testInitWithJSON() {
        var dict: [String: AnyObject] = ["id": 1 as AnyObject, "name": "schemename" as AnyObject, "type": "schemetype" as AnyObject]
        var accountDictArray: [[String: AnyObject]] = []
        for i in 0..<2 {
            let accountDict: [String : AnyObject] = ["accountid": (100 + i) as AnyObject, "accounttype": "accounttype\(i)" as AnyObject, "cardtype": "cardtype\(i)" as AnyObject, "cardsuffix": "cardsuffix\(i)" as AnyObject, "description": "description\(i)" as AnyObject, "expiration": "expiration\(i)" as AnyObject]
            accountDictArray.append(accountDict)
        }
        dict["accounts"] = accountDictArray as AnyObject?
        let json = JSON(dict)
        //Tests
        let oloBillingScheme = OloBillingScheme(json: json)
        //Asserts
        XCTAssertEqual(oloBillingScheme.id, (dict["id"] as! NSNumber).int64Value, "id should have been same")
        XCTAssertEqual(oloBillingScheme.name, (dict["name"] as! String), "name should have been same")
        XCTAssertEqual(oloBillingScheme.type, (dict["type"] as! String), "type should have been same")
        XCTAssertEqual(oloBillingScheme.accounts.count, (dict["accounts"] as! [[String: AnyObject]]).count, "accounts.count should have been same")
        for i in 0..<2 {
            let oloBillingAccount = oloBillingScheme.accounts[i]
            let accountDict = (dict["accounts"] as! [[String: AnyObject]])[i]
            XCTAssertEqual(oloBillingAccount.accountId, (accountDict["accountid"] as! NSNumber).int64Value, "accountId should have been same")
            XCTAssertEqual(oloBillingAccount.accountType, (accountDict["accounttype"] as! String), "accountType should have been same")
            XCTAssertEqual(oloBillingAccount.cardType, (accountDict["cardtype"] as! String), "cardType should have been same")
            XCTAssertEqual(oloBillingAccount.cardSuffix, (accountDict["cardsuffix"] as! String), "cardSuffix should have been same")
            XCTAssertEqual(oloBillingAccount.desc, (accountDict["description"] as! String), "desc should have been same")
            XCTAssertEqual(oloBillingAccount.expiration, (accountDict["expiration"] as! String), "expiration should have been same")
        }
    }
}
