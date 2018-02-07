//
//  FavoriteItemsOrderedTableViewCell.swift
//  JambaJuice
//
//  Created by VT02 on 10/6/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit

class FavoriteItemsOrderedTableViewCell: UITableViewCell {

    @IBOutlet  weak var productNameLabel:UILabel!
    @IBOutlet weak var modifiersLabel: UILabel!
    @IBOutlet weak var sizeLabel:UILabel!
    @IBOutlet weak var priceLabel:UILabel!
    @IBOutlet weak var seeMore: UIButton!
    @IBOutlet weak var modifiersLabellHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var seeMoreBottomConstraintConstant:NSLayoutConstraint!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
}
