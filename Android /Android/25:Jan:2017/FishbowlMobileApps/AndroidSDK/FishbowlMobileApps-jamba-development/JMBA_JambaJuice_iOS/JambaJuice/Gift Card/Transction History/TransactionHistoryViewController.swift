//
//  TransactionHistoryViewController.swift
//  UIProject
//
//  Created by vThink on 9/15/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import SVProgressHUD
import InCommSDK

class TransactionHistoryViewController: UIViewController, UITableViewDelegate, UITableViewDataSource{
    @IBOutlet weak var tableView    : UITableView!
    var giftCardId                  : Int32?
    var giftCardTransactionHistory  : InCommGiftCardTransactionHistory?
    
    // MARK: - View did load
    override func viewDidLoad() {
        let triggerTime = (Int64(NSEC_PER_SEC) * 0)
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, triggerTime), dispatch_get_main_queue(), { () -> Void in
            self.getTransactionHistoryDetails()
        })
        super.viewDidLoad()
    }
    
    // MARK: - Did receive memory warning
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    //MARK: - Tableview delegate
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if giftCardTransactionHistory == nil{
            return 0
        }
        return giftCardTransactionHistory!.transactionHistory.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("TransactionHistoryTableViewCell", forIndexPath: indexPath) as! TransactionHistoryTableViewCell
        cell.setData((giftCardTransactionHistory?.transactionHistory[indexPath.row])!)
        return cell
    }
    
    // MARK: - Get transaction history details
    func getTransactionHistoryDetails(){
        if giftCardId == nil{
            self.presentError(GiftCardAppConstants.generalError)
            return
        }
        SVProgressHUD.showWithStatus("Loading...", maskType: .Clear)
        GiftCardCreationService.sharedInstance.getTransactionHistoryDetails(giftCardId) {
            (inCommTransactionHistory, error) in
            SVProgressHUD.dismiss()
            // Incomm response: I have investigated the error message on our end and it appears that Radiant/NCR does not support transaction history.
            // So we ignoring this error message
            if error != nil && error!.code != 500202 {
                self.presentError(error)
                return
            } else if inCommTransactionHistory != nil && inCommTransactionHistory?.transactionHistory.count > 0 {
                self.giftCardTransactionHistory = inCommTransactionHistory
                self.tableView.reloadData()
            }
        }
    }
}
