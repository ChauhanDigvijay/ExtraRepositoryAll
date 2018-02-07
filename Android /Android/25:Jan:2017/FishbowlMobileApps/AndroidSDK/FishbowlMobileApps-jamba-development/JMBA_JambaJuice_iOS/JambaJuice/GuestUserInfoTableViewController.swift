//
//  GuestUserInfoTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 08/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class GuestUserInfoTableViewController: UITableViewController, UITextFieldDelegate {
    
    @IBOutlet weak var firstNameTextField: UITextField!
    @IBOutlet weak var lastNameTextField: UITextField!
    @IBOutlet weak var emailAddressTextField: UITextField!
    @IBOutlet weak var phoneNumberTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        firstNameTextField.becomeFirstResponder()
        
        // If already entered, populate from stored info (works for multiple orders made while the application is open).
        if UserService.guestUserInfo != nil {
            firstNameTextField.text = UserService.guestUserInfo!.firstName
            lastNameTextField.text = UserService.guestUserInfo!.lastName
            emailAddressTextField.text = UserService.guestUserInfo!.emailAddress
            phoneNumberTextField.text = UserService.guestUserInfo!.phoneNumber
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    func tryValidatingAndPopulateData() -> Bool {
        tableView.endEditing(true)
        if validateFields() {
            let reference = NSUUID().UUIDString // Random UUID
            UserService.guestUserInfo = GuestUserInfo(firstName: firstNameTextField.text!.trim(), lastName: lastNameTextField.text!.trim(), emailAddress: emailAddressTextField.text!.trim(), phoneNumber: phoneNumberTextField.text!.stringByRemovingNonNumericCharacters(), reference: reference)
            return true
        }
        return false
    }
    
    func validateFields() -> Bool {
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
        // Validate phone number
        if phoneNumberTextField.text == nil || phoneNumberTextField.text!.trim().isEmpty {
            presentOkAlert("Phone number required", message: "Please enter your phone number") {
                self.phoneNumberTextField.becomeFirstResponder()
            }
            return false
        }
        
        if phoneNumberTextField.text!.isTenDigitPhone() == false {
            presentOkAlert("Invalid phone number", message: "Please enter a valid 10 digit phone number") {
                self.phoneNumberTextField.becomeFirstResponder()
            }
            return false
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
            phoneNumberTextField.becomeFirstResponder()
        }
        else if textField == phoneNumberTextField {
            (parentViewController as! GuestUserInfoViewController).continueToNextStep()
        }
        return true
    }
    
    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 18)
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return tableView.rowHeight
    }
    
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        if textField == phoneNumberTextField {
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.phoneNumberInputLimit)
        }
        else if textField == firstNameTextField || textField == lastNameTextField {
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.oloFirstAndLastNameLimit)
        }
        else {
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.oloEmailAddressLimit)
        }
    }

}
