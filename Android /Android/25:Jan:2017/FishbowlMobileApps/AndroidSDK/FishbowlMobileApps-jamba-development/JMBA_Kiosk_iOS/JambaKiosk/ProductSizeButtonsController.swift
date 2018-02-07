//
//  ProductSizeButtonsController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/10/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

protocol ProductSizeButtonsControllerDelegate {
    func setSelectedProductSize(sizeOption: StoreMenuProductModifierOption?)
    func selectedSizeOptionIdFromUserChoice() -> Int64?
}

class ProductSizeButtonsController {

    var delegate: ProductSizeButtonsControllerDelegate?
    var product: Product?
    var containerView: UIView?

    private var sizeOptions: [StoreMenuProductModifierOption] = []

    // Cache selected button index for cases where user choice gets reset (make it light)
    private(set) var selectedSizeButtonIndex = 0

    func updateSizeButtons(sizeOptions: [StoreMenuProductModifierOption]?) {
        removeExistingButtons()

        // Product menu has not been fetched
        if product?.storeMenuProduct == nil {
            return
        }

        self.sizeOptions = sizeOptions ?? []

        // We always show at least one button
        let sizeCount = self.sizeOptions.count
        let buttonCount = max(1, sizeCount)

        if selectedSizeButtonIndex == 0 {

        }
        if let selectedSizeOptionId = delegate?.selectedSizeOptionIdFromUserChoice() {
            let modifierIds = self.sizeOptions.map { $0.modifierId }
            if let index: Int = modifierIds.indexOf(selectedSizeOptionId) {
                selectedSizeButtonIndex = index
            }
        }
        selectedSizeButtonIndex = min(selectedSizeButtonIndex, buttonCount - 1)

        delegate?.setSelectedProductSize(self.sizeOptions.getAt(selectedSizeButtonIndex))

        addSizeButtons(buttonCount)
        selectButton(selectedSizeButtonIndex)
    }

    func selectButton(selectedButton: UIButton) {
        guard let buttons = containerView?.subviews as? [UIButton] else {
            return
        }
        for button in buttons {
            button.selected = false
            button.backgroundColor = UIColor.whiteColor()
        }
        // Highlight only if more than one button is available
        if buttons.count > 1 {
            selectedButton.selected = true
            selectedButton.backgroundColor = UIColor(hex: Constants.jambaLightBlueColor)
        } else {
            selectedButton.backgroundColor = UIColor(hex: Constants.jambaLightGrayColor)
        }
        selectedSizeButtonIndex = selectedButton.tag
    }

    func selectButton(index: Int) {
        guard let button = (containerView?.subviews as? [UIButton])?.getAt(index) else {
            return
        }
        selectButton(button)
    }

    private func addSizeButtons(count: Int) {
        let fullWidth = containerView?.frame.width ?? 0
        let width = ((fullWidth - CGFloat(count - 1)) / CGFloat(count))
        for index in 0..<count {
            addButton(width, index: index)
        }
    }

    private func addButton(width: CGFloat, index: Int) -> UIButton {
        let button = UIButton()

        if let sizeOption = sizeOptions.getAt(index) {
            setTitleForSizeOptionButton(sizeOption, button: button)
        } else {
            setTitleForSingleButton(button, cost: product?.storeMenuProduct?.cost ?? 0)
        }

        let x = (width + 1.0) * CGFloat(index)
        button.frame = CGRectMake(x, 0, width, containerView?.frame.height ?? 0)
        button.addTarget(self, action: "productSizeSelected:", forControlEvents: .TouchUpInside)
        button.tag = index
        button.titleLabel?.adjustsFontSizeToFitWidth = true
        button.titleLabel?.minimumScaleFactor = 0.5
        button.titleLabel?.numberOfLines = 0
        button.titleLabel?.textAlignment = .Center
        containerView?.addSubview(button)
        return button
    }

    private func setTitleForSingleButton(button: UIButton, cost: Double) {
        let textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        let sizeFont = UIFont(name: "Archer-Bold", size: 17)!
        let sizeAttributes: [String: AnyObject] = [NSFontAttributeName: sizeFont, NSForegroundColorAttributeName: textColor]
        let productPriceString = String(format: "$%.2f", cost)
        let productPriceAttrString = NSAttributedString(string: productPriceString, attributes: sizeAttributes)
        button.setAttributedTitle(productPriceAttrString, forState: .Normal)
    }

    private func setTitleForSizeOptionButton(sizeOption: StoreMenuProductModifierOption, button: UIButton) {
        let textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        let mutableTitleAttributedString = NSMutableAttributedString()
        let sizeFont = UIFont(name: "Archer-Bold", size: 17)!
        let sizeAttributes: [String: AnyObject] = [NSFontAttributeName : sizeFont, NSForegroundColorAttributeName : textColor]

        // Size Attributed String
        let sizeAttrString = NSAttributedString(string: "\(sizeOption.name)", attributes: sizeAttributes)

        // Price Attributed String
        let priceFont = UIFont(name: "HelveticaNeue", size: 13)!
        let priceAttributes: [String: AnyObject] = [NSFontAttributeName: priceFont, NSForegroundColorAttributeName: textColor]
        let priceString = String(format: "$%.2f", sizeOption.cost)
        let priceAttrString = NSAttributedString(string: "\n\(priceString)", attributes: priceAttributes)

        // Final title
        mutableTitleAttributedString.appendAttributedString(sizeAttrString)
        mutableTitleAttributedString.appendAttributedString(priceAttrString)
        button.setAttributedTitle(mutableTitleAttributedString.copy() as? NSAttributedString, forState: .Normal)
    }

    private func removeExistingButtons() {
        if let buttons = containerView?.subviews {
            for button in buttons {
                button.removeFromSuperview()
            }
        }
    }


    // MARK: Actions

    @IBAction func productSizeSelected(sender: UIButton) {
        selectButton(sender)
        delegate?.setSelectedProductSize(sizeOptions.getAt(selectedSizeButtonIndex))
    }

}
