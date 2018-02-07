//
//  GiftCardTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 9/13/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK
import AlamofireImage


class GiftCardTableViewCell: UITableViewCell {
    @IBOutlet weak var cardNameLabel                    :   UILabel!
    @IBOutlet weak var cardNumberLabel                  :   UILabel!
    @IBOutlet weak var avlBalanceLabel                  :   UILabel!
    @IBOutlet weak var cardImageView                    :   UIImageView!
    @IBOutlet weak var selectionImageBackgroundView     :   UIView!
    @IBOutlet weak var selectionBackgroundShadowView    :   UIView!
    @IBOutlet weak var checkImageview: UIImageView!
    
    // MARK: - Set data
    func setData(_ giftCard:InCommUserGiftCard,selectedGiftCard:InCommUserGiftCard?,selection:Bool){
        if selection{
            if selectedGiftCard != nil{
               selectionImageBackgroundView.isHidden = false
                selectionBackgroundShadowView.isHidden = true
                checkImageview.isHidden = false
                self.backgroundColor = UIColor.clear
            }else{
               selectionImageBackgroundView.isHidden = false
                selectionBackgroundShadowView.isHidden = false
                checkImageview.isHidden = true
              //  self.backgroundColor = UIColor.lightGray
               self.backgroundColor = UIColor(red: 243, green: 243, blue: 243, alpha: 0.5)
               
            }
        }else{
            selectionImageBackgroundView.isHidden = true
            selectionBackgroundShadowView.isHidden = true
            self.backgroundColor = UIColor.clear
            }
        cardNameLabel.text = giftCard.cardName
        cardNumberLabel.text = giftCard.cardNumber
        avlBalanceLabel.text = String(format:"$%.2f",giftCard.balance)
        cardImageView.layer.cornerRadius = 8.0
        cardImageView.layer.masksToBounds = true
        if let url = URL(string: (giftCard.thumbnailImageUrl)){
            
            cardImageView.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.cardImageView.image = image.value
                self.cardImageView.layer.cornerRadius = 0.0
                self.cardImageView.layer.masksToBounds = false
            })
        }
    }
}
