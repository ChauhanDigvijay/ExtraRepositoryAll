//
//  OloFaveServiceTests.swift
//  Olo
//
//  Created by Taha Samad on 06/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
@testable import Olo

class OloFaveServiceTests: OloTestCase {

    //Using:
    //VendorID: 9496
    //productID: 769852,
    //modifierID: 5546663
    //optionID: 37698190
    //BasketID: AA42EBE2-4FB1-42D1-8D2D-F1517A16C39E
//    func testFaveAPICalls() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//                expectation.fulfill()
//            }
//            else {
//                let sharedOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//                let basketId = sharedOloSDKTestsConfigurations.OloUserBasketId
//                OloFaveService.createFave(basketId, description: "my custom description") { (faves, error) -> Void in
//                    if error != nil {
//                        print(error!.localizedDescription)
//                        XCTAssert(false, "Request failed")
//                        expectation.fulfill()
//                    }
//                    else if faves.count != 1 {
//                        XCTAssert(false, "Created fave not returned or too many faves returned")
//                        expectation.fulfill()
//                    }
//                    else
//                    {
//                        OloFaveService.faves { (fetchedFaves, error) -> Void in
//                            if error != nil {
//                                print(error!.localizedDescription)
//                                XCTAssert(false, "Request failed")
//                                expectation.fulfill()
//                            }
//                            else if faves.count != 1 {
//                                XCTAssert(false, "Created fave not returned or too many faves returned")
//                                expectation.fulfill()
//                            }
//                            else
//                            {
//                                XCTAssertEqual(fetchedFaves[0].id, faves[0].id, "fave id should be same")
//                                XCTAssertEqual(fetchedFaves[0].vendorId, 9496, "vendor id should be same")
//                                OloFaveService.deleteFave(fetchedFaves[0].id) { (error) -> Void in
//                                    if error != nil {
//                                        print(error!.localizedDescription)
//                                        XCTAssert(false, "Request failed")
//                                    }
//                                    expectation.fulfill()
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        self.waitForExpectationsWithTimeout(20, handler: nil)
//    }

}
