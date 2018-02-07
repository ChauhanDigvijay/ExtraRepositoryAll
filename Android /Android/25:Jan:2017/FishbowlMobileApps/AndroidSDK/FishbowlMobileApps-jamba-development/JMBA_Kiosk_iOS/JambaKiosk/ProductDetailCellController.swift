//
//  ProductDetailCellController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/11/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

protocol ProductDetailCellControllerDelegate: MakeItLightTableViewCellDelegate {
    func selectedTypeOptionIdFromUserChoice() -> Int64?
    func isOptionSelectedForUserChoice(optionId: Int64) -> Bool
}

typealias ModifierAndOptionIndices = (modifierIndex: Int, optionIndex: Int)

class ProductDetailCellController {

    var product: Product?
    var delegate: ProductDetailCellControllerDelegate?

    private var typeCatExpanded: Bool = true
    private var modifierIdsWithExpandedOptions = Set<Int64>()

    func cellForRowAtIndexPath(tableView: UITableView, indexPath: NSIndexPath) -> UITableViewCell? {
        if indexPath.row == rowIndexForMakeItLightTableViewCell() {
            return createMakeItLightTableViewCell(tableView, indexPath: indexPath)
        } else if indexPath.row == rowIndexForTypeCategoryCell() {
            let modifierIndex = -2
            guard let modifier = product?.storeMenuProduct?.productTypeAndSizeTopLevelModifier else {
                fatalError("Could not get modifier")
            }
            return createBoostCategoryTableViewCell(tableView, indexPath: indexPath, modifier: modifier, modifierIndex: modifierIndex)
        } else if indexPath.row <= maxRowIndexForTypeCell() {
            let modifierIndex = -2
            let optionIndex = indexPath.row  - 1
            guard let modifier = product?.storeMenuProduct?.productTypeAndSizeTopLevelModifier else {
                fatalError("Could not get modifier")
            }
            return createBoostTableViewCell(tableView, indexPath: indexPath, modifier: modifier, modifierIndex: modifierIndex, optionIndex: optionIndex)
        }

        let modifierAndOptionIndices = modifierAndOptionIndexFromIndexPath(indexPath)
        // If modifier index is valid
        if modifierAndOptionIndices.modifierIndex >= 0 {
            // If option index is valid
            if modifierAndOptionIndices.optionIndex >= 0 {
                return createBoostTableViewCell(tableView, indexPath: indexPath, modifierAndOptionIndices: modifierAndOptionIndices)
            } else {
                return createBoostCategoryTableViewCell(tableView, indexPath: indexPath, modifierAndOptionIndices: modifierAndOptionIndices)
            }
        }
        return nil
    }

    private func createMakeItLightTableViewCell(tableView: UITableView, indexPath: NSIndexPath) -> MakeItLightTableViewCell {
        guard let cell = tableView.dequeueReusableCellWithIdentifier("MakeItLightTableViewCell", forIndexPath: indexPath) as? MakeItLightTableViewCell else {
            fatalError("Failed to load cell")
        }
        cell.switchControl.on = makeItLightSelected()
        cell.selectionStyle = .None
        cell.delegate = delegate
        return cell
    }

    private func makeItLightSelected() -> Bool {
        let makeItLightOptionId = product?.storeMenuProduct?.productTypeAndSizeTopLevelModifier?.makeItLightOptionId()
        let selectedTypeOptionId = delegate?.selectedTypeOptionIdFromUserChoice()
        return makeItLightOptionId != nil && makeItLightOptionId == selectedTypeOptionId
    }

    private func createBoostCategoryTableViewCell(tableView: UITableView, indexPath: NSIndexPath, modifier: StoreMenuProductModifier, modifierIndex: Int) -> BoostCategoryTableViewCell {
        guard let cell = tableView.dequeueReusableCellWithIdentifier("BoostCategoryTableViewCell", forIndexPath: indexPath) as? BoostCategoryTableViewCell else {
            fatalError("Failed to load cell")
        }
        populateBoostCategoryCellWithModifier(cell, modifier: modifier, modifierIndex: modifierIndex)
        cell.layoutIfNeeded() // Make cell label take proper dimensions
        return cell
    }

