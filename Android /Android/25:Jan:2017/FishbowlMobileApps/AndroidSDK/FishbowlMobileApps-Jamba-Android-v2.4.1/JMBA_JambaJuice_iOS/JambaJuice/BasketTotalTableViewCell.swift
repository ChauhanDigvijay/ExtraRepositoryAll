//
//  BasketTotalTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 29/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class BasketTotalTableViewCell: UITableViewCell {
    
    @IBOutlet weak var subtotalCostLabel: UILabel!
    @IBOutlet weak var discountLabel: UILabel!
    @IBOutlet weak var deliveryLabel: UILabel!
    @IBOutlet weak var taxCostLabel: UILabel!
    @IBOutlet weak var totalCostLabel: UILabel!
    @IBOutlet weak var deliveryViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var deliveryView: UIView!
    @IBOutlet weak var deliveryInfoButton:UIButton!
    
    func updateDeliveryLabel() {
        if BasketService.sharedBasket?.deliveryMode == deliveryMode.delivery.rawValue {
            deliveryViewHeightConstraint.constant = 20
            deliveryView.isHidden = false
        } else {
            deliveryViewHeightConstraint.constant = 0
            deliveryView.isHidden = true
        }
    }
    
}
