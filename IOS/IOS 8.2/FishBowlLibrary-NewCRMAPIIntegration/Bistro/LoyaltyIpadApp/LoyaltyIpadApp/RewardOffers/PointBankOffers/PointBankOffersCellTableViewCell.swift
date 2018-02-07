//
//  PointBankOffersCellTableViewCell.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 19/01/17.
//  Copyright © 2017 fishbowl. All rights reserved.
//

import UIKit

class PointBankOffersCellTableViewCell: UITableViewCell {

    @IBOutlet weak var imgTrack: UIImageView!
    @IBOutlet weak var lblTrack: UILabel!
    @IBOutlet weak var imgTrackLastPoint: UIImageView!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblDescription: UILabel!
    @IBOutlet weak var btnRedeemPointBankOutlet: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func configureCellWithData(dict : [String: AnyObject],index : Int,earnPoints : Int) -> Void
    {
        if (dict["fishBowlPromotion"] as? NSDictionary != nil)
        {
            let publicname = dict["fishBowlPromotion"] as? NSDictionary
            let publicnameStr = publicname?.valueForKey("publicname")
            lblTitle.text = publicnameStr as? String
        }
        else
        {
            lblTitle.text = "no title"
        }
//        if (dict["campaignDescription"] as? String != nil)
//        {
//            lblDescription.text = dict["campaignDescription"] as? String
//        }
//        else
//        {
//            lblDescription.text = ""
//        }
      
        
       
        
    }

}
