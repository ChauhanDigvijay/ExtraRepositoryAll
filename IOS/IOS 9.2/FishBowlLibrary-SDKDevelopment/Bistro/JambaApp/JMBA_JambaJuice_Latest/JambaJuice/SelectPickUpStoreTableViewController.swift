//
//  SelectPickUpStoreTableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/5/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class SelectPickUpStoreTableViewController: UITableViewController, StoreListAdapterDelegate {

    @IBOutlet weak var searchBar: UISearchBar!
    
    private let storeListAdapter = StoreListAdapter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.Orange)
        storeListAdapter.configureSearchBar(searchBar, tableView: tableView, delegate: self)

        // Want to run this only once but after animations on main thread are complete
        UIApplication.afterDelay(0.1) {
            self.storeListAdapter.searchInitialLocation()
        }
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    // MARK: - Navigation

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let indexPath = tableView.indexPathForSelectedRow!
        let store = storeListAdapter.stores[indexPath.row]
        let nextVC = segue.destinationViewController as! SelectPickUpStoreViewController
        nextVC.store = store
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
