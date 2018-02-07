//
//  NSDate+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 6/22/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

extension NSDate {

    /// Return date in ISO format
    public func ISOString() -> String {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter.stringFromDate(self)
    }
    
    public func timeAgoWithDayAsMinUnit() -> String {
        let now = NSDate()
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        let components = calendar.components(.Day, fromDate: self, toDate: now, options: .WrapComponents)
        var days = components.day
        
        var pastFutureString = "ago"
        
        if days < 0 {
            days = abs(days)
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
        
        if days < 30 {
            var pluralString = ""
            if days > 1 {
                pluralString = "s"
            }
            return "\(days) day\(pluralString) \(pastFutureString)"
        }

        if days >= 30 && days < 365 {
            let months = days / 30
            var pluralString = ""
            if months > 1 {
                pluralString = "s"
            }
            return "\(months) month\(pluralString) \(pastFutureString)"
        }

        let years = days / 365
        var pluralString = ""
        if years > 1 {
            pluralString = "s"
        }
        return "\(years) year\(pluralString) \(pastFutureString)"
    }
    
    public func isTodayInGregorianCalendar() -> Bool {
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        return calendar.isDateInToday(self)
    }
    
    public func isTomorrowInGregorianCalendar() -> Bool {
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        return calendar.isDateInTomorrow(self)
    }
    
    public func differenceFromTodayInGregorianCalendar() -> NSDateComponents {
        let now = NSDate()
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        let components = calendar.components([.Day, .Month, .Year], fromDate: now, toDate: self, options: .WrapComponents)
        return components
    }
    
    public func differenceToTodayInGregorianCalendar() -> NSDateComponents {
        let now = NSDate()
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        let components = calendar.components([.Day, .Month, .Year], fromDate: self, toDate: now, options: .WrapComponents)
        return components
    }
    
    public func minOfHourInGregorianCalendar() -> Int {
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        return calendar.component(.Minute, fromDate: self)
    }

    public func hourOfDayInGregorianCalendar() -> Int {
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        return calendar.component(.Hour, fromDate: self)
    }
    
    public func dayOfMonthInGregorianCalendar() -> Int {
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        return calendar.component(.Day, fromDate: self)
    }
    
    public func monthOfYearInGregorianCalendar() -> Int {
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        return calendar.component(.Month, fromDate: self)
    }
    
    public func yearInGregorianCalendar() -> Int {
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        return calendar.component(.Year, fromDate: self)
    }

    public class func currentDayOfMonthInGregorianCalendar() -> Int {
        let today = NSDate()
        return today.dayOfMonthInGregorianCalendar()
    }

    public class func currentMonthOfYearInGregorianCalendar() -> Int {
        let today = NSDate()
        return today.monthOfYearInGregorianCalendar()
    }

    public class func currentYearInGregorianCalendar() -> Int {
        let today = NSDate()
        return today.yearInGregorianCalendar()
    }
    
}
