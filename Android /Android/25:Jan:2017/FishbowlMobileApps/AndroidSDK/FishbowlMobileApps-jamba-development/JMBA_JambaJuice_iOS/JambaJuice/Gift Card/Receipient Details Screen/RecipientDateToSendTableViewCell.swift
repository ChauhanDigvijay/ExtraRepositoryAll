//
//  RecipientDateToSendTableViewCell.swift
//  JambaJuice
//
//  Created by VT010 on 9/30/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit


class RecipientDateToSendTableViewCell : UITableViewCell{
    @IBOutlet weak var dateToSendLabel:UILabel!
    
    
    func setData(dateToSend:NSDate){
        dateToSendLabel.text = GiftCardAppConstants.getDateToSend(dateToSend)
    }
}