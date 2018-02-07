//
//  SwipedCreditCardCollectionViewCell.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/16/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol SwipedCreditCardCollectionViewCellDelegate {
    func swipedCreditCardEnterZipcode(view: UIView)
    func swipedCreditCardEnterCVV(view: UIView)
}

class SwipedCreditCardCollectionViewCell: UICollectionViewCell {

    @IBOutlet var cardNumberLabel: UILabel!
    @IBOutlet var cardholderNameLabel: UILabel!
    @IBOutlet var expirationDateLabel: UILabel!
    @IBOutlet var zipcodeLabel: UILabel!
    @IBOutlet var zipcodeView: UIView!
    @IBOutlet var cvvLabel: UILabel!
    @IBOutlet var cvvView: UIView!
    @IBOutlet var selectedBorderView: UIView!

    var delegate: SwipedCreditCardCollectionViewCellDelegate?

    func update(creditCard: SwipedCreditCard) {
        let lastFour = creditCard.cardNumber.substringFromIndex(creditCard.cardNumber.endIndex.advancedBy(-4))
        let cardPrefix = "XXXX XXXX XXXX "

        cardNumberLabel.text = "\(cardPrefix)\(lastFour)"
        cardholderNameLabel.text = creditCard.cardholderName
        expirationDateLabel.text = creditCard.expirationDate

        updateOptionalLabel(zipcodeLabel, value: creditCard.zipcode, placeholder: "Zip")
        updateOptionalLabel(cvvLabel, value: creditCard.cvv, placeholder: "CVV")

        zipcodeView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: "enterZipcode:"))
        cvvView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: "enterCVV:"))
        deselectAllFields()
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

    func selectZipcodeField() {
        zipcodeView.stroke(UIColor.whiteColor(), width: 1)
        cvvView.stroke(UIColor.clearColor(), width: 0)
        delegate?.swipedCreditCardEnterZipcode(zipcodeView)
    }

    func selectCVVField() {
        zipcodeView.stroke(UIColor.clearColor(), width: 0)
        cvvView.stroke(UIColor.whiteColor(), width: 1)
        delegate?.swipedCreditCardEnterCVV(cvvView)
    }

    func deselectAllFields() {
        zipcodeView.stroke(UIColor.clearColor(), width: 0)
        cvvView.stroke(UIColor.clearColor(), width: 0)
    }

    private func updateOptionalLabel(label: UILabel, value: String?, placeholder: String) {
        if value != nil {
            label.text = value
            label.textColor = UIColor.whiteColor()
            label.font = UIFont(name: Constants.ocrAStandard, size: 18)
        } else {
            label.text = placeholder
            label.textColor = UIColor(hex: Constants.jambaDarkGreenColor)
            label.font = UIFont(name: Constants.archerMedium, size: 18)
        }
    }


    // MARK: UIGestureRecognizer

    func enterZipcode(sender: AnyObject) {
        selectZipcodeField()
    }

    func enterCVV(sender: AnyObject) {
        selectCVVField()
    }

}
