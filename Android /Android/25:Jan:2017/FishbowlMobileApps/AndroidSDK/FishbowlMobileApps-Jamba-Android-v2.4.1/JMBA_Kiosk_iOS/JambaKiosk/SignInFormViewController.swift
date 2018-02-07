//
//  SignInFormViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/17/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol SignInFormViewControllerDelegate {
    func authenticateUser(username: String, password: String)
}

class SignInFormViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet var usernameTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!

    var delegate: SignInFormViewControllerDelegate?

    override func viewDidLoad() {
        super.viewDidLoad()
        usernameTextField.becomeFirstResponder()
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


    // MARK: - Navigation

    @IBAction func cancel(sender: AnyObject) {
        trackButtonPressWithName("Cancel")
        SessionExpirationService.sharedInstance.trackUserActivity()
        dismissModalController()
    }

    @IBAction func authenticateUser(sender: UIButton) {
        trackButtonPress(sender)
        authenticateUser()
    }

    private func authenticateUser() {
        SessionExpirationService.sharedInstance.trackUserActivity()

        // Validate username
        guard let username = usernameTextField.text?.trim() else {
            presentOkAlert("Field required", message: "Please enter a valid username") {
                self.usernameTextField.becomeFirstResponder()
            }
            return
        }
        if username.isEmpty {
            presentOkAlert("Field required", message: "Please enter a valid username") {
                self.usernameTextField.becomeFirstResponder()
            }
            return
        }

        // Validate password
        guard let password = passwordTextField.text?.trim() else {
            presentOkAlert("Field required", message: "Please enter a valid password") {
                self.passwordTextField.becomeFirstResponder()
            }
            return
        }
        if password.isEmpty {
            presentOkAlert("Field required", message: "Please enter a valid password") {
                self.passwordTextField.becomeFirstResponder()
            }
            return
        }

        delegate?.authenticateUser(username.trim(), password: password)
        dismissModalController()
    }


    // MARK: UITextFieldDelegate

    func textFieldShouldReturn(textField: UITextField) -> Bool {
        if textField == usernameTextField {
            passwordTextField.becomeFirstResponder()
        } else if textField == passwordTextField {
            trackKeyboardReturn()
            authenticateUser()
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
