//
//  UpSellProductImagesCollectionViewCell.swift
//  JambaJuice
//
//  Created by VT010 on 10/25/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit

class UpSellProductImagesCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var productImageView:UIImageView!
    
    
    func update(_ url: String) {
        if let url = URL(string: url) {
            productImageView.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.productImageView.image = image.value
            })
        }
    }
}
