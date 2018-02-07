//
//  AddPaymentSingleDDLTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 12/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

class AddPaymentSingleDDLTableViewCell: UITableViewCell {
    @IBOutlet weak var labelName    : UITextField!
    var cellData                    : FieldSetDataModel!
    
    func setCellData(fieldData:FieldSetDataModel) {
        labelName.text = fieldData.field_value
        cellData = fieldData
        labelName.enabled = false
    }
}