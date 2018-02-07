	//
//  AppDelegate.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 3/12/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import CoreData
import Fabric
import Crashlytics
import HDK
import Parse
import Bolts
import OloSDK
import SpendGoSDK
import XCGLogger
import CoreBluetooth
import SVProgressHUD



// Global reference to shared instance
let log = XCGLogger.defaultInstance()

// Global variable for push token
var pushToken:String?
    
    //Global Decleration for product id
    var productID: Int64?
    var productName:String?
    var isAppEvent : Bool = false
    
    var arrBoostCount : NSMutableArray = NSMutableArray()
    var isAppClose : Bool = false
    var productIDStr:String?
    var isIDContain :Bool = false
    var pickTime:String?
    var deviceType : String?
    var TotalModifierCost : Double = 0.0
    var intPopupFrequency : Int = 0;



@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate,CBPeripheralManagerDelegate,clpSdkDelegate{
    
    var window: UIWindow?
    
    var reachability: Reachability?
    
    var label : UILabel?
    
    private var timer : NSTimer?

    
    var isReachable : Bool = true
    var isPushOpen : Bool = false

    var strStoreId : String = ""

    var offerTitle : String = ""
    var offerId : String = ""

  //  var aiv: UIActivityIndicatorView!

    
    var oneTimeAlertShow : Int = 0

    
    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {

        
        // Push Notification config for IOS 8.0
        let settings = UIUserNotificationSettings(forTypes: [.Alert, .Badge, .Sound], categories: nil)
        UIApplication.sharedApplication().registerUserNotificationSettings(settings)
        if(UIApplication.instancesRespondToSelector(#selector(UIApplication.registerUserNotificationSettings(_:)))) {
            
            UIApplication.sharedApplication().registerUserNotificationSettings(settings)
        }
        
        
      var sysInfo = utsname()
      uname(&sysInfo)
      let machine = Mirror(reflecting: sysInfo.machine)
      let identifier = machine.children.reduce("") { identifier, element in
          guard let value = element.value as? Int8 where value != 0 else { return identifier }
          return identifier + String(UnicodeScalar(UInt8(value)))
      }
      
      
        
        deviceType = self.platformType(identifier) as String
//        
//        log.setup(.Verbose, showLogLevel: true, showFileNames: true, showLineNumbers: true, writeToFile: nil, fileLogLevel: nil)
//        log.info("Application Version: \(UIApplication.versionNumber()).\(UIApplication.buildNumber())")
        
        // Configure default error domain
        NSError.applicationErrorDomain = "com.jambajuice"
        
        // Load Jamba configuration
        let config = Configuration.sharedConfiguration
        
        // Initialize Google Analytics
        GAI.sharedInstance().trackUncaughtExceptions = true
        GAI.sharedInstance().dispatchInterval = 20
        GAI.sharedInstance().logger.logLevel = GAILogLevel.Warning
        GAI.sharedInstance().trackerWithTrackingId(config.GoogleAnalyticsAccountID)
        GAI.sharedInstance().defaultTracker.allowIDFACollection = true
        
        // Initialize AppsFlyer
        AppsFlyerTracker.sharedTracker().appsFlyerDevKey = config.AppsFlyerDevKey
        AppsFlyerTracker.sharedTracker().appleAppID = "932885438"

        
        // Initialize Parse.com
        Parse.enableLocalDatastore()
        Parse.setApplicationId(config.ParseApplicationID, clientKey: config.ParseClientKey)
        PFAnalytics.trackAppOpenedWithLaunchOptions(launchOptions)
        
        // Olo/SpendGo configuration
        SpendGoService.configurationForService(config.SpendGoBaseURL, APIKey: config.SpendGoAPIKey, SigningKey: config.SpendGoSigningKey, logger: log)
        OloService.configurationForService(config.OloBaseURL, APIKey: config.OloAPIKey, logger: log)
        
        // Initialize Instabug
        if config.InstabugAPIToken != nil {
            #if (arch(i386) || arch(x86_64)) && os(iOS)
                Instabug.startWithToken(config.InstabugAPIToken, captureSource: IBGCaptureSourceUIKit, invocationEvent: IBGInvocationEventShake)
            #else
                Instabug.startWithToken(config.InstabugAPIToken, captureSource: IBGCaptureSourceUIKit, invocationEvent: IBGInvocationEventScreenshot)
            #endif
        }
        
        // Urbanairship push notifications
        /*    let UAconfig = UAConfig.defaultConfig()
        UAconfig.inProduction = true  // Set to false if debugging with Xcode
        UAconfig.productionAppKey = config.UrbanAirshipAppKey
        UAconfig.productionAppSecret = config.UrbanAirshipAppSecret
        UAconfig.developmentAppKey = config.UrbanAirshipAppKey
        UAconfig.developmentAppSecret = config.UrbanAirshipAppSecret
        UAirship.takeOff(UAconfig)*/
        
        
        ProductService.forceLoadProductsInBackground()
        
        // Initialize user session
        UserService.loadSession()
        
        // Clear application notification badge
        application.applicationIconBadgeNumber = 0
        
//         aiv = UIActivityIndicatorView(activityIndicatorStyle: .Gray)
//        aiv.center = self.window!.rootViewController!.view.center
//        self.window!.rootViewController!.view!.addSubview(aiv)
//        aiv.startAnimating()
        
//        self.window?.userInteractionEnabled = false

//        clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer));
        
        clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer),withClientID:AppConstants.URL.AppPointingClientID(intPointingServer));

        
        clpAnalyticsService.sharedInstance.clpsdkobj?.delegate = self
        
        // Track first time launch
        if SettingsManager.setting(.FirstTimeLaunch) == nil {
            
            AnalyticsService.trackEvent("application", action: "first_time_launch")
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("FIRST_TIME_LAUNCH");
            }

            SettingsManager.setSetting(.FirstTimeLaunch, value: NSDate())
        }
        AnalyticsService.trackEvent("application", action: "did_launch")
        
       
        // Used for when the app awake in kill mode
        if launchOptions?[UIApplicationLaunchOptionsLocationKey] != nil  {
            clpAnalyticsService.sharedInstance.startStandardUpdates();
        }
        
        // internet Reachability
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(AppDelegate.networkStatusChanged(_:)), name: ReachabilityStatusChangedNotification, object: nil)
        Reach().monitorReachabilityChanges()
        
   
        // Initialize Fabric
        Fabric.with([Crashlytics()])

        
        return true
    }
    
    
    func mobileSettingsResponseSucceed()
    {
        
       // bluetooth permission
        
        if clpAnalyticsService.sharedInstance.clpsdkobj!.isTriggerBeacon == true
        {
        
            let queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)
             peripheralManager = CBPeripheralManager(delegate: self, queue: queue)
             if let manager = peripheralManager{
                 manager.delegate = self
             }
        }
        
        if clpAnalyticsService.sharedInstance.clpsdkobj?.appVersion != UIApplication.versionNumber() && clpAnalyticsService.sharedInstance.clpsdkobj?.isForceUpdate == true
        {
            let uiAlert = UIAlertController(title: "Alert !", message: clpAnalyticsService.sharedInstance.clpsdkobj?.appInformation, preferredStyle: UIAlertControllerStyle.Alert)
            
            self.window?.makeKeyAndVisible()
            self.window!.rootViewController!.presentViewController(uiAlert, animated: true, completion: {
                
                _ in })
            
            uiAlert.addAction(UIAlertAction(title: "Ok", style: .Default, handler: { action in
               // print("Click of default button")
                let iTunesLink: String = "https://itunes.apple.com/us/app/jamba-juice/id932885438?mt=8"
                UIApplication.sharedApplication().openURL(NSURL(string: iTunesLink)!)

            }))
        }
            
        
       else if  clpAnalyticsService.sharedInstance.clpsdkobj?.appVersion == UIApplication.versionNumber() && clpAnalyticsService.sharedInstance.clpsdkobj?.isForceUpdate == true
        {
            
            let defaults=NSUserDefaults.standardUserDefaults();
            if defaults.integerForKey("intFrequency") != 0
            {
                intPopupFrequency = defaults.integerForKey("intFrequency")
            }
            
            if intPopupFrequency == clpAnalyticsService.sharedInstance.clpsdkobj?.intFrequency
            {
                intPopupFrequency = 0
            }
            
            if (intPopupFrequency == 0)
            {
            
            let uiAlert = UIAlertController(title: "Alert !", message: clpAnalyticsService.sharedInstance.clpsdkobj?.appInformation, preferredStyle: UIAlertControllerStyle.Alert)
            
            self.window?.makeKeyAndVisible()
            self.window!.rootViewController!.presentViewController(uiAlert, animated: true, completion: {
                
                _ in })
            
            uiAlert.addAction(UIAlertAction(title: "Ok", style: .Default, handler: { action in
                // print("Click of default button")
                let iTunesLink: String = "https://itunes.apple.com/us/app/jamba-juice/id932885438?mt=8"
                UIApplication.sharedApplication().openURL(NSURL(string: iTunesLink)!)
                
            }))
                uiAlert.addAction(UIAlertAction(title: "Cancel", style: .Cancel, handler: { action in
                            print("Click of cancel button")
                        }))
            
        }
            
            intPopupFrequency  = intPopupFrequency + 1
            
            defaults.setObject(intPopupFrequency, forKey: "intFrequency");
            defaults.synchronize();

        }
        
        
        }
        
    
    func platformType(platform : NSString) -> NSString{
        if platform.isEqualToString("iPhone1,1")  {
            return "iPhone 1G"
        }
        else if platform.isEqualToString("iPhone1,2"){
            return "iPhone 3G"
        }
        else if platform.isEqualToString("iPhone2,1"){
            return "iPhone 3GS"
        }
        else if platform.isEqualToString("iPhone3,1"){
            return "iPhone 4"
        }
        else if platform.isEqualToString("iPhone3,3"){
            return "Verizon iPhone 4"
        }
        else if platform.isEqualToString("iPhone4,1"){
            return "iPhone 4S"
        }
        else if platform.isEqualToString("iPhone5,1"){
            return "iPhone 5"
        }
        else if platform.isEqualToString("iPhone5,2"){
            return "iPhone 5"
        }
        else if platform.isEqualToString("iPhone5,3"){
            return "iPhone 5c"
        }
        else if platform.isEqualToString("iPhone5,4"){
            return "iPhone 5c"
        }
        else if platform.isEqualToString("iPhone6,1"){
            return "iPhone 5s"
        }
        else if platform.isEqualToString("iPhone6,2"){
            return "iPhone 5s"
        }
        else if platform.isEqualToString("iPhone7,2"){
            return "iPhone 6"
        }
        else if platform.isEqualToString("iPhone7,1"){
            return "iPhone 6 Plus"
        }
        else if platform.isEqualToString("iPhone8,1"){
            return "iPhone 6s"
        }
        else if platform.isEqualToString("iPhone8,2"){
            return "iPhone 6s Plus"
        }
        else if platform.isEqualToString("iPod1,1"){
            return "iPod Touch 1G"
        }
        else if platform.isEqualToString("iPod2,1"){
            return "iPod Touch 2G"
        }
        else if platform.isEqualToString("iPod3,1"){
            return "iPod Touch 3G"
        }
        else if platform.isEqualToString("iPod4,1"){
            return "iPod Touch 4G"
        }
        else if platform.isEqualToString("iPod5,1"){
            return "iPod Touch 5G"
        }
        else if platform.isEqualToString("iPad1,1"){
            return "iPad"
        }
        else if platform.isEqualToString("iPad2,1"){
            return "iPad 2"
        }
        else if platform.isEqualToString("iPad2,2"){
            return "iPad 2"
        }
        else if platform.isEqualToString("iPad2,3"){
            return "iPad 2"
        }
        else if platform.isEqualToString("iPad2,4"){
            return "iPad 2"
        }
        else if platform.isEqualToString("iPad2,5"){
            return "iPad Mini"
        }
        else if platform.isEqualToString("iPad2,6"){
            return "iPad Mini"
        }
        else if platform.isEqualToString("iPad2,7"){
            return "iPad Mini"
        }
        else if platform.isEqualToString("iPad3,1"){
            return "iPad 3"
        }
        else if platform.isEqualToString("iPad3,2"){
            return "iPad 3"
        }
        else if platform.isEqualToString("iPad3,3"){
            return "iPad 3"
        }
        else if platform.isEqualToString("iPad3,4"){
            return "iPad 4"
        }
        else if platform.isEqualToString("iPad3,5"){
            return "iPad 4"
        }
        else if platform.isEqualToString("iPad3,6"){
            return "iPad 4"
        }
        else if platform.isEqualToString("iPad4,1"){
            return "iPad Air"
        }
        else if platform.isEqualToString("iPad4,2"){
            return "iPad Air"
        }
        else if platform.isEqualToString("iPad4,3"){
            return "iPad Air"
        }
        else if platform.isEqualToString("iPad4,4"){
            return "iPad Mini 2G"
        }
        else if platform.isEqualToString("iPad4,5"){
            return "iPad Mini 2G";}
        else if platform.isEqualToString("iPad4,6"){
            return "iPad Mini 2G";
        }
        else if platform.isEqualToString("iPad4,7"){
            return "iPad Mini 3"
        }
        else if platform.isEqualToString("iPad4,8"){
            return "iPad Mini 3";}
        else if platform.isEqualToString("iPad4,9"){
            return "iPad Mini 3"
        }
        else if platform.isEqualToString("iPad5,3"){
            return "iPad Air 2"
        }
        else if platform.isEqualToString("iPad5,4"){
            return "iPad Air 2"
        }
        else if platform.isEqualToString("AppleTV2,1"){
            return "Apple TV 2G"
        }
        else if platform.isEqualToString("AppleTV3,1"){
            return "Apple TV 3"
        }
        else if platform.isEqualToString("AppleTV3,2"){
            return "Apple TV 3"
        }
        else if platform.isEqualToString("i386"){
            return "Simulator"
        }
        else if platform.isEqualToString("x86_64"){
            return "Simulator"
        }
        else
        {
            return ""
        }
    }
    
    func storeListResponseSucceed()
    {
        
//        self.window?.userInteractionEnabled = true
        
       // aiv.stopAnimating()
      //  aiv.removeFromSuperview()
  
        self.window?.userInteractionEnabled = true
        
        
        print("store response success");
        if (pushToken != nil)
        {
        if SettingsManager.setting(.FirstTimePush) == nil {
            SettingsManager.setSetting(.FirstTimePush, value: pushToken!)
            print("store response clpCustomerCreation");
        }
        }
        
        clpAnalyticsService.sharedInstance.clpCustomerCreation()


    }
    
    func storeListResponseFailed()
    {
        
//        self.window?.userInteractionEnabled = true
        
//        aiv.stopAnimating()
//        aiv.removeFromSuperview()
        
        let controller = UIAlertController(title: "Error",
                                           message: "All Stores not loaded properly. This might cause that you will not get push notifications.",
                                           preferredStyle: .Alert)
        
        controller.addAction(UIAlertAction(title: "OK",
            style: .Default,
            handler: nil))
        
        // self.window?.makeKeyAndVisible()
      //  self.window!.rootViewController!.presentViewController(controller, animated: true, completion: { _ in })
        
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
            timer = NSTimer.scheduledTimerWithTimeInterval(NSTimeInterval(intDelayFire), target: self, selector: #selector(AppDelegate.runTimedCode), userInfo: nil, repeats: true)
            timer?.fire()
        }
        
    }
    
    
    
    func openDynamicPassViaPN(strofferid:String)
    {
        
//        if(intPointingServer == 2)
//        {
//            clpAnalyticsService.sharedInstance.clpsdkobj!.openStaticPass()
//            
//            
//        }
//        else
//        {
            openDynamicPass(strofferid)
            
//        }
        
    }

    
    
    func runTimedCode()
    {
//        clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer));
        
        clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer),withClientID:AppConstants.URL.AppPointingClientID(intPointingServer));

        
        clpAnalyticsService.sharedInstance.clpsdkobj?.appEventTimerMethodCall();
        
    }
    
    
    func clpClosePassbook() {
        
    }
    func clpOpenPassbook() {
        
        
    }
    
    func clpResponseFail(error: NSError!) {
        NSLog("error", error);
        
        clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
        
        
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
                
                request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                request.setValue("mobilesdk", forHTTPHeaderField: "Application")
                request.setValue("1173", forHTTPHeaderField: "tenantid")
                let strData : String = NSUserDefaults.standardUserDefaults().stringForKey("access_token")!
                request.setValue(strData, forHTTPHeaderField: "access_token")
                request.setValue(clpAnalyticsService.sharedInstance.clpsdkobj?.client_ID, forHTTPHeaderField: "client_id")
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
                            clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer),withClientID:AppConstants.URL.AppPointingClientID(intPointingServer));

                            if respnosedata?.length > 0
                            {
                                clpAnalyticsService.sharedInstance.clpsdkobj!.openPassbookAndShowwithData(respnosedata)
                            }
                            dispatch_async(dispatch_get_main_queue()) {
                                SVProgressHUD.dismiss()
                            }
                        }
                        else {
                            // Failure
                            dispatch_async(dispatch_get_main_queue()) {
                                SVProgressHUD.dismiss()
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

    
    
    func clpPushDataBinding(strOfferTitle: String, withId strOfferId: String) {
        
        self.offerTitle = strOfferTitle
        self.offerId = strOfferId
        
    }

    
    func networkStatusChanged(notification: NSNotification) {
        //let userInfo = notification.userInfo
        
        let status = Reach().connectionStatus()
        switch status {
        case .Unknown, .Offline:
            isReachable = false
            print("Not connected")
        case .Online(.WWAN):
            isReachable = true
            print("Connected via WWAN")
        case .Online(.WiFi):
            isReachable = true
            print("Connected via WiFi")
        }
    }
    
    
    
    func applicationWillResignActive(application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }
    
    func applicationDidEnterBackground(application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
        AnalyticsService.trackEvent("application", action: "did_enter_background")
        AnalyticsService.sendPending()
    }
    
    func applicationWillEnterForeground(application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
        AnalyticsService.setUserId(UserService.sharedUser?.emailAddress)
        AnalyticsService.trackEvent("application", action: "will_enter_foreground")
        
        if clpAnalyticsService.sharedInstance.clpsdkobj?.appVersion != UIApplication.versionNumber() && clpAnalyticsService.sharedInstance.clpsdkobj?.isForceUpdate == true
        {
            let uiAlert = UIAlertController(title: "Alert !", message: clpAnalyticsService.sharedInstance.clpsdkobj?.appInformation, preferredStyle: UIAlertControllerStyle.Alert)
            
            self.window?.makeKeyAndVisible()
            self.window!.rootViewController!.presentViewController(uiAlert, animated: true, completion: {
                
                _ in })
            
            uiAlert.addAction(UIAlertAction(title: "Ok", style: .Default, handler: { action in
                // print("Click of default button")
                let iTunesLink: String = "https://itunes.apple.com/us/app/jamba-juice/id932885438?mt=8"
                UIApplication.sharedApplication().openURL(NSURL(string: iTunesLink)!)
                
            }))
        }

    }
    
    func applicationDidBecomeActive(application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.            
        AppsFlyerTracker.sharedTracker().trackAppLaunch()
        NSNotificationCenter.defaultCenter().postNotificationName("appDidBecomeActive", object: nil)

    }
    
    func applicationWillTerminate(application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
        // Saves changes in the application's managed object context before the application terminates.
        AnalyticsService.trackEvent("application", action: "will_terminate")
        clpAnalyticsService.sharedInstance.clpTrackScreenView("APP_CLOSE");

        AnalyticsService.sendPending()
    }
    
    func application(application: UIApplication, openURL url: NSURL, sourceApplication: String?, annotation: AnyObject) -> Bool {
        AnalyticsService.trackOpenURL(url, sourceApplication: sourceApplication, annotation: annotation)
        
        if url.absoluteString.rangeOfString("jambajuice://") != nil {
            log.verbose("Launch app with URL: \(url)")
            return true
        }
        // Handle Facebook callbacks
        //return FBSDKApplicationDelegate.sharedInstance().application(application, openURL: url, sourceApplication: sourceApplication, annotation: annotation)
        return false
    }
    
    // Push notification for register to remote notification
    func application( application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: NSData ) {
        let characterSet: NSCharacterSet = NSCharacterSet( charactersInString: "<>" )
        
        let deviceTokenString: String = ( deviceToken.description as NSString )
            .stringByTrimmingCharactersInSet( characterSet )
            .stringByReplacingOccurrencesOfString( " ", withString: "" ) as String
        pushToken=deviceTokenString;
        NSLog(deviceTokenString);
        
        
        
        label = UILabel(frame: CGRectMake(0, 450, 320, 60))
        label!.numberOfLines = 2;
        label!.textAlignment = NSTextAlignment.Center
        label!.text = deviceTokenString
        self.window!.addSubview(label!)
        label?.hidden = true
        
        
        let defaults=NSUserDefaults.standardUserDefaults();
        defaults.setObject(deviceTokenString, forKey: "pushToken");
        defaults.synchronize();
        
   
    }
    
    
    // Push notification did fail
    func application(application: UIApplication,didFailToRegisterForRemoteNotificationsWithError error: NSError
        ) {
            //Log an error for debugging purposes, user doesn't need to know
            NSLog("Failed to get token; error: %@", error)
            
            clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
            

    }
    
    // Push notification  didreceive
    func application(application: UIApplication, didReceiveRemoteNotification userInfo: [NSObject : AnyObject], fetchCompletionHandler completionHandler: (UIBackgroundFetchResult) -> Void) {
        isPushOpen = true
        
        print("push reveive");

        clpAnalyticsService.sharedInstance.receiveRemoteNotification(userInfo);
        
        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
        {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("PUSH_OFFER_RECIEVE");
        }

        print(userInfo);
        
        
    }
    var peripheralManager : CBPeripheralManager?
    
    // A newly-generated UUID for our beacon //
    let uuid = NSUUID()
    
    // The identifier of our beacon is the identifier of our bundle here //
    let identifier = NSBundle.mainBundle().bundleIdentifier!
    
    // Made up major and minor versions of our beacon region /
    let major: CLBeaconMajorValue = 1
    let minor: CLBeaconMinorValue = 0
    
    func peripheralManagerDidUpdateState(peripheral: CBPeripheralManager){
        
        peripheral.stopAdvertising()
        
        NSLog("The peripheral state is ")
        switch peripheral.state{
        case .PoweredOff:
            NSLog("Powered off")
            let central=CBCentralManager();
            central
            
        case .PoweredOn:
            NSLog("Powered on")
        case .Resetting:
            NSLog("Resetting")
        case .Unauthorized:
            NSLog("Unauthorized")
        case .Unknown:
            NSLog("Unknown")
        case .Unsupported:
            NSLog("Unsupported")
        }
        
        // Bluetooth is now powered on //
        if peripheral.state != .PoweredOn{
            NSLog("ruui");
            
            
            let controller = UIAlertController(title: "Bluetooth",
                message: "Please turn Bluetooth on",
                preferredStyle: .Alert)
            
            controller.addAction(UIAlertAction(title: "OK",
                style: .Default,
                handler: nil))
            
            //            presentViewController(controller, animated: true, completion: nil)
            
        } else {
            
            let manufacturerData = identifier.dataUsingEncoding(
                NSUTF8StringEncoding,
                allowLossyConversion: false)
            
            let theUUid = CBUUID(NSUUID: uuid)
            
            let dataToBeAdvertised:[String: AnyObject!] = [
                CBAdvertisementDataLocalNameKey : "Sample peripheral",
                CBAdvertisementDataManufacturerDataKey : manufacturerData,
                CBAdvertisementDataServiceUUIDsKey : [theUUid],
            ]
            
            peripheral.startAdvertising(dataToBeAdvertised)
            
        }
        
    }
    
 
}

