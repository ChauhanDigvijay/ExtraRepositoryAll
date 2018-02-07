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
import SVProgressHUD

//MARK: AddNewGiftCardViewController Delegate
protocol AddNewGiftCardViewControllerDelegate: class {
    func preStatusValidation()
}

class AddNewGiftCardViewController: UIViewController,UITableViewDelegate, AmountSelectionViewControllerDelegate, PickerViewControllerDelegate, TemplateSelectionViewControllerDelegate, PurchaserDetailsViewControllerDelegate, RecepientsDetailViewControllerDelegate, AddPaymentViewControllerDelegate, PaymentViewControllerPassObjectDelegate,PaySafeWebViewDelegate {
    
    //field name
    enum addNewFieldName : String {
        case giftCardPreview    = "Gift Card Preview"
        case selectCardAmount   = "Select Card Amount"
        case selectQuantity     = "Select Quantity"
        case paymentAccount     = "Payment Account"
        case purchaserDetails   = "Purchaser Details"
        case recipient          = "Recipient"
        case totalOrderSummary  = "Total Order Summary"
    }
    
    //segue identifiers
    enum SegueIdentifiers : String {
        case showPaymentListVC       = "showPaymentListVC"
        case AmountSelectionVC       = "AmountSelectionVC"
        case showPickerViewVC        = "showPickerViewVC"
        case showTemplateSelectionVC = "showTemplateSelectionVC"
        case showPurchaserDetailsVC  = "showPurchaserDetailsVC"
        case showRecepientsDetailVC  = "showRecepientsDetailVC"
        case totalOrderSummary       = "Total Order Summary"
    }
    
    @IBOutlet weak var tableView        : UITableView!
    @IBOutlet weak var pickerView       : UIView!
    private var pickerViewController    : PickerViewController?
    private var addPaymentViewController: AddPaymentViewController?
    weak var delegate                   : AddNewGiftCardViewControllerDelegate?
    var addNewCardCellDetails           : [AddNewGiftCardDataModel] = []        //array for cell details
    var quantitySelectedValue           : String = "1"                          //retain quantity value
    var userPaymentDetails              : InCommUserPaymentAccountDetails?      //retain payment details
    var userSelectedTemplate            : InCommBrandCardImage?                 //retain template value
    var giftCardBrand                   : InCommBrand?                          //brand details
    var initialAmountINString           : String = ""                           //initial amount format
    var userSubmittedGiftCards          :[InCommSubmittedOrderItemGiftCard] = []
    var userSubmittedPaymentAccountDetails: InCommUserPaymentAccountDetails? = nil
    var inCommSubmitOrder               :InCommSubmitOrder?
    var submitOrderStatus               :Bool = false
    var associateCardsStatus            :Bool = false
    var associatePaymentStatus          :Bool = false
    var submitPromoOrderStatus          :Bool = false
    var operationDoneByUser             :Bool = false
    
    var paySafeWebView:PaySafeWebView = PaySafeWebView(frame: CGRectZero)
    var inCommOrgId:String?
    var inCommSessionId:String?
    
    // InComm order id
    var inCommOrderId:String?

    override func viewDidLoad() {
        operationDoneByUser = false         //initially the user didn't do any operation
        giftCardBrand = InCommGiftCardBrandDetails.sharedInstance.brand
        initializeObject()
        createVestaWebSession { (vestaWebSession, error) in
            if error == nil{
                self.startWebSession()
            }
        }
        // Get incomm order id to proceed an order
        GiftCardCreationService.sharedInstance.getInCommOrderId { (inCommOrderId, error) in
            if error == nil{
                self.inCommOrderId = inCommOrderId
            }
        }

        pickerView.hidden = true
        super.viewDidLoad()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == SegueIdentifiers.showPaymentListVC.rawValue {
            let targetController =  segue.destinationViewController as! PaymentListViewController
            targetController.headerTitle = GiftCardAppConstants.addNewPaymentTitle
            
            //remove "$" from card amount
            var cardAmount = addNewCardCellDetails[addNewFieldName.selectCardAmount.hashValue].fieldRightSideValue
            if (addNewCardCellDetails[addNewFieldName.selectCardAmount.hashValue].fieldRightSideValue.containsString("$")) {
                cardAmount = String(cardAmount.characters.dropFirst())
            }
            
            targetController.cardAmount = Double(cardAmount)!
            targetController.enableSavePayment = true
            targetController.usePaymentList = true
            targetController.userNewlyAddedCreditCard = GiftCardCreationService.sharedInstance.paymentDetails
            targetController.userSelectedCard = GiftCardCreationService.sharedInstance.paymentDetailsId
            targetController.userSavedPaymentDetails = GiftCardCreationService.sharedInstance.savePaymentDetails
            targetController.addPaymentDetailDelegate = self
            targetController.paymentDetailDelegate = self
            
        } else if segue.identifier == SegueIdentifiers.AmountSelectionVC.rawValue {
            let targetController = segue.destinationViewController as! AmountSelectionViewController
            targetController.userSelectedAmount =  GiftCardCreationService.sharedInstance.cardAmount
            targetController.delegate = self
        } else if segue.identifier == SegueIdentifiers.showPickerViewVC.rawValue {
            pickerViewController = segue.destinationViewController as? PickerViewController
            pickerViewController?.delegate = self
        } else if segue.identifier == SegueIdentifiers.showTemplateSelectionVC.rawValue {
            let targetController = segue.destinationViewController as! TemplateSelectionViewController
            targetController.selectedBrandImageCode = GiftCardCreationService.sharedInstance.incommBrandImageCode
            targetController.delegate = self
        } else if segue.identifier == SegueIdentifiers.showPurchaserDetailsVC.rawValue {
            let targetController = segue.destinationViewController as! PurchaserDetailsViewController
            targetController.purchaserInformationFromPayment = GiftCardCreationService.sharedInstance.purchaserInformationFromPayment
            targetController.newPaymentDetails = GiftCardCreationService.sharedInstance.paymentDetails
            targetController.existingPayment = GiftCardCreationService.sharedInstance.existingPaymentDetails
            targetController.purchaserDetail = GiftCardCreationService.sharedInstance.purchaserDetails
            targetController.delegate = self
        } else if segue.identifier == SegueIdentifiers.showRecepientsDetailVC.rawValue {
            let targetController = segue.destinationViewController as! RecepientsDetailViewController
            targetController.purchaserDetails = GiftCardCreationService.sharedInstance.purchaserDetails
            targetController.recipientDetails = GiftCardCreationService.sharedInstance.recipientDetails
            targetController.orderMessage = GiftCardCreationService.sharedInstance.recipientMessage
            targetController.recipientSelf = GiftCardCreationService.sharedInstance.recipientSelf
            targetController.delegate = self
        }
    }
    
