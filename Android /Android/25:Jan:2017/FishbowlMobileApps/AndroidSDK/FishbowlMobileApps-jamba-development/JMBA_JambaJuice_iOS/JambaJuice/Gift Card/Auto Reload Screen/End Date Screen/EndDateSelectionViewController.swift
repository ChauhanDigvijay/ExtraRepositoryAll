//
//  EndDateSelectionViewController.swift
//  JambaJuice
//
//  Created by vThink on 17/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke
import SVProgressHUD

//MARK: EndDateSelectionViewControllerDelegate
protocol EndDateSelectionViewControllerDelegate: class {
    func endDateSelected(value:String);
}

class EndDateSelectionViewController: UIViewController, UITableViewDelegate,  DatePickerViewControllerDelegate {
    
    //field name
    enum fieldName : String {
        case never  = "Never"
        case after  = "After"
        case on     = "On"
        
    }
    
    @IBOutlet weak var tableView            : UITableView!
    @IBOutlet weak var tableViewBottomSpace : NSLayoutConstraint!
    @IBOutlet weak var datePickerView       : UIView!
    private var datePickerViewController    : DatePickerViewController?
    weak var delegate                       : EndDateSelectionViewControllerDelegate?
    var neverSelection                      : Bool?
    var occuranceNumber                     : String?
    var endDate                             : String?
    var startDate                           : String?
    var SelectedValue                       : String?
    let dateFormatter                       = NSDateFormatter()
    
    override func viewDidLoad() {
        datePickerView.hidden = true
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(EndDateSelectionViewController.keyboardDidShow(_:)), name: UIKeyboardWillShowNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(EndDateSelectionViewController.keyboardWillDismiss(_:)), name: UIKeyboardWillHideNotification, object: nil)
        
        //maintain the date format
        dateFormatter.dateFormat = GiftCardAppConstants.ShortDateFormat
        
        //date is the selected value
        if (dateFormatter.dateFromString(SelectedValue!) != nil) {
            neverSelection = false
            endDate = SelectedValue
            occuranceNumber = "0"
            //number of occurances is the selected value
        } else if (Int16(SelectedValue!) != nil) {
            neverSelection = false
            endDate = ""
            occuranceNumber = SelectedValue
            
            //never is the selected value
        } else if SelectedValue != "" {
            neverSelection = true
            endDate = ""
            occuranceNumber = "0"
            
            //nothing is selected
        } else {
            neverSelection = false
            endDate = ""
            occuranceNumber = "0"
        }
        
