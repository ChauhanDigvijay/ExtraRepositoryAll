//
//  CancelOrderStatusTableViewCell.swift
//  JambaJuice
//
//  Created by VT02 on 10/6/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit

class CancelOrderStatusTableViewCell: UITableViewCell {
    @IBOutlet weak var descLabel: UILabel!
    @IBOutlet weak var orderStatusLabel: UILabel!
    
    @IBOutlet weak var cancelReasonLabel: UILabel!
    @IBOutlet weak var cancelReasonLabelHeight: NSLayoutConstraint!
    
    func updateCell(orderStatus: OrderStatus!){
        let costString = String(format: "Total $%.2f",  orderStatus.total)
        descLabel.text = costString
        if orderStatus.status.isEmpty{
            orderStatusLabel.isHidden = true
        }else{
            switch orderStatus.status {
            case orderStatusValue.completed.rawValue:
                orderStatusLabel.textColor = UIColor(hex: Constants.jambaGreenColor)
                break
            case orderStatusValue.cancelled.rawValue:
                orderStatusLabel.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                break
            case orderStatusValue.failed.rawValue:
                orderStatusLabel.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                break
            default:
                orderStatusLabel.textColor = UIColor(hex: Constants.jambaMediumGrayColor)
                break
            }
            orderStatusLabel.text = orderStatus.status
        }
        if  orderStatus.status == orderStatusValue.cancelled.rawValue{
            cancelReasonLabel.isHidden = false
            cancelReasonLabel.text = "Cancellation Reason: Canceled by user"
            cancelReasonLabelHeight.constant = 30
            cancelReasonLabel.adjustsFontSizeToFitWidth = true
        } else {
            cancelReasonLabel.isHidden = true
            cancelReasonLabelHeight.constant = 0
        }
    }

}
