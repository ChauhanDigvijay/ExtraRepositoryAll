//
//  EmailReceipt.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/19/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK

protocol EmailReceiptViewControllerDelegate {
    func userEnteredReceiptEmailAddress(emailAddress: String?)
}

class EmailReceiptViewController: UIViewController {

    @IBOutlet var emailAddressTextField: UITextField!

    var delegate: EmailReceiptViewControllerDelegate?

    override func viewDidLoad() {
        super.viewDidLoad()
        emailAddressTextField.becomeFirstResponder()
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpirationWarning:", name: SessionExpirationService.sessionAboutToExpireNotificatioName, object: nil)
    }

    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }


    // MARK: User actions

    @IBAction func noReceipt(sender: UIButton) {
        trackButtonPress(sender)
        delegate?.userEnteredReceiptEmailAddress(nil)
        SessionExpirationService.sharedInstance.trackUserActivity()
    }

    @IBAction func sendReceipt(sender: UIButton) {
        trackButtonPress(sender)
        sendReceipt()
    }

    private func sendReceipt() {
        SessionExpirationService.sharedInstance.trackUserActivity()
        guard let emailAddress = emailAddressTextField.text?.trim() else {
            presentOkAlert("Error", message: "Please enter a valid email adddress")
            return
        }
        if emailAddress.isEmail() == false {
            presentOkAlert("Error", message: "Please enter a valid email adddress")
            return
        }
        delegate?.userEnteredReceiptEmailAddress(emailAddress)
    }


    // MARK: UITextFieldDelegate

    func textFieldShouldReturn(textField: UITextField) -> Bool {
        if textField == emailAddressTextField {
            trackKeyboardReturn()
            sendReceipt()
        }
        return true
    }

    @IBAction func textFieldDidChange(textField: UITextField) {
        SessionExpirationService.sharedInstance.trackUserActivity()
    }


    // MARK: Notifications

    func sessionExpirationWarning(notification: NSNotification) {
        performSegueWithIdentifier("SessionExpirationSegue", sender: self)
    }

}
