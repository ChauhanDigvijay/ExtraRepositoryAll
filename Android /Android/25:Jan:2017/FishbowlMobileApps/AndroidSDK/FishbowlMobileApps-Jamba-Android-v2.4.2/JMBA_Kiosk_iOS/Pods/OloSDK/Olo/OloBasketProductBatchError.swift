//
//  OloBasketProductBatchError.swift
//  Olo
//
//  Created by Taha Samad on 05/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloBasketProductBatchError {

    public var productId: Int64
    public var message: String
    
    public init(json: JSON) {
        productId = json["productid"].int64Value
        message   = json["message"].stringValue
    }

}
