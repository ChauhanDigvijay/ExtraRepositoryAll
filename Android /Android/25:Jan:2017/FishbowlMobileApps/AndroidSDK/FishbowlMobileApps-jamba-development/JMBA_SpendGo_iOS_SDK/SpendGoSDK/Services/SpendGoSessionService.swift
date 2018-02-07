//
//  SpendGoSessionService.swift
//  SpendGoSDK
//
//  Created by Eneko Alonso on 6/21/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias SpendGoLookUpCallback = (status: String?, error: NSError?) -> Void
public typealias SpendGoInvalidAuthTokenCallback = (request:NSURLRequest , res:NSHTTPURLResponse, jsonObject:JSON, callback: SpendGoServiceCallback) -> Void

public class SpendGoSessionService {

    public static var spendGoId: String?
    public static var authToken: String?

    //This callback if set can be used to intercept and provide centralized handling for Invalid Auth Error Error Code 1010
    //It is responsibility of the this callback to call the passed callback with appropiate parameter
    public static var invalidAuthTokenCallback: SpendGoInvalidAuthTokenCallback?

    /// Create a new member
    public class func addMember(user: SpendGoUser, password: String, favoriteStoreId: Int64, sendWelcomeEmail: Bool = true, emailValidated: Bool = false, callback: SpendGoErrorAnd202Callback) {
        var JSONDict = user.serializeAsJSONDictionary()
        JSONDict["password"]           = password
        JSONDict["send_welcome_email"] = sendWelcomeEmail
        JSONDict["email_validated"]    = emailValidated
        JSONDict["favorite_store_id"]  = NSNumber(longLong: favoriteStoreId)
        
        SpendGoService.post("/add", parameters: JSONDict, needsAuthToken: false) { (response, error) -> Void in
            if error != nil {
                if error!.domain == SpendGoErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue && error!.code == 202 {
                    // Special case: user account has been created but email needs to be verified by the user. Continue with no error.
                    callback(error: nil, didReturn202: true)
                    return
                }
                callback(error: error, didReturn202: false)
                return
            }
            print(response.description)
            self.spendGoId = response["spendgo_id"].stringValue
            // Ignore user id and username retrieved, not needed
            callback(error: nil, didReturn202: false)
        }
    }
    
    /// Authenticate an exising member
    public class func signIn(emailOrPhoneNumber: String, password: String, callback: SpendGoErrorCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["value"] = emailOrPhoneNumber
        JSONDict["password"] = password
        JSONDict["device_id"] = UIDevice.currentDevice().identifierForVendor!.UUIDString
        JSONDict["os_name"] = "iOS"
        JSONDict["os_version"] = NSProcessInfo.processInfo().operatingSystemVersionString
        JSONDict["manufacturer"] = "Apple"
        
        SpendGoService.post("/signin", parameters: JSONDict, needsAuthToken: false) { (response, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            self.spendGoId = response["spendgo_id"].stringValue
            self.authToken = response["auth_token"].stringValue
            callback(error: nil)
        }
    }

    /// Logout, terminate current session
    public class func logout() {
        if authToken != nil {
            SpendGoService.post("/signoff", parameters: [:], needsAuthToken: true) { (response, error) in
                // Do nothing
            }
        }
        authToken = nil
        spendGoId = nil
    }

    /// Lookup member status by Email
    public class func lookupEmail(emailAddress: String, callback: SpendGoLookUpCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["email"] = emailAddress
        lookupWithParamters(JSONDict, callback: callback)
    }

    /// Lookup member status by phone
    public class func lookupPhoneNumber(phoneNumber: String, callback: SpendGoLookUpCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["phone"] = phoneNumber
        lookupWithParamters(JSONDict, callback: callback)
    }
    
    /// Lookup member status
    private class func lookupWithParamters(parameters: SpendGoJSONDictonary, callback: SpendGoLookUpCallback) {
        SpendGoService.post("/lookup", parameters: parameters, needsAuthToken: false) { (response, error) in
            if error != nil {
                callback(status:nil, error:error)
            } else {
                callback(status:response["status"].stringValue, error:nil)
            }
        }
    }
    
}
