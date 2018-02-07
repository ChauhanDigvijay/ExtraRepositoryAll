//
//  GiftCardDetailTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 8/22/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class GiftCardDetailTableViewCell: UITableViewCell {
    @IBOutlet weak var cellTitle : UILabel!
    @IBOutlet weak var cellValue : UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
    // MARK: - Set data
    func setData(cellTitle:String, cellValue:String){
        self.cellTitle.text = cellTitle
        self.cellValue.text = cellValue
    }
    
}
