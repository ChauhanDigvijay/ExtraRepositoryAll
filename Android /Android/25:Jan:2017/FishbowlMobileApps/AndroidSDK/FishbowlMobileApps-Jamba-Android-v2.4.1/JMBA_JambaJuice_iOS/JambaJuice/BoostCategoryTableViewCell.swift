//
//  BoostCategoryTableViewCell.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class BoostCategoryTableViewCell: UITableViewCell {

    @IBOutlet weak var categoryLabel: UILabel!
    @IBOutlet weak var expandedStateImageView: UIImageView!
    var modifier: StoreMenuProductModifier!
    var parentModifier: StoreMenuProductModifier?

    //For ease of reference
    var modifierIndex: Int!
}
