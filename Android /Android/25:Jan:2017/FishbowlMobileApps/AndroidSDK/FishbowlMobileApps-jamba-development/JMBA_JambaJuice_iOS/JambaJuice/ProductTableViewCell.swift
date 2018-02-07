//
//  ProductTableViewCell.swift
//  JambaJuice
//
//  Created by VT010 on 10/17/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import Haneke

class ProductTableViewCell: UITableViewCell {
    
    @IBOutlet weak var productImageView:UIImageView!
    @IBOutlet weak var productNameLabel:UILabel!
    @IBOutlet weak var productCategoryNameLabel:UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(productWithCategory:ProductWithCategory){
        productCategoryNameLabel.text = productWithCategory.category.name
        productNameLabel.text = productWithCategory.product.name
        if let url = NSURL(string: productWithCategory.product.thumbImageUrl) {
            productImageView.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
        }
    }

}
