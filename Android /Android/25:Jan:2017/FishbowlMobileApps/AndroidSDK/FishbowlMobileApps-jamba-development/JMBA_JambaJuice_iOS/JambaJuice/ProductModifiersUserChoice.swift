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
    //ModifierId: Set of optionId & quantity
    var selectedOptionIdsForModifierId = [Int64: Set<ProductModifiersMaxChoiceModel>]()
    var selectedQuantityForModifierId = [Int64: Int]()
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
                for optionIds in selectedOptionIds {
                    if optionIds.choiceId == optionId {
                        return true
                    }
                }
            }
        }
        return false
    }
    
    //check option is available in the array. if it is available then return the quantity of the option
    func isOptionIdSelectedGetQuantity(optionId: Int64) -> Int {
        if optionId == selectedTypeOptionId {
            return 1
        }
        else if optionId == selectedSizeOptionId {
            return 1
        }
        else {
            for (_, selectedOptionIds) in selectedOptionIdsForModifierId {
                for optionIds in selectedOptionIds {
                    if optionIds.choiceId == optionId {
                        return optionIds.quantity
                    }
                }
            }
        }
        return 0
    }
    
    //get the total quantity selected by a user in a particular modifier
    func getTotalQuantity(modifierId: Int64) -> Int {
        var totalQuantity = 0
        if selectedOptionIdsForModifierId[modifierId] == nil {
            return 0
        }
        for selectedOptionIds in selectedOptionIdsForModifierId[modifierId]! {
            totalQuantity = totalQuantity + selectedOptionIds.quantity
        }
        return totalQuantity - 1    //-1 for parent option
    }
    
    //get the total quantity selected by a user in a particular modifier without that option
    func getTotalQuantityWithOptionId(modifierId: Int64, optionId: Int64) -> Int {
        var totalQuantity = 0
        if selectedOptionIdsForModifierId[modifierId] == nil {
            return 0
        }
        for selectedOptionIds in selectedOptionIdsForModifierId[modifierId]! {
            if selectedOptionIds.choiceId != optionId {
                totalQuantity = totalQuantity + selectedOptionIds.quantity
            }
        }
        return totalQuantity - 1    //-1 for parent option
    }
    
    //get the total quantity selected by a user in a particular modifier
    func updateQuantity(optionId: Int64, quantity:Int) -> Bool {
        for (_, selectedOptionIds) in selectedOptionIdsForModifierId {
            for optionIds in selectedOptionIds {
                if optionIds.choiceId == optionId {
                    optionIds.quantity = quantity
                    return true
                }
            }
        }
        return false
    }
    
    //get the total quantity selected by a user in a particular modifier option
    func getQuantityForAOption(optionId: Int64) -> Int {
        for (_, selectedOptionIds) in selectedOptionIdsForModifierId {
            for optionIds in selectedOptionIds {
                if optionIds.choiceId == optionId {
                    return optionIds.quantity
                }
            }
        }
        return 1
    }
    
    mutating func addSelectedOptionIdForModifierId(modifierId: Int64, optionId: Int64) {
        if selectedOptionIdsForModifierId[modifierId] == nil {
            selectedOptionIdsForModifierId[modifierId] = Set<ProductModifiersMaxChoiceModel>()
        }
        let productModifiersMaxChoiceModel = ProductModifiersMaxChoiceModel(choiceId: optionId, quantity: 1)
        selectedOptionIdsForModifierId[modifierId]!.insert(productModifiersMaxChoiceModel)
        //        selectedOptionIdsForModifierId[modifierId]!.insert(optionId)
    }
    
    mutating func removeSelectedOptionIdForModifierId(modifierId: Int64, optionId: Int64) {
        if let temp = selectedOptionIdsForModifierId[modifierId] {
            selectedOptionIdsForModifierId[modifierId] = Set<ProductModifiersMaxChoiceModel>()
            for choice in temp {
                if choice.choiceId != optionId {
                    let productModifiersMaxChoiceModel = ProductModifiersMaxChoiceModel(choiceId: choice.choiceId, quantity: choice.quantity)
                    selectedOptionIdsForModifierId[modifierId]!.insert(productModifiersMaxChoiceModel)
                }
            }
            //            selectedOptionIdsForModifierId[modifierId]!.remove(productModifiersMaxChoiceModel)
        }
    }
    
}
