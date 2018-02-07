//
//  OccurancesTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 17/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit

class OccurancesTableViewCell: UITableViewCell {
    
    @IBOutlet weak var name                 : UILabel!
    @IBOutlet weak var occuranceTextField   : UITextField!
    
    func setData(_ occuranceNumber:String) {
        occuranceTextField.text = occuranceNumber
    }
    
    override func layoutSubviews() {
        if (occuranceTextField.text == "0") {
            makeDeActiveCell()
        }
    }
    
    func makeActiveCell() {
        occuranceTextField.becomeFirstResponder()
        self.alpha = GiftCardAppConstants.tableViewCellActiveState
    }
    
    func makeDeActiveCell() {
        occuranceTextField.text = ""
        self.alpha = GiftCardAppConstants.tableViewCellDeactiveState
    }
    
}
