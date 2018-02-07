//
//  AppDelegate.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 9/1/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Fabric
import Crashlytics
import HDK
import Parse
import OloSDK
import SpendGoSDK
import XCGLogger

// Global reference to shared instance
let log = XCGLogger.defaultInstance()

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        log.setup(.Verbose, showLogLevel: false, showFileNames: true, showLineNumbers: true, writeToFile: nil, fileLogLevel: nil)
        log.info("Application Version: \(UIApplication.versionNumber()).\(UIApplication.buildNumber())")

        // Configure default error domain
        NSError.applicationErrorDomain = "com.jambajuice"

        // Initialization
        initializeFrameworks(launchOptions)

        // Always load products when application launches
        ProductService.forceLoadProductsInBackground()

        // Track first time launch
        SettingsManager.registerDefaultSettings()
        if SettingsManager.setting(.FirstTimeLaunch) == nil {
            AnalyticsService.trackEvent("application", action: "first_time_launch")
            SettingsManager.setSetting(.FirstTimeLaunch, value: NSDate())
        }

        AnalyticsService.trackEvent("application", action: "did_launch")
        return true
    }

    private func initializeFrameworks(launchOptions: [NSObject: AnyObject]?) {
        // Load Jamba configuration
        let config = Configuration.sharedConfiguration

        AnalyticsService.initialize(config.googleAnalyticsAccountID)

        // Initialize Fabric
        Fabric.with([Crashlytics()])

        // Initialize Parse.com
        Parse.enableLocalDatastore()
        Parse.setApplicationId(config.parseApplicationID, clientKey: config.parseClientKey)
        PFAnalytics.trackAppOpenedWithLaunchOptions(launchOptions)

        // Olo/SpendGo configuration
        SpendGoService.configurationForService(config.spendGoBaseURL, APIKey: config.spendGoAPIKey, SigningKey: config.spendGoSigningKey, logger: log)
        OloService.configurationForService(config.oloBaseURL, APIKey: config.oloAPIKey, logger: log)

        // Initialize Instabug
        if config.instabugAPIToken != nil {
            #if (arch(i386) || arch(x86_64)) && os(iOS)
                Instabug.startWithToken(config.instabugAPIToken, captureSource: IBGCaptureSourceUIKit, invocationEvent: IBGInvocationEventShake)
            #else
                Instabug.startWithToken(config.instabugAPIToken, captureSource: IBGCaptureSourceUIKit, invocationEvent: IBGInvocationEventScreenshot)
            #endif
        }
    }

    func applicationWillResignActive(application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }

}
