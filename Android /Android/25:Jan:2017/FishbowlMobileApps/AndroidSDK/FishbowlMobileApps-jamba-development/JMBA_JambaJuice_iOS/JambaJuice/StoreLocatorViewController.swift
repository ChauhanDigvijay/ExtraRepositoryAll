//
//  StoreLocatorViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/30/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import MapKit
import SVProgressHUD
import HDK
import CoreLocation

enum AlertType: Int{
    case kNonSignedInScreen = 0
    case kFavouriteStoreEmpty = 1
    case kFavouriteStoreNotOrderAhead = 2
}

class StoreLocatorViewController: ScrollableModalViewController, StoreListAdapterDelegate {
    
    private var topMarginMinimum: CGFloat = 20
    
    @IBOutlet weak var topMarginConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var searchBar: UISearchBar!
    //  @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var jambaLogo: UIImageView!
    @IBOutlet weak var downArrow: UIImageView!
    
    // Stores searchtype orderAhead
    var storeSearchTypeOrderAhead:Bool = false
    
    // Basket transfer
    var basketTransfer:Bool = false
    
    // Alerttype
    var alertType: AlertType = .kNonSignedInScreen
    
    // Screen name
    var changePreferredStoreProfileScreen:Bool = false
    
    private let storeListAdapter = StoreListAdapter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        jambaLogo.hidden=false;
        downArrow.hidden=false;
        
        // Hide basket flag icon from the window
        BasketFlagViewController.sharedInstance.hideFlag()
        
        topMarginConstraint.constant = Constants.homeScreenNavigationMenuHeight
        storeListAdapter.searchType = .AllStores
        storeListAdapter.configureSearchBar(searchBar, tableView: tableView, delegate: self)
        //  updateScreen()
        
        // Store List only support order ahead
        if(storeSearchTypeOrderAhead){
            if basketTransfer == false{
                let action = UIAlertAction(title: "Ok", style: .Cancel) { action in
                    self.searchBar.becomeFirstResponder()
                }
                if alertType == .kFavouriteStoreNotOrderAhead{
                    presentAlert("Choose Store", message: "Your preferred store doesn't offer Order Ahead. Please select a different store for your order.", actions: action)
                }
                else{
                    presentAlert("Choose Store", message: "Please select search and select a store where your order will be fulfilled.", actions: action)
                }
            }
            
            storeListAdapter.searchType = .OrderAhead
        }
            
        else{
            // Want to run this only once but after animations on main thread are complete
            UIApplication.afterDelay(0.1) {
                self.storeListAdapter.searchInitialLocation()
            }
        }
        
