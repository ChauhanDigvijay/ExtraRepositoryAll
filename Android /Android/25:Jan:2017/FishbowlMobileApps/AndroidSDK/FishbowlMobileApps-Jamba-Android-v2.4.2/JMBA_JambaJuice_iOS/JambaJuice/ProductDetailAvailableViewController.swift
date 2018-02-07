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
    func costUpdated(_ cost: Double)
    func keyboardDidShow()
    func showProductQuantityPickerView(_ selectedvalue:String, parentModifier: StoreMenuProductModifier, option: StoreMenuProductModifierOption)
}

enum ModifiersType : Int {
    case kTypeModifier = -2
    case kSpecialInstruction = -1
    case kOptionModifier = 0
}

class ProductDetailAvailableViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, ProductQuantityTableViewCellDelegate, MakeItLightTableViewCellDelegate, UITextFieldDelegate, SubstitutionsViewControllerDelegate,  BoostTableViewCellDelegate {
    
    // Product sizes might or might not be present on the product
    @IBOutlet weak var productSizesView: UIView!
    @IBOutlet weak var productSizesHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var productSizesTopSpaceConstraint: NSLayoutConstraint!
    @IBOutlet weak var tableView: UITableView!
    
    fileprivate var substitutionsViewController: SubstitutionsViewController?
    fileprivate var productDetailViewController: ProductDetailViewController?
    
    weak var delegate: ProductDetailAvailableViewControllerDelegate?
    var product: Product!
    var currentIndex:IndexPath?
    var productModifiersQuantity:[ProductModifiersMaxChoiceModel] = []
    
    
    // State Tacking Of UI
    fileprivate var modifierIdsWithExpandedOptions = Set<Int64>()
    fileprivate var addSpecialInstructionsExpanded: Bool = false
    fileprivate var typeCatExpanded: Bool = true
    
    fileprivate var modifierIdsWithExpandedSubCategoryOptions = Set<Int64>()
    
    //RemoveSubstitutionModel
    var subRemoveSubstitutionModels : [RemoveSubstitutionModel] = []
    
    
    // User Choice Model
    var userChoice = ProductModifiersUserChoice()
    var selectedSizeButtonIndex = 0//This is not in User Choice Model as this is related to UI.
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        updateScreen()
        tableView.estimatedRowHeight = 70
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.reloadData()
        NotificationCenter.default.addObserver(self, selector: #selector(ProductDetailAvailableViewController.keyboardDidShow(_:)), name: NSNotification.Name.UIKeyboardDidShow, object: nil)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    func updateQuantity() {
        let currentQuantity = BasketService.itemsInBasket()
        let newTotal = Int64(userChoice.quantity) + currentQuantity
        //Check if current quantity is 10
        if currentQuantity >= Constants.oloItemLimit {
            userChoice.quantity = 0
        }
            //If current quantity is not ten but selected quantity + current quantity exceeds ten
            //Then  adjust the selected quantity
        else if newTotal > Constants.oloItemLimit {
            userChoice.quantity = Constants.oloItemLimit - Int64(currentQuantity)
        }
            //Check if we have chosen 0 and we have room in basket. Then reset to at least 1.
        else if userChoice.quantity == 0 && currentQuantity < Constants.oloItemLimit {
            userChoice.quantity = 1
        }
        let index = rowIndexForQuantityCell()
        let cell = tableView.cellForRow(at: IndexPath(row: index, section: 0)) as? ProductQuantityTableViewCell
        if cell != nil {
            populateQuantityCell(cell!)
        }
        updateCost()
    }
    
    func updateScreen() {
        updateProductSizes()
        updateQuantity()
        updateCost()
        tableView.reloadData()
    }
    
    func contentHeight() -> CGFloat {
        tableView.reloadData()
        return tableView.contentSize.height + productSizesHeightConstraint.constant
    }
    
