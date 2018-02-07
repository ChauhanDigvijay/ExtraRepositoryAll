//
//  AddGiftCardPreviewViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 23/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import SVProgressHUD

class AddGiftCardPreviewViewCell: UITableViewCell {
    @IBOutlet weak var giftCardImage            : UIImageView!
    @IBOutlet weak var giftCardtextLabel        : UILabel!
    @IBOutlet weak var giftCardchangeDesignLabel: UILabel!
    
    func setCellData(imageHidden:Bool, imageURL:String) {
        giftCardchangeDesignLabel.hidden = imageHidden
        giftCardtextLabel.hidden = !imageHidden
        if !imageHidden {
            if let url = NSURL(string: imageURL){
                giftCardImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
                giftCardImage.layer.masksToBounds = true
                giftCardImage.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success: { (image) in
                    self.giftCardImage.image = image
                    self.giftCardImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.giftCardImage.layer.masksToBounds = false
                })
            }
        }
    }
}