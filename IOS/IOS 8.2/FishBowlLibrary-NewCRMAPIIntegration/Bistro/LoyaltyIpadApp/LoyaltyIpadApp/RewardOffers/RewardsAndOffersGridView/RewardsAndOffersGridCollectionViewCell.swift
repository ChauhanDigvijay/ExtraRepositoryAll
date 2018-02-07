//
//  RewardsAndOffersGridCollectionViewCell.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 21/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class RewardsAndOffersGridCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var lblOfferTitleGrid: UILabel!
    
    @IBOutlet weak var imgOfferGrid: UIImageView!
    
    func configureCellWithData(dict : [String: AnyObject],index : Int) -> Void
    {
        
        if (dict["campaignTitle"] as? String != nil)
        {
            lblOfferTitleGrid.text = dict["campaignTitle"] as? String
        }
        else
        {
            lblOfferTitleGrid.text = "no title"
        }
//        lblOfferTitleGrid.layer.borderColor = UIColor.lightGrayColor().CGColor;
//        lblOfferTitleGrid.layer.borderWidth=0.5
        if (dict["passCustomStripUrl"] as? String != nil)
        {
            let strUrl: String = (dict["passCustomStripUrl"] as? String)!
            let strurlLogo: String = "http://\(strUrl)"
            
            imgOfferGrid.sd_setImageWithURL(NSURL(string: strurlLogo), placeholderImage:UIImage(contentsOfFile:"placeholder.png"))
            //self.callImage(strUrl, setIndex: 7)
        }
        else
        {
            print("no image")
            imgOfferGrid.image=UIImage(named: "buttonBG.png")
        }
        
    }
    
}
