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

//MARK: PaymentViewController Delegate
protocol PaymentViewControllerDelegate: class {
    func refreshUserAssociatedGiftCard()
}

//MARK: PaymentViewController Delegate
protocol PaymentViewControllerPassObjectDelegate: class {
    func paymentAdded(_ addedPaymentDetails:InCommSubmitPayment?, savePayment:InCommUserPaymentAccountDetails?, paymentDetailWithId: InCommSubmitPaymentWithId?, creditCardDetail: InCommUserPaymentAccount?, creditCardImage:String, cardLastDigits:String)
}

typealias InCommAutoReloadDeleteCallBack = (_ error: NSError?) -> Void

class PaymentViewController: UIViewController,UITableViewDelegate, UITableViewDataSource {
    
    //field name
    enum paymentDetailFieldName : String {
        case cardType       = "Card Type"
        case cardNumber     = "Card Number"
        case expirationDate = "Expiration Date"
        case ccv            = "Security Code (CVV)"
        case fullName       = "Full Name"
        case street1        = "Address 1"
        case street2        = "Address 2"
        case city           = "City"
        case state          = "State"
        case zipCode        = "Zip Code"
        case country        = "Country"
        case saveButton     = "Save"
    }
    
    //field type
    enum paymentTypeFieldType : String {
        case switchOnly     = "Switch Only"
        case imageOnly      = "Image Only"
        case singleLabel    = "Single Label"
    }
    
    
    //field name array
    var name_label_text: [String] = [paymentDetailFieldName.cardType.rawValue,paymentDetailFieldName.cardNumber.rawValue,paymentDetailFieldName.expirationDate.rawValue,paymentDetailFieldName.ccv.rawValue,paymentDetailFieldName.fullName.rawValue,paymentDetailFieldName.street1.rawValue,paymentDetailFieldName.street2.rawValue,paymentDetailFieldName.city.rawValue,paymentDetailFieldName.state.rawValue,paymentDetailFieldName.zipCode.rawValue,paymentDetailFieldName.country.rawValue];
    var details_label_text  : [String] = [];                        //field value array
    var monthInText         : [String] = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"] //month array
    var cardTypeImageURL    : String = ""                           //card type (visa/American express) image url
    var creditCardDetail    : InCommUserPaymentAccount?             //credit card detail
    var cardAmount          : Double = 0                            //card amount for creation flow
    weak var delegate       : PaymentViewControllerDelegate?
    weak var paymentViewControllerPassObjectDelegate    : PaymentViewControllerPassObjectDelegate?
    var screenMode          = GiftCardAppConstants.PaymentScreenMode.ViewWithTrashOperation.rawValue
    @IBOutlet weak var tableView            : UITableView!
    @IBOutlet weak var submitButton         : UIButton!
    @IBOutlet weak var trashButton          : UIButton!
    @IBOutlet weak var tableViewBottomSpace : NSLayoutConstraint!
    
    override func viewDidLoad() {
        prepareTableViewData()
        if screenMode == GiftCardAppConstants.PaymentScreenMode.ViewOnly.rawValue {
            trashButton.isHidden = true
            submitButton.isHidden = true
            tableViewBottomSpace.constant = -GiftCardAppConstants.buttonHeight
        } else if screenMode == GiftCardAppConstants.PaymentScreenMode.ViewWithTrashOperation.rawValue {
            trashButton.isHidden = false
            submitButton.isHidden = false
            tableViewBottomSpace.constant = 0
        } else  {
            trashButton.isHidden = true
            submitButton.isHidden = false
            tableViewBottomSpace.constant = 0
        }
        super.viewDidLoad()
    }
    
    // MARK: Table view Delegates
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if creditCardDetail != nil && details_label_text.count > 0{
            return name_label_text.count;
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (indexPath.row == paymentDetailFieldName.cardType.hashValue) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "PaymentImageTableViewCell", for: indexPath) as! PaymentImageTableViewCell
            
