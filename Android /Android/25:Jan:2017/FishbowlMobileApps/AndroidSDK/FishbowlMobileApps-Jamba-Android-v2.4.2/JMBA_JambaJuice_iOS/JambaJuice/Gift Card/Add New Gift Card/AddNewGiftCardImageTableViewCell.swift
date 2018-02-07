//
//  AddNewGiftCardImageTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 26/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import AlamofireImage

class AddNewGiftCardImageTableViewCell: UITableViewCell {
    @IBOutlet weak var name                             : UILabel!
    @IBOutlet weak var value                            : UILabel!
    @IBOutlet weak var arrowImage                       : UIImageView!
    @IBOutlet weak var creditCardTypeImage              : UIImageView!
    @IBOutlet weak var fieldRightSideValueTrailingSpace : NSLayoutConstraint!
    
    func setCellData(_ field_name:String, field_value:String, imageHidden:Bool, imageURL:String) {
        name.text = field_name
        value.text = field_value
        creditCardTypeImage.isHidden = imageHidden
        if !imageHidden {
            if let url = URL(string: imageURL){
                creditCardTypeImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
                creditCardTypeImage.layer.masksToBounds = true
                
                creditCardTypeImage.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                    self.creditCardTypeImage.image = image.value
                    self.creditCardTypeImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.creditCardTypeImage.layer.masksToBounds = false
                })
                
            }
        }
    }
}
