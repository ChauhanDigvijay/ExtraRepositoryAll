//
//  AddPaymentTwoTextFieldTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 12/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

class AddPaymentTwoTextFieldTableViewCell: UITableViewCell {
    @IBOutlet weak var firstTextField   : UITextField!
    @IBOutlet weak var secondTextField  : UITextField!
    var cellData                        : FieldSetDataModel!
    
    @IBAction func alignment() {
        let text:NSString = NSString(string: secondTextField.text!)
        if (text.length > 0) {
            secondTextField.textAlignment = .Left
        } else {
            secondTextField.textAlignment = .Right
        }
    }
}

