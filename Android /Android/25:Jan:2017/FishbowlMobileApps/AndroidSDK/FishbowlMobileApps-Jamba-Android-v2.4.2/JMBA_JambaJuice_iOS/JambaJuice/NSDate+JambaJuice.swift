//
//  NSDate+JambaJuice.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/26/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation

extension Date {
    func oloDateTimeString() -> String {
        let oloDateTimeFormatter = DateFormatter()
        oloDateTimeFormatter.dateFormat = "yyyyMMdd HH:mm"
        return oloDateTimeFormatter.string(from: self)
    }
    
    func oloDateString() -> String {
        let oloDateFormatter = DateFormatter()
        oloDateFormatter.dateFormat = "yyyyMMdd"
        return oloDateFormatter.string(from: self)
    }
    
    func dateString() -> String {
        let appDateFormatter = DateFormatter()
        appDateFormatter.dateStyle = .medium
        return appDateFormatter.string(from: self)
    }
    
    func timeString() -> String {
        let appTimeFormatter = DateFormatter()
        appTimeFormatter.timeStyle = .short
        return appTimeFormatter.string(from: self)
    }
    
    func currentDateIfDateInFuture() -> Date {
        let currentDateTime = Date()
        if timeIntervalSince1970 > currentDateTime.timeIntervalSince1970 {
            return currentDateTime
        }
        return self
    }
    
    func orderHistoryDateString() -> String {
        let appDateFormatter = DateFormatter()
        appDateFormatter.dateFormat = "MMM d, yyyy"
        return appDateFormatter.string(from: self)
    }
}

