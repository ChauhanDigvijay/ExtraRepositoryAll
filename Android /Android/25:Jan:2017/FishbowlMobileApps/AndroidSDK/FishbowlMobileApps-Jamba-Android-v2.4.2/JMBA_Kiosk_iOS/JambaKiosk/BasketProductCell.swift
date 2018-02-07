//
//  BasketProductCell.swift
//  JambaKiosk
//
//  Created by Kieran Culliton on 9/15/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import MGSwipeTableCell

class BasketProductCell: MGSwipeTableCell {

    @IBOutlet var productLabel: UILabel!
    @IBOutlet var productModifiersLabel: UILabel!
    @IBOutlet var priceLabel: UILabel!

    func update(basketProduct: BasketProduct) {
        productLabel.text = basketProduct.name.capitalizedString
        let choiceNames = Set(basketProduct.choices.map { $0.name }.filter { $0.hasPrefix("click here to") == false })
        productModifiersLabel.text = choiceNames.joinWithSeparator(", ").uppercaseString
        priceLabel.text = String(format: "$%.02f", basketProduct.totalCost)

        // Configure right button
        let deleteButton = MGSwipeButton(title: "Delete", backgroundColor: UIColor(hex: Constants.jambaGarnetColor))
        rightButtons = [deleteButton]
    }

}
