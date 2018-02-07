//
//   Configure the view for the selected state     } FavoriteStoreDetailTableviewCell.swift
//  JambaJuice
//
//  Created by VT02 on 10/6/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit

class FavoriteStoreDetailTableViewCell: UITableViewCell {
    @IBOutlet weak var storeNameAndAddress: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }

}
