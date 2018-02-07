//
//  ProductMenuViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/9/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class ProductMenuViewController: UIViewController, ProductCollectionViewControllerDelegate, ProductDetailViewControllerDelegate, BasketViewControllerDelegate {

    @IBOutlet var allProductsButton: UIButton!
    @IBOutlet var sessionExpirationLabel: UILabel!

    private let productCollectionSegue = "ProductCollectionSegue"
    private let productDetailSegue = "ProductDetailSegue"
    private var productContainerViewController: SwappableContainerViewController?

    private let basketSegue = "BasketSegue"
    private let basketEmptySegue = "EmptyBasketSegue"
    private var basketContainerViewController: SwappableContainerViewController?

    override func viewDidLoad() {
        super.viewDidLoad()

        sessionExpirationLabel.hidden = true
        showProductCollection()
        updateBasketStatus()

        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpirationUpdate:", name: SessionExpirationService.secondsLeftNotificatioName, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpirationReset:", name: SessionExpirationService.sessionResetNotificationName, object: nil)
    }

    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }

    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        // Refresh basket status in case user came back from checkout screen
        updateBasketStatus()
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    private func updateBasketStatus() {
        let productCount = BasketService.sharedInstance.currentBasket?.products.count ?? 0
        if productCount > 0 {
            showBasketWithProducts()
        } else {
            showEmptyBasket()
        }
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ProductContainerSegue" {
            productContainerViewController = segue.destinationViewController as? SwappableContainerViewController
        } else if segue.identifier == "BasketContainerSegue" {
            basketContainerViewController = segue.destinationViewController as? SwappableContainerViewController
        }
    }


    // MARK: Navigation

    @IBAction func showAllProducts(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        showProductCollection()
    }

    @IBAction func cancelOrder(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        performSegueWithIdentifier("CancelOrderSegue", sender: self)
    }


    // Product Container Swapping

    private func showProductCollection() {
        allProductsButton.hidden = true
        productContainerViewController?.swapToViewControllerWithSegueIdentifier(productCollectionSegue, viewControllerWillLoad: { viewController in
            let vc = viewController as? ProductCollectionViewController
            vc?.delegate = self
        })
    }

    private func showProductDetail(product: Product) {
        allProductsButton.hidden = false
        productContainerViewController?.swapToViewControllerWithSegueIdentifier(productDetailSegue, viewControllerWillLoad: { viewController in
            let vc = viewController as? ProductPageViewController
            vc?.productDetailDelegate = self
            vc?.initialProduct = product
        }, viewControllerWillAppear: { viewController in
            let vc = viewController as? ProductPageViewController
            vc?.initializeWithProduct(product)
        })
    }


    // Basket Container Swapping

    private func showBasketWithProducts() {
        basketContainerViewController?.swapToViewControllerWithSegueIdentifier(basketSegue, viewControllerWillLoad: { viewController in
            let vc = viewController as? BasketViewController
            vc?.delegate = self
        })
    }

    private func showEmptyBasket() {
        basketContainerViewController?.swapToViewControllerWithSegueIdentifier(basketEmptySegue, viewControllerWillLoad: { viewController in
            // Nothing to do
        })
    }


    // MARK: ProductCollectionViewControllerDelegate

    func didSelectProductFromCollection(product: Product) {
        SessionExpirationService.sharedInstance.trackUserActivity()
        showProductDetail(product)
    }


    // MARK: ProductDetailViewControllerDelegate

    func didAddProductToBasket(product: Product) {
        SessionExpirationService.sharedInstance.trackUserActivity()
        updateBasketStatus()
    }


    // MARK: BasketViewControllerDelegate

    func didEmptyBasket() {
        SessionExpirationService.sharedInstance.trackUserActivity()
        updateBasketStatus()
    }

    func didProceedToCheckout() {
        trackButtonPressWithName("Check Out")
        SessionExpirationService.sharedInstance.trackUserActivity()
        performSegueWithIdentifier("CheckOutSegue", sender: self)
    }


    // MARK: Notifications

    func sessionExpirationReset(notification: NSNotification) {
        sessionExpirationLabel.hidden = true
    }

    func sessionExpirationUpdate(notification: NSNotification) {
        let secondsLeft: Int = notification.userInfo?[SessionExpirationService.secondsLeftKey] as? Int ?? 0
        sessionExpirationLabel.hidden = false
        sessionExpirationLabel.text = "Your session will expire in \(secondsLeft) seconds..."
    }

}
