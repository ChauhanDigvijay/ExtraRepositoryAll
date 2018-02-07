//
//  AppDelegate.swift
//  SDKDemo
//
//  Created by Gourav Shukla on 17/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

import UIKit
import UserNotifications
import Fabric
import Crashlytics


fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}

fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l > r
  default:
    return rhs < lhs
  }
}

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate,UINavigationControllerDelegate,clpSdkDelegate,UNUserNotificationCenterDelegate {

    
    var window: UIWindow?
    var nav: UINavigationController?
    var label : UILabel?
    var deviceType : String?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
      // add viewController to root
        window = UIWindow(frame: UIScreen.main.bounds)
        
     if #available(iOS 10.0, *) {
            let center  = UNUserNotificationCenter.current()
            center.delegate = self
            center.requestAuthorization(options: [.sound, .alert, .badge]) { (granted, error) in
                if error == nil{
                    UIApplication.shared.registerForRemoteNotifications()
                }
            }
        } else {
            
            application.registerForRemoteNotifications()
            application.registerUserNotificationSettings(UIUserNotificationSettings(types: .alert,categories: nil))
            // Fallback on earlier versions
        }
    
        //Splash code
        let seconds = 1.4
        let delay = seconds * Double(NSEC_PER_SEC)  // nanoseconds per seconds
        let dispatchTime = DispatchTime.now() + Double(Int64(delay)) / Double(NSEC_PER_SEC)
        let imageData = try? Data(contentsOf: Bundle.main.url(forResource: "MyBistro", withExtension: "gif")!)
        
        let advTimeGif = UIImage.gifImageWithData(imageData!)
        let imageView2 = UIImageView(image: advTimeGif)
        imageView2.frame = CGRect(x: ((self.window?.screen.bounds.size.width)!/2-75), y: ((self.window?.screen.bounds.size.height)!/2-90), width: 150, height: 180)
      
        imageView2.animationImages = advTimeGif!.images;
        imageView2.animationDuration = advTimeGif!.duration;
        imageView2.animationRepeatCount = 1;
        imageView2.image = advTimeGif!.images?.last
        imageView2.startAnimating()
        
        let VC1:UIViewController = UIViewController()
        let splashImage = UIImage(named: "MyBistroSplashScreen.png")!
        let splashImageView = UIImageView(image: splashImage)
        splashImageView.frame = (window?.screen.bounds)!
        window?.rootViewController=VC1
        VC1.view.addSubview(splashImageView);
        VC1.view.bringSubview(toFront: splashImageView)
        
        splashImageView.addSubview(imageView2);
        splashImageView.bringSubview(toFront: imageView2)
        DispatchQueue.main.asyncAfter(deadline: dispatchTime, execute: {
            
            // here code perfomed with delay
            VC1.view.removeFromSuperview()
            self.window?.removeFromSuperview()
            
            let obj = LoginViewController(nibName: "LoginViewController", bundle: nil)
            //obj.delegate = self;
            self.nav = UINavigationController(rootViewController: obj)
            self.window!.rootViewController = self.nav
            self.nav?.navigationBar.isHidden = true
            self.nav!.interactivePopGestureRecognizer!.isEnabled = false
            self.window!.backgroundColor = UIColor.white
        })
        
       window!.makeKeyAndVisible()
        
        var sysInfo = utsname()
        uname(&sysInfo)
        let machine = Mirror(reflecting: sysInfo.machine)
        let identifier = machine.children.reduce("") { identifier, element in
            guard let value = element.value as? Int8 , value != 0 else { return identifier }
            return identifier + String(UnicodeScalar(UInt8(value)))
        }
        
        NSLog("Device Type ----> %@", self.platformType(identifier as NSString));
        
        print(self.platformType(identifier as NSString))
        
        deviceType = self.platformType(identifier as NSString) as String
        
        UserDefaults.standard.set(deviceType, forKey: "deviceType")
        UserDefaults.standard.synchronize()

        FBSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: launchOptions)
        
        Fabric.with([Crashlytics.self])
        
        return true
    }
    
    
    func platformType(_ platform : NSString) -> NSString{
        if platform.isEqual(to: "iPhone1,1"){
            return "iPhone 1G"
        }
        else if platform.isEqual(to: "iPhone1,2"){
            return "iPhone 3G"
        }
        else if platform.isEqual(to: "iPhone2,1"){
            return "iPhone 3GS"
        }
        else if platform.isEqual(to: "iPhone3,1"){
            return "iPhone 4"
        }
        else if platform.isEqual(to: "iPhone3,3"){
            return "Verizon iPhone 4"
        }
        else if platform.isEqual(to: "iPhone4,1"){
            return "iPhone 4S"
        }
        else if platform.isEqual(to: "iPhone5,1"){
            return "iPhone 5"
        }
        else if platform.isEqual(to: "iPhone5,2"){
            return "iPhone 5"
        }
        else if platform.isEqual(to: "iPhone5,3"){
            return "iPhone 5c"
        }
        else if platform.isEqual(to: "iPhone5,4"){
            return "iPhone 5c"
        }
        else if platform.isEqual(to: "iPhone6,1"){
            return "iPhone 5s"
        }
        else if platform.isEqual(to: "iPhone6,2"){
            return "iPhone 5s"
        }
        else if platform.isEqual(to: "iPhone7,2"){
            return "iPhone 6"
        }
        else if platform.isEqual(to: "iPhone7,1"){
            return "iPhone 6 Plus"
        }
        else if platform.isEqual(to: "iPhone8,1"){
            return "iPhone 6s"
        }
        else if platform.isEqual(to: "iPhone8,2"){
            return "iPhone 6s Plus"
        }
        else if platform.isEqual(to: "iPod1,1"){
            return "iPod Touch 1G"
        }
        else if platform.isEqual(to: "iPod2,1"){
            return "iPod Touch 2G"
        }
        else if platform.isEqual(to: "iPod3,1"){
            return "iPod Touch 3G"
        }
        else if platform.isEqual(to: "iPod4,1"){
            return "iPod Touch 4G"
        }
        else if platform.isEqual(to: "iPod5,1"){
            return "iPod Touch 5G"
        }
        else if platform.isEqual(to: "iPad1,1"){
            return "iPad"
        }
        else if platform.isEqual(to: "iPad2,1"){
            return "iPad 2"
        }
        else if platform.isEqual(to: "iPad2,2"){
            return "iPad 2"
        }
        else if platform.isEqual(to: "iPad2,3"){
            return "iPad 2"
        }
        else if platform.isEqual(to: "iPad2,4"){
            return "iPad 2"
        }
        else if platform.isEqual(to: "iPad2,5"){
            return "iPad Mini"
        }
        else if platform.isEqual(to: "iPad2,6"){
            return "iPad Mini"
        }
        else if platform.isEqual(to: "iPad2,7"){
            return "iPad Mini"
        }
        else if platform.isEqual(to: "iPad3,1"){
            return "iPad 3"
        }
        else if platform.isEqual(to: "iPad3,2"){
            return "iPad 3"
        }
        else if platform.isEqual(to: "iPad3,3"){
            return "iPad 3"
        }
        else if platform.isEqual(to: "iPad3,4"){
            return "iPad 4"
        }
        else if platform.isEqual(to: "iPad3,5"){
            return "iPad 4"
        }
        else if platform.isEqual(to: "iPad3,6"){
            return "iPad 4"
        }
        else if platform.isEqual(to: "iPad4,1"){
            return "iPad Air"
        }
        else if platform.isEqual(to: "iPad4,2"){
            return "iPad Air"
        }
        else if platform.isEqual(to: "iPad4,3"){
            return "iPad Air"
        }
        else if platform.isEqual(to: "iPad4,4"){
            return "iPad Mini 2G"
        }
        else if platform.isEqual(to: "iPad4,5"){
            return "iPad Mini 2G";}
        else if platform.isEqual(to: "iPad4,6"){
            return "iPad Mini 2G";
        }
        else if platform.isEqual(to: "iPad4,7"){
            return "iPad Mini 3"
        }
        else if platform.isEqual(to: "iPad4,8"){
            return "iPad Mini 3";
        }
        else if platform.isEqual(to: "iPad4,9"){
            return "iPad Mini 3"
        }
        else if platform.isEqual(to: "iPad5,3"){
            return "iPad Air 2"
        }
        else if platform.isEqual(to: "iPad5,4"){
            return "iPad Air 2"
        }
        else if platform.isEqual(to: "AppleTV2,1"){
            return "Apple TV 2G"
        }
        else if platform.isEqual(to: "AppleTV3,1"){
            return "Apple TV 3"
        }
        else if platform.isEqual(to: "AppleTV3,2"){
            return "Apple TV 3"
        }
        else if platform.isEqual(to: "i386"){
            return "Simulator"
        }
        else if platform.isEqual(to: "x86_64"){
            return "Simulator"
        }
        else
        {
            return ""
        }
    }
    
    //dyanmic pass open
    func openDynamicPass(viaPN strofferid:String)
    {
        print("pass opening when push notifiaction comes.")
        openDynamicPass(strofferid)
    }
    
    // dynamic pass api
    func openDynamicPass(_ offerId:String)
    {
            let defaults = UserDefaults.standard
            if let customerId = defaults.string(forKey: "customerID")
            {
                  print("customer id --------",customerId);
                  print("offer id --------",offerId);
                
                let strData : String = UserDefaults.standard.string(forKey: "access_token")!

                let sessionConfig = URLSessionConfiguration.default
                let session = URLSession(configuration: sessionConfig, delegate: nil, delegateQueue: nil)
                
               // 38663150045
                
                let baseUrl = "\(baseURL)\("/mobile/getPass")"
                var request = URLRequest(url: URL(string: baseUrl)!)
                
                request.setValue(contentType, forHTTPHeaderField: "Content-Type")
                request.setValue(applicationType, forHTTPHeaderField: "Application")
                request.setValue("fishbowl", forHTTPHeaderField: "tenantName")
                request.setValue(TanentID, forHTTPHeaderField: "tenantid")
                
                request.setValue(clientID, forHTTPHeaderField: "client_id")
                request.setValue(ClientSecret, forHTTPHeaderField: "client_secret")
                
                request.setValue(strData, forHTTPHeaderField: "access_token")
                request.httpMethod = "POST"
                request.timeoutInterval = 30
                
                do {
                    
                let params = ["memberid":customerId, "campaignId":offerId, "deviceName":"\(String(describing: deviceType))"] as Dictionary<String, String>
                    
                    request.httpBody = try! JSONSerialization.data(withJSONObject: params, options: [])
                    
                    let task = session.dataTask(with: request, completionHandler: { data, response, error in
                        guard data != nil else {
                            print("no data found: \(String(describing: error))")
                            return
                       }
                        
                    do {
                        if let json = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary {
                            let success = json["success"] as? Int                                  // Okay, the `json` is here, let's get the value for 'success' out of it
                            print("Success: \(String(describing: success))")
                            
                                let obj = clpsdk()
                                
                                if data?.count > 0
                                {
                                    obj.openPassbookAndShowwithData(data)
                                }
                                DispatchQueue.main.async { [unowned self] in
                                    //SVProgressHUD.dismiss()
                                    //[self .removeLoadingView(self.inputView!)]
                                }
                          }
                    }catch let parseError {
                        print(parseError)                                                          // Log the error thrown by `JSONObjectWithData`
                        let jsonStr = NSString(data: data!, encoding: String.Encoding.utf8.rawValue)
                        print("Error could not parse JSON: '\(String(describing: jsonStr))'")
                    }
                    }) 
                    task.resume()
                }
                catch {
                    print(error)
                }
            }
    }
    
   // profile update
