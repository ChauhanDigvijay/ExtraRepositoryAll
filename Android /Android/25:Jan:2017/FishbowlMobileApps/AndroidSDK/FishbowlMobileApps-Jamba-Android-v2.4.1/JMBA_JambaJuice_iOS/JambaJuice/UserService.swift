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
import SwiftyJSON


typealias UserCallback = (_ user: User?, _ error: NSError?) -> Void
typealias UserErrorCallback = (_ error: NSError?) -> Void
public typealias UserDeliverAddressCallback = (_ deliverAddress:[OloDeliveryAddress], _ error: NSError?) -> Void
typealias UserDeliveryTrackingErrorCallback = (_ deliveryTrackingDetail: OloSavedDeliverAdrdress?, _ error: NSError?) -> Void

class UserService {
    
    static var guestUserInfo: GuestUserInfo?
    static var signUpUserInfo: SignUpUserInfo?
    
    
    
    
    fileprivate(set) static var sharedUser: User?
    
    //Store Session Maintenance
    fileprivate(set) static var selecteStore : Store?

    class func loadSession(complete: @escaping () -> Void) {
        SessionPersistenceManager.loadUserSession { user in
            if user != nil && user!.spendgoAuthToken != nil && user!.oloAuthToken != nil {
                self.sharedUser = user
                SpendGoSessionService.authToken = user!.spendgoAuthToken
                SpendGoSessionService.spendGoId = user!.id
                OloSessionService.authToken = user!.oloAuthToken
            }
            AnalyticsService.setUserId(user?.emailAddress)
            complete()
        }
    }
    
    class func saveSession() {
        if sharedUser != nil {
            SessionPersistenceManager.persistUserSession(sharedUser!)
        }
    }
    
