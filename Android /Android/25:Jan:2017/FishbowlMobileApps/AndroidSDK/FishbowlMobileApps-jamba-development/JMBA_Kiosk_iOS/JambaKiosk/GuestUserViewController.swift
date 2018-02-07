//
//  GuestUserViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/17/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol GuestUserViewControllerDelegate {
    func authenticateUser()
}

class GuestUserViewController: UIViewController {

    var delegate: GuestUserViewControllerDelegate?


    // MARK: User actions

//    @IBAction func enterPhoneNumber(sender: UIButton) {
//        trackButtonPress(sender)
//        SessionExpirationService.sharedInstance.trackUserActivity()
//        delegate?.enterPhoneNumber()
//    }

    @IBAction func authenticateUser(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        delegate?.authenticateUser()
    }

}
