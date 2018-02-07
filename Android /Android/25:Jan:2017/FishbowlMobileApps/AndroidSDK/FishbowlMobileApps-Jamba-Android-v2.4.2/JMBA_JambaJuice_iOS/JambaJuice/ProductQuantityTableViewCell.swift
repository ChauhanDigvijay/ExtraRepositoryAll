//
//  ProductQuantityTableViewCell.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol ProductQuantityTableViewCellDelegate: class {
    func productQuantityTableViewCellDidReceiveDecreaseEvent(_ cell: ProductQuantityTableViewCell)
    func productQuantityTableViewCellDidReceiveIncreaseEvent(_ cell: ProductQuantityTableViewCell)
}

class ProductQuantityTableViewCell: UITableViewCell {

    weak var delegate: ProductQuantityTableViewCellDelegate?
    @IBOutlet weak var quantityLabel: UILabel!
    @IBOutlet weak var quantityTextLabel: UILabel!
    @IBOutlet weak var increaseQuantityButton: UIButton!
    @IBOutlet weak var decreaseQuantityButton: UIButton!
    
    @IBAction func increaseQuantity(_ sender: UIButton) {
        delegate?.productQuantityTableViewCellDidReceiveIncreaseEvent(self)
    }
    
    @IBAction func decreaseQuantity(_ sender: UIButton) {
        delegate?.productQuantityTableViewCellDidReceiveDecreaseEvent(self)
    }
    
}
