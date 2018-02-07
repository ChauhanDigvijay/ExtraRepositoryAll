//
//  ProfileTableViewCell.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 23/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class ProfileTableViewCell: UITableViewCell,UITextFieldDelegate
{
    @IBOutlet weak var viewContainDate: UIView!
    @IBOutlet weak var lblDateShowfield: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblSelectStoreValue: UILabel!
    @IBOutlet weak var lblSelectType: UILabel!
    @IBOutlet weak var btnCheckUnCheckMaleOutlet: UIButton!
    @IBOutlet weak var btnCheckUnCheckFemaleOutlet: UIButton!
    @IBOutlet weak var btnCheckOutlet: UIButton!
    @IBOutlet weak var lblOption: UILabel!
    @IBOutlet weak var viewContainSelectTypeData: UIView!
    @IBOutlet weak var viewContainOption: UIView!
    @IBOutlet weak var viewContainText: UIView!
    @IBOutlet weak var lblFieldName: UILabel!
    
    @IBOutlet weak var viewContainGender: UIView!
    @IBOutlet weak var txtTypeOfField: UITextField!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    func configureCellWithData(dict : [String: AnyObject],index : Int) -> Void
    {
        lblFieldName.text = dict["Name"] as? String
        txtTypeOfField.text = dict["firstName"] as? String
        txtTypeOfField.userInteractionEnabled=false
        txtTypeOfField.delegate=self
        
        txtTypeOfField.returnKeyType = .Next
    }
    
    
    
}