    class func fetchUserUpdate(_ callback: @escaping UserErrorCallback) {
        if sharedUser == nil {
            callback(NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"User is not logged in."]))
            return
        }
        let localSharedUser = self.sharedUser!
        // Get member from SpendGo
        SpendGoUserService.getMemberById(localSharedUser.id) { (spendGoUser, error) -> Void in
            if error != nil {
                callback(error)
                return
            }
            let newUser = User(user: localSharedUser)
            newUser.updateUserFromSpendGoUser(spendGoUser!)
            //Check if we need to fetch the restaurant id
            if newUser.favoriteStore != nil && newUser.favoriteStore!.restaurantId == nil {
                StoreService.storeByStoreCode(newUser.favoriteStore!.storeCode) { (storesFromOlo, location, error) -> Void in
                    log.error("Error:\(String(describing: error?.localizedDescription))")
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
    
    fileprivate class func fetchUserUpdateInternal(_ newUser: User, callback: UserErrorCallback) {
        //Check If we are still logged in and with same user
        if sharedUser != nil && sharedUser!.id == newUser.id {
            //Save user.
            sharedUser = newUser
            //Save session info
            saveSession()
            // Update incomm profile details
            InCommUserConfigurationService.sharedInstance.updateInCommProfileDetails()
            callback(nil)
        }
        else {
            callback(NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"User is not logged in."]))
        }
    }
    
    class func signUpUser(_ signUpUser: SignUpUserInfo, callback: @escaping UserErrorCallback) {
        // TODO: Validate fields have a value
        // signUpUser.validate()
        let dateString = signUpUserInfo!.birthdate!.ISOString()
        let spendGoUser = SpendGoUser(emailAddress: signUpUserInfo!.emailAddress!, firstName: signUpUserInfo!.firstName!, lastName: signUpUserInfo!.lastName!, phoneNumber: signUpUserInfo!.phoneNumber!, enrollForEmailUpdates: signUpUserInfo!.enrollForEmailUpdates, enrollForTextUpdates: signUpUserInfo!.enrollForTextUpdates, dateOfBirth: dateString)
        
        SpendGoSessionService.addMember(spendGoUser, password: signUpUserInfo!.password!, favoriteStoreCode: signUpUserInfo!.favoriteStoreCode!, sendWelcomeEmail: true, emailValidated: true) { (error, didReturn202) -> Void in
            if error != nil {
                callback(error)
                return
            }
            
            //fishbowl Signup User
            if let user = UserService.signUpUserInfo {
                FishbowlApiClassService.sharedInstance.registerUser(user)
            }
            
            AnalyticsService.trackUserRegistration()
            callback(nil)
        }
    }
    
    class func signInUser(_ emailOrPhoneNumber: String, password: String, callback: @escaping UserCallback) {
        SpendGoSessionService.signIn(emailOrPhoneNumber, password: password) { (error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            if SpendGoSessionService.spendGoId == nil || SpendGoSessionService.authToken == nil {
                callback(nil, NSError(description: "Unknown error while authenticating user"))
                return
            }
            
            // Get member from SpendGo
            SpendGoUserService.getMemberById(SpendGoSessionService.spendGoId!) { (spendGoUser, error) -> Void in
                if error != nil {
                    //Reverse anything set in prior steps
                    self.logoutUser{
                        
                    }
                    callback(nil, error)
                    return
                }
                
                BasketService.deleteBasket()
                CurrentStoreService.sharedInstance.deleteCurrentStore()
                
                let user = User(spendGoUser: spendGoUser!, spendGoAuthToken: SpendGoSessionService.authToken!)
                AuditService.trackUserAccess(user, action: "login")
                AnalyticsService.trackUserLogin()
                
                if user.favoriteStore == nil {
                    log.warning("Info: User does not have a favorite store!!")
                    self.getOloAuthTokenFromSpendGoAuthToken(user, callback: callback)
                    return
                }
                
                shortcutItemConfigure()
                // See if we can find a restaurant for this store code
                StoreService.storeByStoreCode(user.favoriteStore!.storeCode) { (stores, location, error) -> Void in
                    log.error("Error:\(String(describing: error?.localizedDescription))")
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
    
    class func shortcutItemConfigure(){ // Configure shortcut menu
        if #available(iOS 9.0, *){
            // setup Shortcut menu
            let rewards = UIMutableApplicationShortcutItem(type: "jambajuice.rewards", localizedTitle: "Rewards", localizedSubtitle: nil, icon: UIApplicationShortcutIcon(templateImageName: "cup"), userInfo: nil)
            UIApplication.shared.shortcutItems!.insert(rewards, at: 0)// Pushing item to shortcut menu items array
        }
    }
    
    fileprivate class func resetStoreAndProceed(_ user: User!, callback: @escaping UserCallback){
        // Reset current store as user prefered store
        CurrentStoreService.sharedInstance.resetStore(user.favoriteStore!)
        
        
        // See if we can find a restaurant for this store code
        StoreService.storeByStoreCode(user.favoriteStore!.storeCode) { (stores, location, error) -> Void in
            log.error("Error:\(String(describing: error?.localizedDescription))")
            if let storeFromOlo = stores.first {
                // Change prefered store as current store
                user.favoriteStore?.updateOloRestaurantProperties(storeFromOlo)
                CurrentStoreService.sharedInstance.resetStore(user.favoriteStore!)
            }
            self.getOloAuthTokenFromSpendGoAuthToken(user, callback: callback)
        }
    }
    
    /// Authenticate or create Olo User with external vendor auth token
    fileprivate class func getOloAuthTokenFromSpendGoAuthToken(_ user: User, callback: @escaping UserCallback) {
        //If any basket is active, let link it to the user.
        let basketId  = BasketService.sharedBasket?.id
        //Now exchange SpendGo Auth Token With Olo
        OloSessionService.getOrCreate("spendgo", providerToken: user.spendgoAuthToken!, basketId: basketId) { (oloAuthToken, error) -> Void in
            if error != nil {
                //Reverse anything set in prior steps
                self.logoutUser{
                }
                callback(nil, error)
                return
            }
            //Set Olo OAuth Toke
            user.oloAuthToken = oloAuthToken
            //Save user.
            self.sharedUser = user
            //Save session info
            self.saveSession()
            callback(self.sharedUser, nil)
        }
    }
    
    /// Request new password
    class func forgotPassword(_ emailAddress: String, callback: @escaping UserErrorCallback) {
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("FORGOT_PASSWORD")
        SpendGoUserService.forgotPassword(emailAddress) { (error) -> Void in
            callback(error)
            AnalyticsService.trackEvent("user_account", action: "forgot_password")
        }
    }
    
    /// Check member status by email address
    class func lookupEmailAddress(_ emailAddress: String, callback: @escaping UserErrorCallback) {
        SpendGoSessionService.lookupEmail(emailAddress) { (status, error) in
            if error != nil {
                callback(error)
                return
            }
            if status!.isEmpty {//Should not happen
                callback(NSError(description: "Unexpected error occurred while processing the request."))
                return
            }
            else if status == "InvalidEmail" {
                callback(NSError(description: "This email address is not valid. Please enter a valid email address."))
                return
            }
            else if status != "NotFound" {
                // Use code 403 to prompt user to log in
                callback(NSError(description: "This email address is associated with another account. Please enter a different email address or proceed to log in.", code: 403))
                return
            }
            callback(nil)
        }
    }
    
    // Check member status by phone number
    class func lookupPhoneNumber(_ phoneNumber: String, callback: @escaping UserErrorCallback) {
        SpendGoSessionService.lookupPhoneNumber(phoneNumber) { (status, error) in
            if error != nil {
                callback(error)
                return
            }
            if status!.isEmpty {//Should not happen
                callback(NSError(description: "Unexpected error occurred while processing the request."))
                return
            }
            else if status != "NotFound" && status != "StarterAccount" {
                // Use code 403 to prompt user to log in
                callback(NSError(description: "This phone number is associated with another account. Please enter a different phone number or proceed to log in.", code: 403))
                return
            }
            callback(nil)
        }
    }
    
    /// Terminate current session
    class func logoutUser(complete:FishbowlCompleteCallback) {
        // Finally logout fishbowl
        sharedUser = nil
        BasketService.deleteBasket()
        
        SessionPersistenceManager.clearUserSession()
        ProductService.deleteAllRecentlyOrderedProducts()
        OloSessionService.deleteAuthenticationToken()
        SpendGoSessionService.logout()
        if(CurrentStoreService.sharedInstance.currentStore != nil){
            CurrentStoreService.sharedInstance.deleteCurrentStore()
        }
        
     //   SignedInMainViewController.sharedInstance().closeAfterSignout()
        AnalyticsService.setUserId(nil)
        AnalyticsService.trackEvent("user_account", action: "logout")
        //clear offers stored in shared instance
        OfferService.sharedInstance.clearOfferList()
        
        // Delete incomm details
        InCommUserConfigurationService.sharedInstance.logoutInCommUser()
        if #available(iOS 9.0, *) {
            for index in 0..<UIApplication.shared.shortcutItems!.count{
                if UIApplication.shared.shortcutItems![index].localizedTitle == "Rewards"{
                    UIApplication.shared.shortcutItems!.remove(at: index)
                    break
                }
            }
        }
        return complete()
    }
    
    /// Update email address and opt in status
    class func updateUserEmailAddress(_ emailAddress: String, emailOptIn: Bool, callback: @escaping UserErrorCallback) {
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("UPDATE_EMAIL")
        SpendGoUserService.updateMemberEmail(emailAddress, emailOptIn: emailOptIn) { (error, didReturn202) in
            // Expect 202 in all cases, but no error
            callback(error)
            
            // Update fishbowl customer for email address
            FishbowlApiClassService.sharedInstance.updateUserDetails()
            AnalyticsService.trackEvent("user_account", action: "update_email")
            
            // update incomm profile
            InCommUserConfigurationService.sharedInstance.updateInCommProfileDetails()
        }
    }
    
    /// Update phone number and opt in status
    class func updateUserPhoneNumber(_ phoneNumber: String, smsOptIn: Bool, callback: @escaping UserErrorCallback) {
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("UPDATE_PHONE")
        SpendGoUserService.updateMemberPhone(phoneNumber, smsOptIn: smsOptIn) { (error, didReturn202) -> Void in
            if error == nil {
                self.sharedUser!.phoneNumber = phoneNumber
                self.sharedUser!.smsOptIn = smsOptIn
                self.saveSession()
                
                // Update fishbowl customer for phone number changes
                FishbowlApiClassService.sharedInstance.updateUserDetails()
                
            }
            callback(error)
            AnalyticsService.trackEvent("user_account", action: "update_phone")
        }
    }
    
    /// Update favorite store
    class func updateFavoriteStore(_ store: Store, callback: @escaping UserErrorCallback) {
        var storeCodeIntValue = ""
        if let storeCodeIntegerValue = Int(store.storeCode){
            storeCodeIntValue = String(storeCodeIntegerValue)
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("\(storeCodeIntValue)", item_name: store.name.lowercased(), event_name: "UPDATE_FAVORITE_STORE")
        if store.storeCode.isEmpty {//Should never happen
            callback(NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Store Id can not be determined."]))
            return
        }
        SpendGoUserService.updateMemberFavoriteStore(store.storeCode) { (error) -> Void in
            if error == nil {
                self.sharedUser!.favoriteStore = store
                self.saveSession()
                
                // Update fishbowl customer  store address change or updation
                FishbowlApiClassService.sharedInstance.updateUserFavouriteStore(UserService.sharedUser!)
                
            }
            callback(error)
            
            AnalyticsService.trackEvent("user_account", action: "update_favorite_store", label: store.name)
        }
    }
    
    /// Update user details
    /// These changes are saved immediatelly
    class func updateUserInfo(_ firstName: String?, lastName: String?, profileImageName: String?, emailOptIn: Bool, smsOptIn: Bool, dateOfBirth: Date?,pushOpt: Bool, callback: @escaping UserErrorCallback) {
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
                self.sharedUser!.pushOptIn = pushOpt
                
                // Update fishbowl customer for user info change
                FishbowlApiClassService.sharedInstance.updateUserDetails()
                
                // Update incomm user profile details
                InCommUserConfigurationService.sharedInstance.updateInCommProfileDetails()
                FishbowlApiClassService.sharedInstance.submitMobileAppEvent("UPDATE_PROFILE")
                
            }
            callback(error)
            AnalyticsService.trackEvent("user_account", action: "update_profile")
            
            
        }
    }
    
    /// Update user password on SpendGo API
    /// Password is changed immediately
    class func changePassword(_ newPassword: String, callback: @escaping UserErrorCallback) {
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("UPDATE_PASSWORD")
        SpendGoUserService.updateMemberPassword(newPassword) { (error) -> Void in
            callback(error)
            AnalyticsService.trackEvent("user_account", action: "update_password")
        }
    }
    
    /// Save user profile avatar locally
    /// Eventually this will be saved on our backend
    fileprivate class func saveProfileImageName(_ profileImageName: String?) {
        if profileImageName != nil {
            sharedUser!.profileImageName = profileImageName!
            saveSession()
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("UPDATE_PROFILE_IMAGE");
            AnalyticsService.trackEvent("user_account", action: "update_profile_image", label: profileImageName)
        }
    }
    
    
    class func recentOrders(_ callback: @escaping UserErrorCallback) {
        
        OloUserService.recentOrders { (status, error) -> Void in
            if error != nil {
                callback(error)
                return
            }
            let list = status.map { oloOrderStatus in OrderStatus(oloOrderStatus: oloOrderStatus) }
            
            // vendor ext ref should not be empty
            let flist = list.filter({ (status) -> Bool in
                return !status.vendorExtRef.isEmpty
            })
            
            self.sharedUser?.recentOrders = flist
            callback(nil)
            if list.count > 0 {
                // NotificationService.removeTag("no_purchases")
            } else {
                // NotificationService.addTag("no_purchases")
            }
        }
    }
    
    class  func getDeliveryTrackingDetails(_ id: String, callback: @escaping UserDeliveryTrackingErrorCallback) {
        OloOrderService.getOrederAddress(id) { (deliveryAddressList, error) in
            if error != nil {
                callback(nil, error)
            } else {
                if deliveryAddressList.count > 0 {
                    callback(deliveryAddressList[0], error)
                }
                callback(nil, nil)
            }
        }
    }
    
    
    class func favouriteOrders(_ callback: @escaping UserErrorCallback) {
        
        OloFaveService.faves { (favorites, error) in
            if error != nil {
                callback(error)
                return
            } else {
                let list = favorites.map { favourite in FavouriteOrder(oloFave: favourite) }
                var favOrder = list
                favOrder.sort(by: { $0.id > $1.id })
                self.sharedUser?.favouriteOrders = favOrder
                //                for index in 0..<favOrder.count {
                //                    OloBasketService.createFromFave(favOrder[index].id, callback: { (basket, error) in
                //                        if error != nil {
                //                            callback(error: error)
                //                            return
                //                        } else {
                //                            favOrder[index].updateProducts(basket!)
                //                        }
                //                        if index == favOrder.count - 1 {
                //                            self.sharedUser?.favouriteOrders = favOrder
                //                        }
                //
                //                    })
                //                }
                
                callback(nil)
            }
        }
    }
    
    class func addFavouriteOrder(_ basketId:String, description:String, callback: @escaping UserErrorCallback) {
        OloFaveService.createFave(basketId, description: description) { (favorites, error) in
            
            callback(error)
            if error == nil && favorites.count > 0 {
                //update the local array
                var favOrder = FavouriteOrder(oloFave: favorites[0])
                OloBasketService.createFromFave(favOrder.id, callback: { (basket, error) in
                    if error != nil {
                        
                        return
                    } else {
                        favOrder.updateProducts(basket!)
                        if self.sharedUser!.favouriteOrders == nil {
                            self.sharedUser!.favouriteOrders = [favOrder]
                        } else {
                            //insert in first index
                            self.sharedUser!.favouriteOrders?.insert(favOrder, at: 0)
                        }
                        NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.FavouriteOrdersUpdated.rawValue), object: nil)
                    }
                })
            }
        }
    }
    
    class func removeFavouriteOrder(_ favouriteId:Int64, callback: @escaping UserErrorCallback) {
        OloFaveService.deleteFave(favouriteId) { (error) in
            
            if error == nil {
                //update the local array
                for index in 0..<self.sharedUser!.favouriteOrders!.count {
                    if favouriteId == self.sharedUser!.favouriteOrders![index].id {
                        self.sharedUser!.favouriteOrders!.remove(at: index)
                        NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.FavouriteOrdersUpdated.rawValue), object: nil)
                        callback(error)
                        break
                    }
                }
            } else {
                callback(error)
            }
        }
    }
    
    class func updateCurrentStore(){
        if(UserService.sharedUser != nil){
            self.saveSession()
        }
    }
    
    //get signaturekey for fishbowl login
    class func getSpendGoSignature() -> String {
        return SpendGoUserService.getSignatureKeyForFishbowlLogin(UserService.sharedUser!.id)
    }
    
    //update user device
    
    
    //MARK:- get User DeliveryAddress
    class func getUserSavedDeliverAddress(_ callback:@escaping UserDeliverAddressCallback) {
        OloUserService.getSavedDeliveryaddressList { (deliveryAddressList, error) in
            callback(deliveryAddressList, error)
        }
    }
}
