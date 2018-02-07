//
//  InCommBillingStates.swift
//  InCommSDK
//
//  Created by vThink on 8/26/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommBillingStates{
    
    public var name:String
    public var code:String
    public var countryCode:String
    public var type:String
    
    public init(json: JSON){
        name        = json["Name"].stringValue
        code        = json["Code"].stringValue
        countryCode = json["CountryCode"].stringValue
        type        = json["Type"].stringValue
    }
}
