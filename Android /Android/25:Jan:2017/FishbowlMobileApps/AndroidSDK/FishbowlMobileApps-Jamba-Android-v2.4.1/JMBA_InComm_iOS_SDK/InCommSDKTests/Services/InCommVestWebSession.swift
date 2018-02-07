//
//  InCommVestWebSession.swift
//  InCommSDK
//
//  Created by VT010 on 10/4/16.
//  Copyright © 2016 Hathway. All rights reserved.
//

import Foundation
import InCommSDK
import XCTest

/*class InCommUserGiftCardServiceTests: InCommTestCase{
    
    func testVestWebSession(){
       let expectation = self.expectationWithDescription("InComm Service's Tests")
        InCommService.get("/VestaWebSessionId", parameters: nil) { (response, error) -> Void in
            XCTAssertNotNil(userGiftCards, "data should not be nil")
            XCTAssertNil(error, "error should be nil")
            
            if error != nil{
                print(error!.localizedDescription)
                return
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)

            if error != nil {
                callback(vestaWebSession: nil, error: error)
                return
            }
            let orgId = response["VestaOrgId"].stringValue
            let webSessionId = response["VestaWebSessionId"].stringValue
            let vestaWebSession = (vestaOrgId: orgId, vestaWebSessionId: webSessionId)
        }
    }
} */