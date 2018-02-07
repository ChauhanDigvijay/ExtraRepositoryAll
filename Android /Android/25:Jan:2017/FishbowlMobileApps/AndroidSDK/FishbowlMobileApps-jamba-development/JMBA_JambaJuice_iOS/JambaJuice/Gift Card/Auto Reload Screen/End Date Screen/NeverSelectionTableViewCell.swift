//
//  NeverSelectionTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 17/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit

class NeverSelectionTableViewCell: UITableViewCell {
    
    @IBOutlet weak var name         : UILabel!
    @IBOutlet weak var CheckImage   : UIImageView!
    
    func setData(showCheckImage:Bool) {
        CheckImage.hidden = !showCheckImage
    }
    
    override func layoutSubviews() {
        if (CheckImage.hidden) {
            makeDeActiveCell()
        }
    }
    
    func makeActiveCell() {
        self.alpha = GiftCardAppConstants.tableViewCellActiveState
    }
    
    func makeDeActiveCell() {
        CheckImage.hidden = true
        self.alpha = GiftCardAppConstants.tableViewCellDeactiveState
    }
    
}