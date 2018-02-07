//
//  AutoReloadWithoutValueCell.swift
//  JambaJuice
//
//  Created by vThink on 13/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK

class AutoReloadWithoutValueCell: UITableViewCell {
    
    @IBOutlet weak var labelName            : UILabel!
    @IBOutlet weak var radioButtonImageView : UIImageView!
    @IBOutlet weak var amountTextField      : UITextField!
    var cellData                            : AutoReloadSettingsDataModel!
    var incommUserCard                      : InCommUserGiftCard?
    
    func setCellData(_ fieldData:AutoReloadSettingsDataModel, switchState:Bool, incommCard:InCommUserGiftCard) {
        cellData = fieldData
        incommUserCard = incommCard
        cellData.switchState = switchState
        labelName.text = fieldData.name
        amountTextField.text = fieldData.field_value
        
        if fieldData.checkBoxSelected {
            radioButtonImageView.image = GiftCardAppConstants.radioButtonEnabled
        } else {
            radioButtonImageView.image = GiftCardAppConstants.radioButtonDisabled
        }
        
    }
    
    override func layoutSubviews() {
        //change the apperance of the cell based on switch state
        if !cellData.switchState  {
            makeDeActiveCell()
        } else {
            makeActiveCell()
        }
    }
    
    func makeActiveCell() {
        self.alpha = GiftCardAppConstants.tableViewCellActiveState
    }
    
    func makeDeActiveCell() {
        self.alpha = GiftCardAppConstants.tableViewCellDeactiveState
    }
}
