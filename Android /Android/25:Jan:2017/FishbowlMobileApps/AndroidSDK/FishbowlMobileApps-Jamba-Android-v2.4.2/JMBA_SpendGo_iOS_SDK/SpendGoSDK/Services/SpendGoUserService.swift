//
//  SpendGoUserService.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias SpendGoUserCallback = (_ user: SpendGoUser?, _ error: NSError?) -> Void
public typealias SpendGoErrorCallback = (_ error: NSError?) -> Void
public typealias SpendGoErrorAnd202Callback = (_ error: NSError?, _ didReturn202: Bool) -> Void

open class SpendGoUserService {

    /// Update logged in user
    open class func updateMember(_ firstName: String?, lastName: String?, dateOfBirth: Date?, emailOptIn: Bool, smsOptIn: Bool, callback: @escaping SpendGoErrorCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId as AnyObject?
        JSONDict["first_name"] = firstName as AnyObject?
        JSONDict["last_name"] = lastName as AnyObject?
        JSONDict["email_opt_in"] = emailOptIn as AnyObject?
        JSONDict["sms_opt_in"] = smsOptIn as AnyObject?
        if dateOfBirth != nil {
            JSONDict["dob"] = userDOBString(dateOfBirth!) as AnyObject?
        }
        updateMember(JSONDict) { (error, didReturn202) in
            //No case of didReturn202 being true
            callback(error)
        }
    }
    
    /// Update logged in user email address
    open class func updateMemberEmail(_ emailAddress: String, emailOptIn: Bool, callback: @escaping SpendGoErrorAnd202Callback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId as AnyObject?
        JSONDict["email"] = emailAddress as AnyObject?
        JSONDict["email_opt_in"] = emailOptIn as AnyObject?
        updateMember(JSONDict, callback: callback)
    }
    
    /// Update logged in user phone number
    open class func updateMemberPhone(_ phoneNumber: String, smsOptIn: Bool, callback: @escaping SpendGoErrorAnd202Callback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId as AnyObject?
        JSONDict["phone"] = phoneNumber as AnyObject?
        JSONDict["sms_opt_in"] = smsOptIn as AnyObject?
        updateMember(JSONDict, callback: callback)
    }
    
    /// Update logged in user password
    open class func updateMemberPassword(_ password: String, callback: @escaping SpendGoErrorCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId as AnyObject?
        JSONDict["password"] = password as AnyObject?
        updateMember(JSONDict) { (error, didReturn202) in
            //No case of didReturn202 being true
            callback(error)
        }
    }

    /// Update logged in user favorite store
    open class func updateMemberFavoriteStore(_ storeCode: String, callback: @escaping SpendGoErrorCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId as AnyObject?
        JSONDict["favorite_store_code"] = storeCode as AnyObject?
        updateMember(JSONDict) { (error, didReturn202) in
            //No case of didReturn202 being true
            callback(error)
        }
    }
    
    /// Update individual parameters for the logged in user
    fileprivate class func updateMember(_ parameters: SpendGoJSONDictonary, callback: @escaping SpendGoErrorAnd202Callback) {
        SpendGoService.post("/update", parameters: parameters, needsAuthToken: true) { (response, error) in
            if error != nil {
                if error!.domain == SpendGoErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue && error!.code == 202 {
                    // Special case: user email has been saved but email needs to be verified by the user. Continue with no error.
                    callback(nil, true)
                    return
                }
                callback(error, false)
                return
            }
            callback(nil, false)
        }
    }

    /// Get member by phone number
    open class func getMemberByPhoneNumber(_ phoneNumber: String, callback: @escaping SpendGoUserCallback) {
        let parameters = ["phone": phoneNumber]
        getMember(parameters as SpendGoJSONDictonary, callback: callback)
    }
    
    /// Get member by email address
    open class func getMemberByEmailAddress(_ emailAddress: String, callback: @escaping SpendGoUserCallback) {
        let parameters = ["email": emailAddress]
        getMember(parameters as SpendGoJSONDictonary, callback: callback)

    }

    /// Get member by Id
    open class func getMemberById(_ id: String, callback: @escaping SpendGoUserCallback) {
        let parameters = ["spendgo_id": id]
        getMember(parameters as SpendGoJSONDictonary, callback: callback)
    }

    /// Get member
    class func getMember(_ parameters: SpendGoJSONDictonary, callback: @escaping SpendGoUserCallback) {
        SpendGoService.post("/get", parameters: parameters, needsAuthToken: true) { (response, error) in
            if error != nil {
                callback(nil, error)
                return
            }
            callback(SpendGoUser(json: response), nil)
        }
    }
    
    /// Request new password
    open class func forgotPassword(_ emailAddress: String, callback: @escaping SpendGoErrorCallback) {
        //TODO: This is optional as per Anil
        //let finalURLString = ForgotPasswordBaseURL + "\(arc4random_uniform(9999))"
        let parameters = ["email": emailAddress]
        SpendGoService.post("/forgotPassword", parameters: parameters as SpendGoJSONDictonary, needsAuthToken: false) { (response, error) in
            callback(error)
        }
    }

    open class func userDOBString(_ dob: Date) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter.string(from: dob)
    }
    
    open class func getSignatureKeyForFishbowlLogin(_ id: String) -> String {
        let parameters = ["spendgo_id": id]
        return SpendGoService.calculateSignatureForFishbowlLogin(.post, path: "/get", parameters: parameters as SpendGoJSONDictonary?, needsAuthToken: true)
    }
    
    

}
