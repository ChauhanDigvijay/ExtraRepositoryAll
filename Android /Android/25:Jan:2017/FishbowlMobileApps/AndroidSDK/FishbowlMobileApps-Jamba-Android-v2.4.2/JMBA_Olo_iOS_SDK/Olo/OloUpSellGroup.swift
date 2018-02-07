//
//  OloUpSellGroup.swift
//  Olo
//
//  Created by VT010 on 10/20/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation
import Foundation
import SwiftyJSON

public struct OloUpSellGroup {
    
    public var title: String
    public var items: [OloUpSellItem]
    
    public init(json: JSON) {
        title                  = json["title"].stringValue
        items            = json["items"].arrayValue.map { item in OloUpSellItem(json: item) }
    }
}
