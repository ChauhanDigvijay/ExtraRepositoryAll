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

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        
        UIApplication.afterDelay(2) {
            self.next()
        }
    }
    
    private func next() {
        if let _ = SettingsManager.setting(.UserOnboardedNationwide) as? Bool {
            
            // Session check for logged in user
            if(UserService.sharedUser==nil){
                performSegueWithIdentifier("NonSignedInMainVC", sender: self)
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
                performSegueWithIdentifier("SignedInMainVC", sender: self)
            }
        } else {
            performSegueWithIdentifier("ShowOnboardingScreen", sender: self)
            SettingsManager.setSetting(.UserOnboardedNationwide, value: true)
        }
    }
}
