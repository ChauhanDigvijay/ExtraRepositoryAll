//
//  FieldSetDataModel.swift
//  JambaGiftCard
//
//  Created by vThink on 16/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class FieldSetDataModel {
    var section        : String = ""                //section name, in which the field
    var name           : String = ""                //field name
    var mandatory      : Bool   = true              //field mandatory or not
    var field_type     : String = ""                //field type will be enum eg- textfield,ddl
    var field_value    : String = ""                //field value
    var field_image    : String = ""                //field image url
    var ddl_value      : String = ""                //field drop dowl list selected value
    var keyboardType   : UIKeyboardType = .default  //field(textfield) keyboard type
}
