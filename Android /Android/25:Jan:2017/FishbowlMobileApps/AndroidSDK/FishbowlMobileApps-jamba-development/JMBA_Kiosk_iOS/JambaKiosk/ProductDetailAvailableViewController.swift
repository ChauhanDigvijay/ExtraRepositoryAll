//
//  ProductDetailAvailableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol ProductDetailAvailableViewControllerDelegate: class {
    func productDetailAvailableTableViewContentChanged()
    func costUpdated(cost: Double)
}

class ProductDetailAvailableViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, MakeItLightTableViewCellDelegate, ProductSizeButtonsControllerDelegate, ProductDetailCellControllerDelegate {

    @IBOutlet weak var productSizesView: UIView!
    @IBOutlet weak var tableView: UITableView!

    weak var delegate: ProductDetailAvailableViewControllerDelegate?
    var product: Product!

    private(set) var userChoice = ProductModifiersUserChoice()
    private let sizeButtonsController = ProductSizeButtonsController()
    private let productDetailCellController = ProductDetailCellController()

    override func viewDidLoad() {
        super.viewDidLoad()

        sizeButtonsController.containerView = productSizesView
        sizeButtonsController.product = product
        sizeButtonsController.delegate = self
        productDetailCellController.product = product
        productDetailCellController.delegate = self

        userChoice.quantity = 1
        updateScreen()

        tableView.estimatedRowHeight = 70
        tableView.rowHeight = UITableViewAutomaticDimension
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    func updateScreen() {
        updateProductSizes()
        updateCost()
        tableView.reloadData()
    }

    func contentHeight() -> CGFloat {
        return tableView.contentSize.height + productSizesView.frame.height
    }

    private func updateProductSizes() {
        let sizeModifier = sizeModifierForUserChoice()
        sizeButtonsController.updateSizeButtons(sizeModifier?.options)
    }

    private func sizeModifierForUserChoice() -> StoreMenuProductModifier? {
        guard let typeAndSizeModifier = product?.storeMenuProduct?.productTypeAndSizeTopLevelModifier else {
            return nil
        }
        if typeAndSizeModifier.isASimpleSizeModifier() {
            return typeAndSizeModifier
        }

        if userChoice.selectedTypeOptionId == nil && typeAndSizeModifier.options.count > 0 {
            userChoice.selectedTypeOptionId = typeAndSizeModifier.options[0].modifierId
        }
        let userChoiceSelectedTypeOption = userChoice.selectedTypeOptionId
        for option in typeAndSizeModifier.options {
            if option.modifierId == userChoiceSelectedTypeOption && option.modifiers.count == 1 {
                return option.modifiers[0]
            }
        }

        return nil
    }


    // MARK: UITableViewDataSource

    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return productDetailCellController.totalNumberOfRowsNeeded()
    }

    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        guard let cell = productDetailCellController.cellForRowAtIndexPath(tableView, indexPath: indexPath) else {
            fatalError("Unexpected index path")
        }
        return cell
    }


    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let cell = tableView.cellForRowAtIndexPath(indexPath)

        if let boostTVC = cell as? BoostTableViewCell {
            // Tap on a boost cell
            didTapOnBoostCell(boostTVC, indexPath: indexPath)
        } else if let boostCatTVC = cell as? BoostCategoryTableViewCell {
            // Tap on a boost caregory (expand / collapse)
            didTapOnBoostCategoryCell(boostCatTVC)
        }
    }

    private func didTapOnBoostCell(boostTableViewCell: BoostTableViewCell, indexPath: NSIndexPath) {
        let modifierIndex = boostTableViewCell.modifierIndex
        let optionIndex = boostTableViewCell.optionIndex
        tableView.deselectRowAtIndexPath(indexPath, animated: true)

        if modifierIndex == -2 {
            let typeAndSizeModifier = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!
            let option = typeAndSizeModifier.options[optionIndex]
            if userChoice.selectedTypeOptionId == option.modifierId {
                presentOkAlert("Option Not Selected", message: "Please select at least one option.")
                return
            }
            let oldTypeOptionId = userChoice.selectedTypeOptionId
            var oldOptionIndex = 0
            for option in typeAndSizeModifier.options {
                if oldTypeOptionId == option.modifierId {
                    break
                }
                oldOptionIndex++
            }
            if let oldCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: oldOptionIndex + 1, inSection: 0)) as? BoostTableViewCell {
                oldCell.selectedImageView.image = UIImage(named: "checkbox-unchecked")
            }
            boostTableViewCell.selectedImageView.image = UIImage(named: "checkbox-checked")
            userChoice.selectedTypeOptionId = option.modifierId
            userChoice.selectedSizeOptionId = nil
            updateProductSizes()
            updateCost()
        } else {
            let modifier = product.storeMenuProduct!.productModifiers[modifierIndex]
            let option = modifier.options[optionIndex]
            if userChoice.isOptionIdSelected(option.modifierId) {
                deselectOptionId(option.modifierId, inModifier: modifier)
                boostTableViewCell.selectedImageView.image = UIImage(named: "checkbox-unchecked")
            } else if selectOptionId(option.modifierId, inModifier: modifier) {
                boostTableViewCell.selectedImageView.image = UIImage(named: "checkbox-checked")
            }
            updateCost()
        }
    }

    private func didTapOnBoostCategoryCell(categoryTableViewCell: BoostCategoryTableViewCell) {
        let modifierIndex = categoryTableViewCell.modifierIndex
        if modifierIndex == -2 {
            productDetailCellController.toggleBoostCategoryExpanded()
        } else {
            let modifier = product.storeMenuProduct!.productModifiers[modifierIndex]
            productDetailCellController.addRemoveModifierIdWithExpandedOptions(modifier)
        }
        tableView.reloadData()
        delegate?.productDetailAvailableTableViewContentChanged()
    }


    //MARK: Func for helping UI population decisions

    func selectOptionId(optionId: Int64, inModifier modifier: StoreMenuProductModifier) -> Bool {
        if modifier.maxSelects != nil {
            let userChoiceSelectedOptionIds = userChoice.selectedOptionIdsForModifierId[modifier.modifierId]
            if userChoiceSelectedOptionIds != nil && !userChoiceSelectedOptionIds!.contains(optionId) {
                if userChoiceSelectedOptionIds!.count + 1 > modifier.maxSelects! {
                    presentOkAlert("Too Many Options", message: "No more than than \(modifier.maxSelects!) options allowed from this group.")
                    return false
                }
            }
        }
        userChoice.addSelectedOptionIdForModifierId(modifier.modifierId, optionId: optionId)
        if modifier.optionIdForUnnestedOption != nil {
            userChoice.addSelectedOptionIdForModifierId(modifier.modifierId, optionId: modifier.optionIdForUnnestedOption!)
        }
        return true
    }

    func deselectOptionId(optionId: Int64, inModifier modifier: StoreMenuProductModifier) {
        userChoice.removeSelectedOptionIdForModifierId(modifier.modifierId, optionId: optionId)
        //If we had unnested modifier
        if modifier.optionIdForUnnestedOption != nil {
            let selectedOptionIdsForModifierId = userChoice.selectedOptionIdsForModifierId[modifier.modifierId]
            if selectedOptionIdsForModifierId != nil && selectedOptionIdsForModifierId!.count == 1 {
                userChoice.removeSelectedOptionIdForModifierId(modifier.modifierId, optionId: modifier.optionIdForUnnestedOption!)
            }
        }
    }

    // MARK: Cell Delegates

    func makeItLightTableViewCellDidChangeSwitchState(cell: MakeItLightTableViewCell) {
        let isMakeItLight = cell.switchControl.on
        let optionId: Int64!
        if isMakeItLight {
            optionId = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!.makeItLightOptionId()!
        } else {
            optionId = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!.classicOptionId()!
        }
        userChoice.selectedTypeOptionId = optionId
        userChoice.selectedSizeOptionId = nil
        updateProductSizes()
        updateCost()
    }


    // MARK: ProductSizeButtonsControllerDelegate

    func setSelectedProductSize(sizeOption: StoreMenuProductModifierOption?) {
        userChoice.selectedSizeOptionId = sizeOption?.modifierId
        updateCost()
    }

    func selectedSizeOptionIdFromUserChoice() -> Int64? {
        return userChoice.selectedSizeOptionId
    }

    func selectedTypeOptionIdFromUserChoice() -> Int64? {
        return userChoice.selectedTypeOptionId
    }

    func isOptionSelectedForUserChoice(optionId: Int64) -> Bool {
        return userChoice.isOptionIdSelected(optionId)
    }


    // MARK: Cost

    private func updateCost() {
        let cost = calculateCost()
        delegate?.costUpdated(cost)
    }

    private func calculateCost() -> Double {
        if product.storeMenuProduct == nil || product.storeMenuProduct!.hasPopulatedModifiers == false {
            return 0
        }
        var cost = product.storeMenuProduct!.cost//The base cost
        if userChoice.selectedSizeOptionId != nil {
            let sizeModifier = sizeModifierForUserChoice()
            for sizeOption in sizeModifier!.options {
                if sizeOption.modifierId == userChoice.selectedSizeOptionId {
                    cost += sizeOption.cost
                }
            }
        }
        if userChoice.selectedOptionIdsForModifierId.count > 0 {
            let modifiers = product.storeMenuProduct!.productModifiers
            for modifier in modifiers {
                for option in modifier.options {
                    if userChoice.isOptionIdSelected(option.modifierId) {
                        cost += option.cost
                    }
                }
            }
        }
        return cost * Double(userChoice.quantity)
    }

}
