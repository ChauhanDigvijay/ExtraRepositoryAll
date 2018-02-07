//
//  OloUserService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SwiftyJSON

public typealias OloUserCallback = (_ user: OloUser?, _ error: NSError?) -> Void
public typealias OloOrderStatusCallback = (_ status: [OloOrderStatus], _ error: NSError?) -> Void
public typealias OloUserContactDetailsCallback = (_ details: String?, _ error: NSError?) -> Void


open class OloUserService {
   
    open class func forgotPassword(_ emailAddress: String, callback: @escaping OloErrorCallback) {
        let payload = [
            "emailaddress": emailAddress
        ]
        OloService.post("/users/forgotpassword", parameters: payload) { (response, error) -> Void in
            callback(error)
        }
    }
    
    open class func changePassword(_ currentPassword: String, newPassword: String,callback: @escaping OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        let payload = [
            "currentpassword": currentPassword,
            "newpassword": newPassword
        ]
        OloService.post("/users/\(authToken!)/password", parameters: payload) { (response, error) -> Void in
            callback(error)
        }
    }
    
    open class func userInformation(_ callback: @escaping OloUserCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(nil, OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        OloService.get("/users/\(authToken!)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let user = OloUser(json: response)
            OloSessionService.authToken = user.authToken
            callback(user, nil)
        }
    }
    
    open class func updateUserInformation(_ user: OloUser, callback: @escaping OloUserCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(nil, OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        var payload:[String:AnyObject] = [
            "firstname": user.firstName as AnyObject,
            "lastname": user.lastName as AnyObject,
            "emailaddress": user.emailAddress as AnyObject
        ]
        if user.cardSuffix != nil {
            payload["cardsuffix"] = user.cardSuffix! as AnyObject?
        }
        else {
            payload["cardsuffix"] = NSNull()
        }
        OloService.put("/users/\(authToken!)", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let user = OloUser(json: response)
            OloSessionService.authToken = user.authToken
            callback(user, nil)
        }
    }
    
    open class func recentOrders(_ callback: @escaping OloOrderStatusCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback([], OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        OloService.get("/users/\(authToken!)/recentorders", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let orderStatusArray = response["orders"].arrayValue.map { item in OloOrderStatus(json: item) }
            callback(orderStatusArray, nil)
        }
    }
    
    open class func addCreditCard(_ cardNumber: String, expiryMonth: Int, expiryYear: Int, cvv: String, zip: String, callback: @escaping OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        let payload:[String:AnyObject] = [
            "cardnumber": cardNumber as AnyObject,
            "expirymonth": expiryMonth as AnyObject,
            "expiryyear": expiryYear as AnyObject,
            "cvv": cvv as AnyObject,
            "zip": zip as AnyObject
        ]
        OloService.post("/users/\(authToken!)/creditcard", parameters: payload) { (response, error) -> Void in
            callback(error)
        }
    }
  
    open class func deleteCreditCard(_ callback: @escaping OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        OloService.delete("/users/\(authToken!)/creditcard", parameters: nil) { (response, error) -> Void in
            callback(error)
        }
    }

    open class func contactDetails(_ callback: @escaping OloUserContactDetailsCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(nil, OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        OloService.get("/users/\(authToken!)/contactdetails", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            else {
                let contactDetails = response["contactdetails"].stringValue
                callback(contactDetails, nil)
            }
        }
    }
    
    open class func updateContactDetails(_ contactDetails: String, callback: @escaping OloUserContactDetailsCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(nil, OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let payload:[String:AnyObject] = [
            "contactdetails": contactDetails as AnyObject
        ]
        OloService.put("/users/\(authToken!)/contactdetails", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            else {
                let contactDetails = response["contactdetails"].stringValue
                callback(contactDetails, nil)
            }
        }
    }
    
    //MARK:- Dispatch Address
    open class func getSavedDeliveryaddressList(_ callback: @escaping OloBasketDeliveryAddressListCallback) {
        let authToken = OloSessionService.authToken
        guard let token = authToken else {
            callback([], OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        
        OloService.get("/users/\(token)/userdeliveryaddresses") { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let address = OloUserDeliveryAddressList(json: response)
            callback(address.deliverAddress, nil)
        }
    }
    
    
    open class func deleteSavedDeliveryaddressList(_ addressId:String,callback: @escaping OloErrorCallback) {
        let authToken = OloSessionService.authToken
        guard let token = authToken else {
            callback(OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        
        OloService.delete("/users/\(token)/userdeliveryaddresses/\(addressId)") { (response, error) -> Void in
            if error != nil {
                callback(error)
                return
            }
            callback(nil)
        }
    }
}

