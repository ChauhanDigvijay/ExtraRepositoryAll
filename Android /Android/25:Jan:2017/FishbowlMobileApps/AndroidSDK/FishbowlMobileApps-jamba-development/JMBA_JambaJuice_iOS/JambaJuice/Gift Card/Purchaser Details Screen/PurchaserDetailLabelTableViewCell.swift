//
//  PurchaserDetailLabelTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 26/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class PurchaserDetailLabelTableViewCell: UITableViewCell {
    @IBOutlet weak var navigationButton : UIImageView!
    @IBOutlet weak var name             : UILabel!
    var clearText                       : Bool   = true
    var fieldName                       : String = ""
}