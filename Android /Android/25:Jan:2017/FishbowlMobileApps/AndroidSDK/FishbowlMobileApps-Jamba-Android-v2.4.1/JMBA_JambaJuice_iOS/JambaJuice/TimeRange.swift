//
//  TimeRange.swift
//  JambaJuice
//
//  Created by Taha Samad on 18/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct TimeRange {
    
    var weekday: String
    var start: Date?
    var end: Date?
    
    init(timeRange: OloTimeRange) {
        weekday = timeRange.weekday
        start = timeRange.start.dateFromOloDateTimeString()
        end = timeRange.end.dateFromOloDateTimeString()
    }
    
    func isValid() -> Bool {
        return start != nil && end != nil
    }
    
    var rangeString: String? {
        get {
            if !isValid() {
                return nil
            }
            let startTime = start!.timeString()
            let endTime = end!.timeString()
            return "\(startTime) - \(endTime)"
        }
    }
    
}
