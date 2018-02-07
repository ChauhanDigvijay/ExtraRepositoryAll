//
//  PaymentMethodViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 02/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import MGSwipeTableCell

enum SelectedPaymentCardType : String {
    case Credit = "Credit"
    case Jamba = "Jamba"
}

enum BillingSchemeType : String{
    case CreditCard = "creditcard"
    case JambaCard  = "giftcard"
    case PayInStore = "payinstore"
}

class PaymentMethodViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, MGSwipeTableCellDelegate{
    @IBOutlet weak var tableView: UITableView!
    var selectedIndex: Int?
    var appliedOfferId : String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    fileprivate func billingAccountCount() -> Int {
        var count = 0
        if BasketService.sharedBasket!.billingSchemes != nil {
            count = BasketService.sharedBasket!.billingSchemes!.reduce(0) { (initial, scheme) -> Int in
                initial + scheme.accounts.count
            }
        }
        return count
    }
    
    
    
    fileprivate func billingSchemeAndAccountForIndex(_ index: Int) -> (BillingScheme, BillingAccount) {
        var billingSch: BillingScheme?
        var billingAccount: BillingAccount?
        var currentSchemeStartingIndex = 0
        var nextSchemeStartingIndex = 0
        if BasketService.sharedBasket!.billingSchemes != nil {
            for billingScheme in BasketService.sharedBasket!.billingSchemes! {
                //Update for current iteration
                if (BasketService.sharedBasket!.deliveryMode == deliveryMode.delivery.rawValue ? billingScheme.type != BillingSchemeType.PayInStore.rawValue : true){
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
    
    @IBAction func placeOrder(_ sender: UIButton) {
        // Fishbowl Event
        var basketId = ""
        if let basket = BasketService.sharedBasket{
            basketId = "\(basket.id)"
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent(basketId, item_name: "", event_name: "SUBMIT_ORDER")
        
        if selectedIndex == nil {
            presentOkAlert("Payment Method", message: "Please select a payment method.")
            return
        }
        let (_, billingAccount) = billingSchemeAndAccountForIndex(selectedIndex!)
        sender.isEnabled = false
        SVProgressHUD.show(withStatus: "Processing order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.placeOrderWithBillingAccount(billingAccount) { (orderStatus, error) -> Void in
            sender.isEnabled = true
            SVProgressHUD.dismiss()
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
    
    //MARK: TableView Row Total Count
    func tableViewRowCount() -> Int{
        // Default value for Pay using another credit card
        var count = 1;
        // Default value for Jamba Card Navigation cell only logged in user for precaution
        if UserService.sharedUser != nil{
            count = count + 1
        }
        count = count + billingAccountCount()
        return count
    }
    
    //MARK: DataSource & Delegate
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableViewRowCount()
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row < billingAccountCount() {
            let (billingScheme, billingAccount) = billingSchemeAndAccountForIndex(indexPath.row)
            if billingScheme.type == BillingSchemeType.CreditCard.rawValue {
                let paymentAccountTableViewCell = tableView.dequeueReusableCell(withIdentifier: "PaymentAccountTableViewCell", for: indexPath) as! PaymentAccountTableViewCell
                paymentAccountTableViewCell.selectionStyle = .none
                if let creditCardTypeImage = UIImage(named: paymentAccountTableViewCell.getCreditCardImageName(creditCardTypeName:  billingAccount.cardType)){
                    paymentAccountTableViewCell.creditCardTypeImageView.image = creditCardTypeImage
                    paymentAccountTableViewCell.delegate = self
                }
                paymentAccountTableViewCell.creditCardCVVNumber.text = billingAccount.cardSuffix
                paymentAccountTableViewCell.checkImageView.isHidden = selectedIndex != indexPath.row
                paymentAccountTableViewCell.delegate = self
                paymentAccountTableViewCell.updateTableViewCell()
                return paymentAccountTableViewCell
            } else if billingScheme.type == BillingSchemeType.JambaCard.rawValue{
                let paymentAccountTableViewCell = tableView.dequeueReusableCell(withIdentifier: "PaymentAccountTableViewCell", for: indexPath) as! PaymentAccountTableViewCell
                paymentAccountTableViewCell.selectionStyle = .none
                if let creditCardTypeImage = UIImage(named:GiftCardAppConstants.jambaDefaultCard){
                    paymentAccountTableViewCell.creditCardTypeImageView.image = creditCardTypeImage
                    paymentAccountTableViewCell.delegate = self
                }
                paymentAccountTableViewCell.creditCardCVVNumber.text = billingAccount.cardSuffix
                paymentAccountTableViewCell.checkImageView.isHidden = selectedIndex != indexPath.row
                paymentAccountTableViewCell.delegate = self
                paymentAccountTableViewCell.updateTableViewCell()
                return paymentAccountTableViewCell
            }
            else if billingScheme.type == BillingSchemeType.PayInStore.rawValue{
                let paymentInfoCell = tableView.dequeueReusableCell(withIdentifier: "PaymentInfoTableViewCell", for: indexPath) as! PaymentInfoTableViewCell
                paymentInfoCell.selectionStyle = .none
                paymentInfoCell.nameLabel.text = billingScheme.name
                paymentInfoCell.bottomDetailLabel.text = "\(billingAccount.accountType)"
                paymentInfoCell.checkImageView.isHidden = selectedIndex != indexPath.row
                return paymentInfoCell
            }
        }
        let paymentInfoNavigationTableViewCell = tableView.dequeueReusableCell(withIdentifier: "PaymentInfoNavigationTableViewCell", for: indexPath) as! PaymentInfoNavigationTableViewCell
        paymentInfoNavigationTableViewCell.selectionStyle = .none
        if UserService.sharedUser != nil{
            let indexPathRowCount = indexPath.row + 1
            let totalCount = tableViewRowCount()
            if indexPathRowCount == totalCount{
                if self.checkPaymentMethodType(paymentMethodType: BillingSchemeType.JambaCard.rawValue){
                    paymentInfoNavigationTableViewCell.titleLabel.text = "Pay with another Jamba Card"
                }else{
                    paymentInfoNavigationTableViewCell.titleLabel.text = "Pay with Jamba Card"
                }
                return paymentInfoNavigationTableViewCell
            }
        }
        if self.checkPaymentMethodType(paymentMethodType: BillingSchemeType.CreditCard.rawValue){
            paymentInfoNavigationTableViewCell.titleLabel.text = "Pay with another Credit Card"
        }else{
            paymentInfoNavigationTableViewCell.titleLabel.text = "Pay with Credit Card"
        }
        
        return paymentInfoNavigationTableViewCell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.row < billingAccountCount() {
            if selectedIndex != nil {
                if selectedIndex == indexPath.row{
                    if  let cell = tableView.cellForRow(at: IndexPath(row: indexPath.row, section: 0)) as? PaymentAccountTableViewCell{
                        cell.checkImageView.isHidden = true
                    }else if  let cell = tableView.cellForRow(at: IndexPath(row: indexPath.row, section: 0)) as? PaymentInfoTableViewCell{
                        cell.checkImageView.isHidden = true
                    }
                    selectedIndex = nil
                }else{
                    if  let cell = tableView.cellForRow(at: IndexPath(row: indexPath.row, section: 0)) as? PaymentAccountTableViewCell{
                        cell.checkImageView.isHidden = false
                    }else if  let cell = tableView.cellForRow(at: IndexPath(row: indexPath.row, section: 0)) as? PaymentInfoTableViewCell{
                        cell.checkImageView.isHidden = false
                    }
                    if let currentCell = tableView.cellForRow(at: IndexPath(row: selectedIndex!, section: 0)) as? PaymentInfoTableViewCell{
                        currentCell.checkImageView.isHidden = true
                    }else if let currentCell = tableView.cellForRow(at: IndexPath(row: selectedIndex!, section: 0)) as? PaymentAccountTableViewCell{
                        currentCell.checkImageView.isHidden = true
                    }
                    selectedIndex = indexPath.row
                }
            }
            else{
                selectedIndex = indexPath.row
                if let currentCell = tableView.cellForRow(at: IndexPath(row: selectedIndex!, section: 0)) as? PaymentInfoTableViewCell{
                    currentCell.checkImageView.isHidden = false
                }else if let currentCell = tableView.cellForRow(at: IndexPath(row: selectedIndex!, section: 0)) as? PaymentAccountTableViewCell{
                    currentCell.checkImageView.isHidden = false
                }
           }
        }
        else{
            if UserService.sharedUser != nil{
                let indexPathRowCount = indexPath.row + 1
                let totalCount = tableViewRowCount()
                if indexPathRowCount == totalCount{
                    self.performSegue(withIdentifier: "GiftCardList", sender: self)
                    return
                }
            }
            self.performSegue(withIdentifier: "EnterCreditCard", sender: self)
        }
    }
    func swipeTableCell(_ cell: MGSwipeTableCell, tappedButtonAt index: Int, direction: MGSwipeDirection, fromExpansion: Bool) -> Bool {
        if let indexPath = tableView.indexPath(for: cell) {
            let (billingScheme, billingAccount) = billingSchemeAndAccountForIndex(indexPath.row)
            if billingScheme.type == BillingSchemeType.CreditCard.rawValue {
                self.removeCreditOrJambaCard(accountId:  billingAccount.accountId, selectedCardType: SelectedPaymentCardType.Credit.rawValue)
            }else if  billingScheme.type == BillingSchemeType.JambaCard.rawValue {
                self.removeCreditOrJambaCard(accountId:  billingAccount.accountId, selectedCardType: SelectedPaymentCardType.Jamba.rawValue)
            }
            
        }
        
        return true
    }
    func removeCreditOrJambaCard(accountId: Int64!,selectedCardType:String){
        presentConfirmation("Remove \(selectedCardType) Card", message: "Are you sure you want to delete this saved \(selectedCardType.lowercased()) card?", buttonTitle: "Yes") { (confirmed) in
            if confirmed{
                SVProgressHUD.show(withStatus: "Removing \(selectedCardType) Card...")
                SVProgressHUD.setDefaultMaskType(.clear)
                BasketService.deleteCreditCard(creditCardId: accountId) { (error) in
                    SVProgressHUD.dismiss()
                    if error != nil{
                        self.presentError(error)
                    }else{
                        self.presentOkAlert("Success!", message: "\(selectedCardType) card removed successfully.", callback: {
                            self.selectedIndex = nil
                            self.tableView.reloadData()
                        })
                    }
                }
            }
        }
    }
    
    //MARK: check payment method type
    func checkPaymentMethodType(paymentMethodType:String) -> Bool{
        var result = false
        if let billingSchemes = BasketService.sharedBasket!.billingSchemes {
            if let index = billingSchemes.index(where: {$0.type ==  paymentMethodType}){
                let scheme = billingSchemes[index]
                if scheme.accounts.count > 0{
                    result = true
                }
            }
        }
          return result
    }
}
