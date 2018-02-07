//
//  ChangeEmailAdrressViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 19/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class ChangeEmailAddressViewController: UIViewController {
    
    @IBOutlet weak var greenCopy: UILabel!
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var emailAddressTextField: UITextField!
    @IBOutlet weak var changeEmailAddressButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        emailAddressTextField.becomeFirstResponder()
        
        if UIScreen.main.is35inch() {
            greenCopy.removeFromSuperview()
            redButtonHeightConstraint.constant = 0
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    
    // MARK: User Actions
    
    @IBAction func changeEmailAddress(_ sender: UIButton) {
        trackButtonPress(sender)
        changeEmailAddress()
    }
    
    fileprivate func changeEmailAddress() {
        //Resign First Responder
        view.endEditing(true)

        guard let emailAddress = emailAddressTextField.text else {
            presentOkAlert("Email Required", message: "Please enter an email address") {
                self.emailAddressTextField.becomeFirstResponder()
            }
            return
        }
        
        if !hasEmailChanged() {
            presentOkAlert("Update Email", message: "Please enter a different email than the current one in order to update it.")
            return
        }
        
        // Validate email not empty
        if emailAddress.trim().isEmpty {
            presentOkAlert("Email Required", message: "Please enter an email address") {
                self.emailAddressTextField.becomeFirstResponder()
            }
            return
        }
        
        // Validate email address
        if !emailAddress.isEmail() {
            presentOkAlert("Invalid email", message: "Please enter a valid email address") {
                self.emailAddressTextField.becomeFirstResponder()
            }
            return
        }
        
        SVProgressHUD.show(withStatus: "Updating email...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.lookupEmailAddress(emailAddress) { (error) -> Void in
            if error != nil {
                SVProgressHUD.dismiss()
                self.presentError(error)
                return
            }
            UserService.updateUserEmailAddress(emailAddress, emailOptIn: UserService.sharedUser!.emailOptIn) { error in
                SVProgressHUD.dismiss()
                if error != nil {
                    self.presentError(error)
                    return
                }
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.UserEmailAddressChanged.rawValue), object: self)
                self.changeEmailAddressButton.isHidden = true
                self.performSegue(withIdentifier: "EmailAddressUpdated", sender: self)
            }
        }
    }
    
    fileprivate func hasEmailChanged() -> Bool {
        return emailAddressTextField.text != (UserService.sharedUser!.emailAddress ?? "")
    }
    
    
    //MARK: TextFieldDelegate
    
    func textField(_ textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoEmailAddressLimit)
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        changeEmailAddress()
        return true
    }
    
}
