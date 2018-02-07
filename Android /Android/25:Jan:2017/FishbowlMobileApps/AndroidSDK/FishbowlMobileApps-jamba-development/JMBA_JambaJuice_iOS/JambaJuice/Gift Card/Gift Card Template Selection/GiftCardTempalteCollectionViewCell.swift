//
//  GiftCardTempalteCollectionViewCell.swift
//  JambaGiftCards
//
//  Created by vThink on 5/23/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

class GiftCardTempalteCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var cardTemplateImageView              : UIImageView!
    @IBOutlet weak var selectionImageView                 : UIImageView!
    @IBOutlet weak var selectionView                      : UIView!
    @IBOutlet weak var selectionImageViewHeightConstraint : NSLayoutConstraint!
    @IBOutlet weak var selectionImageViewWidthConstraint  : NSLayoutConstraint!
    internal  var incommBrandCardImage                    : InCommBrandCardImage?
    
    func updateSelection(){
        self.selectionImageView.hidden = false
        self.selectionView.hidden = false
        self.selectionView.backgroundColor = UIColor(red: 255/255,green:255/255,blue:255/255, alpha:0.5)
    }
    
    func updateSelectionImageView(cell:GiftCardTempalteCollectionViewCell){
        if let url = NSURL(string: cell.incommBrandCardImage!.imageUrl){
            cardTemplateImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            cardTemplateImageView.layer.masksToBounds = true
            cardTemplateImageView.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success: { (image) in
                self.cardTemplateImageView.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.cardTemplateImageView.layer.masksToBounds = false
                self.cardTemplateImageView.image = image
            })
        }
        
        let selectionImageViewWidthHeight = cell.frame.size.height/3
        cell.selectionImageView.frame.size = CGSizeMake(selectionImageViewWidthHeight, selectionImageViewWidthHeight)
        cell.selectionImageView.hidden = true
        cell.selectionView.hidden = true
    }
}
