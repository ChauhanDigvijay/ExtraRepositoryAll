//
//  ClearBasketTableViewCell.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/13/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol ClearBasketTableViewCellDelegate {
    func clearBasket()
}

class ClearBasketTableViewCell: UITableViewCell {

    @IBOutlet var clearBasketButton: UIButton!

    var delegate: ClearBasketTableViewCellDelegate?

    override func awakeFromNib() {
        clearBasketButton.roundify(4)
        clearBasketButton.stroke(UIColor(hex: 0xEBEBEB), width: 1)
    }

    @IBAction func clearBasket(sender: UIButton) {
        delegate?.clearBasket()
    }

}
