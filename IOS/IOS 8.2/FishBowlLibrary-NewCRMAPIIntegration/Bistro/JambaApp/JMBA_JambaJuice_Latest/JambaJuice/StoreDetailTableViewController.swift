//
//  StoreDetailTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 19/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import MapKit
import HDK

class StoreDetailTableViewController: UITableViewController, UIAlertViewDelegate {
 
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var orderAheadImageView: UIImageView!
    @IBOutlet weak var orderAheadLabel: UILabel!

    //NOTE:If is not safe to assume this property to be set initially.
    var store: Store! {
        didSet {
            loadData()
        }
    }
    
    private var scheduleDictionary: StoreScheduleDictionary? //Assume missing is closed

    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 100
        tableView.rowHeight = UITableViewAutomaticDimension
        loadData()
    }
    
    func loadData() {
        //Crashlytics reports crash due to nil unwrapping of store.
        //So not assuming anything about presence of store.
        if view == nil || store == nil {
            return
        }
        if store.supportsOrderAhead {
            loadStoreScheduleIfNeeded()
            orderAheadImageView.image = UIImage(named: "order-ahead-icon")
            orderAheadLabel.text = "Order Ahead is available at this store"
            orderAheadLabel.textColor = UIColor(hex: Constants.jambaDarkGreenColor)
        } else {
            orderAheadImageView.image = UIImage(named: "order-ahead-icon-gray")
            orderAheadLabel.text = "Order Ahead is not available at this store"
            orderAheadLabel.textColor = UIColor(hex: Constants.jambaGrayColor)
        }
        
        // Update map
        if store.latitude != 0 && store.longitude != 0 {
            let annotation = MKPointAnnotation()
            annotation.coordinate = CLLocationCoordinate2DMake(store.latitude, store.longitude)
            mapView.addAnnotation(annotation)
            mapView.showsUserLocation = LocationService.userHasAuthorizeLocation()
            mapView.showAnnotations(mapView.annotations, animated: true)
        }
        tableView.reloadData()
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        let storeName = store != nil ? store.name : "Store Name N/A"
        AnalyticsService.trackEvent("view", action: "store_view", label: storeName)
        tableView.reloadData() // For autolayout
    }

    private func loadStoreScheduleIfNeeded() {
        if store.storeSchedule == nil || !store.storeSchedule!.hasTimingsForAWeek() {
            loadStoreSchedule()
        } else {
            scheduleDictionary = store.storeSchedule?.timingsStringsForAWeek()
        }
    }

    // Load restaurant schedule. RestaurantID must be set
    private func loadStoreSchedule() {
        StoreService.storeSchedule(store.restaurantId!) { (schedule, error) in
            assertMainThread()
            if error != nil {
                self.presentError(error)
                return
            }
            self.store.storeSchedule = schedule
            self.scheduleDictionary = schedule?.timingsStringsForAWeek()
            //Just Reload the schedule row
            self.tableView.reloadData()
        }
    }


    // MARK: UITableViewDataSource
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if store == nil {
            return 0
        }
        else if scheduleDictionary != nil {
            return 3
        }
        else {
            return 2
        }
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var cellReuseIdentifier = ""
        switch indexPath.row {
            case 0: cellReuseIdentifier = "StoreDetailAddressCell"
            case 1: cellReuseIdentifier = "StoreDetailPhoneCell"
            case 2: cellReuseIdentifier = "StoreDetailScheduleCell"
            default: assert(false, "unexpected row received")
        }
        
        let cell = tableView.dequeueReusableCellWithIdentifier(cellReuseIdentifier, forIndexPath: indexPath) as UITableViewCell
        
        if indexPath.row < 2 {
            let detailCell = cell as! StoreDetailTableViewCell
            if indexPath.row == 0 {
                detailCell.detailLabel.text = store.address
            } else {
                // Some SpendGo stores have "null" as phone number
                if invalidPhone() {
                    detailCell.detailLabel.text = "Not available"
                    detailCell.actionLabel.hidden = true
                    detailCell.selectionStyle = .None
                }
                else {
                    detailCell.detailLabel.text = store.phone.prettyFormattedPhoneNumber()
                    detailCell.actionLabel.hidden = false
                    detailCell.selectionStyle = .Default
                }
            }
        }
        else {
            let detailCell = cell as! StoreDetailScheduleTableViewCell
            if let schedule = scheduleDictionary {
                detailCell.update(schedule)
            }
        }
        return cell
    }
    
//    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
//        switch indexPath.row {
//        case 0: return 110
//        case 1: return 86
//        default: return 185
//        }
//    }

    // MARK: UITableViewDelegate
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if indexPath.row == 0 {
            let location = CLLocation(latitude: store.latitude, longitude: store.longitude)
            MapsManager.openMapsWithDirectionsToLocation(location, name: store.name)
            AnalyticsService.trackEvent("stores", action: "get_directions", label: store.name)
        }
        else if indexPath.row == 1 {
            if !invalidPhone() {
                CallManager.call(store.phone, presentingViewController: self)
                AnalyticsService.trackEvent("stores", action: "call_store", label: store.name)
            }
        }
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
    private func invalidPhone() -> Bool {
        return store.phone.trim().isEmpty || store.phone == "null"
    }
    
}
