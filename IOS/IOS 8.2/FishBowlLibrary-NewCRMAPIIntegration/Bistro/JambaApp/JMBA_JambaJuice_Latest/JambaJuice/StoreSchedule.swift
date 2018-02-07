//
//  StoreTiming.swift
//  JambaJuice
//
//  Created by Taha Samad on 18/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

typealias StoreScheduleDictionary = [String:String]

struct StoreSchedule {
    
    var ranges: [TimeRange]

    static let secondsPerDay = 86400  // 24 * 60 * 60
    private let weekMap = [
        "mon": "Monday",
        "tue": "Tuesday",
        "wed": "Wednesday",
        "thu": "Thursday",
        "fri": "Friday",
        "sat": "Saturday",
        "sun": "Sunday"
    ]
    
    init(restaurantCalendar: OloRestaurantCalendar) {
        ranges = restaurantCalendar.ranges.map { range in TimeRange(timeRange: range) }
    }
    
    /// Returns dictonary of store hours for a entire week
    /// Assume closed on missing day.
    /// ["Monday": "6:30 am - 4:30 pm",...]
    func timingsStringsForAWeek() -> StoreScheduleDictionary {
        var schedule = StoreScheduleDictionary()
        for timeRange in ranges {
            if let dayString = weekMap[timeRange.weekday.lowercaseString] {
                schedule[dayString] = timeRange.rangeString
            }
        }
        return schedule
    }
    
    func hasTimingsForTodayAndTomorrow() -> Bool {
        let currentDate = NSDate()
        let newDate = currentDate.dateByAddingTimeInterval(NSTimeInterval(StoreSchedule.secondsPerDay))
        let dates = [currentDate, newDate]
        return hasTimingsForDates(dates)
    }
    
    func hasTimingsForAWeek() -> Bool {
        let currentDate = NSDate()
        var dates = [currentDate]
        for i in 1..<6 {
            let newDate = currentDate.dateByAddingTimeInterval(NSTimeInterval(i * StoreSchedule.secondsPerDay))
            dates.append(newDate)
        }
        return hasTimingsForDates(dates)
    }
    
    func hasTimingsForDates(dates: [NSDate]) -> Bool {
        if ranges.count < dates.count {
            return false
        }
        
        // Make a set of dates in ISO format
        var dateSet = Set<String>(dates.map { $0.oloDateString() })
        // Remove any dates in our range list
        for range in ranges {
            if range.isValid() {
                dateSet.remove(range.start!.oloDateString())
            }
        }

        // Dates match if set is empty
        return dateSet.count == 0
    }
    
    func startAndEndDateTimesForDate(date: NSDate) -> TimeRange? {
        //Surprisingly we need to init calendar with Identifier to make the check work
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        for range in ranges {
            if range.isValid() {
                if calendar.isDate(range.start!, inSameDayAsDate: date) {
                    return range
                }
            }
        }
        return nil
    }

    func startAndEndDateTimesForToday() -> TimeRange? {
        return startAndEndDateTimesForDate(NSDate())
    }
    
    func startAndEndDateTimesForTomorrow() -> TimeRange? {
        return startAndEndDateTimesForDate(NSDate(timeIntervalSinceNow: NSTimeInterval(StoreSchedule.secondsPerDay)))
    }
    
}
