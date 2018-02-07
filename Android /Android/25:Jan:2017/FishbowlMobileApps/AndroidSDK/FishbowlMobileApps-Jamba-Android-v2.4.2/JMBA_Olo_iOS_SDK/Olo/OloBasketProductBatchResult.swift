//
//  OloBasketProductBatchResult.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

open class OloBasketProductBatchResult {
    
    open var basket: OloBasket
    open var errors: [OloBasketProductBatchError]
    
    public init(json: JSON) {
        basket = OloBasket(json: json["basket"])
        errors = json["errors"].arrayValue.map { item in OloBasketProductBatchError(json: item) }
    }
    
}
