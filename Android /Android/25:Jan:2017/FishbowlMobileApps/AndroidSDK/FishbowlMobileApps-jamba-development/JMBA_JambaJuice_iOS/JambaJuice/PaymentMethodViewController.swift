//
//  PaymentMethodViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 02/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class PaymentMethodViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    var selectedIndex: Int?
    var appliedOfferId : String = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    private func billingAccountCount() -> Int {
        var count = 0
        if var billingScheme = BasketService.sharedBasket!.billingSchemes {
            billingScheme = billingScheme.filter { $0.type != "giftcard" }  //Disable gift card
            count = billingScheme.reduce(0) { (initial, scheme) -> Int in
                initial + scheme.accounts.count
            }
        }
        return count
    }
    
    //Disable gift card
    //MARK: Check the gift card billing scheme
    //    func checkGiftCardBillingSchemeCount()->Int{
    //        var count = 1
    //        if let billingSchemes = BasketService.sharedBasket!.billingSchemes{
    //            for billingScheme in billingSchemes {
    //                if billingScheme.type == "giftcard"{
    //                    count = count + 1
    //                     break
    //                }
    //            }
    //            return count
    //        }
    //        else{
    //            return count
    //        }
    //    }
    
    private func billingSchemeAndAccountForIndex(index: Int) -> (BillingScheme, BillingAccount) {
        var billingSch: BillingScheme?
        var billingAccount: BillingAccount?
        var currentSchemeStartingIndex = 0
        var nextSchemeStartingIndex = 0
        if BasketService.sharedBasket!.billingSchemes != nil {
            for billingScheme in BasketService.sharedBasket!.billingSchemes! {
                //Update for current iteration
                //Disable gift card
                if billingScheme.type != "giftcard" {
                    nextSchemeStartingIndex += billingScheme.accounts.count
                    if index >= currentSchemeStartingIndex && index < nextSchemeStartingIndex {
                        billingSch = billingScheme
                        billingAccount = billingScheme.accounts[index - currentSchemeStartingIndex]
                        break
                    }
                    //Update for next iteration
                    currentSchemeStartingIndex += billingScheme.accounts.count
                }
            }
        }
        assert(billingSch != nil, "billingScheme should have been found for index:\(index)")
        assert(billingAccount != nil, "billingAccount should have been found for index:\(index)")
        return (billingSch!, billingAccount!)
    }
    
    @IBAction func placeOrder(sender: UIButton) {
        if selectedIndex == nil {
            presentOkAlert("Payment Method", message: "Please select a payment method.")
            return
        }
        let (_, billingAccount) = billingSchemeAndAccountForIndex(selectedIndex!)
        sender.enabled = false
        SVProgressHUD.showWithStatus("Processing order...", maskType: .Clear)
        BasketService.placeOrderWithBillingAccount(billingAccount) { (orderStatus, error) -> Void in
            sender.enabled = true
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            (self.navigationController as! BasketNavigationController).dismiss()
            
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
                clpAnalyticsService.sharedInstance.clpTrackScreenView("SUBMIT_ORDER")
            }
            
            //            let defaults = NSUserDefaults.standardUserDefaults()
            //            if let customerId = defaults.stringForKey("CustomerId")
            //            {
            //                ClpOfferService.redeemedOffer(customerId,offerId: self.appliedOfferId){ (offerRedeemStatus : String?, error) -> Void in
            //                    SVProgressHUD.dismiss()
            //
            //                    if error != nil {
            //                        self.presentError(error)
            //                        return
            //                    }
            //                    print("joe** response is here \(offerRedeemStatus)")
            //
            //                }
            
            //            }
            
            // Reset gift card list for refresh the balance
            GiftCardCreationService.sharedInstance.resetInCommUserGiftCards()
            
            // TODO: Home Controller should listen to this to make get recent orders call (no need to pass order status here)
            NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.OrderPlaced.rawValue, object: self, userInfo: nil)
            
            BasketService.lastOrderStatus = orderStatus!
            let vc = UIViewController.instantiate("OrderConfirmationVC", storyboard: "Main") as! OrderConfirmationViewController
            self.view.window!.addSubview(vc.view)
        }
    }
    
    //MARK: TableView Row Total Count
    func tableViewRowCount() -> Int{
        var count = 0;
        count = count + billingAccountCount()
        count = count + 1   //for pay using another credit card
        //Disable gift card
        // count = count + checkGiftCardBillingSchemeCount()
        return count
    }
    
    //MARK: DataSource & Delegate
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableViewRowCount()
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if indexPath.row < billingAccountCount() {
            let (billingScheme, billingAccount) = billingSchemeAndAccountForIndex(indexPath.row)
            let paymentInfoCell = tableView.dequeueReusableCellWithIdentifier("PaymentInfoTableViewCell", forIndexPath: indexPath) as! PaymentInfoTableViewCell
            if billingScheme.type == "creditcard" {
                paymentInfoCell.nameLabel.text = billingAccount.cardType
                paymentInfoCell.bottomDetailLabel.text = "XXXX XXXX XXXX " + billingAccount.cardSuffix
            } else if billingScheme.type == "giftcard" {
                paymentInfoCell.nameLabel.text = billingScheme.name
                paymentInfoCell.bottomDetailLabel.text = "XXXX XXXX XXXX \(billingAccount.cardSuffix)"
            } else {
                paymentInfoCell.nameLabel.text = billingScheme.name
                paymentInfoCell.bottomDetailLabel.text = "\(billingAccount.accountType) \(billingAccount.accountId)"
            }
            paymentInfoCell.checkImageView.hidden = selectedIndex != indexPath.row
            return paymentInfoCell
        }
        else {
            let paymentInfoNavigationTableViewCell = tableView.dequeueReusableCellWithIdentifier("PaymentInfoNavigationTableViewCell", forIndexPath: indexPath) as! PaymentInfoNavigationTableViewCell
            //Disable Gift Card
            //            if checkGiftCardBillingSchemeCount() == 2{
            //                let indexPathRowCount = indexPath.row + 1
            //                let totalCount = tableViewRowCount()
            //                if indexPathRowCount == totalCount{
            //                    paymentInfoNavigationTableViewCell.titleLabel.text = "Pay with Jamba Card"
            //                    return paymentInfoNavigationTableViewCell
            //                }
            //                else{
            //                    paymentInfoNavigationTableViewCell.titleLabel.text = "Pay with another credit card"
            //                    return paymentInfoNavigationTableViewCell
            //                }
            //            }
            //            else{
            paymentInfoNavigationTableViewCell.titleLabel.text = "Pay with another credit card"
            return paymentInfoNavigationTableViewCell
            //            }
        }
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if indexPath.row < billingAccountCount() {
            if selectedIndex != nil {
                let cell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: selectedIndex!, inSection: 0)) as? PaymentInfoTableViewCell
                if cell != nil {
                    cell!.checkImageView.hidden = true
                }
            }
            selectedIndex = indexPath.row
            let currentCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: selectedIndex!, inSection: 0)) as! PaymentInfoTableViewCell
            currentCell.checkImageView.hidden = false
        }
        else{
            //Disable Gift Card
            //            if checkGiftCardBillingSchemeCount() == 2{
            //                let indexPathRowCount = indexPath.row + 1
            //                let totalCount = tableViewRowCount()
            //                if indexPathRowCount == totalCount{
            //                    self.performSegueWithIdentifier("GiftCardList", sender: self)
            //                }
            //                else{
            //                    self.performSegueWithIdentifier("EnterCreditCard", sender: self)
            //                }
            //            } else if checkGiftCardBillingSchemeCount() == 1 {
            self.performSegueWithIdentifier("EnterCreditCard", sender: self)
            //            }
        }
        // tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
}
