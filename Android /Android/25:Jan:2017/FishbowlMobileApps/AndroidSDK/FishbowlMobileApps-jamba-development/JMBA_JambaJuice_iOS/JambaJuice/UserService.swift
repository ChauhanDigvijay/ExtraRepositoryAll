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
    
    static var guestUserInfo: GuestUserInfo?
    static var signUpUserInfo: SignUpUserInfo?
    
    
    
    
    private(set) static var sharedUser: User?
    
    //Store Session Maintenance
    private(set) static var selecteStore : Store?
    
    class func loadSession() {
        SessionPersistenceManager.loadUserSession { user in
            if user != nil && user!.spendgoAuthToken != nil && user!.oloAuthToken != nil {
                self.sharedUser = user
                SpendGoSessionService.authToken = user!.spendgoAuthToken
                SpendGoSessionService.spendGoId = user!.id
                OloSessionService.authToken = user!.oloAuthToken
                
            } else {
                // If user is not logged in, flag as no purchases made (for logged-in users, rely on order history)
                // NotificationService.addTag("no_purchases")
            }
            AnalyticsService.setUserId(user?.emailAddress)
            
            // Register remote push notification service when user session loaded
            UIApplication.sharedApplication().registerForRemoteNotifications()
        }
    }
    
    class func saveSession() {
        if sharedUser != nil {
            SessionPersistenceManager.persistUserSession(sharedUser!)
        }
    }
    
    class func fetchUserUpdate(callback: UserErrorCallback) {
        if sharedUser == nil {
            callback(error: NSError(description: "User is not logged in."))
            return
        }
        let localSharedUser = self.sharedUser!
        // Get member from SpendGo
        SpendGoUserService.getMemberById(localSharedUser.id) { (spendGoUser, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            let newUser = User(user: localSharedUser)
            newUser.updateUserFromSpendGoUser(spendGoUser!)
            //Check if we need to fetch the restaurant id
            if newUser.favoriteStore != nil && newUser.favoriteStore!.restaurantId == nil {
                StoreService.storeByStoreCode(newUser.favoriteStore!.storeCode) { (storesFromOlo, location, error) -> Void in
                    log.error("Error:\(error?.localizedDescription)")
                    if storesFromOlo.count > 0 {
                        newUser.favoriteStore!.updateOloRestaurantProperties(storesFromOlo[0])
                    }
                    self.fetchUserUpdateInternal(newUser, callback: callback)
                }
            }
            else {
                self.fetchUserUpdateInternal(newUser, callback: callback)
            }
        }
    }
    
    private class func fetchUserUpdateInternal(newUser: User, callback: UserErrorCallback) {
        //Check If we are still logged in and with same user
        if sharedUser != nil && sharedUser!.id == newUser.id {
            //Save user.
            sharedUser = newUser
            //Save session info
            saveSession()
            
            //Disable Gift Card
            // Update incomm profile details
            //            InCommUserConfigurationService.sharedInstance.updateInCommProfileDetails()
            callback(error: nil)
        }
        else {
            callback(error: NSError(description: "User is not logged in"))
        }
    }
    
    class func signUpUser(signUpUser: SignUpUserInfo, callback: UserErrorCallback) {
        // TODO: Validate fields have a value
        // signUpUser.validate()
        let dateString = signUpUserInfo!.birthdate!.ISOString()
        let spendGoUser = SpendGoUser(emailAddress: signUpUserInfo!.emailAddress!, firstName: signUpUserInfo!.firstName!, lastName: signUpUserInfo!.lastName!, phoneNumber: signUpUserInfo!.phoneNumber!, enrollForEmailUpdates: signUpUserInfo!.enrollForEmailUpdates, enrollForTextUpdates: signUpUserInfo!.enrollForTextUpdates, dateOfBirth: dateString)
        
        SpendGoSessionService.addMember(spendGoUser, password: signUpUserInfo!.password!, favoriteStoreId: signUpUserInfo!.favoriteStoreId!, sendWelcomeEmail: true, emailValidated: true) { (error, didReturn202) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            
            AnalyticsService.trackUserRegistration()
            callback(error: nil)
        }
    }
    
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
                
                BasketService.deleteBasket()
                CurrentStoreService.sharedInstance.deleteCurrentStore()
                
                let user = User(spendGoUser: spendGoUser!, spendGoAuthToken: SpendGoSessionService.authToken!)
                AuditService.trackUserAccess(user, action: "login")
                // Update clyp customer
                //                clpAnalyticsService.sharedInstance.updateClpCustomer(user,password: "");
                AnalyticsService.trackUserLogin()
                
                if user.favoriteStore == nil {
                    log.warning("Info: User does not have a favorite store!!")
                    self.getOloAuthTokenFromSpendGoAuthToken(user, callback: callback)
                    return
                }
                // See if we can find a restaurant for this store code
                StoreService.storeByStoreCode(user.favoriteStore!.storeCode) { (stores, location, error) -> Void in
                    log.error("Error:\(error?.localizedDescription)")
                    if let storeFromOlo = stores.first {
                        user.favoriteStore!.updateOloRestaurantProperties(storeFromOlo)
                        if(user.favoriteStore!.supportsOrderAhead){
                            // When favourite store support order Ahead
                            CurrentStoreService.sharedInstance.resetStore(user.favoriteStore!)
                        }
                    }
                    self.getOloAuthTokenFromSpendGoAuthToken(user, callback: callback)
                }
                
            }
        }
    }
    
    private class func resetStoreAndProceed(user: User!, callback: UserCallback){
        // Reset current store as user prefered store
        CurrentStoreService.sharedInstance.resetStore(user.favoriteStore!)
        
        
        // See if we can find a restaurant for this store code
        StoreService.storeByStoreCode(user.favoriteStore!.storeCode) { (stores, location, error) -> Void in
            log.error("Error:\(error?.localizedDescription)")
            if let storeFromOlo = stores.first {
                // Change prefered store as current store
                user.favoriteStore?.updateOloRestaurantProperties(storeFromOlo)
                CurrentStoreService.sharedInstance.resetStore(user.favoriteStore!)
            }
            self.getOloAuthTokenFromSpendGoAuthToken(user, callback: callback)
        }
    }
    
    /// Authenticate or create Olo User with external vendor auth token
    private class func getOloAuthTokenFromSpendGoAuthToken(user: User, callback: UserCallback) {
        //If any basket is active, let link it to the user.
        let basketId  = BasketService.sharedBasket?.id
        //Now exchange SpendGo Auth Token With Olo
        OloSessionService.getOrCreate("spendgo", providerToken: user.spendgoAuthToken!, basketId: basketId) { (oloAuthToken, error) -> Void in
            if error != nil {
                //Reverse anything set in prior steps
                self.logoutUser()
                callback(user: nil, error: error)
                return
            }
            //Set Olo OAuth Toke
            user.oloAuthToken = oloAuthToken
            //Save user.
            self.sharedUser = user
            //Save session info
            self.saveSession()
            callback(user: self.sharedUser, error: nil)
        }
    }
    
    /// Request new password
    class func forgotPassword(emailAddress: String, callback: UserErrorCallback) {
        SpendGoUserService.forgotPassword(emailAddress) { (error) -> Void in
            callback(error: error)
            AnalyticsService.trackEvent("user_account", action: "forgot_password")
            let user = UserService.sharedUser
            
            if(user != nil)
            {
                productName = user?.emailAddress
                isAppEvent = true
            }
            clpAnalyticsService.sharedInstance.clpTrackScreenView("FORGOT_PASSWORD");
            
        }
    }
    
    /// Check member status by email address
    class func lookupEmailAddress(emailAddress: String, callback: UserErrorCallback) {
        SpendGoSessionService.lookupEmail(emailAddress) { (status, error) in
            if error != nil {
                callback(error: error)
                return
            }
            if status!.isEmpty {//Should not happen
                callback(error: NSError(description: "Unexpected error occurred while processing the request."))
                return
            }
            else if status == "InvalidEmail" {
                callback(error: NSError(description: "This email address is not valid. Please enter a valid email address."))
                return
            }
            else if status != "NotFound" {
                // Use code 403 to prompt user to log in
                callback(error: NSError(description: "This email address is associated with another account. Please enter a different email address or proceed to log in.", code: 403))
                return
            }
            callback(error: nil)
        }
    }
    
    // Check member status by phone number
    class func lookupPhoneNumber(phoneNumber: String, callback: UserErrorCallback) {
        SpendGoSessionService.lookupPhoneNumber(phoneNumber) { (status, error) in
            if error != nil {
                callback(error: error)
                return
            }
            if status!.isEmpty {//Should not happen
                callback(error: NSError(description: "Unexpected error occurred while processing the request."))
                return
            }
            else if status != "NotFound" && status != "StarterAccount" {
                // Use code 403 to prompt user to log in
                callback(error: NSError(description: "This phone number is associated with another account. Please enter a different phone number or proceed to log in.", code: 403))
                return
            }
            callback(error: nil)
        }
    }
    
    /// Terminate current session
    class func logoutUser() {
        
        let user = UserService.sharedUser
        
        if(user != nil)
        {
            if let str1 = user?.firstName
            {
                if let str2 = user?.lastName
                {
                    let str3 = str1 + str2
                    
                    productName = str3
                    if let id : String = user!.id
                    {
                        productID = Int64(id)
                    }
                    isAppEvent = true
                }
                
                clpAnalyticsService.sharedInstance.clpTrackScreenView("LOGOUT");
            }
        }
        
        sharedUser = nil
        BasketService.deleteBasket()
        
        SessionPersistenceManager.clearUserSession()
        ProductService.deleteAllRecentlyOrderedProducts()
        OloSessionService.deleteAuthenticationToken()
        SpendGoSessionService.logout()
        // Update clyp customer registration back to guest
        //        clpAnalyticsService.sharedInstance.clpCustomerRegistration()
        if(CurrentStoreService.sharedInstance.currentStore != nil){
            CurrentStoreService.sharedInstance.deleteCurrentStore()
        }
        
        SignedInMainViewController.sharedInstance().closeAfterSignout()
        
        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.LoggedInStateChanged.rawValue, object: nil)
        AnalyticsService.setUserId(nil)
        AnalyticsService.trackEvent("user_account", action: "logout")
        
        //clear offers stored in shared instance
        OfferService.sharedInstance.clearOfferList()
        
        //logout fishbowl user
        //        ClpApiClassService.sharedInstance.logoutUser()
        
        //Disable Gift Card
        // Delete incomm details
        //        InCommUserConfigurationService.sharedInstance.logoutInCommUser()
        
    }
    
    /// Update email address and opt in status
    class func updateUserEmailAddress(emailAddress: String, emailOptIn: Bool, callback: UserErrorCallback) {
        SpendGoUserService.updateMemberEmail(emailAddress, emailOptIn: emailOptIn) { (error, didReturn202) in
            // Expect 202 in all cases, but no error
            callback(error: error)
            // Update clyp customer for email address
            //            clpAnalyticsService.sharedInstance.updateClpCustomer(UserService.sharedUser!,password: "");
            //            ClpApiClassService.sharedInstance.updateUserDetails()
            //            ClpApiClassService.sharedInstance.registerUser()
            AnalyticsService.trackEvent("user_account", action: "update_email")
            let user = UserService.sharedUser
            
            //Disable Gift Card
            // update incomm profile
            //            InCommUserConfigurationService.sharedInstance.updateInCommProfileDetails()
            
            if(user != nil)
            {
                if let str1 = user?.firstName
                {
                    if let str2 = user?.lastName
                    {
                        let str3 = str1 + str2
                        productName = str3
                        if let id : String = user!.id
                        {
                            productID = Int64(id)
                        }
                        isAppEvent = true
                    }
                    clpAnalyticsService.sharedInstance.clpTrackScreenView("UPDATE_EMAIL");
                }
            }
            
        }
    }
    
    /// Update phone number and opt in status
    class func updateUserPhoneNumber(phoneNumber: String, smsOptIn: Bool, callback: UserErrorCallback) {
        SpendGoUserService.updateMemberPhone(phoneNumber, smsOptIn: smsOptIn) { (error, didReturn202) -> Void in
            if error == nil {
                self.sharedUser!.phoneNumber = phoneNumber
                self.sharedUser!.smsOptIn = smsOptIn
                self.saveSession()
                // Update clyp customer for phone number changes
                //                clpAnalyticsService.sharedInstance.updateClpCustomer(UserService.sharedUser!,password: "");
                //                ClpApiClassService.sharedInstance.updateUserDetails()
                //                ClpApiClassService.sharedInstance.registerUser()
                
            }
            callback(error: error)
            AnalyticsService.trackEvent("user_account", action: "update_phone")
            let user = UserService.sharedUser
            
            if(user != nil)
            {
                productName = phoneNumber
                if let id : String = user!.id
                {
                    productID = Int64(id)
                }
                isAppEvent = true
            }
            
            clpAnalyticsService.sharedInstance.clpTrackScreenView("UPDATE_PHONE");
            
        }
    }
    
    /// Update favorite store
    class func updateFavoriteStore(store: Store, callback: UserErrorCallback) {
        if store.id == nil {//Should never happen
            callback(error: NSError(description: "Store Id can not be determined."))
            return
        }
        let storeId = Int64(store.id!)
        SpendGoUserService.updateMemberFavoriteStore(storeId!) { (error) -> Void in
            if error == nil {
                
                //                var clpsdvarj:clpsdk?
                //       clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.URL.AppPointingURL(intPointingServer))
                
                
                var storeId : String = ""
                let regex = try! NSRegularExpression(pattern: "[0-9]", options: NSRegularExpressionOptions())
                if regex.firstMatchInString(store.storeCode, options: NSMatchingOptions(), range:NSMakeRange(0, store.storeCode.characters.count)) != nil {
                    storeId = String(clpAnalyticsService.sharedInstance.clpsdkobj!.filterFavoritearray( String(Int((store.storeCode))!)))
                    
                }
                    
                else
                {
                    storeId = String(clpAnalyticsService.sharedInstance.clpsdkobj!.filterFavoritearray(store.storeCode))
                    
                }
                
                
                let defaults=NSUserDefaults.standardUserDefaults();
                defaults.setObject(storeId, forKey: "storeId");
                defaults.synchronize();
                self.sharedUser!.favoriteStore = store
                self.saveSession()
                // Update clyp for store address change or updation
                //                clpAnalyticsService.sharedInstance.updateClpCustomer(UserService.sharedUser!,password: "");
                //                ClpApiClassService.sharedInstance.updateUserFavouriteStore(UserService.sharedUser!)
                //                ClpApiClassService.sharedInstance.registerUser()
                
            }
            callback(error: error)
            
            AnalyticsService.trackEvent("user_account", action: "update_favorite_store", label: store.name)
            productName = store.name
            productID = storeId
            isAppEvent = true;
            clpAnalyticsService.sharedInstance.clpTrackScreenView("FAV_STORE");
            
        }
    }
    
    /// Update user details
    /// These changes are saved immediatelly
    class func updateUserInfo(firstName: String?, lastName: String?, profileImageName: String?, emailOptIn: Bool, smsOptIn: Bool, dateOfBirth: NSDate?, callback: UserErrorCallback) {
        saveProfileImageName(profileImageName)
        SpendGoUserService.updateMember(firstName, lastName: lastName, dateOfBirth: dateOfBirth, emailOptIn: emailOptIn, smsOptIn: smsOptIn) { (error) -> Void in
            if error == nil {
                //Check and update only required fields
                if firstName != nil {
                    self.sharedUser!.firstName = firstName
                }
                if lastName != nil {
                    self.sharedUser!.lastName = lastName
                }
                if dateOfBirth != nil {
                    self.sharedUser!.dateOfBirth = dateOfBirth!
                }
                self.sharedUser!.smsOptIn = smsOptIn
                self.sharedUser!.emailOptIn = emailOptIn
                
                // Update clyp customer for user info change
                //                clpAnalyticsService.sharedInstance.updateClpCustomer(UserService.sharedUser!,password: "");
                //                ClpApiClassService.sharedInstance.updateUserDetails()
                //                ClpApiClassService.sharedInstance.registerUser()
                
                
                //Disable Gift Card
                // Update incomm user profile details
                //                InCommUserConfigurationService.sharedInstance.updateInCommProfileDetails()
                
                
            }
            callback(error: error)
            AnalyticsService.trackEvent("user_account", action: "update_profile")
            let user = UserService.sharedUser
            
            if(user != nil)
            {
                if let str1 = user?.firstName
                {
                    if let str2 = user?.lastName
                    {
                        let str3 = str1 + str2
                        productName = str3
                        if let id : String = user?.id
                        {
                            productID = Int64(id)
                        }
                        isAppEvent = true
                    }
                    clpAnalyticsService.sharedInstance.clpTrackScreenView("UPDATE_PROFILE");
                }
            }
            
        }
    }
    
    /// Update user password on SpendGo API
    /// Password is changed immediately
    class func changePassword(newPassword: String, callback: UserErrorCallback) {
        SpendGoUserService.updateMemberPassword(newPassword) { (error) -> Void in
            callback(error: error)
            AnalyticsService.trackEvent("user_account", action: "update_password")
            let user = UserService.sharedUser
            
            if(user != nil)
            {
                if let str1 = user?.firstName
                {
                    if let str2 = user?.lastName
                    {
                        let str3 = str1 + str2
                        productName = str3
                        if let id : String = user!.id
                        {
                            productID = Int64(id)
                        }
                        isAppEvent = true
                    }
                    clpAnalyticsService.sharedInstance.clpTrackScreenView("UPDATE_PASSWORD");
                }
            }
            
        }
    }
    
    /// Save user profile avatar locally
    /// Eventually this will be saved on our backend
    private class func saveProfileImageName(profileImageName: String?) {
        if profileImageName != nil {
            sharedUser!.profileImageName = profileImageName!
            saveSession()
            AnalyticsService.trackEvent("user_account", action: "update_profile_image", label: profileImageName)
            let user = UserService.sharedUser
            
            if(user != nil)
            {
                productName = profileImageName
                if let id : String  = user?.id
                {
                    productID = Int64(id)
                    
                }
                isAppEvent = true
            }
            clpAnalyticsService.sharedInstance.clpTrackScreenView("UPDATE_PROFILE_IMAGE");
            
        }
    }
    
    
    class func recentOrders(callback: UserErrorCallback) {
        
        
        
        OloUserService.recentOrders { (status, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            let list = status.map { oloOrderStatus in OrderStatus(oloOrderStatus: oloOrderStatus) }
            
            // vendor ext ref should not be empty
            let flist = list.filter({ (status) -> Bool in
                return !status.vendorExtRef.isEmpty
            })
            
            self.sharedUser?.recentOrders = flist
            callback(error: nil)
            if list.count > 0 {
                // NotificationService.removeTag("no_purchases")
            } else {
                // NotificationService.addTag("no_purchases")
            }
        }
    }
    
    
    class func updateCurrentStore(){
        if(UserService.sharedUser != nil){
            self.saveSession()
        }
    }
    
}
