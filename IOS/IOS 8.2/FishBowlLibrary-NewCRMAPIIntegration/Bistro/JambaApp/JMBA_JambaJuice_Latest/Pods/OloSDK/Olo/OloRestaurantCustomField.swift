//
//  OloRestaurantCustomField.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/24/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

// OLO Restaurant Custom Field
//
//    {
//        id (int): ,
//        label (string): ,
//        required (bool): ,
//        validationregex (string): ,
//        qualificationcriteria (string):
//    }

import UIKit
import SwiftyJSON

public struct OloRestaurantCustomField {

    public var id: Int64
    public var label: String
    public var required: Bool
    public var validationRegex: String
    public var qualificationCriteria: String
    
    public init(json: JSON) {
        id                    = json["id"].int64Value
        label                 = json["label"].stringValue
        required              = json["required"].boolValue
        validationRegex       = json["validationregex"].stringValue
        qualificationCriteria = json["qualificationcriteria"].stringValue
    }
    
}