    //MARK: - IBAction methods
    //close screen - before closing ask confirmation
    @IBAction func close(){
        //if user doen't do any changes close the screen otherwise alert the user
        //check user done any operation
        if !operationDoneByUser {
                // Update gift card order cancel status
                var inCommOrderStatusDetails: InCommOrderStatusDetails = InCommOrderStatusDetails()
                inCommOrderStatusDetails.inCommOrderId = self.inCommOrderId
                inCommOrderStatusDetails.orderStatus = InCommOrderStatusDetails.OrderStatus.CANCELLED.rawValue
                GiftCardCreationService.sharedInstance.updateInCommOrderStatus(inCommOrderStatusDetails)
            closeScreen()
            return
        }
        
        self.presentConfirmationWithYesOrNo("Attention", message: "Do you wish to cancel current gift card order?") { (confirmed) in
            if (confirmed) {
                //clear all values store in shared instance
                self.closeScreen()
            }
        }
    }
    
    func closeScreen() {
        self.inCommOrderId = nil
        GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    //MARK: Save Gift Card
    @IBAction func saveGiftCard(){
        submitOrder()
    }
    
    // MARK: - Table view Delegates
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return addNewCardCellDetails.count;
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.giftCardPreview.rawValue) {
            var width = tableView.frame.width - (GiftCardAppConstants.tableViewGiftCardLeadingAndTrailingSpace)  //table view width - (leading & trailing space)
            
            // for ipad
            if !GiftCardAppConstants.isiPhone {
                width = tableView.frame.width / GiftCardAppConstants.GiftCardRatioWidth
            }
            
            if (addNewCardCellDetails[indexPath.row].fieldRightSideValue == "") {
                return (width * CGFloat(GiftCardAppConstants.GiftCardRatioHeight)) + GiftCardAppConstants.tableViewGiftCardTopAndsBottomSpace
            } else {
                return (width * CGFloat(GiftCardAppConstants.GiftCardRatioHeight)) + (2 * GiftCardAppConstants.tableViewGiftCardTopAndsBottomSpace)
            }
        } else {
            return GiftCardAppConstants.tableViewCellHeight
        }
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.paymentAccount.rawValue) {
            let cell = tableView.dequeueReusableCellWithIdentifier("AddNewGiftCardImageTableViewCell", forIndexPath: indexPath) as! AddNewGiftCardImageTableViewCell
            
            var imageHide = false       //image need to show or hide
            if (addNewCardCellDetails[indexPath.row].fieldCreditCardTypeImage == "") {
                imageHide = true
            }
            
            cell.setCellData(addNewCardCellDetails[indexPath.row].fieldName, field_value: addNewCardCellDetails[indexPath.row].fieldRightSideValue, imageHidden: imageHide, imageURL: addNewCardCellDetails[indexPath.row].fieldCreditCardTypeImage)
            
            return cell
        }else if (indexPath.row > 0) {
            let cell = tableView.dequeueReusableCellWithIdentifier("AddNewGiftCardViewCell", forIndexPath: indexPath) as! AddNewGiftCardViewCell
            
            cell.setCellData(addNewCardCellDetails[indexPath.row].fieldName, field_value: addNewCardCellDetails[indexPath.row].fieldRightSideValue, navigationImage: addNewCardCellDetails[indexPath.row].fieldImage)
            
            if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.totalOrderSummary.rawValue) {
                cell.setCellBackgroundlightColor()
            }
            
            return cell
        } else {
            let cell = tableView.dequeueReusableCellWithIdentifier("AddGiftCardPreviewViewCell", forIndexPath: indexPath) as! AddGiftCardPreviewViewCell
            
            var imageHide = false       //image need to show or hide
            if (addNewCardCellDetails[indexPath.row].fieldRightSideValue == "") {
                imageHide = true
            }
            
            cell.setCellData(imageHide, imageURL: addNewCardCellDetails[indexPath.row].fieldRightSideValue)
            
            return cell
        }
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.giftCardPreview.rawValue) {
            performSegueWithIdentifier(SegueIdentifiers.showTemplateSelectionVC.rawValue, sender: nil);
        } else if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.selectCardAmount.rawValue) {
            performSegueWithIdentifier(SegueIdentifiers.AmountSelectionVC.rawValue, sender: nil);
        } else if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.selectQuantity.rawValue) {
            showQuantityPicker()
        } else if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.paymentAccount.rawValue) {
            performSegueWithIdentifier(SegueIdentifiers.showPaymentListVC.rawValue, sender: nil);
        } else if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.purchaserDetails.rawValue) {
            if (GiftCardCreationService.sharedInstance.paymentDetails == nil && GiftCardCreationService.sharedInstance.paymentDetailsId == nil)  {
                self.presentOkAlert("Alert", message: "Please enter payment information")
            } else {
                performSegueWithIdentifier(SegueIdentifiers.showPurchaserDetailsVC.rawValue, sender: nil);
            }
        } else if (addNewCardCellDetails[indexPath.row].fieldName == addNewFieldName.recipient.rawValue) {
            if GiftCardCreationService.sharedInstance.purchaserDetails == nil {
                self.presentOkAlert("Alert", message: "Please enter purchaser details")
            } else {
                performSegueWithIdentifier(SegueIdentifiers.showRecepientsDetailVC.rawValue, sender: nil);
            }
            
        }
        
    }
    
    // MARK: - Amount selection screen delegate
    func selectedAmount(value:String) {
        operationDoneByUser = true
        //calculate total summary
        let amountString:String = String(value.characters.dropFirst())
        addNewCardCellDetails[addNewFieldName.selectCardAmount.hashValue].fieldRightSideValue = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,Double(amountString)!)
        let amount = Double(amountString)! * Double (addNewCardCellDetails[addNewFieldName.selectQuantity.hashValue].fieldRightSideValue)!
        addNewCardCellDetails[addNewFieldName.totalOrderSummary.hashValue].fieldRightSideValue = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,amount)
        
        //update card amount in corresponding cell
        var indexPath = NSIndexPath(forRow: addNewFieldName.selectCardAmount.hashValue, inSection: 0)
        var cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AddNewGiftCardViewCell {
            cellType.value.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,Double(amountString)!)
        }
        
        //update total summary in cell
        indexPath = NSIndexPath(forRow: addNewFieldName.totalOrderSummary.hashValue, inSection: 0)
        cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AddNewGiftCardViewCell {
            cellType.value.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,amount)
        }
        if (GiftCardCreationService.sharedInstance.paymentDetails != nil || GiftCardCreationService.sharedInstance.paymentDetailsId != nil) {
            GiftCardCreationService.sharedInstance.updatePaymentDetailsAmount(amount)
            
        }
        GiftCardCreationService.sharedInstance.setCardAmout(addNewCardCellDetails[addNewFieldName.selectCardAmount.hashValue].fieldRightSideValue)
    }
    
    // MARK: - Picker view delegates
    func showQuantityPicker() {
        var dataSource:[[String]] = [[]]
        
        for index in 0...giftCardBrand!.quantities.count - 1 {
            if let quantity = giftCardBrand?.quantities[index] {
                dataSource[0].append(String(format: "%d",quantity))
            }
        }
        
        pickerViewController?.pickerData = dataSource
        pickerViewController?.noOfComponents = 1
        pickerViewController?.selectedValue1 = addNewCardCellDetails[addNewFieldName.selectQuantity.hashValue].fieldRightSideValue
        pickerViewController?.pickerHeaderTitleLabel.text = addNewFieldName.selectQuantity.rawValue
        if addNewCardCellDetails[addNewFieldName.selectQuantity.hashValue].fieldRightSideValue != "" {
            quantitySelectedValue = addNewCardCellDetails[addNewFieldName.selectQuantity.hashValue].fieldRightSideValue
        }
        pickerViewController?.picker.reloadAllComponents()
        pickerViewController?.retainSelectedValue()
        
        let indexPath = NSIndexPath(forRow: addNewFieldName.selectQuantity.hashValue, inSection: 0)
        tableView.scrollToRowAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
        
        pickerView.hidden = false
        pickerViewController?.showPicker()
    }
    
    func pickerValueChanged(value:String,index :Int) {
        operationDoneByUser = true
        quantitySelectedValue = value
        //update quantity value in corresponding cell
        var indexPath = NSIndexPath(forRow: addNewFieldName.selectQuantity.hashValue, inSection: 0)
        var cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AddNewGiftCardViewCell {
            cellType.value.text = quantitySelectedValue
            addNewCardCellDetails[addNewFieldName.selectQuantity.hashValue].fieldRightSideValue = quantitySelectedValue
        }
        
        //calculate total summary
        let amountString:String = String(addNewCardCellDetails[addNewFieldName.selectCardAmount.hashValue].fieldRightSideValue.characters.dropFirst())
        let amount = Double(amountString)! * Double(addNewCardCellDetails[addNewFieldName.selectQuantity.hashValue].fieldRightSideValue)!
        addNewCardCellDetails[addNewFieldName.totalOrderSummary.hashValue].fieldRightSideValue = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,amount)
        
        //update value in total summary cell
        indexPath = NSIndexPath(forRow: addNewFieldName.totalOrderSummary.hashValue, inSection: 0)
        cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AddNewGiftCardViewCell {
            cellType.value.text = addNewCardCellDetails[addNewFieldName.totalOrderSummary.hashValue].fieldRightSideValue
        }
        if (GiftCardCreationService.sharedInstance.paymentDetails != nil || GiftCardCreationService.sharedInstance.paymentDetailsId != nil) {
            GiftCardCreationService.sharedInstance.updatePaymentDetailsAmount(amount)
            
        }
        GiftCardCreationService.sharedInstance.setCardAmout(addNewCardCellDetails[addNewFieldName.selectCardAmount.hashValue].fieldRightSideValue)
        GiftCardCreationService.sharedInstance.setCardQuantity(Int(quantitySelectedValue)!)
    }
    
    func  closepickerScreen()  {
        pickerView.hidden = true
    }
    
    // MARK: - Template selection screen delegates
    func cardSelected(value:InCommBrandCardImage) {
        operationDoneByUser = true
        GiftCardCreationService.sharedInstance.setIncommBrandImageCode(value.imageCode)
        userSelectedTemplate = value
        addNewCardCellDetails[addNewFieldName.giftCardPreview.hashValue].fieldRightSideValue = userSelectedTemplate!.imageUrl
        tableView.reloadData()
    }
    
    // MARK: - Purchaser information screen delegates
    func purchaserDetailSaved(purchaserDetails:InCommOrderPurchaser,purchaserDetailsFromPayment:Bool) {
        //update purchaser details value
        addNewCardCellDetails[addNewFieldName.purchaserDetails.hashValue].fieldRightSideValue = purchaserDetails.emailAddress
        var indexPath = NSIndexPath(forRow: addNewFieldName.purchaserDetails.hashValue, inSection: 0)
        var cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AddNewGiftCardViewCell {
            cellType.value.text = purchaserDetails.emailAddress
        }
        
        //update recipient details. in case in purchaser email changed the recipient email need to change
        if let recipientDetail = GiftCardCreationService.sharedInstance.recipientDetails {
            indexPath = NSIndexPath(forRow: addNewFieldName.recipient.hashValue, inSection: 0)
            cell = tableView.cellForRowAtIndexPath(indexPath)
            addNewCardCellDetails[addNewFieldName.recipient.hashValue].fieldRightSideValue = recipientDetail.emailAddress!
            if let cellType = cell as? AddNewGiftCardViewCell {
                cellType.value.text = recipientDetail.emailAddress
            }
        }
        
        //update shared Instance & recipient values
        GiftCardCreationService.sharedInstance.setPurchaserDetails(purchaserDetails)
        GiftCardCreationService.sharedInstance.setPurchaserInformationFromPayment(purchaserDetailsFromPayment)
        
        //update recipient Details
        if var recipientDetail = GiftCardCreationService.sharedInstance.recipientDetails {
            if ((GiftCardCreationService.sharedInstance.recipientSelf) != nil && GiftCardCreationService.sharedInstance.recipientSelf) {
                recipientDetail.firstName = purchaserDetails.firstName
                recipientDetail.lastName = purchaserDetails.lastName
                recipientDetail.emailAddress = purchaserDetails.emailAddress
                
                GiftCardCreationService.sharedInstance.setReceipientDetails(recipientDetail)
            }
            
        }
        
        
    }
    
    // MARK: - Recipient information screen delegates
    func receipientDetailSaved(recipientDetails:InCommOrderRecipientDetails, message:String, recipientSelf:Bool) {
        //update value in local object
        addNewCardCellDetails[addNewFieldName.recipient.hashValue].fieldRightSideValue = recipientDetails.emailAddress!
        
        //update value in cell
        let indexPath = NSIndexPath(forRow: addNewFieldName.recipient.hashValue, inSection: 0)
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AddNewGiftCardViewCell {
            cellType.value.text = recipientDetails.emailAddress
        }
        
        //update value in shared instance
        GiftCardCreationService.sharedInstance.setReceipientDetails(recipientDetails)
        GiftCardCreationService.sharedInstance.setReceipientMessage(message)
        GiftCardCreationService.sharedInstance.setReceipientSelf(recipientSelf)
    }
    
    //MARK: - Payment screen delegate
    func paymentAdded(addedPaymentDetails:InCommSubmitPayment?, savePayment:InCommUserPaymentAccountDetails?, paymentDetailWithId: InCommSubmitPaymentWithId?, creditCardDetail: InCommUserPaymentAccount?, creditCardImage:String, cardLastDigits:String) {
        operationDoneByUser = true
        popToRootViewController()
        //reset values
        if (addedPaymentDetails != nil) {
            GiftCardCreationService.sharedInstance.setPaymentDetails(addedPaymentDetails!)
            
            if savePayment != nil {
                GiftCardCreationService.sharedInstance.setSavePaymentDetails(savePayment!)
            }
            
            //if there is any existing payment choosen clear it
            GiftCardCreationService.sharedInstance.resetPaymentDetailsId()
            
            //update purchaser information
            if var purhaserDetail = GiftCardCreationService.sharedInstance.purchaserDetails {
                if ((GiftCardCreationService.sharedInstance.purchaserInformationFromPayment) != nil && GiftCardCreationService.sharedInstance.purchaserInformationFromPayment) {
                    purhaserDetail.firstName = addedPaymentDetails!.firstName!
                    purhaserDetail.lastName = addedPaymentDetails!.lastName!
                    purhaserDetail.country = addedPaymentDetails!.country!
                    
                    GiftCardCreationService.sharedInstance.setPurchaserDetails(purhaserDetail)
                }
                
                //update recipient Details
                if var recipientDetail = GiftCardCreationService.sharedInstance.recipientDetails {
                    if ((GiftCardCreationService.sharedInstance.recipientSelf) != nil && GiftCardCreationService.sharedInstance.recipientSelf) {
                        recipientDetail.firstName = addedPaymentDetails!.firstName!
                        recipientDetail.lastName = addedPaymentDetails!.lastName!
                        recipientDetail.emailAddress = purhaserDetail.emailAddress
                        
                        GiftCardCreationService.sharedInstance.setReceipientDetails(recipientDetail)
                    }
                    
                }
                
            }
        }
        
        if (paymentDetailWithId != nil) {
            GiftCardCreationService.sharedInstance.setPaymentDetailsId(paymentDetailWithId!)
            GiftCardCreationService.sharedInstance.setexistingPaymentDetails(creditCardDetail!)
            
            //clear user entered manual entries
            GiftCardCreationService.sharedInstance.resetPaymentDetails()
            
            //update purchaser information
            if var purhaserDetail = GiftCardCreationService.sharedInstance.purchaserDetails {
                if ((GiftCardCreationService.sharedInstance.purchaserInformationFromPayment) != nil && GiftCardCreationService.sharedInstance.purchaserInformationFromPayment) {
                    purhaserDetail.firstName = creditCardDetail!.firstName
                    purhaserDetail.lastName = creditCardDetail!.lastName
                    purhaserDetail.country = creditCardDetail!.country
                    
                    GiftCardCreationService.sharedInstance.setPurchaserDetails(purhaserDetail)
                }
                
                //update recipient Details
                if var recipientDetail = GiftCardCreationService.sharedInstance.recipientDetails {
                    if ((GiftCardCreationService.sharedInstance.recipientSelf) != nil && GiftCardCreationService.sharedInstance.recipientSelf) {
                        recipientDetail.firstName = creditCardDetail!.firstName
                        recipientDetail.lastName = creditCardDetail!.lastName
                        recipientDetail.emailAddress = purhaserDetail.emailAddress
                        
                        GiftCardCreationService.sharedInstance.setReceipientDetails(recipientDetail)
                    }
                    
                }
                
            }
        }
        
        
        //get total summary amount
        let indexPath = NSIndexPath(forRow: addNewFieldName.paymentAccount.hashValue, inSection: 0)
        let amountString = String(addNewCardCellDetails[addNewFieldName.totalOrderSummary.hashValue].fieldRightSideValue.characters.dropFirst())
        let amount = Double(amountString)
        
        //update value in shared instance
        GiftCardCreationService.sharedInstance.setCardAmout(addNewCardCellDetails[addNewFieldName.selectCardAmount.hashValue].fieldRightSideValue)
        if (GiftCardCreationService.sharedInstance.paymentDetails != nil || GiftCardCreationService.sharedInstance.paymentDetailsId != nil) {
            GiftCardCreationService.sharedInstance.updatePaymentDetailsAmount(amount!)
        }
        
        //update value in payment detail cell
        let cell = tableView.cellForRowAtIndexPath(indexPath)
        if let cellType = cell as? AddNewGiftCardImageTableViewCell {
            cellType.value.hidden = false
            cellType.creditCardTypeImage.hidden = false
            
            //get value from notification center
            cellType.value.text = cardLastDigits
            addNewCardCellDetails[addNewFieldName.paymentAccount.hashValue].fieldRightSideValue = cardLastDigits
            addNewCardCellDetails[addNewFieldName.paymentAccount.hashValue].fieldCreditCardTypeImage = creditCardImage
            
            //update/show credit card image
            if let url = NSURL(string: creditCardImage){
                cellType.creditCardTypeImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
                cellType.creditCardTypeImage.layer.masksToBounds = true
                cellType.creditCardTypeImage.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success: { (image) in
                    cellType.creditCardTypeImage.image = image
                    cellType.creditCardTypeImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    cellType.creditCardTypeImage.layer.masksToBounds = false
                    SVProgressHUD.dismiss()
                })
            }
        }
    }
    
    
    // MARK: - Table field initialization
    func initializeObject() {
        //dynamically get minimum value from brand details & convert as a amount
        
        initialAmountINString = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,giftCardBrand!.variableAmountDenominationMinimumValue!);
        
        // Defaulat Brand image code
        if (self.giftCardBrand?.quantities.count > 0) {
            GiftCardCreationService.sharedInstance.setCardQuantity(Int((self.giftCardBrand?.quantities[0])!))
            GiftCardCreationService.sharedInstance.setCardAmout(self.initialAmountINString)
        }
        let inCommBrandImage = self.giftCardBrand?.cardImages[0]
        GiftCardCreationService.sharedInstance.setIncommBrandImageCode(inCommBrandImage?.imageCode)
        
        
        var data:AddNewGiftCardDataModel = AddNewGiftCardDataModel()
        data.fieldName = addNewFieldName.giftCardPreview.rawValue
        //        data.fieldImage = cardSelected(inCommBrandImage?.imageUrl)
        data.fieldRightSideValue = inCommBrandImage!.imageUrl
        addNewCardCellDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = addNewFieldName.selectCardAmount.rawValue
        data.fieldRightSideValue = initialAmountINString
        addNewCardCellDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = addNewFieldName.selectQuantity.rawValue
        data.fieldImage = UIImage(named: "down-button")!
        if let quantity = giftCardBrand?.quantities[0] {
            data.fieldRightSideValue = String(format: "%d",quantity)
        }
        addNewCardCellDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = addNewFieldName.paymentAccount.rawValue
        data.fieldRightSideValue = ""
        addNewCardCellDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = addNewFieldName.purchaserDetails.rawValue
        data.fieldRightSideValue = ""
        addNewCardCellDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = addNewFieldName.recipient.rawValue
        data.fieldRightSideValue = ""
        addNewCardCellDetails.append(data)
        
        //get total summary by brand minimum value * brand minimum quantity
        let recipientAmountString = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,(Double(giftCardBrand!.quantities[0]) * (giftCardBrand?.variableAmountDenominationMinimumValue)!))
        
        data = AddNewGiftCardDataModel()
        data.fieldName = addNewFieldName.totalOrderSummary.rawValue
        data.fieldRightSideValue = recipientAmountString
        addNewCardCellDetails.append(data)
        tableView.reloadData()
    }
    
    // MARK: - Submit order
    // Submit an user order
    func submitOrder(){
        if submitOrderValidation() == false{
            return
        }
        SVProgressHUD.showWithStatus("Purchasing Gift Card...",maskType: .Clear)
        
        if inCommSessionId == nil || inCommOrgId == nil{
            createVestaWebSession{ (vestaWebSession, error) in
                if error != nil{
                    SVProgressHUD.dismiss()
                    self.presentConfirmation("Failure", message:"Unable to proceed please Retry", buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.submitOrder()
                        }else{
                            return
                        }
                    })
                }
            }
        }
        
       else if self.inCommOrderId == nil{
             GiftCardCreationService.sharedInstance.getInCommOrderId{ (inCommOrderId, error) in
                if error != nil{
                    SVProgressHUD.dismiss()
                    self.presentConfirmation("Failure", message:"Unable to proceed please Retry", buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.submitOrder()
                        }
                        else{
                            return
                        }
                    })
                }else{
                    self.inCommOrderId = inCommOrderId
                    self.inCommSubmitOrder?.id = self.inCommOrderId
                    self.startWebSession()
                    self.purchasingGiftCard()
                }
             }
        }
        
        else{
            self.inCommSubmitOrder?.id = self.inCommOrderId
            self.startWebSession()
            self.purchasingGiftCard()
        }
    }
    
     
        
  
    
    // MARK: - Order submission
    func purchasingGiftCard(){
        var inCommOrderStatusDetails: InCommOrderStatusDetails = InCommOrderStatusDetails()
        inCommOrderStatusDetails.inCommOrderId = self.inCommOrderId
        if submitOrderStatus == false{
            if inCommSubmitOrder!.payment != nil{
                inCommSubmitOrder!.payment!.vestaOrgId = inCommOrgId
                inCommSubmitOrder!.payment!.vestaWebSessionId = inCommSessionId
            }
            else{
                inCommSubmitOrder!.paymentWithId!.vestaWebSessionId = inCommSessionId!
                inCommSubmitOrder!.paymentWithId!.vestaOrgId = inCommOrgId!
            }
            GiftCardCreationService.sharedInstance.createOrSendGiftCard(inCommSubmitOrder, callback: { (order, error) in
                if error != nil{
                    SVProgressHUD.dismiss()
                    self.presentConfirmation("Failure", message:error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.submitOrder()
                        }
                        else{
                            // Update order failed status
                            inCommOrderStatusDetails.orderStatus = InCommOrderStatusDetails.OrderStatus.FAILED.rawValue
                            GiftCardCreationService.sharedInstance.updateInCommOrderStatus(inCommOrderStatusDetails)
                            return
                        }
                    })
                }
                else{
                    // Update order completed status
                    inCommOrderStatusDetails.orderStatus = InCommOrderStatusDetails.OrderStatus.COMPLETED.rawValue
                    GiftCardCreationService.sharedInstance.updateInCommOrderStatus(inCommOrderStatusDetails)
                    
                    if let associateCardToUser = GiftCardCreationService.sharedInstance.recipientSelf {
                        if associateCardToUser {
                            //track clyp analytics event for creating the gift card for self use
                            clpAnalyticsService.sharedInstance.clpTrackScreenView("GiftCardCreatedSelf");
                        } else {
                            //track clyp analytics event for creating the gift card for others
                            clpAnalyticsService.sharedInstance.clpTrackScreenView("GiftCardCreatedOthers");
                        }
                    }
                    self.userSubmittedGiftCards = order!.submittedOrderItemGiftCards!
                    self.submitOrderStatus = true
                    self.associateGiftCards()
                }
            })
        }
        else{
            self.associateGiftCards()
        }
    }
    
    
    // MARK: - Associate submitted gift cards
    // Associate gift cards to user account
    func associateGiftCards(){
        if associateGiftCardValidation(){
            SVProgressHUD.showWithStatus("Associating Gift Card...",maskType:.Clear)
            let index = userSubmittedGiftCards.count - 1
            let giftCardToken = userSubmittedGiftCards[index].token
            GiftCardCreationService.sharedInstance.associateGiftCardToUser(giftCardToken, callback: { (inCommUserGiftCard, error) in
                if error != nil{
                    if error!.code == GiftCardAppConstants.cardAlreadyProvisionedError{
                        self.removeAndVaildateUserSubmittedCard(index)
                        return
                    }
                    SVProgressHUD.dismiss()
                    var message:String = "An Unexpected error occured while processing your request. Save gift card to account failed."
                    if self.associatePaymentValidation(){
                        message = "An Unexpected error occured while processing your request. Save gift card to account failed. Save payment failed."
                    }
                    self.presentConfirmation("Failure", message:message, buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.submitOrder()
                        }
                        else{
                            self.presentConfirmation("Confirm", message: "Card not successfully associated with account. Do you wish to cancel?", buttonTitle: "Retry", callback: { (confirmed) in
                                if confirmed{
                                    self.submitOrder()
                                }
                                else{
                                    GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
                                    self.dismissViewControllerAnimated(true, completion: nil)
                                }
                            })
                        }
                    })
                }
                else{
                    self.removeAndVaildateUserSubmittedCard(index)
                }
            })
        }
        else{
            self.associatePaymentAccountToUser()
        }
    }
    
    // MARK: - Remove submitted gift card
    // Remove the associated gift card from user submitted gift cards array
    func removeAndVaildateUserSubmittedCard(index:Int){
        self.userSubmittedGiftCards.removeAtIndex(index)
        if self.userSubmittedGiftCards.count == 0{
            self.associateCardsStatus = true
            self.associatePaymentAccountToUser()
        }
        else{
            self.associateGiftCards()
        }
    }
    
    // MARK: - Associate payment account to user
    // Associate payment account to user
    func associatePaymentAccountToUser(){
        if associatePaymentValidation(){
            SVProgressHUD.showWithStatus("Associating Payment Account...",maskType: .Clear)
            GiftCardCreationService.sharedInstance.associatePaymentDetailsToUser(userSubmittedPaymentAccountDetails) { (inCommUserPaymentAccount, error) in
                SVProgressHUD.dismiss()
                if error != nil{
                    var message:String = "An Unexpected error occured while processing your request. Create gift card sucessful. Save payment failed."
                    if self.associateGiftCardStatusValidation(){
                        message = "An Unexpected error occured while processing your request. Create gift card sucessful. Save gift card to account successful. Save payment failed."
                    }
                    self.presentConfirmation("Failure", message:message, buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.submitOrder()
                        }
                        else{
                            self.presentConfirmation("Confirm", message: "Payment not successfully associated with account. Do you wish to cancel?", buttonTitle: "Retry", callback: { (confirmed) in
                                if confirmed{
                                    self.submitOrder()
                                }
                                else{
                                    GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
                                    self.dismissViewControllerAnimated(true, completion:{
                                        self.delegate?.preStatusValidation()
                                    })
                                }
                            })
                            
                        }
                    })
                }
                else{
                    //self.orderSuccess()
                    self.submitPromoOrder()
                }
            }
        }
        else{
            
            self.submitPromoOrder()
            
            //orderSuccess()
        }
    }
    
    // MARK: - prepare order
    // Prepare incomm order
    func prepareOrder() -> InCommSubmitOrder{
        let amount = String(GiftCardCreationService.sharedInstance.cardAmount.characters.dropFirst())
        let quantity = GiftCardCreationService.sharedInstance.cardQuantity
        let incommBrandImageCode = GiftCardCreationService.sharedInstance.incommBrandImageCode
        let recipientMessage = GiftCardCreationService.sharedInstance.recipientMessage
        let recipientDetails = GiftCardCreationService.sharedInstance.recipientDetails
        let paymentDetails = GiftCardCreationService.sharedInstance.paymentDetails
        let paymentDetailsId = GiftCardCreationService.sharedInstance.paymentDetailsId
        let purchaserDetails = GiftCardCreationService.sharedInstance.purchaserDetails
        
        var item = InCommOrderItem(brandId: InCommGiftCardBrandDetails.sharedInstance.brandID,amount: Double(amount)!,quantity:quantity)
        item.imageCode = incommBrandImageCode
        item.messageText = recipientMessage
        var items:[InCommOrderItem]! = []
        items.append(item)
        
        var recipient = recipientDetails
        recipient.items = items
        var recipients:[InCommOrderRecipientDetails]! = []
        recipients.append(recipient)
        
        
        return InCommSubmitOrder(purchaser: purchaserDetails, recipients: recipients, payment:paymentDetails, paymentWithId: paymentDetailsId)
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
    
    
    //MARK: - Submit order validation
    func submitOrderValidation() -> Bool{
        if (GiftCardCreationService.sharedInstance.incommBrandImageCode == nil) {
            self.presentOkAlert("Alert", message: "Please choose design template")
            return false
        } else if (GiftCardCreationService.sharedInstance.paymentDetails == nil && GiftCardCreationService.sharedInstance.paymentDetailsId == nil) {
            self.presentOkAlert("Alert", message: "Please select payment details")
            return false
        } else if (GiftCardCreationService.sharedInstance.purchaserDetails == nil) {
            self.presentOkAlert("Alert", message: "Please enter purchaser details")
            return false
        } else if (GiftCardCreationService.sharedInstance.recipientMessage == nil) {
            self.presentOkAlert("Alert", message: "Please enter recipient details")
            return false
        }
        inCommSubmitOrder = prepareOrder()
        return true
    }
    
    // MARK: - Associate gift card validation
    func associateGiftCardValidation()->Bool{
        let associateCardToUser = GiftCardCreationService.sharedInstance.recipientSelf
        if associateCardToUser != nil && associateCardToUser == true && associateCardsStatus == false && self.userSubmittedGiftCards.count != 0{
            return true
        }
        return false
    }
    
    // MARK: - Associate gift card status
    func associateGiftCardStatusValidation()->Bool{
        let associateCardToUser = GiftCardCreationService.sharedInstance.recipientSelf
        if associateCardToUser != nil && associateCardToUser == true && associateCardsStatus == true && self.userSubmittedGiftCards.count == 0{
            return true
        }
        return false
    }
    
    // MARK: - Associate payment validation
    func associatePaymentValidation() -> Bool{
        let savePaymentDetails = GiftCardCreationService.sharedInstance.savePaymentDetails
        if savePaymentDetails != nil && associatePaymentStatus == false{
            userSubmittedPaymentAccountDetails = savePaymentDetails
            return true
        }
        return false
    }
    
    // MARK: - Order success
    func orderSuccess(){
        //prepare success message
        let alertMessage = "Gift Card successfully sent to " + GiftCardCreationService.sharedInstance.recipientDetails.emailAddress!
        
        GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
        self.userSubmittedPaymentAccountDetails = nil
        self.associatePaymentStatus = true
        let action = UIAlertAction(title: "Ok", style: .Cancel) { action in
            //            self.dismissViewControllerAnimated(true, completion:nil)
            self.dismissViewControllerAnimated(true, completion: {
                self.delegate?.preStatusValidation()
            })
        }
        self.presentAlert("Message", message: alertMessage, actions: action)
    }
    
    // MARK: - De alloc notification
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    // MARK: - Create and start web session
    func createVestaWebSession(callback:InCommVestaWebSessionCallback){
        GiftCardCreationService.sharedInstance.createVestWebSession { (vestaWebSession, error) in
            if error != nil{
                self.presentError(error)
                return callback(vestaWebSession: nil, error: error)
            }
            else{
                self.inCommOrgId = vestaWebSession?.vestaOrgId
                self.inCommSessionId = vestaWebSession?.vestaWebSessionId
                return callback(vestaWebSession: vestaWebSession, error: nil)
            }
        }
    }
    
    func startWebSession(){
        self.paySafeWebView.delegate = self
        self.paySafeWebView.start()
    }
    
    // MARK: - Pay safe webview delegate
    func getDetails(webview: PaySafeWebView) -> (orgId: String, sessionId: String){
        return (orgId: inCommOrgId!, sessionId: inCommSessionId!)
    }
    
    // MARK: - Submit promo order
    func submitPromoOrder(){
        if (GiftCardUtil.validatePromoOffer()) {
            let recipient:InCommOrderRecipientDetails! = inCommSubmitOrder!.recipients.first
            let orderItem:InCommOrderItem! = recipient.items.first
            let totalOrderAmount:Double = orderItem.amount * Double(orderItem.quantity)
            
            // Purchaser
            var purchaser:InCommOrderPurchaser = InCommOrderPurchaser.init(firstName: inCommSubmitOrder!.purchaser.firstName, lastName: inCommSubmitOrder!.purchaser.lastName, emailAddress: inCommSubmitOrder!.purchaser.emailAddress,country:inCommSubmitOrder!.purchaser.country)
            purchaser.suppressReceiptEmail = true
            
            // Recipient
            let recipientDetails:InCommPromoRecipientDetails = InCommPromoRecipientDetails.init(firstName: purchaser.firstName, lastName: purchaser.lastName, emailAddress: purchaser.emailAddress, country: purchaser.country)
            
            // Recipients
            var recipients : [InCommPromoRecipientDetails] = []
            recipients.append(recipientDetails)
            
            // Payment
            let payment:InCommPromoOrderPayment = InCommPromoOrderPayment.init(orderPaymentMethod: GiftCardAppConstants.noFundsColloectedOrderPaymentMethodType)
            
            // Incomm input
            let inCommInput = SubmitInCommAuthToken.init(spendGoKey: Configuration.sharedConfiguration.SpendGoAPIKey,spendGoAuthorizationToken: (UserService.sharedUser?.spendgoAuthToken)!, spendGoId: (UserService.sharedUser?.id)!)
            
            let messageTo = purchaser.firstName + " " + purchaser.lastName
            
            // Query
            let query:InCommQuery = InCommQuery.init(amount: totalOrderAmount, messageTo: messageTo, inCommInput: inCommInput, inCommToken: GiftCardCreationService.sharedInstance.inCommAuthToken!)
            
            // IncommResult
            let inCommResult = InCommResult.init(payment: payment, purchaser: purchaser, recipients:recipients)
            
            // InCommPromoOrder
            let inCommPromoOrder:InCommPromoOrder = InCommPromoOrder.init(query: query, result: inCommResult)
            
            GiftCardCreationService.sharedInstance.submitPromoOrder(inCommPromoOrder)
        }
        SVProgressHUD.dismiss()
        self.inCommSubmitOrder = nil
        self.submitPromoOrderStatus = true
        self.orderSuccess()
    }
}