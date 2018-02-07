//
//  SignUpDOBViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 01/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK

class SignUpDOBViewController: UIViewController {

    @IBOutlet weak var dotsPageControl: UIPageControl!
    @IBOutlet weak var greenCopyLabel: UILabel!
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var dobTextField: UITextField!
    weak var datePicker: UIDatePicker!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //Make Date Picker and set its actions
        //Since datePicker var is weak. We need to have a local strong var to keep it alive till added to textField
        let dtPicker = UIDatePicker()
        datePicker = dtPicker
        datePicker.datePickerMode = .Date
        datePicker.addTarget(self, action: #selector(SignUpDOBViewController.dateChanged), forControlEvents: .ValueChanged)
        
        //Set selected date
        let comps = NSDateComponents()
        comps.year = NSDate.currentYearInGregorianCalendar() - Constants.jambaInsiderMinimumAge
        comps.month = NSDate.currentMonthOfYearInGregorianCalendar()
        comps.day = NSDate.currentDayOfMonthInGregorianCalendar()
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        let selectedDate = calendar.dateFromComponents(comps)
        datePicker.date = selectedDate!
        
        //Set date Picker as the input view.
        dobTextField.inputView = datePicker
        dobTextField.tintColor = UIColor.clearColor()

        //Set field for the first time
        //dateChanged()
        dobTextField.becomeFirstResponder()
        
        if UIScreen.mainScreen().is35inch() {
            dotsPageControl.removeFromSuperview()
            greenCopyLabel.removeFromSuperview()
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func shouldPerformSegueWithIdentifier(identifier: String?, sender: AnyObject?) -> Bool {
        return validateField()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        UserService.signUpUserInfo?.birthdate = datePicker.date
        log.verbose("\(UserService.signUpUserInfo?.birthdate)")
    }
    
    func dateChanged() {
        let dateString = datePicker.date.dateString()
        dobTextField.text = dateString
    }
    
    func validateField() -> Bool {
        view.endEditing(true)
        let dobString = dobTextField.text?.trim() ?? ""
        // Validate dob
        if dobString.isEmpty {
            presentOkAlert("Date of birth required", message: "Please enter your date of birth") {
                self.dobTextField.becomeFirstResponder()
            }
            return false
        }
        //Validate dob format
        let datePickerDOBStringFormatter = NSDateFormatter()
        datePickerDOBStringFormatter.dateStyle = .MediumStyle
        let dobFromDatePickerStringFormat = datePickerDOBStringFormatter.dateFromString(dobString)
        if !dobString.isValidDateOfBirth() && dobFromDatePickerStringFormat == nil {
            presentOkAlert("Invalid date of birth", message: "Please enter a valid date of birth") {
                self.dobTextField.becomeFirstResponder()
            }
            return false
        }
        let dob: NSDate
        if dobFromDatePickerStringFormat != nil {
            dob = dobFromDatePickerStringFormat!
            //In case user entered string in same format as datePicker
            datePicker.date = dob
        }
        else {
            //Get DOB from text
            dob = dobString.dateFromCommonDateFormats()!
            datePicker.date = dob
            dateChanged()
        }
        log.verbose("\(dob)")
        //Compare DOB
        let components = dob.differenceToTodayInGregorianCalendar()
        let years = components.year
        if years < 18 {
            presentOkAlert("Under 18 years", message: "You need to be at least 18 years old to sign up") {
                self.dobTextField.becomeFirstResponder()
            }
            return false
        }
        return true
    }
    
    //MARK: TextFieldDelegate
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        trackKeyboardReturn()
        textField.resignFirstResponder()
        return true
    }
    
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.dobInputLimit)
    }
    
}
