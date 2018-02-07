//
//  ChangePhoneNumberViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 09/07/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class ChangePhoneNumberViewController: UIViewController {

    @IBOutlet weak var greenCopy: UILabel!
    
    @IBOutlet weak var phoneNumberTextField: UITextField!
    @IBOutlet weak var updatePhoneNumberButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        phoneNumberTextField.becomeFirstResponder()

        if UIScreen.mainScreen().is35inch() {
            greenCopy.removeFromSuperview()
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    
    // MARK: User Actions
    
    @IBAction func changePhoneNumber(sender: UIButton) {
        trackButtonPress(sender)
        changePhoneNumber()
    }
    
    private func changePhoneNumber() {
        //Resign First Responder
        view.endEditing(true)
        
        if !hasPhoneNumberChanged() {
            presentOkAlert("Update Phone Number", message: "Please enter a different phone number than the current one in order to update it.")
            return
        }
        
        guard let phoneNumber = phoneNumberTextField.text else {
            presentOkAlert("New phone number required", message: "Please enter your new phone number") {
                self.phoneNumberTextField.becomeFirstResponder()
            }
            return
        }
        
        // Validate phone number
        if phoneNumber.trim().isEmpty {
            presentOkAlert("New phone number required", message: "Please enter your new phone number") {
                self.phoneNumberTextField.becomeFirstResponder()
            }
            return
        }
        
        if !phoneNumber.isTenDigitPhone() {
            presentOkAlert("Invalid phone number", message: "Please enter a valid 10 digit phone number") {
                self.phoneNumberTextField.becomeFirstResponder()
            }
            return
        }
        
        SVProgressHUD.showWithStatus("Updating phone number...", maskType: .Clear)
        UserService.lookupPhoneNumber(phoneNumber) { (error) -> Void in
            if error != nil {
                SVProgressHUD.dismiss()
                self.presentError(error)
                return
            }
            let phoneNumber = self.phoneNumberTextField.text!.stringByRemovingNonNumericCharacters()
            UserService.updateUserPhoneNumber(phoneNumber, smsOptIn: UserService.sharedUser!.smsOptIn) { error in
                SVProgressHUD.dismiss()
                if error != nil {
                    self.presentError(error)
                    return
                }
                NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.UserPhoneNumberChanged.rawValue, object: self)
                self.popViewController()
            }
        }
    }
    
    private func hasPhoneNumberChanged() -> Bool {
        return phoneNumberTextField.text != UserService.sharedUser!.phoneNumber
    }
    
    
    //MARK: TextFieldDelegate

    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.phoneNumberInputLimit)
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        trackKeyboardReturn()
        changePhoneNumber()
        return true
    }

}
