//
//  SignUpPhoneNumberViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 5/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import SVProgressHUD

class SignUpPhoneNumberViewController: UIViewController {
    
    @IBOutlet weak var dotsPageControl: UIPageControl!
    @IBOutlet weak var greenCopyLabel: UILabel!
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var phoneNumberTextField: UITextField!
    @IBOutlet weak var personalizedUpdatesSwitch: UISwitch!
    @IBOutlet weak var continueButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        phoneNumberTextField.becomeFirstResponder()
        
        if UIScreen.main.is35inch() {
            dotsPageControl.removeFromSuperview()
            greenCopyLabel.removeFromSuperview()
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    fileprivate func validateFields() -> Bool {
        view.endEditing(true)
        
        guard let phoneNumber = phoneNumberTextField.text else {
            presentOkAlert("Phone number required", message: "Please enter your phone number") {
                self.phoneNumberTextField.becomeFirstResponder()
            }
            return false
        }
        
        // Validate phone number
        if phoneNumber.trim().isEmpty {
            presentOkAlert("Phone number required", message: "Please enter your phone number") {
                self.phoneNumberTextField.becomeFirstResponder()
            }
            return false
        }
        
        if !phoneNumber.isTenDigitPhone() {
            presentOkAlert("Invalid phone number", message: "Please enter a valid 10 digit phone number") {
                self.phoneNumberTextField.becomeFirstResponder()
            }
            return false
        }
        return true
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        return tryAndPrepareForContinuing()
    }
    
    func tryAndPrepareForContinuing() -> Bool {
        if !validateFields() {
            return false
        }
        UserService.signUpUserInfo?.phoneNumber = phoneNumberTextField.text!.stringByRemovingNonNumericCharacters()
        UserService.signUpUserInfo?.enrollForTextUpdates = personalizedUpdatesSwitch.isOn
        log.verbose("\(String(describing: UserService.signUpUserInfo))")
        return true
    }
    
    @IBAction func validatePhoneNumberAndContinue(_ sender: UIButton) {
        trackButtonPress(sender)
        continueSignUp()
    }
    
    fileprivate func continueSignUp() {
        if tryAndPrepareForContinuing() == false {
            return
        }
        
        // Per legal requirements, confirm for opt-in twice
        if personalizedUpdatesSwitch.isOn {
            presentConfirmation("Personalized Offers", message: "I agree to receive personalized offers by text message. Standard carrier charges may apply.", buttonTitle: "Agree", callback: { (confirmed) -> Void in
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
        SVProgressHUD.show(withStatus: "") // No message
        SVProgressHUD.setDefaultMaskType(.clear)
        continueButton.isEnabled = false
        UserService.lookupPhoneNumber(UserService.signUpUserInfo!.phoneNumber!) { (error) -> Void in
            SVProgressHUD.dismiss()
            self.continueButton.isEnabled = true
            if error != nil {
                if error!.code == 403 {
                    // Special case, give user option to jump to log in screen
                    self.presentConfirmation("Existing Account", message: error!.localizedDescription, buttonTitle: "Log In") { (confirmed) -> Void in
                        if confirmed {
                            NonSignedInViewController.sharedInstance().openLoginScreen();
                        }
                        return
                    }
                    return
                }
                self.presentError(error)
                return
            }
            self.performSegue(withIdentifier: "SignupPushConfirmation", sender: self)
        }
    }
    
    //MARK: TextFieldDelegate
    
    func textField(_ textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.phoneNumberInputLimit)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        textField.resignFirstResponder()
        continueSignUp()
        return true
    }
}
