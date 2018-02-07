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
     import AppsFlyerLib
     import SwiftyJSON
     import UserNotifications
     
     
     // Global reference to shared instance
     let log = XCGLogger.default
     
     
     
     //Global Decleration for product id
     var intPopupFrequency : Int = 0;
     
     // Global lat long
     var userLocationLat: String = ""
     var userLocationLong: String = ""
     
     @UIApplicationMain
     class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate{
        var window: UIWindow?
        
        
        var reachability: Reachability?
        
        var label : UILabel?
        
        fileprivate var timer : Timer?
        
        
        var isReachable : Bool = true
        
        
        var strStoreId : String = ""
        
        var oneTimeAlertShow : Int = 0
        
        // Jamba notification  shortcut item
        var currentShortCutItem: JambaNotification = JambaNotification.None
        
        func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
            
            LocationService.sharedInstance.getUserLocation { (location, error) in
                if error == nil{
                    if location != nil{
                        userLocationLat = "\(location!.coordinate.latitude)"
                        userLocationLong = "\(location!.coordinate.longitude)"
                    }
                }
            }
            
            // Fishbowl device id
            let defaults = UserDefaults.standard
            if defaults.string(forKey: "device_id") == nil{
                let deviceId = UIDevice.current.identifierForVendor!.uuidString
                let formatedDeviceId = (deviceId as NSString).replacingOccurrences(of: "-", with: "")
                defaults.set(formatedDeviceId, forKey: "device_id")
                defaults.synchronize()
            }
            
            
            // Configure default error domain
            NSError.applicationErrorDomain = "com.jambajuice"
            
            // Load Jamba configuration
            let config = Configuration.sharedConfiguration
            
            // Initialize Google Analytics
            GAI.sharedInstance().trackUncaughtExceptions = true
            GAI.sharedInstance().dispatchInterval = 20
            GAI.sharedInstance().logger.logLevel = GAILogLevel.warning
            GAI.sharedInstance().tracker(withTrackingId: config.GoogleAnalyticsAccountID)
            GAI.sharedInstance().defaultTracker.allowIDFACollection = true
            
            // Initialize AppsFlyer
            //initialize app flyer if it is production build
            if config.ProductionBuild {
                AppsFlyerTracker.shared().appsFlyerDevKey = config.AppsFlyerDevKey
                AppsFlyerTracker.shared().appleAppID = "932885438"
            }
            
            Parse.initialize(with: ParseClientConfiguration(block: { (configuration: ParseMutableClientConfiguration) -> Void in
                configuration.server = config.ParseBaseURL // '/' important after 'parse'
                configuration.applicationId = config.ParseApplicationID
                configuration.clientKey = config.ParseClientKey
                configuration.isLocalDatastoreEnabled = true
            }))
            
            
            
            PFAnalytics.trackAppOpened(launchOptions: launchOptions)
            
            // Olo/SpendGo configuration
            SpendGoService.configurationForService(config.SpendGoBaseURL, APIKey: config.SpendGoAPIKey, SigningKey: config.SpendGoSigningKey, logger: log)
            OloService.configurationForService(config.OloBaseURL, APIKey: config.OloAPIKey, logger: log)
            
            
            ProductService.forceLoadProductsInBackground()
            ProductService.updateAds()
            
            
            // Clear application notification badge
            application.applicationIconBadgeNumber = 0
            
            AnalyticsService.trackEvent("application", action: "did_launch")
            
            
            
            
            // internet Reachability
            
            NotificationCenter.default.addObserver(self, selector: #selector(AppDelegate.networkStatusChanged(_:)), name: NSNotification.Name(rawValue: ReachabilityStatusChangedNotification), object: nil)
            Reach().monitorReachabilityChanges()
            
            
            // Initialize Fabric
            Fabric.with([Crashlytics()])
            
            
            FishbowlApiClassService.sharedInstance.forceUpdate()
            
            // Validate push notification taped
            if let remoteNotification = launchOptions?[UIApplicationLaunchOptionsKey.remoteNotification] as? NSDictionary {
                NSLog("push receive when kill")
                
                self.application(application, didReceiveRemoteNotification: remoteNotification as! [AnyHashable: Any])
            }
            return true
        }
        
        func networkStatusChanged(_ notification: Notification) {
            //let userInfo = notification.userInfo
            
            let status = Reach().connectionStatus()
            switch status {
            case .unknown, .offline:
                isReachable = false
                print("Not connected")
            case .online(.wwan):
                isReachable = true
                print("Connected via WWAN")
            case .online(.wiFi):
                isReachable = true
                print("Connected via WiFi")
            }
        }
        
        // Called when APNs has assigned the device a unique token
        func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
            // Convert token to string
            let deviceTokenString = deviceToken.reduce("", {$0 + String(format: "%02X", $1)})
            let defaults=UserDefaults.standard
            defaults.set(deviceTokenString, forKey: "pushToken");
            defaults.synchronize();
            NSLog("Push token: %@" ,deviceTokenString)
            FishbowlApiClassService.sharedInstance.updateUserDevice {
                // Device updated
            }
            // Persist it in your backend in case it's new
        }
        
        // Push notification for register to remote notification
        func application(_ application: UIApplication,didFailToRegisterForRemoteNotificationsWithError error: Error
            ) {
            //Log an error for debugging purposes, user doesn't need to know
            NSLog("Failed to get token; error: %@", error.localizedDescription)
            
            FishbowlApiClassService.sharedInstance.updateUserDevice {
                // Device updated
            }
        }
        
        func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
            NSLog("push trigger %@", userInfo)
            PushPopupViewController.sharedInstance.processPushNotification(userInfo)
        }
        
        
        func applicationWillResignActive(_ application: UIApplication) {
            // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
            // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
        }
        
        func applicationDidEnterBackground(_ application: UIApplication) {
            // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
            // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
            AnalyticsService.trackEvent("application", action: "did_enter_background")
            AnalyticsService.sendPending()
        }
        
        func applicationWillEnterForeground(_ application: UIApplication) {
            // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
            AnalyticsService.setUserId(UserService.sharedUser?.emailAddress)
            AnalyticsService.trackEvent("application", action: "will_enter_foreground")
            
        }
        
        func applicationDidBecomeActive(_ application: UIApplication) {
            // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
            AppsFlyerTracker.shared().trackAppLaunch()
            NotificationCenter.default.post(name: Notification.Name(rawValue: "appDidBecomeActive"), object: nil)
            
        }
        
        func applicationWillTerminate(_ application: UIApplication) {
            // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
            // Saves changes in the application's managed object context before the application terminates.
            AnalyticsService.trackEvent("application", action: "will_terminate")
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("APP_CLOSE")
            AnalyticsService.sendPending()
        }

        // Below iOS 9.0
        func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
            AnalyticsService.trackOpenURL(url, sourceApplication: sourceApplication, annotation: nil)

            if url.absoluteString.range(of: "jambajuice://") != nil {
                log.verbose("Launch app with URL: \(url)")
                return true
            }
            return false
        }
        @available(iOS 9.0, *) // To check IOS version
        func application(_ app: UIApplication,open url: URL,
                         options: [UIApplicationOpenURLOptionsKey : Any] = [:]) -> Bool{
            
            AnalyticsService.trackOpenURL(url,options: options)
            if url.absoluteString.range(of: "jambajuice://") != nil {
                log.verbose("Launch app with URL: \(url)")
                return true
            }
            return false
            
        }
        
        @available(iOS 9.0, *) // To check IOS version
        func application(_ application: UIApplication, performActionFor shortcutItem: UIApplicationShortcutItem, completionHandler: @escaping (Bool) -> Void) {
            UIApplication.afterDelay(1) {
                NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.ShortcutItemCloseBasket.rawValue), object: nil)
                self.handleShortcutItem(shortcutItem)
            }
            completionHandler(true)
        }
        @available(iOS 9.0, *)
        func handleShortcutItem(_ shortcutItem: UIApplicationShortcutItem){
            
            switch shortcutItem.type{
            case "jambajuce.startOrder":
                self.openProductMenu()
                break
            case "jambajuice.rewards":
                self.openRewards()
                break
            case "jambajuice.locations":
                self.openLocationFinder()
                break
            default:
                break
            }
        }
        func openProductMenu(){
            let getCurrentViewController = getVisibleViewControllerFrom(window!.rootViewController)
            if getCurrentViewController is StoreLocatorViewController && CurrentStoreService.sharedInstance.currentStore == nil{
                return
            }else if getCurrentViewController is ProductMenuViewController{
                return
            }else if getCurrentViewController is UIAlertController{
                if getCurrentViewController?.title == "Choose Store" || getCurrentViewController?.title == "Error"{
                    currentShortCutItem = JambaNotification.None
                    return
                }
            }
            if(UserService.sharedUser == nil){
                currentShortCutItem = JambaNotification.OrderMenuWhenNonSignedIn
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.OrderMenuWhenNonSignedIn.rawValue), object: nil)
            }else {
                currentShortCutItem = JambaNotification.OrderMenuWhenSignedIn
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.OrderMenuWhenSignedIn.rawValue), object: nil)
            }
        }
        func openLocationFinder(){
            let topViewController = self.getVisibleViewControllerFrom(self.window!.rootViewController)
            if(topViewController is StoreLocatorViewController){
                currentShortCutItem = JambaNotification.None
                return
            }else if topViewController is UIAlertController{
                if topViewController?.title == "Choose Store" || topViewController?.title == "Error"{
                    currentShortCutItem = JambaNotification.None
                    return
                }
            }
            if(UserService.sharedUser == nil){
                currentShortCutItem = JambaNotification.LocationSearchWhenNonSignedIn
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.LocationSearchWhenNonSignedIn.rawValue), object: nil)
            }else {
                currentShortCutItem = JambaNotification.LocationSearchWhenSignedIn
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.LocationSearchWhenSignedIn.rawValue), object: nil)
            }
        }
        func openRewards() {
            if(UserService.sharedUser != nil){
                currentShortCutItem = JambaNotification.OpenRewardsAndOfferDetail
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.OpenRewardsAndOfferDetail.rawValue), object: nil)
            }
        }
        
        
        // Register push notification when fishbowl login successful
        func registerPushNotification(){
            // Push notification registration alert
            if #available(iOS 10.0, *){
                UNUserNotificationCenter.current().delegate = self
                UNUserNotificationCenter.current().requestAuthorization(options: [.badge, .sound, .alert], completionHandler: {(granted, error) in
                    if (granted)
                    {
                        UIApplication.inMainThread {
                            UIApplication.shared.registerForRemoteNotifications()
                        }
                    }
                    else{
                        NSLog("Push status false");
                    }
                })
            } else{
                let settings = UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
                UIApplication.inMainThread {
                    UIApplication.shared.registerUserNotificationSettings(settings)
                    UIApplication.shared.registerForRemoteNotifications()
                }
            }
        }
        
        // Get push notification status
        func getPushNotificationEnableStatus() -> Bool{
            if UIApplication.shared.responds(to: #selector(getter: UIApplication.currentUserNotificationSettings)) == true {
                let settings = UIApplication.shared.currentUserNotificationSettings
                if (settings?.types.contains(.alert) == true){
                    return true
                } else {
                    return false
                }
            }else{
                return false
            }
        }
        
        func getVisibleViewControllerFrom(_ vc: UIViewController?) -> UIViewController? {
            if let nc = vc as? UINavigationController {
                return self.getVisibleViewControllerFrom(nc.visibleViewController)
            } else if let tc = vc as? UITabBarController {
                return self.getVisibleViewControllerFrom(tc.selectedViewController)
            } else {
                if let pvc = vc?.presentedViewController {
                    return self.getVisibleViewControllerFrom(pvc)
                } else {
                    return vc
                }
            }
        }
     }
     
