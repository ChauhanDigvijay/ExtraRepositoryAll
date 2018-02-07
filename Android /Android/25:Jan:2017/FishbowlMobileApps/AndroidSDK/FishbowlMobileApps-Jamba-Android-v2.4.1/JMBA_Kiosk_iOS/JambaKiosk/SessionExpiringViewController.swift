//
//  SessionExpiringViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/16/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class SessionExpiringViewController: UIViewController {

    @IBOutlet var sessionExpiringLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        updateSessionExpiringLabel(SessionExpirationService.sharedInstance.secondsLeft)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpirationUpdate:", name: SessionExpirationService.secondsLeftNotificatioName, object: nil)
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
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

    func sessionExpirationUpdate(notification: NSNotification) {
        let secondsLeft: Int = notification.userInfo?[SessionExpirationService.secondsLeftKey] as? Int ?? 0
        updateSessionExpiringLabel(secondsLeft)
    }

    private func updateSessionExpiringLabel(secondsLeft: Int) {
        sessionExpiringLabel.text = "Your session will expire\nin \(secondsLeft) seconds..."
    }

}
