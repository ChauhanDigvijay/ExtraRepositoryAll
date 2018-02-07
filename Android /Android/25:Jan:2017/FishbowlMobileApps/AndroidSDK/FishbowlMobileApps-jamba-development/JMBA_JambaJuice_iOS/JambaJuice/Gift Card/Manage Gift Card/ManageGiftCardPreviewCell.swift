//
//  ManageGiftCardPreviewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 25/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

class ManageGiftCardPreviewCell: UITableViewCell {
    
    @IBOutlet weak var giftCardImage : UIImageView!
    
    //set image on cell
    func setImage(userGiftCard:InCommUserGiftCard) {
        if let url = NSURL(string: (userGiftCard.imageUrl)){
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
