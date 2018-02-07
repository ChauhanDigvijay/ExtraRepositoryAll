//
//  AddPaymentCheckTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 19/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

class AddPaymentCheckTableViewCell: UITableViewCell {
    @IBOutlet weak var labelName                    : UILabel!
    @IBOutlet weak var switchButtonForSavePayment   : UISwitch!
    var cellData:FieldSetDataModel!
    
    func setCellData(_ fieldData:FieldSetDataModel,enableSwitch:Bool,state:Bool) {
        cellData = fieldData
        switchButtonForSavePayment.setOn(state, animated: false)
    }
}
