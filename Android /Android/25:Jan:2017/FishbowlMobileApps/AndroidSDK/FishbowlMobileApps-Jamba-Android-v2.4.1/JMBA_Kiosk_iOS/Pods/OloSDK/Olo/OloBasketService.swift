//
//  OloBasketService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

public typealias OloBasketCallback = (basket: OloBasket?, error: NSError?) -> Void
public typealias OloBasketProductBatchResultCallback = (result: OloBasketProductBatchResult?, error: NSError?) -> Void
public typealias OloBasketValidationCallback = (result: OloBasketValidation?, error: NSError?) -> Void
public typealias OloBasketSubmitCallback = (status: OloOrderStatus?, error: NSError?) -> Void
public typealias OloBasketBillingSchemesCallback = (billingSchemes: [OloBillingScheme], error: NSError?) -> Void
public typealias OloBasketLoyaltySchemesCallback = (loyaltySchemes: [OloLoyaltyScheme], error: NSError?) -> Void
public typealias OloBasketLoyaltyRewardsCallback = (loyaltyRewards: [OloLoyaltyReward], error: NSError?) -> Void


public class OloBasketService {
    
    public class func basketWithBasketId(basketId: String, callback: OloBasketCallback) {
        OloService.get("/baskets/\(basketId)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func createBasket(vendorId: Int64, callback: OloBasketCallback) {
        var parameters: [String : AnyObject] = [
            "vendorid": NSNumber(longLong: vendorId)
        ]
        
        if let authToken = OloSessionService.authToken {
            parameters["authtoken"] = authToken
        }
        OloService.post("/baskets/create", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func addProduct(basketId: String, newBasketProduct: OloNewBasketProduct, callback: OloBasketCallback) {
        let parameters = newBasketProduct.serializeAsJSONDictionary()
        OloService.post("/baskets/\(basketId)/products", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func addProducts(basketId: String, newBasketProducts: [OloNewBasketProduct],callback: OloBasketProductBatchResultCallback) {
        let parameters = ["products": newBasketProducts.map { $0.serializeAsJSONDictionary() }]
        OloService.post("/baskets/\(basketId)/products/batch", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(result: nil, error: error)
                return
            }
            
            let basketProductBatchResult = OloBasketProductBatchResult(json: response)
            callback(result: basketProductBatchResult, error: nil)
        }
    }
    
    public class func updateProduct(basketId: String, basketProductId: Int64, newBasketProduct: OloNewBasketProduct, callback: OloBasketCallback) {
        let parameters = newBasketProduct.serializeAsJSONDictionary()
        OloService.put("/baskets/\(basketId)/products/\(basketProductId)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func removeProduct(basketId: String, basketProductId: Int64, callback: OloBasketCallback) {
        OloService.delete("/baskets/\(basketId)/products/\(basketProductId)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    // Use 0,15,30,45 for minute
    public class func updateTimeWanted(basketId: String, isManualFire: Bool, year: Int64, month: Int64, day: Int64, hour: Int64, minute: Int64, callback: OloBasketCallback) {
        let parameters: [String : AnyObject] = [
            "ismanualfire": isManualFire,
            "year": NSNumber(longLong: year),
            "month": NSNumber(longLong: month),
            "day": NSNumber(longLong: day),
            "hour": NSNumber(longLong: hour),
            "minute": NSNumber(longLong: minute)
        ]
        OloService.put("/baskets/\(basketId)/timewanted", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func deleteTimeWanted(basketId: String, callback: OloBasketCallback) {
        OloService.delete("/baskets/\(basketId)/timewanted", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func validate(basketId: String, callback: OloBasketValidationCallback) {
        OloService.post("/baskets/\(basketId)/validate", parameters: [:]) { (response, error) -> Void in
            if error != nil {
                callback(result: nil, error: error)
                return
            }
            
            let basketValidation = OloBasketValidation(json: response)
            callback(result: basketValidation, error: nil)
        }
    }
    
    public class func submit(basketId: String, basketSubmitBody: OloBasketSubmitBody, callback: OloBasketSubmitCallback) {
        let authToken = OloSessionService.authToken
        var finalBasketBody = basketSubmitBody
        
        if authToken != nil && finalBasketBody.userType == "user" {
            finalBasketBody.authToken = authToken
        }
        let validationError = finalBasketBody.validateValues()
        if validationError != nil {
            callback(status: nil, error: validationError)
        }
        let parameters = finalBasketBody.serializeAsJSONDictionary()
        OloService.post("/baskets/\(basketId)/submit", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(status: nil, error: error)
                return
            }
            
            let orderStatus = OloOrderStatus(json: response)
            callback(status: orderStatus, error: nil)
        }
    }
    
    public class func applyCoupon(basketId: String, couponCode: String, callback: OloBasketCallback) {
        let parameters = ["couponcode": couponCode]
        OloService.put("/baskets/\(basketId)/coupon", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func deleteCoupon(basketId: String, callback: OloBasketCallback) {
        OloService.delete("/baskets/\(basketId)/coupon", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func billingSchemes(basketId: String, callback: OloBasketBillingSchemesCallback) {
        OloService.get("/baskets/\(basketId)/billingschemes", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(billingSchemes: [], error: error)
                return
            }
            let billingSchemes = response["billingschemes"].arrayValue.map { OloBillingScheme(json: $0) }
            callback(billingSchemes: billingSchemes, error: nil)
        }
    }

    public class func configureLoyaltySchemes(basketId: String, schemeId: Int64, phoneNumber: String, callback: OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "authtoken": OloSessionService.authToken!,
            "schemeid": NSNumber(longLong: schemeId),
            "membershipnumber": phoneNumber
        ]
        OloService.post("/baskets/\(basketId)/loyaltyschemes", parameters: parameters) { (response, error) -> Void in
            // Ignore response, just return error if any
            callback(error: error)
        }
    }

    public class func loyaltySchemes(basketId: String, callback: OloBasketLoyaltySchemesCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(loyaltySchemes: [], error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "authtoken": OloSessionService.authToken!
        ]
        OloService.get("/baskets/\(basketId)/loyaltyschemes", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(loyaltySchemes: [], error: error)
                return
            }
            let loyaltySchemes = response["schemes"].arrayValue.map { OloLoyaltyScheme(json: $0) }
            callback(loyaltySchemes: loyaltySchemes, error: nil)
        }
    }
    
    public class func loyaltyRewards(basketId: String, membershipId: Int64, callback: OloBasketLoyaltyRewardsCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(loyaltyRewards: [], error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "authtoken": OloSessionService.authToken!,
            "membershipid": NSNumber(longLong: membershipId)
        ]
        OloService.get("/baskets/\(basketId)/loyaltyrewards/qualifying", parameters: parameters) { (response, error) in
            if error != nil {
                callback(loyaltyRewards: [], error: error)
                return
            }
            let loyaltyRewards = response["rewards"].arrayValue.map { OloLoyaltyReward(json: $0) }
            callback(loyaltyRewards: loyaltyRewards, error: nil)
        }
    }

    public class func applyReward(basketId: String, membershipId: Int64, rewardReference: String, callback: OloBasketCallback) {
        let parameters = [
            "references": [rewardReference],
            "membershipid": NSNumber(longLong: membershipId)
        ]
        OloService.put("/baskets/\(basketId)/loyaltyrewards/byref", parameters: parameters) { (response, error) in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func removeReward(basketId: String, rewardId: Int64, callback: OloBasketCallback) {
        OloService.delete("/baskets/\(basketId)/loyaltyrewards/\(rewardId)", parameters: nil) { (response, error) in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func createFromOrderStatusId(orderStatusId: String, callback: OloBasketCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(basket: nil, error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "id": orderStatusId
        ]
        
        OloService.post("/baskets/createfromorder?authtoken=\(authToken!)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
    public class func createFromFave(faveId: Int64, callback: OloBasketCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(basket: nil, error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "faveid": NSNumber.init(longLong: faveId)
        ]
        
        OloService.post("/baskets/createfromfave?authtoken=\(authToken!)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(basket: nil, error: error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket: basket, error: nil)
        }
    }
    
}
