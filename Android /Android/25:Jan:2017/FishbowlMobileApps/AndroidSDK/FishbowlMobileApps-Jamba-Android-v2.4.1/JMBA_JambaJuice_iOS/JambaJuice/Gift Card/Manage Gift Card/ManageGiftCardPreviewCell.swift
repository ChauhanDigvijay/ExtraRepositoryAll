//
//  ManageGiftCardPreviewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 25/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import AlamofireImage

class ManageGiftCardPreviewCell: UITableViewCell {
    
    @IBOutlet weak var giftCardImage : UIImageView!
    
    //set image on cell
    func setImage(_ userGiftCard:InCommUserGiftCard) {
        if let url = URL(string: (userGiftCard.imageUrl)){
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
