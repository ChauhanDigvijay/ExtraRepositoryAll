//
//  OloCategory.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/22/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

// OLO Product Category
//
//    {
//        id (int): ,
//        name (string): ,
//        description (string): ,
//        products (array[product]):
//    }

import UIKit
import SwiftyJSON

public struct OloCategory {

    public var id: Int64
    public var name: String
    public var desc: String
    public var products: [OloProduct]

    public init(json: JSON) {
        id       = json["id"].int64Value
        name     = json["name"].stringValue
        desc     = json["description"].stringValue
        products = json["products"].arrayValue.map { item in OloProduct(json: item) }
    }
    
}
