//
//  GiftCardImageTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 8/25/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit


class GiftCardImageTableViewCell: UITableViewCell {
    
    @IBOutlet weak var giftCardImageView : UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    // MARK: Set gift card image
    func setGiftCardImage(_ imageUrl: String!){
        if let url = URL(string: imageUrl){
            giftCardImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            giftCardImageView.layer.masksToBounds = true
            
            giftCardImageView.af_setImage(withURL: url, placeholderImage: nil, filter: nil, progress: nil, progressQueue: DispatchQueue.main, imageTransition: UIImageView.ImageTransition.noTransition, runImageTransitionIfCached: false, completion: { (response) in
                if let image = response.result.value{
                    self.giftCardImageView.image = image
                    self.giftCardImageView.layer.masksToBounds = false
                    self.giftCardImageView.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                }
            })
        }
    }
    
}
