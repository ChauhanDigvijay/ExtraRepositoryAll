//
//  MyOrdersTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
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


enum orderStatusValue:String {
    case scheduled  = "Scheduled"
    case inProgress = "In Progress"
    case completed  = "Completed"
    case cancelled  = "Canceled"
    case failed     = "Failed"
    case delivered  = "Delivered"
}

class MyOrdersTableViewController: UITableViewController {
    
    var selectedOrderStatus: OrderStatus?
    var selectedFavouriteOrder: FavouriteOrder?
    var isOrderHistorySelected:Bool = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.lightBlue)
        NotificationCenter.default.addObserver(self, selector: #selector(MyOrdersTableViewController.recentOrdersUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.LoggedInStateChanged.rawValue), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(MyOrdersTableViewController.recentOrdersUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.RecentOrdersUpdated.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(MyOrdersTableViewController.recentOrdersUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.FavouriteOrdersUpdated.rawValue), object: nil)
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 70
        tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        trackScreenView()
    }
    
    func recentOrdersUpdated(_ notification: Notification) {
        if UserService.sharedUser == nil {//User Logged Out. This is in prepration for auto-logout scenerios
            dismissModalController()
            return
        }
        
        //if favourite order is null, Then Switch the screen
        if UserService.sharedUser!.favouriteOrders?.count == 0 {
            isOrderHistorySelected = true
        }
        
        tableView.reloadData()
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrdersFavouriteTableViewCell") as! MyOrdersFavouriteTableViewCell
        if isOrderHistorySelected {
            cell.favouriteOrderButton.isSelected = false
            cell.orderHistoryButton.isSelected = true
        } else {
            cell.favouriteOrderButton.isSelected = true
            cell.orderHistoryButton.isSelected = false
        }
        return cell
    }
    // MARK: TableView Datasource/Delegate
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if UserService.sharedUser != nil{
            if isOrderHistorySelected{
                if UserService.sharedUser!.recentOrders != nil{
                    return max(1,UserService.sharedUser!.recentOrders!.count)
                }else {
                    return 1
                }
            }else{
                if UserService.sharedUser!.favouriteOrders != nil{
                    return max(1,UserService.sharedUser!.favouriteOrders!.count)
                }else{
                    return 1
                }
            }
        }
//        if (UserService.sharedUser!.recentOrders != nil  || UserService.sharedUser!.favouriteOrders != nil){
//                if isOrderHistorySelected{
//                    return max(1,UserService.sharedUser!.recentOrders!.count)
//                } else {
//                    return max(1,UserService.sharedUser!.favouriteOrders!.count)
//                }
//        }
//        }
        return 1
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        if indexPath.row == 0 && UserService.sharedUser!.favouriteOrders?.count > 0 {
//            let cell = tableView.dequeueReusableCellWithIdentifier("MyOrdersFavouriteTableViewCell", forIndexPath: indexPath) as! MyOrdersFavouriteTableViewCell
//            
//            if isOrderHistorySelected {
//                cell.favouriteOrderButton.selected = false
//                cell.orderHistoryButton.selected = true
//            } else {
//                cell.favouriteOrderButton.selected = true
//                cell.orderHistoryButton.selected = false
//            }
//            return cell
//        }
        if isOrderHistorySelected {
            if UserService.sharedUser!.recentOrders != nil && UserService.sharedUser!.recentOrders?.count > 0 {
                let index = indexPath.row
                let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrdersTableViewCell", for: indexPath) as! MyOrdersTableViewCell
                let orderStatus = UserService.sharedUser!.recentOrders![index]
                cell.nameLabel.text = orderStatus.truncateProductNames()
                if orderStatus.timePlaced != nil {
                    cell.descLabel.text = "Ordered " + orderStatus.timePlaced!.currentDateIfDateInFuture().timeAgoWithDayAsMinUnit()
                }
                else {
                    cell.descLabel.text = ""
                }
                //update status with color
                cell.orderStatus.text = orderStatus.status
                switch orderStatus.status {
                case orderStatusValue.inProgress.rawValue:
                    cell.orderStatus.textColor = UIColor(hex: Constants.jambaGreenColor)
                    break
                case orderStatusValue.scheduled.rawValue:
                    cell.orderStatus.textColor = UIColor(hex: Constants.jambaMediumGrayColor)
                    break
                case orderStatusValue.completed.rawValue:
                    cell.orderStatus.textColor = UIColor(hex: Constants.jambaGarnetColor)
                    break
                case orderStatusValue.cancelled.rawValue:
                    cell.orderStatus.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                    break
                default:
                    cell.orderStatus.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                    break
                }
                return cell
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "NoRecordsFound") as! MyOrderAndFavoriteEmptyTableViewCell
                cell.emptyMessage.text = "No Orders Found"
                return cell
            }
        }
        else {
            if UserService.sharedUser!.favouriteOrders != nil && UserService.sharedUser!.favouriteOrders?.count > 0{
                let index = indexPath.row
                let cell = tableView.dequeueReusableCell(withIdentifier: "MyFavouriteOrderTableViewCell", for: indexPath) as! MyFavouriteOrderTableViewCell
                let favourite = UserService.sharedUser!.favouriteOrders![index]
                cell.nameLabel.text = favourite.name
                cell.productLabel.text = favourite.vendorName
                return cell
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: "NoRecordsFound") as! MyOrderAndFavoriteEmptyTableViewCell
                cell.emptyMessage.text = "No Favorite Orders Found"
                return cell
            }
        }
    }
    
    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 74
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0{
            if !isOrderHistorySelected && (UserService.sharedUser!.favouriteOrders == nil || UserService.sharedUser!.favouriteOrders?.count == 0){
//                tableView.scrollEnabled = false
                return 150
            }else if isOrderHistorySelected && (UserService.sharedUser!.recentOrders == nil || UserService.sharedUser!.recentOrders?.count == 0) {
//                tableView.scrollEnabled = false
                return 150
            }
        }
        return 70
    }
    
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if isOrderHistorySelected {
            if UserService.sharedUser!.recentOrders != nil && UserService.sharedUser!.recentOrders!.count > 0{
                selectedOrderStatus = UserService.sharedUser!.recentOrders![indexPath.row]
            }else{
                return
            }
        } else {
            if UserService.sharedUser!.favouriteOrders != nil && UserService.sharedUser!.favouriteOrders!.count > 0{
                selectedFavouriteOrder = UserService.sharedUser!.favouriteOrders![indexPath.row]
            }else{
                return
            }
        }
        self.performSegue(withIdentifier: "OrderDetail", sender: nil)
    }
    
    //MARK: Navigation
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "OrderDetail" {
            let vc = segue.destination as! MyOrderDetailViewController
            if sender != nil{
                vc.orderStatus = UserService.sharedUser!.recentOrders!.first
            }else{
                if isOrderHistorySelected {
                    vc.orderStatus = selectedOrderStatus
                } else {
                    vc.favourite = selectedFavouriteOrder
                }
            }
        }
    }
    
    //MARK:- IBAction
    @IBAction func orderHistorySelected(_ sender:UIButton) {
        if sender.tag == 0{
            isOrderHistorySelected = true
            tableView.reloadData()
        } else {
            isOrderHistorySelected = false
            tableView.reloadData()
        }
    }
    
    func showOrderDetail(){
        performSegue(withIdentifier: "OrderDetail", sender: true)
    }
}
