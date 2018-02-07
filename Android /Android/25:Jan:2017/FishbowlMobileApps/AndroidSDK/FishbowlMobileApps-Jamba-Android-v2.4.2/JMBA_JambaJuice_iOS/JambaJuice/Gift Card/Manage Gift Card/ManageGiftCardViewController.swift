//
//  ManageGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 09/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK

import SVProgressHUD


class ManageGiftCardViewController: UIViewController,UITableViewDelegate,UITableViewDataSource, GiftCardWebviewDelegate, AutoReloadSettingsViewControllerDelegate {
    
    //field name in table view
    enum fieldName:String {
        case cardPreview        = "Gift Card Preview"
        case autoReload         = "Auto Reload"
        case viewDetail         = "Card Details"
        case transactionHistory = "Transaction History"
        case giftCardSupport    = "Gift Card Support"
    }
    
    enum SegueIdentifiers:String {
        case cardDetailVC               = "cardDetailVC"
        case showAutoReloadVC           = "showAutoReloadVC"
        case showTransactionHistoryVC   = "TransactionHistory"
        case giftCardSupportSI          = "GiftCardSupportIdentifier"
        case reloadGiftCardScreen       = "ReloadGiftCardScreen"
    }
    
    @IBOutlet var tableView:UITableView!
    
    //field name in array
    var name_label_text: [String]    = [fieldName.cardPreview.rawValue,fieldName.autoReload.rawValue,fieldName.viewDetail.rawValue, fieldName.giftCardSupport.rawValue];
    //field value in array
    var details_label_text: [String] = ["1","","",""];
    
