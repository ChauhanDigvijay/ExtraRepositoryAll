//
//  EnterCreditCardViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 03/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class EnterCreditCardViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UIPickerViewDelegate, UIPickerViewDataSource, UITextFieldDelegate, CardIOPaymentViewControllerDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var barBackButton: UIBarButtonItem!
    
    
    //Adding month number before name as credit cards mostly have expiration date in form of mm/yyyy i.e. numbers.
    private static let months = ["01-January", "02-February", "03-March", "04-April", "05-May", "06-June", "07-July", "08-August", "09-September", "10-October", "11-November", "12-December"]
    
    private var creditCardNumber = ""
    private var securityCode = ""
    private var expiryDate = ""
    private var zip = ""
    private var storePaymentInfo = false
    private var creditCardNumberInputLimitWithoutDashes = 5
    
    private var scannedExpiryMonth: UInt = 0
    private var scannedExpiryYear: UInt = 0
    
    var appliedOfferId : String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 60
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        //let creditCardInputCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 0, inSection: 0)) as? PaymentInfoInputTableViewCell
        //creditCardInputCell?.textField.becomeFirstResponder()
        
        barBackButton.accessibilityActivate()
        barBackButton.accessibilityTraits = UIAccessibilityTraitButton
        barBackButton.accessibilityIdentifier = "Enter Credit Card Back Button"
        
        // CardIO preload utilities
        CardIOUtilities.preload()
    }
    
    @IBAction func textFieldEditingChanged(sender: UITextField) {
        switch sender.tag {
        case 0: creditCardNumber = formatCreditCardNumber(sender) ?? ""
        case 1: securityCode = sender.text ?? ""
        case 2: expiryDate = sender.text ?? ""
        case 3: zip = sender.text ?? ""
        default: assert(false, "unexpected tag encountered")
        }
    }
    
    @IBAction func valueChanged(sender: UISwitch) {
        storePaymentInfo = sender.on
    }
    //Disable gift card
    //MARK: Check the gift card billing scheme
    //    func checkGiftCardBillingSchemeStatus()->Bool{
    //        var status = false
    //        if let billingSchemes = BasketService.sharedBasket!.billingSchemes{
    //            for billingScheme in billingSchemes {
    //                if billingScheme.type == "giftcard"{
    //                    status = true
    //                    break
    //                }
    //            }
    //            return status
    //        }
    //        else{
    //            return status
    //        }
    //    }
    
    //MARK: TableView DateSource & Delegate
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //Store Payment Info only makes sense in case of logged in user.
        if UserService.sharedUser != nil {
            if section == 0{
                return 5
            }
            else{
                return 1
            }
        }
        else {
            return 4
        }
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int{
        if UserService.sharedUser != nil{
            //Disable gift card
            //            if checkGiftCardBillingSchemeStatus(){
            //                return 2
            //            }
            //            else{
            return 1
            //            }
            
        }
        else{
            return 1
        }
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell: UITableViewCell!
        if indexPath.section == 1{
            return tableView.dequeueReusableCellWithIdentifier("PaymentInfoNavigationTableViewCell", forIndexPath: indexPath)
        }
        else{
            if indexPath.row == 4 {
                let switchCell = tableView.dequeueReusableCellWithIdentifier("PaymentInfoSwitchTableViewCell", forIndexPath: indexPath) as! PaymentInfoSwitchTableViewCell
                switchCell.switchControl.on = storePaymentInfo
                cell = switchCell
            }
            else {
                let inputCell = tableView.dequeueReusableCellWithIdentifier("PaymentInfoInputTableViewCell", forIndexPath: indexPath) as! PaymentInfoInputTableViewCell
                switch indexPath.row {
                case 0:
                    inputCell.setData("Card Number", value: creditCardNumber)
                    formatCreditCardNumber(inputCell.textField)
                case 1:
                    inputCell.setData("Security Code", value: securityCode)
                case 2:
                    inputCell.setData("Expiration Date(mm/yyyy)", value: expiryDate)
                    setupPickerOnInputTableViewCell(inputCell)
                case 3:
                    inputCell.setData("ZIP", value: zip)
                    
                default: assert(false, "Unexpected row ecountered")
                }
                inputCell.textField.tag = indexPath.row
                inputCell.textField.delegate = self
                cell = inputCell
            }
            return cell
        }
        
    }
    
    func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if section == 0{
            return "Credit Card Information"
        }
        else{
            return "(OR) Pay with Jamba Card"
        }
    }
    
    
    
    func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 18)
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if indexPath.row == 4 { // Store Payment Info with switch
            return 110
        }
        else if indexPath.section == 1{ // Gift Card Info
            return 60
        }
        return tableView.rowHeight
    }
    
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if indexPath.section == 1{
            self.view.endEditing(true)
            self.performSegueWithIdentifier("GiftCardList", sender: self)
            tableView.deselectRowAtIndexPath(indexPath, animated: false)
        }
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
        let expiryYear = Int(scannedExpiryYear)
        let expiryMonth = Int(scannedExpiryMonth)
        if expiryYear != 0 && expiryMonth != 0 {
            let currentYear = NSDate.currentYearInGregorianCalendar()
            pickerView.selectRow(expiryMonth-1, inComponent: 0, animated: false)
            if expiryYear >= currentYear{
                pickerView.selectRow(expiryYear-currentYear, inComponent: 1, animated: false)
            }
            updateExpiryTimeInInputTableViewCell(inputCell,pickerView:pickerView )
            scannedExpiryYear = 0
            scannedExpiryMonth = 0
        }
    }
    
    func setMonthRowInitially(pickerView: UIPickerView) {
        let currentMonth = NSDate.currentMonthOfYearInGregorianCalendar()
        pickerView.selectRow(currentMonth - 1, inComponent: 0, animated: false)
    }
    
    func selectionChanged(pickerView: UIPickerView) {
        if  let inputCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: 2, inSection: 0)) as? PaymentInfoInputTableViewCell{
            updateExpiryTimeInInputTableViewCell(inputCell, pickerView: pickerView)
        }
        
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
                
                //                let defaults = NSUserDefaults.standardUserDefaults()
                //                if let customerId = defaults.stringForKey("CustomerId")
                //                {
                //                    ClpOfferService.redeemedOffer(customerId,offerId: self.appliedOfferId){ (offerRedeemStatus : String?, error) -> Void in
                //                        SVProgressHUD.dismiss()
                //
                //                        if error != nil {
                //                            self.presentError(error)
                //                            return
                //                        }
                //                        print("joe** response is here \(offerRedeemStatus)")
                //
                //                    }
                
                //                }
                
                
                
                
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
            return true
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
    
    func formatCreditCardNumber(textField: UITextField) -> String {
        var creditCardNumber = textField.text!
        
        if textField.text?.length >= Constants.creditCardNumberInputLimit {     //check maximum limit
            let charIndex = textField.text!.startIndex.advancedBy(Constants.creditCardNumberInputLimit - 1)
            creditCardNumber = textField.text!.substringToIndex(charIndex)
        }
        creditCardNumber = creditCardNumber.stringByRemovingNonNumericCharacters()
        
        //set credit card image based on credit card number
        setCreditCardImage(creditCardNumber, index: textField.tag)
        
        //format credit card number
        if (creditCardNumber.length > 0 ) {
            var formattedCreditcard = "";
            for index in 0...creditCardNumber.length-1 {
                if (index % 4==0 && index > 0) {
                    let charIndex = creditCardNumber.startIndex.advancedBy(index)
                    formattedCreditcard += "-" + String(creditCardNumber[charIndex])
                } else {
                    let charIndex = creditCardNumber.startIndex.advancedBy(index)
                    formattedCreditcard += String(creditCardNumber[charIndex])
                }
            }
            textField.text = formattedCreditcard
            return formattedCreditcard
        } else {
            textField.text = creditCardNumber
        }
        return creditCardNumber
    }
    
    //set credit card image
    func setCreditCardImage(creditCardNumber:String, index:Int) {
        let cell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: index, inSection: 0))
        if let cellType = cell as? PaymentInfoInputTableViewCell {
            cellType.setCreditCardImage(creditCardNumber)
        }
    }
    
    //check for valid credit card
    func isValidCreditCard(creditCardNumber:String) -> Bool {
        let cardRegEx = String(format: "[0-9]{0,%d}?", Constants.creditCardNumberInputLimit - creditCardNumberInputLimitWithoutDashes)
        
        let cardTest = NSPredicate(format:"SELF MATCHES %@", cardRegEx)
        return cardTest.evaluateWithObject(creditCardNumber)
    }
    
    // Action for camera button pressed
    @IBAction func scanCreditCard(sender:UIButton){
        let cardIOVC = CardIOPaymentViewController(paymentDelegate: self)
        cardIOVC.modalPresentationStyle = .FormSheet
        cardIOVC.disableManualEntryButtons = true
        cardIOVC.collectCVV = false
        cardIOVC.collectExpiry = false
        cardIOVC.scanExpiry = true
        
        presentViewController(cardIOVC, animated: true, completion: nil)
    }
    
    func userDidCancelPaymentViewController(paymentViewController: CardIOPaymentViewController!) {
        paymentViewController.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func userDidProvideCreditCardInfo(cardInfo: CardIOCreditCardInfo!, inPaymentViewController paymentViewController: CardIOPaymentViewController!) {
        // Card info
        if let info = cardInfo{
            creditCardNumber = info.cardNumber
            scannedExpiryMonth = info.expiryMonth
            scannedExpiryYear = info.expiryYear
        }
        paymentViewController.dismissViewControllerAnimated(true) {
            self.tableView.reloadData()
        }
    }
    
}
