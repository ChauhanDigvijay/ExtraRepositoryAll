//
//  ReloadGiftCardImagePreviewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 05/09/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import SVProgressHUD
import AlamofireImage

class ReloadGiftCardImagePreviewCell: UITableViewCell {
    @IBOutlet weak var giftCardImage : UIImageView!
    
    func setCellData(_ imageURL:String) {
        if let url = URL(string: imageURL){
            giftCardImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            giftCardImage.layer.masksToBounds = true
            giftCardImage.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.giftCardImage.image = image.value
                self.giftCardImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.giftCardImage.layer.masksToBounds = false
            })
        } else {
            self.giftCardImage.image = GiftCardAppConstants.jambaGiftCardDefaultImage
        }
    }
}
