//
//  RecepientDetailTextFieldViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 27/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class RecepientDetailTextFieldViewCell: UITableViewCell {
    @IBOutlet weak var textfield : UITextField!
    
    func setCellData(placeholderText:String, textFieldValue:String, textFieldEnabled:Bool, textFieldKeyboardType: UIKeyboardType) {
        textfield.placeholder = placeholderText
        textfield.text = textFieldValue
        textfield.enabled = textFieldEnabled
        textfield.keyboardType = textFieldKeyboardType
    }
}