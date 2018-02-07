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
    
    func setData(_ showCheckImage:Bool) {
        CheckImage.isHidden = !showCheckImage
    }
    
    override func layoutSubviews() {
        if (CheckImage.isHidden) {
            makeDeActiveCell()
        }
    }
    
    func makeActiveCell() {
        self.alpha = GiftCardAppConstants.tableViewCellActiveState
    }
    
    func makeDeActiveCell() {
        CheckImage.isHidden = true
        self.alpha = GiftCardAppConstants.tableViewCellDeactiveState
    }
    
}
