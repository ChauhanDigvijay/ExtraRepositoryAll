//
//  OloSessionService.swift
//  Olo
//
//  Created by Taha Samad on 4/28/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias OloSessionUserCallback = (user: OloUser?, error: NSError?) -> Void
public typealias OloSessionAuthTokenCallback = (oloAuthToken: String?, error: NSError?) -> Void
public typealias OloSessionInvalidAuthTokenCallback = (request:NSURLRequest , res:NSHTTPURLResponse, jsonObject:JSON, callback: OloServiceCallback) -> Void

public class OloSessionService {
    
    public static var authToken: String?
    
    //This callback if set can be used to intercept and provide centralized handling for Invalid Auth Error Olo Error Code 100,101
    //It is responsibility of the this callback to call the passed callback with appropiate parameter
    public static var invalidAuthTokenCallback: OloSessionInvalidAuthTokenCallback?
    
    public class func createUser(firstName: String, lastName: String, contactNumber: String, emailAddress: String, password: String, callback: OloSessionUserCallback) {
        let payload = [
            "firstname": firstName,
            "lastname": lastName,
            "emailaddress": emailAddress,
            "contactnumber": contactNumber,
            "password": password
        ]
        OloService.post("/users/create", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(user: nil, error: error)
                return
            }
            let user = OloUser(json: response)
            self.authToken = user.authToken
            callback(user: user, error: nil)
        }
    }
    
    public class func authenticateUser(login: String, password: String, basketId: String?, callback: OloSessionUserCallback) {
        var payload = [
            "login": login,
            "password": password
        ]
        if basketId != nil {
            payload["basketid"] = basketId!
        }
        OloService.post("/users/authenticate", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(user: nil, error: error)
                return
            }
            let user = OloUser(json: response["user"])
            self.authToken = user.authToken
            callback(user: user, error: nil)
        }
    }
    
    // If user is authenticated, remove token from the API, but return immediately
    // Ignore any possible API errors, as we want to end our user session in any case
    public class func deleteAuthenticationToken() {
        if let authToken = OloSessionService.authToken {
            OloService.delete("/users/\(authToken)", parameters: nil) { (response, error) in
                // Ignore any possible errors
            }
        }
        OloSessionService.authToken = nil
    }

    /// Authenticate or create user with third party vendor
    public class func getOrCreate(provider: String, providerToken: String, contactNumber: String? = nil, basketId: String? = nil, callback: OloSessionAuthTokenCallback) {
        var payload:[String:AnyObject] = [
            "provider": provider,
            "providertoken": providerToken
        ]
        payload["contactnumber"] = contactNumber
        payload["basketid"] = basketId
        OloService.post("/users/getorcreate", parameters: payload) { (response, error) -> Void in
            if error != nil {
                callback(oloAuthToken: nil, error: error)
                return
            }
            else {
                let authToken = response["authtoken"].stringValue
                self.authToken = authToken
                callback(oloAuthToken: authToken, error: nil)
            }
        }
    }

}
