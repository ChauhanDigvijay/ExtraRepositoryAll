//
//  ProductModifiersMaxChoiceModel.swift
//  JambaJuice
//
//  Created by vThink Technologies on 21/11/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation

class ProductModifiersMaxChoiceModel: Hashable {
    var choiceId    : Int64                             = 0
    var quantity    : Int                               = 0
    var hashValue: Int {
        return self.quantity
    }
    
    init(choiceId:Int64,quantity:Int) {
        self.choiceId = choiceId
        self.quantity = quantity
    }
}
func ==(lhs: ProductModifiersMaxChoiceModel, rhs: ProductModifiersMaxChoiceModel) -> Bool {
    return lhs.choiceId == rhs.choiceId
}