    func createBoostCategoryTableViewCell(tableView: UITableView, indexPath: NSIndexPath, modifierAndOptionIndices: ModifierAndOptionIndices) -> BoostCategoryTableViewCell {
        guard let modifier = product?.storeMenuProduct?.productModifiers[modifierAndOptionIndices.modifierIndex] else {
            fatalError("Could not get modifier")
        }
        return createBoostCategoryTableViewCell(tableView, indexPath: indexPath, modifier: modifier, modifierIndex: modifierAndOptionIndices.modifierIndex)
    }

    private func createBoostTableViewCell(tableView: UITableView, indexPath: NSIndexPath, modifier: StoreMenuProductModifier, modifierIndex: Int, optionIndex: Int) -> BoostTableViewCell {
        guard let cell = tableView.dequeueReusableCellWithIdentifier("BoostTableViewCell", forIndexPath: indexPath) as? BoostTableViewCell else {
            fatalError("Failed to load cell")
        }
        populateBoostCellWithModifier(cell, modifier: modifier, modifierIndex: modifierIndex, optionIndex: optionIndex)
        cell.layoutIfNeeded() // Make cell label take proper dimensions
        return cell
    }

    private func createBoostTableViewCell(tableView: UITableView, indexPath: NSIndexPath, modifierAndOptionIndices: ModifierAndOptionIndices) -> BoostTableViewCell {
        guard let modifier = product?.storeMenuProduct?.productModifiers[modifierAndOptionIndices.modifierIndex] else {
            fatalError("Could not get modifier")
        }
        return createBoostTableViewCell(tableView, indexPath: indexPath, modifier: modifier, modifierIndex: modifierAndOptionIndices.modifierIndex, optionIndex: modifierAndOptionIndices.optionIndex)
    }

    func toggleBoostCategoryExpanded() {
        typeCatExpanded = !typeCatExpanded
    }

    func addRemoveModifierIdWithExpandedOptions(modifier: StoreMenuProductModifier) {
        if modifierIdsWithExpandedOptions.contains(modifier.modifierId) {
            modifierIdsWithExpandedOptions.remove(modifier.modifierId)
        } else {
            modifierIdsWithExpandedOptions.insert(modifier.modifierId)
        }
    }


    func modifierAndOptionIndexFromIndexPath(indexPath: NSIndexPath) -> ModifierAndOptionIndices {
        //Number of rows need by each modifier accounting for expanded state. 1 if not expanded.
        let arrayOfNumberOfRowsForEachModif = arrayOfNumberOfRowsForEachModifier()
        //If array.count > 0, which means we have modifiers to deal with
        if arrayOfNumberOfRowsForEachModif.count > 0 {
            //First Modifier start index depending upon make it light row is present or not.
            var startIndexOfCurrentModifier = 0
            let num = numberOfRowsForTypeSection()
            if  num > 0 {
                startIndexOfCurrentModifier = num
            } else if needsRowForMakeItLightTableViewCell() {
                startIndexOfCurrentModifier = 1
            }
            //Index of Current Modifier in Array. Start from 0
            var currentModifierIndex = 0
            //Loop one by one.
            for numberOfRowsForModif in arrayOfNumberOfRowsForEachModif {
                //Start Index of next modifier
                let startIndexOfNextModifier = startIndexOfCurrentModifier + numberOfRowsForModif
                if indexPath.row == startIndexOfCurrentModifier {
                    //If we are exactly at the start of modifier cells
                    return (modifierIndex: currentModifierIndex, optionIndex: -1)
                } else if indexPath.row > startIndexOfCurrentModifier && indexPath.row <  startIndexOfNextModifier {
                    //Is our indexPath below the next modifiers index.
                    //Now calculate index of option with-in modifier.
                    return (modifierIndex: currentModifierIndex, optionIndex: indexPath.row - startIndexOfCurrentModifier - 1)
                }
                //Update for next iteration
                currentModifierIndex++
                startIndexOfCurrentModifier = startIndexOfNextModifier
            }
        }
        return (modifierIndex: -1, optionIndex: -1)
    }

