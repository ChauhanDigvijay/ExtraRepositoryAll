//
//  GiftCardTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 9/13/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

class GiftCardTableViewCell: UITableViewCell {
    @IBOutlet weak var cardNameLabel                    :   UILabel!
    @IBOutlet weak var cardNumberLabel                  :   UILabel!
    @IBOutlet weak var avlBalanceLabel                  :   UILabel!
    @IBOutlet weak var cardImageView                    :   UIImageView!
    @IBOutlet weak var selectionImageBackgroundView     :   UIView!
    @IBOutlet weak var selectionBackgroundShadowView    :   UIView!
    
    // MARK: - Set data
    func setData(giftCard:InCommUserGiftCard,selectedGiftCard:InCommUserGiftCard?){
        if selectedGiftCard != nil{
          selectionImageBackgroundView.hidden = false
          selectionBackgroundShadowView.hidden = false
        }
        else{
            selectionImageBackgroundView.hidden = true
            selectionBackgroundShadowView.hidden = true
        }
        cardNameLabel.text = giftCard.cardName
        cardNumberLabel.text = giftCard.cardNumber
        avlBalanceLabel.text = String(format:"$%.2f",giftCard.balance)
        cardImageView.layer.cornerRadius = 8.0
        cardImageView.layer.masksToBounds = true
        if let url = NSURL(string: (giftCard.thumbnailImageUrl)){
            cardImageView.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success:{(image) in
                self.cardImageView.image = image
                self.cardImageView.layer.cornerRadius = 0.0
                self.cardImageView.layer.masksToBounds = false
            })
        }
    }
}
