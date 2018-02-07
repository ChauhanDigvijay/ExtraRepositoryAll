//
//  UpSellProductsImages.swift
//  JambaJuice
//
//  Created by VT010 on 10/21/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

struct UpSellProductsImages {
    static let parseClassName = "Upsell"
    var defaultImageURL: String
    var campaignImageURL: String
    
    init(parseObject: PFObject) {
        defaultImageURL   = parseObject["default_image_url"] as? String ?? ""
        campaignImageURL  = parseObject["campaign_image_url"] as? String ?? ""
    }
}

