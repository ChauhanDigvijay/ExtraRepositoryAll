//
//  InCommAutoReload.swift
//  InCommSDK
//
//  Created by vThink on 8/10/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommAutoReload{
    
    public var amount: Double!
    public var endsOn: NSDate?
    public var giftCardId: Int32!
    public var minimumBalance: Double?
    public var numberOfOccurancesRemaining: Int16?
    public var paymentAccountId: Int32!
    public var startsOn: NSDate!
    public var reloadFrequencyId: InCommReloadFrequencyType!
    
    public init(amount: Double!, endsOn: NSDate?, giftCardId: Int32!, minimumBalance: Double?, numberOfOccurancesRemaining: Int16?, paymentAccountId: Int32!, startsOn: NSDate!, reloadFrequencyId: InCommReloadFrequencyType!){
        self.amount                      = amount
        self.endsOn                      = endsOn
        self.giftCardId                  = giftCardId
        self.minimumBalance              = minimumBalance
        self.numberOfOccurancesRemaining = numberOfOccurancesRemaining
        self.paymentAccountId            = paymentAccountId
        self.startsOn                    = startsOn
        self.reloadFrequencyId           = reloadFrequencyId
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict                            = InCommJSONDictionary()
        jsonDict["Amount"]                      = amount
        jsonDict["EndsOn"]                      = endsOn?.InCommDateTimeFormatString()
        jsonDict["GiftCardId"]                  = NSNumber(int: giftCardId)
        jsonDict["MinimumBalance"]              = minimumBalance
        if numberOfOccurancesRemaining != nil{
            jsonDict["NumberOfOccurancesRemaining"] = NSNumber(short: numberOfOccurancesRemaining!)
        }
        jsonDict["PaymentAccountId"]            = NSNumber(int: paymentAccountId)
        jsonDict["StartsOn"]                    = startsOn.InCommDateTimeFormatString()
        jsonDict["ReloadFrequencyId"]           = reloadFrequencyId.rawValue
        return jsonDict
    }
    
    
}