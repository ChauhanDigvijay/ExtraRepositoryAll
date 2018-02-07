//
//  AddPaymentNavigationTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 19/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class AddPaymentNavigationTableViewCell: UITableViewCell {
    @IBOutlet weak var labelName    : UILabel!
    var cellData                    : FieldSetDataModel!
    
    func  setCellData(_ fieldData:FieldSetDataModel) {
        labelName.text = fieldData.name
        cellData = fieldData
    }
}
