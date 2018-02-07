//
//  OloProduct.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/22/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

//  OLO Product
//    {
//        id (int): ,
//        chainproductid (int): ,
//        name (string): ,
//        description (string): ,
//        cost (float): ,
//        basecalories (string): ,
//        maxcalories (string): ,
//        displayid (string): ,
//        starthour (float): ,
//        endhour (float): ,
//        imagefilename (string):
//    }

import UIKit
import SwiftyJSON

public struct OloProduct {

    public var id: Int64
    public var chainProductId: Int64
    public var name: String
    public var desc: String
    public var cost: Double
    public var baseCalories: String
    public var maxCalories: String
    public var displayId: String
    public var startHour: Double
    public var endHour: Double
    public var imageFilename: String
    
    public init(json: JSON) {
        id              = json["id"].int64Value
        chainProductId  = json["chainproductid"].int64Value
        name            = json["name"].stringValue
        desc            = json["description"].stringValue
        cost            = json["cost"].doubleValue
        baseCalories    = json["basecalories"].stringValue
        maxCalories     = json["maxcalories"].stringValue
        displayId       = json["displayid"].stringValue
        startHour       = json["starthour"].doubleValue
        endHour         = json["endhour"].doubleValue
        imageFilename   = json["imagefilename"].stringValue
    }
    
}
