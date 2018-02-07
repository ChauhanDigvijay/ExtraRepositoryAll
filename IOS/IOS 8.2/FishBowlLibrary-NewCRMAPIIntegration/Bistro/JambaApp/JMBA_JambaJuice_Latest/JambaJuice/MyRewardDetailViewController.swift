//
//  MyRewardDetailViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class MyRewardDetailViewController: UITableViewController {
    
    @IBOutlet weak var startNewOrderButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.estimatedRowHeight = 44
        tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        tableView.reloadData() // For autolayout
    }
 
    @IBAction func startNewOrder(sender: UIButton) {
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
            performSegueWithIdentifier("SelectPickupLocationSegue", sender: self)
        } else {
            performSegueWithIdentifier("SearchPickUpStoreSegue", sender: self)
        }
    }
    
    // MARK: TableView delegate
    
    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont(name: Constants.archerMedium, size: 18)
    }

}
