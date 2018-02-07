//
//  OloMenuServiceTests.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/22/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import XCTest
@testable import Olo

class OloMenuServiceTests: OloTestCase {

    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }

    func testRestaurantMenu() {
        let expectation = self.expectationWithDescription("OLOService")
        OloRestaurantService.restaurants { (restaurants, error) -> Void in
            if error != nil {
                XCTAssert(false, "Request failed")
                print(error!.localizedDescription)
                return
            }
            XCTAssert(restaurants.count > 0, "Got restaurants!")
            if restaurants.count > 0 {
                let restaurant = restaurants[0]
                OloMenuService.restaurantMenu(restaurant) { (categories, error) -> Void in
                    if error != nil {
                        XCTAssert(false, "Request failed")
                        print(error!.localizedDescription)
                        return
                    }
                    XCTAssert(categories.count > 0, "Got product categories!")
                    for category in categories {
                        XCTAssert(category.name.isEmpty == false, "Category has a name")
                        XCTAssert(category.products.count > 0, "Category has products")
                        for product in category.products {
                            XCTAssert(product.name.isEmpty == false, "Product has a name")
                            XCTAssert(product.id > 0, "Product has an Id")
                        }
                    }

                    expectation.fulfill()
                }
            }
        }
        self.waitForExpectationsWithTimeout(5.0, handler: nil)
    }
    
    func testProductModifiers() {
        self.helperTestProductModifiersOrOptions(true)
    }
    
    func testProductOptions() {
        self.helperTestProductModifiersOrOptions(false)
    }
    
    //MARK: Helper
    
    func helperTestProductModifiersOrOptions(shouldCheckModifiers: Bool) -> Void
    {
        let expectation = self.expectationWithDescription("OLOService")
        OloRestaurantService.restaurants { (restaurants, error) -> Void in
            if error != nil {
                XCTAssert(false, "Request failed")
                print(error!.localizedDescription)
                expectation.fulfill()
                return
            }
            XCTAssert(restaurants.count > 0, "Unable to get restaurants!")
            if restaurants.count > 0 {
                let restaurant = restaurants[0]
                OloMenuService.restaurantMenu(restaurant) { (categories, error) -> Void in
                    if error != nil {
                        XCTAssert(false, "Request failed")
                        print(error!.localizedDescription)
                        expectation.fulfill()
                        return
                    }
                    XCTAssert(categories.count > 0, "Unable to get product categories!")
                    if categories.count > 0 {
                        let category = categories[0]
                        XCTAssert(category.products.count > 0, "Category has no products")
                        let products = category.products
                        if products.count > 0 {
                            let product = products[0]
                            let callback:OloModifierCallback = { ( modifiers, error) -> Void in
                                if error != nil {
                                    XCTAssert(false, "Request failed")
                                    print(error!.localizedDescription)
                                    expectation.fulfill()
                                    return
                                }
                                if modifiers.count > 0 {
                                    for modifier in modifiers {
                                        self.helperTestModifier(modifier)
                                    }
                                }
                                expectation.fulfill()
                            }
                            if shouldCheckModifiers {
                                OloMenuService.productModifiers(product, callback: callback)
                            }
                            else {
                                OloMenuService.productOptions(product, callback: callback)
                            }
                            
                        }
                    }
                }
            }
        }
        self.waitForExpectationsWithTimeout(10.0, handler: nil)
    }
    
    private func helperTestModifier(modifier: OloModifier) -> Void {
        XCTAssert(modifier.id > 0, "Modifier should have id")
        XCTAssertFalse(modifier.desc.isEmpty, "Modifier should have non-empty desc")
//        Not Necessarily filled
//        XCTAssertFalse(modifier.parentChoiceId.isEmpty, "Modifier should have non-empty parentChoiceId")
//        XCTAssertFalse(modifier.minSelects.isEmpty, "Modifier should have non-empty minSelects")
//        XCTAssertFalse(modifier.maxSelects.isEmpty, "Modifier should have non-empty maxSelects")
        for option in modifier.options {
            self.helperTestOption(option)
        }
    }
    
    private func helperTestOption(option: OloOption) -> Void {
        XCTAssert(option.id > 0, "Option should have id")
        XCTAssertFalse(option.name.isEmpty, "Option should have non-empty name")
        XCTAssert(option.cost > 0, "Option should have cost")
        for field in option.fields {
            self.helperTestCustomField(field)
        }
        for modifier in option.modifiers {
            self.helperTestModifier(modifier)
        }
    }
    
    private func helperTestCustomField(customField: OloCustomField) -> Void {
        XCTAssert(customField.id > 0, "CustomField should have id")
        XCTAssertFalse(customField.label.isEmpty, "CustomField should have non-empty label")
        XCTAssert(customField.maxLength > 0, "CustomField should have maxLength")
    }

}
