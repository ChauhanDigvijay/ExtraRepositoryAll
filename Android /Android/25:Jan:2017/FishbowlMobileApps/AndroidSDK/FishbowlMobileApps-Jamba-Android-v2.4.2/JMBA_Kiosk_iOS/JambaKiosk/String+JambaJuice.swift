//
//  String+JambaJuice.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/26/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation

extension String {

    func dateFromOloDateTimeString() -> NSDate? {
        let oloDateTimeFormatter = NSDateFormatter()
        oloDateTimeFormatter.dateFormat = "yyyyMMdd HH:mm"
        return oloDateTimeFormatter.dateFromString(self)
    }

    func stringByUnescapingXML() -> String {
        return self.stringByReplacingOccurrencesOfString("&amp;", withString: "&", options: .CaseInsensitiveSearch)
            .stringByReplacingOccurrencesOfString("&#xD;&#xA;&#xD;&#xA;", withString: "\n", options: .CaseInsensitiveSearch) // Double line break into one
            .stringByReplacingOccurrencesOfString("&#xD;&#xA;", withString: "\n", options: .CaseInsensitiveSearch)
            .stringByTrimmingCharactersInSet(NSCharacterSet.whitespaceAndNewlineCharacterSet())
    }

    // Formats accepted mm/dd/yyyy or mm-dd-yyyy or mm.dd.yyyy format
    func dateFromCommonDateFormats() -> NSDate? {
        let separator: String
        if (self.rangeOfString(".", options: .CaseInsensitiveSearch, range: nil, locale: nil) != nil) {
            separator = "."
        } else if (self.rangeOfString("/", options: .CaseInsensitiveSearch, range: nil, locale: nil) != nil) {
            separator = "/"
        } else {
            separator = "-"
        }
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "MM\(separator)dd\(separator)yyyy"
        let dob = dateFormatter.dateFromString(self)
        return dob
    }

}
