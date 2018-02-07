//
//  ProductDetailViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Haneke

protocol ProductDetailViewControllerDelegate {
    func didAddProductToBasket(product: Product)
}


class ProductDetailViewController: UIViewController, UIScrollViewDelegate, ProductDetailAvailableViewControllerDelegate {

    @IBOutlet var scrollView: UIScrollView!
    @IBOutlet var scrollViewContentHeightConstraint: NSLayoutConstraint!
    @IBOutlet var productImageView: UIImageView!
    @IBOutlet var productNameLabel: UILabel!
    @IBOutlet var productIngredientsLabel: UILabel!
    @IBOutlet var addToCartView: UIView!
    @IBOutlet var cartTotalAmountLabel: UILabel!

    var delegate: ProductDetailViewControllerDelegate?
    var product: Product!  // Product must be set before loading this VC

    private var productAvailableViewController: ProductDetailAvailableViewController?

    override func viewDidLoad() {
        super.viewDidLoad()

        productNameLabel.text = product.name
        productIngredientsLabel.text = product.ingredients.lowercaseString
        if let url = NSURL(string: product.imageUrl) {
            productImageView.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
        }
        loadModifiersIfNeeded()
        updateScreen()
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ProductAvailableSegue" {
            productAvailableViewController = segue.destinationViewController as? ProductDetailAvailableViewController
            productAvailableViewController?.product = product
            productAvailableViewController?.delegate = self
        }
    }

    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        updateScreen()
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        updateScreen()
        AnalyticsService.trackEvent("view", action: "product_view", label: product.name)
    }

    private func heightForScrollViewContent() -> CGFloat {
        let height = productAvailableViewController!.contentHeight() + productImageView.frame.height
        log.verbose("New scroll view content height: \(height)")
        return height
    }

    private func updateScreen() {
        // Adjust scroll view content height
        let minScrollViewContentHeight = max(CGFloat(610), scrollView.frame.height)
        scrollViewContentHeightConstraint.constant = max(heightForScrollViewContent(), minScrollViewContentHeight)
        productAvailableViewController?.updateScreen()
    }

    private func loadModifiersIfNeeded() {
        if product.storeMenuProduct == nil || product.storeMenuProduct!.hasPopulatedModifiers {
            return
        }

        log.verbose("Loading product modifiers: \(self.product.name)")
        StoreService.sharedInstance.loadModifiersForProduct(product.storeMenuProduct!.productId) { (modifiers, error) in
            if error != nil {
                self.presentError(error)
                return
            }
            self.product.storeMenuProduct?.setModifiers(modifiers)
            self.updateScreen() // Adjust scrollview content height
        }
    }


    // MARK: UIScrollViewDelegate

    func scrollViewWillBeginDragging(scrollView: UIScrollView) {
        SessionExpirationService.sharedInstance.trackUserActivity()
    }


    // MARK: ProductAvailableViewControllerDelegate

    func productDetailAvailableTableViewContentChanged() {
        updateScreen()
    }

    func costUpdated(cost: Double) {
        SessionExpirationService.sharedInstance.trackUserActivity()
        cartTotalAmountLabel.text = String(format: "$%.2f", cost)
    }


    // MARK: User actions

    /// Add current product with selected options to basket
    /// Requirements: User must have started an order and basket must exist
    @IBAction func addToBasket(sender: UIButton) {
        trackButtonPress(sender)

        sender.enabled = false
        createBasketIfNeeded {
            if self.validateChoices() == false {
                sender.enabled = true
                return
            }

            let options = self.optionIdsFromUserChoice()
            BasketService.sharedInstance.addProduct(self.product, quantity: 1, options: options, specialInstructions: "") { error in
                sender.enabled = true
                if error != nil {
                    self.presentError(error)
                    return
                }
                self.delegate?.didAddProductToBasket(self.product)
            }
        }
    }

    private func createBasketIfNeeded(complete: Void -> Void) {
        if BasketService.sharedInstance.currentBasket != nil {
            complete()
            return
        }
        BasketService.sharedInstance.createBasket { error in
            if error != nil {
                self.presentError(error)
                return
            }
            complete()
        }
    }

    private func validateChoices() -> Bool {
        let existingQuantity = BasketService.sharedInstance.numberOfItemsInBasket()
        //If we already have 10 items in basket
        if existingQuantity >= 10 {
            presentOkAlert("Too Many Items", message: "The maximum number of items allowed per order is 10")
            return false
        }
        return validateUserChoice()
    }

    private func optionIdsFromUserChoice() -> [String] {
        let userChoice = productAvailableViewController!.userChoice
        var optionsAsInt64Ids = Set<Int64>()
        for (_, seletedOptionIdsForModifierID) in userChoice.selectedOptionIdsForModifierId {
            optionsAsInt64Ids.unionInPlace(seletedOptionIdsForModifierID)
        }
        if userChoice.selectedTypeOptionId != nil {
            optionsAsInt64Ids.insert(userChoice.selectedTypeOptionId!)
        }
        if userChoice.selectedSizeOptionId != nil {
            optionsAsInt64Ids.insert(userChoice.selectedSizeOptionId!)
        }
        return optionsAsInt64Ids.map { "\($0)" }
    }

    private func validateUserChoice() -> Bool {
        var errorString = ""
        if product.storeMenuProduct != nil {
            for productModifier in product.storeMenuProduct!.productModifiers {
                var subtract = 0
                if productModifier.optionIdForUnnestedOption != nil {
                    subtract = 1
                }
                let maxSelects = productModifier.maxSelects
                let minSelects = productModifier.minSelects
                let userChoice = productAvailableViewController!.userChoice
                let selectedOptionIdsForModifierId = userChoice.selectedOptionIdsForModifierId[productModifier.modifierId]
                if selectedOptionIdsForModifierId != nil {
                    if maxSelects != nil {
                        if Int64(selectedOptionIdsForModifierId!.count - subtract) > maxSelects! {
                            errorString += "Please select up to \(productModifier.maxSelects!) options from the group \(productModifier.name)"
                        }
                    }
                    if minSelects != nil {
                        if Int64(selectedOptionIdsForModifierId!.count - subtract) < minSelects! {
                            errorString += "Please select at least \(productModifier.minSelects!) options from the group \(productModifier.name)"
                        }
                    }
                }
            }
        }
        if errorString.characters.count > 0 {
            presentOkAlert("Invalid Options", message: errorString)
            return false
        }
        return true
    }

}
