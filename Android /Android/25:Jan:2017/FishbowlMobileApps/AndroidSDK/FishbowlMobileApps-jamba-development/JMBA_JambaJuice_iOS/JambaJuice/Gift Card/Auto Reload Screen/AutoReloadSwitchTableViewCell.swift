//
//  AutoReloadSwitchTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 13/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK

class AutoReloadSwitchTableViewCell: UITableViewCell {
    @IBOutlet weak var labelName                    : UILabel!
    @IBOutlet weak var switchButtonForAutoReload    : UISwitch!
    var cellData                                    : AutoReloadSettingsDataModel!
    var incommUserCard                              : InCommUserGiftCard?
    
    func setCellData(fieldData:AutoReloadSettingsDataModel,incommCard:InCommUserGiftCard) {
        cellData = fieldData
        incommUserCard = incommCard
        switchButtonForAutoReload.setOn(fieldData.switchState, animated: false)
        self.backgroundColor = UIColor(red: 74/255, green: 74/255, blue: 74/255, alpha: 1.0)
    }
}