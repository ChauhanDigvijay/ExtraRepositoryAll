//
//  BoostTableViewCell.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class BoostTableViewCell: UITableViewCell {

    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var selectedImageView: UIImageView!

    //For ease of reference
    var modifierIndex: Int!
    var optionIndex: Int!

}
