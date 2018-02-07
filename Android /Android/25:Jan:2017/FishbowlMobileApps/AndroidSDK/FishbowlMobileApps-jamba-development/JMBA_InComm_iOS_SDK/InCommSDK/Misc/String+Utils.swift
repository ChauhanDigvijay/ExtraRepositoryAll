//
//  String+Utils.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/7/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

extension String {

    //2015-08-18T14:17:56.9200Z
    /// Return NSDate from InComm DateTime format
    public func dateFromInCommFormatString() -> NSDate? {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSZ"
        return dateFormatter.dateFromString(self)
    }
}