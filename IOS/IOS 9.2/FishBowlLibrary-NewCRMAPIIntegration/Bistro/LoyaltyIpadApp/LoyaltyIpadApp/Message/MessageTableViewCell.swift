//
//  MessageTableViewCell.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 09/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class MessageTableViewCell: UITableViewCell
{
    
    @IBOutlet weak var imgCheck: UIImageView!
    @IBOutlet weak var btnCheckUnCheckOutlet: UIButton!
    @IBOutlet weak var lblMessageDate: UILabel!
    @IBOutlet weak var lblMessage: UILabel!
    
    var dictData = [String : AnyObject]()
    var check1 = true
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    override func setSelected(selected: Bool, animated: Bool)
    {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
       func configureCellWithData(dict : [String: AnyObject],index : Int) -> Void
    {
        lblMessageDate.text = dict["messageSent"] as? String
        lblMessage.text = dict["message"] as? String
        if (dict["isRead"] as? Bool)! == true
        {
            check1=true
            imgCheck.image=UIImage(named: "unChecked.png")
        }
        else
        {
            check1=false
            imgCheck.image=UIImage(named: "checked.png")
        }
    
    }
}
