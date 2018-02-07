//
//  NSDate+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 6/22/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

extension Date {

    /// Return date in ISO format
    public func ISOString() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter.string(from: self)
    }
    
    public func timeAgoWithDayAsMinUnit() -> String {
        let now = Date()
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        let components = (calendar as NSCalendar).components(.day, from: self, to: now, options: .wrapComponents)
        var days = components.day
        
        var pastFutureString = "ago"
        
        if days! < 0 {
            days = Swift.abs(-1 * days!)
            pastFutureString = "from now"
        }
        
        if days == 0 {//The day only becomes 1 after 24h irrespective of day
            if calendar.isDateInToday(self) {
                return "today"
            }
            else if calendar.isDateInTomorrow(self) {
                return "1 day from now"
            }
            else {//Case of yesterday with less then 24h diff
                return "1 day ago"
            }
        }
        
        if days! < 30 {
            var pluralString = ""
            if days! > 1 {
                pluralString = "s"
            }
            return "\(days!) day\(pluralString) \(pastFutureString)"
        }

        if days! >= 30 && days! < 365 {
            let months = days! / 30
            var pluralString = ""
            if months > 1 {
                pluralString = "s"
            }
            return "\(months) month\(pluralString) \(pastFutureString)"
        }

        let years = days! / 365
        var pluralString = ""
        if years > 1 {
            pluralString = "s"
        }
        return "\(years) year\(pluralString) \(pastFutureString)"
    }
    
    public func isTodayInGregorianCalendar() -> Bool {
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        return calendar.isDateInToday(self)
    }
    
    public func isTomorrowInGregorianCalendar() -> Bool {
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        return calendar.isDateInTomorrow(self)
    }
    
    public func differenceFromTodayInGregorianCalendar() -> DateComponents {
        let now = Date()
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        let components = (calendar as NSCalendar).components([.day, .month, .year], from: now, to: self, options: .wrapComponents)
        return components
    }
    
    public func differenceToTodayInGregorianCalendar() -> DateComponents {
        let now = Date()
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        let components = (calendar as NSCalendar).components([.day, .month, .year], from: self, to: now, options: .wrapComponents)
        return components
    }
    
    public func minOfHourInGregorianCalendar() -> Int {
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        return (calendar as NSCalendar).component(.minute, from: self)
    }

    public func hourOfDayInGregorianCalendar() -> Int {
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        return (calendar as NSCalendar).component(.hour, from: self)
    }
    
    public func dayOfMonthInGregorianCalendar() -> Int {
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        return (calendar as NSCalendar).component(.day, from: self)
    }
    
    public func monthOfYearInGregorianCalendar() -> Int {
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        return (calendar as NSCalendar).component(.month, from: self)
    }
    
    public func yearInGregorianCalendar() -> Int {
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        return (calendar as NSCalendar).component(.year, from: self)
    }

    public static func currentDayOfMonthInGregorianCalendar() -> Int {
        let today = Date()
        return today.dayOfMonthInGregorianCalendar()
    }

    public static func currentMonthOfYearInGregorianCalendar() -> Int {
        let today = Date()
        return today.monthOfYearInGregorianCalendar()
    }

    public static func currentYearInGregorianCalendar() -> Int {
        let today = Date()
        return today.yearInGregorianCalendar()
    }
    
}
