//
//  FeedbackViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 8/24/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SVProgressHUD

class FeedbackViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, UITextViewDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var sendFeedbackButton: UIButton!
    var feedback = Feedback()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)        
        configureNavigationBar(.LightBlue)
        tableView.estimatedRowHeight = 53
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.tableFooterView = UIView()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        //Make first responder
        let cellAt2ndRow = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 1, inSection: 0))
        if let cell = cellAt2ndRow as? FeedbackTextFieldTableViewCell {
            cell.textField.becomeFirstResponder()
        }
        else if let cell = cellAt2ndRow as? FeedbackTextViewTableViewCell {
            cell.textView.becomeFirstResponder()
        }
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    //MARK: TableView Delegate & DataSource
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return UserService.sharedUser == nil ? 4 : 2
    }
    
    static let tagForEmailAddressTextField = 10
    static let tagForPhoneNumberTextField = 20
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let needsUserInfoCells = UserService.sharedUser == nil
        var caseNum = indexPath.row
        //We only need 2 rows in case of logged in
        if !needsUserInfoCells && indexPath.row == 1 {
            caseNum = 3
        }
        switch caseNum {
        case 0:
            let cell = tableView.dequeueReusableCellWithIdentifier("FeedbackBasicCell", forIndexPath: indexPath) as! FeedbackBasicTableViewCell
            cell.label.text = feedback.feedbackType.rawValue
            return cell
        case 1:
            let textFieldCell = tableView.dequeueReusableCellWithIdentifier("FeedbackTextFieldCell", forIndexPath: indexPath) as! FeedbackTextFieldTableViewCell
            textFieldCell.textField.placeholder = "Email address (optional)"
            textFieldCell.textField.keyboardType = .EmailAddress
            textFieldCell.textField.text = feedback.emailAddress ?? ""
            textFieldCell.textField.delegate = self
            textFieldCell.textField.tag = FeedbackViewController.tagForEmailAddressTextField
            textFieldCell.textField.addTarget(self, action: #selector(FeedbackViewController.textFieldEditingChanged(_:)), forControlEvents: .EditingChanged)
            return textFieldCell
        case 2:
            let textFieldCell = tableView.dequeueReusableCellWithIdentifier("FeedbackTextFieldCell", forIndexPath: indexPath) as! FeedbackTextFieldTableViewCell
            textFieldCell.textField.placeholder = "Phone number (optional)"
            textFieldCell.textField.keyboardType = .PhonePad
            textFieldCell.textField.text = feedback.phoneNumber ?? ""
            textFieldCell.textField.delegate = self
            textFieldCell.textField.tag = FeedbackViewController.tagForPhoneNumberTextField
            textFieldCell.textField.addTarget(self, action: #selector(FeedbackViewController.textFieldEditingChanged(_:)), forControlEvents: .EditingChanged)
            return textFieldCell
        case 3:
            let cell = tableView.dequeueReusableCellWithIdentifier("FeedbackTextViewCell", forIndexPath: indexPath) as! FeedbackTextViewTableViewCell
            cell.textView.text = feedback.feedbackString
            cell.placeholderLabel.hidden = !feedback.feedbackString.isEmpty
            cell.textView.delegate = self
            cell.textView.scrollEnabled = false
            //No need to adjust size since, initially its empty.
            return cell
        default:
            assert(false, "Unexpected indexPath")
            return UITableViewCell()
        }
    }
    
    func textFieldEditingChanged(textField: UITextField) {
        if textField.tag == FeedbackViewController.tagForEmailAddressTextField {
            feedback.emailAddress = textField.text ?? ""
        }
        else {
            feedback.phoneNumber = textField.text ?? ""
        }
    }
    
    //MARK: TextField Delegate
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        let limit: Int!
        if textField.tag == FeedbackViewController.tagForEmailAddressTextField {
            limit = Constants.spendGoEmailAddressLimit
        }
        else {
            limit = Constants.phoneNumberInputLimit
        }
        return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: limit)
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        if textField.tag == FeedbackViewController.tagForEmailAddressTextField {
            let cell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 2, inSection: 0)) as? FeedbackTextFieldTableViewCell
            cell?.textField.becomeFirstResponder()
        }
        else {
            let cell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 3, inSection: 0)) as? FeedbackTextViewTableViewCell
            cell?.textView.becomeFirstResponder()
        }
        return true
    }
    
    //MARK: TextView Delegate
    
    func textViewDidChange(textView: UITextView) {
        //Set Feedback String
        feedback.feedbackString = textView.text
        //Get IndexPath and Cell
        let indexPathOfLastRow: NSIndexPath!
        if UserService.sharedUser != nil {
            indexPathOfLastRow = NSIndexPath(forRow: 1, inSection: 0)
        }
        else {
            indexPathOfLastRow = NSIndexPath(forRow: 3, inSection: 0)
        }
        let cell = tableView.cellForRowAtIndexPath(indexPathOfLastRow) as? FeedbackTextViewTableViewCell
        //Set State of placeholder cell.
        cell?.placeholderLabel.hidden = !textView.text.isEmpty
        //Check new size
        let size = textView.bounds.size
        let newSize = textView.sizeThatFits(CGSize(width: size.width, height: CGFloat.max))
        // Resize the cell only when cell's size is changed
        if size.height != newSize.height {
            UIView.setAnimationsEnabled(false)
            tableView.beginUpdates()
            tableView.endUpdates()
            UIView.setAnimationsEnabled(true)
            tableView.scrollToRowAtIndexPath(indexPathOfLastRow, atScrollPosition: .Bottom, animated: false)
        }
    }
    
    //MARK: Action
    
    @IBAction func sendFeedback(sender: UIButton) {
        sendFeedback()
    }
    
    func sendFeedback() {
        // Resign First Responder
        view.endEditing(true)
        //Validate Fields
        if validateFields() {
            pruneAndPopulateFields()
            SVProgressHUD.showWithStatus("Sending Feedback...", maskType: .Clear)
            FeedbackService.submitFeedback(feedback) { (error) -> Void in
                SVProgressHUD.dismiss()
                if error != nil {
                    clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")

                    self.presentError(error)
                    return
                }
                self.performSegueWithIdentifier("FeedbackSent", sender: self)
            }
        }
    }
    
    func validateFields() -> Bool {
        let isLoggedIn = UserService.sharedUser != nil
        if !isLoggedIn {
            //We are not logged in
            //Only validate if this is non-nil and filled.
            if feedback.emailAddress != nil && !feedback.emailAddress!.trim().isEmpty {
                // Validate email
                if !feedback.emailAddress!.isEmail() {
                    presentOkAlert("Invalid email address", message: "Please enter a valid email address") {
                        let cell = self.tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 1, inSection: 0)) as? FeedbackTextFieldTableViewCell
                        cell?.textField.becomeFirstResponder()
                    }
                    return false
                }
            }
            //Only validate if this is non-nil and filled.
            if feedback.phoneNumber != nil && !feedback.phoneNumber!.trim().isEmpty{
                // Validate phone
                if !feedback.phoneNumber!.isTenDigitPhone() {
                    presentOkAlert("Invalid phone number", message: "Please enter a valid 10-digit phone number") {
                        let cell = self.tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 2, inSection: 0)) as? FeedbackTextFieldTableViewCell
                        cell?.textField.becomeFirstResponder()
                    }
                    return false
                }
            }
        }
        //Validate feedback is not empty
        if feedback.feedbackString.trim().isEmpty {
            presentOkAlert("Feedback required", message: "Please enter feedback text/comment") {
                let row = isLoggedIn ? 1 : 3
                let cell = self.tableView.cellForRowAtIndexPath(NSIndexPath(forRow: row, inSection: 0)) as? FeedbackTextViewTableViewCell
                cell?.textView.becomeFirstResponder()
            }
            return false
        }
        return true
    }
    
    func pruneAndPopulateFields() {
        if UserService.sharedUser == nil {
            //Convert phone to numeric string
            feedback.phoneNumber = feedback.phoneNumber?.stringByRemovingNonNumericCharacters()
        }
        else {
            feedback.emailAddress = UserService.sharedUser!.emailAddress ?? ""
            feedback.phoneNumber = UserService.sharedUser!.phoneNumber
        }
    }

}