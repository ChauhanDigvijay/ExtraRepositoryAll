//
//  UserService.swift
//  JambaJuice
//
//  Created by Taha Samad on 07/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK
import SpendGoSDK
import Parse
import HDK

typealias UserCallback = (user: User?, error: NSError?) -> Void
typealias UserErrorCallback = (error: NSError?) -> Void

class UserService {

    private(set) static var sharedUser: User?

    class func signInUser(emailOrPhoneNumber: String, password: String, callback: UserCallback) {
        SpendGoSessionService.signIn(emailOrPhoneNumber, password: password) { (error) -> Void in
            if error != nil {
                callback(user: nil, error: error)
                return
            }
            if SpendGoSessionService.spendGoId == nil || SpendGoSessionService.authToken == nil {
                callback(user: nil, error: NSError(description: "Unknown error while authenticating user"))
                return
            }

            // Get member from SpendGo
            SpendGoUserService.getMemberById(SpendGoSessionService.spendGoId!) { (spendGoUser, error) -> Void in
                if error != nil {
                    //Reverse anything set in prior steps
                    self.logoutUser()
                    callback(user: nil, error: error)
                    return
                }

                let user = User(spendGoUser: spendGoUser!, spendGoAuthToken: SpendGoSessionService.authToken!)
                AuditService.trackUserAccess(user, action: "login")
                AnalyticsService.trackUserLogin()
                self.getOloAuthTokenFromSpendGoAuthToken(user, callback: callback)
            }
        }
    }

    /// Authenticate or create Olo User with external vendor auth token
    private class func getOloAuthTokenFromSpendGoAuthToken(user: User, callback: UserCallback) {
        //If any basket is active, let link it to the user.
        let basketId  = BasketService.sharedInstance.currentBasket?.basketId
        //Now exchange SpendGo Auth Token With Olo
        OloSessionService.getOrCreate("spendgo", providerToken: user.spendgoAuthToken!, basketId: basketId) { (oloAuthToken, error) -> Void in
            if error != nil {
                //Reverse anything set in prior steps
                self.logoutUser()
                callback(user: nil, error: error)
                return
            }
            self.sharedUser = user
            self.sharedUser?.oloAuthToken = oloAuthToken
            callback(user: self.sharedUser, error: nil)
        }
    }

    /// Terminate current session
    class func logoutUser() {
        sharedUser = nil
        OloSessionService.deleteAuthenticationToken()
        SpendGoSessionService.logout()
        AnalyticsService.setUserId(nil)
        AnalyticsService.trackEvent("user_account", action: "logout")
    }

}
