//
//  OloOption.swift
//  JambaJuice
//
//  Created by Taha Samad on 23/04/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloOption {

    public var id: Int64
    public var name: String
    public var isDefault: Bool
    public var cost: Double
    public var children: Bool
    public var modifiers: [OloModifier]
    public var fields: [OloCustomField]
    
    public init(json: JSON) {
        id         = json["id"].int64Value
        name       = json["name"].stringValue
        isDefault  = json["isdefault"].boolValue
        cost       = json["cost"].doubleValue
        children   = json["children"].boolValue
        modifiers  = json["modifiers"].arrayValue.map { item in OloModifier(json: item) }
        fields     = json["fields"].arrayValue.map { item in OloCustomField(json: item) }
    }
    
}
