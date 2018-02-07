//
//  BasketViewAdapter.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/16/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation
import MGSwipeTableCell

protocol BasketViewAdapterDelegate {
    func didReloadBasketData()
    func didRemoveProductFromBasket(product: BasketProduct, error: NSError?)
    func clearBasket()
}

// NSObject required to be delegate of table view
class BasketViewAdapter: NSObject, UITableViewDataSource, UITableViewDelegate, UIScrollViewDelegate, MGSwipeTableCellDelegate, ClearBasketTableViewCellDelegate {

    var delegate: BasketViewAdapterDelegate?

    private var tableView: UITableView?
    private var basketProducts: [BasketProduct] = []

    func configureTableView(tableView: UITableView) {
        self.tableView = tableView
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 73
        tableView.rowHeight = UITableViewAutomaticDimension
        reloadData()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "addBasketProduct:", name: BasketService.addProductNotificationName, object: nil)
    }

    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }

    private func reloadData() {
        basketProducts = BasketService.sharedInstance.currentBasket?.products ?? []
        tableView?.reloadData()
        delegate?.didReloadBasketData()
    }


    // MARK: UITableViewDataSource

    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 2
    }

    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return basketProducts.count
        } else if section == 1 {
            return 1
        }
        fatalError("Invalid section")
    }

    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            guard let cell = tableView.dequeueReusableCellWithIdentifier("ProductCell") as? BasketProductCell else {
                fatalError("Could not load cell")
            }
            guard let basketProduct = basketProducts.getAt(indexPath.row) else {
                fatalError("Could not load product")
            }
            cell.delegate = self
            cell.update(basketProduct)
            return cell
        } else if indexPath.section == 1 {
            guard let cell = tableView.dequeueReusableCellWithIdentifier("EmptyBasketCell") as? ClearBasketTableViewCell else {
                fatalError("Could not load cell")
            }
            cell.delegate = self
            return cell
        }
        fatalError("Invalid section")
    }


    // MARK: UITableViewDelegate

    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        if indexPath.section == 0 {
            guard let cell = tableView.cellForRowAtIndexPath(indexPath) as? BasketProductCell else {
                fatalError("Could not load cell")
            }
            SessionExpirationService.sharedInstance.trackUserActivity()
            cell.showSwipe(.RightToLeft, animated: true)
        }
    }


    // MARK: UIScrollViewDelegate

    func scrollViewWillBeginDragging(scrollView: UIScrollView) {
        SessionExpirationService.sharedInstance.trackUserActivity()
    }


    // MARK: MGSwipeTableCellDelegate

    func swipeTableCell(cell: MGSwipeTableCell!, tappedButtonAtIndex index: Int, direction: MGSwipeDirection, fromExpansion: Bool) -> Bool {
        if let indexPath = tableView?.indexPathForCell(cell) {
            removeProduct(indexPath.row)
            return true
        }
        return false
    }

    private func removeProduct(index: Int) {
        if let product = BasketService.sharedInstance.currentBasket?.products.getAt(index) {
            BasketService.sharedInstance.removeProduct(product) { error in
                self.delegate?.didRemoveProductFromBasket(product, error: error)
                self.reloadData()
            }
        }
    }


    // MARK: ClearBasketTableViewCellDelegate

    func clearBasket() {
        delegate?.clearBasket()
    }


    // MARK: Notifications

    func addBasketProduct(notification: NSNotification) {
        reloadData()
    }

}
