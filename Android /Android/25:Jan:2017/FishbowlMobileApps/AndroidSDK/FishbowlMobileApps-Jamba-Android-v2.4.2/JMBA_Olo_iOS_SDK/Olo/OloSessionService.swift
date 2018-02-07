//
//  OloSessionService.swift
//  Olo
//
//  Created by Taha Samad on 4/28/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias OloSessionUserCallback = (_ user: OloUser?, _ error: NSError?) -> Void
public typealias OloSessionAuthTokenCallback = (_ oloAuthToken: String?, _ error: NSError?) -> Void
public typealias OloSessionInvalidAuthTokenCallback = (_ request:URLRequest , _ res:HTTPURLResponse, _ jsonObject:JSON, _ callback: OloServiceCallback) -> Void

open class OloSessionService {
    
    open static var authToken: String?
    
    //This callback if set can be used to intercept and provide centralized handling for Invalid Auth Error Olo Error Code 100,101
    //It is responsibility of the this callback to call the passed callback with appropiate parameter
    open static var invalidAuthTokenCallback: OloSessionInvalidAuthTokenCallback?
    
    open class func createUser(_ firstName: String, lastName: String, contactNumber: String, emailAddress: String, password: String, callback: @escaping OloSessionUserCallback) {
        let payload = [
            "firstname": firstName,
            "lastname": lastName,
            "emailaddress": emailAddress,
            "contactnumber": contactNumber,
            "password": password
        ]
        OloService.post("/users/create", parameters: payload as OloJSONDictionary) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let user = OloUser(json: response)
            self.authToken = user.authToken
            callback(user, nil)
        }
    }
    
    open class func authenticateUser(_ login: String, password: String, basketId: String?, callback: @escaping OloSessionUserCallback) {
        var payload = [
            "login": login,
            "password": password
        ]
        if basketId != nil {
            payload["basketid"] = basketId!
        }
        OloService.post("/users/authenticate", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let user = OloUser(json: response["user"])
            self.authToken = user.authToken
            callback(user, nil)
        }
    }
    
    // If user is authenticated, remove token from the API, but return immediately
    // Ignore any possible API errors, as we want to end our user session in any case
    open class func deleteAuthenticationToken() {
        if let authToken = OloSessionService.authToken {
            OloService.delete("/users/\(authToken)", parameters: nil) { (response, error) in
                // Ignore any possible errors
            }
        }
        OloSessionService.authToken = nil
    }

    /// Authenticate or create user with third party vendor
    open class func getOrCreate(_ provider: String, providerToken: String, contactNumber: String? = nil, basketId: String? = nil, callback: @escaping OloSessionAuthTokenCallback) {
        var payload:[String:AnyObject] = [
            "provider": provider as AnyObject,
            "providertoken": providerToken as AnyObject
        ]
        payload["contactnumber"] = contactNumber as AnyObject?
        payload["basketid"] = basketId as AnyObject?
        OloService.post("/users/getorcreate", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            else {
                let authToken = response["authtoken"].stringValue
                self.authToken = authToken
                callback(authToken, nil)
            }
        }
    }

}
