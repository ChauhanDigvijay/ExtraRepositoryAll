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
    fileprivate static let months = ["01-January", "02-February", "03-March", "04-April", "05-May", "06-June", "07-July", "08-August", "09-September", "10-October", "11-November", "12-December"]
    
    fileprivate var creditCardNumber = ""
    fileprivate var securityCode = ""
    fileprivate var expiryDate = ""
    fileprivate var zip = ""
    fileprivate var storePaymentInfo = false
    fileprivate var creditCardNumberInputLimitWithoutDashes = 5
    
    fileprivate var scannedExpiryMonth: UInt = 0
    fileprivate var scannedExpiryYear: UInt = 0
    
    var appliedOfferId : String = ""
    
    var selectedMonthInCurrentYear = 0
    var selectedCurrentYear = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 60
    }
    
    override func viewDidAppear(_ animated: Bool) {
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
    
    @IBAction func textFieldEditingChanged(_ sender: UITextField) {
        switch sender.tag {
        case 0: creditCardNumber = formatCreditCardNumber(sender) 
        case 1: securityCode = sender.text ?? ""
        case 2: expiryDate = sender.text ?? ""
        case 3: zip = sender.text ?? ""
        default: assert(false, "unexpected tag encountered")
        }
    }
    
    @IBAction func valueChanged(_ sender: UISwitch) {
        storePaymentInfo = sender.isOn
    }
    //Disable gift card
    //MARK: Check the gift card billing scheme
        func checkGiftCardBillingSchemeStatus()->Bool{
            var status = false
            if let billingSchemes = BasketService.sharedBasket!.billingSchemes{
                for billingScheme in billingSchemes {
                    if billingScheme.type == "giftcard"{
                        status = true
                        break
                    }
                }
                return status
            }
            else{
                return status
            }
        }
    
    //MARK: TableView DateSource & Delegate
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
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
    
    func numberOfSections(in tableView: UITableView) -> Int{
        if UserService.sharedUser != nil{
            //Disable gift card
                        if checkGiftCardBillingSchemeStatus(){
                            return 2
                        }
                        else{
            return 1
                        }
            
        }
        else{
            return 1
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: UITableViewCell!
        if indexPath.section == 1{
            return tableView.dequeueReusableCell(withIdentifier: "PaymentInfoNavigationTableViewCell", for: indexPath)
        }
        else{
            if indexPath.row == 4 {
                let switchCell = tableView.dequeueReusableCell(withIdentifier: "PaymentInfoSwitchTableViewCell", for: indexPath) as! PaymentInfoSwitchTableViewCell
                switchCell.switchControl.isOn = storePaymentInfo
                cell = switchCell
            }
            else {
                let inputCell = tableView.dequeueReusableCell(withIdentifier: "PaymentInfoInputTableViewCell", for: indexPath) as! PaymentInfoInputTableViewCell
                switch indexPath.row {
                case 0:
                    inputCell.setData("Card Number", value: creditCardNumber)
                    _ = formatCreditCardNumber(inputCell.textField)
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
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if section == 0{
            return "Credit Card Information"
        }
        else{
            return "(OR) Pay with Jamba Card"
        }
    }
    
    
    
    func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 18)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 4 { // Store Payment Info with switch
            return 110
        }
        else if indexPath.section == 1{ // Gift Card Info
            return 60
        }
        return tableView.rowHeight
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 1{
            self.view.endEditing(true)
            self.performSegue(withIdentifier: "GiftCardList", sender: self)
            tableView.deselectRow(at: indexPath, animated: false)
        }
    }
    
    //MARK: ---
    
    func setupPickerOnInputTableViewCell(_ inputCell: PaymentInfoInputTableViewCell) {
        let pickerView = UIPickerView()
        pickerView.delegate = self
        pickerView.dataSource = self
        inputCell.textField.inputView = pickerView
        inputCell.tintColor = UIColor.clear
        //Set field for the first time
        setMonthRowInitially(pickerView)
        let expiryYear = Int(scannedExpiryYear)
        let expiryMonth = Int(scannedExpiryMonth)
        if expiryYear != 0 && expiryMonth != 0 {
            let currentYear = Date.currentYearInGregorianCalendar()
            pickerView.selectRow(expiryMonth-1, inComponent: 0, animated: false)
            if expiryYear >= currentYear{
                pickerView.selectRow(expiryYear-currentYear, inComponent: 1, animated: false)
            }
            updateExpiryTimeInInputTableViewCell(inputCell,pickerView:pickerView )
            scannedExpiryYear = 0
            scannedExpiryMonth = 0
        }
    }
    
    func setMonthRowInitially(_ pickerView: UIPickerView) {
        let currentMonth = Date.currentMonthOfYearInGregorianCalendar()
        pickerView.selectRow(0, inComponent: 0, animated: false)
        selectedMonthInCurrentYear = currentMonth
        selectedCurrentYear = true
    }
    
    func selectionChanged(_ pickerView: UIPickerView) {
        if  let inputCell = tableView.cellForRow(at: IndexPath(row: 2, section: 0)) as? PaymentInfoInputTableViewCell{
            updateExpiryTimeInInputTableViewCell(inputCell, pickerView: pickerView)
        }
        
    }
    
    func updateExpiryTimeInInputTableViewCell(_ inputCell: PaymentInfoInputTableViewCell, pickerView: UIPickerView) {
        //if the current year is selected, then get the user month
        //eg current month is may
        //list [may, june, july, August, september, october, november, december] count(8)
        //if user selcts september then picker selecter row is 4
        //to get 9 (totalMonth(12)-listedItems(8) +userSelectedRow(4)+1) = 9
        let selectedMonth = (EnterCreditCardViewController.months.count - pickerView.numberOfRows(inComponent: 0)) + pickerView.selectedRow(inComponent: 0) + 1
        let currentYear = Date.currentYearInGregorianCalendar()
        let selectedYear = pickerView.selectedRow(inComponent: 1) + currentYear
        expiryDate = String(format: "%02d/%04d", arguments: [selectedMonth, selectedYear])
        inputCell.textField.text = expiryDate
        
        //Check the user
        selectedCurrentYear = (pickerView.selectedRow(inComponent: 1) == 0)
        pickerView.reloadComponent(0)
    }
    
    //MARK: PickerView DataSource & Delegate
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if component == 0 {
            // if current year is selected remove previous month
            if selectedCurrentYear {
                return 12 - selectedMonthInCurrentYear + 1
            } else {
                return 12 // 12 months
            }
            //list all months
        } else {
            return Constants.maxCreditCardExpiryYears // Number of years from current year
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        selectionChanged(pickerView)
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if component == 0 {
            // if current year is selected remove previous month
            if selectedCurrentYear {
                return EnterCreditCardViewController.months[selectedMonthInCurrentYear + row - 1]
                //list all months
            } else {
                return EnterCreditCardViewController.months[row]
            }
            
        }
        else {
            let currentYear = Date.currentYearInGregorianCalendar()
            return "\(row + currentYear)"
        }
    }
    
    //MARK: ---
    
    
    @IBAction func placeOrder(_ sender: UIButton) {
        // Fishbowl Event
        var basketId = ""
        if let basket = BasketService.sharedBasket{
            basketId = "\(basket.id)"
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent(basketId, item_name: "", event_name: "SUBMIT_ORDER")
        trackButtonPress(sender)
        
        if validateFields() {
            sender.isEnabled = false
            view.endEditing(true)
            //Get Months and Years as Int64
            let monthYearComps = expiryDate.components(separatedBy: "/")
            let expiryMonth = monthYearComps[0] 
            let expiryYear = monthYearComps[1] 
            
            //This is critical so lets show HUD
            SVProgressHUD.show(withStatus: "Processing order...")
            SVProgressHUD.setDefaultMaskType(.clear)
            BasketService.placeOrderWithCreditCard(creditCardNumber, securityCode: securityCode, zip: zip, expiryMonth: expiryMonth, expiryYear: expiryYear, savePaymentInfo: storePaymentInfo) { (orderStatus, error) -> Void in
                SVProgressHUD.dismiss()
                sender.isEnabled = true
                if error != nil {
                    self.presentError(error)
                    return
                }
                (self.navigationController as! BasketNavigationController).dismiss()
                
                // TODO: Home Controller should listen to this to make get recent orders call (no need to pass order status here)
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.OrderPlaced.rawValue), object: self, userInfo: nil)
                
                BasketService.lastOrderStatus = orderStatus!
                let vc = UIViewController.instantiate("OrderConfirmationVC", storyboard: "Main") as! OrderConfirmationViewController
                self.view.window!.addSubview(vc.view)
            }
        }
    }
    
    func validateFields() -> Bool {
        view.endEditing(true)
        
        // Validate card number name
        let cardNumberCell = tableView.cellForRow(at: IndexPath(row: 0, section: 0)) as? PaymentInfoInputTableViewCell
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
        let securityCodeCell = tableView.cellForRow(at: IndexPath(row: 1, section: 0)) as? PaymentInfoInputTableViewCell
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
        let expiryDateCell = tableView.cellForRow(at: IndexPath(row: 2, section: 0)) as? PaymentInfoInputTableViewCell
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
        let components = expiryDate.components(separatedBy: "/")
        if components.count == 2 {
            let month = Int(components[0])
            let year = Int(components[1])
            if month != nil && year != nil {
                let currentMonth = Date.currentMonthOfYearInGregorianCalendar()
                let currentYear = Date.currentYearInGregorianCalendar()
                //This is possible in case of pasting
                if year! > currentYear {
                    expirationDateValid = true
                }
                else if year! == currentYear && month! >= currentMonth {
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
        let zipCodeCell = tableView.cellForRow(at: IndexPath(row: 3, section: 0)) as? PaymentInfoInputTableViewCell
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
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
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
    
    func formatCreditCardNumber(_ textField: UITextField) -> String {
        
        var creditCardNumber = textField.text!
        creditCardNumber = creditCardNumber.stringByRemovingNonNumericCharacters()
        
        //set credit card image based on credit card number
        setCreditCardImage(creditCardNumber, index: textField.tag)
        textField.text = creditCardNumber.formatCreditCardNumber()
        return textField.text ?? ""
    }
    
    //set credit card image
    func setCreditCardImage(_ creditCardNumber:String, index:Int) {
        let cell = tableView.cellForRow(at: IndexPath(row: index, section: 0))
        if let cellType = cell as? PaymentInfoInputTableViewCell {
            cellType.setCreditCardImage(creditCardNumber)
        }
    }
    
    //check for valid credit card
    func isValidCreditCard(_ creditCardNumber:String) -> Bool {
        let cardRegEx = String(format: "[0-9]{0,%d}?", Constants.creditCardNumberInputLimit - creditCardNumberInputLimitWithoutDashes)
        
        let cardTest = NSPredicate(format:"SELF MATCHES %@", cardRegEx)
        return cardTest.evaluate(with: creditCardNumber)
    }
    
    // Action for camera button pressed
    @IBAction func scanCreditCard(_ sender:UIButton){
        let cardIOVC = CardIOPaymentViewController(paymentDelegate: self)
        cardIOVC?.modalPresentationStyle = .formSheet
        cardIOVC?.disableManualEntryButtons = true
        cardIOVC?.collectCVV = false
        cardIOVC?.collectExpiry = false
        cardIOVC?.scanExpiry = true
        
        present(cardIOVC!, animated: true, completion: nil)
    }
    
    func userDidCancel(_ paymentViewController: CardIOPaymentViewController!) {
        paymentViewController.dismiss(animated: true, completion: nil)
    }
    
    func userDidProvide(_ cardInfo: CardIOCreditCardInfo!, in paymentViewController: CardIOPaymentViewController!) {
        // Card info
        if let info = cardInfo{
            creditCardNumber = info.cardNumber
            scannedExpiryMonth = info.expiryMonth
            scannedExpiryYear = info.expiryYear
        }
        paymentViewController.dismiss(animated: true) {
            self.tableView.reloadData()
        }
    }
    
}

extension String {
    func formatCreditCardNumber() -> String {
        var creditCardNumber = self
        
        if (self.length) >= Constants.creditCardNumberInputLimit {     //check maximum limit
            let charIndex = self.characters.index(self.startIndex, offsetBy: Constants.creditCardNumberInputLimit - 1)
            creditCardNumber = String(self[..<charIndex])
        }
        creditCardNumber = creditCardNumber.stringByRemovingNonNumericCharacters()
        
        //format credit card number
        if (creditCardNumber.characters.count > 0 ) {
            var formattedCreditcard = "";
            for index in 0..<creditCardNumber.characters.count {
                if (index % 4==0 && index > 0) {
                    let charIndex = creditCardNumber.index(creditCardNumber.startIndex, offsetBy: index)
                    formattedCreditcard += "-" + String(creditCardNumber[charIndex])
                } else {
                    let charIndex = creditCardNumber.index(creditCardNumber.startIndex, offsetBy: index)
                    formattedCreditcard += String(creditCardNumber[charIndex])
                }
            }
            return formattedCreditcard
        }
        return creditCardNumber
    }
}
