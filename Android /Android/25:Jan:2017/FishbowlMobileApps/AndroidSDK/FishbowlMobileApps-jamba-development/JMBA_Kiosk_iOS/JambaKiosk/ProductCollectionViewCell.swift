//
//  ProductCollectionViewCell.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/10/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Haneke

class ProductCollectionViewCell: UICollectionViewCell {

    @IBOutlet var productImageView: UIImageView!
    @IBOutlet var productNameLabel: UILabel!

    var product: Product?

    func update(product: Product) {
        self.product = product
        productNameLabel.text = product.name
        if let url = NSURL(string: product.imageUrl) {
            productImageView.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
        }
    }

}
