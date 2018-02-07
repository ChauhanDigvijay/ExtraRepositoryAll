//
//  OloBillingAccount.swift
//  Pods
//
//  Created by Taha Samad on 6/23/15.
//
//

import Foundation
import SwiftyJSON

public struct OloBillingAccount {
    
    public var accountId: Int64
    public var accountType: String
    public var cardType: String
    public var cardSuffix: String
    public var desc: String
    public var expiration: String
    
    public init(json: JSON) {
        accountId   = json["accountid"].int64Value
        accountType = json["accounttype"].stringValue
        cardType    = json["cardtype"].stringValue
        cardSuffix  = json["cardsuffix"].stringValue
        desc        = json["description"].stringValue
        expiration  = json["expiration"].stringValue
    }
}