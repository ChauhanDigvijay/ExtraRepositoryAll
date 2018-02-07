//
//  SignUpNameAndEmailAddrViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 5/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import SVProgressHUD

class SignUpNameAndEmailAddrViewController: UIViewController, UITextFieldDelegate, UIScrollViewDelegate {
    
    @IBOutlet weak var dotsPageControl: UIPageControl!
    @IBOutlet weak var greenCopyLabel: UILabel?
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var firstNameTextField: UITextField!
    @IBOutlet weak var lastNameTextField: UITextField!
    @IBOutlet weak var emailAddressTextField: UITextField!
    @IBOutlet weak var personalizedUpdatesSwitch: UISwitch!
    @IBOutlet weak var continueButton: UIButton!
    @IBOutlet weak var scrollView: UIScrollView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        firstNameTextField.becomeFirstResponder()
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)

        if UIScreen.mainScreen().is35inch() {
            dotsPageControl.removeFromSuperview()
            greenCopyLabel?.removeFromSuperview()
            greenCopyLabel = nil
            redButtonHeightConstraint.constant = 0
        }
        else if UIScreen.mainScreen().is4inch() {
            greenCopyLabel?.removeFromSuperview()
            greenCopyLabel = nil
        }
        else {
            greenCopyLabel?.text = "Let's create your profile.\nWhat is your first name?"
        }
        
        firstNameTextField.text = UserService.signUpUserInfo?.firstName
        lastNameTextField.text = UserService.signUpUserInfo?.lastName
        emailAddressTextField.text = UserService.signUpUserInfo?.emailAddress
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    private func validateFields() -> Bool {
        // Validate first name
        if firstNameTextField.text == nil || firstNameTextField.text!.trim().isEmpty {
            presentOkAlert("First name required", message: "Please enter your first name") {
                self.firstNameTextField.becomeFirstResponder()
            }
            return false
        }
        
        // Validate last name
        if lastNameTextField.text == nil || lastNameTextField.text!.trim().isEmpty {
            presentOkAlert("Last name required", message: "Please enter your last name") {
                self.lastNameTextField.becomeFirstResponder()
            }
            return false
        }

        // Validate email
        if emailAddressTextField.text == nil || emailAddressTextField.text!.trim().isEmpty {
            presentOkAlert("Email required", message: "Please enter an email address") {
                self.emailAddressTextField.becomeFirstResponder()
            }
            return false
        }

        if emailAddressTextField.text!.isEmail() == false {
            presentOkAlert("Invalid email", message: "Please enter a valid email address") {
                self.emailAddressTextField.becomeFirstResponder()
            }
            return false
        }

        // Save fields
        UserService.signUpUserInfo?.firstName = firstNameTextField.text!.trim()
        UserService.signUpUserInfo?.lastName = lastNameTextField.text!.trim()
        UserService.signUpUserInfo?.emailAddress = emailAddressTextField.text!.trim()
        UserService.signUpUserInfo?.enrollForEmailUpdates = personalizedUpdatesSwitch.on
        log.verbose("\(UserService.signUpUserInfo)")
        
        return true
    }
    
    //MARK: TextFieldDelegate
    
    func textFieldDidBeginEditing(textField: UITextField) {
        //Flash indicator after delay, so the scrollview has already scrolled text field to visible.
        NSTimer.scheduledTimerWithTimeInterval(0.5, target: scrollView, selector: #selector(UIScrollView.flashScrollIndicators), userInfo: nil, repeats: false)

        if textField == firstNameTextField {
            greenCopyLabel?.text = "Let's create your profile.\nWhat is your first name?"
        }
        else if textField == lastNameTextField {
            greenCopyLabel?.text = "Let's create your profile.\nWhat is your last name?"
        }
        else if textField == emailAddressTextField {
            greenCopyLabel?.text = "Let's create your profile.\nWhat is your email?"
        }
    }
    
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        if textField == firstNameTextField {
            return firstNameTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoFirstNameLimit)
        }
        else if textField == lastNameTextField {
            return lastNameTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoLastNameLimit)
        }
        else if textField == emailAddressTextField {
            return emailAddressTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoEmailAddressLimit)
        }
        return true
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        trackKeyboardReturn()
        if textField == firstNameTextField {
            lastNameTextField.becomeFirstResponder()
        }
        else if textField == lastNameTextField {
            emailAddressTextField.becomeFirstResponder()
        }
        else if textField == emailAddressTextField {
            continueSignUp()
        }
        return true
    }
    
    //MARK: --
    
    @IBAction func validateEmailAddressAndContinue(sender: UIButton) {
        trackButtonPress(sender)
        continueSignUp()
    }
    
    private func continueSignUp() {
        view.endEditing(true)
        if !validateFields() {
            return
        }
        
        // Per legal requirements, confirm for opt-in twice
        if personalizedUpdatesSwitch.on {
            presentConfirmation("Personalized Offers", message: "I agree to receive personalized offers by email.", buttonTitle: "Agree", callback: { (confirmed) -> Void in
                if confirmed {
                    self.nextStep()
                }
            })
            return
        }
        
        nextStep()
    }

    private func nextStep() {
        SVProgressHUD.showWithMaskType(.Clear) // No message
        continueButton.enabled = false
        UserService.lookupEmailAddress(UserService.signUpUserInfo!.emailAddress!) { (error) -> Void in
            SVProgressHUD.dismiss()
            self.continueButton.enabled = true
            if error != nil {
                if error!.code == 403 {
                    // Special case, give user option to jump to log in screen
                    self.presentConfirmation("Existing Account", message: error!.localizedDescription, buttonTitle: "Log In") { (confirmed) -> Void in
                        if confirmed {
                         //   HomeViewController.sharedInstance().openLoginScreen()
                            NonSignedInViewController.sharedInstance().openLoginScreen()
                        }
                        return
                    }
                    return
                }
                self.presentError(error)
                return
            }
            self.performSegueWithIdentifier("SignUpNameAndEmailDone", sender: self)
        }
    }
    
}