    private func populateBoostCellWithModifier(cell: BoostTableViewCell, modifier: StoreMenuProductModifier, modifierIndex: Int, optionIndex: Int) {
        //Add Boost Cell
        let option = modifier.options[optionIndex]
        cell.nameLabel.text = option.name
        cell.priceLabel.text = String(format: "$%.2f", option.cost)
        if delegate?.isOptionSelectedForUserChoice(option.modifierId) ?? false {
            cell.selectedImageView.image = UIImage(named: "checkbox-checked")
        } else {
            cell.selectedImageView.image = UIImage(named: "checkbox-unchecked")
        }
        //For Ease of Reference
        cell.modifierIndex = modifierIndex
        cell.optionIndex = optionIndex
        cell.selectionStyle = .Default
    }

    private func populateBoostCategoryCellWithModifier(cell: BoostCategoryTableViewCell, modifier: StoreMenuProductModifier, modifierIndex: Int) {
        //Add Boost Category Cell
        cell.categoryLabel.text = modifier.name
        var imageName = "plus-button-black"
        if modifierIdsWithExpandedOptions.contains(modifier.modifierId) {
            imageName = "less-button-black"
        } else if modifierIndex == -2 && typeCatExpanded {
            imageName = "less-button-black"
        }
        cell.expandedStateImageView.image = UIImage(named: imageName)
        //For Ease of Reference
        cell.modifierIndex = modifierIndex
        cell.selectionStyle = .None
    }

    private func arrayOfNumberOfRowsForEachModifier() -> [Int] {
        var arrayOfNumberOfRows: [Int] = []
        if let productModifiers = product?.storeMenuProduct?.productModifiers {
            for modifier in productModifiers {
                if modifierIdsWithExpandedOptions.contains(modifier.modifierId) {
                    arrayOfNumberOfRows.append(modifier.options.count + 1) //1 for header cell
                } else {
                    arrayOfNumberOfRows.append(1) //Representing a closed section.
                }
            }
        }
        return arrayOfNumberOfRows
    }

    private func totalNumberOfRowsForModifiers() -> Int {
        let arrayOfNumberOfRowsForEachModif = arrayOfNumberOfRowsForEachModifier()
        var totalNumberOfRowsForModifiers = 0
        for numberOfRowsForEachModifier in arrayOfNumberOfRowsForEachModif {
            totalNumberOfRowsForModifiers += numberOfRowsForEachModifier
        }
        return totalNumberOfRowsForModifiers
    }

    func totalNumberOfRowsNeeded() -> Int {
        var numberOfRows = 0
        numberOfRows += totalNumberOfRowsForModifiers() //Total number of rows needed by modifiers, including any for expanded state.
        numberOfRows += needsRowForMakeItLightTableViewCell() ? 1 : 0//If a row is needed for make it light
        numberOfRows += numberOfRowsForTypeSection()
        return numberOfRows
    }

    private func numberOfRowsForTypeSection() -> Int {
        guard let storeMenuProduct = product?.storeMenuProduct else {
            return 0
        }
        guard let typeAndSizeModifier = storeMenuProduct.productTypeAndSizeTopLevelModifier else {
            return 0
        }
        if typeAndSizeModifier.hasSizeModifierOn2ndLevelButNotMakeItLightChoice() {
            if typeCatExpanded {
                return typeAndSizeModifier.options.count + 1
            } else {
                return 1
            }
        }
        return 0
    }

    func needsRowForMakeItLightTableViewCell() -> Bool {
        return product?.storeMenuProduct?.productTypeAndSizeTopLevelModifier?.isASmoothieTypeAndSizeModifier() ?? false
    }

    func rowIndexForMakeItLightTableViewCell() -> Int {
        if needsRowForMakeItLightTableViewCell() {
            return 0
        }
        return -1
    }

    func rowIndexForTypeCategoryCell() -> Int {
        if numberOfRowsForTypeSection() > 0 {
            return 0
        }
        return -1
    }

    func maxRowIndexForTypeCell() -> Int {
        if numberOfRowsForTypeSection() > 1 {
            return numberOfRowsForTypeSection() - 1
        }
        return -1
    }

}
