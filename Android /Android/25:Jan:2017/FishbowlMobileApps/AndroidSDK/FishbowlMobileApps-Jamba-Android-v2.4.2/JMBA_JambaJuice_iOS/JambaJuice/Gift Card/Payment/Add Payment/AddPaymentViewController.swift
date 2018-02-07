//
//  ManageGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 09/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import HDK
import SVProgressHUD
import AlamofireImage
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
    switch (lhs, rhs) {
    case let (l?, r?):
        return l < r
    case (nil, _?):
        return true
    default:
        return false
    }
}


//MARK: PaymentViewController Delegate
protocol AddPaymentViewControllerDelegate: class {
    func paymentAdded(_ addedPaymentDetails:InCommSubmitPayment?, savePayment:InCommUserPaymentAccountDetails?, paymentDetailWithId: InCommSubmitPaymentWithId?, creditCardDetail: InCommUserPaymentAccount?, creditCardImage:String, cardLastDigits:String)
}

class AddPaymentViewController: UIViewController,UITableViewDelegate,UITableViewDataSource, PickerViewControllerDelegate, UITextFieldDelegate, GiftCardWebviewDelegate, CardIOPaymentViewControllerDelegate, AddPaymentSingleTextFieldTableViewCellDelegate {
    
    //field name
    enum fieldName : String {
        case firstName      = "First Name"
        case lastName       = "Last Name"
        case street1        = "Address 1"
        case street2        = "Address 2(Optional)"
        case city           = "City"
        case state          = "State"
        case zipCode        = "Zip Code"
        case country        = "Country"
        case cardType       = "Card Type"
        case cardNumber     = "Card Number"
        case ccv            = "Security Code (CVV)"
        case expiryDate     = "Expiration Date (MM/YYYY)"
        case termsText      = "By tapping save card, you agree to the program terms and conditions."
        case savePayment    = "Save this Payment details"
    }
    
    //field type
    enum fieldType : String {
        case singletextField    = "Single text Field"
        case doubletextField    = "Double text Field"
        case ddlWithImage       = "DDL Image"
        case ddl                = "DDL Only"
        case navigation         = "Navigation"
        case checkingField      = "Checking Field"
    }
    
    //section name's
    enum section : String {
        case personalDetails    = "Personal Details"
        case cardDetails        = "Card Details"
    }
    
    var personalDetails         : [FieldSetDataModel] = []                              //personal details field array
    var cardDetails             : [FieldSetDataModel] = []                              //card details field array
    var brand                   : InCommBrand?                                          //brand details
    var countryCode             : [String] = []                                         //selected country - code
    var stateCode               : [String] = []                                         //selected state code
    var tableCellIndexPath      : IndexPath = IndexPath(row: 0, section:0);    //current selected textbox
    var keyboardHeight          : CGFloat = 0                                           //keyboard height
    var pickerSelectedValue     : FieldSetDataModel = FieldSetDataModel();
    var pickerSelectedIndex     : Int = 0                                               //picker selected index for country & state
    var userNewlyAddedCreditCard: InCommSubmitPayment?
    weak var delegate           : AddPaymentViewControllerDelegate?
    var userPaymentDetails      : InCommUserPaymentAccountDetails?
    var userSavedPaymentDetails : InCommUserPaymentAccountDetails?
    fileprivate var pickerViewController:PickerViewController?
    @IBOutlet weak var tableView            : UITableView!
    @IBOutlet weak var tableViewBottomSpace : NSLayoutConstraint!
    @IBOutlet weak var headerLabel          : UILabel!
    @IBOutlet weak var submitButton         : UIButton!
    @IBOutlet weak var pickerView           : UIView!
    var headerTitle             : String?                                               //title of the screen
    var cardAmount              : Double = 0.00                                         //card amount for API details
    var enableSavePayment       : Bool = true                                           //select enable for save payment option
    var saveBillingPayment      : Bool = true                                           //for gift card creation flow
    fileprivate var creditCardNumberString = ""
    override func viewDidLoad() {
        
        brand = InCommGiftCardBrandDetails.sharedInstance.brand
        
        initializeObject()
        
        //set screen title
        headerLabel.text = headerTitle
        fillpaymentDetails()
        pickerView.isHidden = true
        
        //load header design
        let nib = UINib(nibName: "TableHeaderSection", bundle: nil)
        tableView.register(nib, forHeaderFooterViewReuseIdentifier: "TableHeaderSection")
        
        NotificationCenter.default.addObserver(self, selector: #selector(AddPaymentViewController.keyboardDidShow(_:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(AddPaymentViewController.keyboardWillDismiss(_:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        
        super.viewDidLoad()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showPickerView" {
            pickerViewController = segue.destination as? PickerViewController
            pickerViewController?.delegate = self
        } else if segue.identifier == "showJambaTermsPageVC" {
            if let webPageViewController = segue.destination as? GiftCardWebview {
                webPageViewController.delegate = self
            }
        }
    }
    
    
    // MARK: - Table view delegates
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2;
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let header = self.tableView.dequeueReusableHeaderFooterView(withIdentifier: "TableHeaderSection") as! TableHeaderSection
        if (section == 0) {
            header.headerText.text = "Personal Details";
        } else {
            header.headerText.text = "Card Details";
        }
        return header
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (section == 0) {
            return personalDetails.count
        }
        return cardDetails.count;
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var fieldData:FieldSetDataModel = FieldSetDataModel()
        
        //check it is a personal details section or card details
        if (indexPath.section == 0) {
            fieldData = personalDetails[indexPath.row]
        } else {
            fieldData = cardDetails[indexPath.row]
        }
        
