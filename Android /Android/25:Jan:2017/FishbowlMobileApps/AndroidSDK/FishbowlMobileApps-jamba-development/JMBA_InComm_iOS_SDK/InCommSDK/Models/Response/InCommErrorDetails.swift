//
//  InCommErrorDetails.swift
//  InCommSDK
//
//  Created by vThink on 9/2/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON


public struct InCommErrorDetails{
    
    public var validationErrors:InCommValidationErrors?
    public var requestID:String?
    public var code:Int32?
    public var message:String?
    
    public init(json: JSON){
        
        validationErrors = InCommValidationErrors(json: json["ValidationErrors"])
        requestID = json["RequestId"].string
        code = json["Code"].int32
        message = json["Message"].string
        
    }
}