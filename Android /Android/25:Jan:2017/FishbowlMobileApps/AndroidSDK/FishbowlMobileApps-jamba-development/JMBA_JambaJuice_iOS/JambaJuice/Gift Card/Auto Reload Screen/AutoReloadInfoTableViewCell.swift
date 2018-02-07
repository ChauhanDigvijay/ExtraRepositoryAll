//
//  AutoReloadInfoTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 16/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import InCommSDK

class AutoReloadInfoTableViewCell: UITableViewCell {
    @IBOutlet weak var labelName : UILabel!
    @IBOutlet weak var infoIcon  : UIImageView!
    var cellData                 : AutoReloadSettingsDataModel!
    var incommUserCard           : InCommUserGiftCard?
    let giftCardBrand            = InCommGiftCardBrandDetails.sharedInstance.brand!
    
    func setCellData(switchState:Bool, fieldData:AutoReloadSettingsDataModel,incommCard:InCommUserGiftCard) {
        cellData = fieldData
        incommUserCard = incommCard
        cellData.switchState = switchState
        
        labelName.text = String(format: "eGift Card Price Range " + GiftCardAppConstants.amountWithZeroDecimalPoint + " - " + GiftCardAppConstants.amountWithZeroDecimalPoint + " Auto-reloads occur once per day", giftCardBrand.variableAmountDenominationMinimumValue!, giftCardBrand.variableAmountDenominationMaximumValue!)
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