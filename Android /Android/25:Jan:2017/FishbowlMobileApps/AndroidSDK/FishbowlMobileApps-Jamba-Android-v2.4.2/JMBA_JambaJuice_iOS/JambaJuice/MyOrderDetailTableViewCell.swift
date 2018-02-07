//
//  MyOrderDetailTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/23/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class MyOrderDetailTableViewCell: UITableViewCell {

    @IBOutlet weak var nameLabel: UILabel!
    func updateCell(orderStatus: OrderStatus!){
        var detailsText =  ""
        if orderStatus.readyTime != nil{
            let dateString = orderStatus.readyTime!.orderHistoryDateString()
            let timeString = orderStatus.readyTime!.timeString()
            if orderStatus.status == orderStatusValue.cancelled.rawValue{
                detailsText  = "Ordered " + orderStatus.timePlaced!.currentDateIfDateInFuture().timeAgoWithDayAsMinUnit();                                nameLabel.adjustsFontSizeToFitWidth = true
            } else if let deliveryStatus = orderStatus.deliveryStatus{
                var deliveryDetails = ""
                if deliveryStatus.drivername != ""{
                    deliveryDetails = " by \(deliveryStatus.drivername) (\(deliveryStatus.driverphonenumber.formatPhoneNumber())) from \(deliveryStatus.deliveryservice) service."
                }
                if deliveryStatus.status == orderStatusValue.delivered.rawValue{
                    let deliveryValue = deliveryDetails == "" ? "." : deliveryDetails
                    detailsText = "Order delivered on \(dateString) at \(timeString)\(deliveryValue)"
                }else {
                    let deliveryValue = deliveryDetails == "" ? "." : deliveryDetails
                    detailsText = "Order will be delivered on \(dateString) at \(timeString)\(deliveryValue)"
                }
            } else{
                if orderStatus.readyTime!.timeIntervalSinceNow > 0{
                    detailsText = "Pick up on \(dateString) at \(timeString)"
                }else{
                    detailsText = "Picked up on \(dateString) at \(timeString)"
                }
            }
        } else {
            detailsText = "Pick up time not available"
        }
        nameLabel.text = detailsText
        nameLabel.adjustsFontSizeToFitWidth = true
    }
}
