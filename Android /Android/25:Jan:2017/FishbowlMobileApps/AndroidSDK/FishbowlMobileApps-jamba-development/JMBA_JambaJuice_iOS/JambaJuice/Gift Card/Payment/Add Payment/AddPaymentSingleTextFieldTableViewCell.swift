//
//  AddPaymentSingleTextFieldTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 12/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

class AddPaymentSingleTextFieldTableViewCell: UITableViewCell {
    @IBOutlet weak var firstTextField   : UITextField!
    var cellData                        : FieldSetDataModel!
    
    func setCellData(fieldData:FieldSetDataModel) {
        firstTextField.placeholder = fieldData.name
        firstTextField.text = fieldData.field_value
        firstTextField.enabled = false
        firstTextField.keyboardType = fieldData.keyboardType
        cellData = fieldData
    }
}
