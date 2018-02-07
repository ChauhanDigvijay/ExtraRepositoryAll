//
//  RecepientDetailCheckTypeViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 27/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class RecepientDetailCheckTypeViewCell: UITableViewCell {
    @IBOutlet weak var labelName : UILabel!
    @IBOutlet weak var imageName : UIImageView!
    
    func setCellData(_ labelName:String,showImage:Bool) {
        self.labelName.text = labelName
        self.imageName.isHidden = showImage
    }
}
