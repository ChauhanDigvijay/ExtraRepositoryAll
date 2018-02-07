//
//  JambaGiftCardsCell.swift
//  Gift Card
//
//  Created by vThink on 23/05/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import Haneke
import InCommSDK

protocol JambaGiftCardsCellDelegate: class {
    func cardDetailsButtonPressed(selectdCardIndex:Int!)
}

class JambaGiftCardsCell: UICollectionViewCell {
    
    @IBOutlet weak var jambaGiftCardImageView   : UIImageView!
    weak var delegate                           : JambaGiftCardsCellDelegate?
    var index                                   : Int!
    
    
    @IBAction func cardDetaisButtonPressed(sender:UIButton){
        delegate?.cardDetailsButtonPressed(self.index)
    }
    
    func applyCardImageAndIndex(url:String!,index:Int){
        self.index = index
        if let url  = NSURL(string:url){
            jambaGiftCardImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            jambaGiftCardImageView.layer.masksToBounds = true
            jambaGiftCardImageView.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage,success: { (image) in
                self.jambaGiftCardImageView.image = image
                self.jambaGiftCardImageView.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.jambaGiftCardImageView.layer.masksToBounds = false
            })
        }
    }
}