            cell.setCellData(name_label_text[indexPath.row], imageURL: cardTypeImageURL)
            
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "PaymentLabelTableViewCell", for: indexPath) as! PaymentLabelTableViewCell
            cell.labelName.text = name_label_text[indexPath.row]
            cell.details.text = details_label_text[indexPath.row]
            
            if (indexPath.row == paymentDetailFieldName.cardNumber.hashValue || indexPath.row == paymentDetailFieldName.ccv.hashValue) {
                cell.details.numberOfLines = 1
                cell.details.textColor = UIColor(red: 155/255, green: 155/255, blue:155/255, alpha:1.0)
                cell.details.lineBreakMode = .byTruncatingHead
            }
            if (name_label_text[indexPath.row] == paymentDetailFieldName.street1.rawValue || name_label_text[indexPath.row] == paymentDetailFieldName.street2.rawValue) {
                cell.labelHeightConstraint.constant = GiftCardAppConstants.labelDoubleLineHeight
            } else {
                cell.labelHeightConstraint.constant = GiftCardAppConstants.labelSingleLineHeight
            }
            if (name_label_text[indexPath.row] == paymentDetailFieldName.country.rawValue) {
                cell.details.text = details_label_text[indexPath.row]
            } else if (name_label_text[indexPath.row] == paymentDetailFieldName.state.rawValue) {
                cell.details.text = details_label_text[indexPath.row]
            }
            return cell
        }
    }
    
    //MARK: - IBAction methods
    @IBAction func useThisPayment() {
        //save values in shared instance
        let paymentdetails = InCommSubmitPaymentWithId(amount: cardAmount, paymentAccountId: creditCardDetail!.id,vestaOrgId: "", vestaWebSessionId: "")
        
        //card details like number & card type image url send to add new gift card screen
        let creditCardNumber = creditCardDetail!.creditCardNumber
        
        paymentViewControllerPassObjectDelegate?.paymentAdded(nil, savePayment:nil, paymentDetailWithId: paymentdetails, creditCardDetail: creditCardDetail!, creditCardImage:cardTypeImageURL, cardLastDigits:creditCardNumber)
        
        //close the screen
        if (self.isBeingPresented) {
            self.dismiss(animated: true, completion: nil)
        } else {
            self.popToRootViewController()
        }
    }
    
    @IBAction func close(){
        popViewController()
    }
    
    //MARK: - Delete payment info
    @IBAction func deletePaymentInfo() {
        self.presentConfirmation("Delete", message: "Are you sure you want to delete the credit card?", buttonTitle: "OK") { (confirmed) in
            if confirmed{
                self.getAutoReloadConfigGiftCardsForPaymentAccountId()
            }
        }
    }
    
    //MARK: - Get auto reload config cards for payment account
    func getAutoReloadConfigGiftCardsForPaymentAccountId(){
        SVProgressHUD.show(withStatus: "Processing...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.getAutoReloadGiftCardsForPaymentAccount(creditCardDetail!.id){ (giftCardsDetailsWithAutoReloadDetails, error) in
            if error != nil{
                SVProgressHUD.dismiss()
                self.presentConfirmation("Failure", message: error!.localizedDescription, buttonTitle: "Retry"){ (confirmed) in
                    if confirmed{
                        self.getAutoReloadConfigGiftCardsForPaymentAccountId()
                    }
                    else{
                        self.confirmExit(error)
                    }
                }
            }
            else if giftCardsDetailsWithAutoReloadDetails.count == 0{
                self.deleteCreditCard()
            }
            else{
                SVProgressHUD.dismiss()
                let giftCardCount = giftCardsDetailsWithAutoReloadDetails.count
                var giftCardMessage = "\(giftCardCount) gift cards"
                if giftCardCount == 1{
                    giftCardMessage = "\(giftCardCount) gift card"
                }
                
                self.presentConfirmation("Confirm", message: "Your payment account associated with auto reoload config of \(giftCardMessage). \n Please confirm you want to delete the credit card?", buttonTitle: "OK", callback: { (confirmed) in
                    if confirmed{
                        self.deleteAutoReload(giftCardsDetailsWithAutoReloadDetails)
                    }
                    else{
                        return
                    }
                })
            }
        }
    }
    
    //MARK: - Delete auto reload config details
    func deleteAutoReload(_ deleteAutoReloadDetails:[CardAutoReloadDetails]){
        var autoReloadDetails:[CardAutoReloadDetails] = deleteAutoReloadDetails
        let index = autoReloadDetails.count - 1
        let deleteDetail = autoReloadDetails[index]
        self.deleteAutoReload(deleteDetail) { (error) in
            if error != nil{
                SVProgressHUD.dismiss()
                self.presentConfirmation("Failure", message: error!.localizedDescription, buttonTitle: "Retry"){ (confirmed) in
                    if confirmed{
                        self.deleteAutoReload(autoReloadDetails)
                    }
                    else{
                        self.confirmExit(error)
                    }
                }
            }
            else{
                autoReloadDetails.remove(at: index)
                if autoReloadDetails.count == 0{
                    self.deleteCreditCard()
                }
                else{
                    self.deleteAutoReload(autoReloadDetails)
                }
            }
        }
    }
    
    //MARK: - Delete auto reload config detail
    func deleteAutoReload(_ deleteAutoReloadDetails:CardAutoReloadDetails, callback:@escaping InCommAutoReloadDeleteCallBack){
        let cardId = deleteAutoReloadDetails.userGiftCard!.cardId
        let autoRelaodId = deleteAutoReloadDetails.userGiftCard!.autoReloadId
        let autoReloadCardName = deleteAutoReloadDetails.userGiftCard!.cardName
        SVProgressHUD.show(withStatus: "Removing auto reload config from \(autoReloadCardName)")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.deleteAutoReloadDetails(cardId, autoReloadId: autoRelaodId) { (error) in
            if error != nil{
                return callback(error)
            }
            else{
                return callback(nil)
            }
        }
    }
    
    //MARK: - Delete credit card
    func deleteCreditCard(){
        SVProgressHUD.show(withStatus: "Deleting Credit Card...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.deletePaymentAccount(creditCardDetail!.id) { (error) in
            SVProgressHUD.dismiss()
            if error != nil{
                self.presentConfirmation("Failure", message: error!.localizedDescription, buttonTitle: "Retry"){ (confirmed) in
                    if confirmed{
                        self.deleteCreditCard()
                    }
                    else{
                        self.confirmExit(error)
                    }
                }
            }
            else{
                self.presentOkAlert("Success", message: "Credit card deleted successfully", callback: {
                    self.delegate?.refreshUserAssociatedGiftCard()
                    self.close()
                })
            }
        }
    }
    
    //MARK: - ConfirmExit
    func confirmExit(_ error:NSError!){
        SVProgressHUD.dismiss()
        let confirmMessage = error.localizedDescription + "\n Please confirm to exit?"
        self.presentConfirmation("Confirm", message: confirmMessage, buttonTitle: "OK") { (confirmed) in
            if confirmed{
                self.delegate?.refreshUserAssociatedGiftCard()
                self.close()
            }
            else{
                self.getAutoReloadConfigGiftCardsForPaymentAccountId()
            }
        }
    }
    
    //MARK: - Prepare data for table view
    func prepareTableViewData() {
        details_label_text = [cardTypeImageURL,
                              "xxxx xxxx xxxx " + (creditCardDetail?.creditCardNumber)!,
                              "\(getMonthInText(creditCardDetail!.creditCardExpirationMonth)) \(creditCardDetail!.creditCardExpirationYear)",
                              "xxx",
                              "\(creditCardDetail!.firstName) \(creditCardDetail!.lastName)",(creditCardDetail?.streetAddress1)!,
                              (creditCardDetail?.streetAddress2) == nil ? "" : (creditCardDetail?.streetAddress2)!,
                              (creditCardDetail?.city)!,(creditCardDetail?.stateProvince)!,
                              (creditCardDetail?.zipPostalCode)!,(creditCardDetail?.country) == nil ? "" : (creditCardDetail?.country)!]
    }
    //MARK: - Get the month name by passing month number
    func getMonthInText(_ cardMonthyValue:UInt16) -> String {
        let index:Int = Int(cardMonthyValue) - 1
        return monthInText[index]
    }
}
