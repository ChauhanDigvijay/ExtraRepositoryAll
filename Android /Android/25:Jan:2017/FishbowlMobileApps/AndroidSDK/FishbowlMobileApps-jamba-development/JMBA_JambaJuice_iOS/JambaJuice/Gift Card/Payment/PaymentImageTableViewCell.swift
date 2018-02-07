//
//  PaymentImageTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 11/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//
import UIKit
import InCommSDK
import Haneke

class PaymentImageTableViewCell: UITableViewCell {
    @IBOutlet weak var labelName    : UILabel!
    @IBOutlet weak var details      :UIImageView!
    
    func setCellData(field_name:String,imageURL:String) {
        labelName.text = field_name
        if let url = NSURL(string: (imageURL)){
            details.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            details.layer.masksToBounds = true
            details.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success:{(image) in
                self.details.image = image
                self.details.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.details.layer.masksToBounds = false
            })
        }
    }
}