        super.viewDidLoad()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ShowDatePickerVC" {
            datePickerViewController = segue.destinationViewController as? DatePickerViewController
            datePickerViewController?.delegate = self
        }
    }
    
    // MARK: - Table view delegates
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 3
    }
    
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        //cell creation for switch
        if (indexPath.row == fieldName.never.hashValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("NeverSelectionTableViewCell", forIndexPath: indexPath) as! NeverSelectionTableViewCell
            
            cell.setData(neverSelection!)
            
            return cell
        } else if (indexPath.row == fieldName.after.hashValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("OccurancesTableViewCell", forIndexPath: indexPath) as! OccurancesTableViewCell
            
            cell.setData(occuranceNumber!)
            
            return cell
        } else  {
            let cell = tableView.dequeueReusableCellWithIdentifier("EndDateSelectionTableViewCell", forIndexPath: indexPath) as! EndDateSelectionTableViewCell
            
            cell.setData(endDate!)
            
            return cell
        }
        
        
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        view.endEditing(true)
        //cell action for refill balance option
        if (indexPath.row == fieldName.never.hashValue) {
            let cell = tableView.cellForRowAtIndexPath(indexPath)
            if let cellType = cell as? NeverSelectionTableViewCell {
                cellType.CheckImage.hidden = !neverSelection!
                SelectedValue = fieldName.never.rawValue
                delegate?.endDateSelected(fieldName.never.rawValue)
                close()
            }
            //cell action for on a end date option
        } else if (indexPath.row == fieldName.after.hashValue) {
            var index = NSIndexPath(forRow: fieldName.never.hashValue, inSection: indexPath.section)
            var cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? NeverSelectionTableViewCell {
                cellType.makeDeActiveCell()
            }
            index = NSIndexPath(forRow: fieldName.after.hashValue, inSection: indexPath.section)
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? OccurancesTableViewCell {
                cellType.occuranceTextField.becomeFirstResponder()
                cellType.makeActiveCell()
            }
            
            index = NSIndexPath(forRow: fieldName.on.hashValue, inSection: indexPath.section)
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? EndDateSelectionTableViewCell {
                cellType.makeDeActiveCell()
            }
            
        } else if (indexPath.row == fieldName.on.hashValue) {
            var index = NSIndexPath(forRow: fieldName.never.hashValue, inSection: indexPath.section)
            var cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? NeverSelectionTableViewCell {
                cellType.CheckImage.hidden = !false
                cellType.makeDeActiveCell()
            }
            index = NSIndexPath(forRow: fieldName.after.hashValue, inSection: indexPath.section)
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? OccurancesTableViewCell {
                cellType.makeDeActiveCell()
            }
            
            index = NSIndexPath(forRow: fieldName.on.hashValue, inSection: indexPath.section)
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? EndDateSelectionTableViewCell {
                cellType.makeActiveCell()
            }
            showDatePicker()
        }
    }
    
    //MARK: - Date picker delegates
    func showDatePicker() {
        //set minimum Date
        var selectedDate = dateFormatter.dateFromString(startDate!)
        selectedDate = selectedDate?.dateByAddingTimeInterval(NSTimeInterval(60*60*24))
        datePickerViewController?.picker.minimumDate = selectedDate
        
        //retain value for date picker
        datePickerViewController?.retainSelectedValue(endDate!)
        datePickerViewController?.pickerHeaderTitleLabel.text = fieldName.on.rawValue
        
        //show date picker
        datePickerView.hidden = false
        datePickerViewController?.showPicker()
        
        //move the position
        tableViewBottomSpace.constant = 0
        let indexPath = NSIndexPath(forRow: fieldName.on.hashValue, inSection: 0)
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW,  (Int64(NSEC_PER_SEC) * 0)), dispatch_get_main_queue(), { () -> Void in
            self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
        })
    }
    
    func datePickerValueChanged(value:String) {
        delegate?.endDateSelected(value)
        SelectedValue = value
        close()
    }
    
    //close the date picker
    func closeDatePickerScreen() {
        datePickerView.hidden = true
        tableViewBottomSpace.constant = 0
    }
    
    
    // MARK: - IBAction methods
    @IBAction func close(){
        popViewController()
    }
    
    @IBAction func saveOption(){
        if SelectedValue == "" {
            self.presentOkAlert("Error", message: "Please choose any one option")
        }
        delegate?.endDateSelected(SelectedValue!)
        popViewController()
    }
    
    //MARK: - Text field delegates
    // MARK: - TextField delegate
    func keyboardDidShow(notification: NSNotification) {
        let userInfo:NSDictionary = notification.userInfo!
        let keyboardFrame:NSValue = userInfo.valueForKey(UIKeyboardFrameEndUserInfoKey) as! NSValue
        let keyboardRectangle = keyboardFrame.CGRectValue()
        
        let indexPath:NSIndexPath = NSIndexPath(forRow: fieldName.after.hashValue, inSection: 0)
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.tableViewBottomSpace.constant = keyboardRectangle.height
            self.view.layoutIfNeeded()
        }) { (completed) in
            if completed {
                self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
            }
        }
    }
    
    func keyboardWillDismiss(notification: NSNotification) {
        let userInfo:NSDictionary = notification.userInfo!
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue) {
            self.tableViewBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }
    }
    
    func textFieldDidBeginEditing(textField: UITextField) {
        
        var index = NSIndexPath(forRow: fieldName.never.hashValue, inSection: 0)
        var cell = tableView.cellForRowAtIndexPath(index)
        if let cellType = cell as? NeverSelectionTableViewCell {
            cellType.makeDeActiveCell()
        }
        index = NSIndexPath(forRow: fieldName.after.hashValue, inSection: 0)
        cell = tableView.cellForRowAtIndexPath(index)
        if let cellType = cell as? OccurancesTableViewCell {
            cellType.occuranceTextField.becomeFirstResponder()
            cellType.makeActiveCell()
        }
        
        index = NSIndexPath(forRow: fieldName.on.hashValue, inSection: 0)
        cell = tableView.cellForRowAtIndexPath(index)
        if let cellType = cell as? EndDateSelectionTableViewCell {
            cellType.makeDeActiveCell()
        }
        
    }
    
    
    //validate & store the amount enter by user
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        var textValue = textField.text! as NSString
        textValue = textValue.stringByReplacingCharactersInRange(range, withString: string)
        let textValueInString = String(textValue)
        let indexPath = NSIndexPath(forRow: fieldName.after.hashValue, inSection: 0)
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? OccurancesTableViewCell {
            if !isValidRange(textValueInString) {
                cellType.occuranceTextField.text = occuranceNumber
                return false
                //find $ is in text
            } else {
                occuranceNumber = textValueInString
                SelectedValue = occuranceNumber
            }
        }
        return true
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        delegate?.endDateSelected(textField.text!)
        close()
        return true
    }
    
    
    //MARK: - Regex for custom amount validation
    func isValidRange(testStr:String) -> Bool {
        let amountRegEx = "[0-9]{0,2}"
        
        let amountTest = NSPredicate(format:"SELF MATCHES %@", amountRegEx)
        return amountTest.evaluateWithObject(testStr)
    }
    
    // MARK: - de alloc notification
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
}



