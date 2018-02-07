//
//  ForgotPasswordConfirmationViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class ForgotPasswordConfirmationViewController: UIViewController {
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func closeToHomeScreen() {
        NonSignedInViewController.sharedInstance().openLoginScreen();
    }
    
}