        if (fieldData.field_type == (fieldType.ddl.rawValue)) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "AddPaymentSingleDDLTableViewCell", for: indexPath) as! AddPaymentSingleDDLTableViewCell
            
            cell.labelName.delegate = self
            
            cell.setCellData(fieldData)
            
            return cell
        } else if (fieldData.field_type == (fieldType.ddlWithImage.rawValue)) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "AddPaymentDDLImageTableviewCell", for: indexPath) as! AddPaymentDDLImageTableviewCell
            cell.setCellData(fieldData)
            
            return cell
            
        } else if (fieldData.field_type == (fieldType.navigation.rawValue)) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "AddPaymentNavigationTableViewCell", for: indexPath) as! AddPaymentNavigationTableViewCell
            
            cell.setCellData(fieldData)
            
            return cell
            
        } else if (fieldData.field_type == (fieldType.checkingField.rawValue)) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "AddPaymentCheckTableViewCell", for: indexPath) as! AddPaymentCheckTableViewCell
            
            let state = cardDetails[indexPath.row].field_value == "true" ? true : false
            cell.setCellData(fieldData, enableSwitch: enableSavePayment, state: state)
            
            cell.cellData = fieldData
            return cell
            
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "AddPaymentSingleTextFieldTableViewCell", for: indexPath) as! AddPaymentSingleTextFieldTableViewCell
            
            cell.delegate = self
            cell.firstTextField.delegate = self
            cell.setCellData(fieldData)
            if fieldData.name == fieldName.cardNumber.rawValue {
                cell.setCreditCardCellData(fieldData)
                _ = formatCreditCardNumber(cell.firstTextField)
                cell.firstTextField.addTarget(self, action: #selector(AddPaymentViewController.textFieldEditingChanged(_:)), for: .editingChanged)
            } /*else {
             cell.setCreditCardCellData(fieldData)
             _ = formatCreditCardNumber(cell.firstTextField)
             cell.setCellData(fieldData)
             } */
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.cellForRow(at: indexPath)
        disableTextBox()
        tableCellIndexPath = indexPath
        if let cellType = cell as? AddPaymentDDLImageTableviewCell {
            tableView.scrollToRow(at: indexPath, at: .middle, animated: true)
            pickerSelectedValue = FieldSetDataModel();
            pickerSelectedValue = cellType.cellData
            
            showCreditCardTypePicker()
            view.endEditing(true)
        } else if let cellType = cell as? AddPaymentSingleDDLTableViewCell {
            view.endEditing(true)
            
            pickerSelectedValue = FieldSetDataModel();
            pickerSelectedValue = cellType.cellData
            if (cellType.cellData.name == fieldName.expiryDate.rawValue) {
                showDatePicker()
            } else if (cellType.cellData.name == fieldName.country.rawValue){
                showCountryPicker()
            } else if (cellType.cellData.name == fieldName.state.rawValue) {
                showStatePicker()
            }
        } else if let cellType = cell as? AddPaymentSingleTextFieldTableViewCell {
            tableViewBottomSpace.constant = keyboardHeight
            tableView.scrollToRow(at: indexPath, at: .top, animated: true)
            cellType.firstTextField.isEnabled = true
            cellType.firstTextField.becomeFirstResponder()
        } else if (cell as? AddPaymentNavigationTableViewCell) != nil {
            performSegue(withIdentifier: "showJambaTermsPageVC", sender: nil)
        }
    }
    @objc func textFieldEditingChanged(_ textField: UITextField) {
        if textField.placeholder == "Card Number" {
            creditCardNumberString = formatCreditCardNumber(textField)
        }
    }
    
    // MARK: - Show custom picker's
    func showDatePicker() {
        //array index for expiry date
        let arrayIndex = fieldName.expiryDate.hashValue - personalDetails.count
        
        tableViewBottomSpace.constant = 0
        
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double((Int64(NSEC_PER_SEC) * 0)) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.tableView.scrollToRow(at: self.tableCellIndexPath, at: .top, animated: true)
        })
        
        //expiration credit card month
        var dataSource:[[String]] = [["01","02","03","04","05","06","07","08","09","10","11","12"],[]]
        
        //expiration credit card year
        let date = Date()
        let calendar = Calendar.current
        let components = (calendar as NSCalendar).components( [.year], from: date)
        
        let year =  components.year
        for index in year!...year!+50 {
            dataSource[1].append(String(index))
        }
        
        //retain the previous selected value or reset to first position
        var selectedDate:String = ""
        var selectedYear:String = ""
        let dateStringLength = cardDetails[arrayIndex].field_value.characters.count
        if (cardDetails[arrayIndex].field_value == fieldName.expiryDate.rawValue ) {
            pickerSelectedValue.field_value = "\(dataSource[0][0]) - \(dataSource[1][0])"
        } else {
            pickerSelectedValue.field_value = cardDetails[arrayIndex].field_value
            selectedDate = (cardDetails[arrayIndex].field_value as NSString).substring(to: dateStringLength - 7)
            selectedYear = (cardDetails[arrayIndex].field_value as NSString).substring(from: dateStringLength - 4)
            
            if let sdi = Int(selectedDate) {
                selectedDate = String(format: "%02d", sdi)
            }else {
                selectedDate = dataSource[0][0]
            }
        }
        //load data's for picker
        pickerViewController?.pickerData = dataSource
        pickerViewController?.noOfComponents = 2
        pickerViewController?.pickerHeaderTitleLabel.text = fieldName.expiryDate.rawValue
        pickerViewController?.picker.reloadAllComponents()
        
        //retain values
        pickerViewController?.selectedValue1 = selectedDate
        pickerViewController?.selectedValue2 = selectedYear
        pickerViewController?.retainSelectedValue()
        
        pickerView.isHidden = false
        pickerViewController?.showPicker()
    }
    
    func showCountryPicker() {
        tableViewBottomSpace.constant = 0
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double((Int64(NSEC_PER_SEC) * 0)) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.tableView.scrollToRow(at: self.tableCellIndexPath, at: .top, animated: true)
        })
        
        var dataSource:[[String]] = [[]]
        countryCode = []
        //load country values & retain the selection
        pickerSelectedIndex = 0
        for index in 0...brand!.billingCountries.count - 1 {
            dataSource[0].insert(brand!.billingCountries[index].name, at: index)
            countryCode.insert(brand!.billingCountries[index].code, at: index)
            if (personalDetails[fieldName.country.hashValue].field_value == brand!.billingCountries[index].name) {
                pickerSelectedIndex = index
            }
        }
        
        if (personalDetails[fieldName.country.hashValue].field_value == fieldName.country.rawValue ) {
            pickerSelectedValue.field_value = dataSource[0][0]
        } else {
            pickerSelectedValue.field_value = personalDetails[fieldName.country.hashValue].field_value
        }
        
        //load data's for picker
        pickerViewController?.pickerData = dataSource
        pickerViewController?.noOfComponents = 1
        pickerViewController?.pickerHeaderTitleLabel.text = fieldName.country.rawValue
        pickerViewController?.picker.reloadAllComponents()
        pickerViewController?.picker.selectRow(pickerSelectedIndex, inComponent: 0, animated: false)
        
        //show picker animation
        pickerView.isHidden = false
        pickerViewController?.showPicker()
    }
    
    func showStatePicker() {
        if (personalDetails[fieldName.country.hashValue].ddl_value == "") {
            self.presentOkAlert("Alert", message: "Please choose country")
            return
        }
        tableViewBottomSpace.constant = 0
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double((Int64(NSEC_PER_SEC) * 0)) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.tableView.scrollToRow(at: self.tableCellIndexPath, at: .top, animated: true)
        })
        var dataSource:[[String]] = [[]]
        stateCode = []
        pickerSelectedIndex = 0
        
        //load state data's
        var i = -1
        for index in 0...brand!.billingStates.count - 1 {
            if (personalDetails[fieldName.country.hashValue].ddl_value == brand!.billingStates[index].countryCode) {
                i = i + 1
                dataSource[0].insert(brand!.billingStates[index].name, at: i)
                stateCode.insert(brand!.billingStates[index].code, at: i)
                if (personalDetails[fieldName.state.hashValue].field_value == brand!.billingStates[index].name) {
                    pickerSelectedIndex = i
                }
            }
        }
        if (personalDetails[fieldName.state.hashValue].field_value == fieldName.state.rawValue ) {
            pickerSelectedValue.field_value = dataSource[0][0]
        } else {
            pickerSelectedValue.field_value = personalDetails[fieldName.state.hashValue].field_value
        }
        
        //load data's for picker
        pickerViewController?.pickerData = dataSource
        pickerViewController?.noOfComponents = 1
        pickerViewController?.pickerHeaderTitleLabel.text = fieldName.state.rawValue
        pickerViewController?.picker.reloadAllComponents()
        pickerViewController?.picker.selectRow(pickerSelectedIndex, inComponent: 0, animated: false)
        
        pickerView.isHidden = false
        pickerViewController?.showPicker()
    }
    
    //show credit card type picker
    func showCreditCardTypePicker() {
        //array index position for credit card type
        let arrayIndex = fieldName.cardType.hashValue - personalDetails.count
        
        var dataSource:[[String]] = [[]]
        for index in 0...brand!.creditCardTypesAndImages.count-1 {
            dataSource[0].append(brand!.creditCardTypesAndImages[index].creditCardType)
        }
        
        tableViewBottomSpace.constant = 0
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double((Int64(NSEC_PER_SEC) * 0)) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.tableView.scrollToRow(at: self.tableCellIndexPath, at: .top, animated: true)
        })
        pickerViewController?.pickerData = dataSource
        pickerViewController?.noOfComponents = 1
        pickerViewController?.pickerHeaderTitleLabel.text = fieldName.cardType.rawValue
        pickerViewController?.picker.reloadAllComponents()
        pickerViewController?.picker.selectRow(0, inComponent: 0, animated: false)
        pickerSelectedValue.field_value = cardDetails[arrayIndex].field_value
        pickerViewController?.selectedValue1 = cardDetails[arrayIndex].field_value
        pickerViewController?.retainSelectedValue()
        pickerSelectedValue.field_image = brand!.creditCardTypesAndImages[0].thumbnailImageUrl
        pickerView.isHidden = false
        pickerViewController?.showPicker()
    }
    
    //diable textbox when focus on next textbox (or) done editing
    func disableTextBox() {
        let cell = tableView.cellForRow(at: tableCellIndexPath)
        if let cellType = cell as? AddPaymentSingleTextFieldTableViewCell {
            cellType.firstTextField.isEnabled = false
        }
    }
    
    // MARK: - Picker view delegates
    func pickerValueChanged(_ value:String,index :Int) {
        pickerSelectedValue.field_value = value
        pickerSelectedIndex = index
        
        let cell = tableView.cellForRow(at: tableCellIndexPath)
        if let cellType = cell as? AddPaymentSingleDDLTableViewCell {
            cellType.labelName.text = pickerSelectedValue.field_value
            
            //update value in personal details
            if (tableCellIndexPath.section == 0) {
                if (personalDetails[tableCellIndexPath.row].name == fieldName.country.rawValue) {
                    
                    // if country is choosen different then clear the selected state
                    if (personalDetails[tableCellIndexPath.row].ddl_value != countryCode[pickerSelectedIndex]) {
                        personalDetails[tableCellIndexPath.row - 2].ddl_value = ""
                        personalDetails[tableCellIndexPath.row - 2].field_value = fieldName.state.rawValue
                        let indexPath = IndexPath(row: tableCellIndexPath.row  - 2, section: tableCellIndexPath.section)
                        let cell2 = tableView.cellForRow(at: indexPath)
                        if let cellType2 = cell2 as? AddPaymentSingleDDLTableViewCell {
                            cellType2.labelName.text = fieldName.state.rawValue
                        }
                    }
                    
                    //store selected value
                    personalDetails[tableCellIndexPath.row].ddl_value = countryCode[pickerSelectedIndex]
                } else if (personalDetails[tableCellIndexPath.row].name == fieldName.state.rawValue) {
                    personalDetails[tableCellIndexPath.row].ddl_value = stateCode[pickerSelectedIndex]
                }
                personalDetails[tableCellIndexPath.row].field_value = pickerSelectedValue.field_value
                
                //update value in credit card details
            } else {
                cardDetails[tableCellIndexPath.row].field_value = pickerSelectedValue.field_value
                cardDetails[tableCellIndexPath.row].ddl_value = pickerSelectedValue.field_value
            }
        } else if let cellType = cell as? AddPaymentDDLImageTableviewCell {
            for cardType in brand!.creditCardTypesAndImages {
                if (cardType.creditCardType == pickerSelectedValue.field_value) {
                    
                    //set current card image
                    if let url = URL(string: (cardType.thumbnailImageUrl)){
                        cellType.imageName.isHidden = false
                        cellType.imageName.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
                        cellType.imageName.layer.masksToBounds = true
                        
                        cellType.imageName.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                            cellType.imageName.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                            cellType.imageName.layer.masksToBounds = false
                            cellType.imageName.image = image.value
                            
                        })
                    }
                    if (tableCellIndexPath.section == 0) {
                        personalDetails[tableCellIndexPath.row].field_value = pickerSelectedValue.field_value
                        personalDetails[tableCellIndexPath.row].field_image = cardType.thumbnailImageUrl
                    } else {
                        cardDetails[tableCellIndexPath.row].field_value = pickerSelectedValue.field_value
                        cardDetails[tableCellIndexPath.row].field_image = cardType.thumbnailImageUrl
                    }
                    break
                }
            }
        }
        tableViewBottomSpace.constant = 0
    }
    
    //close/hide the picker view
    func  closepickerScreen()  {
        pickerView.isHidden = true
        tableViewBottomSpace.constant = 0
    }
    
    //MARK: - Validate the user entered information
    func validateTextFields() ->String {
        var errorMessage:String = ""
        
        //check personal details are valid
        for personalDetail in personalDetails {
            if personalDetail.field_type == fieldType.singletextField.rawValue {
                if (personalDetail.mandatory && personalDetail.field_value.trim().isEmpty) {
                    errorMessage = "Please enter \(personalDetail.name.lowercased())"
                    return errorMessage
                }
                if (personalDetail.name == fieldName.zipCode.rawValue && personalDetail.field_value.characters.count > 0) {
                    if !(personalDetail.field_value.isZipCode()) {
                        errorMessage = "Please enter a valid \(personalDetail.name.lowercased())"
                        return errorMessage
                    }
                    
                }
                //check the ddl values are selected or not
            }  else if (personalDetail.mandatory && (personalDetail.name == personalDetail.field_value || personalDetail.ddl_value == "")) {
                errorMessage = "Please choose \(personalDetail.name.lowercased())"
                return errorMessage
                
            }
            // checking firstname and last name minimum characters validation
            if (personalDetail.name == fieldName.firstName.rawValue && personalDetail.field_value.characters.count < 3) || (personalDetail.name == fieldName.lastName.rawValue && personalDetail.field_value.characters.count < 3){
                errorMessage = "Please enter '" + personalDetail.name.lowercased() + "' minimum 3 characters"
                return errorMessage
            }
            //checking for street address validation
            if (personalDetail.name == fieldName.street1.rawValue && personalDetail.field_value.characters.count < 4) {
                errorMessage = "Please enter '" + personalDetail.name.lowercased() + "' minimum 4 characters"
                return errorMessage
            }
        }
        
        //check card details are valid
        for cardDetail in cardDetails {
            if (cardDetail.field_type == fieldType.singletextField.rawValue) {
                if (cardDetail.mandatory && cardDetail.field_value.trim().isEmpty) {
                    errorMessage = "Please enter \(cardDetail.name.lowercased())"
                    return errorMessage
                }
                if (cardDetail.name == fieldName.cardNumber.rawValue && cardDetail.field_value.characters.count > 0) {
                    if !(isValidCard(cardDetail.field_value)) {
                        errorMessage = "Please enter a valid \(cardDetail.name.lowercased())"
                        return errorMessage
                    }
                } else if (cardDetail.name == fieldName.ccv.rawValue && cardDetail.field_value.characters.count > 0) {
                    if !(cardDetail.field_value.isSecurityCode()) {
                        errorMessage = "Please enter a valid \(cardDetail.name.lowercased())"
                        return errorMessage
                    }
                }
            } else if (cardDetail.field_type == fieldType.ddl.rawValue && cardDetail.name != fieldName.cardType.rawValue) {
                if (cardDetail.mandatory && (cardDetail.name == cardDetail.field_value || cardDetail.ddl_value == "")) {
                    errorMessage = "Please enter \(cardDetail.name.lowercased())"
                    return errorMessage
                }
            }
        }
        
        // validate credit card expiry date
        let date = Date()
        let calendar = Calendar.current
        let components = (calendar as NSCalendar).components( [.month, .year], from: date)
        let expMonth = getMonthInString(cardDetails[fieldName.expiryDate.hashValue - personalDetails.count].field_value)
        let expYear = getYearInString(cardDetails[fieldName.expiryDate.hashValue - personalDetails.count].field_value)
        if components.year == Int(expYear) && Int(expMonth)<components.month{
            errorMessage = "Select a valid expiration date"
            return errorMessage
        }
        //
        return errorMessage
    }
    
    // MARK: - IBAction methods
    @IBAction func close(){
        popViewController()
    }
    
    @IBAction func submit(){
        let error = validateTextFields()
        if (error.characters.count > 0) {
            self.presentOkAlert("Error", message: (error))
            return
        }
        
        if (cardDetails[fieldName.savePayment.hashValue - personalDetails.count].field_value == "true") {
            saveUserPaymentDetails()
        } else  {
            saveBillingDetails()
        }
    }
    
    //switch button
    @IBAction func switchButtonChanged(_ sender: UISwitch) {
        //if save payment is mandatory then alert the user
        if !enableSavePayment {
            self.presentOkAlert("Alert", message: "Save this payment is mandatory", callback: {
                self.cardDetails[fieldName.savePayment.hashValue - self.personalDetails.count].field_value = "true"
                sender.setOn(true, animated: true)
            })
        }
        if (sender.isOn) {
            cardDetails[fieldName.savePayment.hashValue - personalDetails.count].field_value = "true"
        } else {
            cardDetails[fieldName.savePayment.hashValue - personalDetails.count].field_value = "false"
        }
    }
    
    
    // MARK: - Save & get information
    //save card details of the user
    func saveUserPaymentDetails() {
        //find last two digit for state code
        let stateCodeArray = personalDetails[fieldName.state.hashValue].ddl_value.components(separatedBy: "-")
        var stateCode = ""
        if stateCodeArray.count > 1 {
            stateCode = stateCodeArray[1]
        }
        
        let personalDetailsCount = personalDetails.count
        
        userPaymentDetails = InCommUserPaymentAccountDetails(firstName: personalDetails[fieldName.firstName.hashValue].field_value, lastName: personalDetails[fieldName.lastName.hashValue].field_value, streetAddress1: personalDetails[fieldName.street1.hashValue].field_value,streetAddress2: personalDetails[fieldName.street2.hashValue].field_value, city: personalDetails[fieldName.city.hashValue].field_value, stateProvince: stateCode, country: personalDetails[fieldName.country.hashValue].ddl_value, zipPostalCode: personalDetails[fieldName.zipCode.hashValue].field_value, creditCardExpirationMonth: getMonthInString(cardDetails[fieldName.expiryDate.hashValue - personalDetailsCount].field_value), creditCardNumber: (cardDetails[fieldName.cardNumber.hashValue - personalDetailsCount].field_value).stringByRemovingNonNumericCharacters(), creditCardVerificationCode: cardDetails[fieldName.ccv.hashValue - personalDetailsCount].field_value, creditCardTypeCode: cardDetails[fieldName.cardType.hashValue - personalDetailsCount].field_value, creditCardExpirationYear: getYearInString(cardDetails[fieldName.expiryDate.hashValue - personalDetailsCount].field_value), userId:InCommUserConfigurationService.sharedInstance.inCommUserId)
        
        if (saveBillingPayment) {
            saveBillingDetails()
            
            //if there is no gift card creation flow then associate card to user
        } else {
            SVProgressHUD.show(withStatus: "Loading...")
            SVProgressHUD.setDefaultMaskType(.clear)
            InCommUserPaymentService.addPaymentAccount(InCommUserConfigurationService.sharedInstance.inCommUserId, userPaymentAccount: userPaymentDetails!) { (userPaymentAccount, error) in
                SVProgressHUD.dismiss()
                if (error != nil) {
                    self.presentError(error)
                    return
                } else {
                    let paymentWithId = InCommSubmitPaymentWithId(amount: self.cardAmount, paymentAccountId: userPaymentAccount?.id, vestaOrgId: "", vestaWebSessionId: "")
                    self.delegate?.paymentAdded(nil, savePayment: nil, paymentDetailWithId: paymentWithId, creditCardDetail: userPaymentAccount, creditCardImage: self.cardDetails[fieldName.cardType.hashValue - personalDetailsCount].field_image, cardLastDigits: (userPaymentAccount?.creditCardNumber)!)
                    return
                }
            }
        }
    }
    
    //save the user payment information for card creation flow
    func saveBillingDetails() {
        //find last two digit for state code
        let stateTwoDigitCode = personalDetails[fieldName.state.hashValue].ddl_value.characters.count - 2
        let stateTwoDigitCodeInString = (personalDetails[fieldName.state.hashValue].ddl_value as NSString).substring(from: stateTwoDigitCode)
        
        let personalDetailsCount = personalDetails.count
        
        //save the InCommSubmitPayment in shared instance
        let paymentdetails = InCommSubmitPayment(amount: cardAmount, orderPaymentMethod:InCommOrderPaymentMethod.CreditCard, firstName: personalDetails[fieldName.firstName.hashValue].field_value, lastName: personalDetails[fieldName.lastName.hashValue].field_value, streetAddress1:personalDetails[fieldName.street1.hashValue].field_value , streetAddress2: personalDetails[fieldName.street2.hashValue].field_value, city:personalDetails[fieldName.city.hashValue].field_value, stateProvince: stateTwoDigitCodeInString, country: personalDetails[fieldName.country.hashValue].ddl_value, zipPostalCode: personalDetails[fieldName.zipCode.hashValue].field_value, creditCardNumber:(cardDetails[fieldName.cardNumber.hashValue - personalDetailsCount].field_value).stringByRemovingNonNumericCharacters(), creditCardVerificationCode: cardDetails[fieldName.ccv.hashValue - personalDetailsCount].field_value, creditCardExpirationMonth: getMonth(cardDetails[fieldName.expiryDate.hashValue - personalDetailsCount].field_value), creditCardExpirationYear: getYear(cardDetails[fieldName.expiryDate.hashValue - personalDetailsCount].field_value), creditCardType: cardDetails[fieldName.cardType.hashValue - personalDetailsCount].field_value)
        
        //get last 4 digit of character to display in add gift card screen
        // Let last 4 digits
        let cardNumberRemoveNonNumericValues = cardDetails[fieldName.cardNumber.hashValue - personalDetailsCount].field_value.stringByRemovingNonNumericCharacters()
        let cardLastFourDigits = String(cardNumberRemoveNonNumericValues.suffix(4))
        //pass card details via delegate
        delegate?.paymentAdded(paymentdetails, savePayment: userPaymentDetails, paymentDetailWithId: nil, creditCardDetail: nil, creditCardImage: cardDetails[fieldName.cardType.hashValue - personalDetailsCount].field_image, cardLastDigits: cardLastFourDigits)
    }
    
    // MARK: - TextField delegate
    //when showing keyboard, the textbox automatically scroll up
    @objc func keyboardDidShow(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        let keyboardFrame:NSValue = userInfo.value(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
        let keyboardRectangle = keyboardFrame.cgRectValue
        self.keyboardHeight = keyboardRectangle.height
        
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.tableViewBottomSpace.constant = self.keyboardHeight
            self.view.layoutIfNeeded()
        }, completion: { (completed) in
            if completed {
                self.tableView.scrollToRow(at: self.tableCellIndexPath, at: .top, animated: true)
            }
        }) 
    }
    
    //dismiss keyboard
    @objc func keyboardWillDismiss(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.tableViewBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }) 
    }
    
    // Update text changes values
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        var textValue = textField.text! as NSString
        textValue = textValue.replacingCharacters(in: range, with: string) as NSString
        if (tableCellIndexPath.section == 0) {
            // Validating max length of 20 characters in first name and last name and street1 city validation
            let fieldNameText = personalDetails[tableCellIndexPath.row].name
            let textFieldValue = textValue as String
            if string == ""{
                personalDetails[tableCellIndexPath.row].field_value = textFieldValue
                return true
            }
            else if fieldNameText == fieldName.firstName.rawValue || fieldNameText == fieldName.lastName.rawValue{
                if GiftCardAppConstants.nameValidation(textFieldValue){
                    personalDetails[tableCellIndexPath.row].field_value = textFieldValue
                    return true
                }else{
                    return false
                }
            }else if fieldNameText == fieldName.street1.rawValue{
                if self.addressValidation(textFieldValue){
                    personalDetails[tableCellIndexPath.row].field_value = textFieldValue
                    return true
                }else{
                    return false
                }
            }else if fieldNameText == fieldName.city.rawValue{
                if self.cityValidation(textFieldValue){
                    personalDetails[tableCellIndexPath.row].field_value = textFieldValue
                    return true
                }else{
                    return false
                }
            }else{
                personalDetails[tableCellIndexPath.row].field_value = textFieldValue
                return true
            }
            
        } else {
            cardDetails[tableCellIndexPath.row].field_value = textValue as String
            return true
        }
    }
    
    
    //navigate text field to next box
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let cell = tableView.cellForRow(at: tableCellIndexPath)
        var nextIndex:IndexPath = tableCellIndexPath
        if let cellType = cell as? AddPaymentSingleTextFieldTableViewCell {
            cellType.firstTextField.isEnabled = false
            cellType.firstTextField.resignFirstResponder()
            if (tableCellIndexPath.row+1 <= personalDetails.count - 1) {
                nextIndex = IndexPath(row: tableCellIndexPath.row+1, section:tableCellIndexPath.section);
            } else {
                //if the first section is completed go for second section
                nextIndex = IndexPath(row: 0, section:tableCellIndexPath.section+1);
            }
        }
        
        let resultCell = tableView.cellForRow(at: nextIndex)
        if let cellType = resultCell as? AddPaymentSingleTextFieldTableViewCell {
            
            tableView.scrollToRow(at: tableCellIndexPath, at: .top, animated: true)
            cellType.firstTextField.isEnabled = true
            cellType.firstTextField.becomeFirstResponder()
            tableCellIndexPath = nextIndex
            return true
        } else {
            view.endEditing(true)
            return false
        }
    }
    
    
    
    //MARK: - get screen values
    //MARK: Retain add payment values when user try to edit
    func fillpaymentDetails() {
        let personalDetailsCount = personalDetails.count
        
        if let payment = userNewlyAddedCreditCard {
            headerLabel.text = "Edit Payment"
            //personal details array
            personalDetails[fieldName.firstName.hashValue].field_value = (payment.firstName)!
            personalDetails[fieldName.lastName.hashValue].field_value = (payment.lastName)!
            personalDetails[fieldName.street1.hashValue].field_value = (payment.streetAddress1)!
            personalDetails[fieldName.street2.hashValue].field_value = (payment.streetAddress2)!
            personalDetails[fieldName.country.hashValue].field_value = getCountryName((payment.country)!)
            personalDetails[fieldName.country.hashValue].ddl_value = (payment.country)!
            personalDetails[fieldName.state.hashValue].field_value = getStateName((payment.country)!, stateCode: (payment.stateProvince)!)
            personalDetails[fieldName.state.hashValue].ddl_value = (payment.country)! + "-" + (payment.stateProvince)!
            personalDetails[fieldName.city.hashValue].field_value = (payment.city)!
            personalDetails[fieldName.zipCode.hashValue].field_value = (payment.zipPostalCode)!
            
            //card details array
            cardDetails[fieldName.cardType.hashValue - personalDetailsCount].field_value = (payment.creditCardType)!
            
            // set credit card type
        InCommGiftCardBrandDetails.sharedInstance.getCreditCardTypeImageNameOrImageURLForCreditCardType((payment.creditCardType)!, callback: { (imageURL, imageName) in
                var imageNameOrImageURL = imageName
                if imageURL != nil && !imageURL!.isEmpty{
                    imageNameOrImageURL = imageURL
                }
                cardDetails[fieldName.cardType.hashValue - personalDetailsCount].field_image = imageNameOrImageURL!
            })
            
            cardDetails[fieldName.cardNumber.hashValue - personalDetailsCount].field_value = (payment.creditCardNumber)!
            cardDetails[fieldName.ccv.hashValue - personalDetailsCount].field_value = (payment.creditCardVerificationCode)!
            cardDetails[fieldName.expiryDate.hashValue - personalDetailsCount].field_value = String(format: "%d - %d",(payment.creditCardExpirationMonth)!,(payment.creditCardExpirationYear)!)
            cardDetails[fieldName.expiryDate.hashValue - personalDetailsCount].ddl_value = String(format: "%d - %d",(payment.creditCardExpirationMonth)!,(payment.creditCardExpirationYear)!)
            if (userSavedPaymentDetails != nil) {
                cardDetails[fieldName.savePayment.hashValue - personalDetailsCount].field_value = "true"
            } else {
                cardDetails[fieldName.savePayment.hashValue - personalDetailsCount].field_value = "false"
            }
            tableView.reloadData()
        } else {
            // Preselect Default Country Code as US
            personalDetails[fieldName.country.hashValue].field_value = getCountryName((GiftCardAppConstants.giftCardPaymentDefaultCountryCode))
            personalDetails[fieldName.country.hashValue].ddl_value = GiftCardAppConstants.giftCardPaymentDefaultCountryCode
            if let user = UserService.sharedUser{
                if let firstName = user.firstName{
                    personalDetails[fieldName.firstName.hashValue].field_value = firstName
                }
                if let lastName = user.lastName{
                    personalDetails[fieldName.lastName.hashValue].field_value = lastName
                }
            }
        }
    }
    
    //get country name based on country code
    func getCountryName(_ countryCode:String) -> String {
        for country in (brand?.billingCountries)! {
            if (countryCode == country.code) {
                return country.name
            }
        }
        return ""
    }
    
    //get state name based on country code & state code
    func getStateName(_ countryCode:String, stateCode:String) -> String {
        let fullStateCode = countryCode+"-"+stateCode
        for state in (brand?.billingStates)! {
            if (fullStateCode == state.code) {
                return state.name
            }
        }
        return ""
    }
    
    func getMonth(_ date:String) -> UInt16 {
        let index2 = date.range(of: " - ", options: .backwards)?.lowerBound
        let month = UInt16(String(date[..<index2!]))!
        return month
    }
    func getYear(_ date:String) -> UInt16 {
        let year = UInt16(String(date.suffix(4)))
        return year!
    }
    
    func getMonthInString(_ date:String) -> String {
        let index2 = date.range(of: " - ", options: .backwards)?.lowerBound
        let month:String = String(date[..<index2!])
        return month
    }
    func getYearInString(_ date:String) -> String {
        let year:String = String(date.suffix(4))
        return year
    }
    
    // MARK: - Table field initialization
    func initializeObject() {
        var data:FieldSetDataModel = FieldSetDataModel()
        data.section = (section.personalDetails.rawValue)
        data.name = (fieldName.firstName.rawValue)
        data.mandatory = true
        data.keyboardType = UIKeyboardType.alphabet
        data.field_value = ""
        data.field_type = (fieldType.singletextField.rawValue)
        personalDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.personalDetails.rawValue)
        data.name = (fieldName.lastName.rawValue)
        data.mandatory = true
        data.keyboardType = UIKeyboardType.alphabet
        data.field_value = ""
        data.field_type = (fieldType.singletextField.rawValue)
        personalDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.personalDetails.rawValue)
        data.name = (fieldName.street1.rawValue)
        data.mandatory = true
        data.field_value = ""
        data.field_type = (fieldType.singletextField.rawValue)
        personalDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.personalDetails.rawValue)
        data.name = (fieldName.street2.rawValue)
        data.mandatory = false
        data.field_value = ""
        data.field_type = (fieldType.singletextField.rawValue)
        personalDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.personalDetails.rawValue)
        data.name = (fieldName.city.rawValue)
        data.mandatory = true
        data.field_value = ""
        data.field_type = (fieldType.singletextField.rawValue)
        personalDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.personalDetails.rawValue)
        data.name = (fieldName.state.rawValue)
        data.mandatory = true
        data.field_value = fieldName.state.rawValue
        data.field_type = (fieldType.ddl.rawValue)
        personalDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.personalDetails.rawValue)
        data.name = (fieldName.zipCode.rawValue)
        data.mandatory = true
        data.keyboardType = .numbersAndPunctuation
        data.field_value = ""
        data.field_type = (fieldType.singletextField.rawValue)
        personalDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.personalDetails.rawValue)
        data.name = (fieldName.country.rawValue)
        data.mandatory = true
        data.field_value = fieldName.country.rawValue
        data.field_type = (fieldType.ddl.rawValue)
        personalDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.cardDetails.rawValue)
        data.name = (fieldName.cardType.rawValue)
        data.mandatory = true
        data.field_value = (brand?.creditCardTypesAndImages[0].creditCardType)!
        
        // Credit card image url or name for type
  InCommGiftCardBrandDetails.sharedInstance.getCreditCardTypeImageNameOrImageURLForCreditCardType((data.field_value), callback: { (imageURL, imageName) in
            var imageNameOrImageURL = imageName
            if imageURL != nil && !imageURL!.isEmpty{
                imageNameOrImageURL = imageURL
            }
            data.field_image = imageNameOrImageURL!
        })
        
        data.field_type = (fieldType.ddlWithImage.rawValue)
        cardDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.cardDetails.rawValue)
        data.name = (fieldName.cardNumber.rawValue)
        data.mandatory = true
        data.keyboardType = .numbersAndPunctuation
        data.field_value = ""
        data.field_type = (fieldType.singletextField.rawValue)
        cardDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.cardDetails.rawValue)
        data.name = (fieldName.ccv.rawValue)
        data.mandatory = true
        data.keyboardType = .numbersAndPunctuation
        data.field_value = ""
        data.field_type = (fieldType.singletextField.rawValue)
        cardDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.cardDetails.rawValue)
        data.name = (fieldName.expiryDate.rawValue)
        data.mandatory = true
        data.field_value = fieldName.expiryDate.rawValue
        data.field_type = (fieldType.ddl.rawValue)
        cardDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.cardDetails.rawValue)
        data.name = (fieldName.termsText.rawValue)
        data.mandatory = false
        data.field_value = fieldName.termsText.rawValue
        data.field_type = (fieldType.navigation.rawValue)
        cardDetails.append(data)
        
        data = FieldSetDataModel()
        data.section = (section.cardDetails.rawValue)
        data.name = (fieldName.savePayment.rawValue)
        data.mandatory = false
        data.field_value = "true"
        data.field_type = (fieldType.checkingField.rawValue)
        cardDetails.append(data)
    }
    
    //MARK: - Regex for valid credit card
    func isValidCard(_ testStr:String) -> Bool {
        let validCardDetails = testStr.stringByRemovingNonNumericCharacters()
        let creditCardRegEx = "[0-9]{12,16}?"
        let creditCardTest = NSPredicate(format:"SELF MATCHES %@", creditCardRegEx)
        return creditCardTest.evaluate(with: validCardDetails)
    }
    
    // MARK: Gift card webview delegate
    func webviewURLPath(_ webview: GiftCardWebview) -> String {
        return GiftCardAppConstants.jambaGiftCardTermsUrl
    }
    
    func webviewHeaderText(_ webview: GiftCardWebview) -> String {
        return "Terms & Conditions"
    }
    
    // MARK: de alloc Notification
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    // MARK: - Address validation
    func addressValidation(_ textValue:String) -> Bool{
        let regexText = NSPredicate(format: "SELF MATCHES %@", "^[a-zA-Z0-9.,-:/;\"')( ]+$")
        return regexText.evaluate(with: textValue)
    }
    
    // MARK: - City validation
    func cityValidation(_ textValue:String) -> Bool{
        let regexText = NSPredicate(format: "SELF MATCHES %@", "^[a-zA-Z- ]+$")
        return regexText.evaluate(with: textValue)
    }
    
    //MARK:- Card IO Delegates
    // Action for camera button pressed
    func creditCardImageSelected() {
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
            //credit card number
            let cardNumber:String = info.cardNumber.formatCreditCardNumber()
            cardDetails[fieldName.cardNumber.hashValue - personalDetails.count].field_value = cardNumber
            //credit card type (VISA,AMEX)
            let firstLetter = cardNumber.characters.first == nil ? "" : "\(cardNumber.characters.first!)"
            
            // for image name or image url value
            InCommGiftCardBrandDetails.sharedInstance.getCreditCardTypeImageNameOrImageURL("\(firstLetter)", callback: { (imageURL, imageName) in
                var imageNameOrImageURL = imageName
                if imageURL != nil && !imageURL!.isEmpty{
                    imageNameOrImageURL = imageURL!
                }
                cardDetails[fieldName.cardType.hashValue - personalDetails.count].field_image = imageNameOrImageURL!
            })
            // for credit card type
            cardDetails[fieldName.cardType.hashValue - personalDetails.count].field_value =
                InCommGiftCardBrandDetails.sharedInstance.getCreditCardType("\(firstLetter)")
            //expiry Dates
            if info.expiryMonth != 0 {
                cardDetails[fieldName.expiryDate.hashValue - personalDetails.count].field_value = String(format:"%2d - %4d",info.expiryMonth,info.expiryYear)
                cardDetails[fieldName.expiryDate.hashValue - personalDetails.count].ddl_value = pickerSelectedValue.field_value
            }
        }
        paymentViewController.dismiss(animated: true) {
            self.tableView.reloadData()
        }
    }
    
    func formatCreditCardNumber(_ textField: UITextField) -> String {
        var creditCardNumber = textField.text!
        creditCardNumber = creditCardNumber.stringByRemovingNonNumericCharacters()
        
        //set credit card image based on credit card number
        setCreditCardImage(creditCardNumber, index: textField.tag)
        textField.text = creditCardNumber.formatCreditCardNumber()
        return textField.text ?? ""
    }
    //let indexPath = IndexPath(row: tableCellIndexPath.row  - 1, section: tableCellIndexPath.section)
    //set credit card image
    func setCreditCardImage(_ creditCardNumber:String, index: Int) {
        let cell = tableView.cellForRow(at: IndexPath(row: tableCellIndexPath.row-1, section: tableCellIndexPath.section))
        if let cellType = cell as? AddPaymentDDLImageTableviewCell {
            let firstLetter = creditCardNumber.characters.first == nil ? "" : "\(creditCardNumber.characters.first!)"
            // For credit card image name or image url
            InCommGiftCardBrandDetails.sharedInstance.getCreditCardTypeImageNameOrImageURL(firstLetter
                , callback: { (imageURL, imageName) in
                    var imageNameOrImageURL = imageName
                    if imageURL != nil && !imageURL!.isEmpty{
                        imageNameOrImageURL = imageURL
                    }
                    cardDetails[fieldName.cardType.hashValue - personalDetails.count].field_image = imageNameOrImageURL!
            })
            // For credit card type
            cardDetails[fieldName.cardType.hashValue - personalDetails.count].field_value = InCommGiftCardBrandDetails.sharedInstance.getCreditCardType(firstLetter)
            cellType.setCreditCardImage(creditCardNumber)
        }
    }
}
