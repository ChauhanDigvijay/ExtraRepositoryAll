//
//  OloBasketService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

public typealias OloBasketTransferCallback = (_ basketTransfer: OloBasketTransfer?, _ error: NSError?) -> Void
public typealias OloBasketCallback = (_ basket: OloBasket?, _ error: NSError?) -> Void
public typealias OloBasketProductBatchResultCallback = (_ result: OloBasketProductBatchResult?, _ error: NSError?) -> Void
public typealias OloBasketValidationCallback = (_ result: OloBasketValidation?, _ error: NSError?) -> Void
public typealias OloBasketSubmitCallback = (_ status: OloOrderStatus?, _ error: NSError?) -> Void
public typealias OloBasketBillingSchemesCallback = (_ billingSchemes: [OloBillingScheme], _ error: NSError?) -> Void
public typealias OloBasketLoyaltySchemesCallback = (_ loyaltySchemes: [OloLoyaltyScheme], _ error: NSError?) -> Void
public typealias OloBasketLoyaltyRewardsCallback = (_ loyaltyRewards: [OloLoyaltyReward], _ error: NSError?) -> Void

public typealias OloBasketSavedDeliveryAddressListCallback = (_ deliveryAddressList: [OloSavedDeliverAdrdress], _ error: NSError?) -> Void
public typealias OloBasketDeliveryAddressListCallback = (_ deliveryAddressList: [OloDeliveryAddress], _ error: NSError?) -> Void
public typealias OloBasketDeliveryAddressCallback = (_ deliveryAddressList: OloDeliveryAddress?, _ error: NSError?) -> Void
public typealias OloBasketUpsellCallback = (_ result: [OloUpSellGroup], _ error: NSError?) -> Void

open class OloBasketService {
    
