//
//  PaymentImageTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 11/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//
import UIKit
import InCommSDK
import AlamofireImage

class PaymentImageTableViewCell: UITableViewCell {
    @IBOutlet weak var labelName    : UILabel!
    @IBOutlet weak var details      :UIImageView!
    
    func setCellData(_ field_name:String,imageURL:String) {
        labelName.text = field_name
        if let url = URL(string: (imageURL)){
            details.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            details.layer.masksToBounds = true
            details.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.details.image = image.value
                self.details.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.details.layer.masksToBounds = false
            })
        }
    }
}
