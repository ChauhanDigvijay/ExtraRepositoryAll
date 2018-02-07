//
//  DeliveryStatusTableViewCell.swift
//  JambaJuice
//
//  Created by VT02 on 10/6/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit

class DeliveryStatusTableViewCell: UITableViewCell {
     @IBOutlet weak var deliveryStatusLabel: UILabel!
    func updateTableViewCell(orderStatus: OrderStatus!){
        if orderStatus.deliveryStatus?.status == nil{
            deliveryStatusLabel.isHidden = true
        }else{
            deliveryStatusLabel.isHidden = false
            let deliveryStatus = orderStatus.deliveryStatus!.status
            deliveryStatusLabel.text = deliveryStatus
            switch deliveryStatus {
            case orderStatusValue.delivered.rawValue:
                deliveryStatusLabel.textColor = UIColor(hex: Constants.jambaGreenColor)
                break
            case orderStatusValue.cancelled.rawValue:
                deliveryStatusLabel.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                break
            case orderStatusValue.failed.rawValue:
                deliveryStatusLabel.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                break
            default:
                deliveryStatusLabel.textColor = UIColor(hex: Constants.jambaMediumGrayColor)
                break
            }
        }

}
}
