//
//  String+JambaJuice.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/26/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation

extension String {
    
    func dateFromOloDateTimeString() -> Date? {
        let oloDateTimeFormatter = DateFormatter()
        oloDateTimeFormatter.dateFormat = "yyyyMMdd HH:mm"
        return oloDateTimeFormatter.date(from: self)
    }
    
    func stringByUnescapingXML() -> String {
        return self.replacingOccurrences(of: "&amp;", with: "&", options: .caseInsensitive)
            .replacingOccurrences(of: "&#xD;&#xA;&#xD;&#xA;", with: "\n", options: .caseInsensitive) // Double line break into one
            .replacingOccurrences(of: "&#xD;&#xA;", with: "\n", options: .caseInsensitive)
            .trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
    }

    //Formats accepted mm/dd/yyyy or mm-dd-yyyy or mm.dd.yyyy format
    func dateFromCommonDateFormats() -> Date? {
        let separator: String
        if (self.range(of: ".", options: .caseInsensitive, range: nil, locale: nil) != nil) {
            separator = "."
        }
        else if (self.range(of: "/", options: .caseInsensitive, range: nil, locale: nil) != nil) {
            separator = "/"
        }
        else { // -
            separator = "-"
        }
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MM\(separator)dd\(separator)yyyy"
        //Get DOB from text
        let dob = dateFormatter.date(from: self)
        return dob
    }
    
    
    func convertStringToDate(_ strDate:String) -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat="yyyy-MM-dd HH:mm:ss";
        let date = dateFormatter.date(from: strDate)
        
        var number = 0
        if let formattedDate = date {
            number = self.daysFrom(formattedDate)
        } else {
            return ""
        }
        if number < 0{
            return "Expired"
        }
        else if number == 0{
            return "Expires today"
        }
        else if number == 1{
             return "Expires in \(number) day"
        }else{
             return "Expires in \(number) days"
        }
    }
    
    func daysFrom(_ date:Date) -> Int{
        
        let todaydate = Date()
        return (Calendar.current as NSCalendar).components(.day, from: todaydate, to: date, options: []).day!
    }
    
}
