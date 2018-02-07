//
//  ManageGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 09/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

//MARK: AmountSelectionViewController Delegate
protocol PurchaserDetailsViewControllerDelegate: class {
    func purchaserDetailSaved(purchaserDetails:InCommOrderPurchaser,purchaserDetailsFromPayment:Bool)
}

class PurchaserDetailsViewController: UIViewController,UITableViewDelegate,PickerViewControllerDelegate, UITextFieldDelegate {
    
    enum fieldName:String {
        case switchLabelText = "Reuse Personal Details entered in Payment Screen"
        case firstName = "First Name"
        case lastName = "Last Name"
        case emailAddrress = "Email Address"
        case country = "Select Country"
    }
    
    enum SwitchFieldOption:String {
        case off = "OFF"
        case on = "ON"
    }
    
    private var pickerViewController        : PickerViewController?
    var pickerSelectedValue                 : String = ""
    var name_label_text                     : [String] = [fieldName.switchLabelText.rawValue,fieldName.firstName.rawValue,fieldName.lastName.rawValue,fieldName.emailAddrress.rawValue,fieldName.country.rawValue];
    var details_label_text                  : [String] = [SwitchFieldOption.off.rawValue,"","","",fieldName.country.rawValue];
    var currentIndex                        : NSIndexPath = NSIndexPath(forRow: 0, inSection: 0)
    var countryCode                         :[String]  = []
    var selectedCountryCode                 = ""
    var selectedIndex                       = 0
    //replacement of shared instance
    var purchaserInformationFromPayment     : Bool?
    var newPaymentDetails                   : InCommSubmitPayment?
    var existingPayment                     : InCommUserPaymentAccount?
    var purchaserDetail                     : InCommOrderPurchaser?
    @IBOutlet weak var tableView            : UITableView!
    @IBOutlet weak var tableViewBottomSpace : NSLayoutConstraint!
    @IBOutlet weak var pickerView           : UIView!
    weak var delegate                       : PurchaserDetailsViewControllerDelegate?
    
    override func viewDidLoad() {
        pickerView.hidden = true
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(PurchaserDetailsViewController.keyboardDidShow(_:)), name: UIKeyboardWillShowNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(PurchaserDetailsViewController.keyboardWillDismiss(_:)), name: UIKeyboardWillHideNotification, object: nil)
        
