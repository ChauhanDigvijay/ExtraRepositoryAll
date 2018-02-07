//
//  EndDateSelectionTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 17/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit

class EndDateSelectionTableViewCell: UITableViewCell {
    
    @IBOutlet weak var name         : UILabel!
    @IBOutlet weak var endDateLabel : UILabel!
    
    func setData(endDate:String) {
        endDateLabel.text = endDate
    }
    
    override func layoutSubviews() {
        if (endDateLabel.text == "") {
            makeDeActiveCell()
        }
    }
    
    func makeActiveCell() {
        self.alpha = GiftCardAppConstants.tableViewCellActiveState
    }
    
    func makeDeActiveCell() {
        endDateLabel.text = ""
        self.alpha = GiftCardAppConstants.tableViewCellDeactiveState
    }
}