//
//  MakeItLightTableViewCell.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol MakeItLightTableViewCellDelegate: class {
    func makeItLightTableViewCellDidChangeSwitchState(cell: MakeItLightTableViewCell)
}

class MakeItLightTableViewCell: UITableViewCell {

    weak var delegate: MakeItLightTableViewCellDelegate?
    @IBOutlet weak var makeItLightLabel: UILabel!
    @IBOutlet weak var switchControl: UISwitch!

    @IBAction func switchValueChanged(sender: AnyObject) {
        delegate?.makeItLightTableViewCellDidChangeSwitchState(self)
    }

}
