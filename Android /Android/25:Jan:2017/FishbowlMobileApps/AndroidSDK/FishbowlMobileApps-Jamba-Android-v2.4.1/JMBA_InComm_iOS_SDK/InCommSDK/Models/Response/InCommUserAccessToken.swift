//
//  InCommUserAccessToken.swift
//  InCommSDK
//
//  Created by vThink on 8/24/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommUserAccessToken{
    
    public var userID: Int32
    public var token: String
    
    public init (json: JSON){
        userID     = json["UserId"].int32Value
        token      = json["Token"].stringValue
    }
}