        //Retain The purchaser information
        if let purchaserDetail = purchaserDetail {
            if let purchaserInformationFromPayment = purchaserInformationFromPayment {
                if purchaserInformationFromPayment {
                    details_label_text[fieldName.switchLabelText.hashValue] = SwitchFieldOption.on.rawValue
                } else {
                    details_label_text[fieldName.switchLabelText.hashValue] = SwitchFieldOption.off.rawValue
                }
            }
            details_label_text[fieldName.firstName.hashValue] = purchaserDetail.firstName
            details_label_text[fieldName.lastName.hashValue] = purchaserDetail.lastName
            details_label_text[fieldName.emailAddrress.hashValue] = purchaserDetail.emailAddress
            details_label_text[fieldName.country.hashValue] = getCountryName(purchaserDetail.country)
            name_label_text[fieldName.country.hashValue] = getCountryName(purchaserDetail.country)
            selectedCountryCode = purchaserDetail.country
        }
        else{
            defaultCountryCode()
        }
        super.viewDidLoad()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "showPickerView" {
            pickerViewController = segue.destinationViewController as? PickerViewController
            pickerViewController?.delegate = self
        }
    }
    
    
    // MARK: - Tableview delegates
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return name_label_text.count;
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if (name_label_text[indexPath.row] == fieldName.switchLabelText.rawValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("PurchaserDetailSwitchTableViewCell", forIndexPath: indexPath) as! PurchaserDetailSwitchTableViewCell
            cell.name.text = name_label_text[indexPath.row]
            cell.switchButton.setOn(details_label_text[fieldName.switchLabelText.hashValue] == SwitchFieldOption.on.rawValue,animated: false)
            return cell
            //for country field
        } else if (indexPath.row == fieldName.country.hashValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("PurchaserDetailLabelTableViewCell", forIndexPath: indexPath) as! PurchaserDetailLabelTableViewCell
            cell.name.text = details_label_text[indexPath.row]
            cell.fieldName = fieldName.country.rawValue
            return cell
        } else {
            let cell = tableView.dequeueReusableCellWithIdentifier("PuchaserDetailTextFieldTableViewCell", forIndexPath: indexPath) as! PuchaserDetailTextFieldTableViewCell
            cell.textfield.placeholder = name_label_text[indexPath.row]
            cell.textfield.text = details_label_text[indexPath.row]
            cell.textfield.delegate = self
            if (name_label_text[indexPath.row] == fieldName.emailAddrress.rawValue) {
                cell.clearText = false
                cell.textfield.keyboardType = .EmailAddress
            }
            cell.textfield.enabled = false
            return cell
        }
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        disableTextBox()
        currentIndex = indexPath
        var cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
            if (details_label_text[fieldName.switchLabelText.hashValue] == SwitchFieldOption.off.rawValue || cellType.textfield.placeholder == fieldName.emailAddrress.rawValue ) {
                cellType.textfield.enabled = true
                tableView.scrollToRowAtIndexPath(currentIndex, atScrollPosition: .Top, animated: true)
                cellType.textfield.becomeFirstResponder()
                return
            }
        }
        cell = tableView.cellForRowAtIndexPath(indexPath)
        if (cell as? PurchaserDetailLabelTableViewCell) != nil {
            if (indexPath.row == fieldName.country.hashValue) {
                //show country selection picker when the switch is in off state
                if (details_label_text[fieldName.switchLabelText.hashValue] == SwitchFieldOption.off.rawValue ) {
                    showCountryPicker()
                }
            }
        }
        tableView.scrollToRowAtIndexPath(currentIndex, atScrollPosition: .Top, animated: true)
    }
    
    //MARK: - Clear/fill the details
    func clearText() {
        for index in 0...name_label_text.count - 1 {
            let indexPath = NSIndexPath(forRow: index, inSection: 0)
            var cell = tableView.cellForRowAtIndexPath(indexPath)
            if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
                if cellType.clearText {
                    cellType.alpha = GiftCardAppConstants.tableViewCellActiveState
                    details_label_text[index] = ""
                    cellType.textfield.text = ""
                }
            }
            cell = tableView.cellForRowAtIndexPath(indexPath)
            if let cellType = cell as? PurchaserDetailLabelTableViewCell {
                cellType.alpha = GiftCardAppConstants.tableViewCellActiveState
                details_label_text[fieldName.country.hashValue] = getCountryName(GiftCardAppConstants.giftCardPaymentDefaultCountryCode)
                cellType.name.text = details_label_text[fieldName.country.hashValue]
                selectedCountryCode = GiftCardAppConstants.giftCardPaymentDefaultCountryCode
            }
        }
    }
    
    //fill the purchaser details from payment object in shared instance
    func fillPurchaserDetails() {
        if let paymentDetails = newPaymentDetails {
            for index in 0...name_label_text.count - 1 {
                let indexPath = NSIndexPath(forRow: index, inSection: 0)
                var cell = tableView.cellForRowAtIndexPath(indexPath)
                if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
                    if cellType.textfield.placeholder == fieldName.firstName.rawValue {
                        cellType.alpha = GiftCardAppConstants.tableViewCellDeactiveState
                        cellType.textfield.text = paymentDetails.firstName
                        cellType.textfield.resignFirstResponder()
                        details_label_text[index] = paymentDetails.firstName!
                    } else if (cellType.textfield.placeholder == fieldName.lastName.rawValue) {
                        cellType.alpha = GiftCardAppConstants.tableViewCellDeactiveState
                        cellType.textfield.text = paymentDetails.lastName
                        cellType.textfield.resignFirstResponder()
                        details_label_text[index] = paymentDetails.lastName!
                    }
                }
                cell = tableView.cellForRowAtIndexPath(indexPath)
                if let cellType = cell as? PurchaserDetailLabelTableViewCell {
                    if cellType.fieldName == fieldName.country.rawValue {
                        cellType.alpha = GiftCardAppConstants.tableViewCellDeactiveState
                        cellType.name.text = getCountryName(paymentDetails.country!)
                        selectedCountryCode = paymentDetails.country!
                        details_label_text[index] = getCountryName(paymentDetails.country!)
                    }
                }
            }
        } else if let paymentDetails = existingPayment {
            for index in 0...name_label_text.count - 1 {
                let indexPath = NSIndexPath(forRow: index, inSection: 0)
                var cell = tableView.cellForRowAtIndexPath(indexPath)
                if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
                    if cellType.textfield.placeholder == fieldName.firstName.rawValue {
                        cellType.alpha = GiftCardAppConstants.tableViewCellDeactiveState
                        cellType.textfield.text = paymentDetails.firstName
                        cellType.textfield.resignFirstResponder()
                        details_label_text[index] = paymentDetails.firstName
                    } else if (cellType.textfield.placeholder == fieldName.lastName.rawValue) {
                        cellType.alpha = GiftCardAppConstants.tableViewCellDeactiveState
                        cellType.textfield.resignFirstResponder()
                        cellType.textfield.text = paymentDetails.lastName
                        details_label_text[index] = paymentDetails.lastName
                    }
                }
                cell = tableView.cellForRowAtIndexPath(indexPath)
                if let cellType = cell as? PurchaserDetailLabelTableViewCell {
                    cellType.alpha = GiftCardAppConstants.tableViewCellDeactiveState
                    cellType.name.text = getCountryName(paymentDetails.country)
                    details_label_text[index] = getCountryName(paymentDetails.country)
                    selectedCountryCode = paymentDetails.country
                }
            }
        }
    }
    
    //disable the selected textbox
    func disableTextBox()  {
        let cell = tableView.cellForRowAtIndexPath(currentIndex)
        if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
            cellType.textfield.enabled = false
        }
    }
    
    // MARK: - TextField delegate
    func keyboardDidShow(notification: NSNotification) {
        let userInfo:NSDictionary = notification.userInfo!
        let keyboardFrame:NSValue = userInfo.valueForKey(UIKeyboardFrameEndUserInfoKey) as! NSValue
        let keyboardRectangle = keyboardFrame.CGRectValue()
        let keyboardHeight = keyboardRectangle.height
        
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.tableViewBottomSpace.constant = keyboardHeight
            self.view.layoutIfNeeded()
        }) { (completed) in
            if completed {
                self.tableView.scrollToRowAtIndexPath(self.currentIndex, atScrollPosition: .Top, animated: true)
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
    
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        var textValue = textField.text! as NSString
        textValue = textValue.stringByReplacingCharactersInRange(range, withString: string)
        let textStringValue = textValue as String
        if string == ""{
            details_label_text[currentIndex.row] = textStringValue
            return true
        }
        // Validating firstname and last name characters and length
        else if (name_label_text[currentIndex.row] == fieldName.firstName.rawValue || name_label_text[currentIndex.row] == fieldName.lastName.rawValue){
            if GiftCardAppConstants.nameValidation(textStringValue){
                details_label_text[currentIndex.row] = textStringValue
                return true
            }else{
                return false
            }
        }else{
            details_label_text[currentIndex.row] = textStringValue
            return true
        }
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        var cell = tableView.cellForRowAtIndexPath(currentIndex)
        if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
            cellType.textfield.enabled = false
            if cellType.textfield.placeholder != fieldName.emailAddrress.rawValue {
                currentIndex = NSIndexPath(forRow: currentIndex.row+1, inSection: currentIndex.section)
                cell = tableView.cellForRowAtIndexPath(currentIndex)
                if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
                    cellType.textfield.enabled = true
                    cellType.textfield.becomeFirstResponder()
                }
            } else {
                view.endEditing(true)
                tableView.reloadData()
                return true
            }
        }else {
            view.endEditing(true)
        }
        return true
    }
    
    //MARK: - IBAction methods
    @IBAction func switchButtonChanged(sender: UISwitch) {
        if (sender.on) {
            details_label_text[fieldName.switchLabelText.hashValue] = SwitchFieldOption.on.rawValue
            
            fillPurchaserDetails()
        } else {
            details_label_text[fieldName.switchLabelText.hashValue] = SwitchFieldOption.off.rawValue
            
            clearText()
        }
    }
    
    @IBAction func savePurchaserInformatiom() {
        if !validation() {
            return
        }
        let purchaserDetail = InCommOrderPurchaser(firstName: details_label_text[fieldName.firstName.hashValue].trim(), lastName: details_label_text[fieldName.lastName.hashValue].trim(), emailAddress: details_label_text[fieldName.emailAddrress.hashValue].trim(), country: selectedCountryCode)
        
        var purchaserDetailFromPayment:Bool = false
        if details_label_text[fieldName.switchLabelText.hashValue] == SwitchFieldOption.on.rawValue {
            purchaserDetailFromPayment = true
        }
        delegate?.purchaserDetailSaved(purchaserDetail, purchaserDetailsFromPayment: purchaserDetailFromPayment)
        self.closeScreen()
    }
    
    @IBAction func closeScreen() {
        if (self.isBeingPresented()) {
            dismissViewControllerAnimated(true, completion: nil)
        } else {
            popViewController()
        }
    }
    
    //MARK: - Country picker
    func showCountryPicker() {
        tableViewBottomSpace.constant = 0
        let brand = InCommGiftCardBrandDetails.sharedInstance.brand!
        var dataSource:[[String]] = [[]]
        
        for index in 0...brand.billingCountries.count - 1 {
            dataSource[0].insert(brand.billingCountries[index].name, atIndex: index)
            countryCode.insert(brand.billingCountries[index].code, atIndex: index)
            if (selectedCountryCode != "" && brand.billingCountries[index].code == selectedCountryCode) {
                selectedIndex = index
            }
        }
        
        if (selectedCountryCode == "") {
            pickerSelectedValue = dataSource[0][0]
            selectedIndex = 0
        } else {
            pickerSelectedValue = name_label_text[fieldName.country.hashValue]
        }
        pickerViewController?.pickerData = dataSource
        pickerViewController?.noOfComponents = 1
        pickerViewController?.pickerHeaderTitleLabel.text = fieldName.country.rawValue
        pickerViewController?.picker.reloadAllComponents()
        pickerViewController?.picker.selectRow(selectedIndex, inComponent: 0, animated: false)
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW,  (Int64(NSEC_PER_SEC) * 0)), dispatch_get_main_queue(), { () -> Void in
            self.tableView.scrollToRowAtIndexPath(self.currentIndex, atScrollPosition: .Top, animated: true)
        })
        
        pickerView.hidden = false
        pickerViewController?.showPicker()
    }
    
    func pickerValueChanged(value:String,index :Int) {
        pickerSelectedValue = value
        selectedIndex = index
        
        //update selected value in array
        details_label_text[currentIndex.row] = pickerSelectedValue
        selectedCountryCode = countryCode[selectedIndex]
        tableView.reloadData()
    }
    
    func  closepickerScreen()  {
        tableViewBottomSpace.constant = 0
        pickerView.hidden = true
    }
    
    //MARK: - Get country name
    func getCountryName(countryCode: String) -> String {
        let brand = InCommGiftCardBrandDetails.sharedInstance.brand!
        for country in brand.billingCountries {
            if (countryCode == country.code) {
                return country.name
            }
        }
        return ""
    }
    
    //MARK: - Validation
    func validation() -> Bool {
        for index in 0...name_label_text.count - 1 {
            if (name_label_text[index] == fieldName.firstName.rawValue || name_label_text[index] == fieldName.lastName.rawValue) {
                let value = details_label_text[index].trim()
                if (value.characters.count == 0) {
                    let message = "Please enter " + name_label_text[index].lowercaseString
                    self.presentOkAlert("Alert", message: message)
                    return false
                }else if value.characters.count < 3{
                    let message =  "Please enter '" + name_label_text[index].lowercaseString + "' minimum 3 characters"
                    self.presentOkAlert("Alert", message: message)
                    return false
                }
            } else if (name_label_text[index] == fieldName.emailAddrress.rawValue) {
                let value = details_label_text[index].trim()
                if (value.characters.count == 0) {
                    let message = "Please enter " + name_label_text[index].lowercaseString
                    self.presentOkAlert("Alert", message: message)
                    return false
                }
                if !value.isEmail() {
                    self.presentOkAlert("Alert", message: "Please enter valid email address" )
                    return false
                }
            } else if (name_label_text[index] == fieldName.country.rawValue) {
                if (details_label_text[index].trim().characters.count == 0) {
                    let message = "Please enter " + name_label_text[index].lowercaseString
                    self.presentOkAlert("Alert", message: message)
                    return false
                }
            }
        }
        return true
    }
    
    // MARK: Default country code
    func defaultCountryCode(){
        details_label_text[fieldName.country.hashValue] = getCountryName(GiftCardAppConstants.giftCardPaymentDefaultCountryCode)
        name_label_text[fieldName.country.hashValue] = getCountryName(GiftCardAppConstants.giftCardPaymentDefaultCountryCode)
        selectedCountryCode = GiftCardAppConstants.giftCardPaymentDefaultCountryCode
    }
    
    // MARK: De alloc Notification
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
}