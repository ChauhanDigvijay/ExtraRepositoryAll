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

class PaymentListViewController: UIViewController,UITableViewDelegate,UITableViewDataSource, PaymentViewControllerDelegate {
    
    @IBOutlet weak var tableView            : UITableView!
    @IBOutlet weak var noRecordFoundView    : UIView!
    @IBOutlet weak var addNewPaymentButton  : UIButton!
    let refreshControl                      = UIRefreshControl()
    var userPaymentAccountDetails           : [InCommUserPaymentAccount] = [];
    var creditCardImages                    : [String] = []
    var selectedIndex                       : Int = -1
    var brand                               : InCommBrand?
    var headerTitle                         : String = GiftCardAppConstants.addNewPaymentTitle  //add payment screen Title
    var cardAmount                          : Double = 0.00                                     //card amount for gift card creation flow
    var enableSavePayment                   : Bool = true                                       //save payment has to enable in add payment screen
    var saveBillingPayment                  : Bool = true                                       //gift card creation flow
    var usePaymentList                      : Bool = false                                      //payment list
    var selectedCreditCardId                : Int32 = 0                                         //paymentt list
    var screenMode                          = GiftCardAppConstants.PaymentScreenMode.ViewWithTrashOperation.rawValue
    var userNewlyAddedCreditCard            : InCommSubmitPayment?
    var userSelectedCard                    : InCommSubmitPaymentWithId?
    var userSavedPaymentDetails             : InCommUserPaymentAccountDetails?
    var paymentDetailDelegate               : PaymentViewControllerPassObjectDelegate?
    var addPaymentDetailDelegate            : AddPaymentViewControllerDelegate?
    
    override func viewDidLoad() {
        noRecordFoundView.isHidden = true
        brand = InCommGiftCardBrandDetails.sharedInstance.brand
        
        //after launching the screen call the get gift card API
        let triggerTime = (Int64(NSEC_PER_SEC) * 0)
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double(triggerTime) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.getUserPaymentDetails()
        })
        
        //update "add new credit card button name"
        updateButtonName()
        
        if let paymentDetailId = userSelectedCard {
            selectedCreditCardId = paymentDetailId.paymentAccountId
        }
        
        refreshControl.addTarget(self, action: #selector(PaymentListViewController.handleRefresh(_:)), for: .valueChanged)
        
        refreshControl.endRefreshing()
        
        self.tableView.addSubview(refreshControl)
        super.viewDidLoad()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showAddPaymentVC" {
            let targetController =  segue.destination as! AddPaymentViewController
            targetController.headerTitle = headerTitle
            targetController.enableSavePayment = enableSavePayment
            targetController.cardAmount = cardAmount
            targetController.saveBillingPayment = saveBillingPayment
            targetController.userNewlyAddedCreditCard = userNewlyAddedCreditCard
            targetController.userSavedPaymentDetails = userSavedPaymentDetails
            targetController.delegate = addPaymentDetailDelegate
        } else if segue.identifier == "showPaymentDetailsVC" {
            let targetController =  segue.destination as! PaymentViewController
            targetController.cardTypeImageURL = creditCardImages[selectedIndex]
            targetController.creditCardDetail = userPaymentAccountDetails[selectedIndex]
            targetController.cardAmount = cardAmount
            targetController.screenMode = screenMode
            targetController.delegate = self
            targetController.paymentViewControllerPassObjectDelegate = paymentDetailDelegate
        }
    }
    
    // MARK: IBAction Methods
    @IBAction func close(){
        if self.isBeingPresented {
            self.dismiss(animated: true, completion: nil)
        } else {
            popViewController()
        }
    }
    
    @IBAction func submitButton(){
        self.dismiss(animated: true, completion: nil)
    }
    
    // MARK: Table view Delegates
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (userPaymentAccountDetails.count > 0) {
            return userPaymentAccountDetails.count
        } else {
            return 0
        }
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell{
        let cell = tableView.dequeueReusableCell(withIdentifier: "PaymentListTableViewCell", for: indexPath) as! PaymentListTableViewCell
        
                cell.setCellData(userPaymentAccountDetails[indexPath.row], imageUrl: creditCardImages[indexPath.row],selectedCreditCardId: selectedCreditCardId)
                return cell
    }
//    
//    private func tableView(_ tableView: UITableView, cellForRowAtIndexPath indexPath: IndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCell(withIdentifier: "PaymentListTableViewCell", for: indexPath) as! PaymentListTableViewCell
//        
//        cell.setCellData(userPaymentAccountDetails[indexPath.row], imageUrl: creditCardImages[indexPath.row],selectedCreditCardId: selectedCreditCardId)
//        
//        return cell
//    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedIndex = indexPath.row
        performSegue(withIdentifier: "showPaymentDetailsVC", sender: nil);
    }
    
    // MARK: - API calls
    func getUserPaymentDetails() {
        SVProgressHUD.show(withStatus: "Loading...")
        SVProgressHUD.setDefaultMaskType(.clear)
        getUserPaymentDetailsAPICall()
    }
    
    func getUserPaymentDetailsAPICall() {
        refreshControl.endRefreshing()
        InCommUserPaymentService.getPaymentAccountsDetails(InCommUserConfigurationService.sharedInstance.inCommUserId) {
            (userPaymentAccounts, error) in
            self.refreshControl.endRefreshing()
            if let error = error {
                //check user session using error code
                if (error.code == GiftCardAppConstants.errorCodeInvalidUser) {
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, incommError) in
                        if inCommUserStatus{
                            SVProgressHUD.dismiss()
                            self.getUserPaymentDetails()
                        }else{
                            SVProgressHUD.dismiss()
                            //try again message
                            if incommError != nil && incommError!.code == -1009{
                                self.askConfirmationMessage(incommError!)

                            }else{
                                self.askConfirmationMessage(error)
                            }
                        }
                    })
                }else{
                    SVProgressHUD.dismiss()
                    self.askConfirmationMessage(error)
                }
            }
            else{
                SVProgressHUD.dismiss()
                self.userPaymentAccountDetails = userPaymentAccounts
                if (self.userPaymentAccountDetails.count == 0) {
                    self.noRecordFoundView.isHidden = false
                }else{
                    self.mapCreditCardImages()
                    self.tableView.reloadData()
                }
            }
        }
    }
    
    func askConfirmationMessage(_ error:NSError) {
        self.presentConfirmation("Error", message: error.localizedDescription, buttonTitle: "Retry") {(confirmed) -> Void in
            if confirmed{
                self.getUserPaymentDetails()
            }
        }
    }
    
    // MARK: - Map credit card images to the particular Account
    func mapCreditCardImages() {
        creditCardImages = []
        for userPaymentAccountDetail in userPaymentAccountDetails {
            for cardTypeDetail in brand!.creditCardTypesAndImages {
                if cardTypeDetail.creditCardType == userPaymentAccountDetail.creditCardTypeCode {
                    creditCardImages.append(cardTypeDetail.thumbnailImageUrl)
                    break
                }
            }
        }
        self.tableView.reloadData()
    }
    
    //update Add new credit card button name
    func updateButtonName() {
        if (userNewlyAddedCreditCard != nil) {
            addNewPaymentButton.setTitle("Edit Credit Card", for: UIControlState())
        } else {
            addNewPaymentButton.setTitle("Add New Credit Card", for: UIControlState())
        }
    }
    
    //MARK: - page refresh
    func handleRefresh(_ sender: UIRefreshControl) {
        getUserPaymentDetails()
    }
    
    //MARK: - payment view controller delegate to refresh page
    func refreshUserAssociatedGiftCard() {
        getUserPaymentDetails()
    }
}
