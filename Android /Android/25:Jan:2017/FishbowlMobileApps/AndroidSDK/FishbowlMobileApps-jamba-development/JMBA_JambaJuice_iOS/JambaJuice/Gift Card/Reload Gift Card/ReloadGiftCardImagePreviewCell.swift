//
//  ReloadGiftCardImagePreviewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 05/09/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import SVProgressHUD
import Haneke

class ReloadGiftCardImagePreviewCell: UITableViewCell {
    @IBOutlet weak var giftCardImage : UIImageView!
    
    func setCellData(imageURL:String) {
        if let url = NSURL(string: imageURL){
            giftCardImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            giftCardImage.layer.masksToBounds = true
            giftCardImage.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, format: Format<UIImage>(name: "original"), failure: { (error) in
                }, success: { (image) in
                    self.giftCardImage.image = image
                    self.giftCardImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.giftCardImage.layer.masksToBounds = false
            })
        } else {
            self.giftCardImage.image = GiftCardAppConstants.jambaGiftCardDefaultImage
        }
    }
}