//
//  OloBillingField.swift
//  Olo
//
//  Created by vThink on 9/14/16.
//  Copyright © 2016 Fishbowl, Inc. All rights reserved.
//

import UIKit
import SwiftyJSON

public struct OloBillingField{
    public var name:String
    public var value:String
    
    public init(){
        name = ""
        value = ""
    }
    
    public func serializeAsJSONDictionary() -> OloJSONDictionary{
        var jsonDict = OloJSONDictionary()
        jsonDict["name"] = name as AnyObject?
        jsonDict["value"]   = value as AnyObject?
        return jsonDict
    }
}
