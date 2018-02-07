//
//  ManageGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 09/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK


//MARK: AmountSelectionViewController Delegate
protocol PurchaserDetailsViewControllerDelegate: class {
    func purchaserDetailSaved(_ purchaserDetails:InCommOrderPurchaser,purchaserDetailsFromPayment:Bool)
}

class PurchaserDetailsViewController: UIViewController,UITableViewDelegate,UITableViewDataSource,PickerViewControllerDelegate, UITextFieldDelegate {
    
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
    
    fileprivate var pickerViewController        : PickerViewController?
    var pickerSelectedValue                 : String = ""
    var name_label_text                     : [String] = [fieldName.switchLabelText.rawValue,fieldName.firstName.rawValue,fieldName.lastName.rawValue,fieldName.emailAddrress.rawValue,fieldName.country.rawValue];
    var details_label_text                  : [String] = [SwitchFieldOption.off.rawValue,"","","",fieldName.country.rawValue];
    var currentIndex                        : IndexPath = IndexPath(row: 0, section: 0)
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
        pickerView.isHidden = true
        
        NotificationCenter.default.addObserver(self, selector: #selector(PurchaserDetailsViewController.keyboardDidShow(_:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(PurchaserDetailsViewController.keyboardWillDismiss(_:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showPickerView" {
            pickerViewController = segue.destination as? PickerViewController
            pickerViewController?.delegate = self
        }
    }
    
    
    // MARK: - Tableview delegates
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return name_label_text.count;
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (name_label_text[indexPath.row] == fieldName.switchLabelText.rawValue) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "PurchaserDetailSwitchTableViewCell", for: indexPath) as! PurchaserDetailSwitchTableViewCell
            cell.name.text = name_label_text[indexPath.row]
            cell.switchButton.setOn(details_label_text[fieldName.switchLabelText.hashValue] == SwitchFieldOption.on.rawValue,animated: false)
            return cell
            //for country field
        } else if (indexPath.row == fieldName.country.hashValue) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "PurchaserDetailLabelTableViewCell", for: indexPath) as! PurchaserDetailLabelTableViewCell
            cell.name.text = details_label_text[indexPath.row]
            cell.fieldName = fieldName.country.rawValue
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "PuchaserDetailTextFieldTableViewCell", for: indexPath) as! PuchaserDetailTextFieldTableViewCell
            cell.textfield.placeholder = name_label_text[indexPath.row]
            cell.textfield.text = details_label_text[indexPath.row]
            cell.textfield.delegate = self
            if (name_label_text[indexPath.row] == fieldName.emailAddrress.rawValue) {
                cell.clearText = false
                cell.textfield.keyboardType = .emailAddress
            }
            cell.textfield.isEnabled = false
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        disableTextBox()
        currentIndex = indexPath
        var cell = tableView.cellForRow(at: indexPath)
        if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
            if (details_label_text[fieldName.switchLabelText.hashValue] == SwitchFieldOption.off.rawValue || cellType.textfield.placeholder == fieldName.emailAddrress.rawValue ) {
                cellType.textfield.isEnabled = true
                tableView.scrollToRow(at: currentIndex, at: .top, animated: true)
                cellType.textfield.becomeFirstResponder()
                return
            }
        }
        cell = tableView.cellForRow(at: indexPath)
        if (cell as? PurchaserDetailLabelTableViewCell) != nil {
            if (indexPath.row == fieldName.country.hashValue) {
                //show country selection picker when the switch is in off state
                if (details_label_text[fieldName.switchLabelText.hashValue] == SwitchFieldOption.off.rawValue ) {
                    showCountryPicker()
                }
            }
        }
        tableView.scrollToRow(at: currentIndex, at: .top, animated: true)
    }
    
    //MARK: - Clear/fill the details
    func clearText() {
        for index in 0...name_label_text.count - 1 {
            let indexPath = IndexPath(row: index, section: 0)
            var cell = tableView.cellForRow(at: indexPath)
            if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
                if cellType.clearText {
                    cellType.alpha = GiftCardAppConstants.tableViewCellActiveState
                    details_label_text[index] = ""
                    cellType.textfield.text = ""
                }
            }
            cell = tableView.cellForRow(at: indexPath)
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
                let indexPath = IndexPath(row: index, section: 0)
                var cell = tableView.cellForRow(at: indexPath)
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
                cell = tableView.cellForRow(at: indexPath)
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
                let indexPath = IndexPath(row: index, section: 0)
                var cell = tableView.cellForRow(at: indexPath)
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
                cell = tableView.cellForRow(at: indexPath)
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
        let cell = tableView.cellForRow(at: currentIndex)
        if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
            cellType.textfield.isEnabled = false
        }
    }
    
    // MARK: - TextField delegate
    func keyboardDidShow(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        let keyboardFrame:NSValue = userInfo.value(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
        let keyboardRectangle = keyboardFrame.cgRectValue
        let keyboardHeight = keyboardRectangle.height
        
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.tableViewBottomSpace.constant = keyboardHeight
            self.view.layoutIfNeeded()
        }, completion: { (completed) in
            if completed {
                self.tableView.scrollToRow(at: self.currentIndex, at: .top, animated: true)
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
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        var textValue = textField.text! as NSString
        textValue = textValue.replacingCharacters(in: range, with: string) as NSString
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
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        var cell = tableView.cellForRow(at: currentIndex)
        if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
            cellType.textfield.isEnabled = false
            if cellType.textfield.placeholder != fieldName.emailAddrress.rawValue {
                currentIndex = IndexPath(row: currentIndex.row+1, section: currentIndex.section)
                cell = tableView.cellForRow(at: currentIndex)
                if let cellType = cell as? PuchaserDetailTextFieldTableViewCell {
                    cellType.textfield.isEnabled = true
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
    @IBAction func switchButtonChanged(_ sender: UISwitch) {
        if (sender.isOn) {
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
        if (self.isBeingPresented) {
            dismiss(animated: true, completion: nil)
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
            dataSource[0].insert(brand.billingCountries[index].name, at: index)
            countryCode.insert(brand.billingCountries[index].code, at: index)
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
        
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double((Int64(NSEC_PER_SEC) * 0)) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.tableView.scrollToRow(at: self.currentIndex, at: .top, animated: true)
        })
        
        pickerView.isHidden = false
        pickerViewController?.showPicker()
    }
    
    func pickerValueChanged(_ value:String,index :Int) {
        pickerSelectedValue = value
        selectedIndex = index
        
        //update selected value in array
        details_label_text[currentIndex.row] = pickerSelectedValue
        selectedCountryCode = countryCode[selectedIndex]
        tableView.reloadData()
    }
    
    func  closepickerScreen()  {
        tableViewBottomSpace.constant = 0
        pickerView.isHidden = true
    }
    
    //MARK: - Get country name
    func getCountryName(_ countryCode: String) -> String {
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
                    let message = "Please enter " + name_label_text[index].lowercased()
                    self.presentOkAlert("Alert", message: message)
                    return false
                }else if value.characters.count < 3{
                    let message =  "Please enter '" + name_label_text[index].lowercased() + "' minimum 3 characters"
                    self.presentOkAlert("Alert", message: message)
                    return false
                }
            } else if (name_label_text[index] == fieldName.emailAddrress.rawValue) {
                let value = details_label_text[index].trim()
                if (value.characters.count == 0) {
                    let message = "Please enter " + name_label_text[index].lowercased()
                    self.presentOkAlert("Alert", message: message)
                    return false
                }
                if !value.isEmail() {
                    self.presentOkAlert("Alert", message: "Please enter valid email address" )
                    return false
                }
            } else if (name_label_text[index] == fieldName.country.rawValue) {
                if (details_label_text[index].trim().characters.count == 0) {
                    let message = "Please enter " + name_label_text[index].lowercased()
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
        NotificationCenter.default.removeObserver(self)
    }
}
