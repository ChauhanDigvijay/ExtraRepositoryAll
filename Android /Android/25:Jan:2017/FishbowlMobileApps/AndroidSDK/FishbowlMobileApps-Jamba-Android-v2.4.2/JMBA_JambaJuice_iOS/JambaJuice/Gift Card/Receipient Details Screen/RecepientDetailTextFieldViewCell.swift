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
    
    func setCellData(_ placeholderText:String, textFieldValue:String, textFieldEnabled:Bool, textFieldKeyboardType: UIKeyboardType) {
        textfield.placeholder = placeholderText
        textfield.text = textFieldValue
        textfield.isEnabled = textFieldEnabled
        textfield.keyboardType = textFieldKeyboardType
    }
}
