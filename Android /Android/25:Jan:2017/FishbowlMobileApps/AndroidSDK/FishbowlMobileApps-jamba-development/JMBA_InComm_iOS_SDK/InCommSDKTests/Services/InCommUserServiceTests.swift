//
//  InCommUserServiceTests.swift
//  InCommSDK
//
//  Created by vThink on 8/20/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK

class InCommUserServiceTests : InCommTestCase{
    
      let responseTimeHandler:NSTimeInterval     = 10
    
    func testGetInCommAuthToken(){
        let authToken = InCommUserService.getUserInCommAuthToken("3634177",secretKey: "UKGvqTkD6qxkSIWAvuzWTkMe8is+WJ8sAaWUehkHqwE=")
        print(authToken)
    }
    
    func testUserID(){
         let expectation = self.expectationWithDescription("InComm Service's Tests")
        InCommUserService.getInCommUserAccessToken { (inCommUserAccessToken, error) in
            
            if error != nil{
                XCTAssertNil(error, "error should be nil")
                print(error)
                 expectation.fulfill()
            }
            else{
                XCTAssertNotNil(inCommUserAccessToken, "data should not be nil")
                print(inCommUserAccessToken)
                expectation.fulfill()
            }
        }
         self.waitForExpectationsWithTimeout(responseTimeHandler, handler: nil)
    }
}