    open class func basketWithBasketId(_ basketId: String, callback: @escaping OloBasketCallback) {
        OloService.get("/baskets/\(basketId)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func createBasket(_ vendorId: Int64, callback: @escaping OloBasketCallback) {
        var parameters: [String : AnyObject] = [
            "vendorid": NSNumber(value: vendorId as Int64)
        ]
        
        if let authToken = OloSessionService.authToken {
            parameters["authtoken"] = authToken as AnyObject?
        }
        OloService.post("/baskets/create", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func basketTransfer(_ vendorId: Int64, basketId:String, callback: @escaping OloBasketTransferCallback){
        var parameters: [String : AnyObject] = [
            "vendorid": NSNumber(value: vendorId as Int64)
        ]
        if let authToken = OloSessionService.authToken {
            parameters["authtoken"] = authToken as AnyObject?
        }
        OloService.post("/baskets/\(basketId)/transfer", parameters: parameters) { (response, error) -> Void in
            if  error != nil {
                callback(nil, error)
                return
            }
            callback(OloBasketTransfer(json: response), nil)
        }
    }
    
    open class func addProduct(_ basketId: String, newBasketProduct: OloNewBasketProduct, callback: @escaping OloBasketCallback) {
        let parameters = newBasketProduct.serializeAsJSONDictionary()
        OloService.post("/baskets/\(basketId)/products", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func addProducts(_ basketId: String, newBasketProducts: [OloNewBasketProduct],callback: @escaping OloBasketProductBatchResultCallback) {
        let parameters = ["products": newBasketProducts.map { $0.serializeAsJSONDictionary() }]
        OloService.post("/baskets/\(basketId)/products/batch", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basketProductBatchResult = OloBasketProductBatchResult(json: response)
            callback(basketProductBatchResult, nil)
        }
    }
    
    open class func addProductsWithChoiceQuantity(_ basketId: String, newBasketProducts: [OloProductWithChoiceQuantity],callback: @escaping OloBasketProductBatchResultCallback) {
        let parameters = ["products": newBasketProducts.map { $0.serializeAsJSONDictionary() }]
        OloService.post("/baskets/\(basketId)/products/batch", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basketProductBatchResult = OloBasketProductBatchResult(json: response)
            callback(basketProductBatchResult, nil)
        }
    }
    
    open class func updateProduct(_ basketId: String, basketProductId: Int64, newBasketProduct: OloNewBasketProduct, callback: @escaping OloBasketCallback) {
        let parameters = newBasketProduct.serializeAsJSONDictionary()
        OloService.put("/baskets/\(basketId)/products/\(basketProductId)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func removeProduct(_ basketId: String, basketProductId: Int64, callback: @escaping OloBasketCallback) {
        OloService.delete("/baskets/\(basketId)/products/\(basketProductId)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    // Use 0,15,30,45 for minute
    open class func updateTimeWanted(_ basketId: String, isManualFire: Bool, year: Int64, month: Int64, day: Int64, hour: Int64, minute: Int64, callback: @escaping OloBasketCallback) {
        let parameters: [String : AnyObject] = [
            "ismanualfire": isManualFire as AnyObject,
            "year": NSNumber(value: year as Int64),
            "month": NSNumber(value: month as Int64),
            "day": NSNumber(value: day as Int64),
            "hour": NSNumber(value: hour as Int64),
            "minute": NSNumber(value: minute as Int64)
        ]
        OloService.put("/baskets/\(basketId)/timewanted", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func deleteTimeWanted(_ basketId: String, callback: @escaping OloBasketCallback) {
        OloService.delete("/baskets/\(basketId)/timewanted", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func validate(_ basketId: String,_ upsell: Bool, callback: @escaping OloBasketValidationCallback) {
        OloService.post("/baskets/\(basketId)/validate?checkupsell=\(upsell)", parameters: [:]) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basketValidation = OloBasketValidation(json: response)
            callback(basketValidation, nil)
        }
    }
    
    open class func submit(_ basketId: String, basketSubmitBody: OloBasketSubmitBody, callback: @escaping OloBasketSubmitCallback) {
        let authToken = OloSessionService.authToken
        var finalBasketBody = basketSubmitBody
        
        if authToken != nil && finalBasketBody.userType == "user" {
            finalBasketBody.authToken = authToken
        }
        let validationError = finalBasketBody.validateValues()
        if validationError != nil {
            callback(nil, validationError)
        }
        let parameters = finalBasketBody.serializeAsJSONDictionary()
        OloService.post("/baskets/\(basketId)/submit", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let orderStatus = OloOrderStatus(json: response)
            callback(orderStatus, nil)
        }
    }
    
    open class func applyCoupon(_ basketId: String, couponCode: String, callback: @escaping OloBasketCallback) {
        let parameters = ["couponcode": couponCode]
        OloService.put("/baskets/\(basketId)/coupon", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func deleteCoupon(_ basketId: String, callback: @escaping OloBasketCallback) {
        OloService.delete("/baskets/\(basketId)/coupon", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func billingSchemes(_ basketId: String, callback: @escaping OloBasketBillingSchemesCallback) {
        OloService.get("/baskets/\(basketId)/billingschemes", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let billingSchemes = response["billingschemes"].arrayValue.map { OloBillingScheme(json: $0) }
            callback(billingSchemes, nil)
        }
    }

    open class func configureLoyaltySchemes(_ basketId: String, schemeId: Int64, phoneNumber: String, callback: @escaping OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "authtoken": OloSessionService.authToken!,
            "schemeid": NSNumber(value: schemeId as Int64),
            "membershipnumber": phoneNumber
        ] as [String : Any]
        OloService.post("/baskets/\(basketId)/loyaltyschemes", parameters: parameters) { (response, error) -> Void in
            // Ignore response, just return error if any
            callback(error)
        }
    }

    open class func loyaltySchemes(_ basketId: String, callback: @escaping OloBasketLoyaltySchemesCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback([], OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "authtoken": OloSessionService.authToken!
        ]
        OloService.get("/baskets/\(basketId)/loyaltyschemes", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let loyaltySchemes = response["schemes"].arrayValue.map { OloLoyaltyScheme(json: $0) }
            callback(loyaltySchemes, nil)
        }
    }
    
    open class func loyaltyRewards(_ basketId: String, membershipId: Int64, callback: @escaping OloBasketLoyaltyRewardsCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback([], OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "authtoken": OloSessionService.authToken!,
            "membershipid": NSNumber(value: membershipId as Int64)
        ] as [String : Any]
        OloService.get("/baskets/\(basketId)/loyaltyrewards/qualifying", parameters: parameters) { (response, error) in
            if error != nil {
                callback([], error)
                return
            }
            let loyaltyRewards = response["rewards"].arrayValue.map { OloLoyaltyReward(json: $0) }
            callback(loyaltyRewards, nil)
        }
    }

    open class func applyReward(_ basketId: String, membershipId: Int64, rewardReference: String, callback: @escaping OloBasketCallback) {
        let parameters = [
            "references": [rewardReference],
            "membershipid": NSNumber(value: membershipId as Int64)
        ] as [String : Any]
        OloService.put("/baskets/\(basketId)/loyaltyrewards/byref", parameters: parameters) { (response, error) in
            if error != nil {
                callback(nil, error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func removeReward(_ basketId: String, rewardId: Int64, callback: @escaping OloBasketCallback) {
        OloService.delete("/baskets/\(basketId)/loyaltyrewards/\(rewardId)", parameters: nil) { (response, error) in
            if error != nil {
                callback(nil, error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func createFromOrderStatusId(_ orderStatusId: String, callback: @escaping OloBasketCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(nil, OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "id": orderStatusId
        ]
        
        OloService.post("/baskets/createfromorder?authtoken=\(authToken!)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func createFromFave(_ faveId: Int64, callback: @escaping OloBasketCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(nil, OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let parameters = [
            "faveid": NSNumber.init(value: faveId as Int64)
        ]
        
        OloService.post("/baskets/createfromfave?authtoken=\(authToken!)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    //MARK:- change delivery mode
    open class func changeDeliveryMode(_ deliveryMode: String, basketId:String, callback: @escaping OloBasketCallback) {
        if deliveryMode != "" {
            let parameters: [String : AnyObject] = [
                "deliverymode": deliveryMode as AnyObject
            ]
            OloService.put("/baskets/\(basketId)/deliverymode", parameters: parameters) { (response, error) -> Void in
                if error != nil {
                    callback(nil, error)
                    return
                }
                let basket = OloBasket(json: response)
                callback(basket, nil)
            }
        }
    }
    
    //MARK:- delivery Address
    open class func saveDeliveryaddress(_ deliveryAddress: OloDeliveryAddress, basketId:String, callback: @escaping OloBasketCallback) {
        let parameters = deliveryAddress.serializeAsJSONDictionary()
        OloService.put("/baskets/\(basketId)/dispatchaddress", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func getBasketdeliveryAddress(_ basketId:String, callback: @escaping OloBasketDeliveryAddressCallback) {
        OloService.get("/baskets/\(basketId)/dispatchaddress") { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            callback(OloDeliveryAddress(json: response), nil)
        }
    }
    
    //MARK:- olo UpSell
    open class func addUpsell(_ basketId: String,_ upsell: OloUpSellRequestItems, callback: @escaping OloBasketCallback) {
        
        let param = upsell.serializeAsJSONDictionary()
        
        OloService.post("/baskets/\(basketId)/upsell", parameters: param) { (response, error) in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let basket = OloBasket(json: response)
            callback(basket, nil)
        }
    }
    
    open class func getUpsell(_ basketId: String, callback: @escaping OloBasketUpsellCallback) {
        OloService.get("/baskets/\(basketId)/upsell") { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let upsell = response["groups"].arrayValue
            let upsellArray = upsell.map { item in OloUpSellGroup(json: item) }
            callback(upsellArray, nil)
        }
    }
}
