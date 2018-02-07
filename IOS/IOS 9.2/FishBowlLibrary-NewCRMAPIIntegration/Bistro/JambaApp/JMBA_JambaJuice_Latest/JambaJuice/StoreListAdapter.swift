//
//  StoreListAdapter.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/5/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import CoreLocation

protocol StoreListAdapterDelegate: class {
    func storeListAdapterDidBeginSearching()
    func storeListAdapterDidEndSearching(error: NSError?)
    func storeListAdapterDidClearResults()
    func storeListAdapterSearchBarDidBeginEditing()
    func storeListAdapterUserDeniedLocationServices()
}


class StoreListAdapter: NSObject, UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate {

    private weak var delegate: StoreListAdapterDelegate?
    private weak var searchBar: UISearchBar?
    private weak var tableView: UITableView?
    var searchType: StoreSearchResultType = .OrderAhead // Olo requirement
    
    private(set) var stores: StoreList = []
    private(set) var location: CLLocation?
    private var searchComplete: Bool = false

    func configureSearchBar(searchBar: UISearchBar, tableView: UITableView, delegate: StoreListAdapterDelegate) {
        self.delegate = delegate
        self.searchBar = searchBar
        self.tableView = tableView
        tableView.tableFooterView = UIView()
        tableView.delegate = self
        tableView.dataSource = self
        searchBar.delegate = self
        updateLocationIcon(Constants.locationIconInactive)
    }
    
    func searchInitialLocation() {
        // Remember last location searched
        if let lastLocation = SettingsManager.setting(.LastSearch) as? String {
            searchBar?.text = lastLocation
            searchStoresNearLocationName(lastLocation)
        }
        else if LocationService.userHasDeniedLocation() {
            // User has declined location services, location services are off or not available.
            // No last location available! Open on default location.
            searchStoresNearLocationName(Constants.defaultSearchLocation)
        }
        else {
            // Search stores near user location (will prompt if not authorized yet)
            searchStoresNearUserLocation()
        }
    }
    
    // Override bookmark icon on search bar with location icon
    private func updateLocationIcon(imageName: String) {
        searchBar?.setImage(UIImage(named: imageName), forSearchBarIcon: UISearchBarIcon.Bookmark, state: UIControlState.Normal)
    }

    // Remove all stores from screen
    private func clearResults() {
        stores = []
        searchComplete = false
        delegate?.storeListAdapterDidClearResults()
    }
    
    // Find stores by location string string
    private func searchStoresNearLocationName(locationName: String) {
        clearResults()
        updateLocationIcon(Constants.locationIconInactive)
        delegate?.storeListAdapterDidBeginSearching()
        StoreService.findStoresByLocationName(locationName, searchType: searchType) { (stores, location, error) in
            UIApplication.inMainThread {
                self.stores = stores
                self.location = location
                self.searchComplete = true
                SettingsManager.setSetting(.LastSearch, value: locationName)
                self.delegate?.storeListAdapterDidEndSearching(error)
                self.tableView?.separatorStyle = stores.count == 0 ? .None : .SingleLine
                self.tableView?.reloadData()
            }
        }
    }
    
    // Find stores by user location
    private func searchStoresNearUserLocation() {
        clearResults()
        updateLocationIcon(Constants.locationIconActive)
        delegate?.storeListAdapterDidBeginSearching()
        StoreService.findStoresByUserLocation(searchType) { (stores, location, error) in
            UIApplication.inMainThread {
                self.stores = stores
                self.location = location
                self.searchComplete = true
                if error != nil || stores.count == 0 {
                    self.updateLocationIcon(Constants.locationIconInactive)
                }
                SettingsManager.deleteSetting(.LastSearch)
                self.delegate?.storeListAdapterDidEndSearching(error)
                self.tableView?.separatorStyle = stores.count == 0 ? .None : .SingleLine
                self.tableView?.reloadData()
            }
        }
    }

    func tableViewHeight() -> CGFloat {
        let rowHeight = tableView?.rowHeight ?? 0
        return CGFloat(max(stores.count, 1)) * rowHeight + 120
    }
    
    
    // MARK: - UITableViewDataSource

    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // Participant stores appears always at the end
        // No results appears only if no stores found
        return searchComplete ? max(stores.count + 1, 2) : 0
    }

    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        // No results cell is same heigh as store cells. Participant stores is always last.
        return indexPath.row == max(stores.count, 1) ? 120 : 90
    }

    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        // No results cell
        if searchComplete && stores.count == 0 && indexPath.row == 0 {
            return tableView.dequeueReusableCellWithIdentifier("StoreLocatorNoResultsTableViewCell")!
        }

        // Participant stores cell is always at the end
        if indexPath.row >= stores.count {
            return tableView.dequeueReusableCellWithIdentifier("ParticipantStoresTableViewCell")!
        }

        // Store cells
        let cell = tableView.dequeueReusableCellWithIdentifier("StoreLocatorTableViewCell") as! StoreLocatorTableViewCell
        let store = stores[indexPath.row]
        cell.storeNameLabel.text = store.name
        if location == nil {
            cell.storeAddressLabel.text = store.street
        } else {
            cell.storeAddressLabel.text = store.streetAndDistanceToLocation(location!)
        }
        cell.storeStatusLabel.text = store.city //  store.storeStatus()  Phase 1 - Store hours not available as a batch
        cell.orderAheadIconImageView.hidden = !store.supportsOrderAhead
        return cell
    }
    
    
    // MARK: UITableViewDelegate
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
    
    // MARK: - Search Bar delegate
    
    func searchBarTextDidBeginEditing(searchBar: UISearchBar) {
        searchBar.showsCancelButton = true
        delegate?.storeListAdapterSearchBarDidBeginEditing()
    }
    
    func searchBarTextDidEndEditing(searchBar: UISearchBar) {
        searchBar.showsCancelButton = false
    }
    
    func searchBarSearchButtonClicked(searchBar: UISearchBar) {
        if let text = searchBar.text {
            searchBar.resignFirstResponder()
            updateLocationIcon(Constants.locationIconInactive)
            searchStoresNearLocationName(text)
        }
    }
    
    func searchBarBookmarkButtonClicked(searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
        if LocationService.userHasDeniedLocation() {
            updateLocationIcon(Constants.locationIconInactive)
            delegate?.storeListAdapterUserDeniedLocationServices()
            return
        }
        
        searchBar.text = ""
        searchStoresNearUserLocation()
    }
    
    func searchBarCancelButtonClicked(searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
    }
    
}
