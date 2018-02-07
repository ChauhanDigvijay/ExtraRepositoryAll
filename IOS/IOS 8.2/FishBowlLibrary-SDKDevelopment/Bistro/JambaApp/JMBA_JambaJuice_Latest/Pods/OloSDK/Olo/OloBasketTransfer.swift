//
//  OloBasketTransfer.swift
//  Pods
//
//  Created by VT010 on 8/3/16.
//
//


import Foundation
import SwiftyJSON


public struct OloBasketTransfer {
    
    public var basket: OloBasket
    public var itemsnottransferred: [String]
    
    public init(json: JSON) {
        basket = OloBasket(json: json["basket"])
        itemsnottransferred = json["itemsnottransferred"].arrayValue.map { item in String(item) }
    }

}
