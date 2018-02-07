//
//  SignUpStepsViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/15/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class SignUpStepsViewController: UIViewController {
    
    @IBOutlet weak var termsLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.lightBlue)
        AnalyticsService.trackEvent("user_account", action: "signup_start")
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("SIGN_UP_START")
        if UserService.signUpUserInfo == nil {
            UserService.signUpUserInfo = SignUpUserInfo()
        }
        
        termsLabel.attributedText = NSAttributedString(string: termsLabel.text!, attributes: [NSUnderlineStyleAttributeName: NSUnderlineStyle.styleSingle.rawValue])
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func openPrivacyAndTerms() {
        trackButtonPressWithName("Terms and Conditions")
        performSegue(withIdentifier: "SignUpTermsAndConditions", sender: self)
    }
    
}

