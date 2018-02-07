//
//  ClpAnalyticsService.swift
//  JambaJuice
//
//  Created by Sridhar R on 1/18/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import UIKit
import SVProgressHUD
// Global clypsdk



class clpAnalyticsService:NSObject,clpSdkDelegate{
    var clpsdkobj:clpsdk?
    private var timer : NSTimer?
    static let sharedInstance=clpAnalyticsService();
    override init() {
        super.init()
        //        clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer));
        
        clpsdkobj = clpsdk.sharedInstanceWithAPIKey()
        
        //        clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer),withClientID:AppConstants.jambaClientKey);
        
        
        //clpsdkobj?.ENABLE_LOCAL_NOTIFICATION=0;
        //clpsdkobj?.delegate = self
        //clpsdkobj?.startStandardUpdate();
    }
    
    func fireTimer(intDelayFire:Int)
    {
        if ((timer) != nil)
        {
            timer?.invalidate()
            timer = nil;
        }
        if intDelayFire != 0
        {
            timer = NSTimer.scheduledTimerWithTimeInterval(NSTimeInterval(intDelayFire), target: self, selector: #selector(clpAnalyticsService.runTimedCode), userInfo: nil, repeats: true)
            timer?.fire()
        }
        
    }
    
    
    func runTimedCode()
    {
        
        //clpsdkobj?.appEventTimerMethodCall();
        
    }
    
    func mobileSettingsResponseSucceed()
    {
        
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
        if isAppEvent == true
        {
            
            
            if let productId = productID
            {
                
                if let productid : String = String(productId)
                {
                    clpTrackScreenViewDict["item_id"] = String(productid);
                }
            }
            if (productName != nil)
            {
                clpTrackScreenViewDict["item_name"]  = productName;
            }
            
            isAppEvent = false;
            
        }
        
        
        if isIDContain == true
        {
            if (productName != nil)
            {
                clpTrackScreenViewDict["item_name"] = productName;
            }
            if (productIDStr != nil)
            {
                clpTrackScreenViewDict["item_id"] =   productIDStr;
            }
            
            isIDContain = false;
        }
        
        
        //        clpsdkobj!.updateAppEvent(clpTrackScreenViewDict);
    }
    
    
    
    // pass event
    func clpTrackScreenPassView(name: String)
    {
        let systemVersion = UIDevice.currentDevice().systemVersion;
        var clpTrackScreenViewDict = [String: String]()
        clpTrackScreenViewDict["event_name"] = name;
        clpTrackScreenViewDict["deviceType"]  = "iPhone";
        clpTrackScreenViewDict["device_os_ver"] = systemVersion;
        if isAppEvent == true
        {
            if let productid : String = String(productID!)
            {
                clpTrackScreenViewDict["item_id"] = String(productid);
            }
            if (productName != nil)
            {
                clpTrackScreenViewDict["item_name"]  = productName;
            }
            
            if let offerid : String = String(productID!)
            {
                clpTrackScreenViewDict["offerid"] = String(offerid);
            }
            isAppEvent = false;
        }
        //clpsdkobj!.updateOfferEventWithPass(clpTrackScreenViewDict);
        
    }
    
    
    
    
    func clpTrackScreenError(name: String)
    {
        var appError = [String: String]()
        appError["event_name"] = name;
        let currentDateTime = NSDate()
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat="yyyy-MM-dd"
        let string = dateFormatter.stringFromDate(currentDateTime)
        appError["event_time"]=string;
        //        //clpsdkobj?.updateAppEvent(appError);
        
    }
    
    
    
    //    func clpCustomerSignupRegistration(){
    //        let clpCustomerObj:CLPCustomer=CLPCustomer();
    //        let companyID=7
    //        let deviceVendor="Apple"
    //        let enabledFlag="Y"
    //
    //
    //        clpCustomerObj.firstName="guest";
    //        clpCustomerObj.emailID = UserService.signUpUserInfo?.emailAddress
    //        clpCustomerObj.homeStore = "0"
    //        clpCustomerObj.homeStoreID = "0"
    //        clpCustomerObj.emailOpted="Y";
    //        clpCustomerObj.smsOpted="Y"
    //        clpCustomerObj.phoneOpted="N"
    //        clpCustomerObj.adOpted="N"
    //
    //        // Clyp company ID for jamba is constant 7
    //        clpCustomerObj.companyID=companyID;
    //
    //        // Push,email and sms option from usr alert information
    //        clpCustomerObj.pushOpted="Y"
    //
    //        let MyKeychainWrapper = KeychainWrapper()
    //
    //        if let deviceId = MyKeychainWrapper.myObjectForKey(kSecValueData) as? CLong
    //        {
    //            print("already there")
    //            clpCustomerObj.customerDeviceID = deviceId
    //        }
    //
    //        else
    //        {
    //            MyKeychainWrapper.mySetObject(UIDevice.currentDevice().identifierForVendor!.UUIDString, forKey:kSecValueData)
    //            MyKeychainWrapper.writeToKeychain()
    //            clpCustomerObj.customerDeviceID = CLong( UIDevice.currentDevice().identifierForVendor!.UUIDString)!;
    //
    //        }
    //
    //        clpCustomerObj.deviceType=UIDevice.currentDevice().model
    //        clpCustomerObj.deviceOsVersion=UIDevice.currentDevice().systemVersion;
    //        clpCustomerObj.deviceVendor=deviceVendor;
    //        clpCustomerObj.enabledFlag=enabledFlag;
    //
    //        let defaults = NSUserDefaults.standardUserDefaults()
    //        clpCustomerObj.pushToken=defaults.stringForKey("pushToken");
    //        clpsdkobj!.saveCustomer(clpCustomerObj) { (cusInfo,error) -> Void in
    //            if(error==nil){
    //                NSLog("Saved");
    //                // clyp geo location update
    //                let defaults=NSUserDefaults.standardUserDefaults();
    //                defaults.setObject(cusInfo.customerID, forKey: "CustomerId");
    //                defaults.synchronize();
    //                clpAnalyticsService.sharedInstance.startStandardUpdates();
    //            }
    //            else{
    //                NSLog("Customer Registration Failed in Clyp%@",error);
    //                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
    //
    //
    //            }
    //        }
    //
    //    }
    
    
    
    
    
    //    func clpCustomerCreation(){
    //
    //        let clpCustomerObj:FBGuest=FBGuest();
    //        clpCustomerObj.firstName="guest";
    //        let MyKeychainWrapper = KeychainWrapper()
    //
    //        if let deviceId = MyKeychainWrapper.myObjectForKey(kSecValueData) as? String
    //        {
    //            print("already there")
    //            clpCustomerObj.deviceId = deviceId
    //        }
    //
    //        else
    //        {
    //            MyKeychainWrapper.mySetObject(UIDevice.currentDevice().identifierForVendor!.UUIDString, forKey:kSecValueData)
    //            MyKeychainWrapper.writeToKeychain()
    //            clpCustomerObj.deviceId = UIDevice.currentDevice().identifierForVendor!.UUIDString;
    //
    //        }
    //
    //        clpCustomerObj.deviceType=UIDevice.currentDevice().model
    //        clpCustomerObj.deviceOsVersion=UIDevice.currentDevice().systemVersion;
    //
    //        let defaults = NSUserDefaults.standardUserDefaults()
    //        clpCustomerObj.pushToken=defaults.stringForKey("pushToken");
    //
    //        clpsdkobj!.createCustomer(clpCustomerObj) { (cusInfo,error) -> Void in
    //
    //            if(error==nil && cusInfo.successFlag == true){
    //                NSLog("Saved");
    //                // clyp geo location update
    //                let defaults=NSUserDefaults.standardUserDefaults();
    //                // defaults.setObject(cusInfo.customerID, forKey: "CustomerId");
    //                defaults.synchronize();
    //                clpAnalyticsService.sharedInstance.startStandardUpdates();
    //            }
    //            else
    //            {
    //                // NSLog("Customer Registration Failed in Clyp%@",error);
    //                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
    //            }
    //        }
    //
    //    }
    
    
    
    //    func clpCustomerRegistration(){
    //        let clpCustomerObj:CLPCustomer=CLPCustomer();
    //        let companyID=7
    //        let deviceVendor="Apple"
    //        let enabledFlag="Y"
    //
    //
    //        clpCustomerObj.firstName="guest";
    //        clpCustomerObj.homeStore="0"
    //        clpCustomerObj.homeStoreID = "0"
    //
    //        clpCustomerObj.emailOpted="Y";
    //        clpCustomerObj.smsOpted="Y"
    //        clpCustomerObj.phoneOpted="N"
    //        clpCustomerObj.adOpted="N"
    //
    //        // Clyp company ID for jamba is constant 7
    //        clpCustomerObj.companyID=companyID;
    //
    //        // Push,email and sms option from usr alert information
    //        clpCustomerObj.pushOpted="Y"
    //
    //        let MyKeychainWrapper = KeychainWrapper()
    //
    //        if let deviceId = MyKeychainWrapper.myObjectForKey(kSecValueData) as? String
    //        {
    //            print("already there")
    //            clpCustomerObj.deviceID = deviceId
    //        }
    //        else
    //        {
    //            MyKeychainWrapper.mySetObject(UIDevice.currentDevice().identifierForVendor!.UUIDString, forKey:kSecValueData)
    //            MyKeychainWrapper.writeToKeychain()
    //            clpCustomerObj.deviceID = UIDevice.currentDevice().identifierForVendor!.UUIDString;
    //        }
    //        clpCustomerObj.deviceType=UIDevice.currentDevice().model
    //        clpCustomerObj.deviceOsVersion=UIDevice.currentDevice().systemVersion;
    //        clpCustomerObj.deviceVendor=deviceVendor;
    //        clpCustomerObj.enabledFlag=enabledFlag;
    //
    //        let defaults = NSUserDefaults.standardUserDefaults()
    //        clpCustomerObj.pushToken=defaults.stringForKey("pushToken");
    //        clpsdkobj!.saveCustomer(clpCustomerObj) { (cusInfo,error) -> Void in
    //            if(error==nil){
    //                NSLog("Saved");
    //                // clyp geo location update
    //                let defaults=NSUserDefaults.standardUserDefaults();
    //                defaults.setObject(cusInfo.customerID, forKey: "CustomerId");
    //                defaults.synchronize();
    //                clpAnalyticsService.sharedInstance.startStandardUpdates();
    //            }
    //            else
    //            {
    //                NSLog("Customer Registration Failed in Clyp%@",error);
    //                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
    //            }
    //        }
    //    }
    
    
    /*func getIPAddresses() -> String {
     var address = String()
     
     // Get list of all interfaces on the local machine:
     var ifaddr : UnsafeMutablePointer<ifaddrs> = nil
     if getifaddrs(&ifaddr) == 0 {
     
     // For each interface ...
     for (var ptr = ifaddr; ptr != nil; ptr = ptr.memory.ifa_next) {
     let flags = Int32(ptr.memory.ifa_flags)
     var addr = ptr.memory.ifa_addr.memory
     
     // Check for running IPv4, IPv6 interfaces. Skip the loopback interface.
     if (flags & (IFF_UP|IFF_RUNNING|IFF_LOOPBACK)) == (IFF_UP|IFF_RUNNING) {
     if addr.sa_family == UInt8(AF_INET) || addr.sa_family == UInt8(AF_INET6) {
     
     // Convert interface address to a human readable string:
     var hostname = [CChar](count: Int(NI_MAXHOST), repeatedValue: 0)
     if (getnameinfo(&addr, socklen_t(addr.sa_len), &hostname, socklen_t(hostname.count),
     nil, socklen_t(0), NI_NUMERICHOST) == 0) {
     address=String.fromCString(hostname)!;
     
     }
     }
     }
     }
     freeifaddrs(ifaddr)
     }
     
     return address
     }*/
    
    func clpResponseFail(error: NSError!) {
        NSLog("error", error);
        
        clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
        
        
    }
    func clpClosePassbook() {
        
    }
    func clpOpenPassbook() {
        
        
    }
    
    func clpPushDataBinding(strOfferTitle: String, withId strOfferId: String) {
        
        let appdelgate = UIApplication.sharedApplication().delegate as! AppDelegate
        appdelgate.offerTitle = strOfferTitle
        appdelgate.offerId = strOfferId
        
    }
    
    
    func receiveRemoteNotification(userInfo:NSObject!) {
        
        //clpsdkobj!.processPushMessage(userInfo! as! [NSObject : AnyObject]);
        
        self.bindData(userInfo! as! [NSObject : AnyObject])
        
        
        // offerid=[userInfo objectForKey:@"offerid"];
    }
    
    func bindData(dic : NSDictionary)
    {
        
        let appdelgate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        appdelgate.offerId = dic["offerid"] as! String
        appdelgate.offerTitle = dic["aps"]!["alert"] as! String
    }
    
    func openDynamicPassViaPN(strofferid:String)
    {
        
        //        if(intPointingServer == 2)
        //        {
        //            clpsdkobj!.openStaticPass()
        //
        //
        //        }
        //        else
        //        {
        openDynamicPass(strofferid)
        
        //        }
        
    }
    
    
    func openDynamicPass(offerId:String)
    {
        
        //123/231455
        
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        
        if appdelegate?.isReachable == true
        {
            
            SVProgressHUD.showWithStatus("Opening Pass...", maskType: .Clear)
            
            
            let defaults = NSUserDefaults.standardUserDefaults()
            if let customerId = defaults.stringForKey("Newmemberid")
            {
                let sessionConfig = NSURLSessionConfiguration.defaultSessionConfiguration()
                let session = NSURLSession(configuration: sessionConfig, delegate: nil, delegateQueue: nil)
                
                let request = NSMutableURLRequest(URL: NSURL(string: "\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getPass")!)
                
                //        request.setValue("application/octet-stream", forHTTPHeaderField: "Content-Type")
                //        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                //        request.setValue(AppConstants.CLPheaderKey, forHTTPHeaderField: "CLP-API-KEY")
                
                request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                request.setValue("mobilesdk", forHTTPHeaderField: "Application")
                request.setValue("1173", forHTTPHeaderField: "tenantid")
                let strData : String = NSUserDefaults.standardUserDefaults().stringForKey("access_token")!
                request.setValue(strData, forHTTPHeaderField: "access_token")
                request.setValue(AppConstants.jambaClientKey, forHTTPHeaderField: "client_id")
                //request.setValue("C65A0DC0F28C469FB7376F972DEFBCB8", forHTTPHeaderField: "client_secret")
                request.setValue("fishbowl", forHTTPHeaderField: "tenantName")
                
                request.HTTPMethod = "POST"
                
                
                let json = ["customerId":customerId,"offerId":offerId,"isPMIntegrated":"false","deviceType":deviceType]
                
                
                do {
                    
                    let bodyData =  try NSJSONSerialization.dataWithJSONObject(json as! [String : String], options: .PrettyPrinted)
                    
                    request.HTTPBody = bodyData
                    
                    let task = session.dataTaskWithRequest(request, completionHandler: { (respnosedata: NSData?, response: NSURLResponse?, error: NSError?) -> Void in
                        let statusCode = (response as! NSHTTPURLResponse).statusCode
                        
                        
                        
                        if (error == nil && statusCode == 200) {
                            // Success
                            
                            //                            clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer));
                            //                            clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer),withClientID:AppConstants.jambaClientKey);
                            
                            if respnosedata?.length > 0
                            {
                                //clpAnalyticsService.sharedInstance.clpsdkobj!.openPassbookAndShowwithData(respnosedata)
                            }
                            dispatch_async(dispatch_get_main_queue()) {
                                SVProgressHUD.dismiss()
                                
                            }
                            
                        }
                        else {
                            // Failure
                            dispatch_async(dispatch_get_main_queue()) {
                                SVProgressHUD.dismiss()
                                
                                print("Failure: %@", error?.localizedDescription);
                            }
                        }
                    })
                    task.resume()
                    
                    
                    
                }
                catch {
                    print(error)
                }
                
            }
            
        }
            
        else
        {
            // presentOkAlert("NO Internet", message: "You seems to be offline. Please check your Internet Connection.")
            
        }
        
        
        
        
    }
    
    func startStandardUpdates(){
        LocationService.sharedInstance.getUserLocation { (location, error) -> Void in
            //self.clpsdkobj?.startStandardUpdate();
        }
    }
    
    // Update the clp customer
    func updatePushToken(pushToken:NSString){
        
    }
    
    
    
    //    func updateClpCustomer(user:User,password:String){
    //
    //        if(user.id.length==0 || user.id.isEmpty){
    //            clpAnalyticsService.sharedInstance.clpCustomerRegistration()
    //            return;
    //        }
    //        else{
    //
    //            let defaults=NSUserDefaults.standardUserDefaults();
    //
    //            let clpCustomerObj:CLPCustomer=CLPCustomer();
    //            let companyID=7
    //            let deviceVendor="Apple"
    //            let enabledFlag="Y"
    //            clpCustomerObj.firstName=user.firstName;
    //            clpCustomerObj.loginID = user.id
    //
    //            //clpCustomerObj.homeStore="0"
    //            //clpCustomerObj.phoneOpted="N"
    //            //clpCustomerObj.adOpted="N"
    //
    //            clpCustomerObj.lastName = user.lastName;
    //            clpCustomerObj.emailID = user.emailAddress;
    //            // clpCustomerObj.password = password
    //            clpCustomerObj.emailOpted = "true";
    //            clpCustomerObj.smsOpted = "true";
    //
    //            //clpCustomerObj.customerAge = user.dateOfBirth! as NSDate;
    //            //clpCustomerObj.customerGender = user.;
    //            //clpCustomerObj.dateOfBirth = user.dateOfBirth as String
    //
    //            clpCustomerObj.dateOfBirth = self.convertDateToString((user.dateOfBirth)!)
    //
    //
    //            clpCustomerObj.cellPhone = user.phoneNumber;
    //            clpCustomerObj.loyalityNo = "";
    //            clpCustomerObj.addressLine1 = "";
    //            clpCustomerObj.addressLine2 = "";
    //            clpCustomerObj.addressCity = "";
    //            clpCustomerObj.addressState = "";
    //            //clpCustomerObj.addressZip = "";
    //            clpCustomerObj.homeStore = "";
    //            clpCustomerObj.customerTenantID = "";
    //            clpCustomerObj.statusCode = 1;
    //            if let favStore = user.favoriteStore{
    //
    //                let regex = try! NSRegularExpression(pattern: "[0-9]", options: NSRegularExpressionOptions())
    //                if regex.firstMatchInString(favStore.storeCode, options: NSMatchingOptions(), range:NSMakeRange(0, favStore.storeCode.characters.count)) != nil {
    //
    //                    let code = Int(favStore.storeCode)
    //                    if let homestore : String = String(code!)  {
    //                        clpCustomerObj.homeStore = homestore;
    //                    }
    //
    //                    if let storeid = defaults.stringForKey("storeId")
    //                    {
    //                        clpCustomerObj.homeStoreID = storeid
    //                    }
    //                    else
    //                    {
    //                        let storeId = String(clpsdkobj!.filterFavoritearray( String(Int((user.favoriteStore!.storeCode))!)))
    //                        defaults.setObject(storeId, forKey: "storeId");
    //                        defaults.synchronize();
    //                        clpCustomerObj.homeStoreID = storeId
    //                    }
    //                }
    //                else
    //                {
    //                    clpCustomerObj.homeStore = favStore.storeCode;
    //                    if let storeid = defaults.stringForKey("storeId")
    //                    {
    //                        clpCustomerObj.homeStoreID = storeid
    //                    }
    //                    else
    //                    {
    //                        let storeId = String(clpsdkobj!.filterFavoritearray(user.favoriteStore!.storeCode))
    //                        defaults.setObject(storeId, forKey: "storeId");
    //                        defaults.synchronize();
    //                        clpCustomerObj.homeStoreID = storeId
    //                    }
    //                }
    //
    //            }
    //            else
    //            {
    //                clpCustomerObj.homeStore = "145"
    //            }
    //
    //            print("storeId----------------- is  \(clpCustomerObj.homeStoreID) storCode is  \(clpCustomerObj.homeStore)")
    //
    //            // Push,email and sms option from usr alert information
    //            clpCustomerObj.smsOpted = self.convertBoolToString(user.smsOptIn)
    //            clpCustomerObj.emailOpted = self.convertBoolToString(user.emailOptIn)
    //            clpCustomerObj.pushOpted = "Y"
    //
    //            clpCustomerObj.phoneOpted = "N";
    //            clpCustomerObj.adOpted = "N";
    //            clpCustomerObj.loyalityRewards="0";
    //
    //            // Clyp company ID for jamba is constant 7
    //            clpCustomerObj.companyID=companyID;
    //
    //
    //            let MyKeychainWrapper = KeychainWrapper()
    //
    //            if let deviceId = MyKeychainWrapper.myObjectForKey(kSecValueData) as? String
    //            {
    //                print("already there")
    //                clpCustomerObj.deviceID = deviceId
    //            }
    //
    //            else
    //            {
    //                MyKeychainWrapper.mySetObject(UIDevice.currentDevice().identifierForVendor!.UUIDString, forKey:kSecValueData)
    //                MyKeychainWrapper.writeToKeychain()
    //                clpCustomerObj.deviceID = UIDevice.currentDevice().identifierForVendor!.UUIDString;
    //
    //            }
    //
    //            clpCustomerObj.deviceType=UIDevice.currentDevice().model
    //            clpCustomerObj.deviceOsVersion=UIDevice.currentDevice().systemVersion;
    //            clpCustomerObj.deviceVendor=deviceVendor;
    //            clpCustomerObj.enabledFlag=enabledFlag;
    //
    //            clpCustomerObj.pushToken=defaults.stringForKey("pushToken");
    //            print("Requesed Dict is \(clpCustomerObj.debugDescription)")
    //
    //            clpsdkobj!.CustomerCreation(clpCustomerObj) { (cusInfo,error) -> Void in
    //
    //                if(error==nil){
    //                    NSLog("Saved");
    //                    print(cusInfo.customerID)
    //                    print(cusInfo.description)
    //
    //                    defaults.setObject(cusInfo.customerID, forKey: "CustomerId");
    //                    defaults.synchronize();
    //                    clpAnalyticsService.sharedInstance.startStandardUpdates();
    //                }
    //                else{
    //                    NSLog("Customer Registration Failed in Clyp%@",error);
    //
    //                    clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
    //                    
    //                    
    //                }
    //            }
    //        }
    //    }
    
    
    // Convert date to string
    //    func convertDateToString(date:NSDate)-> String{
    //        let dateFormatter = NSDateFormatter()
    //       // dateFormatter.dateFormat="yyyy-MM-dd\'T\'HH:mm:ssZ"
    //        dateFormatter.locale = NSLocale.currentLocale()
    //        dateFormatter.dateFormat="yyyy-MM-dd HH:mm:ss Z";
    //        print(date);
    //        let string = dateFormatter.stringFromDate(date)
    //        print(string)
    //        return string;
    //    }
    
    
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






