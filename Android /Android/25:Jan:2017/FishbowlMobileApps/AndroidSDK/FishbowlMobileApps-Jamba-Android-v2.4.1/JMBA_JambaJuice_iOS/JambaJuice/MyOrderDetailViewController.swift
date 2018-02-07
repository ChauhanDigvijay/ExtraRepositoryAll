//
//  MyOrderDetailViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import OloSDK
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


class MyOrderDetailViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var orderStatus: OrderStatus!
    var favourite: FavouriteOrder!
//    var orderStatus: OrderStatus! {
//        didSet {
//            fetchStoreIfNeeded()
//            //get the Delivery Details if it is a delivery
//            if orderStatus.deliveryMode == deliveryMode.delivery.rawValue {
//                fertchDeliveryDetails()
//            }
//            tableView?.reloadData()
//        }
//    }
    
//    var favourite: FavouriteOrder! {
//        didSet {
//            fetchStoreIfNeededByRestaurantId()
//            tableView?.reloadData()
//        }
//    }
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var orderAgainButton: UIButton!
    @IBOutlet weak var favouriteButton: UIButton!
    @IBOutlet weak var favoriteTitle: UILabel!
    
    //expand/collapse state
    enum SeeMore :Int {
        case kSeeMoreNone = -1
        case kSeeMoreExpand = 0
        case kSeeMoreCollapse = 1
    }
    
    var seeMoreArray:[Int] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.lightBlue)
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 94
        tableView.rowHeight = UITableViewAutomaticDimension
        
        if favourite != nil {
            favouriteButton.setTitle("Remove Favorite", for: .normal)
            fetchStoreIfNeededByRestaurantId()
            getProducts()
        }
        if orderStatus != nil {
            fetchStoreIfNeeded()
            //get the Delivery Details if it is a delivery
            if orderStatus.deliveryMode == deliveryMode.delivery.rawValue {
                fertchDeliveryDetails()
            }
        }