    fileprivate func updateProductSizes() {
        //Remove all prev buttons
        for subview in productSizesView.subviews {
            subview.removeFromSuperview()
        }
        
        // Product menu has not been fetched
        if product.storeMenuProduct == nil {
            productSizesView.isHidden = true
            productSizesTopSpaceConstraint.constant = 0
            productSizesHeightConstraint.constant = 0
            return
        }
        let sizeModifier = sizeModifierForUserChoice()
        
        //If sanity passed
        productSizesView.isHidden = false
        let height:CGFloat = 80
        productSizesHeightConstraint.constant = height
        
        //We always show at least one button
        var buttonCount = 1
        var hasSizeModifier = false
        if sizeModifier != nil && sizeModifier!.options.count > 0 {
            hasSizeModifier = true
            buttonCount = sizeModifier!.options.count
        }
        
        let width = ((UIScreen.main.bounds.width - CGFloat(buttonCount - 1)) / CGFloat(buttonCount))
        var x: CGFloat = 0;
        //        let buttons: [SelectableButton] = []
        
        // index of new selected button. Try to use same unless we can not.
        // This is based on the fact that make it light has same number of sizes.
        // and in same sequence.
        if selectedSizeButtonIndex >= buttonCount {
            selectedSizeButtonIndex = buttonCount - 1
        }
        if hasSizeModifier {
            userChoice.selectedSizeOptionId = sizeModifier!.options[selectedSizeButtonIndex].id
        }
        else {
            userChoice.selectedSizeOptionId = nil
        }
        for i in 0..<buttonCount {
            let button = SelectableButton()
            button.normalBackgroundColor = UIColor(hex: Constants.jambaLightGrayColor)
            button.selectedBackgroundColor = UIColor(hex: Constants.jambaLightBlueColor)
            //If there is only one button. If needs to be disabled and not selected state.
            if buttonCount == 1 {
                button.isEnabled = false
            }
                //If we have sizeModifiers, Mark button selected whose id is selected.
            else if hasSizeModifier{
                button.isSelected = userChoice.selectedSizeOptionId == sizeModifier!.options[i].id
            }
                //Should never haven
            else {
                assert(false, "Unexpected state in product detail")
            }
            let textColor = UIColor(hex: Constants.jambaDarkGrayColor)
            let mutableTitleAttributedString = NSMutableAttributedString()
            let sizeFont = UIFont.init(name: "Archer-Bold", size: 17)!//Actual size in psd is 20 but that looks very bad when name is large.
            let sizeAttributes: [NSAttributedStringKey:Any] = [NSAttributedStringKey(rawValue: NSAttributedStringKey.font.rawValue) : sizeFont, NSAttributedStringKey(rawValue: NSAttributedStringKey.foregroundColor.rawValue) : textColor]
            if hasSizeModifier {
                //Size Attributed String
                let sizeAttrString = NSAttributedString(string: "\(sizeModifier!.options[i].name)", attributes: sizeAttributes)
                //Price Attributed String
                let priceFont = UIFont.init(name: "HelveticaNeue", size: 13)!
                let priceAttributes: [NSAttributedStringKey:Any] = [NSAttributedStringKey(rawValue: NSAttributedStringKey.font.rawValue): priceFont, NSAttributedStringKey(rawValue: NSAttributedStringKey.foregroundColor.rawValue): textColor]
                let priceString = String(format: "$%.2f", sizeModifier!.options[i].cost)
                let priceAttrString = NSAttributedString(string: "\n\(priceString)", attributes: priceAttributes)
                //Combine Both
                mutableTitleAttributedString.append(sizeAttrString)
                mutableTitleAttributedString.append(priceAttrString)
            }
            else {
                let productPriceString = String(format: "$%.2f", product.storeMenuProduct!.cost)
                let productPriceAttrString = NSAttributedString(string: productPriceString, attributes: sizeAttributes)
                mutableTitleAttributedString.append(productPriceAttrString)
            }
            button.setAttributedTitle(mutableTitleAttributedString.copy() as? NSAttributedString, for: .normal)
            button.frame = CGRect(x: x, y: 0, width: width, height: height)
            button.addTarget(self, action: #selector(ProductDetailAvailableViewController.productSizeSelected(_:)), for: .touchUpInside)
            button.tag = i
            button.titleLabel?.adjustsFontSizeToFitWidth = true
            button.titleLabel?.minimumScaleFactor = 0.5
            button.titleLabel?.numberOfLines = 0
            button.titleLabel?.textAlignment = .center
            productSizesView.addSubview(button)
            x += width + 1
        }
        productSizesTopSpaceConstraint.constant = 0
    }
    
    
    
    // MARK: UITableViewDataSource
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return totalNumberOfRowsNeeded()
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == rowIndexForMakeItLightTableViewCell() {
            //Make It Light Cell
            let cell = tableView.dequeueReusableCell(withIdentifier: "MakeItLightTableViewCell", for: indexPath) as! MakeItLightTableViewCell
            cell.switchControl.isOn = makeItLightSelected()
            cell.selectionStyle = .none
            cell.delegate = self
            return cell
        }
        
        if indexPath.row == rowIndexForAddSpecialInstructionsCell() {
            //Add Special Instructions Cat Cell
            let cell = tableView.dequeueReusableCell(withIdentifier: "BoostCategoryTableViewCell", for: indexPath) as! BoostCategoryTableViewCell
            cell.categoryLabel.text = "Add Special Instructions"
            cell.selectionStyle = .none
            var imageName = "plus-button"
            if addSpecialInstructionsExpanded {
                imageName = "less-button"
            }
            cell.expandedStateImageView.image = UIImage(named: imageName)
            
            //ModifiersType.kSpecialInstruction.rawValue for Special Instructions
            cell.modifierIndex = ModifiersType.kSpecialInstruction.rawValue
            return cell
        }
        
        if indexPath.row == rowIndexForAddSpecialInstructionsInputCell() {
            //Add Special Instructions Input Cell
            let cell = tableView.dequeueReusableCell(withIdentifier: "SpecialInstructionsInputTableViewCell", for: indexPath) as! SpecialInstructionsInputTableViewCell
            cell.selectionStyle = .none
            cell.textField.text = userChoice.specialInstructions
            cell.textField.delegate = self
            cell.textField.addTarget(self, action: #selector(ProductDetailAvailableViewController.textFieldEditingChanged(_:)), for: UIControlEvents.editingChanged)
            return cell
        }
        
        if indexPath.row == rowIndexForQuantityCell() {
            //Add quantity cell
            let cell = tableView.dequeueReusableCell(withIdentifier: "ProductQuantityTableViewCell", for: indexPath) as! ProductQuantityTableViewCell
            cell.delegate = self
            cell.selectionStyle = .none
            populateQuantityCell(cell)
            return cell
        }
        
        if indexPath.row == rowIndexForCustomizeCell() {
            //Add quantity cell
            let cell = tableView.dequeueReusableCell(withIdentifier: "CustomizeTableViewCell", for: indexPath)
            //            cell.delegate = self
            cell.selectionStyle = .none
            
            //            populateQuantityCell(cell)
            return cell
        }
        
        //Type cat cell
        if indexPath.row == rowIndexForTypeCategoryCell() {
            let modifierIndex = ModifiersType.kTypeModifier.rawValue
            let modifier = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!
            let cell = tableView.dequeueReusableCell(withIdentifier: "BoostCategoryTableViewCell", for: indexPath) as! BoostCategoryTableViewCell
            populateBoostCategoryCellWithModifier(cell, modifier: modifier, modifierIndex: modifierIndex,parentIndex:  -1)
            //Make cell label take proper dimensions
            cell.layoutIfNeeded()
            return cell
        }
        //type customize cell
        if indexPath.row == rowIndexForCustomizeCell() {
            //Make It Light Cell
            let cell = tableView.dequeueReusableCell(withIdentifier: "CustomizeTableViewCell", for: indexPath) as! CustomizeTableViewCell
            cell.selectionStyle = .none
            return cell
        }
        
        //type boost cell
        if indexPath.row <= maxRowIndexForTypeCell() {
            let modifierIndex = ModifiersType.kTypeModifier.rawValue
            let optionIndex = indexPath.row  - 1
            let modifier = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!
            let cell = tableView.dequeueReusableCell(withIdentifier: "BoostTableViewCell", for: indexPath) as! BoostTableViewCell
            populateBoostCellWithModifier(cell, modifier: modifier, modifierIndex: modifierIndex, optionIndex: optionIndex)
            //Make cell label take proper dimensions
            cell.layoutIfNeeded()
            cell.quatityButton.tag = indexPath.row
            cell.delegate = self
            return cell
        }
        
        //Old Code
        //        let modifierAndOptionIndexTuple = modifierAndOptionIndexFromIndexPath(indexPath)
        //        //If modifier index is valid
        //        if modifierAndOptionIndexTuple.0 >= 0 {
        //            let modifierIndex = modifierAndOptionIndexTuple.0
        //            let modifier = product.storeMenuProduct!.productModifiers[modifierIndex]
        //            //If option index is valid
        //            if modifierAndOptionIndexTuple.1 >= 0 {
        //                let optionIndex = modifierAndOptionIndexTuple.1
        //                let cell = tableView.dequeueReusableCellWithIdentifier("BoostTableViewCell", forIndexPath: indexPath) as! BoostTableViewCell
        //                populateBoostCellWithModifier(cell, modifier: modifier, modifierIndex: modifierIndex, optionIndex: optionIndex)
        //                //Make cell label take proper dimensions
        //                cell.layoutIfNeeded()
        //                return cell
        //            }
        //            else {
        //                let cell = tableView.dequeueReusableCellWithIdentifier("BoostCategoryTableViewCell", forIndexPath: indexPath) as! BoostCategoryTableViewCell
        //                populateBoostCategoryCellWithModifier(cell, modifier: modifier, modifierIndex: modifierIndex)
        //                //Make cell label take proper dimensions
        //                cell.layoutIfNeeded()
        //                return cell
        //            }
        //        }
        
        //new code
        let modifierAndOptionIndexTuple = modifierAndOptionIndexFromIndexPath(indexPath)
        //If modifier index is valid
        if modifierAndOptionIndexTuple.0 == nil {
            //If option index is valid
            if modifierAndOptionIndexTuple.1 != nil {
                let option = modifierAndOptionIndexTuple.1
                let cell = tableView.dequeueReusableCell(withIdentifier: "BoostTableViewCell", for: indexPath) as! BoostTableViewCell
                populateBoostCellWithModifier(cell, option: option!, parentModifier: modifierAndOptionIndexTuple.2)
                //Make cell label take proper dimensions
                cell.layoutIfNeeded()
                cell.delegate = self
                cell.quatityButton.tag = indexPath.row
                return cell
            }
            
        }
        else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "BoostCategoryTableViewCell", for: indexPath) as! BoostCategoryTableViewCell
            populateBoostCategoryCellWithModifier(cell, modifier: modifierAndOptionIndexTuple.0!, parentModifier: modifierAndOptionIndexTuple.2, parentOption: modifierAndOptionIndexTuple.1)
            //Make cell label take proper dimensions
            cell.layoutIfNeeded()
            return cell
        }
        assert(false, "unexpected index path")
        return UITableViewCell()
    }
    
    //old code
    //    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
    //        let cell = tableView.cellForRowAtIndexPath(indexPath)
    //
    //        // Tap on a boost cell
    //        if let boostTVC = cell as? BoostTableViewCell {
    //            let modifierIndex = boostTVC.modifierIndex
    //            let optionIndex = boostTVC.optionIndex
    //            if modifierIndex == -2 {
    //                let typeAndSizeModifier = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!
    //                let option = typeAndSizeModifier.options[optionIndex]
    //                if userChoice.selectedTypeOptionId == option.id {
    //                    presentOkAlert("Option Not Selected", message: "Please select at least one option.")
    //                }
    //                else {
    //                    let oldTypeOptionId = userChoice.selectedTypeOptionId
    //                    var oldOptionIndex = 0
    //                    for option in typeAndSizeModifier.options {
    //                        if oldTypeOptionId == option.id {
    //
    //                            break
    //                        }
    //                        oldOptionIndex++
    //                    }
    //                    let oldCell = tableView.cellForRowAtIndexPath(NSIndexPath(forRow: oldOptionIndex + 1, inSection: 0)) as? BoostTableViewCell
    //                    if oldCell != nil {
    //                        oldCell!.selectedImageView.hidden = true
    //                    }
    //                    boostTVC.selectedImageView.hidden = false
    //                    userChoice.selectedTypeOptionId = option.id
    //                    userChoice.selectedSizeOptionId = nil
    //                    updateProductSizes()
    //                    updateCost()
    //                }
    //                tableView.deselectRowAtIndexPath(indexPath, animated: true)
    //            }
    //            else {
    //                let modifier = product.storeMenuProduct!.productModifiers[modifierIndex]
    //                let option = modifier.options[optionIndex]
    //                if userChoice.isOptionIdSelected(option.id) {
    //                    deselectOptionId(option.id, inModifier: modifier)
    //                    boostTVC.selectedImageView.hidden = true
    //                }
    //                else {
    //                    if selectOptionId(option.id, inModifier: modifier) {
    //                        boostTVC.selectedImageView.hidden = false
    //                    }
    //                }
    //                tableView.deselectRowAtIndexPath(indexPath, animated: true)
    //                updateCost()
    //            }
    //        }
    // Tap on a boost caregory (expand / collapse)
    //    else if let boostCatTVC = cell as? BoostCategoryTableViewCell {
    //        let modifierIndex = boostCatTVC.modifierIndex
    //        //Settings
    //        if modifierIndex == -1 {
    //            addSpecialInstructionsExpanded = !addSpecialInstructionsExpanded
    //        }
    //            //Type cat
    //        else if modifierIndex == -2 {
    //            typeCatExpanded = !typeCatExpanded
    //        }
    //        else {
    //            let modifier = product.storeMenuProduct!.productModifiers[modifierIndex]
    //            if modifierIdsWithExpandedOptions.contains(modifier.id) {
    //                modifierIdsWithExpandedOptions.remove(modifier.id)
    //            } else {
    //                modifierIdsWithExpandedOptions.insert(modifier.id)
    //            }
    //        }
    //        tableView.reloadData()  // TODO: Should inject / remove cells animated
    //        delegate?.productDetailAvailableTableViewContentChanged()
    //    }
    //}
    
    
    //new code
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.cellForRow(at: indexPath)
        
        if (cell as? CustomizeTableViewCell) != nil{
            return
        }
            // Tap on a boost cell
        else if let boostTVC = cell as? BoostTableViewCell {
            if boostTVC.modifierIndex == ModifiersType.kTypeModifier.rawValue {
                let typeAndSizeModifier = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!
                let option = typeAndSizeModifier.options[boostTVC.optionIndex]
                if userChoice.selectedTypeOptionId == option.id {
                    presentOkAlert("Option Not Selected", message: "Please select at least one option.")
                }
                else {
                    let oldTypeOptionId = userChoice.selectedTypeOptionId
                    var oldOptionIndex = 0
                    for option in typeAndSizeModifier.options {
                        if oldTypeOptionId == option.id {
                            
                            break
                        }
                        oldOptionIndex += 1;
                    }
                    let oldCell = tableView.cellForRow(at: IndexPath(row: oldOptionIndex + 1, section: 0)) as? BoostTableViewCell
                    if oldCell != nil {
                        //                        oldCell!.selectedImageView.hidden = true
                        oldCell?.hideQuantityField()
                    }
                    
                    //if the Boost cell support quantity, then show the quantity field
                    if oldCell?.parentModifier.supportsChoiceQuantities != nil && (oldCell?.parentModifier.supportsChoiceQuantities)! {
                        oldCell?.showQuantityField()
                    } else {
                        boostTVC.selectedImageView.isHidden = false
                    }
                    
                    userChoice.selectedTypeOptionId = option.id
                    userChoice.selectedSizeOptionId = nil
                    updateProductSizes()
                    updateCost()
                    resetTableView()
                }
                tableView.deselectRow(at: indexPath, animated: true)
            } else {
                if userChoice.isOptionIdSelected(boostTVC.option.id) {
                    deselectOptionId(boostTVC.option.id, inModifier: boostTVC.parentModifier)
                    //                    boostTVC.selectedImageView.hidden = true
                    boostTVC.hideQuantityField()
                }
                else {
                    if selectOptionId(boostTVC.option.id, inModifier: boostTVC.parentModifier) {
                        
                        //if the Boost cell support quantity, then show the quantity field
                        if boostTVC.parentModifier.supportsChoiceQuantities != nil && (boostTVC.parentModifier.supportsChoiceQuantities)! {
                            boostTVC.showQuantityField()
                        } else {
                            boostTVC.selectedImageView.isHidden = false
                        }
                    }
                }
                tableView.deselectRow(at: indexPath, animated: true)
                
                updateCost();
            }
        }
            
            // Tap on a boost caregory (expand / collapse)
        else if let boostCatTVC = cell as? BoostCategoryTableViewCell {
            //check it is an root modifier
            if (boostCatTVC.parentModifier==nil) {
                if let modifierId = boostCatTVC.modifier?.id{
                    if modifierIdsWithExpandedOptions.contains((modifierId)) {
                        modifierIdsWithExpandedOptions.remove((modifierId))
                    } else {
                        modifierIdsWithExpandedOptions.insert((modifierId))
                    }
                    if boostCatTVC.modifierIndex == ModifiersType.kTypeModifier.rawValue {
                        typeCatExpanded = !typeCatExpanded
                    }
                }
                //otherwise sub level modifier
            } else {
                if let modifierId = boostCatTVC.modifier?.id{
                    if modifierIdsWithExpandedSubCategoryOptions.contains((modifierId)) {
                        modifierIdsWithExpandedSubCategoryOptions.remove((modifierId))
                } else {
                        modifierIdsWithExpandedSubCategoryOptions.insert((modifierId))
                }
            }
        }
        tableView.reloadData()  // TODO: Should inject / remove cells animated
        delegate?.productDetailAvailableTableViewContentChanged()
    }
    
    //        delegate
    
}

