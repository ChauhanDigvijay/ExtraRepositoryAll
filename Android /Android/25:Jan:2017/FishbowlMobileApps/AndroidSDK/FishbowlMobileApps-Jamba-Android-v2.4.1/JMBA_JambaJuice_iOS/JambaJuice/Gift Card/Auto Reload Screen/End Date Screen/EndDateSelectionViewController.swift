//
//  EndDateSelectionViewController.swift
//  JambaJuice
//
//  Created by vThink on 17/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK
import SVProgressHUD

//MARK: EndDateSelectionViewControllerDelegate
protocol EndDateSelectionViewControllerDelegate: class {
    func endDateSelected(_ value:String);
}

class EndDateSelectionViewController: UIViewController, UITableViewDelegate,UITableViewDataSource,  DatePickerViewControllerDelegate {
    
    //field name
    enum fieldName : String {
        case never  = "Never"
        case after  = "After"
        case on     = "On"
        
    }
    
    @IBOutlet weak var tableView            : UITableView!
    @IBOutlet weak var tableViewBottomSpace : NSLayoutConstraint!
    @IBOutlet weak var datePickerView       : UIView!
    fileprivate var datePickerViewController    : DatePickerViewController?
    weak var delegate                       : EndDateSelectionViewControllerDelegate?
    var neverSelection                      : Bool?
    var occuranceNumber                     : String?
    var endDate                             : String?
    var startDate                           : String?
    var SelectedValue                       : String?
    let dateFormatter                       = DateFormatter()
    
