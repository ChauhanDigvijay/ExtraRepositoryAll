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
    static let recentOrderLimit = 2
    
    var chainProductId: Int64
    var lastOrderedTime: Date
    
    init(chainProductId: Int64, lastOrderedTime: Date) {
        self.chainProductId = chainProductId
        self.lastOrderedTime = lastOrderedTime
    }
    
    init(parseObject: PFObject) {
        chainProductId   = (parseObject["chainProductId"] as! NSNumber).int64Value
        let timeInterval = parseObject["lastOrderedTime"] as! TimeInterval
        lastOrderedTime  = Date(timeIntervalSince1970: timeInterval)
    }
    
    func serializeAsParseObject() -> PFObject {
        let parseObject = PFObject(className: RecentlyOrderedProduct.parseClassName)
        parseObject["chainProductId"]  = NSNumber(value: chainProductId as Int64)
        parseObject["lastOrderedTime"] = lastOrderedTime.timeIntervalSince1970
        return parseObject
    }

}