//MARK: Func for helping UI population decisions

fileprivate func populateBoostCellWithModifier(_ cell: BoostTableViewCell, modifier: StoreMenuProductModifier, modifierIndex: Int, optionIndex: Int) {
    //Add Boost Cell
    let option = modifier.options[optionIndex]
    cell.option = option
    cell.nameLabel.text = option.name
    cell.priceLabel.text = String(format: "$%.2f", option.cost)
    //        cell.selectedImageView.hidden = !userChoice.isOptionIdSelected(option.id)
    if !userChoice.isOptionIdSelected(option.id) {
        cell.hideQuantityFieldWithoutAnimation()
    } else {
        if modifier.supportsChoiceQuantities != nil && modifier.supportsChoiceQuantities! {
            cell.showQuantityFieldWithoutAnimation()
            cell.updatePriceLabel(String(userChoice.getQuantityForAOption(option.id)))
        } else {
            cell.hideQuantityFieldWithoutAnimation()
            cell.selectedImageView.isHidden = !userChoice.isOptionIdSelected(option.id)
        }
    }
    //For Ease of Reference
    cell.modifierIndex = ModifiersType.kTypeModifier.rawValue
    cell.parentModifier = modifier
    cell.optionIndex = optionIndex
    cell.selectionStyle = .default
    
}

