//
//  InCommOrderPaymentMethod.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public enum InCommOrderPaymentMethod: String {
    case ACH = "ACH"
    case Cash = "Cash"
    case Check = "Check"
    case CorporateAccountBalancePayment = "CorporateAccountBalancePayment"
    case CreditCard = "CreditCard"
    case CreditCardAlternative = "CreditCardAlternative"
    case CreditCardManualEntry = "CreditCardManualEntry"
    case CustomerCredit = "CustomerCredit"
    case EmployeePayment = "EmployeePayment"
    case GeneralLedger = "GeneralLedger"
    case NoFundsCollected = "NoFundsCollected"
    case Other = "Other"
    case Promotional = "Promotional"
    case Reissue = "Reissue"
    case ResellerIssued = "ResellerIssued"
    case Return = "Return"
    case Terms = "Terms"
    case Upload = "Upload"
}
