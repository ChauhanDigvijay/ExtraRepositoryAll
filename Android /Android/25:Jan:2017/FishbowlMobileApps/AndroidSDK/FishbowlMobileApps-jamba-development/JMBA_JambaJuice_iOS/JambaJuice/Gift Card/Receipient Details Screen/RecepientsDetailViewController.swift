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

//MARK: RecepientsDetailViewController Delegate
protocol RecepientsDetailViewControllerDelegate: class {
    func receipientDetailSaved(recipientDetails:InCommOrderRecipientDetails, message:String, recipientSelf:Bool)
}

class RecepientsDetailViewController: UIViewController,UITableViewDelegate, UITextFieldDelegate,DatePickerViewControllerDelegate {
    //field name in enumeration
    enum fieldName:String {
        case purchaseAsGift         = "Purchase as a gift?"
        case yes                    = "Yes"
        case no                     = "No"
        case firstNamelastName      = "Recipient First & Last Name"
        case firstName              = "First Name"
        case lastName               = "Last Name"
        case messageHeader          = "Message"
        case message                = "Enter Message(Optional)"
        case emailAddrressHeader    = "Recipient Email Address"
        case emailAddrress          = "Enter Recipient Address"
        case dateToSendHeader       = "Date to Send"
        case dateToSend             = ""
    }
    
    //section name
    enum sectionNameType:String {
        case header             = "Header"
        case purchaseAsGift     = "Purchase as a gift?"
        case firstNamelastName  = "Recipient First & Last Name"
        case message            = "Message"
        case emailAddrress      = "Recipient Email Address"
        case dateToSend         = "Date to Send"
    }
    
    var fieldSetValue                       :[RecepientDetailsDataModel] = []
    var currentIndex                        : NSIndexPath = NSIndexPath(forRow: 0, inSection: 0)
    @IBOutlet weak var tableView            : UITableView!
    @IBOutlet weak var tableViewBottomSpace : NSLayoutConstraint!
    var keyboardHeight                      : CGFloat = 0
    let paymentDetails                      = GiftCardCreationService.sharedInstance.paymentDetails
    weak var delegate                       : RecepientsDetailViewControllerDelegate?
    
    //replacement For shared Instance
    var purchaserDetails                    : InCommOrderPurchaser?
    var recipientDetails                    : InCommOrderRecipientDetails?
    var orderMessage                        : String?
    var recipientSelf                       : Bool?
    var dateToSend                          : NSDate!
    
    private var datePickerViewController        : DatePickerViewController?
    let dateFormatter                       =  NSDateFormatter()
    
    @IBOutlet weak var datePickerView: UIView!
    
