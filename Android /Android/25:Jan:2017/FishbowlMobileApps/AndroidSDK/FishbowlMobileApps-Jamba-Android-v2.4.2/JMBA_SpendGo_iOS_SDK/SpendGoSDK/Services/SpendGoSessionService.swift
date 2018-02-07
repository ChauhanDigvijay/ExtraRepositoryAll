//
//  SpendGoSessionService.swift
//  SpendGoSDK
//
//  Created by Eneko Alonso on 6/21/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias SpendGoLookUpCallback = (_ status: String?, _ error: NSError?) -> Void
public typealias SpendGoInvalidAuthTokenCallback = (_ request:URLRequest , _ res:HTTPURLResponse, _ jsonObject:JSON, _ callback: SpendGoServiceCallback) -> Void

open class SpendGoSessionService {

    open static var spendGoId: String?
    open static var authToken: String?

    //This callback if set can be used to intercept and provide centralized handling for Invalid Auth Error Error Code 1010
    //It is responsibility of the this callback to call the passed callback with appropiate parameter
    open static var invalidAuthTokenCallback: SpendGoInvalidAuthTokenCallback?

    /// Create a new member
    open class func addMember(_ user: SpendGoUser, password: String, favoriteStoreCode: String, sendWelcomeEmail: Bool = true, emailValidated: Bool = false, callback: @escaping SpendGoErrorAnd202Callback) {
        var JSONDict = user.serializeAsJSONDictionary()
        JSONDict["password"]           = password as AnyObject?
        JSONDict["send_welcome_email"] = sendWelcomeEmail as AnyObject?
        JSONDict["email_validated"]    = emailValidated as AnyObject?
        JSONDict["favorite_store_code"]  = favoriteStoreCode as AnyObject?
        
        SpendGoService.post("/add", parameters: JSONDict, needsAuthToken: false) { (response, error) -> Void in
            if error != nil {
                if error!.domain == SpendGoErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue && error!.code == 202 {
                    // Special case: user account has been created but email needs to be verified by the user. Continue with no error.
                    callback(nil, true)
                    return
                }
                callback(error, false)
                return
            }
            print(response.description)
            self.spendGoId = response["spendgo_id"].stringValue
            // Ignore user id and username retrieved, not needed
            callback(nil, false)
        }
    }
    
    /// Authenticate an exising member
    open class func signIn(_ emailOrPhoneNumber: String, password: String, callback: @escaping SpendGoErrorCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["value"] = emailOrPhoneNumber as AnyObject?
        JSONDict["password"] = password as AnyObject?
        JSONDict["device_id"] = UIDevice.current.identifierForVendor!.uuidString as AnyObject?
        JSONDict["os_name"] = "iOS" as AnyObject?
        JSONDict["os_version"] = ProcessInfo.processInfo.operatingSystemVersionString as AnyObject?
        JSONDict["manufacturer"] = "Apple" as AnyObject?
        
        SpendGoService.post("/signin", parameters: JSONDict, needsAuthToken: false) { (response, error) -> Void in
            if error != nil {
                callback(error)
                return
            }
            self.spendGoId = response["spendgo_id"].stringValue
            self.authToken = response["auth_token"].stringValue
            callback(nil)
        }
    }

    /// Logout, terminate current session
    open class func logout() {
        if authToken != nil {
            SpendGoService.post("/signoff", parameters: [:], needsAuthToken: true) { (response, error) in
                // Do nothing
            }
        }
        authToken = nil
        spendGoId = nil
    }

    /// Lookup member status by Email
    open class func lookupEmail(_ emailAddress: String, callback: @escaping SpendGoLookUpCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["email"] = emailAddress as AnyObject?
        lookupWithParamters(JSONDict, callback: callback)
    }

    /// Lookup member status by phone
    open class func lookupPhoneNumber(_ phoneNumber: String, callback: @escaping SpendGoLookUpCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["phone"] = phoneNumber as AnyObject?
        lookupWithParamters(JSONDict, callback: callback)
    }
    
    /// Lookup member status
    fileprivate class func lookupWithParamters(_ parameters: SpendGoJSONDictonary, callback: @escaping SpendGoLookUpCallback) {
        SpendGoService.post("/lookup", parameters: parameters, needsAuthToken: false) { (response, error) in
            if error != nil {
                callback(nil, error)
            } else {
                callback(response["status"].stringValue, nil)
            }
        }
    }
    
}
