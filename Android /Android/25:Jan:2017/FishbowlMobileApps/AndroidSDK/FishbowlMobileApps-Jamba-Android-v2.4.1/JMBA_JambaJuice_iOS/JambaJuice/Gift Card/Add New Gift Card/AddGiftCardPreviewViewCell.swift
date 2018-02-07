//
//  AddGiftCardPreviewViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 23/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import SVProgressHUD
import AlamofireImage
class AddGiftCardPreviewViewCell: UITableViewCell {
    @IBOutlet weak var giftCardImage            : UIImageView!
    @IBOutlet weak var giftCardtextLabel        : UILabel!
    @IBOutlet weak var giftCardchangeDesignLabel: UILabel!
    
    func setCellData(_ imageHidden:Bool, imageURL:String) {
        giftCardchangeDesignLabel.isHidden = imageHidden
        giftCardtextLabel.isHidden = !imageHidden
        if !imageHidden {
            if let url = URL(string: imageURL){
                giftCardImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
                giftCardImage.layer.masksToBounds = true
                
                giftCardImage.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                    self.giftCardImage.image = image.value
                    self.giftCardImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.giftCardImage.layer.masksToBounds = false
                })
            }
        }
    }
}
