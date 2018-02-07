//
//  NSDate+Utils.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/7/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

extension Date {
    
    //2015-08-18T14:17:56.9200Z
    public func InCommDateTimeFormatString() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")!
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'"
        return dateFormatter.string(from: self)
    }

}
