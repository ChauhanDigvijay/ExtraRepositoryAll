//
//  OrderStatus.swift
//  JambaJuice
//
//  Created by Taha Samad on 04/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct OrderStatus {
    
    //NOTE:
    //Only needed properties from OloOrderStatus are present here.
    //See OloOrderStatus for full list.
    var store: Store? //May or may not be present
    var id: String
    var timePlaced: NSDate?
    var vendorName: String
    var status: String
    var readyTime: NSDate?
    var total: Double
    var subTotal: Double
    var salesTax: Double
    var orderRef: String
    var vendorExtRef: String
    var products: [OrderStatusProduct]
    
    init(oloOrderStatus: OloOrderStatus) {
        id = oloOrderStatus.id
        //Convert to date
        var date = oloOrderStatus.timePlaced.dateFromOloDateTimeString()
        timePlaced = date
        //
        vendorName = oloOrderStatus.vendorName
        status = oloOrderStatus.status
        //Convert to date
        date = oloOrderStatus.readyTime.dateFromOloDateTimeString()
        readyTime = date
        //
        total = oloOrderStatus.total
        subTotal = oloOrderStatus.subTotal
        salesTax = oloOrderStatus.salesTax
        orderRef = oloOrderStatus.orderRef
        vendorExtRef = oloOrderStatus.vendorExtRef
        products = oloOrderStatus.products.map { OrderStatusProduct(oloOrderStatusProduct: $0) }
    }
    
    func commaSepratedNameOfProducts() -> String {
        let names = products.map { $0.name }
        return names.joinWithSeparator(", ")
    }
    
    func commaSepratedSpecialInstructionOfProducts() -> String {
        let specialInstructions = products.filter{ $0.specialInstructions.isEmpty == false }.map{ $0.specialInstructions }
        return specialInstructions.joinWithSeparator(", ")
    }
    
}
