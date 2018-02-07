//
//  ProductModifiersUserChoice.swift
//  JambaJuice
//
//  Created by Taha Samad on 08/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

struct ProductModifiersUserChoice {
    
    var selectedTypeOptionId: Int64?
    var selectedSizeOptionId: Int64?
    var selectedCustomizeOptionIds: [Int64]  = []
    
    var quantity = 1
    //ModifierId: Set of optionId
    var selectedOptionIdsForModifierId = [Int64: Set<Int64>]()
    var specialInstructions = ""
    
    func isOptionIdSelected(optionId: Int64) -> Bool {
        if optionId == selectedTypeOptionId {
            return true
        }
        else if optionId == selectedSizeOptionId {
            return true
        }
        else {
            for (_, selectedOptionIds) in selectedOptionIdsForModifierId {
                if selectedOptionIds.contains(optionId) {
                    return true
                }
            }
        }
        return false
    }

    mutating func addSelectedOptionIdForModifierId(modifierId: Int64, optionId: Int64) {
        if selectedOptionIdsForModifierId[modifierId] == nil {
            selectedOptionIdsForModifierId[modifierId] =  Set<Int64>()
        }
        selectedOptionIdsForModifierId[modifierId]!.insert(optionId)
    }

    mutating func removeSelectedOptionIdForModifierId(modifierId: Int64, optionId: Int64) {
        if selectedOptionIdsForModifierId[modifierId] != nil {
            selectedOptionIdsForModifierId[modifierId]!.remove(optionId)
        }
    }
    
}
