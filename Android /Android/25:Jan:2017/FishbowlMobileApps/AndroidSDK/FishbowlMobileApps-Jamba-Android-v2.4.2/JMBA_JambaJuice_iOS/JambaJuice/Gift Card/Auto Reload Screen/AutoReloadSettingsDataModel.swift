//
//  AutoReloadSettingsDataModel.swift
//  JambaJuice
//
//  Created by vThink on 13/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

class AutoReloadSettingsDataModel {
    var name                    : String  = ""                                                 //field name
    var field_value             : String  = ""                                                 //field value
    var switchState             : Bool    = false                                              //switc state
    var field_image            : String  = ""                                                 //field image url
    var checkBoxSelected        : Bool    = false                                              //maintain checkbox Selection
    var navgationImage          : UIImage = GiftCardAppConstants.backButtonImageWhenPresented  //navigation button for each cell
}