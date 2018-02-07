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

        if UIScreen.main.is35inch() {
            dotsPageControl.removeFromSuperview()
            greenCopyLabel?.removeFromSuperview()
            greenCopyLabel = nil
            redButtonHeightConstraint.constant = 0
        }
        else if UIScreen.main.is4inch() {
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
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    fileprivate func validateFields() -> Bool {
        // Validate first name
        if firstNameTextField.text == nil || firstNameTextField.text!.trim().isEmpty {
            presentOkAlert("First name required", message: "Please enter your first name") {
                self.firstNameTextField.becomeFirstResponder()
            }
            return false
        }else if firstNameTextField.text!.length < 3{
            presentOkAlert("Invalid First Name", message: "First name must be more than 3 characters") {
                self.firstNameTextField.becomeFirstResponder()
            }
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
        UserService.signUpUserInfo?.enrollForEmailUpdates = personalizedUpdatesSwitch.isOn
        log.verbose("\(String(describing: UserService.signUpUserInfo))")
        
        return true
    }
    
    //MARK: TextFieldDelegate
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        //Flash indicator after delay, so the scrollview has already scrolled text field to visible.
        Timer.scheduledTimer(timeInterval: 0.5, target: scrollView, selector: #selector(UIScrollView.flashScrollIndicators), userInfo: nil, repeats: false)

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
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == firstNameTextField {
            if firstNameTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoFirstNameLimit){
                if range.location <= 2 && string == " "{ // First 3 places reserved for character should not allow space.
                    return false
                }else if string != ""{ // It's for character
                    return string.isValidCharacter()
                }else {
                    return true // It's for backspace
                }
            }else{
                return false
            }
        }
        else if textField == lastNameTextField {
            if lastNameTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoLastNameLimit){
                if range.location == 0 && string == " "{ // First place reserved for character should not allow space.
                    return false
                }else if string != ""{ // It's for character
                    return string.isValidCharacter()
                }else {
                    return true // It's for backspace
                }
            }else{
                return false
            }
        }
        else if textField == emailAddressTextField {
            return emailAddressTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoEmailAddressLimit)
        }
        return true
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
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
    
    @IBAction func validateEmailAddressAndContinue(_ sender: UIButton) {
        trackButtonPress(sender)
        continueSignUp()
    }
    
    fileprivate func continueSignUp() {
        view.endEditing(true)
        if !validateFields() {
            return
        }
        
        // Per legal requirements, confirm for opt-in twice
        if personalizedUpdatesSwitch.isOn {
            presentConfirmation("Personalized Offers", message: "I agree to receive personalized offers by email.", buttonTitle: "Agree", callback: { (confirmed) -> Void in
                if confirmed {
                    self.nextStep()
                }else{
                    self.personalizedUpdatesSwitch.setOn(false, animated: true)
                    self.nextStep()
                }
            })
            return
        }
        
        nextStep()
    }

    fileprivate func nextStep() {
        SVProgressHUD.show(withStatus: "")
        SVProgressHUD.setDefaultMaskType(.clear)
        continueButton.isEnabled = false
        UserService.lookupEmailAddress(UserService.signUpUserInfo!.emailAddress!) { (error) -> Void in
            SVProgressHUD.dismiss()
            self.continueButton.isEnabled = true
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
            self.performSegue(withIdentifier: "SignUpNameAndEmailDone", sender: self)
        }
    }
    
}
// Extension for check whether given character is alphabet or not.
extension String{
    func isValidCharacter() -> Bool{
        let regexTest = NSPredicate(format: "SELF MATCHES %@", "^[a-zA-Z ]")
        return regexTest.evaluate(with: self)
    }
}
