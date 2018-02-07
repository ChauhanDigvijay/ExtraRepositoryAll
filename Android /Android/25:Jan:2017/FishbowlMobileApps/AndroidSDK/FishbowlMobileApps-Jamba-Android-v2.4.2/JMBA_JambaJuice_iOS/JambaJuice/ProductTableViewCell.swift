//
//  ProductTableViewCell.swift
//  JambaJuice
//
//  Created by VT010 on 10/17/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import AlamofireImage

class ProductTableViewCell: UITableViewCell {
    
    @IBOutlet weak var productImageView:UIImageView!
    @IBOutlet weak var productNameLabel:UILabel!
    @IBOutlet weak var productCategoryNameLabel:UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setData(_ productWithCategory:ProductWithCategory){
        productCategoryNameLabel.text = productWithCategory.category.name
        productNameLabel.text = productWithCategory.product.name
        if let url = URL(string: productWithCategory.product.thumbImageUrl) {
            productImageView.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.productImageView.image = image.value
            })
        }
    }

}