fileprivate func populateBoostCellWithModifier(_ cell: BoostTableViewCell, option: StoreMenuProductModifierOption, parentModifier: StoreMenuProductModifier?) {
    cell.option = option
    cell.nameLabel.text = option.name
    cell.priceLabel.text = String(format: "$%.2f", option.cost)
    //        cell.selectedImageView.hidden = !userChoice.isOptionIdSelected(option.id)
    if !userChoice.isOptionIdSelected(option.id) {
        cell.hideQuantityFieldWithoutAnimation()
    } else {
        if parentModifier!.supportsChoiceQuantities != nil && parentModifier!.supportsChoiceQuantities! {
            cell.showQuantityFieldWithoutAnimation()
            cell.updatePriceLabel(String(userChoice.getQuantityForAOption(option.id)))
        } else {
            cell.hideQuantityFieldWithoutAnimation()
            cell.selectedImageView.isHidden = !userChoice.isOptionIdSelected(option.id)
        }
    }
    cell.parentModifier = parentModifier
    cell.modifierIndex = ModifiersType.kOptionModifier.rawValue
    cell.selectionStyle = .default
}


fileprivate func populateBoostCategoryCellWithModifier(_ cell: BoostCategoryTableViewCell, modifier: StoreMenuProductModifier, modifierIndex: Int, parentIndex: Int) {
    //Add Boost Category Cell
    
    cell.categoryLabel.text = modifier.name
    let modifierIndex = ModifiersType.kTypeModifier.rawValue
    var imageName = "plus-button"
    //        if isSectionExpandedForModifierId(modifier.id) {
    //            imageName = "less-button"
    //        }
    //        else
    if modifierIndex == ModifiersType.kTypeModifier.rawValue && typeCatExpanded {
        imageName = "less-button"
    }
    cell.expandedStateImageView.image = UIImage(named: imageName)
    //For Ease of Reference
    cell.modifierIndex = modifierIndex
    cell.modifier = modifier
    //        cell.ParentIndex = parentIndex
    cell.selectionStyle = .none
}

fileprivate func populateBoostCategoryCellWithModifier(_ cell: BoostCategoryTableViewCell, modifier: StoreMenuProductModifier, parentModifier: StoreMenuProductModifier?, parentOption: StoreMenuProductModifierOption?) {
    cell.modifier = modifier
    cell.modifierIndex = ModifiersType.kOptionModifier.rawValue
    cell.categoryLabel.text = modifier.name
    var imageName:String;
    if (parentModifier==nil) {
        imageName = "plus-button"
        cell.backgroundColor = UIColor(hex: 0xFFFFFF);
    } else {
        cell.categoryLabel.text = parentOption?.name
        imageName = "back-button"
        cell.backgroundColor = UIColor(hex: 0xF1F1F1);
    }
    if isSectionExpandedForModifierId(modifier.id) && parentModifier==nil {
        imageName = "less-button"
        cell.backgroundColor = UIColor(hex: 0xFFFFFF);
    } else if isSubCategoryExpandedForModifierId(modifier.id) {
        cell.categoryLabel.text = parentOption?.name
        imageName =  "down-button"
        cell.backgroundColor = UIColor(hex: 0xF1F1F1);
    }
    
    cell.parentModifier = parentModifier
    cell.expandedStateImageView.image = UIImage(named: imageName)
    cell.selectionStyle = .none
}

