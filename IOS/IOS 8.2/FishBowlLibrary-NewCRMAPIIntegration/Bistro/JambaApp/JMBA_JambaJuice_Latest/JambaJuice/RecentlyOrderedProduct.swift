//
//  RecentlyOrderedProduct.swift
//  JambaJuice
//
//  Created by Taha Samad on 24/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse
import HDK

typealias RecentlyOrderedProductList = [RecentlyOrderedProduct]

struct RecentlyOrderedProduct {
    
    static let parseClassName = "RecentlyOrderedProduct"
    static let parseOrderKey = "lastOrderedTime"
    
    var chainProductId: Int64
    var lastOrderedTime: NSDate
    
    init(chainProductId: Int64, lastOrderedTime: NSDate) {
        self.chainProductId = chainProductId
        self.lastOrderedTime = lastOrderedTime
    }
    
    init(parseObject: PFObject) {
        chainProductId   = (parseObject["chainProductId"] as! NSNumber).longLongValue
        let timeInterval = parseObject["lastOrderedTime"] as! NSTimeInterval
        lastOrderedTime  = NSDate(timeIntervalSince1970: timeInterval)
    }
    
    func serializeAsParseObject() -> PFObject {
        let parseObject = PFObject(className: RecentlyOrderedProduct.parseClassName)
        parseObject["chainProductId"]  = NSNumber(longLong: chainProductId)
        parseObject["lastOrderedTime"] = lastOrderedTime.timeIntervalSince1970
        return parseObject
    }

}
