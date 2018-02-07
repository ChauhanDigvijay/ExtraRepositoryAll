//
//  BasketChoice.swift
//  JambaJuice
//
//  Created by Taha Samad on 27/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct BasketChoice {

    var basketChoiceId: Int64
    var optionId: Int64
    var name: String
    //var metric: Int64
    //var indent: Int64
    //var cost: Double
    //var customFields: [OloChoiceCustomFieldValue]

    init(oloBasketChoice: OloBasketChoice) {
        basketChoiceId = oloBasketChoice.id
        optionId       = oloBasketChoice.optionId
        name           = oloBasketChoice.name
    }

}
