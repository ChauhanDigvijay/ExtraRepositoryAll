//
//  AutoReloadSettingsViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 10/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke
import SVProgressHUD

protocol AutoReloadSettingsViewControllerDelegate: class {
    func updateAutoReloadDetails(inCommAutoReloadSavable:InCommAutoReloadSavable?, inCommUserPaymentAccount:InCommUserPaymentAccount?)
}

class AutoReloadSettingsViewController: UIViewController, UITableViewDelegate, PickerViewControllerDelegate, DatePickerViewControllerDelegate,AmountSelectionViewControllerDelegate, EndDateSelectionViewControllerDelegate, AddPaymentViewControllerDelegate, PaymentViewControllerPassObjectDelegate, UITextFieldDelegate{
    
    //field name
    enum fieldName : String {
        case autoReload     = "Auto Reload"
        case reloadAmount   = "Select Reload Amount"
        case refillBalance  = "When balance is below"
        case onASpecific    = "On a specific"
        case startDate      = "Start Date"
        case endDate        = "End Date"
        case paymentAccount = "Payment Account"
        case infoMessage    = "Purchaser Details"
    }
    
    @IBOutlet weak var tableView                : UITableView!
    @IBOutlet weak var tableViewBottomSpace     : NSLayoutConstraint!
    @IBOutlet weak var customPickerView         : UIView!
    @IBOutlet weak var datePickerView           : UIView!
    @IBOutlet weak var autoReloadSwitch         : UISwitch!
    @IBOutlet weak var saveButton               : UIButton!
    @IBOutlet weak var deleteButton             : UIButton!
    var inCommAutoReloadSavable                 : InCommAutoReloadSavable?  // Auto reload config details
    var inCommUserPaymentAccount                : InCommUserPaymentAccount? // InComm user payment account
    var inCommAutoReloadAssociatePaymentAccount : InCommUserPaymentAccount? // Auto reload associate payment account
    var inCommUserGiftCardId:Int32!
    var inCommUserCard:InCommUserGiftCard{
        get {
            return GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserGiftCardId)!
        }
    }
    private var pickerViewController            : PickerViewController?
    private var datePickerViewController        : DatePickerViewController?
    weak var delegate                           : AutoReloadSettingsViewControllerDelegate?
    var onASpecificPickerData                   : [String] = ["Weekly","Monthly","Yearly"]
    var giftCardBrand:InCommBrand               = InCommGiftCardBrandDetails.sharedInstance.brand!
    var autoReloadDataFields                    :[AutoReloadSettingsDataModel] = []
    //Auto reload config values
    var amount                                  : Double!
    var giftCardId                              : Int32!
    var minimumBalance                          : Double?
    var paymentAccountId                        : Int32?
    var startsOn                                : NSDate!
    var endsOn                                  : NSDate?
    var numberOfOccurancesRemaining             : Int16?
    var never                                   : Bool?
    var reloadFrequencyId                       : InCommReloadFrequencyType!
    var savePaymentDetails                      : InCommUserPaymentAccountDetails?  //Save payment account details
    let dateFormatter                           = NSDateFormatter()
    var operationDoneByUser                     :Bool   = false         //store user done any changes or not
    
    override func viewDidLoad() {
        operationDoneByUser = false     //initially the user didn't perform any challenge
        //dateformat
        dateFormatter.dateFormat = GiftCardAppConstants.ShortDateFormat
        
        giftCardId = inCommUserCard.cardId
        
        datePickerView.hidden = true
        customPickerView.hidden = true
        initializeObject()
        fillAutoReloadValues()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(AutoReloadSettingsViewController.keyBoardWillShown(_:)), name: UIKeyboardWillShowNotification, object: nil)
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector:#selector(AutoReloadSettingsViewController.keyBoardWillHidden(_:)), name: UIKeyboardWillHideNotification, object: nil)
        
        super.viewDidLoad()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "showPaymentListVC" {
            let targetController =  segue.destinationViewController as! PaymentListViewController
            targetController.headerTitle = GiftCardAppConstants.addNewPaymentTitle
            
            targetController.cardAmount = amount
            targetController.enableSavePayment = true
            targetController.userNewlyAddedCreditCard = GiftCardCreationService.sharedInstance.paymentDetails
            targetController.userSelectedCard = GiftCardCreationService.sharedInstance.paymentDetailsId
            targetController.userSavedPaymentDetails = GiftCardCreationService.sharedInstance.savePaymentDetails
            targetController.enableSavePayment = false
            targetController.addPaymentDetailDelegate = self
            targetController.paymentDetailDelegate = self
        } else if segue.identifier == "ShowPickerVC" {
            pickerViewController = segue.destinationViewController as? PickerViewController
            pickerViewController?.delegate = self
        } else if segue.identifier == "ShowDatePickerVC" {
            datePickerViewController = segue.destinationViewController as? DatePickerViewController
            datePickerViewController?.delegate = self
        } else if segue.identifier == "ShowAmountSelectionVC" {
            let targetController = segue.destinationViewController as! AmountSelectionViewController
            targetController.userSelectedAmount = autoReloadDataFields[fieldName.reloadAmount.hashValue].field_value
            targetController.delegate = self
        } else if segue.identifier == "ShowEndSelectionVC" {
            let targetController = segue.destinationViewController as! EndDateSelectionViewController
            targetController.SelectedValue = autoReloadDataFields[fieldName.endDate.hashValue].field_value
            targetController.startDate = autoReloadDataFields[fieldName.startDate.hashValue].field_value
            targetController.delegate = self
        } else if segue.identifier == "ShowPaymentVC" {
            let targetController = segue.destinationViewController as! PaymentViewController
            targetController.creditCardDetail = inCommUserPaymentAccount
            targetController.cardTypeImageURL = autoReloadDataFields[fieldName.paymentAccount.hashValue].field_image
            targetController.screenMode = GiftCardAppConstants.PaymentScreenMode.ViewOnly.rawValue
        }
    }
    
    
    
    
    
    // MARK: - Table view delegates
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return autoReloadDataFields.count
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        let fieldData:AutoReloadSettingsDataModel = autoReloadDataFields[indexPath.row]
        if (fieldData.name == fieldName.refillBalance.rawValue) {
            return 2 * GiftCardAppConstants.tableViewCellHeight
        }
        return GiftCardAppConstants.tableViewCellHeight
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let fieldData:AutoReloadSettingsDataModel = autoReloadDataFields[indexPath.row]
        
        //cell creation for switch
        if (fieldData.name == fieldName.autoReload.rawValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("AutoReloadSwitchTableViewCell", forIndexPath: indexPath) as! AutoReloadSwitchTableViewCell
            
            cell.setCellData(fieldData, incommCard: inCommUserCard)
            return cell
            
            //cell creation for refill balance
        } else if (fieldData.name == fieldName.refillBalance.rawValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("AutoReloadWithoutValueCell", forIndexPath: indexPath) as! AutoReloadWithoutValueCell
            
            cell.amountTextField.delegate = self
            cell.setCellData(fieldData, switchState: autoReloadDataFields[fieldName.autoReload.hashValue].switchState, incommCard:  inCommUserCard)
            
            return cell
            
            //cell creation for specific week selection balance
        } else if (fieldData.name == fieldName.onASpecific.rawValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("AutoReloadWithValueCell", forIndexPath: indexPath) as! AutoReloadWithValueCell
            
            cell.setCellData(fieldData, switchState: autoReloadDataFields[fieldName.autoReload.hashValue].switchState, incommCard:  inCommUserCard)
            return cell
        } else if (fieldData.name == fieldName.infoMessage.rawValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("AutoReloadInfoTableViewCell", forIndexPath: indexPath) as! AutoReloadInfoTableViewCell
            
            cell.setCellData(autoReloadDataFields[fieldName.autoReload.hashValue].switchState, fieldData: fieldData, incommCard: inCommUserCard)
            return cell
        } else {
            let cell = tableView.dequeueReusableCellWithIdentifier("AutoReloadTableViewCell", forIndexPath: indexPath) as! AutoReloadTableViewCell
            
            var showCreditCardType = false
            if (fieldData.name == fieldName.paymentAccount.rawValue && fieldData.field_image != "") {
                showCreditCardType = true
            }
            
            cell.setCellData(fieldData, showCreditCardType: showCreditCardType, creditCardTypeImageUrl: autoReloadDataFields[fieldName.paymentAccount.hashValue].field_image, navigationImage: fieldData.navgationImage, switchState: autoReloadDataFields[fieldName.autoReload.hashValue].switchState,incommCard: inCommUserCard)
            
            if (fieldData.name == fieldName.startDate.rawValue) {
                cell.navigationImage.hidden = false
               // cell.fieldValueTrailingSpace.constant = GiftCardAppConstants.tableViewTrailingSpace
            }
            return cell
        }
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //if switch state is on, then return the method
        if !autoReloadDataFields[fieldName.autoReload.hashValue].switchState {
            return
        } else if (inCommAutoReloadSavable != nil) {
            if (indexPath.row == fieldName.paymentAccount.hashValue) {
                if inCommUserPaymentAccount != nil {
                    performSegueWithIdentifier("ShowPaymentVC", sender: nil)
                } else {
                    self.presentOkAlert("Alert", message:"The payment account is not associated with your account")
                }
            }
            return
        }
        //cell action for refill balance option
        if (indexPath.row == fieldName.refillBalance.hashValue) {
            refillBalanceCellSelected()
            
            //cell action for on a specific option
        } else if (indexPath.row == fieldName.onASpecific.hashValue) {
            //reset minimumbalance
            minimumBalance = nil
            view.endEditing(true)
            
            var cell = tableView.cellForRowAtIndexPath(indexPath)
            if let cellType = cell as? AutoReloadWithValueCell {
                cellType.alpha = GiftCardAppConstants.tableViewCellActiveState
                cellType.cellData.checkBoxSelected = true
                autoReloadDataFields[fieldName.onASpecific.hashValue].checkBoxSelected = true
                cellType.radioButtonImageView.image = GiftCardAppConstants.radioButtonEnabled
            }
            let nextindexPath = NSIndexPath(forRow: fieldName.refillBalance.hashValue, inSection: indexPath.section)
            cell = tableView.cellForRowAtIndexPath(nextindexPath)
            if let cellType = cell as? AutoReloadWithoutValueCell {
                cellType.alpha = GiftCardAppConstants.tableViewCellDeactiveState
                cellType.cellData.checkBoxSelected = false
                autoReloadDataFields[fieldName.refillBalance.hashValue].checkBoxSelected = false
                cellType.radioButtonImageView.image = GiftCardAppConstants.radioButtonDisabled
                autoReloadDataFields[fieldName.refillBalance.hashValue].field_value = ""
                cellType.amountTextField.text = ""
                cellType.amountTextField.resignFirstResponder()
            }
            showCustomPicker()
            
            //cell action for payment option
        } else if (indexPath.row == fieldName.paymentAccount.hashValue) {
            performSegueWithIdentifier("showPaymentListVC", sender: nil);
            view.endEditing(true)
            
            //cell action for start date option
        } else if (indexPath.row == fieldName.startDate.hashValue) {
            view.endEditing(true)
            showDatePicker()
            
            //cell action for payment option
        } else if (indexPath.row == fieldName.reloadAmount.hashValue) {
            view.endEditing(true)
            performSegueWithIdentifier("ShowAmountSelectionVC", sender: nil);
        } else if (indexPath.row == fieldName.endDate.hashValue) {
            view.endEditing(true)
            performSegueWithIdentifier("ShowEndSelectionVC", sender: nil);
        }
    }
    
    func refillBalanceCellSelected() {
        //update reloadFrequencyId to none
        reloadFrequencyId = InCommReloadFrequencyType.None
        
        //select radio button on refill balance & focus on value
        let indexPath = NSIndexPath(forRow: fieldName.refillBalance.hashValue, inSection: 0)
        var cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AutoReloadWithoutValueCell {
            cellType.alpha = GiftCardAppConstants.tableViewCellActiveState
            cellType.cellData.checkBoxSelected = true
            autoReloadDataFields[fieldName.refillBalance.hashValue].checkBoxSelected = true
            cellType.amountTextField.becomeFirstResponder()
            cellType.radioButtonImageView.image = GiftCardAppConstants.radioButtonEnabled
        }
        
        //select radio button & focus on value
        let nextindexPath = NSIndexPath(forRow: fieldName.onASpecific.hashValue, inSection: indexPath.section)
        cell = tableView.cellForRowAtIndexPath(nextindexPath)
        if let cellType = cell as? AutoReloadWithValueCell {
            cellType.alpha = GiftCardAppConstants.tableViewCellDeactiveState
            cellType.cellData.checkBoxSelected = false
            autoReloadDataFields[fieldName.onASpecific.hashValue].checkBoxSelected = false
            cellType.fieldValue.text = ""
            autoReloadDataFields[fieldName.onASpecific.hashValue].field_value = ""
            cellType.radioButtonImageView.image = GiftCardAppConstants.radioButtonDisabled
        }
    }
    
    //MARK: Amount selection delegate
    func selectedAmount(value:String) {
        autoReloadDataFields[fieldName.reloadAmount.hashValue].field_value = value
        //display selected value in cell
        let indexPath = NSIndexPath(forRow: fieldName.reloadAmount.hashValue, inSection: 0)
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AutoReloadTableViewCell {
            cellType.cellData.field_value = autoReloadDataFields[fieldName.reloadAmount.hashValue].field_value
            cellType.fieldValue.text = autoReloadDataFields[fieldName.reloadAmount.hashValue].field_value
        }
        
        //remove "$" from card amount
        var cardAmount = autoReloadDataFields[fieldName.reloadAmount.hashValue].field_value
        if (cardAmount.containsString("$")) {
            cardAmount = String(cardAmount.characters.dropFirst())
        }
        //update balance
        amount = Double(cardAmount)
    }
    
    //MARK: - Picker view delegate
    func showCustomPicker() {
        var dataSource:[[String]] = [[]]
        //load data for picker view
        for index in 0...onASpecificPickerData.count - 1 {
            dataSource[0].append(onASpecificPickerData[index])
        }
        
        pickerViewController?.pickerData = dataSource
        pickerViewController?.noOfComponents = 1
        pickerViewController?.selectedValue1 = autoReloadDataFields[fieldName.onASpecific.hashValue].field_value
        pickerViewController?.pickerHeaderTitleLabel.text = fieldName.onASpecific.rawValue
        
        //retain selection
        if autoReloadDataFields[fieldName.onASpecific.hashValue].field_value != "" {
            pickerViewController?.retainSelectedValue()
        }
        pickerViewController?.picker.reloadAllComponents()
        
        //move the cell near to pickerview
        tableViewBottomSpace.constant = 0
        let indexPath = NSIndexPath(forRow: fieldName.onASpecific.hashValue, inSection: 0)
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW,  (Int64(NSEC_PER_SEC) * 0)), dispatch_get_main_queue(), { () -> Void in
            self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
        })
        
        //show picker view
        customPickerView.hidden = false
        pickerViewController?.showPicker()
    }
    
    func pickerValueChanged(value:String,index:Int) {
        reloadFrequencyId = InCommReloadFrequencyType(rawValue: value)
        autoReloadDataFields[fieldName.onASpecific.hashValue].field_value = value
        //display selected value in cell
        let indexPath = NSIndexPath(forRow: fieldName.onASpecific.hashValue, inSection: 0)
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AutoReloadWithValueCell {
            cellType.cellData.field_value = autoReloadDataFields[fieldName.onASpecific.hashValue].field_value
            cellType.fieldValue.text = autoReloadDataFields[fieldName.onASpecific.hashValue].field_value
        }
    }
    
    //close the picker view
    func closepickerScreen() {
        customPickerView.hidden = true
        tableViewBottomSpace.constant = 0
    }
    
    //MARK: - Date picker delegates
    func showDatePicker() {
        //retain value for date picker
        datePickerViewController?.retainSelectedValue(autoReloadDataFields[fieldName.startDate.hashValue].field_value)
        datePickerViewController?.pickerHeaderTitleLabel.text = fieldName.startDate.rawValue
        
        //show date picker
        datePickerView.hidden = false
        datePickerViewController?.showPicker()
        
        //move the position
        tableViewBottomSpace.constant = 0
        let indexPath = NSIndexPath(forRow: fieldName.startDate.hashValue, inSection: 0)
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW,  (Int64(NSEC_PER_SEC) * 0)), dispatch_get_main_queue(), { () -> Void in
            self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
        })
    }
    
    func datePickerValueChanged(value:String) {
        autoReloadDataFields[fieldName.startDate.hashValue].field_value = value
        //display selected value in cell
        let indexPath = NSIndexPath(forRow: fieldName.startDate.hashValue, inSection: 0)
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AutoReloadTableViewCell {
            cellType.cellData.field_value = autoReloadDataFields[fieldName.startDate.hashValue].field_value
            cellType.fieldValue.text = autoReloadDataFields[fieldName.startDate.hashValue].field_value
        }
        startsOn = convertDateTimeToUTC(value)
    }
    
    //close the date picker
    func closeDatePickerScreen() {
        datePickerView.hidden = true
        tableViewBottomSpace.constant = 0
    }
    
    //MARK: - Payment screen delegate
    func paymentAdded(addedPaymentDetails:InCommSubmitPayment?, savePayment:InCommUserPaymentAccountDetails?, paymentDetailWithId: InCommSubmitPaymentWithId?, creditCardDetail: InCommUserPaymentAccount?, creditCardImage:String, cardLastDigits:String) {
        
        self.navigationController!.popToViewController((self.navigationController?.viewControllers[1])!, animated: true)
        
        
        //Assign save payment details and payment details id
        savePaymentDetails = savePayment
        paymentAccountId = paymentDetailWithId?.paymentAccountId
        inCommAutoReloadAssociatePaymentAccount = creditCardDetail
        
        //get total summary amount
        let indexPath = NSIndexPath(forRow: fieldName.paymentAccount.hashValue, inSection: 0)
        
        
        //update value in payment detail cell
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AutoReloadTableViewCell {
            cellType.fieldValue.hidden = false
            cellType.creditCardType.hidden = false
            cellType.fieldValueTrailingSpace.constant = 95
            
            //get value from notification center
            cellType.fieldValue.text = cardLastDigits
            autoReloadDataFields[fieldName.paymentAccount.hashValue].field_value = cardLastDigits
            autoReloadDataFields[fieldName.paymentAccount.hashValue].field_image = creditCardImage
            
            //update/show credit card image
            if let url = NSURL(string: creditCardImage){
                cellType.creditCardType.hnk_setImageFromURL(url, placeholder: nil, success: { (image) -> Void in
                    cellType.creditCardType.image = image
                    SVProgressHUD.dismiss()
                    }, failure: { (error) -> Void in
                        SVProgressHUD.dismiss()
                })
            }
        }
    }
    
    //MARK: End date delegate
    func endDateSelected(value:String) {
        autoReloadDataFields[fieldName.endDate.hashValue].field_value = value
        
        let indexPath = NSIndexPath(forRow: fieldName.endDate.hashValue, inSection: 0)
        
        //update value in payment detail cell
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AutoReloadTableViewCell {
            cellType.fieldValue.hidden = false
            cellType.creditCardType.hidden = true
            cellType.fieldValueTrailingSpace.constant = 45
            
            cellType.fieldValue.text = value
        }
        
        //maintain the value in appropriate variable
        if dateFormatter.dateFromString(value) != nil {
            endsOn = convertDateTimeToUTC(value)
            numberOfOccurancesRemaining = nil
            never = nil
            
            return
        } else if let number = Int16(value) {
            numberOfOccurancesRemaining = number
            endsOn = nil
            never = nil
            return
        } else {
            endsOn = nil
            numberOfOccurancesRemaining = nil
            never = true
        }
    }
    
    // MARK: - Present confirmation
    func presentConfirmationWithYesOrNo(title: String, message: String, callback: (confirmed: Bool) -> Void){
        let okAction = UIAlertAction(title: "Yes", style: .Default) { action in
            callback(confirmed: true)
            
        }
        let cancelAction = UIAlertAction(title: "No", style: .Cancel) { action in
            callback(confirmed: false)
        }
        presentAlert(title, message: message, actions: okAction, cancelAction)
    }
    
    // MARK: - IBAction methods
    @IBAction func close(){
        if operationDoneByUser {
            self.presentConfirmationWithYesOrNo("Attention", message: "Do you wish to cancel auto reload rules?") { (confirmed) in
                if (confirmed) {
                    self.popViewController()
                }
            }
        } else {
            self.popViewController()
        }
    }
    
    @IBAction func deleteAutoReload(){
        if inCommAutoReloadSavable == nil{
            return
        }
        self.presentConfirmationWithYesOrNo("Delete", message: "Are you sure you want to delete these auto reload settings?", buttonTitle: "Yes") { (confirmed) in
            if confirmed{
                self.deleteAutoReloadConfig()
            }
        }
    }
    
    @IBAction func switchStateChanged(sender: UISwitch){
        if (sender.on) {
            autoReloadDataFields[fieldName.autoReload.hashValue].switchState = true
            
            enableTableViewCells()
        } else {
            autoReloadDataFields[fieldName.autoReload.hashValue].switchState = false
            disableTableViewCells()
        }
        updateAutoReloadStatus()
    }
    
    //MARK: - Validation
    func validation() -> Bool {
        var hasError = true
        if (minimumBalance != nil) {
            if minimumBalance==0 {
                let message = "Minimum balance should be greater than $0"
                self.presentOkAlert("Error", message: message, callback:  {
                    self.refillBalanceCellSelected()
                })
                return hasError
            } else if (minimumBalance > (giftCardBrand.variableAmountDenominationMaximumValue! - giftCardBrand.variableAmountDenominationMinimumValue!)) {
                let message = String(format: "Amount should be less than " + GiftCardAppConstants.amountWithZeroDecimalPoint,giftCardBrand.variableAmountDenominationMaximumValue!-giftCardBrand.variableAmountDenominationMinimumValue!)
                self.presentOkAlert("Error", message: message, callback: {
                    self.refillBalanceCellSelected()
                })
                return hasError
            } else if (minimumBalance! + amount > giftCardBrand.variableAmountDenominationMaximumValue!) {
                let message = String(format: "Auto reload amount exceeds the maximum allowable balance of " + GiftCardAppConstants.amountWithZeroDecimalPoint,giftCardBrand.variableAmountDenominationMaximumValue!)
                self.presentOkAlert("Error", message: message, callback: {
                    self.refillBalanceCellSelected()
                })
                return hasError
            }
        }
        if (!autoReloadDataFields[fieldName.autoReload.hashValue].switchState) {
            let message = "Please turn on auto reload to save value"
            self.presentOkAlert("Error", message: message)
            return hasError
        }
        if !autoReloadDataFields[fieldName.refillBalance.hashValue].checkBoxSelected && !autoReloadDataFields[fieldName.onASpecific.hashValue].checkBoxSelected {
            let message = "Please choose any one option"
            self.presentOkAlert("Error", message: message)
            return hasError
        }
        if autoReloadDataFields[fieldName.refillBalance.hashValue].checkBoxSelected {
            if (autoReloadDataFields[fieldName.refillBalance.hashValue].field_value == "") {
                let message = "Please enter amount"
                self.presentOkAlert("Error", message: message, callback: {
                    self.refillBalanceCellSelected()
                })
                return hasError
            }
        }
        if (autoReloadDataFields[fieldName.onASpecific.hashValue].checkBoxSelected) {
            if (autoReloadDataFields[fieldName.onASpecific.hashValue].field_value == "") {
                let message = "Please choose " + fieldName.onASpecific.rawValue.lowercaseString
                self.presentOkAlert("Error", message: message)
                return hasError
            }
        }
        if autoReloadDataFields[fieldName.endDate.hashValue].field_value == "" {
            let message = "Please choose " + fieldName.endDate.rawValue.lowercaseString
            self.presentOkAlert("Error", message: message)
            return hasError
        }
        if autoReloadDataFields[fieldName.paymentAccount.hashValue].field_value == "" {
            let message = "Please enter payment information."
            self.presentOkAlert("Error", message: message)
            return hasError
        }
        
        hasError = false
        return hasError
    }
    
    //MARK: - Text field delegates
    //calculate keyboard height 7 move the table view
    func keyBoardWillShown(notification:NSNotification){
        let userInfo:NSDictionary = notification.userInfo!
        let keyboardFrame:NSValue = userInfo.valueForKey(UIKeyboardFrameEndUserInfoKey) as! NSValue
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue) {
            self.tableViewBottomSpace.constant = keyboardFrame.CGRectValue().height
            self.view.layoutIfNeeded()
        }
        let indexPath = NSIndexPath(forRow: fieldName.refillBalance.hashValue, inSection: 0)
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW,  (Int64(NSEC_PER_SEC) * 0)), dispatch_get_main_queue(), { () -> Void in
            self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
        })
    }
    
    
    func textFieldDidBeginEditing(textField: UITextField) {
        //update reloadFrequencyId to none
        reloadFrequencyId = InCommReloadFrequencyType.None
        //if autoReload rules is set, then the ui interaction need to disable
        if inCommAutoReloadSavable != nil || !autoReloadDataFields[fieldName.autoReload.hashValue].switchState {
            let indexPath = NSIndexPath(forRow: fieldName.refillBalance.hashValue, inSection: 0)
            let cell = tableView.cellForRowAtIndexPath(indexPath)
            if let cellType = cell as? AutoReloadWithoutValueCell {
                cellType.amountTextField.resignFirstResponder()
            }
            return
        }
        refillBalanceCellSelected()
        
    }
    
    //validate refill amount
    func keyBoardWillHidden(notification:NSNotification){
        let userInfo:NSDictionary = notification.userInfo!
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue) {
            self.tableViewBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        //if it is empty show alert
        view.endEditing(true)
        if autoReloadDataFields[fieldName.refillBalance.hashValue].field_value == "" {
            showInvalidPriceRangeError()
            return true
        }
        
        //check the range of Amount
        var amountInText = autoReloadDataFields[fieldName.refillBalance.hashValue].field_value
        if amountInText.characters.contains("$") {
            amountInText = String(amountInText.characters.dropFirst())
        }
        let amount = Double(amountInText)
        
        if (amount > giftCardBrand.variableAmountDenominationMaximumValue) {
            showInvalidPriceRangeError()
            return true
        }
        return true
    }
    
    //validate the custom amount entered by user
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        var textValue = textField.text! as NSString
        textValue = textValue.stringByReplacingCharactersInRange(range, withString: string)
        let textValueString = textValue as String
        let indexPath = NSIndexPath(forRow: fieldName.refillBalance.hashValue, inSection: 0)
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AutoReloadWithoutValueCell {
            if !isCustomAmount(textValueString) {
                
                cellType.amountTextField.text = autoReloadDataFields[fieldName.refillBalance.hashValue].field_value
                return false
                
            } else if (!(textValueString.characters.contains("$")) && textValueString.characters.count > 0){
                autoReloadDataFields[fieldName.refillBalance.hashValue].field_value = "$" + textValueString
                cellType.amountTextField.text = "$"
            } else {
                autoReloadDataFields[fieldName.refillBalance.hashValue].field_value = textValueString
            }
            
            //update balance in api details
            var amountInText = autoReloadDataFields[fieldName.refillBalance.hashValue].field_value
            if amountInText.characters.contains("$") {
                amountInText = String(amountInText.characters.dropFirst())
            }
            minimumBalance = Double(amountInText)
        }
        return true
    }
    
    //show invalid range of amount alert
    func showInvalidPriceRangeError () {
        let message = String(format: "Please enter an amount less than " + GiftCardAppConstants.amountWithTwoDecimalPoint, giftCardBrand.variableAmountDenominationMaximumValue!)
        self.presentOkAlert("Alert", message: message) {
            let indexPath = NSIndexPath(forRow: fieldName.refillBalance.hashValue, inSection: 0)
            let cell = self.tableView.cellForRowAtIndexPath(indexPath)
            if let cellType = cell as? AutoReloadWithoutValueCell {
                cellType.amountTextField.text = ""
                self.autoReloadDataFields[fieldName.refillBalance.hashValue].field_value = ""
                cellType.amountTextField.becomeFirstResponder()
            }
        }
    }
    
    
    //MARK: - Initialize object
    func initializeObject() {
        //auto reload switch
        var data:AutoReloadSettingsDataModel = AutoReloadSettingsDataModel()
        data.name = (fieldName.autoReload.rawValue)
        
        //checking for Auto reload is enabled or not
        if (inCommAutoReloadSavable != nil) {
            data.switchState = true
        } else {
            data.switchState = false
        }
        
        autoReloadDataFields.append(data)
        
        //Auto reload Amount
        amount = giftCardBrand.variableAmountDenominationMinimumValue!
        
        data = AutoReloadSettingsDataModel()
        data.name = (fieldName.reloadAmount.rawValue)
        data.field_value = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,giftCardBrand.variableAmountDenominationMinimumValue!)   //load initial amount as minimum amount
        data.navgationImage = GiftCardAppConstants.pushScreenButon
        autoReloadDataFields.append(data)
        
        //when balance is low
        data = AutoReloadSettingsDataModel()
        data.name = (fieldName.refillBalance.rawValue)
        data.checkBoxSelected = false
        data.field_value = ""
        autoReloadDataFields.append(data)
        
        //on a Specific day
        data = AutoReloadSettingsDataModel()
        data.name = (fieldName.onASpecific.rawValue)
        data.field_value = ""
        data.navgationImage = GiftCardAppConstants.backButtonImageWhenPresented
        autoReloadDataFields.append(data)
        
        //start date
        data = AutoReloadSettingsDataModel()
        data.name = (fieldName.startDate.rawValue)
        
        //show current date as default date
        let date = NSDate()
        data.field_value = dateFormatter.stringFromDate(date)
        startsOn = convertDateTimeToUTC(data.field_value)
        data.navgationImage = GiftCardAppConstants.backButtonImageWhenPresented
        autoReloadDataFields.append(data)
        
        //End Date
        data = AutoReloadSettingsDataModel()
        data.name = (fieldName.endDate.rawValue)
        data.field_value = ""
        data.navgationImage = GiftCardAppConstants.pushScreenButon
        autoReloadDataFields.append(data)
        
        //payment details
        data = AutoReloadSettingsDataModel()
        data.name = (fieldName.paymentAccount.rawValue)
        data.field_value = ""
        data.navgationImage = GiftCardAppConstants.pushScreenButon
        autoReloadDataFields.append(data)
        
        //info message details
        data = AutoReloadSettingsDataModel()
        data.name = (fieldName.infoMessage.rawValue)
        autoReloadDataFields.append(data)
        
        // hide auto reload delete button
        deleteButton.hidden = (inCommUserCard.autoReloadId == nil)
    }
    
    //MARK: - retail auto reload values
    func fillAutoReloadValues() {
        if let autoReloadDetails = inCommAutoReloadSavable {
            
            // hide save button (move down the view)
            tableViewBottomSpace.constant = -saveButton.frame.size.height
            saveButton.hidden = true
            
            autoReloadDataFields[fieldName.autoReload.hashValue].switchState = autoReloadDetails.isActive
            autoReloadDataFields[fieldName.reloadAmount.hashValue].field_value = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint, autoReloadDetails.amount)
            
            //if minimum balance is not null, then fill refill balance
            if let minimumBalance = autoReloadDetails.minimumBalance {
                autoReloadDataFields[fieldName.refillBalance.hashValue].checkBoxSelected = true
                
                autoReloadDataFields[fieldName.refillBalance.hashValue].field_value = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint, minimumBalance)
            }
            
            if autoReloadDetails.reloadFrequencyId.rawValue != InCommReloadFrequencyType.None.rawValue {
                autoReloadDataFields[fieldName.onASpecific.hashValue].field_value = autoReloadDetails.reloadFrequencyId.rawValue
                autoReloadDataFields[fieldName.onASpecific.hashValue].checkBoxSelected = true
            }
            
            //update start date
            
            if let startDate = autoReloadDetails.startsOn {
                autoReloadDataFields[fieldName.startDate.hashValue].field_value = dateFormatter.stringFromDate(startDate)
            }
            
            
            if let endDate = autoReloadDetails.endsOn {
                autoReloadDataFields[fieldName.endDate.hashValue].field_value = dateFormatter.stringFromDate(endDate)
            } else if let endValue = autoReloadDetails.numberOfOccurancesRemaining {
                if (endValue > 0) {
                    autoReloadDataFields[fieldName.endDate.hashValue].field_value = String(endValue)
                } else {
                    autoReloadDataFields[fieldName.endDate.hashValue].field_value = "Never"
                }
            } else {
                autoReloadDataFields[fieldName.endDate.hashValue].field_value = "Never"
            }
            
            //update payment information
            if let incommPaymentDetail = inCommUserPaymentAccount {
                autoReloadDataFields[fieldName.paymentAccount.hashValue].field_value = "..." + incommPaymentDetail.creditCardNumber
                mapCreditCardImages()
            }
            tableView.reloadData()
        }
    }
    
    
    //MARK: Regex for Custom Amount Validation
    func isCustomAmount(testStr:String) -> Bool {
        var amountRegEx = "[$]{0,1}[0-9]{0,3}[.]?"
        if testStr.characters.contains(".") {
            amountRegEx = "[$]{0,1}[0-9]{0,3}[.]{1}[0-9]{0,2}"
        }
        let amountTest = NSPredicate(format:"SELF MATCHES %@", amountRegEx)
        return amountTest.evaluateWithObject(testStr)
    }
    
    //MARK: Disable UI Table View Cells for Table cells
    func disableTableViewCells() {
        //        if (inCommUserCard.autoReloadId != nil) {
        //            return
        //        }
        for i in 1...autoReloadDataFields.count {
            let index = NSIndexPath(forRow: i, inSection: 0)
            var cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? AutoReloadWithoutValueCell {
                cellType.makeDeActiveCell()
            }
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? AutoReloadWithValueCell {
                cellType.makeDeActiveCell()
            }
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? AutoReloadTableViewCell {
                cellType.makeDeActiveCell()
            }
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? AutoReloadInfoTableViewCell {
                cellType.makeDeActiveCell()
            }
        }
    }
    
    //MARK: Disable UI Table View Cells for Table cells
    func enableTableViewCells() {
        for i in 1...autoReloadDataFields.count {
            let index = NSIndexPath(forRow: i, inSection: 0)
            var cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? AutoReloadWithoutValueCell {
                cellType.makeActiveCell()
            }
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? AutoReloadWithValueCell {
                cellType.makeActiveCell()
            }
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? AutoReloadTableViewCell {
                cellType.makeActiveCell()
            }
            cell = tableView.cellForRowAtIndexPath(index)
            if let cellType = cell as? AutoReloadInfoTableViewCell {
                cellType.makeActiveCell()
            }
        }
    }
    
    // MARK: Map Credit card Images to the particular Account
    func mapCreditCardImages() {
        if let incommPaymentDetail = inCommUserPaymentAccount {
            for cardTypeDetail in giftCardBrand.creditCardTypesAndImages {
                if cardTypeDetail.creditCardType == incommPaymentDetail.creditCardTypeCode {
                    autoReloadDataFields[fieldName.paymentAccount.hashValue].field_image = cardTypeDetail.thumbnailImageUrl
                    break
                }
            }
        }
        self.tableView.reloadData()
    }
    
    //MARK: Save
    @IBAction func save(sender:UIButton){
        view.endEditing(true)
        if (inCommAutoReloadSavable?.id == nil){
            // check validation has error or not
            if validation() {
                // has error
                return
            }
            
            if savePaymentDetails != nil{
                associatePaymentAccountToUser()
            }
            else{
                createAutoReload()
            }
        }
//        else{
//            updateAutoReloadStatus()
//        }
    }
    
    //MARK: Associate payment account to user
    func associatePaymentAccountToUser(){
        self.resignFirstResponder()
        SVProgressHUD.showWithStatus("Associating Payment Account...", maskType: .Clear)
            GiftCardCreationService.sharedInstance.associatePaymentDetailsToUser(savePaymentDetails) { (inCommUserPaymentAccount, error) in
                if error != nil{
                    SVProgressHUD.dismiss()
                    self.presentConfirmation("Failure", message: "An Unexpected error occured while processing your request, \n associate payment account - failure, \n auto reload settings - failure ", buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.associatePaymentAccountToUser()
                        }
                        else{
                            self.presentConfirmationWithYesOrNo("Confirm", message: "Payment not associated to your account. \n Do you want to discard all changes", buttonTitle: "Yes", callback: { (confirmed) in
                                if confirmed{
                                    self.popViewController()
                                }
                                else{
                                    self.associatePaymentAccountToUser()
                                }
                            })
                        }
                    })
                }
                else{
                    self.paymentAccountId = inCommUserPaymentAccount!.id
                    self.inCommAutoReloadAssociatePaymentAccount = inCommUserPaymentAccount
                    self.createAutoReload()
                }
            }
    }
    
    //MARK: Create auto reload
    func createAutoReload(){
        self.resignFirstResponder()
        let inCommAutoReloadConfig = InCommAutoReload(amount: amount, endsOn:endsOn, giftCardId: giftCardId, minimumBalance: minimumBalance, numberOfOccurancesRemaining: numberOfOccurancesRemaining, paymentAccountId: paymentAccountId, startsOn: startsOn, reloadFrequencyId: reloadFrequencyId)
        SVProgressHUD.showWithStatus("Creating AutoReload...", maskType: .Clear)
        GiftCardCreationService.sharedInstance.setAutoReloadConfig(giftCardId, autoReload: inCommAutoReloadConfig) { (inCommAutoReloadSavable, error) in
            if error != nil{
                SVProgressHUD.dismiss()
                var errorMessage = "An Unexpected error occured while processing your request \n auto reload config - failure"
                if self.savePaymentDetails != nil{
                    errorMessage = "An Unexpected error occured while processing your request, \n associate payment account - completed, \n auto reload config - failure "
                }
                self.presentConfirmation("Failure", message: errorMessage, buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed{
                        self.createAutoReload()
                    }
                    else{
                        self.presentConfirmationWithYesOrNo("Confirm", message: "Auto reload configuration not created. \n Do you want to discard all changes", buttonTitle: "Yes", callback: { (confirmed) in
                            if confirmed{
                                self.popViewController()
                            }
                            else{
                                self.createAutoReload()
                            }
                        })
                    }
                })
            }
            else{
                SVProgressHUD.dismiss()
                //track clyp analytics event for set the rules for auto reload
                clpAnalyticsService.sharedInstance.clpTrackScreenView("GiftCardAutoreloadCreated");
                self.inCommUserPaymentAccount = self.inCommAutoReloadAssociatePaymentAccount
                self.inCommAutoReloadSavable = inCommAutoReloadSavable
                self.presentOkAlert("Success", message: "Successfully configured auto reload", callback: {
                    self.autoReloadDelegate()
                })
            }
        }
    }
    
    //MARK: Update auto reload status
    func updateAutoReloadStatus(){
        if (inCommAutoReloadSavable?.id == nil) {
            operationDoneByUser = true
            return
        }
        self.resignFirstResponder()
        SVProgressHUD.showWithStatus("Updating...", maskType: .Clear)
        GiftCardCreationService.sharedInstance.updateAutoReloadConfigStatus(giftCardId, autoReloadId: inCommAutoReloadSavable!.id, status: autoReloadDataFields[fieldName.autoReload.hashValue].switchState) { (error) in
            SVProgressHUD.dismiss()
            if error != nil{
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed{
                        self.updateAutoReloadStatus()
                    }
                    else{
                        return
                    }
                })
            }
            else{
                self.presentOkAlert("Success", message: "Status updated successfully", callback: {
                    self.inCommAutoReloadSavable!.isActive = self.autoReloadDataFields[fieldName.autoReload.hashValue].switchState
                    self.autoReloadDelegate()
                })
            }
        }
    }
    
    //MARK: Delete auto reload config
    func deleteAutoReloadConfig(){
        SVProgressHUD.showWithStatus("Deleting...", maskType: .Clear)
        GiftCardCreationService.sharedInstance.deleteAutoReloadDetails(giftCardId, autoReloadId: inCommAutoReloadSavable!.id) { (error) in
            SVProgressHUD.dismiss()
            if error != nil{
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed{
                        self.updateAutoReloadStatus()
                    }
                    else{
                        return
                    }
                })
            }
            else{
                //track clyp analytics event for delete the rules for auto reload
//                clpAnalyticsService.sharedInstance.clpTrackScreenView("gift_card_autoreload_removed");
                self.presentOkAlert("Success", message: "Auto reload configure deleted successfully", callback: {
                    self.inCommAutoReloadSavable = nil
                    self.inCommUserPaymentAccount = nil
                    self.autoReloadDelegate()
                })
            }
        }
    }
    
    //MARK: Auto reload config delegate
    
    func autoReloadDelegate(){
        self.popViewController()
        var inCommUserGiftCard = inCommUserCard
        inCommUserGiftCard.autoReloadId = inCommAutoReloadSavable?.id
        GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
        self.delegate?.updateAutoReloadDetails(self.inCommAutoReloadSavable, inCommUserPaymentAccount:  self.inCommUserPaymentAccount)
    }
    
    //MARK: convert date & time for date to send default time is "UTC" format 4:00 PM
    func convertDateTimeToUTC(value:String) -> NSDate {
        let dateInString = value + GiftCardAppConstants.dateToSendTime
        let nsDateFormatter = NSDateFormatter()
        nsDateFormatter.dateFormat = GiftCardAppConstants.LongDateFormat
        nsDateFormatter.timeZone = NSTimeZone(name: GiftCardAppConstants.timeZone)
        return nsDateFormatter.dateFromString(dateInString)!
    }
    
    // MARK: de alloc Notification
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }

}



