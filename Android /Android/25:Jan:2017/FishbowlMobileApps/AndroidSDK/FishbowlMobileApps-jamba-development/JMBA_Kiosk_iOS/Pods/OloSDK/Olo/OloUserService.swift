//
//  OloUserService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SwiftyJSON

public typealias OloUserCallback = (user: OloUser?, error: NSError?) -> Void
public typealias OloOrderStatusCallback = (status: [OloOrderStatus], error: NSError?) -> Void
public typealias OloUserContactDetailsCallback = (details: String?, error: NSError?) -> Void


public class OloUserService {
   
    public class func forgotPassword(emailAddress: String, callback: OloErrorCallback) {
        let payload = [
            "emailaddress": emailAddress
        ]
        OloService.post("/users/forgotpassword", parameters: payload) { (response, error) -> Void in
            callback(error: error)
        }
    }
    
    public class func changePassword(currentPassword: String, newPassword: String,callback: OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        let payload = [
            "currentpassword": currentPassword,
            "newpassword": newPassword
        ]
        OloService.post("/users/\(authToken!)/password", parameters: payload) { (response, error) -> Void in
            callback(error: error)
        }
    }
    
    public class func userInformation(callback: OloUserCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(user: nil, error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        OloService.get("/users/\(authToken!)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(user: nil, error: error)
                return
            }
            let user = OloUser(json: response)
            OloSessionService.authToken = user.authToken
            callback(user: user, error: nil)
        }
    }
    
    public class func updateUserInformation(user: OloUser, callback: OloUserCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(user: nil, error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        var payload:[String:AnyObject] = [
            "firstname": user.firstName,
            "lastname": user.lastName,
            "emailaddress": user.emailAddress
        ]
        if user.cardSuffix != nil {
            payload["cardsuffix"] = user.cardSuffix!
        }
        else {
            payload["cardsuffix"] = NSNull()
        }
        OloService.put("/users/\(authToken!)", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(user: nil, error: error)
                return
            }
            let user = OloUser(json: response)
            OloSessionService.authToken = user.authToken
            callback(user: user, error: nil)
        }
    }
    
    public class func recentOrders(callback: OloOrderStatusCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(status: [], error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        OloService.get("/users/\(authToken!)/recentorders", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(status: [], error: error)
                return
            }
            let orderStatusArray = response["orders"].arrayValue.map { item in OloOrderStatus(json: item) }
            callback(status: orderStatusArray, error: nil)
        }
    }
    
    public class func addCreditCard(cardNumber: String, expiryMonth: Int, expiryYear: Int, cvv: String, zip: String, callback: OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }

        let payload:[String:AnyObject] = [
            "cardnumber": cardNumber,
            "expirymonth": expiryMonth,
            "expiryyear": expiryYear,
            "cvv": cvv,
            "zip": zip
        ]
        OloService.post("/users/\(authToken!)/creditcard", parameters: payload) { (response, error) -> Void in
            callback(error: error)
        }
    }
  
    public class func deleteCreditCard(callback: OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        OloService.delete("/users/\(authToken!)/creditcard", parameters: nil) { (response, error) -> Void in
            callback(error: error)
        }
    }

    public class func contactDetails(callback: OloUserContactDetailsCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(details: nil, error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        OloService.get("/users/\(authToken!)/contactdetails", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(details: nil, error: error)
                return
            }
            else {
                let contactDetails = response["contactdetails"].stringValue
                callback(details: contactDetails, error: nil)
            }
        }
    }
    
    public class func updateContactDetails(contactDetails: String, callback: OloUserContactDetailsCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback(details: nil, error: OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        let payload:[String:AnyObject] = [
            "contactdetails": contactDetails
        ]
        OloService.put("/users/\(authToken!)/contactdetails", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(details: nil, error: error)
                return
            }
            else {
                let contactDetails = response["contactdetails"].stringValue
                callback(details: contactDetails, error: nil)
            }
        }
    }

}

