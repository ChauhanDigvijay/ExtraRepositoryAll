//
//  ClpApiClassService.swift
//  JambaJuice
//
//  Created by VT016 on 27/12/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import UIKit
import SVProgressHUD
import SwiftyJSON
// Global clypsdk

protocol ClpApiClassServiceDelegate: class {
    func fishbowlLoginCallback(successFlag:Bool)
}

class ClpApiClassService:NSObject {
    var apiClass:AnyObject?
    var clpsdkobj:clpsdk?
    
    weak var delegate:ClpApiClassServiceDelegate?
    
    static let sharedInstance=ClpApiClassService();
    override init() {
        super.init()
        apiClass = ApiClasses.sharedManager()
    }
    
    let loginApiUrl = "/member/login"
    let memberCreateApiUrl = "/member/create"
    let memberUpdateApiUrl = "/member/update"
    let getMemberApiUrl = "/member/getMember"
    let updateUserDeviceApiUrl = "/member/deviceUpdate"
    let getAllStoresApiUrl = "/mobile/stores/getstores"
    let getOffersApiUrl = "/mobile/getoffers/"    //need to append customer id
    let getOfferByOfferIdApiUrl = "/mobile/getOfferByOfferId/"     //need to append offer id
    let getRewardsApiUrl = "/mobile/getrewards/strCustomerID/0"  //need to append customer id
    let getLoyaltyPointsApiUrl = "/mobile/getPointRewardInfo/strCustomerID" //need to append customer id
    let getPromoCodeApiUrl = "/mobile/getPromo/" //need to append customer id
    let updateFavouriteStoreApiUrl = "/member/updateStore"
    let logoutApiUrl = "/member/logout"
    let getPointBankOfferApiUrl = "/mobile/getPointBankOffer/strCustomerID"
    let useOffer = "/loyalty/useOffer"
    let getAllRewardsApiUrl = "/loyaltyw/getallrewardoffer"
    let signupRuleListApiUrl = "/loyaltyw/getallrewardofferBaseURL/loyalty/signupRuleList"
    let forgotPasswordApiUrl = "/member/forgetPassMail"
    let changePasswordApiUrl = "/member/changePassword"
    
