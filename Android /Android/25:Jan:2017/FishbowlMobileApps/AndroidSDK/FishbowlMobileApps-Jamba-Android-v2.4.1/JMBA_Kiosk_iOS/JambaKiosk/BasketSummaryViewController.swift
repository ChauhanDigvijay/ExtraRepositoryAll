//
//  BasketSummaryViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/16/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

protocol BasketSummaryViewControllerDelegate {
    func didRemoveProductFromBasket()
    func didEmptyBasket()
}

class BasketSummaryViewController: UIViewController, BasketViewAdapterDelegate {

    var delegate: BasketSummaryViewControllerDelegate?
    private let basketViewAdapter = BasketViewAdapter()

    @IBOutlet var tableView: UITableView!
    @IBOutlet var subtotalLabel: UILabel!
    @IBOutlet var discountLabel: UILabel!
    @IBOutlet var taxLabel: UILabel!
    @IBOutlet var totalLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        basketViewAdapter.delegate = self
        basketViewAdapter.configureTableView(tableView)
    }

    func updateTotals() {
        guard let basket = BasketService.sharedInstance.currentBasket else {
            delegate?.didEmptyBasket() // Assume user removed all products
            return
        }
        subtotalLabel.text = String(format:  "$%.2f", basket.subTotal)
        discountLabel.text = String(format: "-$%.2f", basket.discount)
        taxLabel.text      = String(format:  "$%.2f", basket.salesTax)
        totalLabel.text    = String(format:  "$%.2f", basket.total)
    }


    // MARK: BasketViewAdapterDelegate

    func didReloadBasketData() {
        updateTotals()
    }

    func didRemoveProductFromBasket(product: BasketProduct, error: NSError?) {
        if error != nil {
            self.presentError(error)
        } else {
            delegate?.didRemoveProductFromBasket()
        }
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
