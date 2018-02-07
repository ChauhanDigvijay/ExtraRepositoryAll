//
//  GiftCardImageTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 8/25/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import Haneke

class GiftCardImageTableViewCell: UITableViewCell {
    
    @IBOutlet weak var giftCardImageView : UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    // MARK: Set gift card image
    func setGiftCardImage(imageUrl: String!){
        if let url = NSURL(string: imageUrl){
            giftCardImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            giftCardImageView.layer.masksToBounds = true
            giftCardImageView.hnk_setImageFromURL(url, placeholder: nil, success: { (image) -> Void in
                self.giftCardImageView.image = image
                self.giftCardImageView.layer.masksToBounds = false
                self.giftCardImageView.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                }, failure: { (error) -> Void in
            })
        }
    }
    
}