//        tableView?.reloadData()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        trackScreenView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationItem.title = "Order Detail"
    }
    
    @IBAction func orderAgain(_ sender: UIButton) {
        trackButtonPress(sender)
        orderAgain()
    }
    
    //fetch store details, if it is not available (order History)
    func fetchStoreIfNeeded() {
        if orderStatus != nil && orderStatus.store == nil {
            SVProgressHUD.show(withStatus: "Fetching...")
            SVProgressHUD.setDefaultMaskType(.clear)
            StoreService.storeByStoreCode(orderStatus.vendorExtRef) { (stores, location, error) -> Void in
                SVProgressHUD.dismiss()
                if error != nil {
                    log.error("Error:\(error!.localizedDescription)")
                    return
                }
                if stores.count == 0 {
                    log.error("Error: 0 stores returned")
                    return
                }
                self.orderStatus.store = stores[0]
                self.tableView.reloadData()
            }
        }
    }
    
    //fetch order delivery details by order id
    func fertchDeliveryDetails() {
        SVProgressHUD.show(withStatus: "Fetching...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.getDeliveryTrackingDetails(orderStatus.id) { (deliveryTrackingDetail, error) in
            SVProgressHUD.dismiss()
            if deliveryTrackingDetail != nil {
                self.orderStatus!.deliveryStatus = deliveryTrackingDetail
//                self.orderStatus!.status = deliveryTrackingDetail!.status
                self.tableView.reloadData()
            }
        }
    }
    
    //fetch store details, if it is not available (favourite)
    func fetchStoreIfNeededByRestaurantId() {
        if favourite != nil && favourite.store == nil {
            SVProgressHUD.show(withStatus: "Fetching...")
            SVProgressHUD.setDefaultMaskType(.clear)
            StoreService.storeByRestaurantId(favourite.vendorId, callback: { (stores, location, error) in
                SVProgressHUD.dismiss()
                if error != nil {
                    log.error("Error:\(error!.localizedDescription)")
                    return
                }
                if stores.count == 0 {
                    log.error("Error: 0 stores returned")
                    return
                }
                self.favourite.store = stores[0]
                self.tableView.reloadData()
            })
        }
    }
    
    //order Again functionality
    func orderAgain() {
        // See if we can find a restaurant for this store
        if orderStatus != nil {
            if orderStatus.store == nil {
                SVProgressHUD.show(withStatus: "Preparing for new order...")
                SVProgressHUD.setDefaultMaskType(.clear)
                StoreService.storeByStoreCode(orderStatus.vendorExtRef) { (stores, location, error) -> Void in
                    SVProgressHUD.dismiss()
                    if error != nil {
                        self.presentError(error)
                        return
                    }
                    if stores.count == 0 {
                        self.presentError(NSError(description: "Unable to retrieve store information"))
                        return
                    }
                    let store = stores[0]
                    self.orderStatus.store = store
                    self.validateSameStore()
                }
            }
            else {
                validateSameStore()
            }
        } else if favourite != nil{
            //update favourite store
            if favourite.store == nil {
                SVProgressHUD.show(withStatus: "Preparing for new order...")
                SVProgressHUD.setDefaultMaskType(.clear)
                StoreService.storeByRestaurantId(favourite.vendorId , callback: { (stores, location, error) in
                    SVProgressHUD.dismiss()
                    if error != nil {
                        self.presentError(error)
                        return
                    }
                    if stores.count == 0 {
                        self.presentError(NSError(description: "Unable to retrieve store information"))
                        return
                    }
                    let store = stores[0]
                    self.favourite.store = store
                    self.validateSameStoreForFavouriteOrder()
                })
            } else {
                self.validateSameStoreForFavouriteOrder()
            }
        }
    }
    
    //MARK:- Reorder from recent order
    //check the current store & ordered store
    //if the stores are same then check the basket & create the Basket, else check the basket switch to store retrieve menu & create basket
    func validateSameStore() {
        if CurrentStoreService.sharedInstance.currentStore == nil {
            changeStoreAndStartOrderAgain()
        }
        else if orderStatus.store!.storeCode == CurrentStoreService.sharedInstance.currentStore!.storeCode{
            normalStartOrderAgain()
        }else{
            changeStoreConfirmation()
        }
    }
    
    
    func normalStartOrderAgain() {
        if BasketService.sharedBasket != nil && BasketService.sharedBasket?.products.count > 0  { //Need to ask user
            presentConfirmation("Empty Basket", message: "Re-ordering this order will delete the current basket. Continue?", buttonTitle: "Delete Basket") { confirmed in
                if confirmed {
                    if self.orderStatus != nil {
                        self.StartOrderAgain()
                    } else {
                        self.ReOrderFavouriteOrder()
                    }
                }
            }
            return
        }
        if self.orderStatus != nil {
            self.StartOrderAgain()
        } else {
            self.ReOrderFavouriteOrder()
        }
    }
    
    func changeStoreConfirmation() {
        if BasketService.sharedBasket != nil && BasketService.sharedBasket?.products.count > 0  { //Need to ask user
            
            // Precaution for crash
            var storeName = ""
            
            if orderStatus != nil && orderStatus.store != nil{
                storeName = orderStatus.store!.name
            }else if favourite != nil && favourite.store != nil{
                storeName = favourite.store!.name
            }
            
            presentConfirmation("Start Order", message: " Starting a new order will empty the basket and change the store to \(storeName). Continue?", buttonTitle: "Start Order") { confirmed in
                if confirmed {
                    if self.orderStatus != nil {
                        self.changeStoreAndStartOrderAgain()
                    } else {
                        self.changeStoreAndReOrderFavouriteOrder()
                    }
                }
            }
            return
        }
        var storeName = ""
        if self.orderStatus != nil {
            storeName = orderStatus.store!.name
        } else {
            storeName = favourite.store!.name
        }
        presentConfirmation("Start Order", message: "Start a new order at \(storeName)?", buttonTitle: "Ok") { confirmed in
            if confirmed {
                if self.orderStatus != nil {
                    self.changeStoreAndStartOrderAgain()
                } else {
                    self.changeStoreAndReOrderFavouriteOrder()
                }
            }
        }
    }
    
    func changeStoreAndStartOrderAgain() {
        SVProgressHUD.show(withStatus: "Preparing for new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        var store:Store?
        if self.orderStatus != nil {
            store = orderStatus.store!
        } else {
            store = favourite.store!
        }
        CurrentStoreService.sharedInstance.startNewOrder(store!, callback: { (status, error) in
            SVProgressHUD.dismiss()
            if (error != nil){
                self.presentError(error)
            }else {
                if(status == "Success"){
                    if self.orderStatus != nil {
                        self.StartOrderAgain()
                    } else {
                        self.ReOrderFavouriteOrder()
                    }
                }
                else{
                    self.presentError(NSError.init(description: Constants.genericErrorMessage))
                }
            }
        })
    }
    
    func StartOrderAgain()  {
        // Fishbowl event
        var orderStatusId = ""
        if let status = orderStatus{
            orderStatusId = status.id
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("\(orderStatusId)", item_name: "", event_name: "ORDER_AGAIN")
        orderAgainButton.isEnabled = false
        SVProgressHUD.show(withStatus: "Starting new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.orderAgain(orderStatus) { (basket, error) -> Void in
            self.orderAgainButton.isEnabled = true
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            //Dismiss
            self.dismissModalController()
            //And Open Basket Screen
            BasketFlagViewController.sharedInstance.openBasketVC()
        }
    }
    
    //MARK:- Reorder from favourite order
    func validateSameStoreForFavouriteOrder() {
        if CurrentStoreService.sharedInstance.currentStore == nil {
            changeStoreAndStartOrderAgain()
        }
        else if favourite.store!.storeCode == CurrentStoreService.sharedInstance.currentStore!.storeCode {
            normalStartOrderAgain()
        }else{
            changeStoreConfirmation()
        }
    }
    
    //order the favourite order by switching the current store
    func changeStoreAndReOrderFavouriteOrder() {
        SVProgressHUD.show(withStatus: "Preparing for new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        CurrentStoreService.sharedInstance.startNewOrder(self.favourite.store!, callback: { (status, error) in
            SVProgressHUD.dismiss()
            if (error != nil){
                self.presentError(error)
            }else {
                if(status == "Success"){
                    self.ReOrderFavouriteOrder()
                }
                else{
                    self.presentError(NSError.init(description: Constants.genericErrorMessage))
                }
            }
        })
    }
    
    func ReOrderFavouriteOrder()  {
        // Fishbowl event
        var orderStatusId = ""
        if let status = orderStatus{
            orderStatusId = status.id
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("\(orderStatusId)", item_name: "", event_name: "ORDER_AGAIN")
        orderAgainButton.isEnabled = false
        SVProgressHUD.show(withStatus: "Starting new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.reOrderFavouriteOrder(favourite) { (basket, error) in
            self.orderAgainButton.isEnabled = true
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            //Dismiss
            self.dismissModalController()
            //And Open Basket Screen
            BasketFlagViewController.sharedInstance.openBasketVC()
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "StoreDetailViewSegue"{
            let vc = segue.destination as! StoreViewController
            vc.myOrderViewController = true
            if favourite != nil {
                vc.store = favourite.store
            }else {
                vc.store = orderStatus.store
            }
        }
    }
    
    //MARK: Table View
    
    func numberOfSections(in tableView: UITableView) -> Int {
        if orderStatus != nil {
            var cellCount = 2   //Products & Details
            cellCount += orderStatus.deliveryStatus != nil && orderStatus.deliveryStatus!.id != "" ? 1 : 0 // Delivery Status
            cellCount += orderStatus.deliveryMode == deliveryMode.delivery.rawValue ? 1 : 0     //delivery address
            cellCount += orderStatus.store != nil ? 1 : 0   //Ordered At
            return cellCount
        }
        if favourite != nil {
            return favourite!.store == nil ? 1 : 2
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //order status
        if orderStatus != nil {
            if section == 0 {
                return orderStatus.products.count
            }
            return 1
        }
        //favourite order
        if favourite != nil {
            if section == 0 {
                return favourite.products.count + 1
            } else {
                return 1
            }
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        //Product section
        if indexPath.section == 0 {
            if indexPath.row == 0 && favourite != nil {
                let cell = tableView.dequeueReusableCell(withIdentifier: "FavouriteHeaderTableCell", for: indexPath) as! FavoriteHeaderTableViewCell
                cell.favoriteTitle.text = favourite.name
                return cell
            }
            
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderItemTableViewCell", for: indexPath) as! MyOrderItemTableViewCell
            
            var orderStatusProduct:OrderStatusProduct?
            var index = 0
            if orderStatus != nil {
                orderStatusProduct = orderStatus!.products[indexPath.row]
                index = indexPath.row
            } else if favourite != nil {
                orderStatusProduct = favourite!.products[indexPath.row - 1]
                index = indexPath.row - 1
            }
            
            cell.nameLabel.text = orderStatusProduct!.name.capitalized
            let spInstr = orderStatusProduct!.specialInstructions
            if spInstr.isEmpty {
                cell.descLabel.text = "No special instructions"
            }
            else {
                cell.descLabel.text = spInstr
            }
            let costString = String(format: "$%.2f",  orderStatusProduct!.totalCost)
            cell.rightDescLabel.text = costString
            
            let optionNamesAlreadyIncludedInDescription = NSMutableOrderedSet()
            for basketChoice in  orderStatusProduct!.choices {
                if !basketChoice.name.lowercased().hasPrefix("click here to") {//Filter out click here to...
                    //Check if we have already included a choice with same name
                    //If we haven't return the choice name and include it in the set for future.
                    if !optionNamesAlreadyIncludedInDescription.contains(basketChoice.name.uppercased()) {
                        optionNamesAlreadyIncludedInDescription.add(basketChoice.name.uppercased())
                    }
                }
            }
            cell.optionLabel.text = (optionNamesAlreadyIncludedInDescription.array as! [String]).joined(separator: ", ")
            
            cell.seeMore.tag=index;
            if (seeMoreArray.count == index) {
                let noOfLines = BasketItemTableViewCell.lines(120,label: cell.optionLabel)
                if (noOfLines > BasketItemTableViewCell.defaultNoOfLines) {
                    seeMoreArray.insert(SeeMore.kSeeMoreCollapse.rawValue, at: index)
                    cell.optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See More", for: UIControlState())
                    cell.seeMore.isHidden = false
                } else {
                    cell.optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    seeMoreArray.insert(SeeMore.kSeeMoreNone.rawValue, at: index)
                    cell.seeMore.isHidden = true
                }
            } else if (seeMoreArray.count > index){
                let lines = BasketItemTableViewCell.lines(120, label: cell.optionLabel)
                if (lines <= BasketItemTableViewCell.defaultNoOfLines) {
                    cell.optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.isHidden=true;
                } else if (seeMoreArray[index] == SeeMore.kSeeMoreCollapse.rawValue){
                    cell.optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See More", for: UIControlState())
                    cell.seeMore.isHidden = false
                } else if (seeMoreArray[index] == SeeMore.kSeeMoreExpand.rawValue){
                    cell.optionLabel.numberOfLines = lines
                    cell.optionLabelHeight.constant = CGFloat(lines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See Less", for: UIControlState())
                    cell.seeMore.isHidden = false
                }
            }
            
            //Needed for label to take proper dimensions
            cell.layoutIfNeeded()
            return cell
        }
            
            //Details section
        else if indexPath.section == 1 && orderStatus != nil {
            if indexPath.row == 0 {
                let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderDetailTableViewCell", for: indexPath) as! MyOrderDetailTableViewCell
                
                //pick up Details
                if orderStatus.deliveryMode != deliveryMode.delivery.rawValue {
                    if orderStatus.readyTime != nil {
                        let dateString = orderStatus.readyTime!.orderHistoryDateString()
                        let timeString = orderStatus.readyTime!.timeString()
                        //order not picked yet
                        if orderStatus.readyTime!.timeIntervalSinceNow > 0 {
                            cell.nameLabel.text = "Pick up on \(dateString) at \(timeString)"
                            cell.nameLabel.adjustsFontSizeToFitWidth = true
                        }
                        //order picked
                        else {
                            cell.nameLabel.text = "Picked up on \(dateString) at \(timeString)"
                            cell.nameLabel.adjustsFontSizeToFitWidth = true
                        }
                    }
                    else {
                        cell.nameLabel.text = "Pick up time not available"
                    }
                //Delivery Details
                } else {
                    if orderStatus.readyTime != nil {
                        var deliveryLabelText = ""
                        let dateString = orderStatus.readyTime!.orderHistoryDateString()
                        let timeString = orderStatus.readyTime!.timeString()
                        
                        if orderStatus.readyTime!.timeIntervalSinceNow > 0 {
                            deliveryLabelText = "Order will be delivered on \(dateString) at \(timeString)"
                        }
                        else {
                            deliveryLabelText = "Order was delivered on \(dateString) at \(timeString)"
                        }
                        
                        //delivery service details & status
                        if let address = orderStatus!.deliveryStatus{
                            if address.drivername != ""{
                                deliveryLabelText = "\(deliveryLabelText) by \(address.drivername) (\(address.driverphonenumber.formatPhoneNumber())) from \(address.deliveryservice) service"
                            }
                        }
                        
                        cell.nameLabel.text = deliveryLabelText
                        cell.nameLabel.adjustsFontSizeToFitWidth = true
                    }
                    else {
                        cell.nameLabel.text = "Order time not available"
                    }
                }
                
                //If order is failed one
                if orderStatus!.status == orderStatusValue.failed.rawValue {
                    cell.nameLabel.text = "Your order is not placed"
                }
                
                let costString = String(format: "Total $%.2f",  orderStatus.total)
                cell.descLabel.text = costString
                if orderStatus.status.isEmpty{
                    cell.orderStatusLabel.isHidden = true
                }else{
                    //                    cell.orderStatusLabel.textColor = UIColor(hex: Constants.jambaGarnetColor)
                    
                    cell.orderStatusLabel.text = orderStatus.status
                    switch orderStatus.status {
                    case orderStatusValue.inProgress.rawValue:
                        cell.orderStatusLabel.textColor = UIColor(hex: Constants.jambaGreenColor)
                        break
                    case orderStatusValue.scheduled.rawValue:
                        cell.orderStatusLabel.textColor = UIColor(hex: Constants.jambaMediumGrayColor)
                        break
                    case orderStatusValue.completed.rawValue:
                        cell.orderStatusLabel.textColor = UIColor(hex: Constants.jambaGarnetColor)
                        break
                    case orderStatusValue.cancelled.rawValue:
                        cell.orderStatusLabel.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                        break
                    default:
                        cell.orderStatusLabel.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                        break
                    }
                    cell.orderStatusLabel.text = orderStatus.status
                }
                return cell
            }
        }
        else if (indexPath.section == 2 && orderStatus != nil && orderStatus.deliveryMode == deliveryMode.delivery.rawValue){
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderDetailStoreTableViewCell", for: indexPath) as! MyOrderDetailStoreTableViewCell
            cell.isUserInteractionEnabled = false
            cell.selectionStyle = .none
            cell.rightArrow.isHidden = true
            cell.nameAndAddrLabel.font.withSize(15)
            if let status = orderStatus.deliveryStatus{
                cell.nameAndAddrLabel.text = status.status
                switch status.status {
                case orderStatusValue.inProgress.rawValue:
                    cell.nameAndAddrLabel.textColor = UIColor(hex: Constants.jambaGreenColor)
                    break
                case orderStatusValue.scheduled.rawValue:
                    cell.nameAndAddrLabel.textColor = UIColor(hex: Constants.jambaMediumGrayColor)
                    break
                case orderStatusValue.completed.rawValue:
                    cell.nameAndAddrLabel.textColor = UIColor(hex: Constants.jambaGarnetColor)
                    break
                case orderStatusValue.cancelled.rawValue:
                    cell.nameAndAddrLabel.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                    break
                case orderStatusValue.delivered.rawValue:
                    cell.nameAndAddrLabel.textColor = UIColor(hex: Constants.jambaGreenColor)
                    break
                default:
                    cell.nameAndAddrLabel.textColor = UIColor(hex: Constants.jambaCancelOrderRedColor)
                    break
                }
                return cell
            }else{
                cell.nameAndAddrLabel.text = "Delivery status not available"
                cell.nameAndAddrLabel.textColor = UIColor(hex: Constants.jambaMediumGrayColor)
                return cell
            }
        }
            //Store Detail Section
        else if (indexPath.section == 1 && favourite != nil) || (indexPath.section == 2 && orderStatus != nil && orderStatus.deliveryMode == deliveryMode.pickup.rawValue) || (indexPath.section == 4 && orderStatus != nil && orderStatus.deliveryMode == deliveryMode.delivery.rawValue && orderStatus.deliveryStatus != nil && orderStatus.deliveryStatus!.id != ""){
            var store:Store?
            if favourite != nil {
                store = favourite.store
            } else {
                store = orderStatus.store
            }
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderDetailStoreTableViewCell", for: indexPath) as! MyOrderDetailStoreTableViewCell
            cell.nameAndAddrLabel.text = "\(store!.name)\n\(store!.street)\n\(store!.city), \(store!.state), \(store!.zip)"
            return cell
            
        }
        
        //Delivery Address Section
        else if (indexPath.section == 3 && orderStatus != nil){
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderDetailStoreTableViewCell", for: indexPath) as! MyOrderDetailStoreTableViewCell
            cell.nameAndAddrLabel.text = orderStatus.getDeliveryAddress()
            cell.rightArrow.isHidden = true
            cell.isUserInteractionEnabled = false
            cell.selectionStyle = .none
            return cell
            
        }
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "FavouriteHeaderTableCell", for: indexPath)
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 20)
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if orderStatus != nil {
            if section == 1 {
                return "Details"
            } else if (orderStatus.deliveryMode == deliveryMode.pickup.rawValue && section == 2) || section == 4 {
                return "Ordered at"
            } else if orderStatus.deliveryMode == deliveryMode.delivery.rawValue {
                if section == 2 && orderStatus.deliveryStatus != nil && orderStatus.deliveryStatus!.id != ""{
                    return "Delivery Status"
                }else if (section == 3 && orderStatus.deliveryStatus != nil && orderStatus.deliveryStatus!.id != ""){
                    return "Delivery Address"
                }
            }
        } else if favourite != nil {
            if section == 1 {
                return "Ordered at"
            }
        }
        
        return nil
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0 {
            return 0
        }
        else {
            return tableView.sectionHeaderHeight
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if (indexPath.section == 1 && favourite != nil) || (indexPath.section == 2 && orderStatus != nil && orderStatus.deliveryMode != deliveryMode.delivery.rawValue) || (indexPath.section == 4 && orderStatus != nil && orderStatus.deliveryMode == deliveryMode.delivery.rawValue) {
            
            //show offerdetail screen to rewards storyboard
            //let mainView: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
            //let viewcontroller = mainView.instantiateViewControllerWithIdentifier("StoreDetailVC") as! StoreDetailViewController
            //if favourite != nil {
              //  viewcontroller.store = favourite.store!
            //} else {
                //viewcontroller.store = orderStatus.store!
            //}
            //viewcontroller.changePreferredStoreProfileScreen = true
            //self.navigationController?.pushViewController(viewcontroller, animated: true)
            self.performSegue(withIdentifier: "StoreDetailViewSegue", sender: self)
        }
    }

    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0{
            if favourite != nil && indexPath.row == 0 {
                return 40
            }
            return 143.0    //Product Detail Height
            
        } else if orderStatus != nil && orderStatus.deliveryMode == deliveryMode.delivery.rawValue && (indexPath.section == 1 || indexPath.section == 2 || indexPath.section == 3 || indexPath.section == 4){
            return UITableViewAutomaticDimension    //Delivery details Height (Tracking Status)
//        } else if orderStatus != nil && ((orderStatus.deliveryMode == deliveryMode.delivery.rawValue && indexPath.section == 2) || indexPath.section == 1) {
//            return UITableViewAutomaticDimension    //Delivery Address
        } else if indexPath.row == 0{
            return 92.0
        }else{
            return 94.0
        }
    }
    
    // reload table when show more/show less clicked
    @IBAction func expandCollapseDescription(_ sender: UIButton) {
        if (seeMoreArray[sender.tag] == SeeMore.kSeeMoreCollapse.rawValue) {
            seeMoreArray[sender.tag] = SeeMore.kSeeMoreExpand.rawValue
            
        } else if (seeMoreArray[sender.tag] == SeeMore.kSeeMoreExpand.rawValue) {
            seeMoreArray[sender.tag] = SeeMore.kSeeMoreCollapse.rawValue
           
        }
        self.tableView.reloadData()
    }
    
    @IBAction func addRemoveOrderAsFavouriteOrder() {
        if orderStatus != nil {
            showDescriptionAlert()
            
            //remove the order from favourite
        } else if favourite != nil {
            removeFavouriteOrder()
        }
    }
    
    //Show alert box to enter favourite order name
    func showDescriptionAlert() {
        let alert = UIAlertController(title: "Alert Message", message: "Enter Favorite Name", preferredStyle: .alert)
        
        alert.addTextField(configurationHandler: { (textField) -> Void in
            textField.placeholder = "Favorite Name"
        })
        
        alert.addAction(UIAlertAction(title: "Save", style: .default, handler: { [weak alert] (action) -> Void in
            let textField = alert!.textFields![0] as UITextField
            if textField.text == nil || textField.text == "" {
                self.presentConfirmation("Error", message: "Please enter favorite name to continue", buttonTitle: "OK", callback: { (confirmed) in
                    if confirmed {
                        self.showDescriptionAlert()
                    }
                })
            } else {
                self.getBasketId(textField.text!)
            }
            
            }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    //get basketId for add order as favourite
    //    func getBasketId(description:String) {
    //        var basketId = ""
    //        if BasketService.sharedBasket == nil {
    //            SVProgressHUD.showWithStatus("Adding to Favorite List...", maskType: .Clear)
    //            BasketService.orderAgain(orderStatus) { (basket, error) -> Void in
    //                if error != nil {
    //                    SVProgressHUD.dismiss()
    //                    self.presentConfirmation("Error", message: "Please try again", buttonTitle: "Retry", callback: { (confirmed) in
    //                        if confirmed {
    //                            self.getBasketId(description)
    //                        }
    //                        return
    //                    })
    //                    return
    //                } else {
    //                    basketId = basket!.id
    //                    self.saveFavouriteOrder(basketId, description: description)
    //                    return
    //                }
    //            }
    //        } else {
    //            basketId = BasketService.sharedBasket!.id
    //            self.saveFavouriteOrder(basketId, description: description)
    //        }
    //    }
    
    func getBasketId(_ description:String) {
        var basketId = ""
        SVProgressHUD.show(withStatus: "Adding...")
        SVProgressHUD.setDefaultMaskType(.clear)
        OloBasketService.createFromOrderStatusId(orderStatus.id) { (basket, error) in
            if error != nil {
                SVProgressHUD.dismiss()
                self.presentConfirmation("Error", message: "Please try again", buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed {
                        self.getBasketId(description)
                    }
                    return
                })
                return
            } else {
                basketId = basket!.id
                self.saveFavouriteOrder(basketId, description: description)
                return
            }
        }
    }
    
    
    //save Favourite order
    func saveFavouriteOrder(_ basketId:String, description:String) {
        SVProgressHUD.show(withStatus: "Adding...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.addFavouriteOrder(basketId, description: description) { (error) in
            SVProgressHUD.dismiss()
            if error != nil {
                if error!.code == 200{
                    self.presentError(error)
                }else{
                    self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed {
                            self.saveFavouriteOrder(basketId, description: description)
                        }
                    })
                }
            } else {
                self.presentOkAlert("Success", message: "Order added to favorites.", callback: {
                    self.popViewController()
                })
            }
        }
    }
    
    //remove from favourite order
    func removeFavouriteOrder() {
        SVProgressHUD.show(withStatus: "Removing...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.removeFavouriteOrder(favourite!.id, callback: { (error) in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed {
                        self.removeFavouriteOrder()
                    }
                })
            } else {
                self.presentOkAlert("Success", message: "Order successfully removed from favorites.", callback: {
                    self.popViewController()
                })
            }
        })
    }
    
    //get favorite products
    func getProducts(){
        SVProgressHUD.show(withStatus: "Fetching...")
        SVProgressHUD.setDefaultMaskType(.clear)
        OloBasketService.createFromFave(favourite.id, callback: { (basket, error) in
            if error != nil {
                SVProgressHUD.dismiss()
                self.presentConfirmation("Error", message: "Please try again", buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed {
                        self.getProducts()
                    }
                    return
                })
                return
            } else {
                self.favourite.updateProducts(basket!)
            }
            self.tableView.reloadData()
        })
    }
}

