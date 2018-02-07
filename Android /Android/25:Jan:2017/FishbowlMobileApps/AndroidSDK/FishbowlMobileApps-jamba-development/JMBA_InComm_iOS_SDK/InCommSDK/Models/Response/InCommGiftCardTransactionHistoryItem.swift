//
//  InCommGiftCardTransactionHistoryItem.swift
//  InCommSDK
//
//  Created by vThink on 8/11/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommGiftCardTransactionHistoryItem{
    
    public var cardBalance: Double
    public var transactionAmount: Double
    public var transactionDate: NSDate?
    public var transactionDescription: String
    public var transactionType: InCommGiftCardTransactionType?
    
    public init(json: JSON){
        cardBalance            = json["CardBalance"].doubleValue
        transactionAmount      = json["TransactionAmount"].doubleValue
        transactionDate        = json["TransactionDate"].string?.dateFromInCommFormatString()
        transactionDescription = json["TransactionDescription"].stringValue
        transactionType        = InCommGiftCardTransactionType(rawValue: json["TransactionType"].stringValue)
    }
}