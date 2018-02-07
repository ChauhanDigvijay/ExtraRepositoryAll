//
//  OrderConfirmationViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/19/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class OrderConfirmationViewController: UIViewController {

    @IBOutlet var messageLabel: UILabel!
    var emailReceiptSent: Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()
        if emailReceiptSent {
            messageLabel.text = "We'll call your name at the\npickup counter when it's ready.\n\nYour receipt has been\nemailed to you."
        } else {
            messageLabel.text = "We'll call your name at the\npickup counter when it's ready."
        }
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    @IBAction func close(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.terminateKioskSession()
    }

}
