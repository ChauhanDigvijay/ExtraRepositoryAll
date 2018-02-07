//
//  GuestWithPhoneViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/17/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol GuestWithPhoneViewControllerDelegate {
    func authenticateUser()
}

class GuestWithPhoneViewController: UIViewController {

    var delegate: GuestWithPhoneViewControllerDelegate?


    // MARK: User actions

    @IBAction func authenticateUser(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        delegate?.authenticateUser()
    }

}
