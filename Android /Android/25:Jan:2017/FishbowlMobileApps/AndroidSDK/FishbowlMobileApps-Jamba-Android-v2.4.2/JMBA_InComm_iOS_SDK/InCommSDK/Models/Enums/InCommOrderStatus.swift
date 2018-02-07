//
//  InCommOrderStatus.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public enum InCommOrderStatus: String {
    case Cancelled = "Cancelled"
    case GeneralOrderFailure = "GeneralOrderFailure"
    case OrderPendingApproval = "OrderPendingApproval"
    case OrderPendingInventory = "OrderPendingInventory"
    case OrderPendingPayment = "OrderPendingPayment"
    case Success = "Success"
}