//
//  StoreMenuProductModifierOption.swift
//  JambaJuice
//
//  Created by Taha Samad on 02/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct StoreMenuProductModifierOption {

    var modifierId: Int64
    var name: String
    var cost: Double
    var modifiers: [StoreMenuProductModifier]

    init(oloOption: OloOption) {
        modifierId = oloOption.id
        name = oloOption.name
        cost = oloOption.cost
        modifiers = oloOption.modifiers.map { oloModifier in StoreMenuProductModifier(oloModifier: oloModifier) }
    }

}
