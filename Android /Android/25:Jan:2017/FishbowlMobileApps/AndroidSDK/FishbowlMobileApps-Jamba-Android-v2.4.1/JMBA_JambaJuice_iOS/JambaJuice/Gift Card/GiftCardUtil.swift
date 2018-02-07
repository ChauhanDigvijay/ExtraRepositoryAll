//
//  GiftCardUtil.swift
//  JambaJuice
//
//  Created by vThink Technologies on 18/11/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

open class GiftCardUtil: NSObject {
    class func validatePromoOffer() -> Bool {
        let today = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "dd/MM/yyyy 'at' hh:mm:ss a"
        let jambaPromoEndDay = formatter.date(from: "31/12/2016 at 11:59:59 PM")
        
        //check date if today is passed 26th Dec then the promo offer shouldn't show to user
        if today.compare(jambaPromoEndDay!) == ComparisonResult.orderedAscending {
            return true
        } else {
            return false
        }
    }
}
