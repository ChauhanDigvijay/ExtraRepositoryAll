//
//  ProductCollectionViewCell.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/18/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Haneke

class ProductCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var productImageView: UIImageView!
    var productIndex: Int = -1
    
    func update(product: Product) {
        nameLabel.text = product.name
        if let url = NSURL(string: product.thumbImageUrl) {
            productImageView.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
        }
    }
    
}
