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
    func storeListAdapterDidEndSearching(_ error: NSError?)
    func storeListAdapterDidClearResults()
    func storeListAdapterSearchBarDidBeginEditing()
    func storeListAdapterUserDeniedLocationServices()
}


class StoreListAdapter: NSObject, UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate {

    fileprivate weak var delegate: StoreListAdapterDelegate?
    fileprivate weak var searchBar: UISearchBar?
    fileprivate weak var tableView: UITableView?
    var searchType: StoreSearchResultType = .orderAhead // Olo requirement
    
    fileprivate(set) var stores: StoreList = []
    fileprivate(set) var location: CLLocation?
    fileprivate var searchComplete: Bool = false

    func configureSearchBar(_ searchBar: UISearchBar, tableView: UITableView, delegate: StoreListAdapterDelegate) {
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
    fileprivate func updateLocationIcon(_ imageName: String) {
        searchBar?.setImage(UIImage(named: imageName), for: UISearchBarIcon.bookmark, state: UIControlState())
    }

    // Remove all stores from screen
    fileprivate func clearResults() {
        stores = []
        searchComplete = false
        delegate?.storeListAdapterDidClearResults()
    }
    
    // Find stores by location string string
    fileprivate func searchStoresNearLocationName(_ locationName: String) {
        clearResults()
        updateLocationIcon(Constants.locationIconInactive)
        delegate?.storeListAdapterDidBeginSearching()
        StoreService.findStoresByLocationName(locationName, searchType: searchType) { (stores, location, error) in
            UIApplication.inMainThread {
                self.stores = stores
                self.location = location
                self.searchComplete = true
                SettingsManager.setSetting(.LastSearch, value: locationName as AnyObject)
                self.delegate?.storeListAdapterDidEndSearching(error)
                self.tableView?.separatorStyle = stores.count == 0 ? .none : .singleLine
                self.tableView?.reloadData()
            }
        }
    }
    
    // Find stores by user location
    fileprivate func searchStoresNearUserLocation() {
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
                self.tableView?.separatorStyle = stores.count == 0 ? .none : .singleLine
                self.tableView?.reloadData()
            }
        }
    }

    func tableViewHeight() -> CGFloat {
        let rowHeight = tableView?.rowHeight ?? 0
        return CGFloat(max(stores.count, 1)) * rowHeight + 120
    }
    
    
    // MARK: - UITableViewDataSource

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // Participant stores appears always at the end
        // No results appears only if no stores found
        return searchComplete ? max(stores.count + 1, 2) : 0
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        // No results cell is same heigh as store cells. Participant stores is always last.
//        return indexPath.row == max(stores.count, 1) ? 120 : 120
        return 120
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // No results cell
        if searchComplete && stores.count == 0 && indexPath.row == 0 {
            return tableView.dequeueReusableCell(withIdentifier: "StoreLocatorNoResultsTableViewCell")!
        }

        // Participant stores cell is always at the end
        if indexPath.row >= stores.count {
            return tableView.dequeueReusableCell(withIdentifier: "ParticipantStoresTableViewCell")!
        }

        // Store cells
        let cell = tableView.dequeueReusableCell(withIdentifier: "StoreLocatorTableViewCell") as! StoreLocatorTableViewCell
        let store = stores[indexPath.row]
        cell.storeNameLabel.text = store.name
        cell.storeAddressLabel.text = store.street
        
        //calculate the distance between the store & user
        if location != nil {
            cell.distance.isHidden = false
            cell.distanceMilesText.isHidden = false
            if let distance = store.distanceToLocation(location!) {
                cell.distance.text = String(format: "%0.1f", distance)
            } else {
                cell.distance.isHidden = true
                cell.distanceMilesText.isHidden = true
            }
        } else {
            cell.distance.isHidden = true
            cell.distanceMilesText.isHidden = true
        }
        cell.storeStatusLabel.text = store.city //  store.storeStatus()  Phase 1 - Store hours not available as a batch
        cell.orderAheadIconImageView.isHidden = !store.supportsOrderAhead
        cell.deliveryOptionimageView.isHidden = !store.supportsDelivery
        return cell
    }
    
    
    // MARK: UITableViewDelegate
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    
    // MARK: - Search Bar delegate
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        searchBar.showsCancelButton = true
        delegate?.storeListAdapterSearchBarDidBeginEditing()
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchBar.showsCancelButton = false
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        if let text = searchBar.text {
            searchBar.resignFirstResponder()
            updateLocationIcon(Constants.locationIconInactive)
            searchStoresNearLocationName(text)
            // Fishbowl App Event
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("", item_name: "\(text.lowercased())", event_name: "STORE_SEARCH")
        }
    }
    
    func searchBarBookmarkButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
        if LocationService.userHasDeniedLocation() {
            updateLocationIcon(Constants.locationIconInactive)
            delegate?.storeListAdapterUserDeniedLocationServices()
            return
        }
        
        searchBar.text = ""
        searchStoresNearUserLocation()
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
    }
    
}
