//
//  ProductCollectionViewCell.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/18/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import AlamofireImage

class ProductCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var productImageView: UIImageView!
    var productIndex: Int = -1
    
    func update(_ product: Product) {
        nameLabel.text = product.name
        if let url = URL(string: product.thumbImageUrl) {
            productImageView.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.productImageView.image = image.value
            })
        }
    }
    
}
