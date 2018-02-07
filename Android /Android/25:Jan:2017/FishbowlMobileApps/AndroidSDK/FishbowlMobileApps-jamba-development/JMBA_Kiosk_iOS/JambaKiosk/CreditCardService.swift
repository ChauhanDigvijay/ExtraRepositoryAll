//
//  CreditCardService.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/19/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

class CreditCardService {

    static let sharedInstance = CreditCardService()

    private(set) var swipedCreditCard: SwipedCreditCard?
    private(set) var storedCreditCards: [UserSavedCreditCard] = []

    /// Load user stored credit cards
    func userCreditCards(callback: (error: NSError?) -> Void) {
        guard let basket = BasketService.sharedInstance.currentBasket else {
            callback(error: NSError(description: "Basket not found."))
            return
        }
        OloBasketService.billingSchemes(basket.basketId) { (oloBillingSchemes, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            let oloBillingAccounts = oloBillingSchemes.flatMap { $0.accounts }
            self.storedCreditCards = oloBillingAccounts.map { UserSavedCreditCard(oloBillingAccount: $0) }
            callback(error: nil)
        }
    }

    func updateSwipedCard(swipedCreditCard: SwipedCreditCard?) {
        self.swipedCreditCard = swipedCreditCard
    }

    func updateSwipedCardZipcode(zipcode: String?) {
        swipedCreditCard?.zipcode = zipcode
    }

    func updateSwipedCardCVV(cvv: String?) {
        swipedCreditCard?.cvv = cvv
    }

    func reset() {
        swipedCreditCard = nil
        storedCreditCards = []
    }

}
