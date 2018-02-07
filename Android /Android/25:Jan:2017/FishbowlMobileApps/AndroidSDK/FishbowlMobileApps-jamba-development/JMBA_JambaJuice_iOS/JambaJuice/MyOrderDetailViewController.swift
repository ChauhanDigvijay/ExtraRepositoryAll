//
//  MyOrderDetailViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class MyOrderDetailViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    var orderStatus: OrderStatus! {
        didSet {
            fetchStoreIfNeeded()
            tableView?.reloadData()
        }
    }
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var orderAgainButton: UIButton!
    
    //expand/collapse state
    enum SeeMore :Int {
        case kSeeMoreNone = -1
        case kSeeMoreExpand = 0
        case kSeeMoreCollapse = 1
    }
    
    var seeMoreArray:[Int] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 94
        tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func orderAgain(sender: UIButton) {
        trackButtonPress(sender)
        orderAgain()
    }

    func fetchStoreIfNeeded() {
        if orderStatus != nil && orderStatus.store == nil {
            StoreService.storeByStoreCode(orderStatus.vendorExtRef) { (stores, location, error) -> Void in
                if error != nil {
                    log.error("Error:\(error!.localizedDescription)")
                    return
                }
                if stores.count == 0 {
                    log.error("Error: 0 stores returned")
                    return
                }
                self.orderStatus.store = stores[0]
                self.tableView.reloadRowsAtIndexPaths([NSIndexPath(forRow: 1, inSection: 1)], withRowAnimation: .Automatic)
            }
        }
    }
    
    func orderAgain() {
        // See if we can find a restaurant for this store
        if orderStatus.store == nil {
            SVProgressHUD.showWithStatus("Preparing for new order...", maskType: .Clear)
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
                    self.StartOrderAgain()
                }
            }
            return
        }
        StartOrderAgain()
    }
    
    func changeStoreConfirmation() {
        if BasketService.sharedBasket != nil && BasketService.sharedBasket?.products.count > 0  { //Need to ask user
            presentConfirmation("Start Order", message: " Starting a new order will empty the basket and change the store to \(orderStatus.store!.name). Continue?", buttonTitle: "Start Order") { confirmed in
                if confirmed {
                    self.changeStoreAndStartOrderAgain()
                }
            }
            return
        }
        presentConfirmation("Start Order", message: "Start a new order to store \(orderStatus.store!.name)", buttonTitle: "Ok") { confirmed in
            if confirmed {
                self.changeStoreAndStartOrderAgain()
            }
        }
    }
    
    func changeStoreAndStartOrderAgain() {
        SVProgressHUD.showWithStatus("Preparing for new order...", maskType: .Clear)
        CurrentStoreService.sharedInstance.startNewOrder(self.orderStatus.store!, callback: { (status, error) in
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
        orderAgainButton.enabled = false
        SVProgressHUD.showWithStatus("Starting new order...", maskType: .Clear)
        BasketService.orderAgain(orderStatus) { (basket, error) -> Void in
            self.orderAgainButton.enabled = true
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
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "StoreDetail" {
            (segue.destinationViewController as! StoreViewController).store = orderStatus.store!
        }
    }
    
    //MARK: Table View
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        if orderStatus != nil {
            return 2
        }
        return 0
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if orderStatus != nil {
            if section == 0 {
                return orderStatus.products.count
            }
            //Section 1
            else if orderStatus.store != nil {
                return 2
            }
            else {
                return 1
            }
        }
        return 0
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            let cell = tableView.dequeueReusableCellWithIdentifier("MyOrderItemTableViewCell", forIndexPath: indexPath) as! MyOrderItemTableViewCell
            let orderStatusProduct = orderStatus!.products[indexPath.row]
            cell.nameLabel.text = orderStatusProduct.name.capitalizedString
            let spInstr = orderStatusProduct.specialInstructions
            if spInstr.isEmpty {
                cell.descLabel.text = "No special instructions"
            }
            else {
                cell.descLabel.text = spInstr
            }
            let costString = String(format: "$%.2f",  orderStatusProduct.totalCost)
            cell.rightDescLabel.text = costString
            
            let optionNamesAlreadyIncludedInDescription = NSMutableOrderedSet()
            for basketChoice in  orderStatusProduct.choices {
                if !basketChoice.name.lowercaseString.hasPrefix("click here to") {//Filter out click here to...
                    //Check if we have already included a choice with same name
                    //If we haven't return the choice name and include it in the set for future.
                    if !optionNamesAlreadyIncludedInDescription.containsObject(basketChoice.name.uppercaseString) {
                        optionNamesAlreadyIncludedInDescription.addObject(basketChoice.name.uppercaseString)
                    }
                }
            }
            cell.optionLabel.text = (optionNamesAlreadyIncludedInDescription.array as! [String]).joinWithSeparator(", ")
            
            cell.seeMore.tag=indexPath.row;
            if (seeMoreArray.count == indexPath.row) {
                let noOfLines = BasketItemTableViewCell.lines(120,label: cell.optionLabel)
                if (noOfLines > BasketItemTableViewCell.defaultNoOfLines) {
                    seeMoreArray.insert(SeeMore.kSeeMoreCollapse.rawValue, atIndex: indexPath.row)
                    cell.optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See More", forState: .Normal)
                     cell.seeMore.hidden = false
                } else {
                    cell.optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    seeMoreArray.insert(SeeMore.kSeeMoreNone.rawValue, atIndex: indexPath.row)
                    cell.seeMore.hidden = true
                }
            } else if (seeMoreArray.count > indexPath.row){
                let lines = BasketItemTableViewCell.lines(120, label: cell.optionLabel)
                if (lines <= BasketItemTableViewCell.defaultNoOfLines) {
                    cell.optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.hidden=true;
                } else if (seeMoreArray[indexPath.row] == SeeMore.kSeeMoreCollapse.rawValue){
                    cell.optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See More", forState: .Normal)
                    cell.seeMore.hidden = false
                } else if (seeMoreArray[indexPath.row] == SeeMore.kSeeMoreExpand.rawValue){
                    cell.optionLabel.numberOfLines = lines
                    cell.optionLabelHeight.constant = CGFloat(lines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See Less", forState: .Normal)
                     cell.seeMore.hidden = false
                }
            }
            
            //Needed for label to take proper dimensions
            cell.layoutIfNeeded()
            return cell
        }
        else {
            if indexPath.row == 0 {
                let cell = tableView.dequeueReusableCellWithIdentifier("MyOrderDetailTableViewCell", forIndexPath: indexPath) as! MyOrderDetailTableViewCell
                if orderStatus.readyTime != nil {
                    let dateString = orderStatus.readyTime!.dateString()
                    let timeString = orderStatus.readyTime!.timeString()
                    if orderStatus.readyTime!.timeIntervalSinceNow > 0 {
                        cell.nameLabel.text = "Pick up on \(dateString) at \(timeString)"
                    }
                    else {
                        cell.nameLabel.text = "Picked up on \(dateString) at \(timeString)"
                    }
                }
                else {
                    cell.nameLabel.text = "Pick up time not available"
                }
                let costString = String(format: "Total $%.2f",  orderStatus.total)
                cell.descLabel.text = costString
                if orderStatus.status.isEmpty{
                    cell.orderStatusLabel.hidden = true
                }else{
                    cell.orderStatusLabel.textColor = UIColor(hex: Constants.jambaGarnetColor)
                    cell.orderStatusLabel.text = orderStatus.status
                }
                return cell
            }
            else {
                let cell = tableView.dequeueReusableCellWithIdentifier("MyOrderDetailStoreTableViewCell", forIndexPath: indexPath) as! MyOrderDetailStoreTableViewCell
                cell.nameAndAddrLabel.text = "\(orderStatus.store!.name)\n\(orderStatus.store!.address)"
                return cell
            }
        }
    }
    
    func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 20)
    }
    
    func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if section == 1 {
            return "Details"
        }
        return nil
    }
    
    func tableView(tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0 {
            return 0
        }
        else {
            return tableView.sectionHeaderHeight
        }
    }
        
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
    // reload table when show more/show less clicked
    @IBAction func expandCollapseDescription(sender: UIButton) {
        if (seeMoreArray[sender.tag] == SeeMore.kSeeMoreCollapse.rawValue) {
            seeMoreArray[sender.tag] = SeeMore.kSeeMoreExpand.rawValue
        } else if (seeMoreArray[sender.tag] == SeeMore.kSeeMoreExpand.rawValue) {
            seeMoreArray[sender.tag] = SeeMore.kSeeMoreCollapse.rawValue
        }
        self.tableView.reloadData()
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if indexPath.section == 0{
            return 143.0
        }else if indexPath.row == 0{
            return 92.0
        }else{
            return 94.0
        }
    }
}

