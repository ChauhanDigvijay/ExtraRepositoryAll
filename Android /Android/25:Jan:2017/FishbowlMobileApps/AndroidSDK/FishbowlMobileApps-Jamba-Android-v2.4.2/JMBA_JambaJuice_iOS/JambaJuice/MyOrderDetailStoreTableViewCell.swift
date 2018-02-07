//
//  MyOrderDetailStoreTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/26/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class MyOrderDetailStoreTableViewCell: UITableViewCell {
    
    @IBOutlet weak var nameAndAddrLabel: UILabel!
    @IBOutlet weak var rightArrow: UIImageView!
    func updateOrderedAtStoreDetails(orderStatus:OrderStatus!, orderedAt:Bool){
        rightArrow.isHidden = orderedAt ? false:true
        isUserInteractionEnabled = orderedAt ? true:false
        selectionStyle = .none
        nameAndAddrLabel.font.withSize(15)
        nameAndAddrLabel.text = orderedAt ? orderStatus.getOrderAtStore() : orderStatus.getDeliveryAddress()
    }
}

