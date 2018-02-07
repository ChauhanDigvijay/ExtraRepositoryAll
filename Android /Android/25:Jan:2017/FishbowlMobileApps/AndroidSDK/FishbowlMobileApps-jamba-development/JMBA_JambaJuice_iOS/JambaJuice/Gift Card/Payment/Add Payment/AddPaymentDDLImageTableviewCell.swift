//
//  AddPaymentDDLImageTableviewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 12/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

class AddPaymentDDLImageTableviewCell: UITableViewCell {
    @IBOutlet weak var labelName    : UILabel!
    @IBOutlet weak var imageName    : UIImageView!
    var cellData                    : FieldSetDataModel!
    
    func setCellData(fieldData:FieldSetDataModel) {
        labelName.text = fieldData.name
        if (fieldData.field_image.characters.count > 0) {
            imageName.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            imageName.layer.masksToBounds = true
            if let url  = NSURL(string:fieldData.field_image){
                imageName.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success:{ (image) in
                    self.imageName.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.imageName.layer.masksToBounds = false
                    self.imageName.image = image
                })
            }
        } else {
            imageName.hidden = true
        }
        cellData = fieldData
    }
    
    func setDefaultBackgroundColor() {
        imageName.backgroundColor = GiftCardAppConstants.jambaCreditCardDefaultBackgroundColor
        imageName.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
        imageName.layer.masksToBounds = true
    }
    
    func removeDefaultBackgroundColor() {
        imageName.backgroundColor = UIColor.clearColor()
        imageName.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
        imageName.layer.masksToBounds = false
    }
}