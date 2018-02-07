//
//  SignUpConfirmationViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/15/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class SignUpConfirmationViewController: UIViewController {

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        AnalyticsService.trackEvent("user_account", action: "signup_complete")
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("SIGN_UP_COMPLETE")
    }
   
    @IBAction func closeToHomeScreen() {
       // HomeViewController.sharedInstance().dismissViewControllerAnimated(false, completion: nil)
       // HomeViewController.sharedInstance().openLoginScreen()
//        NonSignedInViewController.sharedInstance().dismissViewControllerAnimated(false, completion:nil)
        NonSignedInViewController.sharedInstance().openLoginScreen()
    }

}
