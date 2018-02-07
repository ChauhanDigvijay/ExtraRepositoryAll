//
//  GiftCardTempalteCollectionViewCell.swift
//  JambaGiftCards
//
//  Created by vThink on 5/23/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import AlamofireImage

class GiftCardTempalteCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var cardTemplateImageView              : UIImageView!
    @IBOutlet weak var selectionImageView                 : UIImageView!
    @IBOutlet weak var selectionView                      : UIView!
    @IBOutlet weak var selectionImageViewHeightConstraint : NSLayoutConstraint!
    @IBOutlet weak var selectionImageViewWidthConstraint  : NSLayoutConstraint!
    internal  var incommBrandCardImage                    : InCommBrandCardImage?
    
    func updateSelection(){
        self.selectionImageView.isHidden = false
        self.selectionView.isHidden = false
        self.selectionView.backgroundColor = UIColor(red: 255/255,green:255/255,blue:255/255, alpha:0.5)
    }
    
    func updateSelectionImageView(_ cell:GiftCardTempalteCollectionViewCell){
        if let url = URL(string: cell.incommBrandCardImage!.imageUrl){
            cardTemplateImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            cardTemplateImageView.layer.masksToBounds = true
            
            cardTemplateImageView.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.cardTemplateImageView.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.cardTemplateImageView.layer.masksToBounds = false
                self.cardTemplateImageView.image = image.value
            })
        }
        
        let selectionImageViewWidthHeight = cell.frame.size.height/3
        cell.selectionImageView.frame.size = CGSize(width: selectionImageViewWidthHeight, height: selectionImageViewWidthHeight)
        cell.selectionImageView.isHidden = true
        cell.selectionView.isHidden = true
    }
}
