//
//  AddPaymentSingleTextFieldTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 12/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK

protocol AddPaymentSingleTextFieldTableViewCellDelegate: class {
    func creditCardImageSelected()
}

class AddPaymentSingleTextFieldTableViewCell: UITableViewCell {
    @IBOutlet weak var firstTextField               : UITextField!
    @IBOutlet weak var cameraImage                  : UIButton!
    @IBOutlet weak var firstTextFieldTrailingSpace  : NSLayoutConstraint!
    var cellData                                    : FieldSetDataModel!
    weak var delegate                               : AddPaymentSingleTextFieldTableViewCellDelegate?
    
    func setCellData(_ fieldData:FieldSetDataModel) {
        firstTextField.placeholder = fieldData.name
        firstTextField.text = fieldData.field_value
        firstTextField.isEnabled = false
        firstTextField.keyboardType = fieldData.keyboardType
        cellData = fieldData
        cameraImage.isHidden = true
        firstTextFieldTrailingSpace.constant = GiftCardAppConstants.tableViewTrailingSpace
    }
    
    func setCreditCardCellData(_ fieldData:FieldSetDataModel) {
        firstTextField.placeholder = fieldData.name
        firstTextField.text = fieldData.field_value
        firstTextField.isEnabled = false
        firstTextField.keyboardType = fieldData.keyboardType
        cellData = fieldData
        cameraImage.isHidden = false
        firstTextFieldTrailingSpace.constant = GiftCardAppConstants.tableViewTrailingSpace + 40 + 10
    }
    
    @IBAction func creditCardImageSelected() {
        delegate?.creditCardImageSelected()
    }
}
