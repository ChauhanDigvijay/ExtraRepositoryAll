//
//  CreditCard.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/16/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

struct SwipedCreditCard {

    var cardNumber: String
    var firstName: String
    var lastName: String
    var expirationMonth: String
    var expirationYear: String
    var zipcode: String?
    var cvv: String?

    var cardholderName: String {
        return "\(firstName) \(lastName)"
    }

    var expirationDate: String {
        return "\(expirationMonth)/\(expirationYear)"
    }

}
