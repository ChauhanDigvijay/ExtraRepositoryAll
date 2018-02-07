//
//  OloUpSellItem.swift
//  Olo
//
//  Created by VT010 on 10/20/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON
public struct OloUpSellItem {
    
    public var id: Int64
    public var name: String
    public var cost: String
    public var shortdescription: String
    public var minquantity: Int
    public var maxquantity: Int
    
    public init(json: JSON) {
        id                  = json["id"].int64Value
        name                = json["name"].stringValue
        cost                = json["cost"].stringValue
        shortdescription    = json["shortdescription"].stringValue
        minquantity         = json["minquantity"].intValue
        maxquantity         = json["maxquantity"].intValue
    }
}
