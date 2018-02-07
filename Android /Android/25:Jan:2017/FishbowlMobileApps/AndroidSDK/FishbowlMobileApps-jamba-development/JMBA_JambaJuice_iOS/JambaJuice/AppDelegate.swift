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
    import SVProgressHUD
    import AppsFlyer
    
    
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
    class AppDelegate: UIResponder, UIApplicationDelegate {
        
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
            
            //        ClpApiClassService.sharedInstance.loginUser();
            
            clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent = false
            clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer = false
            //        clpAnalyticsService.sharedInstance.clpsdkobj?.getMobilePreference()
            
            
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
            //initialize app flyer if it is production build
            if config.ProductionBuild {
                AppsFlyerTracker.sharedTracker().appsFlyerDevKey = config.AppsFlyerDevKey
                AppsFlyerTracker.sharedTracker().appleAppID = "932885438"
            }
            
            Parse.initializeWithConfiguration(ParseClientConfiguration(block: { (configuration: ParseMutableClientConfiguration) -> Void in
                configuration.server = config.ParseBaseURL // '/' important after 'parse'
                configuration.applicationId = config.ParseApplicationID
                configuration.clientKey = config.ParseClientKey
                configuration.localDatastoreEnabled = true
            }))
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
            
            ProductService.forceLoadProductsInBackground()
            ProductService.updateAds()
            
            // Initialize user session
            UserService.loadSession()
            
            // Clear application notification badge
            application.applicationIconBadgeNumber = 0
            
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
            
            
           
            
            // internet Reachability
            
            NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(AppDelegate.networkStatusChanged(_:)), name: ReachabilityStatusChangedNotification, object: nil)
            Reach().monitorReachabilityChanges()
            
            
            // Initialize Fabric
            Fabric.with([Crashlytics()])
            
            //Disable Gift Card
            //        // Validate Promo Alert
            //          if(UserService.sharedUser != nil){
            //                giftCardPromoAlert()
            //        }
            return true
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
            
            if url.absoluteString!.rangeOfString("jambajuice://") != nil {
                log.verbose("Launch app with URL: \(url)")
                return true
            }
            // Handle Facebook callbacks
            //return FBSDKApplicationDelegate.sharedInstance().application(application, openURL: url, sourceApplication: sourceApplication, annotation: annotation)
            return false
        }
        
    
        //Disable Gift Card
        /*func giftCardPromoAlert(){
         let defaults = NSUserDefaults.standardUserDefaults()
         if let showGiftCard = defaults.stringForKey(GiftCardAppConstants.GiftCardPromoOrderAlertOptionKey) {
         if Int(showGiftCard) == GiftCardAppConstants.GiftCardPromoOrderAlertOptionValue.notRightNow.hashValue{
         defaults.setObject(GiftCardAppConstants.GiftCardPromOrderAlertViewShow.show.hashValue, forKey:GiftCardAppConstants.GiftCardPromoOrderAlertViewKey)
         }
         }
         }*/
        
        
    }
    
