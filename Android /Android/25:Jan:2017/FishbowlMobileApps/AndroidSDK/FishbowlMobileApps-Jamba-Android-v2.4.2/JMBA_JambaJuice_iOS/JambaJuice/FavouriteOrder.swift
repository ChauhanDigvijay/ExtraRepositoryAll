//
//  FavouriteOrder.swift
//  JambaJuice
//
//  Created by VT016 on 16/03/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct FavouriteOrder {
    
    //NOTE:
    //Only needed properties from OloFave are present here.
    //See OloFave for full list.
    var store: Store? //May or may not be present
    var id: Int64
    var name: String
    var vendorName: String
    var vendorId: Int64
    var disabled:Bool?
    var online:Bool?
    var total: Double?
    var subTotal: Double?
    var salesTax: Double?
    var deliveryCharge: Double?
    var products: [OrderStatusProduct] = []
    
    init(oloFave: OloFave) {
        id          = oloFave.id
        name        = oloFave.name
        vendorName  = oloFave.vendorName
        vendorId    = oloFave.vendorId
        disabled    = oloFave.disabled
        online      = oloFave.online
    }
    
    mutating func updateProducts(_ oloBasket: OloBasket) {
        self.total           = oloBasket.total
        self.subTotal        = oloBasket.subTotal
        self.salesTax        = oloBasket.salesTax
        self.deliveryCharge  = oloBasket.customerhandoffcharge
        self.products        = oloBasket.products.map { OrderStatusProduct(oloBasket: $0) }
    }
    
    func commaSepratedNameOfProducts() -> String {
        let names = products.map { $0.name }
        return names.joined(separator: ", ")
    }
    func truncateProductNames() -> String{
        let names = products.map { $0.name }
        var name = ""
        if(names.count == 1 && names.count <= 1){
            name = names[0]
        }else if(names.count>1){
            name = "\(names[0]), ..."
        }
        return name
    }
//    func commaSepratedSpecialInstructionOfProducts() -> String {
//        let specialInstructions = products.filter{ $0.specialInstructions.isEmpty == false }.map{ $0.specialInstructions }
//        return specialInstructions.joinWithSeparator(", ")
//    }
    
}
