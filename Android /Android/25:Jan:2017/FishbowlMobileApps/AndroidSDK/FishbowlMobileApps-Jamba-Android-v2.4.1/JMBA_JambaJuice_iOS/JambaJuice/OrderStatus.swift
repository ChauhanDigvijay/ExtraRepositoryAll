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
    var timePlaced: Date?
    var vendorName: String
    var status: String
    var readyTime: Date?
    var total: Double
    var subTotal: Double
    var salesTax: Double
    var orderRef: String
    var vendorExtRef: String
    var products: [OrderStatusProduct]
    var deliveryAddress: OloDeliveryAddress?
    var deliveryMode: String?
    var deliveryStatus: OloSavedDeliverAdrdress?
    
    init(oloOrderStatus: OloOrderStatus) {
        id = oloOrderStatus.id
        //Convert to date
        var date        = oloOrderStatus.timePlaced.dateFromOloDateTimeString()
        timePlaced      = date
        //
        vendorName      = oloOrderStatus.vendorName
        status          = oloOrderStatus.status
        //Convert to date
        date            = oloOrderStatus.readyTime.dateFromOloDateTimeString()
        readyTime       = date
        //
        total           = oloOrderStatus.total
        subTotal        = oloOrderStatus.subTotal
        salesTax        = oloOrderStatus.salesTax
        orderRef        = oloOrderStatus.orderRef
        vendorExtRef    = oloOrderStatus.vendorExtRef
        products        = oloOrderStatus.products.map { OrderStatusProduct(oloOrderStatusProduct: $0) }
        deliveryMode    = oloOrderStatus.deliveryMode
        if oloOrderStatus.deliveryAddress != nil && oloOrderStatus.deliveryAddress?.zipcode != "" {
            deliveryAddress = oloOrderStatus.deliveryAddress
        }
    }
    
    func commaSepratedNameOfProducts() -> String {
        let names = products.map { $0.name }
        return names.joined(separator: ", ")
    }
    func truncateProductNames() -> String{
        let names = products.map { $0.name }
        var name = ""
        if names.count == 0{
            name = "-No Products-"
        }
        else if(names.count==1){
            name = names[0]
        }else if(names.count>1){
            name = "\(names[0]), ..."
        }
        return name
    }
    func commaSepratedSpecialInstructionOfProducts() -> String {
        let specialInstructions = products.filter{ $0.specialInstructions.isEmpty == false }.map{ $0.specialInstructions }
        return specialInstructions.joined(separator: ", ")
    }
    
    //get delivery Address
    func getDeliveryAddress() -> String {
        var addressInString = ""
        //get address

        if let address = self.deliveryAddress {
            if address.zipcode != "" {
                addressInString = "\(address.streetaddress)\n\(address.city), \(address.zipcode)"
                return addressInString
            }
        }
        return "Delivery address is not available"
    }
}
