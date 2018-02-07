//
//  UpSellConfig.swift
//  JambaJuice
//
//  Created by VT010 on 10/21/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import Parse


struct UpSellConfig {
    static let parseClassName = "UpsellConfig"
    static var rotation_interval = 20
    var rotation_int: Int
    init(parseObject: PFObject) {
        rotation_int = (parseObject["rotation_interval"] as? NSNumber)?.intValue ?? UpSellConfig.rotation_interval
    }
    
    init(){
        rotation_int = UpSellConfig.rotation_interval
    }
}
func == (lhs: UpSellConfig, rhs: UpSellConfig) -> Bool {
    return lhs.rotation_int == rhs.rotation_int
}



