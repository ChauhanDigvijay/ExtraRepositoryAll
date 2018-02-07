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
        datePicker.datePickerMode = .date
        datePicker.addTarget(self, action: #selector(SignUpDOBViewController.dateChanged), for: .valueChanged)
        
        //Set selected date
        var comps = DateComponents()
        comps.year = Date.currentYearInGregorianCalendar() - Constants.jambaInsiderMinimumAge
        comps.month = Date.currentMonthOfYearInGregorianCalendar()
        comps.day = Date.currentDayOfMonthInGregorianCalendar()
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        let selectedDate = calendar.date(from: comps)
        datePicker.date = selectedDate!
        
        // Age datepicker restriction
        datePicker.maximumDate = selectedDate!
        
        //Set date Picker as the input view.
        dobTextField.inputView = datePicker
        dobTextField.tintColor = UIColor.clear
        
        //Set field for the first time
        //dateChanged()
        dobTextField.becomeFirstResponder()
        
        if UIScreen.main.is35inch() {
            dotsPageControl.removeFromSuperview()
            greenCopyLabel.removeFromSuperview()
        }
        
        //set default value for push updates
        //        self.personalizedUpdatesSwitch.setOn((UserService.signUpUserInfo!.enrollForPushUpdates), animated: true)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        UserService.signUpUserInfo?.birthdate = datePicker.date
        log.verbose("\(String(describing: UserService.signUpUserInfo?.birthdate))")
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
        let datePickerDOBStringFormatter = DateFormatter()
        datePickerDOBStringFormatter.dateStyle = .medium
        let dobFromDatePickerStringFormat = datePickerDOBStringFormatter.date(from: dobString)
        if !dobString.isValidDateOfBirth() && dobFromDatePickerStringFormat == nil {
            presentOkAlert("Invalid date of birth", message: "Please enter a valid date of birth") {
                self.dobTextField.becomeFirstResponder()
            }
            return false
        }
        let dob: Date
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
        if years! < 18 {
            presentOkAlert("Under 18 years", message: "You need to be at least 18 years old to sign up") {
                self.dobTextField.becomeFirstResponder()
            }
            return false
        }
        return true
    }
    
    //MARK: TextFieldDelegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        textField.resignFirstResponder()
        return true
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.dobInputLimit)
    }
    
    @IBAction func continueSignUp() {
        if validateField() {
            self.performSegue(withIdentifier: "ShowSignUpPasswordVC", sender: nil)
        }
    }
}
