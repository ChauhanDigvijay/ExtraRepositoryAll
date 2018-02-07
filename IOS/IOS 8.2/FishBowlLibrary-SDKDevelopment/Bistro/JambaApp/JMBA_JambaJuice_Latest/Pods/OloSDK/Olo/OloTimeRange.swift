//
//  OloTimeRange.swift
//  Olo
//
//  Created by Taha Samad on 5/15/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloTimeRange {
    
    public var start: String
    public var end: String
    public var weekday: String
    
    public init(json: JSON) {
        start = json["start"].stringValue
        end = json["end"].stringValue
        weekday = json["weekday"].stringValue
    }
    
}
