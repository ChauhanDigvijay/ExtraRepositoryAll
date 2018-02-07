//
//  ReloadGiftCardTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 05/09/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class ReloadGiftCardTableViewCell: UITableViewCell {
    @IBOutlet weak var name                             : UILabel!
    @IBOutlet weak var value                            : UILabel!
    @IBOutlet weak var arrowImage                       : UIImageView!
    @IBOutlet weak var creditCardTypeImage              : UIImageView!
    @IBOutlet weak var fieldRightSideValueTrailingSpace : NSLayoutConstraint!
    
    func setCellData(field_name:String, field_value:String, imageUrl:String, showNavigationIcon:Bool) {
        name.text = field_name
        value.text = field_value
        arrowImage.hidden = !showNavigationIcon
        
        //if the field not contains hide image view & reduce space
        if (imageUrl == "") {
            fieldRightSideValueTrailingSpace.constant = GiftCardAppConstants.tableViewValueWithNavigationIcon
            creditCardTypeImage.hidden = true
        } else {
            fieldRightSideValueTrailingSpace.constant = GiftCardAppConstants.paymentValueWithCreditCardImage
            
            creditCardTypeImage.hidden = true
            if let url = NSURL(string: imageUrl){
                creditCardTypeImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
                creditCardTypeImage.layer.masksToBounds = true
                creditCardTypeImage.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success: { (image) -> Void in
                    self.creditCardTypeImage.image = image
                    self.creditCardTypeImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.creditCardTypeImage.layer.masksToBounds = false
                    }, failure: { (error) -> Void in
                })
            }
        }
    }
    
    func setCellBackgroundlightColor() {
        value.textColor = UIColor(red: 155/255, green: 155/255, blue:155/255, alpha:1.0)
    }
}