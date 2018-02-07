//
//  CancelOrderViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 12/1/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class CancelOrderViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpirationWarning:", name: SessionExpirationService.sessionAboutToExpireNotificatioName, object: nil)
    }


    // MARK: User actions

    @IBAction func continueOrder(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        dismissModalController()
    }

    @IBAction func cancelOrder(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.terminateKioskSession()
    }

    // MARK: Notifications

    func sessionExpirationWarning(notification: NSNotification) {
        performSegueWithIdentifier("SessionExpirationSegue", sender: self)
    }
    
}