fileprivate func numberOfRowsForTypeSection() -> Int {
    if product.storeMenuProduct != nil {
        let typeAndSizeModifier = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier
        if typeAndSizeModifier != nil && typeAndSizeModifier!.hasSizeModifierOn2ndLevelButNotMakeItLightChoice() {
            if typeCatExpanded {
                return typeAndSizeModifier!.options.count + 1
            }
            else {
                return 1
            }
        }
    }
    return 0
}

fileprivate func needsRowForMakeItLightTableViewCell() -> Bool {
    if product.storeMenuProduct != nil && product.storeMenuProduct!.productTypeAndSizeTopLevelModifier != nil && product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!.isASmoothieTypeAndSizeModifier() {
        return true
    }
    return false
}

fileprivate func needsRowForCustomizeItCell() -> Bool {
    for modifier in product.storeMenuProduct!.productModifiers {
        if (modifier.isModifierNameIsCustomizeIt()) {
            return true;
        }
    }
    return false
}

fileprivate func rowIndexForMakeItLightTableViewCell() -> Int {
    if needsRowForMakeItLightTableViewCell() {
        return 0
    }
    return -1
}

fileprivate func rowIndexForTypeCategoryCell() -> Int {
    if numberOfRowsForTypeSection() > 0 {
        return 0
    }
    return -1
}

fileprivate func maxRowIndexForTypeCell() -> Int {
    if numberOfRowsForTypeSection() > 1 {
        return numberOfRowsForTypeSection() - 1
    }
    return -1
}

fileprivate func isSectionExpandedForModifierId(_ id: Int64) -> Bool {
    return modifierIdsWithExpandedOptions.index(of: id) != nil
}

fileprivate func isSubCategoryExpandedForModifierId(_ id: Int64) -> Bool {
    return modifierIdsWithExpandedSubCategoryOptions.index(of: id) != nil
}

//old code
//    private func arrayOfNumberOfRowsForEachModifier() -> [Int] {
//        var arrayOfNumberOfRows: [Int] = []
//        if product.storeMenuProduct != nil {
//            for modifier in product.storeMenuProduct!.productModifiers {
//                if isSectionExpandedForModifierId(modifier.id) {
//                    arrayOfNumberOfRows.append(modifier.options.count + 1)//1 for header cell
//                }
//                else {
//                    arrayOfNumberOfRows.append(1)//Representing a closed section.
//                }
//            }
//        }
//        return arrayOfNumberOfRows
//    }

//new code
fileprivate func totalNoOfRowsForEachModifier() -> (Int) {
    var modifierCount: Int = 0
    for modifier in product.storeMenuProduct!.productModifiers {
        if (!modifier.isModifierNameIsCustomizeIt()) {
            modifierCount += 1;
            
            if isSectionExpandedForModifierId(modifier.id) {
                // for sub modifier
                if modifier.options.count>0 && modifier.options[0].modifiers.count>0 {
                    for option in modifier.options {
                        for smodifier in option.modifiers {
                            modifierCount += 1;
                            // for sub modifier options
                            if isSubCategoryExpandedForModifierId(smodifier.id) {
                                modifierCount += smodifier.options.count;
                            }
                        }
                    }
                }else{
                    // for options in root level modifier
                    modifierCount += modifier.options.count;
                }
                
                
            }
        }
    }
    return modifierCount
}

//old code
//    private func totalNumberOfRowsForModifiers() -> Int {
//        let arrayOfNumberOfRowsForEachModif = arrayOfNumberOfRowsForEachModifier()
//        var totalNumberOfRowsForModifiers = 0
//        for numberOfRowsForEachModifier in arrayOfNumberOfRowsForEachModif {
//            totalNumberOfRowsForModifiers += numberOfRowsForEachModifier
//        }
//        return totalNumberOfRowsForModifiers
//    }

//new code
fileprivate func totalNumberOfRowsForModifiers() -> Int {
    return totalNoOfRowsForEachModifier()
}

fileprivate func totalNumberOfRowsNeeded() -> Int {
    //Check if we have Store Menu and Basket
    if product.storeMenuProduct == nil || BasketService.sharedBasket == nil {
        return 0
    }
    //If yes:
    var numberOfRows = 0
    if supportsSpecialInstructions() {
        numberOfRows = addSpecialInstructionsExpanded ? 2 : 1//1 or 2 row needed for this depending upon if it is expanded or not.
    }
    numberOfRows += totalNumberOfRowsForModifiers() //Total number of rows needed by modifiers, including any for expanded state.
    numberOfRows += needsRowForMakeItLightTableViewCell() ? 1 : 0//If a row is needed for make it light
    numberOfRows += numberOfRowsForTypeSection()
    numberOfRows += 1//Quantity Cell
    numberOfRows += needsRowForCustomizeItCell() ? 1 : 0//Customize Cell neededor not
    return numberOfRows
}

fileprivate func supportsSpecialInstructions() -> Bool {
    let supportsSpInstrs = BasketService.sharedBasket!.store.supportsSpecialInstructions
    return supportsSpInstrs != nil && supportsSpInstrs!
}

fileprivate func rowIndexForAddSpecialInstructionsCell() -> Int {
    let needCustomizecell = needsRowForCustomizeItCell() ? 1 : 0;
    if !supportsSpecialInstructions() {
        return -1
    }
    let totalRows = totalNumberOfRowsNeeded()
    if addSpecialInstructionsExpanded {
        return totalRows - (3 + needCustomizecell) //Adjust for quantity & Instructions Input cell.
    }
    else {
        return totalRows - (2 + needCustomizecell) //Adjust for quantity which is the last cell
    }
}

fileprivate func rowIndexForAddSpecialInstructionsInputCell() -> Int {
    if !supportsSpecialInstructions() {
        return -1
    }
    let needCustomizecell = needsRowForCustomizeItCell() ? 1 : 0;
    if addSpecialInstructionsExpanded {
        let totalRows = totalNumberOfRowsNeeded()
        return totalRows - (2 + needCustomizecell)   //adjust cell position based on presence of customize cell
    }
    else {
        return -1
    }
}

fileprivate func rowIndexForQuantityCell() -> Int {
    return totalNumberOfRowsNeeded() - 1
}

fileprivate func rowIndexForCustomizeCell() -> Int {
    if (needsRowForCustomizeItCell()) {
        return totalNumberOfRowsNeeded() - 2
    }
    return -1;
}

