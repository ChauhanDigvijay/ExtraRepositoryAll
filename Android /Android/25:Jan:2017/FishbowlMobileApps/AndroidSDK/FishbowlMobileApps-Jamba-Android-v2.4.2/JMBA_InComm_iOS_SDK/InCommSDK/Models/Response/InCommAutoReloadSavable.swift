//
//  InCommAutoReloadSavable.swift
//  InCommSDK
//
//  Created by vThink on 8/10/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommAutoReloadSavable{
    public var amount: Double
    public var createdOn: Date?
    public var endsOn: Date?
    public var giftCardId: Int32
    public var id: Int32
    public var isActive: Bool
    public var lastErrorMessage: String
    public var lastModifiedByPortalUserId: Int32
    public var lastModifiedBySystemOn: Date?
    public var lastModifiedByUserOn: Date?
    public var minimumBalance: Double?
    public var nextReloadOn: Double
    public var numberOfOccurancesRemaining: Int16?
    public var paymentAccountId: Int32
    public var startsOn: Date?
    public var reloadFrequencyId: InCommReloadFrequencyType
    
    public init(json:JSON){
        amount                      =  json["Amount"].doubleValue
        createdOn                   =  json["CreatedOn"].string?.dateFromInCommFormatString()
        endsOn                      =  json["EndsOn"].string?.dateFromInCommFormatString()
        giftCardId                  =  json["GiftCardId"].int32Value
        id                          =  json["Id"].int32Value
        isActive                    =  json["IsActive"].boolValue
        lastErrorMessage            =  json["LastErrorMessage"].stringValue
        lastModifiedByPortalUserId  =  json["LastModifiedByPortalUserId"].int32Value
        lastModifiedBySystemOn      =  json["LastModifiedBySystemOn"].string?.dateFromInCommFormatString()
        lastModifiedByUserOn        =  json["LastModifiedByUserOn"].string?.dateFromInCommFormatString()
        minimumBalance              =  json["MinimumBalance"].double
        nextReloadOn                =  json["NextReloadOn"].doubleValue
        numberOfOccurancesRemaining =  json["NumberOfOccurancesRemaining"].int16Value
        paymentAccountId            =  json["PaymentAccountId"].int32Value
        startsOn                    =  json["StartsOn"].string?.dateFromInCommFormatString()
        reloadFrequencyId           =  InCommReloadFrequencyType(rawValue: json["ReloadFrequencyId"].stringValue)!
    }
}
