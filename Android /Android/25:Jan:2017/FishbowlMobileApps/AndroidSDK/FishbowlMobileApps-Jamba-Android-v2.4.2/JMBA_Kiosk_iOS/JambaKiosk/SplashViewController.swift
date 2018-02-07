//
//  SplashViewController.swift
//  JambaKiosk
//
//  Created by Kieran Culliton on 9/14/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class SplashViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        if StoreService.sharedInstance.currentStore == nil {
            navigationController?.popToRootViewController()
            return
        }

        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpirationWarning:", name: SessionExpirationService.sessionAboutToExpireNotificatioName, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpired:", name: SessionExpirationService.sessionExpiredNotificatioName, object: nil)
    }

    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }

    override func viewDidAppear(animated: Bool) {
        trackScreenView()

        // Remove previous controllers from stack, if any (store configuration)
        if navigationController?.viewControllers.count > 0 {
            navigationController?.viewControllers = [self]
        }

        // Pre-create basket and pre-load products
        createBasket()
        loadProducts()
    }


    // MARK: - Navigation

    @IBAction func startNewOrder(sender: UIButton) {
        if ProductDataProvider.productTree.count == 0 {
            SVProgressHUD.showWithStatus("Starting new order...", maskType: .Black)
            loadProducts {
                self.startNewOrder(sender)
            }
            return
        }

        if BasketService.sharedInstance.currentBasket == nil {
            SVProgressHUD.showWithStatus("Starting new order...", maskType: .Black)
            createBasket {
                self.startNewOrder(sender)
            }
            return
        }

        SVProgressHUD.dismiss()
        startKioskSession()
    }

    private func loadProducts(complete: (Void -> Void)? = nil) {
        guard let currentStore = StoreService.sharedInstance.currentStore else {
            self.presentOkAlert("Error", message: "Store not properly configured")
            return
        }
        ProductDataProvider.loadProductsForStore(currentStore) {
            complete?()
        }
    }

    private func createBasket(complete: (Void -> Void)? = nil) {
        BasketService.sharedInstance.createBasket { error in
            if error != nil {
                self.presentError(error)
            } else {
                complete?()
            }
        }
    }

    private func startKioskSession() {
        SessionExpirationService.sharedInstance.startKioskSession()
        performSegueWithIdentifier("BeginOrder", sender: nil)
    }


    // MARK: Notifications

    func sessionExpirationWarning(notification: NSNotification) {
        // If another modal is visible, let them handle the session expiration notification
        if presentedViewController == nil {
            performSegueWithIdentifier("SessionExpirationSegue", sender: self)
        }
    }

    // Main business logic to reset Kiosk status after an order is complete or expired
    // (sessionExpired notification is fired both when the user completes the order and when the session expires)
    func sessionExpired(notification: NSNotification) {
        // Reset Kiosk state
        UserService.logoutUser()
        BasketService.sharedInstance.deleteBasket()
        CreditCardService.sharedInstance.reset()
        RewardService.sharedInstance.reset()

        // Force navigation back to Splash screen
        dismissModalController()  // Close any modal controllers
        popToRootViewController() // Close any child controllers
    }

}
