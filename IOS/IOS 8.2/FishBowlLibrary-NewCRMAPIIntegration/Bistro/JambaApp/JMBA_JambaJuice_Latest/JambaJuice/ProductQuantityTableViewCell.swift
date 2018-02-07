//
//  ProductQuantityTableViewCell.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol ProductQuantityTableViewCellDelegate: class {
    func productQuantityTableViewCellDidReceiveDecreaseEvent(cell: ProductQuantityTableViewCell)
    func productQuantityTableViewCellDidReceiveIncreaseEvent(cell: ProductQuantityTableViewCell)
}

class ProductQuantityTableViewCell: UITableViewCell {

    weak var delegate: ProductQuantityTableViewCellDelegate?
    @IBOutlet weak var quantityLabel: UILabel!
    @IBOutlet weak var quantityTextLabel: UILabel!
    @IBOutlet weak var increaseQuantityButton: UIButton!
    @IBOutlet weak var decreaseQuantityButton: UIButton!
    
    @IBAction func increaseQuantity(sender: UIButton) {
        delegate?.productQuantityTableViewCellDidReceiveIncreaseEvent(self)
    }
    
    @IBAction func decreaseQuantity(sender: UIButton) {
        delegate?.productQuantityTableViewCellDidReceiveDecreaseEvent(self)
    }
    
}
