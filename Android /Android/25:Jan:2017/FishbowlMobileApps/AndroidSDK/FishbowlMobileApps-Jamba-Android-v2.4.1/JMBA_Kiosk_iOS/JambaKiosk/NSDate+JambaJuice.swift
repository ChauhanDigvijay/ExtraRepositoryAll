//
//  NSDate+JambaJuice.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/26/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation

extension NSDate {
    func oloDateTimeString() -> String {
        let oloDateTimeFormatter = NSDateFormatter()
        oloDateTimeFormatter.dateFormat = "yyyyMMdd HH:mm"
        return oloDateTimeFormatter.stringFromDate(self)
    }

    func oloDateString() -> String {
        let oloDateFormatter = NSDateFormatter()
        oloDateFormatter.dateFormat = "yyyyMMdd"
        return oloDateFormatter.stringFromDate(self)
    }

    func dateString() -> String {
        let appDateFormatter = NSDateFormatter()
        appDateFormatter.dateStyle = .MediumStyle
        return appDateFormatter.stringFromDate(self)
    }

    func timeString() -> String {
        let appTimeFormatter = NSDateFormatter()
        appTimeFormatter.timeStyle = .ShortStyle
        return appTimeFormatter.stringFromDate(self)
    }

    func currentDateIfDateInFuture() -> NSDate {
        let currentDateTime = NSDate()
        if timeIntervalSince1970 > currentDateTime.timeIntervalSince1970 {
            return currentDateTime
        }
        return self
    }

}