    var selectedCardImage       : InCommBrandCardImage?
    var inCommUserGiftCardId    : Int32!
    var userCardInfo:InCommUserGiftCard{
        get{
            return GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserGiftCardId)!
        }
    }
    var inCommAutoReloadSavable : InCommAutoReloadSavable?
    var inCommUserPaymentAccount: InCommUserPaymentAccount?
    var continueDeleteWithoutAlert = false
    
    override func viewDidLoad() {
        getAutoReloadDetails()
        super.viewDidLoad()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier! {
        case SegueIdentifiers.cardDetailVC.rawValue:
            let destinationController = segue.destination as! CardDetailsViewController
            destinationController.inCommUserCardId = userCardInfo.cardId
            destinationController.screenIsPresented = false
            break
        case SegueIdentifiers.showAutoReloadVC.rawValue:
            let destinationController = segue.destination as! AutoReloadSettingsViewController
            destinationController.inCommUserGiftCardId = userCardInfo.cardId
            destinationController.inCommAutoReloadSavable = inCommAutoReloadSavable
            destinationController.inCommUserPaymentAccount = inCommUserPaymentAccount
            destinationController.delegate = self
            
            break
        case SegueIdentifiers.showTransactionHistoryVC.rawValue:
            (segue.destination as! TransactionHistoryViewController).giftCardId = userCardInfo.cardId
            break
        case SegueIdentifiers.giftCardSupportSI.rawValue:
            if let vc = segue.destination as? GiftCardWebview {
                vc.delegate = self
            }
            break
        case  SegueIdentifiers.reloadGiftCardScreen.rawValue:
            let destinationController = segue.destination as! UINavigationController
            let targetController = destinationController.topViewController as! ReloadGiftCardViewController
            targetController.inCommUserGiftCardId = userCardInfo.cardId
            break
        default:
            break
        }
        
    }
    
    //MARK: - IBAction methods
    @IBAction func close(){
        self.dismiss(animated: true, completion: nil)
    }
    
    //    @IBAction func deleteGiftCard() {
    //        if continueDeleteWithoutAlert {
    //            deleteCard()
    //        } else {
    //            self.presentConfirmation("Gift Card Removal", message: "Are you sure you want to remove this card from your account?", buttonTitle: "OK") {(confirmed) -> Void in
    //                if confirmed{
    //                    self.deleteCard()
    //                }
    //            }
    //        }
    //    }
    
    //    func deleteCard() {
    //        if userCardInfo.autoReloadId != nil {
    //            deleteAutoReloadRules()
    //        } else {
    //            deleteCardAPICall()
    //        }
    //    }
    
    //    func deleteAutoReloadRules()  {
    //        SVProgressHUD.show(withStatus: "Removing auto reload rules...")
    //        SVProgressHUD.setDefaultMaskType(.clear)
    //        GiftCardCreationService.sharedInstance.deleteAutoReloadDetails(userCardInfo.cardId, autoReloadId: userCardInfo.autoReloadId) { (error) in
    //            if error != nil{
    //                SVProgressHUD.dismiss()
    //                self.retryConfirmationMessage(error!)
    //            }
    //            else{
    //                //remove the auto reload details from the gift card manually
    //                var cardDetails = self.userCardInfo
    //                cardDetails.autoReloadId = nil
    //                self.updateUserGiftCardDetails(cardDetails)
    //                self.continueDeleteWithoutAlert = true
    //                self.deleteCardAPICall()
    //            }
    //        }
    //    }
    
    //    func deleteCardAPICall() {
    //        SVProgressHUD.show(withStatus: "Removing gift card...")
    //        SVProgressHUD.setDefaultMaskType(.clear)
    //        InCommUserGiftCardService.deleteGiftCard(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: userCardInfo.cardId) { (error) in
    //            if (error != nil) {
    //                if (error?.code == GiftCardAppConstants.errorCodeInvalidUser) {
    //                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
    //                        if inCommUserStatus{
    //                            self.deleteCardAPICall()
    //                        }
    //                        else{
    //                            SVProgressHUD.dismiss()
    //                            self.presentError(error)
    //                            return
    //                        }
    //                    })
    //                } else {
    //                    SVProgressHUD.dismiss()
    //                    //update Auto reload status in ui incase of failue
    //                    self.updateAutoReloadDetails(nil, inCommUserPaymentAccount: nil)
    //                    self.retryConfirmationMessage(error!)
    //                    return
    //                }
    //            } else {
    //                SVProgressHUD.dismiss()
    //                
    //                let message = "Gift card \"" + (self.userCardInfo.cardName.lowercased()) + "\" removed successfully"
    //                GiftCardCreationService.sharedInstance.deleteGiftCardFromUserGiftCards(self.userCardInfo.cardId)
    //                self.presentOkAlert("Removed", message: message, callback: {
    //                    self.dismiss(animated: true, completion: {
    //                        self.delegate?.giftCardRemoved()
    //                    })
    //                })
    //            }
    //        }
    //    }
    //    
    //    func retryConfirmationMessage(_ error:NSError) {
    //        self.presentConfirmation("Error", message: error.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
    //            if confirmed{
    //                self.deleteCard()
    //            }
    //            else{
    //                return
    //            }
    //        })
    //    }
    
    // MARK: - Table view delegates
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return name_label_text.count;
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if (name_label_text[indexPath.row] == fieldName.cardPreview.rawValue) {
            var width = tableView.frame.width - GiftCardAppConstants.tableViewGiftCardLeadingAndTrailingSpace  //table view width - (leading & trailing space)
            
            //checking for ipad
            if !GiftCardAppConstants.isiPhone {
                width = tableView.frame.width / GiftCardAppConstants.GiftCardRatioWidth
            }
            return (width * CGFloat(GiftCardAppConstants.GiftCardRatioHeight)) + GiftCardAppConstants.tableViewGiftCardTopAndsBottomSpace
        } else {
            return GiftCardAppConstants.tableViewCellHeight
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (name_label_text[indexPath.row] == fieldName.cardPreview.rawValue) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ManageGiftCardPreviewCell", for: indexPath) as! ManageGiftCardPreviewCell
            
            details_label_text[indexPath.row] = self.userCardInfo.imageUrl
            cell.setImage(userCardInfo)
            
            return cell
        }
        let cell = tableView.dequeueReusableCell(withIdentifier: "ManageGiftCardTableViewCell", for: indexPath) as! ManageGiftCardTableViewCell
        
        cell.setCellData(name_label_text[indexPath.row], field_value: details_label_text[indexPath.row], detailsHorizontalSpace: 10)
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // Precaution for user gift card to avoid crash
        if GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserGiftCardId) == nil && name_label_text[indexPath.row] != fieldName.giftCardSupport.rawValue {
            return
        }
        switch name_label_text[indexPath.row] {
        case fieldName.autoReload.rawValue:
            performSegue(withIdentifier: SegueIdentifiers.showAutoReloadVC.rawValue, sender: self)
            break
        case fieldName.viewDetail.rawValue:
            performSegue(withIdentifier: SegueIdentifiers.cardDetailVC.rawValue, sender: self)
            break
        case fieldName.transactionHistory.rawValue:
            performSegue(withIdentifier: SegueIdentifiers.showTransactionHistoryVC.rawValue, sender: self)
            break
        case fieldName.giftCardSupport.rawValue:
            performSegue(withIdentifier: SegueIdentifiers.giftCardSupportSI.rawValue, sender: self)
            break
        default:
            break
        }
    }
    
    //MARK: - InCommuser card info update delegate
    func updateUserGiftCardDetails(_ inCommUserGiftCard: InCommUserGiftCard) {
        var inCommGiftCard = userCardInfo
        inCommGiftCard = inCommUserGiftCard
        GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommGiftCard)
    }
    
    //MARK: Auto reload delegate - Get user auto reload config details
    func getAutoReloadDetails(){
        if userCardInfo.autoReloadId == nil{
            return
        }
        
        SVProgressHUD.show(withStatus: "Loading...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.getAutoReloadDetails(userCardInfo.cardId, autoReloadId: userCardInfo.autoReloadId, callback: { (inCommAutoReloadSavable, error) in
            if error != nil{
                SVProgressHUD.dismiss()
                if error!.code == GiftCardAppConstants.unableToGetAutoReloadInformation{
                    self.inCommAutoReloadSavable = nil
                    self.inCommUserPaymentAccount = nil
                    return
                } else {
                    self.presentConfirmation("Failure", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.getAutoReloadDetails()
                        }
                        else{
                            self.close()
                        }
                    })
                }
            }
            else{
                self.inCommAutoReloadSavable = inCommAutoReloadSavable
                self.getPaymentDetails()
            }
        })
    }
    
    //MARK: - Get payment details
    func getPaymentDetails(){
        GiftCardCreationService.sharedInstance.getUserPaymentAccountDetails(inCommAutoReloadSavable!.giftCardId, paymentAccountId: inCommAutoReloadSavable!.paymentAccountId) { (inCommUserPaymentAccount, error) in
            SVProgressHUD.dismiss()
            if error != nil{
                if error!.code == GiftCardAppConstants.unableToFindUserPaymentAccount{
                    self.inCommUserPaymentAccount = nil
                }
                else{
                    self.presentConfirmation("Failure", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.getPaymentDetails()
                        }
                        else{
                            self.close()
                        }
                    })
                }
            }
            else{
                self.inCommUserPaymentAccount = inCommUserPaymentAccount
                self.updateAutoReloadDetails(self.inCommAutoReloadSavable, inCommUserPaymentAccount: self.inCommUserPaymentAccount)
            }
        }
    }
    
    // MARK: - Update auto reload details
    func updateAutoReloadDetails(_ inCommAutoReloadSavable: InCommAutoReloadSavable?, inCommUserPaymentAccount: InCommUserPaymentAccount?) {
        self.inCommAutoReloadSavable = inCommAutoReloadSavable
        self.inCommUserPaymentAccount = inCommUserPaymentAccount
        autoReloadErrorMessage()
    }
    
    // MARK: - Gift card webview delegate
    func webviewURLPath(_ webview: GiftCardWebview) -> String {
        return GiftCardAppConstants.jambaGiftCardFAQUrl
    }
    
    func webviewHeaderText(_ webview: GiftCardWebview) -> String {
        return fieldName.giftCardSupport.rawValue
    }
    
    // MARK: - Auto reload error message atributted text
    func autoReloadErrorMessage(){
        let indexPath = IndexPath(row: fieldName.autoReload.hashValue, section: 0)
        let cell = tableView.cellForRow(at: indexPath)
        if let autoReloadCell = cell as? ManageGiftCardTableViewCell{
            
            if self.inCommAutoReloadSavable == nil{
                self.details_label_text[fieldName.autoReload.hashValue] = ""
            }
            else{
                if !(inCommAutoReloadSavable?.isActive)! {
                    self.details_label_text[fieldName.autoReload.hashValue] = "Off"
                } else {
                    self.details_label_text[fieldName.autoReload.hashValue] = "On"
                }
            }
            
            autoReloadCell.setCellData(name_label_text[indexPath.row], field_value: details_label_text[indexPath.row], detailsHorizontalSpace: 10)
            
            if inCommAutoReloadSavable?.lastErrorMessage == nil || (inCommAutoReloadSavable?.lastErrorMessage.isEmpty)! || (inCommAutoReloadSavable?.lastErrorMessage)! == ""{
                return
            }
            
            let autoReloadFieldNameFont:UIFont = UIFont.init(name: "Helvetica Neue", size: 16.0)!
            let autoReloadFieldNameColor:UIColor = UIColor(red: 156.0/255.0, green: 155.0/255.0, blue: 155.0/255.0, alpha: 1.0)
            let autoReloadFieldNameAttribute = [NSAttributedStringKey.font: autoReloadFieldNameFont, NSAttributedStringKey.foregroundColor: autoReloadFieldNameColor]
            let autoReloadFieldString = fieldName.autoReload.rawValue + "\n"
            let autoReloadFieldNameAttributeString = NSMutableAttributedString(string:autoReloadFieldString, attributes: autoReloadFieldNameAttribute)
            
            
            let autoReloadErrorFont:UIFont = UIFont.init(name: "Helvetica Neue", size: 12.0)!
            let  autoReloadErrorColor:UIColor = UIColor(red: 196.0/255.0, green: 19.0/255.0, blue: 72.0/255.0, alpha: 1.0)
            let autoReloadErrorAttribute =  [NSAttributedStringKey.font:autoReloadErrorFont, NSAttributedStringKey.foregroundColor: autoReloadErrorColor]
            let autoReloadErrorString = (inCommAutoReloadSavable?.lastErrorMessage)!
            
            
            let autoReloadErrorAttributeString = NSAttributedString(string:autoReloadErrorString, attributes:autoReloadErrorAttribute)
            
            autoReloadFieldNameAttributeString.append(autoReloadErrorAttributeString)
            autoReloadCell.name.attributedText = autoReloadFieldNameAttributeString
        }
    }
    
}
