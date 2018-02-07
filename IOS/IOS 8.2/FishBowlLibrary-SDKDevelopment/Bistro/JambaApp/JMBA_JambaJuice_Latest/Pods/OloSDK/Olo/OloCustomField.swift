//
//  OloCustomField.swift
//  JambaJuice
//
//  Created by Taha Samad on 23/04/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloCustomField {

    public var id: Int64
    public var isRequired: Bool
    public var label: String
    public var maxLength: Int

    public init(json: JSON) {
        id         = json["id"].int64Value
        isRequired = json["isrequired"].boolValue
        label      = json["label"].stringValue
        maxLength  = json["maxlength"].intValue
    }
    
}
