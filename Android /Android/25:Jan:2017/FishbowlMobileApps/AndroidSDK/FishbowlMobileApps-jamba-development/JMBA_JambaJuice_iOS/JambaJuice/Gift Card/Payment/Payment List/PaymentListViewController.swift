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

class PaymentListViewController: UIViewController,UITableViewDelegate, PaymentViewControllerDelegate {
    
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
        noRecordFoundView.hidden = true
        brand = InCommGiftCardBrandDetails.sharedInstance.brand
        
        //after launching the screen call the get gift card API
        let triggerTime = (Int64(NSEC_PER_SEC) * 0)
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, triggerTime), dispatch_get_main_queue(), { () -> Void in
            self.getUserPaymentDetails()
        })
        
        //update "add new credit card button name"
        updateButtonName()
        
        if let paymentDetailId = userSelectedCard {
            selectedCreditCardId = paymentDetailId.paymentAccountId
        }
        
        refreshControl.addTarget(self, action: #selector(PaymentListViewController.handleRefresh(_:)), forControlEvents: .ValueChanged)
        
        refreshControl.endRefreshing()
        
        self.tableView.addSubview(refreshControl)
        super.viewDidLoad()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "showAddPaymentVC" {
            let targetController =  segue.destinationViewController as! AddPaymentViewController
            targetController.headerTitle = headerTitle
            targetController.enableSavePayment = enableSavePayment
            targetController.cardAmount = cardAmount
            targetController.saveBillingPayment = saveBillingPayment
            targetController.userNewlyAddedCreditCard = userNewlyAddedCreditCard
            targetController.userSavedPaymentDetails = userSavedPaymentDetails
            targetController.delegate = addPaymentDetailDelegate
        } else if segue.identifier == "showPaymentDetailsVC" {
            let targetController =  segue.destinationViewController as! PaymentViewController
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
        if self.isBeingPresented() {
            self.dismissViewControllerAnimated(true, completion: nil)
        } else {
            popViewController()
        }
    }
    
    @IBAction func submitButton(){
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    // MARK: Table view Delegates
    
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (userPaymentAccountDetails.count > 0) {
            return userPaymentAccountDetails.count
        } else {
            return 0
        }
    }
    
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("PaymentListTableViewCell", forIndexPath: indexPath) as! PaymentListTableViewCell
        
        cell.setCellData(userPaymentAccountDetails[indexPath.row], imageUrl: creditCardImages[indexPath.row],selectedCreditCardId: selectedCreditCardId)
        
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        selectedIndex = indexPath.row
        performSegueWithIdentifier("showPaymentDetailsVC", sender: nil);
    }
    
    // MARK: - API calls
    func getUserPaymentDetails() {
        SVProgressHUD.showWithStatus("Loading...", maskType: .Clear)
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
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            SVProgressHUD.dismiss()
                            self.getUserPaymentDetails()
                        }else{
                            SVProgressHUD.dismiss()
                            //try again message
                            self.askConfirmationMessage(error)
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
                    self.noRecordFoundView.hidden = false
                }else{
                    self.mapCreditCardImages()
                    self.tableView.reloadData()
                }
            }
        }
    }
    
    func askConfirmationMessage(error:NSError) {
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
            addNewPaymentButton.setTitle("Edit Credit Card", forState: .Normal)
        } else {
            addNewPaymentButton.setTitle("Add New Credit Card", forState: .Normal)
        }
    }
    
    //MARK: - page refresh
    func handleRefresh(sender: UIRefreshControl) {
        getUserPaymentDetails()
    }
    
    //MARK: - payment view controller delegate to refresh page
    func refreshUserAssociatedGiftCard() {
        getUserPaymentDetails()
    }
}