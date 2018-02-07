//
//  UpSellClass.swift
//  JambaJuice
//
//  Created by VT010 on 10/21/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import Parse
import HDK



struct UpsellConfig {
    
    static let parseClassName = "UpsellConfig"
    static var rotation_interval = 20
    
    var id: String
    var rotation_int: Int
    var adsDetailList: AdDetailList
    
    init(parseObject: PFObject) {
        id = parseObject.objectId!
        rotation_int = (parseObject["rotation_interval"] as? NSNumber)?.intValue ?? AdClass.rotation_interval
        adsDetailList = []
    }
    
    init() {
        id = ""
        rotation_int = AdClass.rotation_interval
        adsDetailList = []
    }
}

func == (lhs: AdClass, rhs: AdClass) -> Bool {
    return lhs.rotation_int == rhs.rotation_int
}

