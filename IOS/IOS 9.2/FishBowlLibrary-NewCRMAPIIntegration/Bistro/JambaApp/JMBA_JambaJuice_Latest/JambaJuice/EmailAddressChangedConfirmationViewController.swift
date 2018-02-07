//
//  EmailAddressChangedConfirmationViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 09/07/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class EmailAddressChangedConfirmationViewController: UIViewController {
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func close() {
        //Presented in context of Nav VC
        (self.presentingViewController as? UINavigationController)?.popViewControllerAnimated(false)
        self.dismissModalController()
    }
    
}
