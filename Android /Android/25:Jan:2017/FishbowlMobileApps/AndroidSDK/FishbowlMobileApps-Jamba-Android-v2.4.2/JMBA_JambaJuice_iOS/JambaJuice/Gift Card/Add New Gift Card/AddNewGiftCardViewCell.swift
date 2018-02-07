//
//  AddNewGiftCardViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 22/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class AddNewGiftCardViewCell: UITableViewCell {
    @IBOutlet weak var name                             : UILabel!
    @IBOutlet weak var value                            : UILabel!
    @IBOutlet weak var arrowImage                       : UIImageView!
    @IBOutlet weak var fieldRightSideValueTrailingSpace : NSLayoutConstraint!
    @IBOutlet weak var arrowImageWidth                  : NSLayoutConstraint!
    
    func setCellData(_ field_name:String, field_value:String, navigationImage:UIImage)  {
        name.text = field_name
        value.text = field_value
        arrowImage.image = navigationImage
    }
    
    func setCellBackgroundlightColor() {
        arrowImage.isHidden = true
        name.textColor = UIColor(red: 155/255, green: 155/255, blue:155/255, alpha:1.0)
        value.textColor = UIColor(red: 155/255, green: 155/255, blue:155/255, alpha:1.0)
    }
}
