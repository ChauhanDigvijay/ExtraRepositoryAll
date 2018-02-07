//
//  InCommGiftCardTransactionType.swift
//  InCommSDK
//
//  Created by vThink on 8/11/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation

public enum InCommGiftCardTransactionType: String{
    case Activate               = "Activate"
    case AddValue               = "AddValue"
    case BalanceInquiry         = "BalanceInquiry"
    case Cashback               = "Cashback"
    case DisableCard            = "DisableCard"
    case EnableCard             = "EnableCard"
    case PartialBalanceTransfer = "PartialBalanceTransfer"
    case RedeemCard             = "RedeemCard"
    case TransactionHistory     = "TransactionHistory"
    case VoidCard               = "VoidCard"
    case VoidTransaction        = "VoidTransaction"
}