//
//  MyOrdersTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK

class MyOrdersTableViewController: UITableViewController {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          

    var selectedOrderStatus: OrderStatus?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.LightBlue)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(MyOrdersTableViewController.recentOrdersUpdated(_:)), name: JambaNotification.LoggedInStateChanged.rawValue, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(MyOrdersTableViewController.recentOrdersUpdated(_:)), name: JambaNotification.RecentOrdersUpdated.rawValue, object: nil)
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 70
        tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    func recentOrdersUpdated(notification: NSNotification) {
        if UserService.sharedUser == nil {//User Logged Out. This is in prepration for auto-logout scenerios
            dismissModalController()
            return
        }

        tableView.reloadData()
    }

    
    // MARK: TableView Datasource/Delegate
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if UserService.sharedUser != nil && UserService.sharedUser!.recentOrders != nil {
            return UserService.sharedUser!.recentOrders!.count
        }
        return 0
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("MyOrdersTableViewCell", forIndexPath: indexPath) as! MyOrdersTableViewCell
        let orderStatus = UserService.sharedUser!.recentOrders![indexPath.row]
        cell.nameLabel.text = orderStatus.commaSepratedNameOfProducts()
        if orderStatus.timePlaced != nil {
            cell.descLabel.text = "Ordered " + orderStatus.timePlaced!.currentDateIfDateInFuture().timeAgoWithDayAsMinUnit()
        }
        else {
            cell.descLabel.text = ""
        }
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        selectedOrderStatus = UserService.sharedUser!.recentOrders![indexPath.row]
        self.performSegueWithIdentifier("OrderDetail", sender: self)
    }
    
    //MARK: Navigation
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        (segue.destinationViewController as! MyOrderDetailViewController).orderStatus = selectedOrderStatus
        selectedOrderStatus = nil
    }

}
