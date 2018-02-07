//
//  SignUpConfirmationViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/15/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class SignUpConfirmationViewController: UIViewController {

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        AnalyticsService.trackEvent("user_account", action: "signup_complete")
    }
   
    @IBAction func closeToHomeScreen() {
       // HomeViewController.sharedInstance().dismissViewControllerAnimated(false, completion: nil)
       // HomeViewController.sharedInstance().openLoginScreen()
//        NonSignedInViewController.sharedInstance().dismissViewControllerAnimated(false, completion:nil)
        NonSignedInViewController.sharedInstance().openLoginScreen()
    }

}
