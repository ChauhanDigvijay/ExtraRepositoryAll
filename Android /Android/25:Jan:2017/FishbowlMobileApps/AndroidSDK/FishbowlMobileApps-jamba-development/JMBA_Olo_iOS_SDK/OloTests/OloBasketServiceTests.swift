//
//  OloBasketServiceTests.swift
//  Olo
//
//  Created by Taha Samad on 5/5/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import XCTest
@testable import Olo

class OloBasketServiceTests: OloTestCase {

    //This will create a new basket, so only run when you need to test this.
    /*func testBasketAPICalls() {
        let expectation = self.expectationWithDescription("OLOService")
        OloSessionService.authToken = nil
        helperGetRestaurants(expectation)
        self.waitForExpectationsWithTimeout(30, handler: nil)
    }*/
    
    func testBillingScheme() {
        let expectation = self.expectationWithDescription("OLOService")
        OloRestaurantService.restaurants { (restaurants, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
            }
            else if restaurants.count == 0 {
                XCTAssert(false, "Could not find any restaurants")
                expectation.fulfill()
            }
            else {
                let restaurant = restaurants[0]
                let sharedOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
                let emailAddress = sharedOloSDKTestsConfigurations.OloUserEmailAddress
                let password = sharedOloSDKTestsConfigurations.OloUserPassword
                OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
                    if error != nil {
                        print(error!.localizedDescription)
                        XCTAssert(false, "Request failed")
                        expectation.fulfill()
                    }
                    else if user == nil {
                        XCTAssert(false, "User should have been returned")
                        expectation.fulfill()
                    }
                    else {
                        OloBasketService.createBasket(restaurant.id) { (basket, error) -> Void in
                            if error != nil {
                                print(error!.localizedDescription)
                                XCTAssert(false, "Request failed")
                                expectation.fulfill()
                            }
                            else if basket == nil {
                                XCTAssert(false, "Basket should have been returned")
                                expectation.fulfill()
                            }
                            else {
                                OloBasketService.billingSchemes(basket!.id) { (billingSchemes, error) -> Void in
                                    if error != nil {
                                        print(error!.localizedDescription)
                                        XCTAssert(false, "Request failed")
                                        expectation.fulfill()
                                    }
                                    else {
                                        XCTAssertTrue(billingSchemes.count > 0, "Billing Scheme shoul have been returned")
                                        expectation.fulfill()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        self.waitForExpectationsWithTimeout(120, handler: nil)
    }
    
     func testSubmitBasket() {
        let expectation = self.expectationWithDescription("OLOService")
        var basketSubmitBody = OloBasketSubmitBody()
        //
        basketSubmitBody.billingMethod = "storedvalue"
        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
        
        //
        basketSubmitBody.userType = "user"
         basketSubmitBody.authToken = "O2aMcNeqdHTC4JN3eVIDcxnnHACsUYmA"
        basketSubmitBody.billingSchemeId = 33
        
        var billingFieldForPin = OloBillingField()
        billingFieldForPin.name = "pin"
        billingFieldForPin.value = "710000sample00377"
        basketSubmitBody.billingFields.append(billingFieldForPin)
        
        var billingFieldForNumber = OloBillingField()
        billingFieldForNumber.name = "number"
        billingFieldForNumber.value = "710000sample00377"
        basketSubmitBody.billingFields.append(billingFieldForNumber)
        
        
        
        OloBasketService.submit(shareOloSDKTestsConfigurations.OloUserBasketId, basketSubmitBody: basketSubmitBody) { (orderStatus, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
            }
            else
            {
                XCTAssert(orderStatus != nil, "order should have been placed")
            }
            expectation.fulfill()
        }
        self.waitForExpectationsWithTimeout(120, handler: nil)
    }
    
    /*func testCreateFromOrder() {
        let expectation = self.expectationWithDescription("OLOService")
        let sharedOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
        let emailAddress = sharedOloSDKTestsConfigurations.OloUserEmailAddress
        let password = sharedOloSDKTestsConfigurations.OloUserPassword
        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
            }
            else if user == nil {
                XCTAssert(false, "User should have been returned")
                expectation.fulfill()
            }
            else {
                OloUserService.recentOrders { (status, error) -> Void in
                    if error != nil {
                        print(error!.localizedDescription)
                        XCTAssert(false, "Request failed")
                        expectation.fulfill()
                    }
                    else if status.count == 0 {
                        XCTAssert(false, "Recent Orders Status should have been returned")
                        expectation.fulfill()
                    }
                    else {
                        OloBasketService.createFromOrderStatusId(status[0].id) { (basket, error) -> Void in
                            if error != nil {
                                print(error!.localizedDescription)
                                XCTAssert(false, "Request failed")
                                expectation.fulfill()
                            }
                            else if basket == nil {
                                XCTAssert(false, "Basket should have been returned")
                                expectation.fulfill()
                            }
                            else {
                                XCTAssertEqual(basket!.products.count, status[0].products.count, "Count of products should have equal")
                                expectation.fulfill()
                            }
                        }
                    }
                }
            }
        }
        self.waitForExpectationsWithTimeout(30, handler: nil)
    }*/

//    func testLoyaltySchemes() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        let basketId = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations.OloUserBasketId
//
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//                expectation.fulfill()
//                return
//            }
//            OloBasketService.loyaltySchemes(basketId, callback: { (loyaltySchemes, error) -> Void in
//                if error != nil {
//                    print(error!.localizedDescription)
//                    XCTAssert(false, "Request failed")
//                    expectation.fulfill()
//                    return
//                }
//                print(loyaltySchemes)
//                expectation.fulfill()
//            })
//        }
//        self.waitForExpectationsWithTimeout(10, handler: nil)
//    }

//    func testLoyaltyRewards() {
//        let expectation = self.expectationWithDescription("OLOService")
//        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
//        let emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
//        let password = shareOloSDKTestsConfigurations.OloUserPassword
//        let basketId = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations.OloUserBasketId
//        
//        OloSessionService.authenticateUser(emailAddress, password: password, basketId: nil) { (user, error) in
//            if error != nil {
//                print(error!.localizedDescription)
//                XCTAssert(false, "Request failed")
//                expectation.fulfill()
//                return
//            }
//            OloBasketService.loyaltySchemes(basketId, callback: { (loyaltySchemes, error) -> Void in
//                if error != nil {
//                    print(error!.localizedDescription)
//                    XCTAssert(false, "Request failed")
//                    expectation.fulfill()
//                    return
//                }
//                let membershipId = loyaltySchemes.first?.membership?.id
//                if membershipId == nil {
//                    print(loyaltySchemes)
//                    XCTAssert(false, "Could not find membershipId")
//                    expectation.fulfill()
//                    return
//                }
//                OloBasketService.loyaltyRewards(basketId, membershipId: membershipId!, callback: { (loyaltyRewards, error) -> Void in
//                    if error != nil {
//                        print(error!.localizedDescription)
//                        XCTAssert(false, "Request failed")
//                        expectation.fulfill()
//                        return
//                    }
//                    print(loyaltyRewards)
//                    expectation.fulfill()
//                })
//            })
//        }
//        self.waitForExpectationsWithTimeout(10, handler: nil)
//    }


    // MARK: Helper methods
    
    private func helperGetRestaurants(expectation: XCTestExpectation) {
        OloRestaurantService.restaurants { (restaurants, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
            }
            else if restaurants.count == 0 {
                XCTAssert(false, "Could not find any restaurants")
                expectation.fulfill()
            }
            else {
                let restaurant = restaurants[0]
                self.helperGetRestaurantMenu(expectation, restaurant: restaurant)
            }
        }
    }
    
    private func helperGetRestaurantMenu(expectation: XCTestExpectation, restaurant: OloRestaurant) {
        OloMenuService.restaurantMenu(restaurant) { (categories, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
            }
            else if categories.count == 0 {
                XCTAssert(false, "Could not find any category in first restaurant's menu")
                expectation.fulfill()
            }
            else {
                let category = categories[0]
                if category.products.count == 0 {
                    XCTAssert(false, "No product in first category")
                    expectation.fulfill()
                }
                else {
                    let product = category.products[0]
                    self.helperGetProductModifiers(expectation, restaurant: restaurant, product: product)
                }
            }
        }
    }

    private func helperGetProductModifiers(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct) {
        
        OloMenuService.productModifiers(product) { (modifiers, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
            }
            else {
                self.helperMakeBasket(expectation, restaurant: restaurant, product: product, modifiers: modifiers)
            }
        }
    }
    
    private func helperMakeBasket(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier]) {
        
        OloBasketService.createBasket(restaurant.id) { (basket, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
            }
            else if basket == nil {
                XCTAssert(false, "Basket should be not nil")
                expectation.fulfill()
            }
            else {
                XCTAssertNotNil(basket!.id, "basket.id should be not nil")
                self.helperAddBasketProduct(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!)
            }
        }
    }
    
    private func helperAddBasketProduct(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, proceedToSubmit: Bool = false) {
        
        let originalBasketId = basket.id.lowercaseString
        var newBasketProduct = OloNewBasketProduct()
        newBasketProduct.productId = product.id
        var optionString = ""
        for modifier in modifiers {
            if modifier.mandatory {
                optionString += "\(modifier.options[0].id),"
            }
        }
        if !optionString.isEmpty {
            optionString = optionString.substringToIndex(optionString.endIndex.predecessor())
        }
        newBasketProduct.options = optionString
        newBasketProduct.quantity = 1
        newBasketProduct.specialInstructions = "special1"
        OloBasketService.addProduct(basket.id, newBasketProduct: newBasketProduct) { (basket, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            else if basket == nil {
                XCTAssert(false, "Basket should be not nil")
                expectation.fulfill()
            }
            else {
                XCTAssertEqual(basket!.id, originalBasketId,"basket.id should be same as original")
                XCTAssertTrue(basket!.products.count == 1, "count should be 1")
                XCTAssertTrue(basket!.products[0].productId == product.id, "product id should be same")
                XCTAssertTrue(basket!.products[0].specialInstructions == newBasketProduct.specialInstructions, "special instructions should be same")
                if !proceedToSubmit {
                    self.helperUpdateProductInBasket(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
                }
                else {
                    self.helperUpdateTimeWanted(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
                }
            }
        }
    }
    
    private func helperUpdateProductInBasket(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct) {
        
        let originalBasketId = basket.id.lowercaseString
        var productWithInstructions = newBasketProduct
        productWithInstructions.specialInstructions = "special2"
        OloBasketService.updateProduct(basket.id, basketProductId: basket.products[0].id, newBasketProduct: productWithInstructions) { (basket, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            else if basket == nil {
                XCTAssert(false, "Basket should be not nil")
                expectation.fulfill()
            }
            else {
                XCTAssertEqual(basket!.id, originalBasketId,"basket.id should be same as original")
                XCTAssertTrue(basket!.products.count == 1, "count should be 1")
                XCTAssertTrue(basket!.products[0].specialInstructions == productWithInstructions.specialInstructions, "special instructions should be same")
                self.helperDeleteProductFromBasket(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
            }
        }
    }
        
    private func helperDeleteProductFromBasket(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct) {
        
        let originalBasketId = basket.id.lowercaseString
        OloBasketService.removeProduct(basket.id, basketProductId: basket.products[0].id) { (basket, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            else if basket == nil {
                XCTAssert(false, "Basket should be not nil")
                expectation.fulfill()
            }
            else {
                XCTAssertEqual(basket!.id, originalBasketId,"basket.id should be same as original")
                XCTAssertTrue(basket!.products.count == 0, "count should be 0")
                //Add as batch
                //self.helperAddProductToBasketAsBatch(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
                //Add simple
                self.helperAddBasketProduct(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, proceedToSubmit: true)
            }
        }
    }
    
    private func helperAddProductToBasketAsBatch(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct) {
        
        let originalBasketId = basket.id.lowercaseString
        OloBasketService.addProducts(basket.id, newBasketProducts: [newBasketProduct]) { (basketProductBatchResult, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            else if basketProductBatchResult == nil {
                XCTAssert(false, "basketProductBatchResult should be not nil")
                expectation.fulfill()
            }
            else {
                let unwrappedBasket = basketProductBatchResult!.basket
                XCTAssertEqual(unwrappedBasket.id, originalBasketId,"basket.id should be same as original")
                XCTAssertTrue(unwrappedBasket.products.count == 1, "count should be 0")
                self.helperUpdateTimeWanted(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket, newBasketProduct: newBasketProduct)
            }
        }
    }
    
    private func helperUpdateTimeWanted(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct) {
        
        let originalBasketId = basket.id.lowercaseString
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "YYYYMMdd HH:mm"
        var timeWanted = dateFormatter.dateFromString(basket.earliestReadyTime)!
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        var minute = calendar.component(.Minute, fromDate: timeWanted)
        if minute % 15 != 0 {
            let minutesToAdd = 15 - (minute % 15)
            timeWanted = timeWanted.dateByAddingTimeInterval(NSTimeInterval(minutesToAdd * 60))
            minute = calendar.component(.Minute, fromDate: timeWanted)
        }
        let day = calendar.component(.Day, fromDate: timeWanted)
        let month = calendar.component(.Month, fromDate: timeWanted)
        let year = calendar.component(.Year, fromDate: timeWanted)
        let hour = calendar.component(.Hour, fromDate: timeWanted)
        
        OloBasketService.updateTimeWanted(basket.id, isManualFire: false, year: Int64(year), month: Int64(month), day: Int64(day), hour: Int64(hour), minute: Int64(minute)) { (basket, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            else if basket == nil {
                XCTAssert(false, "Basket should be not nil")
                expectation.fulfill()
            }
            else {
                XCTAssertEqual(basket!.id, originalBasketId,"basket.id should be same as original")
                //1 Case of testing the time wanted deleted. Test should fail on validation
                //self.helperDeleteTimeWanted(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
                //2Proceed to validation step
                //self.helperBasketValidation(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
                //3Add Coupon Step
                self.helperApplyCoupon(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
            }
        }
    }
    
    private func helperDeleteTimeWanted(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct) {
    
        let originalBasketId = basket.id.lowercaseString
        OloBasketService.deleteTimeWanted(basket.id) { (basket, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
                return
            }
            else if basket == nil {
                XCTAssert(false, "Basket should be not nil")
                expectation.fulfill()
            }
            else {
                XCTAssertEqual(basket!.id, originalBasketId,"basket.id should be same as original")
                self.helperBasketValidation(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct, shouldFail: true)
            }
        }
    }
    
    private func helperApplyCoupon(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct) {
        let originalBasketId = basket.id.lowercaseString
        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
        let couponCode = shareOloSDKTestsConfigurations.OloCouponCode
        OloBasketService.applyCoupon(originalBasketId, couponCode: couponCode) { (basket, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
            }
            else if basket == nil {
                XCTAssert(false, "Basket should be not nil")
                expectation.fulfill()
            }
            else {
                XCTAssert(basket!.couponDiscount > 0, "couponDiscount should have been applied")
                self.helperDeleteCoupon(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
                
            }
        }
    }
    
    private func helperDeleteCoupon(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct) {
        let originalBasketId = basket.id.lowercaseString
        OloBasketService.deleteCoupon(originalBasketId) { (basket, error) -> Void in

            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
                expectation.fulfill()
            }
            else if basket == nil {
                XCTAssert(false, "Basket should be not nil")
                expectation.fulfill()
            }
            else {
                XCTAssert(basket!.couponDiscount <= 0, "couponDiscount should have been removed")
                self.helperBasketValidation(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: basket!, newBasketProduct: newBasketProduct)
            }
        }
    }
    
    private func helperBasketValidation(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct, shouldFail: Bool = false) {
    
        let originalBasketId = basket.id.lowercaseString
        OloBasketService.validate(originalBasketId) { (validation, error) -> Void in
            if shouldFail {
                XCTAssert(error != nil, "Error should have occured")
                expectation.fulfill()
            }
            else {
                if error != nil {
                    print(error!.localizedDescription)
                    XCTAssert(false, "Request failed")
                    expectation.fulfill()
                }
                else if validation == nil {
                    XCTAssert(false, "validation should be not nil")
                    expectation.fulfill()
                }
                else {
                    XCTAssertEqual(validation!.basketId, originalBasketId, "basket.id should be same as original")
                    var updatedBasket = basket
                    updatedBasket.total = validation!.total
                    updatedBasket.subTotal = validation!.subTotal
                    updatedBasket.salesTax = validation!.tax
                    self.helperSubmitBasket(expectation, restaurant: restaurant, product: product, modifiers: modifiers, basket: updatedBasket, newBasketProduct: newBasketProduct)
                }
            }
        }
    }
        
    private func helperSubmitBasket(expectation: XCTestExpectation, restaurant: OloRestaurant, product: OloProduct, modifiers: [OloModifier], basket: OloBasket, newBasketProduct: OloNewBasketProduct) {
        
        var basketSubmitBody = OloBasketSubmitBody()
        //
        basketSubmitBody.billingMethod = "creditcard"
        let shareOloSDKTestsConfigurations = OloSDKTestsConfigurations.sharedOloSDKTestsConfigurations
        basketSubmitBody.cardNumber = shareOloSDKTestsConfigurations.OloUserCreditCardNumber
        basketSubmitBody.expiryMonth = shareOloSDKTestsConfigurations.OloUserCreditCardExpiryMonth
        basketSubmitBody.expiryYear = shareOloSDKTestsConfigurations.OloUserCreditCardExpiryYear
        basketSubmitBody.cvv = shareOloSDKTestsConfigurations.OloUserCreditCardCVV
        basketSubmitBody.zip = shareOloSDKTestsConfigurations.OloUserCreditCardZip
        basketSubmitBody.saveOnFile = "false"
        //
        basketSubmitBody.userType = "guest"
        //Setup Values
        basketSubmitBody.firstName = shareOloSDKTestsConfigurations.OloUserFirstName
        basketSubmitBody.lastName = shareOloSDKTestsConfigurations.OloUserLastName
        basketSubmitBody.emailAddress = shareOloSDKTestsConfigurations.OloUserEmailAddress
        basketSubmitBody.contactNumber = shareOloSDKTestsConfigurations.OloUserPhoneNumber
        basketSubmitBody.reference = "ref"
        
        OloBasketService.submit(basket.id, basketSubmitBody: basketSubmitBody) { (orderStatus, error) -> Void in
            if error != nil {
                print(error!.localizedDescription)
                XCTAssert(false, "Request failed")
            }
            else
            {
                XCTAssert(orderStatus != nil, "order should have been placed")
            }
            expectation.fulfill()
        }
    }
    
}