    //login user with Clp platform
    func loginUser()  {
        let param:NSDictionary = ["username":"testertesting@gmail.com","password":"123456"]
        apiClass?.loginAPI(param as [NSObject : AnyObject], url: loginApiUrl, withTarget: self, withSelector: #selector(self.loginCallBack(_:)))
    }
    
    func loginCallBack(response: AnyObject?) {
        if response == nil {
            return
        }
        if let result = response!.valueForKey("successFlag") as? Bool {
            if result {
                let defaults = NSUserDefaults.standardUserDefaults()
                defaults.setObject(response!.valueForKey("accessToken"), forKey: "access_token")
                defaults.synchronize()
                if UserService.sharedUser != nil {
                    getOffers()
                }
                return
            }
        }
    }
    
    //logout user from Clp platform
    func logoutUser() {
        let defaults = NSUserDefaults.standardUserDefaults()
        defaults.removeObjectForKey("CustomerId")
//        let param:NSDictionary = ["mobile":"Application"]
//        apiClass?.logoutAPI(param as [NSObject : AnyObject], url: logoutApiUrl, withTarget: self, withSelector: #selector(self.logoutCallback(_:)))
    }
    
    func logoutCallback(response: AnyObject?) {
        if response == nil {
            return
        }
        if let result = response!.valueForKey("successFlag") as? Bool {
            if result {
                print("\(response)")
            }
        }
    }
    
    //get the user details from the clp platform
    func getUserDetails() {
        apiClass?.getMember(nil, url: getMemberApiUrl, withTarget: self, withSelector: #selector(self.getUserDetailsCallback(_:)))
    }
    
    func getUserDetailsCallback(response: AnyObject?) {
        if response == nil {
//            delegate?.fishbowlLoginCallback(false)
            return
        }
        if let result = response!.valueForKey("successFlag") as? Bool {
            if result {
                if let customerId = response!.valueForKey("customerID") as? Int {
                    let defaults = NSUserDefaults.standardUserDefaults()
                    defaults.setObject("\(customerId)", forKey: "CustomerId")
                    defaults.synchronize()
                    //                    self.performSelector(#selector(SignInViewController.loginWithFishbowlPlatform))
                    return
                }
            }
        }
//        delegate?.fishbowlLoginCallback(false)
    }
    
    //user registration for clp platform
    func registerUser() {
        let defaults=NSUserDefaults.standardUserDefaults();
        guard let user = UserService.sharedUser else {
            return
        }
        
        var homeStoreCode = "0"
        var homeStoreId = "0"
        if let favStore = user.favoriteStore{
            
            let regex = try! NSRegularExpression(pattern: "[0-9]", options: NSRegularExpressionOptions())
            if regex.firstMatchInString(favStore.storeCode, options: NSMatchingOptions(), range:NSMakeRange(0, favStore.storeCode.characters.count)) != nil {
                
                let code = Int(favStore.storeCode)
                if let homestore : String = String(code!)  {
                    homeStoreCode = homestore
                }
                
                if let storeid = defaults.stringForKey("storeId")
                {
                    homeStoreId = storeid
                } else {
                    //                    let storeId = String(clpsdkobj!.filterFavoritearray( String(Int((user.favoriteStore!.storeCode))!)))
                    //                    defaults.setObject(storeId, forKey: "storeId");
                    defaults.synchronize();
                    //                    clpCustomerObj.homeStoreID = storeId
                }
            } else {
                homeStoreCode = favStore.storeCode
                
                if let storeid = defaults.stringForKey("storeId")
                {
                    homeStoreId = storeid
                } else {
                    
                    //                    let storeId = String(clpsdkobj!.filterFavoritearray(user.favoriteStore!.storeCode))
                    //                    defaults.setObject(storeId, forKey: "storeId");
                    defaults.synchronize();
                    //                    clpCustomerObj.homeStoreID = storeId
                }
            }
        }
        
        var customerDeviceID = 0
        let MyKeychainWrapper = KeychainWrapper()
        
        if let deviceId = MyKeychainWrapper.myObjectForKey(kSecValueData) as? Int {
            customerDeviceID = deviceId
        } else {
            if let deviceId = UIDevice.currentDevice().identifierForVendor?.UUIDString {
                MyKeychainWrapper.mySetObject(deviceId, forKey:kSecValueData)
                MyKeychainWrapper.writeToKeychain()
                //                    clpCustomerObj.customerDeviceID = deviceId
                if Int(deviceId) != nil {
                    customerDeviceID = Int(deviceId)!
                }
            }
        }

        var pushToken = ""
        if defaults.stringForKey("pushToken") != nil {
            pushToken = defaults.stringForKey("pushToken")!
        }
        
        let dictionary: NSDictionary = [
            "firstName" : user.firstName!,
            "lastName" : user.lastName!,
            "loginID" : user.id,
            "email" : user.emailAddress!,
            "phone" : user.phoneNumber,
            "homeStore" : homeStoreCode,
            "homeStoreID" : homeStoreId,
            "customerDeviceID" : customerDeviceID,
            "smsOpted" : self.convertBoolToString(user.smsOptIn),
            "emailOpted" : self.convertBoolToString(user.emailOptIn),
            "pushOpted" : self.convertBoolToString(user.pushOptIn),
            "phoneOpted" :"N",
            "phoneOpted" : "N",
            "loyalityRewards" :"0",
            "companyID" : "7",
            "deviceVendor" : "Apple",
            "deviceOsVersion" : UIDevice.currentDevice().systemVersion,
            "deviceType" : UIDevice.currentDevice().model,
            "enabledFlag" : "Y",
            "pushToken" : pushToken
        ]
        
        apiClass?.registerAPI(dictionary as [NSObject : AnyObject], url: memberCreateApiUrl, withTarget: self, withSelector: #selector(self.userRegistrationCallback(_:)))
    }
    
    func userRegistrationCallback(response: AnyObject?) {
        if response == nil {
            delegate?.fishbowlLoginCallback(false)
            return
        }
        if let result = response!.valueForKey("successFlag") as? Bool {
            if result {
                if let customerId = response!.valueForKey("memberid") as? Int {
                    let defaults = NSUserDefaults.standardUserDefaults()
                    defaults.setObject("\(customerId)", forKey: "CustomerId")
                    defaults.synchronize()
//                    getOffers()
                    updateUserDevice()
                    self.delegate?.fishbowlLoginCallback(true)
                    return
                }
            }
        }
        delegate?.fishbowlLoginCallback(false)
    }
    
    //user registration for clp platform
    func updateUserDetails() {
        let defaults=NSUserDefaults.standardUserDefaults();
        guard let user = UserService.sharedUser else {
            return
        }

        var homeStoreCode = "0"
        var homeStoreId = "0"
        if let favStore = user.favoriteStore{
            
            let regex = try! NSRegularExpression(pattern: "[0-9]", options: NSRegularExpressionOptions())
            if regex.firstMatchInString(favStore.storeCode, options: NSMatchingOptions(), range:NSMakeRange(0, favStore.storeCode.characters.count)) != nil {
                
                let code = Int(favStore.storeCode)
                if let homestore : String = String(code!)  {
                    homeStoreCode = homestore
                }
                
                if let storeid = defaults.stringForKey("storeId")
                {
                    homeStoreId = storeid
                } else {
                    //                    let storeId = String(clpsdkobj!.filterFavoritearray( String(Int((user.favoriteStore!.storeCode))!)))
                    //                    defaults.setObject(storeId, forKey: "storeId");
                    defaults.synchronize();
                    //                    clpCustomerObj.homeStoreID = storeId
                }
            } else {
                homeStoreCode = favStore.storeCode
                
                if let storeid = defaults.stringForKey("storeId")
                {
                    homeStoreId = storeid
                } else {
                    
                    //                    let storeId = String(clpsdkobj!.filterFavoritearray(user.favoriteStore!.storeCode))
                    //                    defaults.setObject(storeId, forKey: "storeId");
                    defaults.synchronize();
                    //                    clpCustomerObj.homeStoreID = storeId
                }
            }
        }
        
        var customerDeviceID = 0
        let MyKeychainWrapper = KeychainWrapper()
        
        if let deviceId = MyKeychainWrapper.myObjectForKey(kSecValueData) as? Int {
            customerDeviceID = deviceId
        } else {
            if let deviceId = UIDevice.currentDevice().identifierForVendor?.UUIDString {
                MyKeychainWrapper.mySetObject(deviceId, forKey:kSecValueData)
                MyKeychainWrapper.writeToKeychain()
                //                    clpCustomerObj.customerDeviceID = deviceId
                if Int(deviceId) != nil {
                    customerDeviceID = Int(deviceId)!
                }
            }
        }

        var pushToken = ""
        if defaults.stringForKey("pushToken") != nil {
            pushToken = defaults.stringForKey("pushToken")!
        }
        
        let dictionary: NSDictionary = [
            "firstName" : user.firstName!,
            "lastName" : user.lastName!,
            "loginID" : user.id,
            "email" : user.emailAddress!,
            "phone" : user.phoneNumber,
            "homeStore" : homeStoreCode,
            "homeStoreID" : homeStoreId,
            "customerDeviceID" : customerDeviceID,
            "smsOpted" : self.convertBoolToString(user.smsOptIn),
            "emailOpted" : self.convertBoolToString(user.emailOptIn),
            "pushOpted" : self.convertBoolToString(user.pushOptIn),
            "phoneOpted" :"N",
            "phoneOpted" : "N",
            "loyalityRewards" :"0",
            "companyID" : "7",
            "deviceVendor" : "Apple",
            "deviceOsVersion" : UIDevice.currentDevice().systemVersion,
            "deviceType" : UIDevice.currentDevice().model,
            "enabledFlag" : "Y",
            "pushToken" : pushToken
        ]
        
        apiClass?.updateProfile(dictionary as [NSObject : AnyObject], url: memberUpdateApiUrl, withTarget: self, withSelector: #selector(self.userProfileUpdateCallback(_:)))
    }
    
    func userProfileUpdateCallback(response: AnyObject?) {
        if response == nil {
            return
        }
        if let result = response!.valueForKey("successFlag") as? Bool {
            if result {
                print("success")
            }
        }
    }
    
    //update user favourite store
    func updateUserFavouriteStore(user: User) {
        if let favStore = user.favoriteStore{
            let defaults = NSUserDefaults.standardUserDefaults()
            let userId = defaults.stringForKey("CustomerId")
            let param: NSDictionary = ["memberid":"\(userId!)","storeCode":"\(favStore.storeCode)"]
            apiClass?.favouritStorehApi(param as [NSObject : AnyObject], url: updateFavouriteStoreApiUrl, withTarget: self, withSelector: #selector(self.userProfileUpdateCallback(_:)))
        }
    }
    
    func userFavouriteStoreUpdateCallback(response: AnyObject?) {
        if response == nil {
            return
        }
        if let result = response!.valueForKey("successFlag") as? Bool {
            if result {
                print("success")
            }
        }
    }
    
    
    //update user device
    func updateUserDevice() {
        let defaults = NSUserDefaults.standardUserDefaults()
        let userId = defaults.stringForKey("CustomerId")
        var pushToken = ""
        if defaults.stringForKey("pushToken") != nil {
            pushToken = defaults.stringForKey("pushToken")!
        }
        let param: NSDictionary = ["appId":"\(NSBundle.mainBundle().bundleIdentifier)","deviceId":UIDevice.currentDevice().identifierForVendor!.UUIDString,"deviceOSVersion":UIDevice.currentDevice().systemVersion,"memberid":"\(userId!)","pushToken":pushToken]
        apiClass?.updateDevice(param as [NSObject : AnyObject], url: updateUserDeviceApiUrl, withTarget: self, withSelector: #selector(self.updateUserDeviceCallBack(_:)))
    }
    
    func updateUserDeviceCallBack(response: AnyObject?) {
        getOffers()
        if response == nil {
            return
        }
        print("User device updation \(response)")
    }
    
    //get user offers
    func getOffers() {
        let defaults = NSUserDefaults.standardUserDefaults()
        let userId = defaults.stringForKey("CustomerId")!
        apiClass?.getOffers(nil, url: "\(getOffersApiUrl)\(userId)/0", withTarget: self, withSelector: #selector(self.getOffersCallBack(_:)))
    }
    
    func getOffersCallBack(response: AnyObject?) {
        if response == nil {
            return
        }
        if let result = response!.valueForKey("successFlag") as? Bool {
            if result {
                let offers = ClpOfferSummary(json: JSON(response!))
                OfferService.sharedInstance.updateOffers(offers);
            }
        }
    }
    
    //get user offers
    func getOfferByOfferId(offerId:String) {
        apiClass?.getuserOffer(nil, url: "\(getOfferByOfferIdApiUrl)\(offerId)", withTarget: self, withSelector: #selector(self.getOfferByOfferIdCallBack(_:)))
    }
    
    func getOfferByOfferIdCallBack(response: AnyObject?) {
        if response == nil {
            NSNotificationCenter.defaultCenter().postNotificationName("offerDetail", object: nil, userInfo: nil)
            return
        }
        NSNotificationCenter.defaultCenter().postNotificationName("offerDetail", object: nil, userInfo: response as?[NSObject : AnyObject])
    }
    
    func getPromoCode(offerId : String, isPMIntegrated : Bool) {
        let defaults = NSUserDefaults.standardUserDefaults()
        let userId = defaults.stringForKey("CustomerId")!
        apiClass?.getPromocode(nil, url: "\(getPromoCodeApiUrl)\(userId)/\(offerId)/\(isPMIntegrated)", withTarget: self, withSelector: #selector(self.getPromoCodeCallBack(_:)))
    }
    
    func getPromoCodeCallBack(response: AnyObject?) {
        if response == nil {
            NSNotificationCenter.defaultCenter().postNotificationName("promoCodeForOffer", object: nil, userInfo: nil)
            return
        }
        NSNotificationCenter.defaultCenter().postNotificationName("promoCodeForOffer", object: nil, userInfo: response as?[NSObject : AnyObject])
    }
    
    
    // app event
    func clpTrackScreenView(name: String)
    {
        
        let systemVersion = UIDevice.currentDevice().systemVersion;
        var clpTrackScreenViewDict = [String: String]()
        //clpTrackScreenViewDict["action"] = "AppEvent";
        clpTrackScreenViewDict["event_name"] = name;
        clpTrackScreenViewDict["deviceType"]  = "iPhone";
        clpTrackScreenViewDict["device_os_ver"] = systemVersion;
        if isAppEvent {
            if let productId = productID {
                if let productid : String = String(productId) {
                    clpTrackScreenViewDict["item_id"] = String(productid);
                }
            }
            if (productName != nil) {
                clpTrackScreenViewDict["item_name"]  = productName;
            }
            isAppEvent = false;
        }
        if isIDContain {
            if (productName != nil) {
                clpTrackScreenViewDict["item_name"] = productName;
            }
            if (productIDStr != nil) {
                clpTrackScreenViewDict["item_id"] =   productIDStr;
            }
            isIDContain = false;
        }
//        clpsdkobj!.updateAppEvent(clpTrackScreenViewDict);
    }
    
    //from clpanalytics service
    func clpTrackScreenError(name: String)
    {
        var appError = [String: String]()
        appError["event_name"] = name;
        let currentDateTime = NSDate()
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat="yyyy-MM-dd"
        let string = dateFormatter.stringFromDate(currentDateTime)
        appError["event_time"]=string;
//        clpsdkobj?.updateAppEvent(appError);
    }
    
    // Convert date to string
    func convertDateToString(date:NSDate)-> String{
        let dateFormatter = NSDateFormatter()
        // dateFormatter.dateFormat="yyyy-MM-dd\'T\'HH:mm:ssZ"
        dateFormatter.locale = NSLocale.currentLocale()
        dateFormatter.dateFormat="yyyy-MM-dd HH:mm:ss Z";
        print(date);
        let string = dateFormatter.stringFromDate(date)
        print(string)
        return string;
    }
    
    // Convert bool to string
    func convertBoolToString(boolValue:Bool)-> String{
        if(boolValue){
            return "Y";
        }
        else{
            return "N";
        }
    }
}
