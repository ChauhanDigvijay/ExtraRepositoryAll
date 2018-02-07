//
//  GuestUserInfoViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 08/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class GuestUserInfoViewController: UIViewController {
    
    @IBOutlet weak var continueButton: UIButton!
    private weak var guestUserInfoTVC: GuestUserInfoTableViewController!
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func shouldPerformSegueWithIdentifier(identifier: String, sender: AnyObject?) -> Bool {
        if identifier == "EnterCreditCard" {
            return guestUserInfoTVC.tryValidatingAndPopulateData()
        }
        return true
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "embeddedTVC" {
            guestUserInfoTVC = segue.destinationViewController as! GuestUserInfoTableViewController
        }
    }
    
    func continueToNextStep() {
        if guestUserInfoTVC.tryValidatingAndPopulateData() {
            performSegueWithIdentifier("EnterCreditCard", sender: self)
        }
    }

}
