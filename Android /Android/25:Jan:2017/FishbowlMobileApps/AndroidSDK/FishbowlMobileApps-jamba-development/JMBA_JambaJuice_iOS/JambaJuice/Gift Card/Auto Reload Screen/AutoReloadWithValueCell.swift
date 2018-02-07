//
//  AutoReloadWithValueCell.swift
//  JambaJuice
//
//  Created by vThink on 13/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK

class AutoReloadWithValueCell: UITableViewCell {
    @IBOutlet weak var labelName            : UILabel!
    @IBOutlet weak var fieldValue           : UILabel!
    @IBOutlet weak var navigationImage      : UIImageView!
    @IBOutlet weak var radioButtonImageView : UIImageView!
    var cellData                            : AutoReloadSettingsDataModel!
    var incommuserCard                      : InCommUserGiftCard?
    
    func setCellData(fieldData:AutoReloadSettingsDataModel, switchState:Bool, incommCard:InCommUserGiftCard) {
        cellData = fieldData
        incommuserCard = incommCard
        cellData.switchState = switchState
        labelName.text = fieldData.name
        fieldValue.text = fieldData.field_value
        navigationImage.image = GiftCardAppConstants.backButtonImageWhenPresented
        if fieldData.checkBoxSelected {
            radioButtonImageView.image = GiftCardAppConstants.radioButtonEnabled
        } else {
            radioButtonImageView.image = GiftCardAppConstants.radioButtonDisabled
        }
        
    }
    
    override func layoutSubviews() {
        //change the apperance of the cell based on switch state
        if !cellData.switchState   {
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