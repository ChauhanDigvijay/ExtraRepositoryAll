//
//  ManageGiftCardTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 09/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK

class ManageGiftCardTableViewCell: UITableViewCell {
    
    @IBOutlet weak var name                     : UILabel!
    @IBOutlet weak var selectButton             : UIButton!
    @IBOutlet weak var creditCardType           : UIImageView!
    @IBOutlet weak var details                  : UILabel!
    @IBOutlet weak var detailsHorizontalSpace   : NSLayoutConstraint!
    
    func setCellData(_ field_name:String,field_value:String,detailsHorizontalSpace:CGFloat) {
        name.text = field_name
        details.text = field_value
        self.detailsHorizontalSpace.constant = detailsHorizontalSpace;
        creditCardType.isHidden = true
    }
}
