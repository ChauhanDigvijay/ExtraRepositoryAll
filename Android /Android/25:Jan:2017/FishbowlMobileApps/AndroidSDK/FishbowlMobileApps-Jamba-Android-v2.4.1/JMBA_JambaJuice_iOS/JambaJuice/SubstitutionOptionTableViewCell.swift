//
//  SubstitutionOptionTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 4/11/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation

class SubstitutionOptionTableViewCell: UITableViewCell  {
    
    @IBOutlet weak var optionName:UILabel!
    @IBOutlet weak var checkImageView:UIImageView!

    var optionId: Int64!
    var indexPath: IndexPath?
    
}
