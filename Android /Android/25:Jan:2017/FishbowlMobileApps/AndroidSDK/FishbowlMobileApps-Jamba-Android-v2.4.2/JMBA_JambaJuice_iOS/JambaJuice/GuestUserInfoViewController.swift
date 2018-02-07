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
    fileprivate weak var guestUserInfoTVC: GuestUserInfoTableViewController!
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if identifier == "EnterCreditCard" {
            return guestUserInfoTVC.tryValidatingAndPopulateData()
        }
        return true
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "embeddedTVC" {
            guestUserInfoTVC = segue.destination as! GuestUserInfoTableViewController
        }
    }
    
    func continueToNextStep() {
        if guestUserInfoTVC.tryValidatingAndPopulateData() {
            performSegue(withIdentifier: "EnterCreditCard", sender: self)
        }
    }

}
