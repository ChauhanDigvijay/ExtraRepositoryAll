//
//  OloBasketChoice.swift
//  Olo
//
//  Created by Taha Samad on 5/5/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloBasketChoice {
    
    public var id: Int64
    public var optionId: Int64
    public var name: String
    public var metric: Int64
    public var indent: Int64
    public var cost: Double
    public var quantity: Int64?
    public var customFields: [OloChoiceCustomFieldValue]
    
    public init(json: JSON) {
        id           = json["id"].int64Value
        optionId     = json["optionid"].int64Value
        name         = json["name"].stringValue
        metric       = json["metric"].int64Value
        indent       = json["indent"].int64Value
        cost         = json["cost"].doubleValue
        quantity     = json["quantity"].int64Value ?? 1
        customFields = json["customfields"].arrayValue.map { item in OloChoiceCustomFieldValue(json: item) }
    }

}
