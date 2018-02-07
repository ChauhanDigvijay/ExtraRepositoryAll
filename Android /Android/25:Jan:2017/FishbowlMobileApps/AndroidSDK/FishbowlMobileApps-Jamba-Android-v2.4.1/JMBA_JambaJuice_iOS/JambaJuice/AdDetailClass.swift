//
//  AdProducts.swift
//  JambaJuice
//
//  Created by vThink Technologies on 12/01/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import Parse
import HDK

typealias AdDetailList = [AdDetailClass]

struct AdDetailClass {
    
    static let parseClassName = "AdDetail"
    static let parseOrderKey = "order_no"
    static let parseStatusKey = "status"
    static let parseStoreCodekey = "store_code"
    
    var productId: Int64
    var producturl: String
    var adName: String
    var order_no: Int
    var image_url: String
    var storeCode: String
    var adStatus: Bool
    var adClass: AdClass
    var categoryId: String
    
    init(parseObject: PFObject) {
        productId           = (parseObject["product_id"] as? NSNumber)?.int64Value ?? 0
        producturl          = parseObject["link_url"] as? String ?? ""
        adName              = parseObject["ad_name"] as? String ?? ""
        order_no            = (parseObject["order_no"] as? NSNumber)?.intValue ?? 0
        image_url           = parseObject["image_url"] as? String ?? ""
        storeCode           = parseObject["store_code"] as? String ?? ""
        adStatus            = parseObject["status"] as? Bool ?? false
        adClass             = AdClass(parseObject: parseObject["Ad"] as! PFObject)
        if parseObject["category"] != nil{
            categoryId = (parseObject["category"] as AnyObject).objectId ?? ""
        }else{
            categoryId = ""
        }
    }
}
