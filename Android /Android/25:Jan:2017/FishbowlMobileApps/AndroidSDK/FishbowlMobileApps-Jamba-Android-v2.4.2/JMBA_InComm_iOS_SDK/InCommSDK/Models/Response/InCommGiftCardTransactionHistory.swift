//
//  InCommGiftCardTransactionHistory.swift
//  InCommSDK
//
//  Created by vThink on 8/11/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommGiftCardTransactionHistory{
    
    public var transactionHistoryDate: Date?
    public var transactionHistory: [InCommGiftCardTransactionHistoryItem]
    
    public init(json: JSON){
        transactionHistoryDate = json["TransactionHistoryDate"].string?.dateFromInCommFormatString()
        transactionHistory     = json["TransactionHistory"].arrayValue.map {InCommGiftCardTransactionHistoryItem (json: $0)}
    }
}
