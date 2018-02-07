//
//  ActivityTableViewCell.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 09/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class ActivityTableViewCell: UITableViewCell {
    @IBOutlet weak var lblType: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblActivityMessage: UILabel!
    @IBOutlet weak var lblCheckPoints: UILabel!
    @IBOutlet weak var lblPointsDescription: UILabel!
    @IBOutlet weak var lblPointsCount: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    func configureCellWithData(dict : [String: AnyObject]) -> Void
    {
        //        {
        //            activityType = "POINT_RULE";
        //            checkNumber = 0;
        //            created = "<null>";
        //            criteriaValue = 0;
        //            desc = "<null>";
        //            eventTime = "<null>";
        //            memberId = 38;
        //            pointsEarned = 2;
        //            ruleId = 466;
        //        }
        
        
        if (dict["eventDate"] as? String != nil)
        {
            var endStr:String
            endStr = (dict["eventDate"] as? String)!
            var myArray: [AnyObject] = endStr.componentsSeparatedByString(" ")
            let end: String = myArray[0] as! String
            NSLog("%@", end)
            let dateFormatter = NSDateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd"
            let date = dateFormatter.dateFromString(end)
            var strCurrentDate1: String = dateFormatter.stringFromDate(date!)
            dateFormatter.dateFormat = "MM/dd/yyyy"
            strCurrentDate1 = dateFormatter.stringFromDate(date!)
            lblDate.text = strCurrentDate1
        }
        else
        {
            lblDate.text="eventDate"
        }
        
        
        if (dict["name"] as? String != nil)
        {
            lblActivityMessage.text = dict["name"] as? String
        }
        else
        {
            lblActivityMessage.text="activityType"
        }
        
        if (dict["activityType"] as! String == "POINT_RULE")
        {
            lblType.text = "Earn"
        }
        else if (dict["activityType"] as! String == "REWARD_RULE")
        {
                lblType.text = "Earn"
        }
        else if (dict["activityType"] as! String == "REDEMPTION")
        {
            lblType.text = "Converted"
        }
        else if (dict["activityType"] as! String == "CLAIM")
        {
            lblType.text = "Redeemed"
        }
        
        print(dict["checkNumber"] as? String)
        
        if (dict["checkNumber"] as? NSNumber != 0 && dict["checkNumber"] as? String != "<null>" && dict["checkNumber"] as? String != nil)
        {
            lblPointsDescription.text = "Purchase check" + "-" + String(dict["checkNumber"] as? NSNumber)
        }
        
        else if (dict["offerId"] as? NSNumber != 0 && dict["desc"] as? String != nil)
        {
            lblPointsDescription.text = "Name of Reward- " + (dict["desc"] as? String)!
        }
        else
        {
             lblPointsDescription.text = ""
        }
        
        print(dict["balance"]!);
        print(dict["balance"]! as! String)
        
        
        if (dict["balance"]!  as? String != nil)
        {
            
            lblCheckPoints.text = (dict["balance"] as? String)! + " Points"
            
//            let strCheck = dict["checkNumber"] as? NSNumber
//            lblCheckPoints.text = "check " + (strCheck?.stringValue)!
        }
        else
        {
            lblCheckPoints.text="balance"
        }
        if (dict["pointsEarned"] as? NSNumber != nil)
        {
            //lblCheckPoints
            let pontscount = dict["pointsEarned"] as? NSNumber
            lblPointsCount.text =  (pontscount?.stringValue)! + " Points"
        }
        else
        {
            lblPointsCount.text="points Earned"
        }
        
    }
    
}
