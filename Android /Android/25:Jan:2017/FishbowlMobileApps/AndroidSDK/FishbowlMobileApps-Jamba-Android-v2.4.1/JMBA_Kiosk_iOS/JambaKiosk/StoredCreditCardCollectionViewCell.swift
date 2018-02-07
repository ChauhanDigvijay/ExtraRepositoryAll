//
//  StoredCreditCardCollectionViewCell.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/16/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class StoredCreditCardCollectionViewCell: UICollectionViewCell {

    @IBOutlet var cardNumberLabel: UILabel!
    @IBOutlet var cardholderNameLabel: UILabel!
    @IBOutlet var expirationDateLabel: UILabel!
    @IBOutlet var cardTypeImageView: UIImageView!
    @IBOutlet var selectedBorderView: UIView!

    func update(creditCard: UserSavedCreditCard) {
        let cardPrefix = creditCard.cardType == "Amex" ? "XXXX XXXXXX X" : "XXXX XXXX XXXX "

        cardNumberLabel.text = "\(cardPrefix)\(creditCard.cardSuffix)"
        cardholderNameLabel.text = creditCard.cardholderName ?? "Awesome Placeholder"
        expirationDateLabel.text  = creditCard.expiration

        switch creditCard.cardType {
        case "Amex":       cardTypeImageView.image = UIImage(named: "cc-logo-inside-amex")
        case "Visa":       cardTypeImageView.image = UIImage(named: "cc-logo-inside-visa")
        case "Mastercard": cardTypeImageView.image = UIImage(named: "cc-logo-inside-mastercard")
        case "Discover":   cardTypeImageView.image = UIImage(named: "cc-logo-inside-discover")
        default: break
        }

        updateSelection()
    }

    func updateSelection() {
        if selected {
            let lightBlueColor = UIColor(hex: Constants.jambaLightBlueColor)
            selectedBorderView.stroke(lightBlueColor, width: 2)
            selectedBorderView.layer.shadowColor = lightBlueColor.CGColor
            selectedBorderView.layer.shadowRadius = 8
        } else {
            selectedBorderView.stroke(UIColor.clearColor(), width: 0)
            selectedBorderView.layer.shadowColor = UIColor.whiteColor().CGColor
            selectedBorderView.layer.shadowRadius = 0
        }
    }

}
