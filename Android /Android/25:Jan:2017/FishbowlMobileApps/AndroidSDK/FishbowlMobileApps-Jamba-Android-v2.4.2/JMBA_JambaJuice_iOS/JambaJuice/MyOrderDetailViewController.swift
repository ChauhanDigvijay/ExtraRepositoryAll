
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
    var seeMoreArray = [Int]()
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var orderAgainButton: UIButton!
    @IBOutlet weak var favouriteButton: UIButton!
    @IBOutlet weak var cancelOrderWidthConstant: NSLayoutConstraint!
    @IBOutlet weak var orderAgainWidthConstant: NSLayoutConstraint!
    @IBOutlet weak var cancelOrderButton:UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.lightBlue)
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 94
        tableView.rowHeight = UITableViewAutomaticDimension
        fetchStoreIfNeeded()
        //get the Delivery Details if it is a delivery
        fertchDeliveryDetails()
        // Cancel order hide and show
        cancelButtonHideShow()
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
            SVProgressHUD.show()
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
        if orderStatus.deliveryMode == deliveryMode.delivery.rawValue {
            SVProgressHUD.show()
            SVProgressHUD.setDefaultMaskType(.clear)
            UserService.getDeliveryTrackingDetails(orderStatus.id) { (deliveryTrackingDetail, error) in
                SVProgressHUD.dismiss()
                if deliveryTrackingDetail != nil {
                    self.orderStatus!.deliveryStatus = deliveryTrackingDetail
                    self.tableView.reloadData()
                }
            }
        }else{
            return
        }
    }
    
    
    
    //order Again functionality
    func orderAgain() {
        // See if we can find a restaurant for this store
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
                    }
                }
            }
            return
        }
        if self.orderStatus != nil {
            self.StartOrderAgain()
        }
    }
    
    func changeStoreConfirmation() {
        if orderStatus.store == nil{
            return
        }
        else if BasketService.sharedBasket != nil && BasketService.sharedBasket?.products.count > 0  { //Need to ask user
            // Precaution for crash
            presentConfirmation("Start Order", message: " Starting a new order will empty the basket and change the store to \(orderStatus.store!.name). Continue?", buttonTitle: "Start Order") { confirmed in
                if confirmed {
                    self.changeStoreAndStartOrderAgain()
                }
            }
            return
        }
        else{
            presentConfirmation("Start Order", message: "Start a new order at \(orderStatus.store!.name)?", buttonTitle: "Ok") { confirmed in
                if confirmed {
                    if self.orderStatus != nil {
                        self.changeStoreAndStartOrderAgain()
                    }
                }
            }
        }
    }
    
    func changeStoreAndStartOrderAgain() {
        SVProgressHUD.show(withStatus: "Preparing for new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        CurrentStoreService.sharedInstance.startNewOrder(orderStatus.store!, callback: { (status, error) in
            SVProgressHUD.dismiss()
            if (error != nil){
                self.presentError(error)
            }else {
                if(status == "Success"){
                    self.StartOrderAgain()
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "StoreDetailViewSegue"{
            let vc = segue.destination as! StoreViewController
            vc.myOrderViewController = true
            vc.store = orderStatus.store
        }
    }
    
    //MARK: Table View
    func numberOfSections(in tableView: UITableView) -> Int {
        if orderStatus.deliveryMode == deliveryMode.delivery.rawValue{
            return  orderStatus.store != nil ? 6 : 5
        }else{
            return orderStatus.store != nil ? 4 : 3
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return section == 0 ? orderStatus.products.count : 1
    }
    
    func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 20)
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        
        if orderStatus.deliveryMode == deliveryMode.delivery.rawValue{
            if section == 0{
                return "Items Ordered"
            }else if section == 1{
                return "Details"
            }else if section == 2{
                return "Delivery Status"
            }else if section == 3{
                return "Delivery Address"
            }else if section == 4{
                return "Order Status"
            }else {
                return "Ordered At"
            }
        }else{
            if section == 0{
                return "Items Ordered"
            }else if section == 1{
                return "Details"
            }else if section == 2{
                return "Order Status"
            }else {
                return "Ordered At"
            }
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return tableView.sectionHeaderHeight
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if  (indexPath.section == 3 && orderStatus != nil && orderStatus.deliveryMode != deliveryMode.delivery.rawValue) || (indexPath.section == 5 && orderStatus != nil && orderStatus.deliveryMode == deliveryMode.delivery.rawValue) {
            self.performSegue(withIdentifier: "StoreDetailViewSegue", sender: self)
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
    
    
    // Cancel order button hide and show
    
    func cancelButtonHideShow(){
        if orderStatus.isEditable!{
            orderAgainWidthConstant.constant = (self.view.frame.width)/2
            cancelOrderWidthConstant.constant = orderAgainWidthConstant.constant
        } else {
            orderAgainWidthConstant.constant = self.view.frame.width
            cancelOrderWidthConstant.constant = 0
        }
    }
    
    // Cancel order button clicked
    @IBAction func cancelOrderButtonClicked(_ sender: UIButton){
        trackButtonPress(sender)
        self.presentConfirmationWithYesOrNo("Cancel Order", message: "Are you sure you want to cancel this order?", buttonTitle: "Yes") { (confirmed) in
            if (confirmed) {
                self.cancelOrderFromBasket()
            }
        }
    }
    
    func cancelOrderFromBasket() {
        SVProgressHUD.show(withStatus: "Cancelling...")
        SVProgressHUD.setDefaultMaskType(.clear)
        OloOrderService.cancelOrder(orderStatus.id,callback: { (status, error) in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
            } else {
                self.presentOkAlert("Success!", message: "Order cancelled successfully.", callback: {
                    
                    let store = self.orderStatus.store
                    self.orderStatus = OrderStatus(oloOrderStatus: status[0])
                    self.orderStatus.store = store
                    
                    let index = UserService.sharedUser?.recentOrders?.index(where: { (item) -> Bool in
                        return item.id == status[0].id
                    })
                    print(index as Any)
                    
                    UserService.sharedUser!.recentOrders?[index!] = self.orderStatus
                    self.fertchDeliveryDetails()
                    self.tableView.reloadData()
                    self.cancelButtonHideShow()
                })
                
            }
            
        })
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        
        if orderStatus.deliveryMode == deliveryMode.delivery.rawValue{
            if indexPath.section == 0{
                return orderItemTableViewCell(indexPath: indexPath)
            }else if indexPath.section == 1{
                return orderDetailTableViewCell(indexPath: indexPath)
            }else if indexPath.section == 2{
                return deliveryStatusTableViewCell(indexPath: indexPath)
            }else if indexPath.section == 3{
                return deliveryAddresTableViewCell(indexPath: indexPath)
            }else if indexPath.section == 4{
                return orderStatusTableViewCell(indexPath: indexPath)
            }else{
                return orderAddressTableViewCell(indexPath: indexPath)
            }
        }else{
            if indexPath.section == 0{
                return orderItemTableViewCell(indexPath: indexPath)
            }else if indexPath.section == 1{
                return orderDetailTableViewCell(indexPath: indexPath)
            } else if indexPath.section == 2{
                return orderStatusTableViewCell(indexPath: indexPath)
            } else{
                return orderAddressTableViewCell(indexPath: indexPath)
            }
        }
    }
    
    
    func orderItemTableViewCell(indexPath:IndexPath) -> MyOrderItemTableViewCell{
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderItemTableViewCell", for: indexPath) as! MyOrderItemTableViewCell
        
        var orderStatusProduct:OrderStatusProduct?
        orderStatusProduct = orderStatus!.products[indexPath.row]
        
        if (seeMoreArray.count == indexPath.row) {
            cell.updateCell(orderStatusProduct: orderStatusProduct!, index: 0)
            let noOfLines = BasketItemTableViewCell.lines(BasketItemTableViewCell.descriptionLabelWidth,label: cell.optionLabel)
            if (noOfLines > BasketItemTableViewCell.defaultNoOfLines) {
                seeMoreArray.insert(SeeMore.kSeeMoreCollapse.rawValue, at: indexPath.row)
            } else {
                seeMoreArray.insert(SeeMore.kSeeMoreNone.rawValue, at: indexPath.row)
            }
        }
        
        cell.updateCell(orderStatusProduct: orderStatusProduct!, index: seeMoreArray[indexPath.row])
        cell.seeMore.tag = indexPath.row
        cell.layoutIfNeeded()
        return cell
    }
    
    func orderDetailTableViewCell(indexPath:IndexPath) -> MyOrderDetailTableViewCell{
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderDetailTableViewCell", for: indexPath) as! MyOrderDetailTableViewCell
        cell.updateCell(orderStatus: orderStatus)
        cell.layoutIfNeeded()
        return cell
    }
    
    func orderStatusTableViewCell(indexPath: IndexPath) -> CancelOrderStatusTableViewCell{
        let cell = tableView.dequeueReusableCell(withIdentifier: "CancelOrderStatusTableViewCell", for: indexPath) as! CancelOrderStatusTableViewCell
        cell.updateCell(orderStatus: orderStatus)
        cell.layoutIfNeeded()
        return cell
        
    }
    
    func orderAddressTableViewCell(indexPath:IndexPath) -> MyOrderDetailStoreTableViewCell{
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderDetailStoreTableViewCell", for: indexPath) as! MyOrderDetailStoreTableViewCell
        cell.updateOrderedAtStoreDetails(orderStatus: orderStatus, orderedAt: true)
        cell.layoutIfNeeded()
        return cell
    }
    
    func deliveryStatusTableViewCell(indexPath: IndexPath) -> DeliveryStatusTableViewCell{
        let cell = tableView.dequeueReusableCell(withIdentifier: "DeliveryStatusTableViewCell", for: indexPath) as! DeliveryStatusTableViewCell
        cell.updateTableViewCell(orderStatus: orderStatus)
        cell.layoutIfNeeded()
        return cell
    }
    
    func deliveryAddresTableViewCell(indexPath: IndexPath) -> MyOrderDetailStoreTableViewCell{
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyOrderDetailStoreTableViewCell", for: indexPath) as! MyOrderDetailStoreTableViewCell
        cell.updateOrderedAtStoreDetails(orderStatus: orderStatus, orderedAt: false)
        cell.layoutIfNeeded()
        return cell
    }
    
    @IBAction func seeMoreExpandCollapse(_ sender: UIButton){
        if (seeMoreArray[sender.tag] == SeeMore.kSeeMoreCollapse.rawValue) {
            seeMoreArray[sender.tag] = SeeMore.kSeeMoreExpand.rawValue
        } else if (seeMoreArray[sender.tag] == SeeMore.kSeeMoreExpand.rawValue) {
            seeMoreArray[sender.tag] = SeeMore.kSeeMoreCollapse.rawValue
        }
        tableView.reloadData()
    }
}
