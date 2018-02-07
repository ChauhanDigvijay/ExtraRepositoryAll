//
//  AuditOrderStatus.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 8/31/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

struct AuditOrder {

    static let parseClassName = "AuditOrder"
    
    fileprivate var basket: Basket
    fileprivate var orderStatus: OrderStatus
    fileprivate var user: User?
    
    init(basket: Basket, orderStatus: OrderStatus, user: User?) {
        self.basket = basket
        self.orderStatus = orderStatus
        self.user = user
    }
    
    func serializeAsParseObject() -> PFObject {
        let parseObject = PFObject(className: AuditOrder.parseClassName)

        // Order Details
        parseObject["orderId"]         = orderStatus.id
        parseObject["vendorName"]      = orderStatus.vendorName
        parseObject["vendorExtRef"]    = orderStatus.vendorExtRef
        parseObject["orderReadyTime"]  = orderStatus.readyTime ?? NSNull()
        parseObject["orderPlacedTime"] = orderStatus.timePlaced ?? NSNull()
        parseObject["orderTotal"]      = orderStatus.total
        parseObject["orderSubtotal"]   = orderStatus.subTotal
        parseObject["orderTax"]        = orderStatus.salesTax
        parseObject["productCount"]    = orderStatus.products.count
        
        // User details
        parseObject["userId"]           = user?.id ?? NSNull()
        parseObject["userEmailAddress"] = user?.emailAddress ?? NSNull()
        parseObject["userPhone"]        = user?.phoneNumber ?? NSNull()
        parseObject["userFirstName"]    = user?.firstName ?? NSNull()
        parseObject["userLastName"]     = user?.lastName ?? NSNull()
        parseObject["userDateOfBirth"]  = user?.dateOfBirth ?? NSNull()
    
        return parseObject
    }
    
}
