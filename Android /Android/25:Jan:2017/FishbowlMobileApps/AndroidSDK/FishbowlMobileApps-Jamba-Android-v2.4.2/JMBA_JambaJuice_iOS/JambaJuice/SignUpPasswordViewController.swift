//
//  SignUpPasswordViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 5/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import SVProgressHUD

class SignUpPasswordViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var dotsPageControl: UIPageControl!
    @IBOutlet weak var greenCopyLabel: UILabel!
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    @IBOutlet weak var passwordValidationImageView: UIImageView!
    @IBOutlet weak var createAccountButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        passwordValidationImageView.isHidden = true
        passwordTextField.becomeFirstResponder()
        
        if UIScreen.main.is35inch() {
            dotsPageControl.removeFromSuperview()
            greenCopyLabel.removeFromSuperview()
            redButtonHeightConstraint.constant = 0
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    @IBAction func createAccount(_ sender: UIButton) {
        trackButtonPress(sender)
        createAccount()
    }
    
    fileprivate func createAccount() {
        //Resign First Responder
        view.endEditing(true)
        
        // Validate password
        if passwordTextField.text == nil || passwordTextField.text!.trim().isEmpty {
            presentOkAlert("Password required", message: "Please enter a password") {
                self.passwordTextField.becomeFirstResponder()
            }
            return
        }
        
        //Check length
        if passwordTextField.text!.length < Constants.spendGoPasswordMinLength {
            presentOkAlert("Password too short", message: "Password should be at least \(Constants.spendGoPasswordMinLength) characters long.") {
                self.passwordTextField.becomeFirstResponder()
            }
            return
        }
        
        // Validate confirmation
        if passwordTextField.text != confirmPasswordTextField.text {
            presentOkAlert("Passwords do not match", message: "Please ensure the password and confirmation are the same") {
                self.confirmPasswordTextField.becomeFirstResponder()
            }
            return
        }
        SVProgressHUD.show(withStatus: "Creating account...")
        SVProgressHUD.setDefaultMaskType(.clear)
        
        UserService.signUpUserInfo?.password = passwordTextField.text!
        log.verbose("\(String(describing: UserService.signUpUserInfo))")
        UserService.signUpUser(UserService.signUpUserInfo!) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            UserService.signUpUserInfo = nil

            self.createAccountButton.isHidden = true
            self.performSegue(withIdentifier: "SignUpConfirmationSegue", sender: self)
        }
    }
    
    @IBAction func textFieldEditingChanged(_ sender: AnyObject) {
        //Since this is going to be a tick image. We want it hidden in case of password mismatch or if both fields are empty.
        passwordValidationImageView.isHidden = passwordTextField.text!.isEmpty || passwordTextField.text != confirmPasswordTextField.text
    }
    
    
    //MARK: TextFieldDelegate

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        if textField == passwordTextField {
            confirmPasswordTextField.becomeFirstResponder()
        } else {
            createAccount()
        }
        return true
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoPasswordMaxLength)
    }

}