//old code
//    private func modifierAndOptionIndexFromIndexPath(indexPath: NSIndexPath) -> (Int, Int) {
//        //Number of rows need by each modifier accounting for expanded state. 1 if not expanded.
//        let arrayOfNumberOfRowsForEachModif = arrayOfNumberOfRowsForEachModifier()
//        //If array.count > 0, which means we have modifiers to deal with
//        if arrayOfNumberOfRowsForEachModif.count > 0 {
//            //First Modifier start index depending upon make it light row is present or not.
//            var startIndexOfCurrentModifier = 0
//            let num = numberOfRowsForTypeSection()
//            if  num > 0 {
//                startIndexOfCurrentModifier = num
//            }
//            else if needsRowForMakeItLightTableViewCell() {
//                startIndexOfCurrentModifier = 1
//            }
//            //Index of Current Modifier in Array. Start from 0
//            var currentModifierIndex = 0
//            //Loop one by one.
//            for numberOfRowsForModif in arrayOfNumberOfRowsForEachModif {
//                //Start Index of next modifier
//                let startIndexOfNextModifier = startIndexOfCurrentModifier + numberOfRowsForModif
//                //If we are exactly at the start of modifier cells
//                if indexPath.row == startIndexOfCurrentModifier {
//                    return (currentModifierIndex, -1)
//                }
//                    //Is our indexPath below the next modifiers index.
//                else if indexPath.row > startIndexOfCurrentModifier && indexPath.row <  startIndexOfNextModifier {
//                    //Now calculate index of option with-in modifier.
//                    return (currentModifierIndex, indexPath.row - startIndexOfCurrentModifier - 1)
//                }
//                //Update for next iteration
//                currentModifierIndex++
//                startIndexOfCurrentModifier = startIndexOfNextModifier
//            }
//        }
//        return (-1, -1)
//    }

