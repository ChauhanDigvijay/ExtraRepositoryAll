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

    var modifierId: Int64
    var name: String
    var mandatory: Bool
    var minSelects: Int64?
    var maxSelects: Int64?
    var options: [StoreMenuProductModifierOption]
    var optionIdForUnnestedOption: Int64?

    init (oloModifier: OloModifier) {
        modifierId = oloModifier.id
        //Make name more appropiate for our app
        var modifierName = oloModifier.desc
        if modifierName.lowercaseString.rangeOfString("add whole food boost") != nil {
            modifierName = "Add Whole Food Boosts"
        } else if modifierName.lowercaseString.rangeOfString("add boost") != nil {
            modifierName = "Add Boosts"
        } else if modifierName.hasSuffix(":") || modifierName.hasSuffix("?") {
            modifierName = modifierName.substringToIndex(modifierName.endIndex.predecessor())
        }
        name = modifierName
        mandatory = oloModifier.mandatory
        if !oloModifier.minSelects.isEmpty {
            minSelects = (oloModifier.minSelects as NSString).longLongValue
        }
        if !oloModifier.maxSelects.isEmpty {
            maxSelects = (oloModifier.maxSelects as NSString).longLongValue
        }
        options = oloModifier.options.map { StoreMenuProductModifierOption(oloOption: $0) }
    }

    func isASimpleSizeModifier() -> Bool {
        return name.lowercaseString.hasPrefix("select size")
    }

    func isASmoothieTypeAndSizeModifier() -> Bool {
        return name.lowercaseString.hasPrefix("select smoothie type")
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
    private func hasNeedlesslyNestedModifiers() -> Bool {
        return options.count == 1 && options[0].modifiers.count > 0
    }

    //Only does it to first level
    func returnModifiersByRemovingNeedlessNesting() -> [StoreMenuProductModifier] {
        if hasNeedlesslyNestedModifiers() {
            var modifiers: [StoreMenuProductModifier] = []
            for i in 0..<options[0].modifiers.count {
                var modifier = options[0].modifiers[i]
                modifier.optionIdForUnnestedOption = options[0].modifierId
                modifiers.append(modifier)
            }
            return modifiers
        }
        return [self]
    }

    func classicOptionId() -> Int64? {
        if isASmoothieTypeAndSizeModifier() {
            for option in options {
                if option.name.lowercaseString.hasPrefix("classic") {
                    return option.modifierId
                }
            }
        }
        return nil
    }

    func makeItLightOptionId() -> Int64? {
        if isASmoothieTypeAndSizeModifier() {
            for option in options {
                if option.name.lowercaseString.hasPrefix("make it light") {
                    return option.modifierId
                }
            }
        }
        return nil
    }

}