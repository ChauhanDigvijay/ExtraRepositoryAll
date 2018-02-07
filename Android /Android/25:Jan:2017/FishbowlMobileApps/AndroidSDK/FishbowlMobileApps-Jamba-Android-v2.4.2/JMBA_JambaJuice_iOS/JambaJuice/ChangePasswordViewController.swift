//
//  ChangePasswordViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 25/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import HDK

class ChangePasswordViewController: UIViewController {
    
    @IBOutlet weak var greenCopy: UILabel!
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var newPasswordTextField: UITextField!
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    @IBOutlet weak var passwordValidationImageView: UIImageView!
    @IBOutlet weak var changePasswordButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()        
        passwordValidationImageView.isHidden = true
        newPasswordTextField.becomeFirstResponder()
        
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
    
    @IBAction func changePassword(_ sender: UIButton) {
        trackButtonPress(sender)
        changePassword()
    }
    
    fileprivate func changePassword() {
        //Resign First Responder
        view.endEditing(true)

        guard let newPassword = newPasswordTextField.text else {
            presentOkAlert("Password Required", message: "Please enter a new password") {
                self.newPasswordTextField.becomeFirstResponder()
            }
            return
        }
        
        // Validate new password
        if newPassword.trim().isEmpty {
            presentOkAlert("Password Required", message: "Please enter a new password") {
                self.newPasswordTextField.becomeFirstResponder()
            }
            return
        }

        // Validate confirmation
        if newPassword != confirmPasswordTextField.text {
            presentOkAlert("Password Mismatch", message: "Please ensure that new password and confirmation are the same") {
                self.confirmPasswordTextField.becomeFirstResponder()
            }
            return
        }
        
        //Check length
        if newPassword.length < Constants.spendGoPasswordMinLength {
            presentOkAlert("Password Too Short", message: "Password should be at least \(Constants.spendGoPasswordMinLength) characters long.") {
                self.newPasswordTextField.becomeFirstResponder()
            }
            return
        }

        SVProgressHUD.show(withStatus: "Updating password...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.changePassword(newPassword) { (error) -> Void in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }

            self.presentOkAlert("Password Updated", message: "Your password was successfully updated.") {
                self.dismissModalController()
            }
        }
    }
    
    @IBAction func textFieldEditingChanged(_ sender: AnyObject) {
        //Since this is going to be a tick image. We want it hidden in case of password mismatch or if both fields are empty.
        passwordValidationImageView.isHidden = newPasswordTextField.text == nil || newPasswordTextField.text!.isEmpty || newPasswordTextField.text != confirmPasswordTextField.text
    }
    
    
    //MARK: TextFieldDelegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        if textField == newPasswordTextField {
            confirmPasswordTextField.becomeFirstResponder()
        }
        else {
            changePassword()
        }
        return true
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoPasswordMaxLength)
    }
    
}
