//
//  MyRewardDetailViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l > r
  default:
    return rhs < lhs
  }
}


class MyRewardDetailViewController: UITableViewController {
    
    @IBOutlet weak var startNewOrderButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.estimatedRowHeight = 44
        tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        tableView.reloadData() // For autolayout
    }
 
    @IBAction func startNewOrder(_ sender: UIButton) {
        trackButtonPress(sender)
        
        // Check if a basket already exists and has at least one product
        if BasketService.sharedBasket?.products.count > 0 {
            // Ask user for confirmation to wipe out current basket
            // If user confirms, wipe basket and start new order
            presentConfirmation("Choose Another Store", message: "Choosing a different store will empty the basket and cancel the current order. Continue?", buttonTitle: "Choose Different Store") { confirmed in
                if confirmed {
                    BasketService.deleteAndCreateNewBasket(nil)
                    self.startNewOrder(sender)
                }
            }
            // Do not start new order
            return
        }
        
        // Check if user is logged in and his preferred store supports order ahead
        if UserService.sharedUser?.favoriteStore?.restaurantId != nil {
            performSegue(withIdentifier: "SelectPickupLocationSegue", sender: self)
        } else {
            performSegue(withIdentifier: "SearchPickUpStoreSegue", sender: self)
        }
    }
    
    // MARK: TableView delegate
    
    override func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 18)
    }

}
