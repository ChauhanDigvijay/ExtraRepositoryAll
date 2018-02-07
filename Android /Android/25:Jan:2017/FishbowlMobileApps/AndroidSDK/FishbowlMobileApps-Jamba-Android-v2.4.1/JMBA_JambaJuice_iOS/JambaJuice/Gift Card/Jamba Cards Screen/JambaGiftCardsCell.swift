//
//  JambaGiftCardsCell.swift
//  Gift Card
//
//  Created by vThink on 23/05/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import AlamofireImage
import InCommSDK

protocol JambaGiftCardsCellDelegate: class {
    func cardDetailsButtonPressed(_ selectdCardIndex:Int!)
}

class JambaGiftCardsCell: UICollectionViewCell {
    
    @IBOutlet weak var jambaGiftCardImageView   : UIImageView!
    weak var delegate                           : JambaGiftCardsCellDelegate?
    var index                                   : Int!
    
    
    @IBAction func cardDetaisButtonPressed(_ sender:UIButton){
        delegate?.cardDetailsButtonPressed(self.index)
    }
    
    func applyCardImageAndIndex(_ url:String!,index:Int){
        self.index = index
        if let url  = URL(string:url){
            jambaGiftCardImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            jambaGiftCardImageView.layer.masksToBounds = true
            
            jambaGiftCardImageView.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.jambaGiftCardImageView.image = image.value
                self.jambaGiftCardImageView.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.jambaGiftCardImageView.layer.masksToBounds = false
            })
        }
    }
}