        productName = searchBar.text
        isAppEvent = true
        clpAnalyticsService.sharedInstance.clpTrackScreenView("STORE_SEARCH");
        
    }
    
    override func viewWillAppear(animated: Bool) {
        // Account for changes on the status bar height
        topMarginMinimum = UIApplication.sharedApplication().statusBarFrame.size.height
        super.viewWillAppear(animated)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    override func heightForScrollViewContent() -> CGFloat {
        return storeListAdapter.tableViewHeight() + searchBar.frame.size.height + 44 // Order Ahead toolbar
    }
    
    
    // MARK: Navigation
    
    @IBAction func closeScreen() {
        trackButtonPressWithName("Stores")
        // HomeViewController.sharedInstance().closeModalScreen()
    }
    
    @IBAction func openOrderScreen() {
        trackButtonPressWithName("Menu")
        // HomeViewController.sharedInstance().openOrderScreen()
    }
    
    @IBAction func continueOnMaps(sender: UIButton) {
        trackButtonPress(sender)
        MapsManager.openMapsSearchingStoresNearLocation(searchBar.text)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let indexPath = tableView.indexPathForSelectedRow!
        let store = storeListAdapter.stores[indexPath.row]
        let nextVC = segue.destinationViewController as! StoreDetailViewController
        nextVC.store = store
        if(storeSearchTypeOrderAhead){
            nextVC.storeSearchTypeOrderAhead = true
            nextVC.basketTransfer = basketTransfer
        }
        // Change preferredStore Screen navigation
        if changePreferredStoreProfileScreen{
            nextVC.changePreferredStoreProfileScreen = true
        }
    }
    
    
    // MARK: - ScrollView delegate
    
    override func scrollViewDidScroll(scrollView: UIScrollView) {
        super.scrollViewDidScroll(scrollView)
        
        hideShowHeaderIcons()
        
        // Maximize screen when user scrolls to the bottom
        if scrollView.contentOffset.y > 0 && topMarginConstraint.constant > topMarginMinimum {
            topMarginConstraint.constant = max(topMarginMinimum, topMarginConstraint.constant - scrollView.contentOffset.y)
            scrollView.contentOffset.y = 0
        }
            // Snap back to open state
        else if scrollView.contentOffset.y < 0 && topMarginConstraint.constant < Constants.homeScreenNavigationMenuHeight {
            topMarginConstraint.constant = min(Constants.homeScreenNavigationMenuHeight, topMarginConstraint.constant - scrollView.contentOffset.y)
            scrollView.contentOffset.y = 0
        }
    }
    
    override func scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y < Constants.scrollViewThresholdToDismissModal {
            trackScreenEvent("close_screen", label: "drag_down")
            return
        }
        
        if decelerate == false {
            snapScrollView()
        }
    }
    
    func scrollViewDidEndDecelerating(scrollView: UIScrollView) {
        snapScrollView()
    }
    
    /// Return the closest end to the given point
    private func closestEnd(point: CGFloat, lowEnd: CGFloat, highEnd: CGFloat) -> CGFloat {
        return abs(point - lowEnd) < abs(point - highEnd) ? lowEnd : highEnd
    }
    
    /// Animate screen if needed. End position should be always close or open, but not in between
    private func snapScrollView() {
        if topMarginConstraint.constant == topMarginMinimum || topMarginConstraint.constant == Constants.homeScreenNavigationMenuHeight {
            return
        }
        
        log.verbose("Before snap animation")
        log.verbose("TopConstraint: \(self.topMarginConstraint.constant)")
        log.verbose("ScrollY: \(self.scrollView.contentOffset.y)")
        
        // Snap scroll view to closest edge (open or close)
        view.layoutIfNeeded()
        topMarginConstraint.constant = closestEnd(topMarginConstraint.constant, lowEnd: topMarginMinimum, highEnd: Constants.homeScreenNavigationMenuHeight)
        
        hideShowHeaderIcons()
        UIView.animateWithDuration(0.25) {
            
            self.view.layoutIfNeeded()
            log.verbose("After snap animation")
            log.verbose("TopConstraint: \(self.topMarginConstraint.constant)")
            log.verbose("ScrollY: \(self.scrollView.contentOffset.y)")
        }
    }
    
    
    // MARK: - StoreListAdapterDelegate
    
    func storeListAdapterDidClearResults() {
        // mapView.removeAnnotations(mapView.annotations)
        scrollViewContentHeightConstraint.constant = UIScreen.mainScreen().bounds.height
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
        updateScreen()
    }
    
    func storeListAdapterSearchBarDidBeginEditing() {
        view.layoutIfNeeded()
        self.jambaLogo.hidden=true;
        self.downArrow.hidden=true;
        topMarginConstraint.constant = topMarginMinimum
        UIView.animateWithDuration(0.25) {
            self.view.layoutIfNeeded()
        }
    }
    
    func storeListAdapterUserDeniedLocationServices() {
        presentOkAlert("Location Services Disabled", message: "To use your own location to find nearby Jamba Juice® stores, please enable Location Services on the Settings app.")
    }
    
    @IBAction func close() {
        //        navigationController?.popViewControllerAnimated(true)
        dismissViewControllerAnimated(true, completion: {
            // Show basket flag icon from the window
            BasketFlagViewController.sharedInstance.showFlag()
        })
    }
    
    //MARK: show/hide the header icon
    func hideShowHeaderIcons() {
        if (topMarginMinimum + 20 > topMarginConstraint.constant) {
            jambaLogo.hidden=true;
            downArrow.hidden=true;
        }
        else if (topMarginConstraint.constant > topMarginMinimum) {
            jambaLogo.hidden=false;
            downArrow.hidden=false;
        }
    }
    
}
