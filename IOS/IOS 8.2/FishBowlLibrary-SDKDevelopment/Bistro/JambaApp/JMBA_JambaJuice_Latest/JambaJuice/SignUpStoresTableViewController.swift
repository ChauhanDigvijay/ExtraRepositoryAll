//
//  SignUpStoresTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 13/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class SignUpStoresTableViewController: UITableViewController, StoreListAdapterDelegate {
    
    @IBOutlet weak var searchBar: UISearchBar!
    
    private let storeListAdapter = StoreListAdapter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        storeListAdapter.searchType = .AllStores
        storeListAdapter.configureSearchBar(searchBar, tableView: tableView, delegate: self)
        
        // Want to run this only once but after animations on main thread are complete
        UIApplication.afterDelay(0.1) {
            self.storeListAdapter.searchInitialLocation()
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    
    // MARK: - Navigation
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let indexPath = tableView.indexPathForSelectedRow!
        let store = storeListAdapter.stores[indexPath.row]
        let nextVC = segue.destinationViewController as! SignUpStoreDetailViewController
        nextVC.store = store
    }
    
    @IBAction func continueAsGuest(sender: AnyObject) {
//        HomeViewController.sharedInstance().openOrderScreen()
        NonSignedInViewController.sharedInstance().openOrderScreenWithProducts()
    }
    
    // MARK: - StoreListAdapterDelegate
    
    func storeListAdapterDidClearResults() {
        tableView.reloadData() // TODO: Move this to adapter?
    }
    
    func storeListAdapterDidBeginSearching() {
        SVProgressHUD.showWithStatus("Finding stores...", maskType: .Clear)
    }
    
    func storeListAdapterDidEndSearching(error: NSError?) {
        SVProgressHUD.dismiss()
        if error != nil {
            presentError(error)
        }
    }
    
    func storeListAdapterSearchBarDidBeginEditing() {
        // Nothing to do
    }
    
    func storeListAdapterUserDeniedLocationServices() {
        presentOkAlert("Location Services Disabled", message: "To use your own location to find nearby Jamba Juice® stores, please enable Location Services on the Settings app.")
    }
    
}
