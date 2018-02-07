//
//  RewardAndOffersTableViewCell.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 16/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class RewardAndOffersTableViewCell: UITableViewCell {
    @IBOutlet weak var lblOfferExpiryDate: UILabel!
    
    @IBOutlet weak var lblOfferTitle: UILabel!
    @IBOutlet weak var lblOfferDescription: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    @IBAction func tapCheck(sender:UIButton)
    {
        //        if check1==true
        //        {
        //            check1=false
        //            imgCheck.image=UIImage(named: "unChecked.png")
        //            dictData=messageArray[sender.tag]
        //            dictData["checked"]=false
        //            messageArray[sender.tag]=dictData
        //
        //        }
        //        else
        //        {
        //            check1=true
        //            imgCheck.image=UIImage(named: "checked.png")
        //            dictData=messageArray[sender.tag]
        //            dictData["checked"]=true
        //            messageArray[sender.tag]=dictData
        //        }
    }
    func configureCellWithData(dict : [String: AnyObject],index : Int) -> Void
    {
        
        if (dict["campaignTitle"] as? String != nil)
        {
            lblOfferTitle.text = dict["campaignTitle"] as? String
        }
        else
        {
            lblOfferTitle.text = "no title"
        }
        if (dict["campaignDescription"] as? String != nil)
        {
            lblOfferDescription.text = dict["campaignDescription"] as? String
        }
        else
        {
            lblOfferDescription.text = ""
        }
        var endStr:String
        if (dict["validityEndDateTime"] as? String != nil)
        {
            endStr = (dict["validityEndDateTime"] as? String)!
            if endStr=="" {
                
                lblOfferExpiryDate.text = "Never expired"
            }
            else
            {
                
                var myArray: [AnyObject] = endStr.componentsSeparatedByString(" ")
                let end: String = myArray[0] as! String
                NSLog("%@", end)
                let currentDate: NSDate = NSDate()
                let currentF: NSDateFormatter = NSDateFormatter()
                currentF.dateFormat = "yyyy-MM-dd"
                let strCurrentDate1: String = currentF.stringFromDate(currentDate)
                let f: NSDateFormatter = NSDateFormatter()
                f.dateFormat = "yyyy-MM-dd"
                let startDate: NSDate = f.dateFromString(strCurrentDate1)!
                //                let endDate: NSDate = f.dateFromString(end)!
                guard let endDate = f.dateFromString(end) else
                {
                    lblOfferExpiryDate.text="Never expired"
                    return
                }
                
                let gregorianCalendar: NSCalendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
                let components: NSDateComponents = gregorianCalendar.components(.Day, fromDate: startDate, toDate: endDate, options:[])
                NSLog("%ld", Int(components.day))
                let totalDays: String = "Expires in \(Int(components.day))  days"
                NSLog("%@", totalDays)
                lblOfferExpiryDate.text = totalDays
            }
        }
        else
        {
            lblOfferExpiryDate.text="Never expired"
            
        }
        
    }
}
