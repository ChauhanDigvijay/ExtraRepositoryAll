//
//  OloFave.swift
//  Olo
//
//  Created by Taha Samad on 06/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

//fave {
//    id (int): ,
//    name (string): ,
//    vendorid (int): ,
//    vendorname (string): ,
//    disabled (bool): if true, this fave cannot be ordered because the menu has changed.,
//    online (bool):
//}

public struct OloFave {
    
    public var id: Int64
    public var name: String
    public var vendorId: Int64
    public var vendorName: String
    public var disabled: Bool
    public var online: Bool
    
    public init(json: JSON) {
        id         = json["id"].int64Value
        name       = json["name"].stringValue
        vendorId   = json["vendorid"].int64Value
        vendorName = json["vendorname"].stringValue
        disabled   = json["disabled"].boolValue
        online     = json["online"].boolValue
    }
    
}
