//
//  InCommOrderItemHelper.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/19/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import XCTest
import InCommSDK

class InCommOrderItemHelper {
    
    class func createOrderItemWithDiffrentiatingNumber(_ num: Int) -> InCommOrderItem {
        //Item
        var item = InCommOrderItem(brandId: "brandId\(num)", amount: Double(num), quantity: UInt32(num))
        item.embossText = "EmbossText\(num)"
        item.embossTextId = "EmbossTextId\(num)"
        item.expirationDate = Date()
        item.imageCode = "ImageCode\(num)"
        item.messageFrom = "MessageFrom\(num)"
        item.messageText = "EmbossText\(num)"
        item.messageTo = "EmbossTo\(num)"
        item.productId = 10 + Int32(num)
        item.productName = "ProductName\(num)"
        return item
    }
    
    class func testOrderItem(_ orderItem: InCommOrderItem) {
        testOrderItemJSONDict(orderItem.serializeAsJSONDictionary(), orderItem: orderItem)
    }
    
    class func testOrderItemJSONDict(_ orderItemJSONDict: InCommJSONDictionary, orderItem: InCommOrderItem) {
    }
}

