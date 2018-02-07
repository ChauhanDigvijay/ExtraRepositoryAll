//
//  GiftCardUtil.swift
//  JambaJuice
//
//  Created by VT016 on 18/11/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

public class GiftCardUtil: NSObject {
    class func validatePromoOffer() -> Bool {
        let today = NSDate()
        let formatter = NSDateFormatter()
        formatter.dateFormat = "dd/MM/yyyy 'at' hh:mm:ss a"
        let jambaPromoEndDay = formatter.dateFromString("31/12/2016 at 11:59:59 PM")
        
        //check date if today is passed 26th Dec then the promo offer shouldn't show to user
        if today.compare(jambaPromoEndDay!) == NSComparisonResult.OrderedAscending {
            return true
        } else {
            return false
        }
    }
}
