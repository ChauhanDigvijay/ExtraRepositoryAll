//
//  TransactionHistoryTableViewCell.swift
//  UIProject
//
//  Created by vThink on 9/15/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import HDK

class TransactionHistoryTableViewCell: UITableViewCell{
    @IBOutlet weak var transactionNameLabel     : UILabel!
    @IBOutlet weak var transctionDateLabel      : UILabel!
    @IBOutlet weak var transactionAmountLabel   : UILabel!
    
    // MARK: - Transaction history data
    func setData(_ transactionHistoryItem:InCommGiftCardTransactionHistoryItem){
        transactionNameLabel.text =  transactionHistoryItem.transactionDescription
        transctionDateLabel.text = transactionHistoryItem.transactionDate!.currentDateIfDateInFuture().timeAgoWithDayAsMinUnit()
        transactionAmountLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,transactionHistoryItem.cardBalance)
    }
}
