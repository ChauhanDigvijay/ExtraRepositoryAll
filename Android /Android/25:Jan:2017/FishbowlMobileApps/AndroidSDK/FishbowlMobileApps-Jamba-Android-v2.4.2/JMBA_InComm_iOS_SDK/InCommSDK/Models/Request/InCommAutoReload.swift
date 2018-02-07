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
    public var endsOn: Date?
    public var giftCardId: Int32!
    public var minimumBalance: Double?
    public var numberOfOccurancesRemaining: Int16?
    public var paymentAccountId: Int32!
    public var startsOn: Date!
    public var reloadFrequencyId: InCommReloadFrequencyType!
    
    public init(amount: Double!, endsOn: Date?, giftCardId: Int32!, minimumBalance: Double?, numberOfOccurancesRemaining: Int16?, paymentAccountId: Int32!, startsOn: Date!, reloadFrequencyId: InCommReloadFrequencyType!){
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
        jsonDict["Amount"]                      = amount as AnyObject?
        jsonDict["EndsOn"]                      = endsOn?.InCommDateTimeFormatString() as AnyObject?
        jsonDict["GiftCardId"]                  = NSNumber(value: giftCardId as Int32)
        jsonDict["MinimumBalance"]              = minimumBalance as AnyObject?
        if numberOfOccurancesRemaining != nil{
            jsonDict["NumberOfOccurancesRemaining"] = NSNumber(value: numberOfOccurancesRemaining! as Int16)
        }
        jsonDict["PaymentAccountId"]            = NSNumber(value: paymentAccountId as Int32)
        jsonDict["StartsOn"]                    = startsOn.InCommDateTimeFormatString() as AnyObject?
        jsonDict["ReloadFrequencyId"]           = reloadFrequencyId.rawValue as AnyObject?
        return jsonDict
    }
    
    
}
