//
//  AddPaymentDDLImageTableviewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 12/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import AlamofireImage

class AddPaymentDDLImageTableviewCell: UITableViewCell {
    @IBOutlet weak var labelName    : UILabel!
    @IBOutlet weak var imageName    : UIImageView!
    var cellData                    : FieldSetDataModel!
    
    func setCellData(_ fieldData:FieldSetDataModel) {
        labelName.text = fieldData.name
        if (fieldData.field_image.characters.count > 0) {
            imageName.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            imageName.layer.masksToBounds = true
            if let url  = URL(string:fieldData.field_image){
                imageName.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                    self.imageName.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.imageName.layer.masksToBounds = false
                    self.imageName.image = image.value
                })
            }
        } else {
            imageName.isHidden = true
        }
        cellData = fieldData
    }
    
    func setDefaultBackgroundColor() {
        imageName.backgroundColor = GiftCardAppConstants.jambaCreditCardDefaultBackgroundColor
        imageName.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
        imageName.layer.masksToBounds = true
    }
    
    func removeDefaultBackgroundColor() {
        imageName.backgroundColor = UIColor.clear
        imageName.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
        imageName.layer.masksToBounds = false
    }
    func setCreditCardImage(_ value:String) {
        // For default value type as VISA
        var creditCardNumberValue = value
        if creditCardNumberValue.length>0 {
            let charIndex = value.characters.index(value.startIndex, offsetBy: 0)
            creditCardNumberValue = String(value[charIndex])
        }
    InCommGiftCardBrandDetails.sharedInstance.getCreditCardTypeImageNameOrImageURL(creditCardNumberValue, callback: { (imageURL, imageName) in
                if imageURL != nil && !imageURL!.isEmpty{
                    if let url = URL(string: imageURL!){
                        self.imageName.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
                        self.imageName.layer.masksToBounds = true
                        
                        self.imageName.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: DispatchQueue.main, imageTransition: UIImageView.ImageTransition.noTransition, runImageTransitionIfCached: false, completion: { (response) in
                            if let image = response.result.value{
                                self.imageName.image = image
                                self.imageName.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                                self.imageName.layer.masksToBounds = false
                            }
                        })
                    }
                }else{
                    self.imageName.image = UIImage(named:imageName!)
                }
            })
            imageName.isHidden = false
    }
}

