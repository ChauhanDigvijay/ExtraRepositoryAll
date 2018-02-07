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
    fileprivate let weekMap = [
        "sun": "Sunday",
        "mon": "Monday",
        "tue": "Tuesday",
        "wed": "Wednesday",
        "thu": "Thursday",
        "fri": "Friday",
        "sat": "Saturday"
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
            if let dayString = weekMap[timeRange.weekday.lowercased()] {
                schedule[dayString] = timeRange.rangeString
            }
        }
        return schedule
    }
    
    func hasTimingsForTodayAndTomorrow() -> Bool {
        let currentDate = Date()
        let newDate = currentDate.addingTimeInterval(TimeInterval(StoreSchedule.secondsPerDay))
        let dates = [currentDate, newDate]
        return hasTimingsForDates(dates)
    }
    
    func hasTimingsForAWeek() -> Bool {
        let currentDate = Date()
        var dates = [currentDate]
        for i in 1..<6 {
            let newDate = currentDate.addingTimeInterval(TimeInterval(i * StoreSchedule.secondsPerDay))
            dates.append(newDate)
        }
        return hasTimingsForDates(dates)
    }
    
    func hasTimingsForDates(_ dates: [Date]) -> Bool {
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
    
    func startAndEndDateTimesForDate(_ date: Date) -> TimeRange? {
        //Surprisingly we need to init calendar with Identifier to make the check work
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        for range in ranges {
            if range.isValid() {
                if calendar.isDate(range.start! as Date, inSameDayAs: date) {
                    return range
                }
            }
        }
        return nil
    }

    func startAndEndDateTimesForToday() -> TimeRange? {
        return startAndEndDateTimesForDate(Date())
    }
    
    func startAndEndDateTimesForTomorrow() -> TimeRange? {
        return startAndEndDateTimesForDate(Date(timeIntervalSinceNow: TimeInterval(StoreSchedule.secondsPerDay)))
    }
    
}