//    func updateProfileAction()
//    {
//        NotificationCenter.default.post(name: Notification.Name(rawValue: "pushNotification"), object: nil)
//   
//        print("delegate call push method");
//    }
    
//        else
//        {
//            // presentOkAlert("NO Internet", message: "You seems to be offline. Please check your Internet Connection.")
//            
//        }
//    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
      // NSUserDefaults.standardUserDefaults().setBool(false, forKey: "isPushBackGround")
        
        FBSDKAppEvents.activateApp();
        
       NotificationCenter.default.post(name: Notification.Name(rawValue: "appDidBecomeActive"), object: nil)
        
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    
    // Push notification for register to remote notification
    func application( _ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data)
    {
        if #available(iOS 10.0, *) {
            
            let tokenString = deviceToken.reduce("", {$0 + String(format: "%02X", $1)})
            print("pushToken--------for device id",tokenString);
            
            let defaults=UserDefaults.standard;
            defaults.set(tokenString, forKey: "pushToken");
            defaults.synchronize();
            
        } else {
                    let characterSet: CharacterSet = CharacterSet( charactersIn: "<>" )
            
                    let deviceTokenString: String = ( deviceToken.description as NSString )
                        .trimmingCharacters( in: characterSet )
                        .replacingOccurrences( of: " ", with: "" ) as String
            
            let defaults=UserDefaults.standard;
            defaults.set(deviceTokenString, forKey: "pushToken");
            defaults.synchronize();
        }
        
    }
    
    // Push notification did fail
    func application(_ application: UIApplication,didFailToRegisterForRemoteNotificationsWithError error: Error
        ) {
        //Log an error for debugging purposes, user doesn't need to know
        print("Failed to get token; error: %@", error)
    }
    
    // push notification delegate function
    
    func clpPushDataBinding(_ strOfferTitle: String, withId strOfferId: String, withMessageType strMessageType:String) {
        
        UserDefaults.standard.set(true, forKey: "isPushBackGround")
        
        UserDefaults.standard.setValue(strOfferId, forKey:"offerID")
        UserDefaults.standard.setValue(strOfferTitle, forKey:"promocode")
        UserDefaults.standard.setValue(strMessageType, forKey: "MessageType")

        UserDefaults.standard.synchronize()
        print("pushcall method clpPushDataBinding")
        
    }
    
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void)
    {
        
        print("pushcall method in didrecieve")

          UserDefaults.standard.set(true, forKey: "push")
          UserDefaults.standard.set(true, forKey: "pushPassCome")
        
        var clpsdkobj:clpsdk = clpsdk()
        
       // clpsdkobj = clpsdk()
        
        clpsdkobj = clpsdk.sharedInstanceWithAPIKey();
        clpsdkobj.delegate = self;
        clpsdkobj.processPushMessage(userInfo)
        //[clpsdkobj! .processPushMessage(userInfo)];
        print(userInfo);
    }
    
  
    // facebook delegate
    func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
        let handled = FBSDKApplicationDelegate.sharedInstance().application(application, open: url, sourceApplication: sourceApplication, annotation: annotation)
        // Add any custom logic here.
        return handled
    }
    
    // push notification ios 10 method
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        
        print("User Info === \(notification.request.content.userInfo)")
        // Handle code here.
        completionHandler([UNNotificationPresentationOptions.sound , UNNotificationPresentationOptions.alert , UNNotificationPresentationOptions.badge])
    }
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        print("User Info === \(response.notification.request.content.userInfo)")
        
        print("pushcall method in didrecieve")
        
        UserDefaults.standard.set(true, forKey: "push")
        UserDefaults.standard.set(true, forKey: "pushPassCome")
        
        var clpsdkobj:clpsdk = clpsdk()
        clpsdkobj = clpsdk.sharedInstanceWithAPIKey();
        clpsdkobj.delegate = self;
        clpsdkobj.processPushMessage(response.notification.request.content.userInfo)
        //[clpsdkobj! .processPushMessage(userInfo)];
        print(response.notification.request.content.userInfo);
        completionHandler()
    }
}

