//
//  BonusTableViewCell.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 13/12/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class BonusTableViewCell: UITableViewCell {
    @IBOutlet weak var lblShowDesc: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func configureCellWithData(dict : [String: AnyObject],index : Int) -> Void
    {
        lblShowDesc.text = dict["description"] as? String
        
    }
}