    override func viewDidLoad() {
        datePickerView.isHidden = true
        
        NotificationCenter.default.addObserver(self, selector: #selector(EndDateSelectionViewController.keyboardDidShow(_:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(EndDateSelectionViewController.keyboardWillDismiss(_:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        
        //maintain the date format
        dateFormatter.dateFormat = GiftCardAppConstants.ShortDateFormat
        
        //date is the selected value
        if (dateFormatter.date(from: SelectedValue!) != nil) {
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowDatePickerVC" {
            datePickerViewController = segue.destination as? DatePickerViewController
            datePickerViewController?.delegate = self
        }
    }
    
    // MARK: - Table view delegates
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 3
    }
    
    
  func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //cell creation for switch
        if (indexPath.row == fieldName.never.hashValue) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "NeverSelectionTableViewCell", for: indexPath) as! NeverSelectionTableViewCell
            
            cell.setData(neverSelection!)
            
            return cell
        } else if (indexPath.row == fieldName.after.hashValue) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "OccurancesTableViewCell", for: indexPath) as! OccurancesTableViewCell
            
            cell.setData(occuranceNumber!)
            
            return cell
        } else  {
            let cell = tableView.dequeueReusableCell(withIdentifier: "EndDateSelectionTableViewCell", for: indexPath) as! EndDateSelectionTableViewCell
            
            cell.setData(endDate!)
            
            return cell
        }
        
        
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        view.endEditing(true)
        //cell action for refill balance option
        if (indexPath.row == fieldName.never.hashValue) {
            let cell = tableView.cellForRow(at: indexPath)
            if let cellType = cell as? NeverSelectionTableViewCell {
                cellType.CheckImage.isHidden = !neverSelection!
                SelectedValue = fieldName.never.rawValue
                delegate?.endDateSelected(fieldName.never.rawValue)
                close()
            }
            //cell action for on a end date option
        } else if (indexPath.row == fieldName.after.hashValue) {
            var index = IndexPath(row: fieldName.never.hashValue, section: indexPath.section)
            var cell = tableView.cellForRow(at: index)
            if let cellType = cell as? NeverSelectionTableViewCell {
                cellType.makeDeActiveCell()
            }
            index = IndexPath(row: fieldName.after.hashValue, section: indexPath.section)
            cell = tableView.cellForRow(at: index)
            if let cellType = cell as? OccurancesTableViewCell {
                cellType.occuranceTextField.becomeFirstResponder()
                cellType.makeActiveCell()
            }
            
            index = IndexPath(row: fieldName.on.hashValue, section: indexPath.section)
            cell = tableView.cellForRow(at: index)
            if let cellType = cell as? EndDateSelectionTableViewCell {
                cellType.makeDeActiveCell()
            }
            
        } else if (indexPath.row == fieldName.on.hashValue) {
            var index = IndexPath(row: fieldName.never.hashValue, section: indexPath.section)
            var cell = tableView.cellForRow(at: index)
            if let cellType = cell as? NeverSelectionTableViewCell {
                cellType.CheckImage.isHidden = !false
                cellType.makeDeActiveCell()
            }
            index = IndexPath(row: fieldName.after.hashValue, section: indexPath.section)
            cell = tableView.cellForRow(at: index)
            if let cellType = cell as? OccurancesTableViewCell {
                cellType.makeDeActiveCell()
            }
            
            index = IndexPath(row: fieldName.on.hashValue, section: indexPath.section)
            cell = tableView.cellForRow(at: index)
            if let cellType = cell as? EndDateSelectionTableViewCell {
                cellType.makeActiveCell()
            }
            showDatePicker()
        }
    }
    
    //MARK: - Date picker delegates
    func showDatePicker() {
        //set minimum Date
        var selectedDate = dateFormatter.date(from: startDate!)
        selectedDate = selectedDate?.addingTimeInterval(TimeInterval(60*60*24))
        datePickerViewController?.picker.minimumDate = selectedDate
        
        //retain value for date picker
        datePickerViewController?.retainSelectedValue(endDate!)
        datePickerViewController?.pickerHeaderTitleLabel.text = fieldName.on.rawValue
        
        //show date picker
        datePickerView.isHidden = false
        datePickerViewController?.showPicker()
        
        //move the position
        tableViewBottomSpace.constant = 0
        let indexPath = IndexPath(row: fieldName.on.hashValue, section: 0)
        
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double((Int64(NSEC_PER_SEC) * 0)) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.tableView.scrollToRow(at: indexPath, at: .top, animated: true)
        })
    }
    
    func datePickerValueChanged(_ value:String) {
        delegate?.endDateSelected(value)
        SelectedValue = value
        close()
    }
    
    //close the date picker
    func closeDatePickerScreen() {
        datePickerView.isHidden = true
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
    func keyboardDidShow(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        let keyboardFrame:NSValue = userInfo.value(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
        let keyboardRectangle = keyboardFrame.cgRectValue
        
        let indexPath:IndexPath = IndexPath(row: fieldName.after.hashValue, section: 0)
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.tableViewBottomSpace.constant = keyboardRectangle.height
            self.view.layoutIfNeeded()
        }, completion: { (completed) in
            if completed {
                self.tableView.scrollToRow(at: indexPath, at: .top, animated: true)
            }
        }) 
    }
    
    func keyboardWillDismiss(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.tableViewBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }) 
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        
        var index = IndexPath(row: fieldName.never.hashValue, section: 0)
        var cell = tableView.cellForRow(at: index)
        if let cellType = cell as? NeverSelectionTableViewCell {
            cellType.makeDeActiveCell()
        }
        index = IndexPath(row: fieldName.after.hashValue, section: 0)
        cell = tableView.cellForRow(at: index)
        if let cellType = cell as? OccurancesTableViewCell {
            cellType.occuranceTextField.becomeFirstResponder()
            cellType.makeActiveCell()
        }
        
        index = IndexPath(row: fieldName.on.hashValue, section: 0)
        cell = tableView.cellForRow(at: index)
        if let cellType = cell as? EndDateSelectionTableViewCell {
            cellType.makeDeActiveCell()
        }
        
    }
    
    
    //validate & store the amount enter by user
    func textField(_ textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        var textValue = textField.text! as NSString
        textValue = textValue.replacingCharacters(in: range, with: string) as NSString
        let textValueInString = String(textValue)
        let indexPath = IndexPath(row: fieldName.after.hashValue, section: 0)
        let cell = tableView.cellForRow(at: indexPath)
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
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        delegate?.endDateSelected(textField.text!)
        close()
        return true
    }
    
    
    //MARK: - Regex for custom amount validation
    func isValidRange(_ testStr:String) -> Bool {
        let amountRegEx = "[0-9]{0,2}"
        
        let amountTest = NSPredicate(format:"SELF MATCHES %@", amountRegEx)
        return amountTest.evaluate(with: testStr)
    }
    
    // MARK: - de alloc notification
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
}



