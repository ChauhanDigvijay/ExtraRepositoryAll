//
//  AmountCollectionViewCell.swift
//  GiftCardMissingScreens
//
//  Created by vThink on 5/31/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit


class AmountCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var amountLabel  :UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.amountLabel.layer.borderWidth = 1.0
        self.amountLabel.layer.borderColor = UIColor(red: 215/255, green: 215/255, blue:215/255, alpha:1.0).CGColor
        self.amountLabel.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
        self.amountLabel.layer.masksToBounds = true
    }
    
    func updateSelection(){
        
    }
    
}
