//
//  OloBatchProductChoice.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

public struct OloBatchProductChoice {

    public var choiceId: Int64
    public var customFields: [OloChoiceCustomFieldValue]

    public init() {
        choiceId = 0
        customFields = []
    }
    
    public func serializeAsJSONDictionary() -> OloJSONDictionary {
        var jsonDict = OloJSONDictionary()
        jsonDict["choiceid"]     = NSNumber(longLong: choiceId)
        jsonDict["customfields"] = customFields.map { item in item.serializeAsJSONDictionary() }
        return jsonDict
    }
    
}