    override func viewDidLoad() {
        //maintain the date format
        dateFormatter.dateFormat = GiftCardAppConstants.ShortDateFormat
        datePickerView.hidden = true
        
        initializeObject()                  //initialize fields
        fillValuesFromRecipientInfo()       //retain previous entered values
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(RecepientsDetailViewController.keyboardDidShow(_:)), name: UIKeyboardWillShowNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(RecepientsDetailViewController.keyboardWillDismiss(_:)), name: UIKeyboardWillHideNotification, object: nil)
        super.viewDidLoad()
    }
    
    // MARK: - Table view delegates
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //if the check box is selected to no or neither of them selected then need not to show recipient field details
        if (fieldSetValue[fieldName.no.hashValue].field_image || (!fieldSetValue[fieldName.yes.hashValue].field_image && !fieldSetValue[fieldName.no.hashValue].field_image)) {
            return fieldSetValue.count - 9      //7 fields need to omit
        }
        return fieldSetValue.count - 2;
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if (fieldSetValue[indexPath.row].name == sectionNameType.firstNamelastName.rawValue || fieldSetValue[indexPath.row].name == sectionNameType.message.rawValue || fieldSetValue[indexPath.row].name == sectionNameType.emailAddrress.rawValue || fieldSetValue[indexPath.row].name == sectionNameType.dateToSend.rawValue) {
            return GiftCardAppConstants.tableViewSectionHeight
        }
        return GiftCardAppConstants.tableViewCellHeight
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if (fieldSetValue[indexPath.row].section == sectionNameType.header.rawValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("RecepientDetailHeaderViewCell", forIndexPath: indexPath) as! RecepientDetailHeaderViewCell
            cell.labelName.text = fieldSetValue[indexPath.row].name
            return cell
        } else if (fieldSetValue[indexPath.row].section == sectionNameType.purchaseAsGift.rawValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("RecepientDetailCheckTypeViewCell", forIndexPath: indexPath) as! RecepientDetailCheckTypeViewCell
            cell.setCellData(fieldSetValue[indexPath.row].name, showImage: !fieldSetValue[indexPath.row].field_image)
            return cell
        } else if (fieldSetValue[indexPath.row].section == sectionNameType.dateToSend.rawValue){
            let cell = tableView.dequeueReusableCellWithIdentifier("RecipientDateToSendTableViewCell", forIndexPath: indexPath) as! RecipientDateToSendTableViewCell
            cell.setData(dateToSend)
            return cell
        }
        else  {
            let cell = tableView.dequeueReusableCellWithIdentifier("RecepientDetailTextFieldViewCell", forIndexPath: indexPath) as! RecepientDetailTextFieldViewCell
            cell.textfield.delegate = self
            cell.textfield.tag = indexPath.row
            cell.setCellData(fieldSetValue[indexPath.row].name, textFieldValue: fieldSetValue[indexPath.row].field_value, textFieldEnabled: false, textFieldKeyboardType: fieldSetValue[indexPath.row].keyboardType)
            
            return cell
        }
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        disableTextBox()
        currentIndex = indexPath
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? RecepientDetailCheckTypeViewCell {
            if (!cellType.imageName.hidden) {
                return
            } else {
                fieldSetValue[indexPath.row].field_image = !fieldSetValue[indexPath.row].field_image
                let index = indexPath.row == fieldName.yes.hashValue ? fieldName.no.hashValue : fieldName.yes.hashValue
                fieldSetValue[index].field_image = !fieldSetValue[indexPath.row].field_image
                if (indexPath.row == fieldName.yes.hashValue) {
                    //clear the purchaser info
                    clearValues()
                } else if (indexPath.row == fieldName.no.hashValue) {
                    //Load values from purchaser Info
                    fillValuesFromPurchaserInfo()
                }
                self.tableView.reloadData()
            }
        }
        
        if let cellType = cell as? RecepientDetailTextFieldViewCell {
            tableViewBottomSpace.constant = keyboardHeight
            tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
            cellType.textfield.enabled = true
            cellType.textfield.becomeFirstResponder()
            
        }
        
        if (cell as? RecepientDetailHeaderViewCell) != nil {
            view.endEditing(true)
        }
        
        if (cell as? RecipientDateToSendTableViewCell) != nil{
            showDatePicker()
            view.endEditing(true)
        }
    }
    
    // MARK: - Diable textfield
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
        keyboardHeight = keyboardRectangle.height
        
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.tableViewBottomSpace.constant = keyboardRectangle.height
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
        let textValueString = textValue as String
        if (fieldSetValue[textField.tag].name ==  fieldName.firstName.rawValue || fieldSetValue[textField.tag].name == fieldName.lastName.rawValue){
            if string == ""{
                fieldSetValue[textField.tag].field_value = textValueString
                return true
            }else{
                if GiftCardAppConstants.nameValidation(textValueString){
                    fieldSetValue[textField.tag].field_value = textValueString
                    return true
                }else{
                    return false
                }
            }
        }else{
            fieldSetValue[textField.tag].field_value = textValueString
            return true
        }
    }
    
    //navigate to next textfield/dismiss keyboard
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        var cell = tableView.cellForRowAtIndexPath(currentIndex)
        if let cellType = cell as? RecepientDetailTextFieldViewCell {
            cellType.textfield.enabled = false
            var indexPath = NSIndexPath(forRow: currentIndex.row+1, inSection: currentIndex.section)
            cell = tableView.cellForRowAtIndexPath(indexPath)
            //check next cell is a textbox or not. if it is focus on that cell textbox
            if let cellType = cell as? RecepientDetailTextFieldViewCell {
                currentIndex = indexPath
                cellType.textfield.enabled = true
                cellType.textfield.becomeFirstResponder()
                return true
            }
            indexPath = NSIndexPath(forRow: indexPath.row+1, inSection: currentIndex.section)
            cell = tableView.cellForRowAtIndexPath(indexPath)
            //check second cell is a textbox or not. if it is focus on that cell textbox
            if let cellType = cell as? RecepientDetailTextFieldViewCell {
                currentIndex = indexPath
                cellType.textfield.enabled = true
                cellType.textfield.becomeFirstResponder()
                return true
            }
            view.endEditing(true)
        }
        return true
    }
    
    //MARK: - IBAction methods
    //save the recipient details in creation service
    @IBAction func saveRecipientInformationInformatiom() {
        if !validation() {
            return
        }
        var receipientDetail = InCommOrderRecipientDetails(firstName: fieldSetValue[fieldName.firstName.hashValue].field_value, lastName: fieldSetValue[fieldName.lastName.hashValue].field_value, emailAddress: fieldSetValue[fieldName.emailAddrress.hashValue].field_value, items: [])
        if fieldSetValue[fieldName.yes.hashValue].field_image {
            if GiftCardAppConstants.getDateToSend(dateToSend)  != "Today" {
                receipientDetail.deliverOn = dateToSend
            } else {
                receipientDetail.deliverOn = nil
            }
        } else {
            receipientDetail.deliverOn = nil
        }
        
        delegate?.receipientDetailSaved(receipientDetail,message: fieldSetValue[fieldName.message.hashValue].field_value, recipientSelf: fieldSetValue[fieldName.no.hashValue].field_image )
        self.popViewController()
    }
    
    @IBAction func closeScreen() {
        //check whether it is presented or pushed
        if (self.isBeingPresented()) {
            dismissViewControllerAnimated(true, completion: nil)
        } else {
            popViewController()
        }
    }
    
    //MARK: - Validation
    func validation() -> Bool {
        if (!fieldSetValue[fieldName.yes.hashValue].field_image && !fieldSetValue[fieldName.no.hashValue].field_image) {
            self.presentOkAlert("Alert", message: "Please select a valid personalization option")
            return false
        }
        for index in 3...fieldSetValue.count - 1 {
            if (fieldSetValue[fieldName.yes.hashValue].field_image) {
                if (fieldSetValue[index].name == fieldName.firstName.rawValue || fieldSetValue[index].name == fieldName.lastName.rawValue) {
                    let value = fieldSetValue[index].field_value.trim()
                    if (value == "" || value.characters.count == 0) {
                        self.presentOkAlert("Alert", message: "Please enter \(fieldSetValue[index].name.lowercaseString)")
                        return false
                    }else if value.characters.count < 3{
                        self.presentOkAlert("Alert", message: "Please enter '\(fieldSetValue[index].name.lowercaseString)' minimum 3 characters")
                        return false
                    }
                }
                if (fieldSetValue[index].name == fieldName.emailAddrress.rawValue) {
                    let value = fieldSetValue[index].field_value.trim()
                    if (value == "") {
                        self.presentOkAlert("Alert", message: "Please enter email address")
                        return false
                    } else if !value.isEmail() {
                        self.presentOkAlert("Alert", message: "Please enter valid email address")
                        return false
                    }
                }
            }
        }
        return true
    }
    
    //MARK: - Option switching func
    //Clear the data when switch from no to yes
    func clearValues() {
        fieldSetValue[fieldName.firstName.hashValue].field_value = ""
        fieldSetValue[fieldName.lastName.hashValue].field_value = ""
        fieldSetValue[fieldName.message.hashValue].field_value = ""
        fieldSetValue[fieldName.emailAddrress.hashValue].field_value = ""
        dateToSend = NSDate()
    }
    
    //fill the recipient details from purchaser details when switch from yes to no
    func fillValuesFromPurchaserInfo() {
        let purchaserDetails = GiftCardCreationService.sharedInstance.purchaserDetails
        fieldSetValue[fieldName.firstName.hashValue].field_value = purchaserDetails.firstName
        fieldSetValue[fieldName.lastName.hashValue].field_value = purchaserDetails.lastName
        fieldSetValue[fieldName.message.hashValue].field_value = ""
        fieldSetValue[fieldName.emailAddrress.hashValue].field_value = purchaserDetails.emailAddress
        dateToSend = NSDate()
    }
    
    //retain previous entered values
    func fillValuesFromRecipientInfo() {
        if let receipientDetails = recipientDetails {
            fieldSetValue[fieldName.firstName.hashValue].field_value = receipientDetails.firstName
            fieldSetValue[fieldName.lastName.hashValue].field_value = receipientDetails.lastName
            fieldSetValue[fieldName.message.hashValue].field_value = orderMessage!
            fieldSetValue[fieldName.emailAddrress.hashValue].field_value = receipientDetails.emailAddress!
            fieldSetValue[fieldName.no.hashValue].field_image = recipientSelf!
            fieldSetValue[fieldName.yes.hashValue].field_image = !recipientSelf!
            dateToSend = receipientDetails.deliverOn == nil ? NSDate() : receipientDetails.deliverOn
        }
    }
    
    func initializeObject()  {
        //check box
        var data = RecepientDetailsDataModel()
        data.section = sectionNameType.header.rawValue
        data.name = sectionNameType.purchaseAsGift.rawValue
        fieldSetValue.append(data)
        
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.purchaseAsGift.rawValue
        data.name = fieldName.yes.rawValue
        fieldSetValue.append(data)
        
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.purchaseAsGift.rawValue
        data.name = fieldName.no.rawValue
        fieldSetValue.append(data)
        
        //first name & last name
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.header.rawValue
        data.name = sectionNameType.firstNamelastName.rawValue
        fieldSetValue.append(data)
        
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.firstNamelastName.rawValue
        data.name = fieldName.firstName.rawValue
        data.field_value = ""
        fieldSetValue.append(data)
        
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.firstNamelastName.rawValue
        data.name = fieldName.lastName.rawValue
        data.field_value = ""
        fieldSetValue.append(data)
        
        //message
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.header.rawValue
        data.name = sectionNameType.message.rawValue
        data.field_value = ""
        fieldSetValue.append(data)
        
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.message.rawValue
        data.name = fieldName.message.rawValue
        data.field_value = ""
        fieldSetValue.append(data)
        
        //email Address
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.header.rawValue
        data.name = sectionNameType.emailAddrress.rawValue
        data.field_value = ""
        fieldSetValue.append(data)
        
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.emailAddrress.rawValue
        data.name = fieldName.emailAddrress.rawValue
        data.keyboardType = .EmailAddress
        data.field_value = ""
        fieldSetValue.append(data)
        
        //date to send
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.header.rawValue
        data.name = sectionNameType.dateToSend.rawValue
        data.field_value = ""
        fieldSetValue.append(data)
        
        data = RecepientDetailsDataModel()
        data.section = sectionNameType.dateToSend.rawValue
        data.name = fieldName.dateToSend.rawValue
        dateToSend = NSDate()
        data.field_value = GiftCardAppConstants.getDateToSend(dateToSend)
        fieldSetValue.append(data)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ShowDatePickerVC" {
            datePickerViewController = segue.destinationViewController as? DatePickerViewController
            datePickerViewController?.delegate = self
        }
    }
    
    //MARK: - Date picker delegates
    func showDatePicker() {
        datePickerViewController?.picker.minimumDate = NSDate()
        datePickerViewController?.pickerHeaderTitleLabel.text = fieldName.dateToSendHeader.rawValue
        
        //show date picker
        datePickerView.hidden = false
        datePickerViewController?.showPicker()
        
        //move the position
        tableViewBottomSpace.constant = 0
        let indexPath = NSIndexPath(forRow: fieldName.dateToSendHeader.hashValue, inSection: 0)
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW,  (Int64(NSEC_PER_SEC) * 0)), dispatch_get_main_queue(), { () -> Void in
            self.tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
        })
    }
    
    func datePickerValueChanged(value:String) {
        dateToSend = convertDateTimeToUTC(value)
        let indexPath = NSIndexPath(forRow: fieldName.dateToSend.hashValue, inSection: 0)
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let recipientCell = cell as? RecipientDateToSendTableViewCell{
            recipientCell.setData(dateToSend)
        }
    }
    
    //convert date & time for date to send default time is "UTC" format 4:00 PM
    func convertDateTimeToUTC(value:String) -> NSDate {
        let dateInString = value + GiftCardAppConstants.dateToSendTime
        let nsDateFormatter = NSDateFormatter()
        nsDateFormatter.dateFormat = GiftCardAppConstants.LongDateFormat
        nsDateFormatter.timeZone = NSTimeZone(name: GiftCardAppConstants.timeZone)
        return nsDateFormatter.dateFromString(dateInString)!
    }
    
    //close the date picker
    func closeDatePickerScreen() {
        datePickerView.hidden = true
        tableViewBottomSpace.constant = 0
    }
    
    // MARK: - de alloc Notification
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
}