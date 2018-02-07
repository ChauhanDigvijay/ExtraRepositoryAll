//
//  BasketViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/13/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

protocol BasketViewControllerDelegate {
    func didEmptyBasket()
    func didProceedToCheckout()
}

class BasketViewController: UIViewController, BasketViewAdapterDelegate {

    var delegate: BasketViewControllerDelegate?
    private let basketViewAdapter = BasketViewAdapter()

    @IBOutlet var tableView: UITableView!
    @IBOutlet var checkoutButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        basketViewAdapter.delegate = self
        basketViewAdapter.configureTableView(tableView)
    }


    // MARK: - Navigation

    @IBAction func checkOut(sender: UIButton) {
        trackButtonPressWithName("Check Out")
        delegate?.didProceedToCheckout()
    }


    // MARK: BasketViewAdapterDelegate

    func didReloadBasketData() {
        guard let basket = BasketService.sharedInstance.currentBasket else {
            delegate?.didEmptyBasket() // Assume user removed all products
            return
        }
        if basket.products.count == 0 {
            delegate?.didEmptyBasket() // Assume user removed all products
        }
        let buttonTitle = String(format: "Check Out      $%.2f", basket.total)
        checkoutButton.setTitle(buttonTitle, forState: .Normal)
    }

    func didRemoveProductFromBasket(product: BasketProduct, error: NSError?) {
        self.presentError(error)
    }

    func clearBasket() {
        trackButtonPressWithName("Clear Basket")
        presentConfirmation("Empty Basket", message: "Are you sure you would like to empty the basket?", buttonTitle: "Empty Basket") { confirmed in
            if confirmed {
                BasketService.sharedInstance.deleteBasket()
                self.delegate?.didEmptyBasket()
            }
        }
    }

}
