//
//  InCommBillingCountries.swift
//  InCommSDK
//
//  Created by vThink on 8/26/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommBillingCountries{
    
    public var name:String
    public var code:String
    
    public init(json:JSON){
        name = json["Name"].stringValue
        code = json["Code"].stringValue
    }
}
