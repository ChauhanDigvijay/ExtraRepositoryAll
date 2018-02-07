//
//  StoreMenuProductModifier.swift
//  JambaJuice
//
//  Created by Taha Samad on 02/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

struct StoreMenuProductModifier {

    var id: Int64
    var name: String
    var mandatory: Bool
    var minSelects: Int64?
    var maxSelects: Int64?
    var minChoiceQuantity: Int64?
    var maxChoiceQuantity: Int64?
    var maxAggregateQuantity: Int64?
    var choiceQuantityIncrement: Int64?
    var supportsChoiceQuantities: Bool?
    var options: [StoreMenuProductModifierOption]
    var optionIdForUnnestedOption: Int64?
    
    let customizeModifierText: String = "not perfect yet"
    let customizeModifierTextSandbox: String = "would you like to customize more" // sandbox server
    let addBoostModifierName: String = "Add Boosts"
    
    init (oloModifier: OloModifier) {
        id = oloModifier.id
        //Make name more appropiate for our app
        var modifierName = oloModifier.desc
        if modifierName.lowercased().range(of:"add whole food boost") != nil {
            modifierName = "Add Whole Food Boosts"
        }
        else if modifierName.lowercased().range(of:"Select Protein Boost:") != nil {
            modifierName = "Select Protein Boost"
        }
        else if modifierName.lowercased().range(of:"add boost") != nil {
            modifierName = addBoostModifierName
        }
        else if modifierName.hasSuffix(":") || modifierName.hasSuffix("?") {
//            modifierName = modifierName.substring(to:modifierName.endIndex.predecessor())
            modifierName = String(modifierName[..<modifierName.index(before: modifierName.endIndex)])
        }
        name = modifierName
        mandatory = oloModifier.mandatory
        if !oloModifier.minSelects.isEmpty {
            minSelects = (oloModifier.minSelects as NSString).longLongValue
        }
        if !oloModifier.maxSelects.isEmpty {
            maxSelects = (oloModifier.maxSelects as NSString).longLongValue            
        }
        if !oloModifier.minChoiceQuantity.isEmpty {
            minChoiceQuantity = (oloModifier.minChoiceQuantity as NSString).longLongValue
        }
        if !oloModifier.maxChoiceQuantity.isEmpty {
            maxChoiceQuantity = (oloModifier.maxChoiceQuantity as NSString).longLongValue
        }
        if !oloModifier.maxAggregateQuantity.isEmpty {
            maxAggregateQuantity = (oloModifier.maxAggregateQuantity as NSString).longLongValue
        }
        if !oloModifier.choiceQuantityIncrement.isEmpty {
            choiceQuantityIncrement = (oloModifier.choiceQuantityIncrement as NSString).longLongValue
        }
        supportsChoiceQuantities = oloModifier.supportsChoiceQuantities
        options = oloModifier.options.map { StoreMenuProductModifierOption(oloOption: $0) }
    }
    
    func isASimpleSizeModifier() -> Bool {
        return name.lowercased().hasPrefix("select size")
    }
        
    func isASmoothieTypeAndSizeModifier() -> Bool {
        return name.lowercased().hasPrefix("select smoothie type")
    }
    
    func hasSizeModifierOn2ndLevel() -> Bool {
        if isASimpleSizeModifier() {
            return false
        }
        for option in options {
            for secondLevelModifier in option.modifiers {
                if secondLevelModifier.isASimpleSizeModifier() {
                    return true
                }
            }
        }
        return false
    }
    
    func hasSizeModifierOn2ndLevelButNotMakeItLightChoice() -> Bool {
        return hasSizeModifierOn2ndLevel() && !isASmoothieTypeAndSizeModifier()
    }
    
    func isTopLevelTypeOrSizeModifier() -> Bool {
        return isASimpleSizeModifier() || hasSizeModifierOn2ndLevel()
    }
    
    //See case of steel-cut oatmeal
    fileprivate func hasNeedlesslyNestedModifiers() -> Bool {
        //if it is a 3rd level modifier and modier name is "add yummy extras" or "customize it"
        if (isModifierNameIsAddYummyExtras() || isModifierNameIsCustomizeIt()) {
            return false
        }
        return options.count == 1 && options[0].modifiers.count > 0
    }
    
    //Only does it to first level
    func returnModifiersByRemovingNeedlessNesting() -> [StoreMenuProductModifier] {
        if hasNeedlesslyNestedModifiers() {
            var modifiers: [StoreMenuProductModifier] = []
            for i in 0..<options[0].modifiers.count {
                var modifier = options[0].modifiers[i]
                modifier.optionIdForUnnestedOption = options[0].id
                modifiers.append(modifier)
                modifiers[i].name=name
            }
            return modifiers
        }
        return [self]
    }
    
    func classicOptionId() -> Int64? {
        if isASmoothieTypeAndSizeModifier() {
            for option in options {
                if option.name.lowercased().hasPrefix("classic") {
                    return option.id
                }
            }
        }
        return nil
    }
    
    func makeItLightOptionId() -> Int64? {
        if isASmoothieTypeAndSizeModifier() {
            for option in options {
                if option.name.lowercased().hasPrefix("make it light") {
                    return option.id
                }
            }
        }
        return nil
    }
    
    //check it is add yummy extra modifer
    func isModifierNameIsAddYummyExtras() ->Bool {
        if (name.lowercased().hasPrefix("add yummy extras")) {
            return true
        }
        return false
    }
    
    //check it is customize it modifer
    func isModifierNameIsCustomizeIt() ->Bool {
        if (name.lowercased().hasPrefix(customizeModifierText)
            ||
            (name.lowercased().hasPrefix(customizeModifierTextSandbox))
            ) {
            return true
        }
        return false
    }
    
    func isAddBoostModifier() -> Bool {
        return (name == addBoostModifierName)        
    }
    
    func isAddToppingModifier() -> Bool {
        return name.lowercased().hasPrefix("add topping")
    }
}
