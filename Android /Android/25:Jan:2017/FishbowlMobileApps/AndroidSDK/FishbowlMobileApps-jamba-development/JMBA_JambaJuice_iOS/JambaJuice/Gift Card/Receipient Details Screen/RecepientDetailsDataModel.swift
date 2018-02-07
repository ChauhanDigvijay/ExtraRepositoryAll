//
//  RecepientDetailsDataModel.swift
//  JambaGiftCard
//
//  Created by vThink on 27/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class RecepientDetailsDataModel {
    var section         :String         = ""        //Section name - in which the field is there
    var name            :String         = ""        //field name
    var field_value     :String         = ""        //corresponding field value
    var field_image     :Bool           = false     //check image - need to show or not (hidden)
    var keyboardType    :UIKeyboardType = .Default
}