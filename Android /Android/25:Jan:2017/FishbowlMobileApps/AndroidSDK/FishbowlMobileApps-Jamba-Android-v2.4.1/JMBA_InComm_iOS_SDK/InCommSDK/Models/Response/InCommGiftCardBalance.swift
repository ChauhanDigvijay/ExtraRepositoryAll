//
//  InCommGiftCardBalance.swift
//  InCommSDK
//
//  Created by vThink on 8/11/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommGiftCardBalance{
    
    public var balance: Double
    public var balanceDate: Date?
    
    public init (json: JSON){
        balance     = json["Balance"].doubleValue
        balanceDate = json["BalanceDate"].string?.dateFromInCommFormatString()
    }
}

