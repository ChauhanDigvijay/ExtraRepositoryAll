//
//  OloRestaurantCalendar.swift
//  Olo
//
//  Created by Taha Samad on 5/15/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloRestaurantCalendar {
    
    public var type: String
    public var ranges: [OloTimeRange]
    
    public init(json: JSON) {
        type = json["type"].stringValue
        ranges = json["ranges"].arrayValue.map { item in OloTimeRange(json: item) }
    }
    
}
