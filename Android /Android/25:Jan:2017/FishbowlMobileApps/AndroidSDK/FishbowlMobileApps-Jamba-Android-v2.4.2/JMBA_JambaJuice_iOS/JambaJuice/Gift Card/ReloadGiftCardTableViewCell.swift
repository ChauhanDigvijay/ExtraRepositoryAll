//
//  ReloadGiftCardTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 05/09/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import AlamofireImage

class ReloadGiftCardTableViewCell: UITableViewCell {
    @IBOutlet weak var name                             : UILabel!
    @IBOutlet weak var value                            : UILabel!
    @IBOutlet weak var arrowImage                       : UIImageView!
    @IBOutlet weak var creditCardTypeImage              : UIImageView!
    @IBOutlet weak var fieldRightSideValueTrailingSpace : NSLayoutConstraint!
    
    func setCellData(_ field_name:String, field_value:String, imageUrl:String, showNavigationIcon:Bool) {
        name.text = field_name
        value.text = field_value
        arrowImage.isHidden = !showNavigationIcon
        
        //if the field not contains hide image view & reduce space
        if (imageUrl == "") {
            fieldRightSideValueTrailingSpace.constant = GiftCardAppConstants.tableViewValueWithNavigationIcon
            creditCardTypeImage.isHidden = true
        } else {
            fieldRightSideValueTrailingSpace.constant = GiftCardAppConstants.paymentValueWithCreditCardImage
            
            creditCardTypeImage.isHidden = true
            if let url = URL(string: imageUrl){
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
    
    func setCellBackgroundlightColor() {
        value.textColor = UIColor(red: 155/255, green: 155/255, blue:155/255, alpha:1.0)
    }
}
