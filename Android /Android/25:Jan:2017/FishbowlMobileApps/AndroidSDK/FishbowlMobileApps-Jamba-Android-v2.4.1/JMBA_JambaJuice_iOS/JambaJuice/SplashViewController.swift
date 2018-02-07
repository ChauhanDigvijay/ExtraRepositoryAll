//
//  SplashViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/7/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Alamofire
import HDK
import Spring
import SVProgressHUD

class SplashViewController: UIViewController {
    
    
    override func viewDidLoad() {
        trackScreenView()
        // Initialize user session
        self.next()
    }
    
    func next() {
        if let _ = SettingsManager.setting(.UserOnboardedNationwide) as? Bool {
            // Check first time launch event
            // Session check for logged in user
            UserService.loadSession {
                // Event track
                if(UserService.sharedUser==nil){
                    UIApplication.afterDelay(1){
                        self.performSegue(withIdentifier: "NonSignedInMainVC", sender: self)
                    }
                    if SettingsManager.setting(.FirstTimeLaunch) == nil {
                        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("FIRST_TIME_LAUNCH")
                        SettingsManager.setSetting(.FirstTimeLaunch, value: Date() as AnyObject)
                    }else{
                       FishbowlApiClassService.sharedInstance.submitMobileAppEvent("APP_OPEN")
                    }
                }
                else{
                  
            
                    // Retrieve current store from user session and assign in current store shared instancee
                    if let userCurrentStore = UserService.sharedUser!.currentStore{
                        if userCurrentStore.supportsOrderAhead == true{
                            CurrentStoreService.sharedInstance.resetStore(userCurrentStore)
                        }
                    }
                        // Retrieve favourite store from user sessio and assign in current store shared instance
                    else if let userFavoriteStore = UserService.sharedUser!.favoriteStore {
                        if userFavoriteStore.supportsOrderAhead == true{
                            CurrentStoreService.sharedInstance.resetStore((userFavoriteStore))
                        }
                    }
                    UIApplication.afterDelay(1){
                        self.performSegue(withIdentifier: "SignedInMainVC", sender: self)
                    }
                }
            }
        } else {
            UIApplication.afterDelay(1){
                self.performSegue(withIdentifier: "ShowOnboardingScreen", sender: self)
                SettingsManager.setSetting(.UserOnboardedNationwide, value: true as AnyObject)
            }
        }
    }
}
