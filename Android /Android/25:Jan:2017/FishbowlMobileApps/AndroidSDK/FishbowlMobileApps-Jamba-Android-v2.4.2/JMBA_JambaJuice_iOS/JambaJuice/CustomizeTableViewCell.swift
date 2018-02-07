//
//  CustomizeTableViewCell.swift
//  JambaJuice
//
//  Created by vThink on 3/29/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit

class CustomizeTableViewCell: UITableViewCell{
    
    @IBOutlet weak var unSelectedView:UIView?
    @IBOutlet weak var selectedView:UIView?
    
    @IBOutlet weak var selectionImageView:UIImageView?
    @IBOutlet weak var navigationImageView: UIImageView?
    @IBOutlet weak var selectedModifierLabel:UILabel?
    @IBOutlet weak var selectedSubstitutionLabel:UILabel?
    
    @IBOutlet weak var modifierLabel:UILabel?
    @IBOutlet weak var navigationOrSelectionImageView:UIImageView?
    
    //For ease of reference
    var modifierIndex: Int!
    var optionIndex: Int!
    
    var option: StoreMenuProductModifierOption?
    var selectedSubstitutionOption:StoreMenuProductModifierOption?

}
