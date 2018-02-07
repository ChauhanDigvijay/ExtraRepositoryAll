//
//  OloBillingScheme.swift
//  Pods
//
//  Created by Taha Samad on 6/23/15.
//
//

import Foundation
import SwiftyJSON

public struct OloBillingScheme {
    
    public var id: Int64
    public var name: String
    public var type: String
    public var accounts: [OloBillingAccount]
    
    public init(json: JSON) {
        id       = json["id"].int64Value
        name     = json["name"].stringValue
        type     = json["type"].stringValue
        accounts = json["accounts"].arrayValue.map { accountJSON in OloBillingAccount(json:accountJSON) }
    }

}
