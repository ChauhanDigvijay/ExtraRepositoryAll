//
//  OloBasketTransfer.swift
//  Pods
//
//  Created by vThink on 8/3/16.
// Copyright © 2016 Fishbowl, Inc. All rights reserved.
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
