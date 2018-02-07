//
//  AutoReloadTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 13/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK

class AutoReloadTableViewCell: UITableViewCell {
    @IBOutlet weak var labelName                : UILabel!
    @IBOutlet weak var fieldValue               : UILabel!
    @IBOutlet weak var creditCardType           : UIImageView!
    @IBOutlet weak var navigationImage          : UIImageView!
    @IBOutlet weak var fieldValueTrailingSpace  : NSLayoutConstraint!
    var cellData                                : AutoReloadSettingsDataModel!
    var incommUserCard                          : InCommUserGiftCard?
    
    func setCellData(fieldData:AutoReloadSettingsDataModel, showCreditCardType:Bool, creditCardTypeImageUrl:String, navigationImage:UIImage, switchState:Bool, incommCard:InCommUserGiftCard) {
        cellData = fieldData
        incommUserCard = incommCard
        cellData.switchState = switchState
        labelName.text = fieldData.name
        fieldValue.text = fieldData.field_value
        self.navigationImage.image = navigationImage
        self.navigationImage.hidden = false
        
        //check whether to show payment information or not
        if showCreditCardType {
            fieldValueTrailingSpace.constant = 95
            creditCardType.hidden = false
            creditCardType.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            creditCardType.layer.masksToBounds = true
            if let url  = NSURL(string:fieldData.field_image){
                creditCardType.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success:{(image) in
                    self.creditCardType.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.creditCardType.layer.masksToBounds = false
                    self.creditCardType.image = image
                })
            }
        } else {
            fieldValueTrailingSpace.constant = 45
            creditCardType.hidden = true
        }
        
    }
    
    override func layoutSubviews() {
        //change the apperance of the cell based on switch state
        if !cellData.switchState  {
            makeDeActiveCell()
        } else {
            makeActiveCell()
        }
    }
    
    func makeActiveCell() {
        self.alpha = GiftCardAppConstants.tableViewCellActiveState
    }
    
    func makeDeActiveCell() {
        self.alpha = GiftCardAppConstants.tableViewCellDeactiveState
    }
}