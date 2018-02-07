//
//  SpendGoUserService.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias SpendGoUserCallback = (user: SpendGoUser?, error: NSError?) -> Void
public typealias SpendGoErrorCallback = (error: NSError?) -> Void
public typealias SpendGoErrorAnd202Callback = (error: NSError?, didReturn202: Bool) -> Void

public class SpendGoUserService {

    /// Update logged in user
    public class func updateMember(firstName: String?, lastName: String?, dateOfBirth: NSDate?, emailOptIn: Bool, smsOptIn: Bool, callback: SpendGoErrorCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId
        JSONDict["first_name"] = firstName
        JSONDict["last_name"] = lastName
        JSONDict["email_opt_in"] = emailOptIn
        JSONDict["sms_opt_in"] = smsOptIn
        if dateOfBirth != nil {
            JSONDict["dob"] = userDOBString(dateOfBirth!)
        }
        updateMember(JSONDict) { (error, didReturn202) in
            //No case of didReturn202 being true
            callback(error: error)
        }
    }
    
    /// Update logged in user email address
    public class func updateMemberEmail(emailAddress: String, emailOptIn: Bool, callback: SpendGoErrorAnd202Callback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId
        JSONDict["email"] = emailAddress
        JSONDict["email_opt_in"] = emailOptIn
        updateMember(JSONDict, callback: callback)
    }
    
    /// Update logged in user phone number
    public class func updateMemberPhone(phoneNumber: String, smsOptIn: Bool, callback: SpendGoErrorAnd202Callback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId
        JSONDict["phone"] = phoneNumber
        JSONDict["sms_opt_in"] = smsOptIn
        updateMember(JSONDict, callback: callback)
    }
    
    /// Update logged in user password
    public class func updateMemberPassword(password: String, callback: SpendGoErrorCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId
        JSONDict["password"] = password
        updateMember(JSONDict) { (error, didReturn202) in
            //No case of didReturn202 being true
            callback(error: error)
        }
    }

    /// Update logged in user favorite store
    public class func updateMemberFavoriteStore(storeCode: String, callback: SpendGoErrorCallback) {
        var JSONDict = SpendGoJSONDictonary()
        JSONDict["spendgo_id"] = SpendGoSessionService.spendGoId
        JSONDict["favorite_store_code"] = storeCode
        updateMember(JSONDict) { (error, didReturn202) in
            //No case of didReturn202 being true
            callback(error: error)
        }
    }
    
    /// Update individual parameters for the logged in user
    private class func updateMember(parameters: SpendGoJSONDictonary, callback: SpendGoErrorAnd202Callback) {
        SpendGoService.post("/update", parameters: parameters, needsAuthToken: true) { (response, error) in
            if error != nil {
                if error!.domain == SpendGoErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue && error!.code == 202 {
                    // Special case: user email has been saved but email needs to be verified by the user. Continue with no error.
                    callback(error: nil, didReturn202: true)
                    return
                }
                callback(error: error, didReturn202: false)
                return
            }
            callback(error: nil, didReturn202: false)
        }
    }

    /// Get member by phone number
    public class func getMemberByPhoneNumber(phoneNumber: String, callback: SpendGoUserCallback) {
        let parameters = ["phone": phoneNumber]
        getMember(parameters, callback: callback)
    }
    
    /// Get member by email address
    public class func getMemberByEmailAddress(emailAddress: String, callback: SpendGoUserCallback) {
        let parameters = ["email": emailAddress]
        getMember(parameters, callback: callback)

    }

    /// Get member by Id
    public class func getMemberById(id: String, callback: SpendGoUserCallback) {
        let parameters = ["spendgo_id": id]
        getMember(parameters, callback: callback)
    }

    /// Get member
    class func getMember(parameters: SpendGoJSONDictonary, callback: SpendGoUserCallback) {
        SpendGoService.post("/get", parameters: parameters, needsAuthToken: true) { (response, error) in
            if error != nil {
                callback(user: nil, error: error)
                return
            }
            callback(user: SpendGoUser(json: response), error: nil)
        }
    }
    
    /// Request new password
    public class func forgotPassword(emailAddress: String, callback: SpendGoErrorCallback) {
        //TODO: This is optional as per Anil
        //let finalURLString = ForgotPasswordBaseURL + "\(arc4random_uniform(9999))"
        let parameters = ["email": emailAddress]
        SpendGoService.post("/forgotPassword", parameters: parameters, needsAuthToken: false) { (response, error) in
            callback(error: error)
        }
    }

    public class func userDOBString(dob: NSDate) -> String {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter.stringFromDate(dob)
    }

}
