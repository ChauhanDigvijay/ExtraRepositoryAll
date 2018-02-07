//
//  EnterCreditCardViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 03/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class EnterCreditCardViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UIPickerViewDelegate, UIPickerViewDataSource, UITextFieldDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var barBackButton: UIBarButtonItem!
    
    //Adding month number before name as credit cards mostly have expiration date in form of mm/yyyy i.e. numbers.
    private static let months = ["01-January", "02-February", "03-March", "04-April", "05-May", "06-June", "07-July", "08-August", "09-September", "10-October", "11-November", "12-December"]
    
    private var creditCardNumber = ""
    private var securityCode = ""
    private var expiryDate = ""
    private var zip = ""
    private var storePaymentInfo = false
    var appliedOfferId : String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        let creditCardInputCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 0, inSection: 0)) as? PaymentInfoInputTableViewCell
        creditCardInputCell?.textField.becomeFirstResponder()
        
        barBackButton.accessibilityActivate()
        barBackButton.accessibilityTraits = UIAccessibilityTraitButton
        barBackButton.accessibilityIdentifier = "Enter Credit Card Back Button"
    }
    
    @IBAction func textFieldEditingChanged(sender: UITextField) {
        switch sender.tag {
        case 0: creditCardNumber = sender.text?.stringByRemovingNonNumericCharacters() ?? ""
        case 1: securityCode = sender.text ?? ""
        case 2: expiryDate = sender.text ?? ""
        case 3: zip = sender.text ?? ""
        default: assert(false, "unexpected tag encountered")
        }
    }
    
    @IBAction func valueChanged(sender: UISwitch) {
        storePaymentInfo = sender.on
    }
    //MARK: TableView DateSource & Delegate
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //Store Payment Info only makes sense in case of logged in user.
        if UserService.sharedUser != nil {
            return 5
        }
        else {
            return 4
        }
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell: UITableViewCell!
        if indexPath.row == 4 {
            let switchCell = tableView.dequeueReusableCellWithIdentifier("PaymentInfoSwitchTableViewCell", forIndexPath: indexPath) as! PaymentInfoSwitchTableViewCell
            switchCell.switchControl.on = storePaymentInfo
            cell = switchCell
        }
        else {
            let inputCell = tableView.dequeueReusableCellWithIdentifier("PaymentInfoInputTableViewCell", forIndexPath: indexPath) as! PaymentInfoInputTableViewCell
            switch indexPath.row {
            case 0:
                inputCell.textField.placeholder = "Card Number"
                inputCell.textField.text = creditCardNumber
                //inputCell.textField.becomeFirstResponder()
            case 1:
                inputCell.textField.placeholder = "Security Code"
                inputCell.textField.text = securityCode
            case 2:
                inputCell.textField.placeholder = "Expiration Date(mm/yyyy)"
                inputCell.textField.text = expiryDate
                setupPickerOnInputTableViewCell(inputCell)
            case 3:
                inputCell.textField.placeholder = "ZIP"
                inputCell.textField.text = zip
                
            default: assert(false, "Unexpected row ecountered")
            }
            inputCell.textField.tag = indexPath.row
            inputCell.textField.delegate = self
            cell = inputCell
        }
        return cell
    }
    
    func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return "Credit Card Information"
    }
    
    func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont(name: Constants.archerMedium, size: 18)
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if indexPath.row == 4 { // Store Payment Info with switch
            return 60
        }
        return tableView.rowHeight
    }
    
    //MARK: ---
    
    func setupPickerOnInputTableViewCell(inputCell: PaymentInfoInputTableViewCell) {
        let pickerView = UIPickerView()
        pickerView.delegate = self
        pickerView.dataSource = self
        inputCell.textField.inputView = pickerView
        inputCell.tintColor = UIColor.clearColor()
        //Set field for the first time
        setMonthRowInitially(pickerView)
    }
    
    func setMonthRowInitially(pickerView: UIPickerView) {
        let currentMonth = NSDate.currentMonthOfYearInGregorianCalendar()
        pickerView.selectRow(currentMonth - 1, inComponent: 0, animated: false)
    }
    
    func selectionChanged(pickerView: UIPickerView) {
        let inputCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 2, inSection: 0)) as! PaymentInfoInputTableViewCell
        updateExpiryTimeInInputTableViewCell(inputCell, pickerView: pickerView)
    }
    
    func updateExpiryTimeInInputTableViewCell(inputCell: PaymentInfoInputTableViewCell, pickerView: UIPickerView) {
        let selectedMonth = pickerView.selectedRowInComponent(0) + 1
        let currentYear = NSDate.currentYearInGregorianCalendar()
        let selectedYear = pickerView.selectedRowInComponent(1) + currentYear
        expiryDate = String(format: "%02d/%04d", arguments: [selectedMonth, selectedYear])
        inputCell.textField.text = expiryDate
    }
    
    //MARK: PickerView DataSource & Delegate
    
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if component == 0 {
            return 12 // 12 months
        } else {
            return Constants.maxCreditCardExpiryYears // Number of years from current year
        }
    }
    
    func pickerView(pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        selectionChanged(pickerView)
    }
    
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if component == 0 {
            return EnterCreditCardViewController.months[row]
        }
        else {
            let currentYear = NSDate.currentYearInGregorianCalendar()
            return "\(row + currentYear)"
        }
    }
    
    //MARK: ---
    
    
    @IBAction func placeOrder(sender: UIButton) {
        trackButtonPress(sender)
        
        if validateFields() {
            sender.enabled = false
            view.endEditing(true)
            //Get Months and Years as Int64
            let monthYearComps = expiryDate.componentsSeparatedByString("/")
            let expiryMonth = monthYearComps[0] ?? "00"
            let expiryYear = monthYearComps[1] ?? "00"
            
            //This is critical so lets show HUD
            SVProgressHUD.showWithStatus("Processing order...", maskType: .Clear)
            BasketService.placeOrderWithCreditCard(creditCardNumber, securityCode: securityCode, zip: zip, expiryMonth: expiryMonth, expiryYear: expiryYear, savePaymentInfo: storePaymentInfo) { (orderStatus, error) -> Void in
                SVProgressHUD.dismiss()
                sender.enabled = true
                if error != nil {
                    
                    clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")

                    self.presentError(error)
                    return
                }
                (self.navigationController as! BasketNavigationController).dismiss()
                
                
                if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                {clpAnalyticsService.sharedInstance.clpTrackScreenView("SUBMIT_ORDER")
                }
                
                // Get the offers after push notification
                
                let defaults = NSUserDefaults.standardUserDefaults()
                if let customerId = defaults.stringForKey("Newmemberid")
                {
                ClpOfferService.redeemedOffer(customerId,offerId: self.appliedOfferId){ (offerRedeemStatus : String?, error) -> Void in
                    SVProgressHUD.dismiss()
                    
                    if error != nil {
                        self.presentError(error)
                        return
                    }
                    print("joe** response is here \(offerRedeemStatus)")
                    
                }
                
            }

            
                
                
                // TODO: Home Controller should listen to this to make get recent orders call (no need to pass order status here)
                NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.OrderPlaced.rawValue, object: self, userInfo: nil)
                
                BasketService.lastOrderStatus = orderStatus!
                let vc = UIViewController.instantiate("OrderConfirmationVC", storyboard: "Main") as! OrderConfirmationViewController
                self.view.window!.addSubview(vc.view)
            }
        }
    }
    
    func validateFields() -> Bool {
        view.endEditing(true)
        
        // Validate card number name
        let cardNumberCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 0, inSection: 0)) as? PaymentInfoInputTableViewCell
        if creditCardNumber.trim().isEmpty {
            presentOkAlert("Card number required", message: "Please enter a credit card number") {
                cardNumberCell?.textField.becomeFirstResponder()
            }
            return false
        }
        //
        if !creditCardNumber.trim().isCreditCardNumber() {
            presentOkAlert("Invalid card number", message: "Please enter a valid credit card number") {
                cardNumberCell?.textField.becomeFirstResponder()
            }
            return false
        }
        
        // Validate security code
        let securityCodeCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 1, inSection: 0)) as? PaymentInfoInputTableViewCell
        if securityCode.trim().isEmpty {
            presentOkAlert("Security code required", message: "Please enter a security code") {
                securityCodeCell?.textField.becomeFirstResponder()
            }
            return false
        }
        //
        if !securityCode.trim().isSecurityCode() {
            presentOkAlert("Invalid security code", message: "Please enter a valid security code") {
                securityCodeCell?.textField.becomeFirstResponder()
            }
            return false
        }
        
        //Validate Expiration Date
        let expiryDateCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 2, inSection: 0)) as? PaymentInfoInputTableViewCell
        if expiryDate.trim().isEmpty {
            presentOkAlert("Expiration date required", message: "Please enter an expiration date") {
                expiryDateCell?.textField.becomeFirstResponder()
            }
            return false
        }
        if !expiryDate.trim().isValidCreditCardExpiry() {
            presentOkAlert("Invalid expiration date", message: "Please enter a valid expiration date in format mm/yyyy") {
                expiryDateCell?.textField.becomeFirstResponder()
            }
            return false
        }
        
        //Validate Month and Year
        var expirationDateValid = false
        let components = expiryDate.componentsSeparatedByString("/")
        if components.count == 2 {
            let month = Int(components[0])
            let year = Int(components[1])
            if month != nil && year != nil {
                let currentMonth = NSDate.currentMonthOfYearInGregorianCalendar()
                let currentYear = NSDate.currentYearInGregorianCalendar()
                //This is possible in case of pasting
                if year > currentYear {
                    expirationDateValid = true
                }
                else if year == currentYear && month >= currentMonth {
                    expirationDateValid = true
                }
            }
        }
        if !expirationDateValid {
            presentOkAlert("Invalid expiration date", message: "The expiration date should be in future") {
                expiryDateCell?.textField.becomeFirstResponder()
            }
            return false
        }
        
        // Validate zip code
        let zipCodeCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 3, inSection: 0)) as? PaymentInfoInputTableViewCell
        if zip.trim().isEmpty {
            presentOkAlert("Zip code required", message: "Please enter a zip code") {
                zipCodeCell?.textField.becomeFirstResponder()
            }
            return false
        }
        //
        if !zip.trim().isZipCode() {
            presentOkAlert("Invalid zip code", message: "Please enter a valid zip code") {
                zipCodeCell?.textField.becomeFirstResponder()
            }
            return false
        }
        return true
    }
    
    //MARK: Text Field Delegate
    
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        if textField.tag == 0 { //CreditCard
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.creditCardNumberInputLimit)
        }
        else if textField.tag == 1 { //SecurityCode
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.cvvInputLimit)
        }
        else if textField.tag == 2 { //ExpirationDate
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.expirationDateInputLimit)
        }
        else if textField.tag == 3 { //Zip
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.zipCodeInputLimit)
        }
        return true
    }
    
}
