//
//  BasketDeliveryModeTableViewCell.swift
//  JambaJuice
//
//  Created by VT02 on 3/9/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit

protocol BasketDeliveryModeTableViewCellDelegate {
    func orderDeliveryModeChanged(_ cell: BasketDeliveryModeTableViewCell)
}

class BasketDeliveryModeTableViewCell: UITableViewCell {
    
    @IBOutlet weak var pickup: SelectableButton!
    @IBOutlet weak var delivery: SelectableButton!
    
    var deliveryModeDelegate: BasketDeliveryModeTableViewCellDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        pickup.addTarget(self, action: #selector(self.selectPickup(_:)), for: .touchUpInside)
        delivery.addTarget(self, action: #selector(self.selectDelivery(_:)), for: .touchUpInside)
        if BasketService.sharedBasket?.deliveryMode == deliveryMode.delivery.rawValue{
            self.selectDelivery(delivery)
        }else{
            self.selectPickup(pickup)
        }
    }
    
    
    func retainSelectedValue() {
        if BasketService.sharedBasket!.deliveryMode == deliveryMode.delivery.rawValue {
            delivery.isSelected = true
            pickup.isSelected = false
        } else {
            pickup.isSelected = true
            delivery.isSelected = false
        }
    }
    
    func selectPickup(_ sender:SelectableButton) {
        log.verbose("Delivery Mode: Pickup")
        pickup.isSelected = true
        delivery.isSelected = false
        if BasketService.sharedBasket?.deliveryMode != deliveryMode.pickup.rawValue {
            BasketService.sharedBasket?.deliveryMode = deliveryMode.pickup.rawValue
            deliveryModeDelegate?.orderDeliveryModeChanged(self)
        }
    }
    func selectDelivery(_ sender:SelectableButton) {
        log.verbose("Delivery Mode: Dispatch")
        delivery.isSelected = true
        pickup.isSelected = false
        if BasketService.sharedBasket?.deliveryMode != deliveryMode.delivery.rawValue {
            BasketService.sharedBasket?.deliveryMode = deliveryMode.delivery.rawValue
            deliveryModeDelegate?.orderDeliveryModeChanged(self)
        }
    }
}