//new code
fileprivate func modifierAndOptionIndexFromIndexPath(_ indexPath: IndexPath) -> (StoreMenuProductModifier?, StoreMenuProductModifierOption?,StoreMenuProductModifier?) {
    //Number of rows need by each modifier accounting for expanded state. 1 if not expanded.
    let arrayOfNumberOfRowsForEachModif = totalNoOfRowsForEachModifier()
    
    //If array.count > 0, which means we have modifiers to deal with
    if arrayOfNumberOfRowsForEachModif > 0 {
        //First Modifier start index depending upon make it light row is present or not.
        var modifierCount = 0
        let num = numberOfRowsForTypeSection()
        if  num > 0 {
            modifierCount = num
        }
        else if needsRowForMakeItLightTableViewCell() {
            modifierCount = 1
        }
        modifierCount -= 1;
        for modifier in product.storeMenuProduct!.productModifiers {
            if (!modifier.isModifierNameIsCustomizeIt()) {
                
                modifierCount += 1;
                //                    productModifiersQuantity.append(ProductModifiersMaxChoiceModel
                if indexPath.row == modifierCount {
                    return (modifier, nil, nil)
                }
                
                if isSectionExpandedForModifierId(modifier.id) {
                    // for sub modifier
                    if modifier.options.count>0 && modifier.options[0].modifiers.count>0 {
                        for option in modifier.options {
                            for smodifier in option.modifiers {
                                modifierCount += 1;
                                if indexPath.row == modifierCount {
                                    return (smodifier, option, modifier)
                                }
                                if (isSubCategoryExpandedForModifierId(smodifier.id)) {
                                    // for sub modifier options
                                    for soption in smodifier.options {
                                        modifierCount += 1;
                                        if indexPath.row == modifierCount {
                                            return (nil, soption, modifier)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        
                        // for options in root level
                        for option in modifier.options {
                            modifierCount += 1;
                            if indexPath.row == modifierCount {
                                return (nil, option, modifier)
                            }
                        }
                    }
                }
                else {
                    // if last part is root modifier
                    if indexPath.row == modifierCount && arrayOfNumberOfRowsForEachModif-1 == modifierCount{
                        let index = product.storeMenuProduct!.productModifiers.count
                        return (product.storeMenuProduct!.productModifiers[index - 1], nil, nil)
                    }
                }
            }
        }
        
        
    }
    return (nil, nil, nil)
}

fileprivate func populateQuantityCell(_ cell: ProductQuantityTableViewCell) {
    cell.quantityLabel.text = "\(userChoice.quantity)"
    cell.decreaseQuantityButton.isEnabled = userChoice.quantity > 1
    let currentQuantity = BasketService.itemsInBasket()
    cell.increaseQuantityButton.isEnabled = currentQuantity + Int64(userChoice.quantity) < 10
}

func sizeModifierForUserChoice() -> StoreMenuProductModifier? {
    var typeAnsSizeModifier = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier
    if typeAnsSizeModifier != nil {
        if  typeAnsSizeModifier!.isASimpleSizeModifier() {
            return typeAnsSizeModifier
        }
        else {
            if userChoice.selectedTypeOptionId == nil && typeAnsSizeModifier!.options.count > 0 {
                userChoice.selectedTypeOptionId = typeAnsSizeModifier!.options[0].id
            }
            let userChoiceSelectedTypeOption = userChoice.selectedTypeOptionId
            for option in typeAnsSizeModifier!.options {
                if option.id == userChoiceSelectedTypeOption { // && option.modifiers.count == 1 {
                    //return option.modifiers[0]
                    //if typeAnsSizeModifier!.options[0].id == userChoiceSelectedTypeOption {
                    
                    for optionModifier in option.modifiers {
                        if (optionModifier.isASimpleSizeModifier()) {
                            // checking option(soy protein/ Whey protein/ Make it light) has group of modifier
                            if (option.modifiers.count > 1) {
                                var modifierarray:[StoreMenuProductModifier]
                                modifierarray = option.modifiers
                                modifierarray.append(typeAnsSizeModifier!)
                                product.storeMenuProduct?.setModifiers(modifierarray)
                            }
                            return optionModifier;
                        }
                    }
                    
                    
                }
                
            }
            //                    if typeAnsSizeModifier!.options[0].id == userChoiceSelectedTypeOption {
            //                        var modifierarray:[StoreMenuProductModifier]
            //                       for optionModifier in typeAnsSizeModifier!.options[0].modifiers {
            //                            if (optionModifier.isASimpleSizeModifier()) {
            //                                modifierarray = typeAnsSizeModifier!.options[0].modifiers
            //                                modifierarray.append(typeAnsSizeModifier!)
            //                                product.storeMenuProduct?.setModifiers(modifierarray)
            //                                return optionModifier;
            //                            }
            //                        }
            //
            //
            //                    }
            
            //                }
            
        }
    }
    return nil
}

func makeItLightSelected() -> Bool {
    let makeItLightOptionId = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier?.makeItLightOptionId()
    return makeItLightOptionId != nil && makeItLightOptionId == userChoice.selectedTypeOptionId
}

func selectOptionId(_ optionId: Int64, inModifier modifier: StoreMenuProductModifier) -> Bool {
    if modifier.isAddBoostModifier(){
        // Fishbowl Event
        var itemName = ""
        let option = modifier.options.filter({$0.id == optionId})
        if let optionName = option.first?.name{
            itemName = optionName
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("\(optionId)", item_name: itemName, event_name: "ADD_BOOST")
    }
    if modifier.maxSelects != nil {
        let userChoiceSelectedOptionIds = userChoice.selectedOptionIdsForModifierId[modifier.id]
        
        if userChoiceSelectedOptionIds != nil && !userChoice.isOptionIdSelected(optionId) {
            if (userChoiceSelectedOptionIds!.count - (getUnNestedOptionId(modifier: modifier))) >= Int(modifier.maxSelects!) {
                presentOkAlert("Too Many Options", message: "No more than \(modifier.maxSelects!) options allowed from this group.")
                return false
            }
        }
    }
    
    if modifier.maxAggregateQuantity != nil {
        let totalQuantity = userChoice.getTotalQuantity(modifier.id)
        if (totalQuantity - (getUnNestedOptionId(modifier: modifier))) >= Int(modifier.maxAggregateQuantity!) {
            presentOkAlert("Too Many Options", message: "No more than \(modifier.maxAggregateQuantity!) Quantity options allowed from this group.")
            return false
        }
    }
    
    //check it is 2 level modifier
    //        if (modifier.options.count>0 && modifier.options[0].modifiers.count>0 && modifier.options[0].modifiers[0].options.count>0) {
    if (modifier.isModifierNameIsAddYummyExtras()) {
        //if it is 2 level modifier then add root level modifier & option then 2nd level modifier & option
        let optionModifier = getCurrentModifierAndOption(inModifier: modifier, optionId: optionId)
        userChoice.addSelectedOptionIdForModifierId(optionModifier.0!, optionId: optionId)
        userChoice.addSelectedOptionIdForModifierId(modifier.id, optionId: optionModifier.1!)
        
    } else {
        //if it is a one level modifier
        userChoice.addSelectedOptionIdForModifierId(modifier.id, optionId: optionId)
    }
    
    if modifier.optionIdForUnnestedOption != nil {
        userChoice.addSelectedOptionIdForModifierId(modifier.id, optionId: modifier.optionIdForUnnestedOption!)
    }
    
    return true
}

func deselectOptionId(_ optionId: Int64, inModifier modifier: StoreMenuProductModifier) {
    
    if modifier.isAddBoostModifier(){
        // Fishbowl Event
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("\(optionId)", item_name: "", event_name: "REMOVE_BOOST")
    }
    
    //check it is 2 level modifier
    //        if (modifier.options.count>0) {
    //            if (modifier.options[0].modifiers.count>0) {
    if (modifier.isModifierNameIsAddYummyExtras()) {
        //if it is 2 level modifier then remove root level modifier & option then 2nd level modifier & option
        let optionModifier = getCurrentModifierAndOption(inModifier: modifier, optionId: optionId)
        userChoice.removeSelectedOptionIdForModifierId(optionModifier.0!, optionId: optionId)
        let selectedOptionIdsForModifierId = userChoice.selectedOptionIdsForModifierId[optionModifier.0!]
        //when there is no option selected in 2nd level then remove top level option
        if selectedOptionIdsForModifierId != nil && selectedOptionIdsForModifierId!.count == 1 {
            userChoice.removeSelectedOptionIdForModifierId(modifier.id, optionId: optionModifier.1!)
            
        }
    } else {
        userChoice.removeSelectedOptionIdForModifierId(modifier.id, optionId: optionId)
    }
    //If we had unnested modifier
    if modifier.optionIdForUnnestedOption != nil {
        let selectedOptionIdsForModifierId = userChoice.selectedOptionIdsForModifierId[modifier.id]
        if selectedOptionIdsForModifierId != nil && selectedOptionIdsForModifierId!.count == 1 {
            userChoice.removeSelectedOptionIdForModifierId(modifier.id, optionId: modifier.optionIdForUnnestedOption!)
        }
    }
}

// MARK: Cell Delegates

func productQuantityTableViewCellDidReceiveDecreaseEvent(_ cell: ProductQuantityTableViewCell) {
    if userChoice.quantity > 1 {
        userChoice.quantity -= 1;
        
    }
    populateQuantityCell(cell)
    updateCost()
}

func productQuantityTableViewCellDidReceiveIncreaseEvent(_ cell: ProductQuantityTableViewCell) {
    let currentQuantity = BasketService.itemsInBasket()
    if currentQuantity + 1 <= 10 {
        userChoice.quantity += 1;
        
    }
    populateQuantityCell(cell)
    updateCost()
}

func makeItLightTableViewCellDidChangeSwitchState(_ cell: MakeItLightTableViewCell) {
    let isMakeItLight = cell.switchControl.isOn
    let optionId: Int64!
    if isMakeItLight {
        optionId = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!.makeItLightOptionId()!
    }
    else {
        optionId = product.storeMenuProduct!.productTypeAndSizeTopLevelModifier!.classicOptionId()!
    }
    userChoice.selectedTypeOptionId = optionId
    userChoice.selectedSizeOptionId = nil
    updateProductSizes()
    updateCost()
    resetTableView()
}

func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
    return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: 16)
}

func textFieldShouldReturn(_ textField: UITextField) -> Bool {
    trackKeyboardReturn()
    textField.resignFirstResponder()
    return false
}

@objc func textFieldEditingChanged(_ textField: UITextField) {
    userChoice.specialInstructions = textField.text ?? ""
}

//MARK: Actions

@IBAction func productSizeSelected(_ sender: SelectableButton) {
    let subviews = productSizesView.subviews as! [SelectableButton]
    for subview in subviews {
        subview.isSelected = false
    }
    sender.isSelected = true
    let index = sender.tag
    
    let sizeModifier = sizeModifierForUserChoice()
    let option = sizeModifier!.options[index]
    userChoice.selectedSizeOptionId = option.id
    selectedSizeButtonIndex = index
    updateCost()
}


// MARK: Cost

fileprivate func updateCost() {
    let cost = calculateCost()
    delegate?.costUpdated(cost)
}

fileprivate func updateCostForCart(_ cost:Double) {
    delegate?.costUpdated(cost)
}

fileprivate func calculateCost() -> Double {
    if product.storeMenuProduct == nil || product.storeMenuProduct!.hasPopulatedModifiers == false {
        return 0
    }
    var cost = product.storeMenuProduct!.cost//The base cost
    if userChoice.selectedSizeOptionId != nil {
        let sizeModifier = sizeModifierForUserChoice()
        for sizeOption in sizeModifier!.options {
            if sizeOption.id == userChoice.selectedSizeOptionId {
                cost += sizeOption.cost
            }
        }
    }
    if userChoice.selectedOptionIdsForModifierId.count > 0 {
        let modifiers = product.storeMenuProduct!.productModifiers
        for modifier in modifiers {
            if modifier.isModifierNameIsCustomizeIt() {
                continue
            }
            
            let isAddBoostModifier = modifier.isAddBoostModifier()
            
            for option in modifier.options {
                //                    if userChoice.isOptionIdSelected(option.id) {
                //                        cost += option.cost
                //                        if !isAddBoostModifier {
                //                            BasketService.sharedBasket!.selectedCustomiseOptionIdsTrack.insert(option.id)
                //                        }
                //                    }
                let quantity = userChoice.isOptionIdSelectedGetQuantity(option.id)
                if quantity > 0 {
                    cost += (option.cost * Double(quantity))
                    if !isAddBoostModifier {
                        BasketService.sharedBasket!.selectedCustomiseOptionIdsTrack.insert(option.id)
                    }
                }
                //for sub level option selected
                for smodifiers in option.modifiers {
                    for soptions in smodifiers.options {
                        if userChoice.isOptionIdSelected(soptions.id) {
                            cost += soptions.cost
                            BasketService.sharedBasket!.selectedCustomiseOptionIdsTrack.insert(soptions.id)
                        }
                    }
                }
            }
        }
    }
    
    if !userChoice.selectedCustomizeOptionIds.isEmpty {
        // get customize it modifier
        let modifiers = product.storeMenuProduct!.productModifiers
        var customizeModifier:[StoreMenuProductModifier]=[]
        for modifier in modifiers {
            if (modifier.isModifierNameIsCustomizeIt()) {
                customizeModifier.append(modifier)
            }
        }
        // EventTracking
        var modifierCost:Double = 0.0
        var modifierOptionId = ""
        // update selected substituion cost
        for option in userChoice.selectedCustomizeOptionIds {
            let optionId = option
            if let suboption = getOptionCost(optionId, modifiers: customizeModifier) {
                cost += suboption.cost
                // Event Tracking
                modifierOptionId = modifierOptionId + "\(optionId)"
                modifierCost = modifierCost + suboption.cost
                BasketService.sharedBasket!.selectedCustomiseOptionIdsTrack.insert(suboption.id)
            }
        }
    }
    
    
    return cost * Double(userChoice.quantity)
}

// get selected option cost based on option id in the given modifier list
func getOptionCost(_ optionId: Int64, modifiers:[StoreMenuProductModifier]) -> StoreMenuProductModifierOption? {
    for modifier in modifiers {
        for option in modifier.options {
            if optionId == option.id {
                return option
            }else if (!option.modifiers.isEmpty){
                if let soption = getOptionCost(optionId, modifiers: option.modifiers) {
                    if soption.cost > 0 {
                        return soption
                    }
                }
            }
        }
    }
    return nil
}


// MARK: Notifications

@objc func keyboardDidShow(_ notification: Notification) {
    delegate?.keyboardDidShow()
}

// Substitution view controller delegate
func closeModelScreen(_ optionIds:[Int64],removeSubstitutionModels:[RemoveSubstitutionModel]) {
    StatusBarStyleManager.pushStyle(.default, viewController: self)
    print("option id in available \(optionIds)")
    userChoice.selectedCustomizeOptionIds=[]
    userChoice.selectedCustomizeOptionIds = optionIds
    subRemoveSubstitutionModels = removeSubstitutionModels
    updateCost()
}
// MARK: Navigation
override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
    if segue.identifier == "SubstittutionSegue" {
        
        var customizeProductModifier:StoreMenuProductModifier?
        let custProduct = product.storeMenuProduct!
        
        for modifiers in custProduct.productModifiers {
            let modifier = modifiers;
            if(modifier.isModifierNameIsCustomizeIt()){
                customizeProductModifier = modifier
                substitutionsViewController = segue.destination as? SubstitutionsViewController
                
                substitutionsViewController!.customizeProductModifier=customizeProductModifier
                substitutionsViewController!.selectedOptionIds.removeAll()
                substitutionsViewController!.removeSubstitutionModels = []
                substitutionsViewController!.removeSubstitutionModels = subRemoveSubstitutionModels
                
                for optionId in userChoice.selectedCustomizeOptionIds {
                    substitutionsViewController!.selectedOptionIds.insert(optionId)
                }
                
                substitutionsViewController!.delegate=self
                break;
            }
        }
        
        
        
        
        /*  let customizeTVC = sender as! CustomizeTableViewCell?
         if(customizeTVC != nil){
         substitutionsViewController = segue.destinationViewController as? SubstitutionsViewController
         // substitutionsViewController?.customizeTVC=customizeTVC
         // substitutionsViewController?.delegate=self
         }*/
    }
}

//get 2nd level modifier based 2nd level option id with root level modifier
func getCurrentModifierAndOption(inModifier rootLevelModifier:StoreMenuProductModifier, optionId:Int64) ->(Int64?,Int64?) {
    for options in rootLevelModifier.options {
        if (options.id == optionId) {
            return (rootLevelModifier.id,options.id)
        }
        for smodifiers in options.modifiers {
            for sOption in smodifiers.options {
                if (sOption.id == optionId) {
                    return (smodifiers.id,options.id)
                }
            }
        }
    }
    return (nil,nil)
}

//to reset the values for modifiers when change the option(soy protein/ Whey protein/ Make it light) present
func resetTableView() {
    selectedSizeButtonIndex = 0
    userChoice.selectedOptionIdsForModifierId.removeAll()
    modifierIdsWithExpandedSubCategoryOptions.removeAll()
    modifierIdsWithExpandedOptions.removeAll()
    tableView.reloadData()
    delegate?.productDetailAvailableTableViewContentChanged()
}

//show the picker for quantity from product detail screen
func showProductQuantityPickerView(_ index:Int, selectedValue:String, parentModifier: StoreMenuProductModifier, option: StoreMenuProductModifierOption) {
    currentIndex = IndexPath(row: index, section: 0)
    delegate?.showProductQuantityPickerView(selectedValue, parentModifier: parentModifier, option: option)
}

//update the quantity selected for the option
func saveProductModifierQuantity(_ value:String) {
    if let indexPath = currentIndex {
        let cellType = tableView.cellForRow(at: indexPath)
        if let cell = cellType as? BoostTableViewCell {
            cell.updatePriceLabel(value)
            _ = userChoice.updateQuantity(cell.option.id, quantity: Int(value)!)
            updateCost()
        }
    }
}

func getUnNestedOptionId(modifier:StoreMenuProductModifier) -> Int{
    if modifier.optionIdForUnnestedOption != nil && userChoice.isOptionIdSelected(modifier.optionIdForUnnestedOption!){
        return 1
    }
    return 0
}

}