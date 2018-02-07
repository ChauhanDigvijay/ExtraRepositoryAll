//
//  OloChoiceCustomFieldValue.swift
//  Olo
//
//  Created by Taha Samad on 5/5/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloChoiceCustomFieldValue {
    
    public var fieldId: Int64
    public var value: String

    public init(json: JSON) {
        fieldId = json["fieldid"].int64Value
        value   = json["value"].stringValue
    }
    
    public func serializeAsJSONDictionary() -> OloJSONDictionary {
        var jsonDict = OloJSONDictionary()
        jsonDict["fieldid"] = NSNumber(longLong: fieldId)
        jsonDict["value"]   = value
        return jsonDict
    }
    
}
