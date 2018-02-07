//
//  BasketViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/15/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import SVProgressHUD
import MGSwipeTableCell
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

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func <= <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l <= r
  default:
    return !(rhs < lhs)
  }
}



enum PickupDay {
    case asap
    case today
    case tomorrow
}

enum SeeMore : Int {
    case kSeeMoreNone = -1
    case kSeeMoreExpand = 0
    case kSeeMoreCollapse = 1
}

enum deliveryMode : String {
    case pickup = "pickup"
    case delivery = "dispatch"
}

private typealias BasketViewControllerAnimationCompleteCallback = () -> Void


class BasketViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, BasketOrderTimeTableViewCellDelegate, MGSwipeTableCellDelegate, BasketDeliveryModeTableViewCellDelegate, UIPickerViewDataSource, UIPickerViewDelegate {
    
    fileprivate static let interval: TimeInterval = (15 * 60)
    
    @IBOutlet weak var checkoutCostLabel: UILabel!
    @IBOutlet weak var checkoutButton: UIButton!
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var datePickerView: UIView!
    @IBOutlet weak var picker: UIPickerView!
    
    // Restaurant open / close times (nil is closed)
    fileprivate var restaurantStartEndToday: TimeRange?
    fileprivate var restaurantStartEndTomorrow: TimeRange?
    fileprivate var restaurantStartEndSelectedDay: TimeRange?
    
    // Times displayed on UI
    fileprivate var startDateTimeUsed: Date?
    fileprivate var endDateTimeUsed: Date?
    fileprivate var pickupTimeUsed: Date?  // will be nil for ASAP orders
    fileprivate var pickerSelectedDate:Date?
    fileprivate var storeOpenStatusText: String = ""
    var selectedDay:PickupDay = .asap
    var deliveryLaterValue:[String] = []            //Picker day value
    var deliveryLaterHourValue:[String] = []        //Picker hour value
    var deliveryLaterMinuteValue:[String] = []      //Picker minute value
    fileprivate static let intervalTime:Double = 15.0

    
    fileprivate var basket: Basket {
        get {
            return BasketService.sharedBasket!
        }
    }
    
    fileprivate var store: Store {
        get {
            return BasketService.sharedBasket!.store
        }
    }
    
    var seeMoreArray = [Int]()
    
    fileprivate weak var timer: Timer?
    
    deinit {
        NotificationCenter.default.removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    //MARK:- override Method
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.orange)
        tableView.estimatedRowHeight = 70
        tableView.rowHeight = UITableViewAutomaticDimension
        loadStoreScheduleIfNeeded()
        updateCheckoutCostLabel()
        datePickerView.isHidden = true
        
        NotificationCenter.default.addObserver(self, selector: #selector(BasketViewController.sharedBasketUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.SharedBasketUpdated.rawValue), object: nil)
        
        // Dismiss when guest request reorder
        NotificationCenter.default.addObserver(self, selector: #selector(BasketViewController.dismissFromStoreDetailViewController), name: NSNotification.Name(rawValue: JambaNotification.ReStartOrderForGuest.rawValue), object: nil)
        
        // Dismiss when user request reorder
        NotificationCenter.default.addObserver(self, selector: #selector(BasketViewController.dismissFromStoreDetailViewController), name: NSNotification.Name(rawValue: JambaNotification.ReStartOrderForUser.rawValue), object: nil)
        
        
        //if the delivery option is choosed, refresh the view
        NotificationCenter.default.addObserver(self, selector: #selector(BasketViewController.resetDeliveryMode), name: NSNotification.Name(rawValue: JambaNotification.OrderdeliveryModeChanged.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(BasketViewController.dismissWhenPushNotificationViewOffer), name: NSNotification.Name(rawValue: JambaNotification.PushNotificationViewOffer.rawValue), object: nil)
        
        // Observer for shortcutitme
        NotificationCenter.default.addObserver(self, selector: #selector(BasketViewController.dismissFromShortcutItem), name: NSNotification.Name(rawValue: JambaNotification.ShortcutItemCloseBasket.rawValue), object: nil)
        
        //prevent empty cell creation
        tableView.tableFooterView = UIView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        //get initially Order selection time value
        if basket.selectedPickupTime == nil {
            pickupTimeUsed = nil
            selectedDay = .asap
        } else {
            pickupTimeUsed = basket.selectedPickupTime! as Date
            if pickupTimeUsed!.isTodayInGregorianCalendar() {
                selectedDay = .today
            }
            else  {
                selectedDay = .tomorrow
                if basket.deliveryMode == deliveryMode.delivery.rawValue {
                    pickerSelectedDate = pickupTimeUsed
                    restaurantStartEndSelectedDay = store.storeSchedule!.startAndEndDateTimesForDate(pickerSelectedDate!)
                }
            }
        }
        
        trackScreenView()
        //We want the timer to be active if view is on-screen
        startTimer()
        // Refresh with promotions and rewards
        tableView.reloadData()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        //We want the timer to stop if view is off-screen
        stopTimer()
    }
    
    func updateCheckoutCostLabel() {
        let totalString = String(format: "$%.2f", basket.total)
        checkoutCostLabel.text = totalString
    }
    
    
    func getCustomiseItCostInBasket() -> Double {
        var cost:Double = 0.0
        for product in BasketService.sharedBasket!.products {
            for choice in product.choices {
                if BasketService.sharedBasket!.selectedCustomiseOptionIdsTrack.contains(choice.optionId) {
                    if let choiceQuantity = choice.quantity {
                        cost = cost + ((choice.cost * Double(choiceQuantity)) * Double(product.quantity))
                    } else {
                        cost = cost + (choice.cost * Double(product.quantity))
                    }
                }
            }
        }
        return cost
    }
    
    
    
    @IBAction func checkout(_ sender: UIButton) {
        // Validate Basket locally
        if validateBasketForCheckout() == false {
            return
        }
        
        // Fishbowl Event
        var fishbowlEvents:[FishbowlEvent] = []
        let basket = BasketService.sharedBasket!
        if basket.timeWanted == nil{
            let pickupTimeEvent = FishbowlEvent.init(item_id: basket.id, item_name: "ASAP", event_name: "PICK_UP_TIME")
            fishbowlEvents.append(pickupTimeEvent)
        }else{
            let pickUpDate = basket.timeWanted!.dateFromOloDateTimeString()
            let day = pickUpDate!.isTodayInGregorianCalendar() ? "TODAY" : "TOMORROW"
            let formatter = DateFormatter()
            formatter.dateFormat = "hh:mm a"
            let pickUpTime = formatter.string(from: pickUpDate! as Date)
            let pickupTimeEvent = FishbowlEvent.init(item_id: basket.id, item_name: "\(day):\(pickUpTime)", event_name: "PICK_UP_TIME")
            fishbowlEvents.append(pickupTimeEvent)
        }
        
        // Fishbowl Event
        let itemName = "PRODUCTS_COUNT = \(basket.products.count);TOTAL_COST = \(basket.total)"
        let checkoutEvent = FishbowlEvent.init(item_id: basket.id, item_name: itemName, event_name: "CHECKOUT")
        fishbowlEvents.append(checkoutEvent)
        
        FishbowlApiClassService.sharedInstance.submitMobileAppEvents(fishbowlEvents)
        
        // Confirmation alert with change store, cancel and proceed choices
        let alertController = UIAlertController(title: "Checkout Confirmation", message: "Please confirm your order will be placed at the \(basket.store.name) store.", preferredStyle: .actionSheet)
        
        let proceedAction = UIAlertAction(title: "Proceed", style: .default, handler: {
            action in
            self.proceedOrder()
        })
        alertController.addAction(proceedAction)
        
        let changeStoreAction = UIAlertAction(title: "Change Store", style: .default, handler: {
            action in
            self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
        })
        alertController.addAction(changeStoreAction)
        
        var cancelAction = UIAlertAction()
        
        // For iPad Support validation
        if UIDevice.current.userInterfaceIdiom == .pad{
            alertController.popoverPresentationController?.sourceView = sender
            alertController.popoverPresentationController?.sourceRect = sender.bounds
            cancelAction = UIAlertAction(title: "Cancel", style: .default, handler: {
                action in
                return
            })
        }
            
        else{
            cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: {
                action in
                return
            })
        }
        
        alertController.addAction(cancelAction)
        
        self.present(alertController, animated: true, completion: nil)
    }
    
    fileprivate func validateBasketForCheckout() -> Bool {
        let basket = BasketService.sharedBasket!
        if basket.products.count <= 0 {
            presentOkAlert("Empty Basket", message: "Please add at least one product to the basket in order to check out.")
            return false
        }
        
        //check delivery address is selected by user for Delivery order
        if basket.deliveryMode == deliveryMode.delivery.rawValue {
            if basket.deliveryAddress == nil || basket.deliveryAddress!.zipcode == "" {
                presentOkAlert("Error", message: "Choose Delivery Address")
                return false
            }
        }
        
        //Set time wanted in basket.
        setupTimeWantedFromBasketPickupTime()
        if !validatePickUpTime() {
            return false
        }
        
        return true
    }
    
    // MARK: Notifications
    
    func sharedBasketUpdated(_ notification: Notification) {
        //Check from baster service since we force unwrap.
        if BasketService.sharedBasket != nil {
            updateCheckoutCostLabel()
            tableView.reloadData()
        }
    }
    
    // MARK: Navigation
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "StoreDetail" {
            (segue.destination as! StoreViewController).store = basket.store
        }
        else if segue.identifier == "EnterCreditCard"
        {
            if let offerId = BasketService.sharedBasket?.offerId
            {
                (segue.destination as! EnterCreditCardViewController).appliedOfferId =  offerId }
            
        }
            
        else if segue.identifier == "SelectPaymentMethod"
        {
            if let offerId = BasketService.sharedBasket?.offerId
            {
                (segue.destination as! PaymentMethodViewController).appliedOfferId =  offerId }
        }
            
        else if segue.identifier == "StoreLocatorSegue"{
            let nc = segue.destination as! UINavigationController
            let vc = nc.viewControllers[0] as! StoreLocatorViewController
            vc.storeSearchTypeOrderAhead = true
            vc.basketTransfer = true
        }
    }
    
    //MARK:- IBAction
    @IBAction func dismiss(_ sender: UIButton) {
        trackButtonPressWithName("Back")
        dismiss()
    }
    
    @IBAction func deleteBasket(_ sender: UIButton) {
        trackButtonPressWithName("DeleteBasket")
        presentConfirmation("Empty Basket", message: "Emptying your basket will cancel the current order. Continue?", buttonTitle: "Empty") { confirmed in
            if confirmed {
                var itemId = ""
                itemId = "\(self.basket.id)"
                FishbowlApiClassService.sharedInstance.submitMobileAppEvent(itemId, item_name:"", event_name: "BASKET_DELETE")
                SVProgressHUD.show(withStatus: "Deleting Basket...")
                SVProgressHUD.setDefaultMaskType(.clear)
                BasketService.deleteAndCreateNewBasket({ (status) in
                    if(status){
                        SVProgressHUD.dismiss()
                        self.dismiss()
                    }
                    else{
                        SVProgressHUD.dismiss()
                        self.presentOkAlert("Empty Basket", message: "Unable to empty basket. Please try again")
                    }
                })
            }
        }
    }
    
    //Close the date picker
    @IBAction func closePickerView() {
        UIView.transition(with: datePickerView, duration: 0.5, options: .transitionCrossDissolve, animations: { 
            self.datePickerView.isHidden = true
            }, completion: nil)
//        datePickerView.hidden = true
        
        pickupTimeUsed = pickerSelectedDate
        BasketService.sharedBasket!.selectedPickupTime = pickerSelectedDate
        //get the selected date from the picker, based on that get the start & end time of store open
        //        let selectedDays = picker.selectedRowInComponent(0) + 1
        //        pickerSelectedDate = getNthtDay(NSDate(), dayValue: selectedDays)
        //        restaurantStartEndSelectedDay = store.storeSchedule!.startAndEndDateTimesForDate(pickerSelectedDate!)
        
        //recalculate the silder values & update the ui
        if let cell = tableView.cellForRow(at: IndexPath(row: 1, section: 3)) as? BasketOrderTimeTableViewCell {
            //            recalculateBasketPickupTimeFromOrderTimeUI(cell, dayChanged: true)
            populateOrderTimeCellLabelsAndButons(cell, day: .tomorrow)
        }
    }
    
    //show delivery info
    @IBAction func showDeliveryInfo() {
        self.presentOkAlert("Info", message: "We use multiple providers to find you the most efficient delivery option. Delivery charges reflect current availability and may change.")
    }
    
    // MARK: Private
    
    fileprivate func dismiss() {
        (navigationController as? BasketNavigationController)?.dismiss()
    }
    
    fileprivate func startTimer() {
        stopTimer()
        let now = Date()
        //Add interval
        let fifteenMinFromNow = now.timeIntervalSince1970 + BasketViewController.interval
        //
        let roundedOff = (fifteenMinFromNow - (fifteenMinFromNow.truncatingRemainder(dividingBy: BasketViewController.interval))) + 1 //1 second after the 15min mark
        let timer = Timer(fireAt: Date(timeIntervalSince1970: roundedOff), interval: BasketViewController.interval, target: self, selector: #selector(BasketViewController.intervalUpdate), userInfo: nil, repeats: true)
        RunLoop.current.add(timer, forMode: RunLoopMode.defaultRunLoopMode)
        self.timer = timer
    }
    
    fileprivate func stopTimer() {
        timer?.invalidate()
        timer = nil
    }
    
    // MARK: - NSTimer Callback
    
    func intervalUpdate() {
        tableView.reloadSections(IndexSet(integer: 2), with: .automatic)
    }
    
    fileprivate func loadStoreScheduleIfNeeded() {
        if store.storeSchedule == nil || !store.storeSchedule!.hasTimingsForTodayAndTomorrow() {
            loadStoreSchedule()
        } else {
            populateStartAndEndDateTimes()
            tableView.reloadSections(IndexSet(integer: 2), with: .automatic)
        }
    }
    
    fileprivate func loadStoreSchedule() {
        if store.restaurantId == nil{
            return
        }
        StoreService.storeSchedule(store.restaurantId!) { (schedule, error) in
            assertMainThread()
            if error != nil {
                self.presentError(error)
                return
            }
            self.store.storeSchedule = schedule
            self.populateStartAndEndDateTimes()
            self.tableView.reloadSections(IndexSet(integer: 2), with: .automatic)
        }
    }
    
    fileprivate func populateStartAndEndDateTimes() {
        restaurantStartEndToday = store.storeSchedule!.startAndEndDateTimesForToday()
        restaurantStartEndTomorrow = store.storeSchedule!.startAndEndDateTimesForTomorrow()
    }
    
    private func reloadStoreScheduele(){
        if store.storeSchedule != nil{
            populateStartAndEndDateTimes()
        }
    }
    
    // MARK:- Table View DataSource
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return CurrentStoreService.sharedInstance.currentStore!.supportsDelivery ? 6 : 5

    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //Items
        if section == 0 {
            //if total quantity matches the max quantity then hide add another product cell
            if BasketService.itemsInBasket() == Constants.oloItemLimit {
                return basket.products.count
            } else {
                return max(basket.products.count + 1, 2)    //min 2 i.e) Empty basket one cell & add Another Item one cell
            }
            
        } else if section == 3 && BasketService.sharedBasket?.deliveryMode == deliveryMode.delivery.rawValue { // create two row for delivery address and timeline
            //after choosing the address only, allow the user to switch the time
            if BasketService.getDeliveryAddress().1 {
                return 2
            } else {
                return 1
            }
            
        }
            //Store,Time,Rewards,Total
        else {
            return 1
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //Store
        if indexPath.section == 1 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "BasketStoreTableViewCell", for: indexPath) as! BasketStoreTableViewCell
            cell.nameAndAddrLabel.text = "\(basket.store.name)\n\(basket.store.address)"
            return cell
        }
        
        //Items
        if indexPath.section == 0 {
            // if the basket is empty, then show empty basket cell & add another product cell
            if basket.products.count == 0 && indexPath.row == 0 {
                return tableView.dequeueReusableCell(withIdentifier: "BasketEmptyTableViewCell")!
            } else if basket.products.count == 0 && indexPath.row == 1 {
                return tableView.dequeueReusableCell(withIdentifier: "BasketAddAnotherItemCell") as! BasketAddAnotherItemCell
            }
            
            //check for add another item cell
            if basket.products.count - 1 < indexPath.row {
                return tableView.dequeueReusableCell(withIdentifier: "BasketAddAnotherItemCell") as! BasketAddAnotherItemCell
            }
            
            let cell = tableView.dequeueReusableCell(withIdentifier: "BasketBasicTableViewCell", for: indexPath) as! BasketItemTableViewCell
            
            if (seeMoreArray.count == indexPath.row) {
                cell.update(basket.products[indexPath.row],state: 0)
                let noOfLines = BasketItemTableViewCell.lines(BasketItemTableViewCell.descriptionLabelWidth,label: cell.descriptionLabel)
                if (noOfLines > BasketItemTableViewCell.defaultNoOfLines) {
                    seeMoreArray.insert(SeeMore.kSeeMoreCollapse.rawValue, at: indexPath.row)
                } else {
                    seeMoreArray.insert(SeeMore.kSeeMoreNone.rawValue, at: indexPath.row)
                }
            }
            
            cell.update(basket.products[indexPath.row],state: seeMoreArray[indexPath.row])
            cell.seeMoreButton.tag = indexPath.row
            cell.delegate = self
            //            cell.frame.size.height = 150;
            return cell
        }
        
        //if the store support delivery then show the delivery Option
        if CurrentStoreService.sharedInstance.currentStore!.supportsDelivery {
            if indexPath.section == 2 {
                //if product deliver avaliable show support order type else show pick up time
                if CurrentStoreService.sharedInstance.currentStore!.supportsDelivery {
                    let cell = tableView.dequeueReusableCell(withIdentifier: "BasketDeliveryMode", for: indexPath) as! BasketDeliveryModeTableViewCell
                    cell.deliveryModeDelegate = self
                    cell.retainSelectedValue()
                    return cell
                } else {
                    let cell = tableView.dequeueReusableCell(withIdentifier: "BasketOrderTimeTableViewCell", for: indexPath) as! BasketOrderTimeTableViewCell
                    cell.delegate = self
                    cell.updateButtonlabel()
                    cell.selectDay(selectedDay)
                    populateOrderTimeUIFromBasketPickupTime(cell)
                    return cell
                }
            }
            
            if indexPath.section == 3 {
                
                if BasketService.sharedBasket?.deliveryMode == deliveryMode.delivery.rawValue{
                    //Delivery Address
                    if indexPath.row == 0{
                        let cell = tableView.dequeueReusableCell(withIdentifier: "BasketDeliveryAddress", for: indexPath) as! BasketDeliveryAddressTableViewCell
                        cell.deliveryAddress.text = BasketService.getDeliveryAddress().0
                        return cell
                        //Delivery choosing time
                    } else if indexPath.row == 1{
                        let cell = tableView.dequeueReusableCell(withIdentifier: "BasketOrderTimeTableViewCell", for: indexPath) as! BasketOrderTimeTableViewCell
                        cell.delegate = self
                        cell.updateButtonlabel()
                        cell.selectDay(selectedDay)
                        populateOrderTimeUIFromBasketPickupTime(cell)
                        return cell
                    }
                }else{
                    // Time
                    let cell = tableView.dequeueReusableCell(withIdentifier: "BasketOrderTimeTableViewCell", for: indexPath) as! BasketOrderTimeTableViewCell
                    cell.delegate = self
                    cell.updateButtonlabel()
                    cell.selectDay(selectedDay)
                    populateOrderTimeUIFromBasketPickupTime(cell)
                    return cell
                }
            }
            
            // Rewards Or Promotion
            if indexPath.section == 4 {
                let cell = tableView.dequeueReusableCell(withIdentifier: "SelectRewardTableViewCell", for: indexPath) as! BasketNavigationTableViewCell
                cell.updateForBasket(basket)
                cell.delegate = self
                return cell
            }
            //otherwise Hide It
        } else {
            if indexPath.section == 2 {
                let cell = tableView.dequeueReusableCell(withIdentifier: "BasketOrderTimeTableViewCell", for: indexPath) as! BasketOrderTimeTableViewCell
                cell.delegate = self
                cell.updateButtonlabel()
                cell.selectDay(selectedDay)
                populateOrderTimeUIFromBasketPickupTime(cell)
                return cell
            }
            else if indexPath.section == 3 {
                let cell = tableView.dequeueReusableCell(withIdentifier: "SelectRewardTableViewCell", for: indexPath) as! BasketNavigationTableViewCell
                cell.updateForBasket(basket)
                cell.delegate = self
                return cell
            }
        }
        
        // Total
        let cell = tableView.dequeueReusableCell(withIdentifier: "BasketTotalTableViewCell", for: indexPath) as! BasketTotalTableViewCell
        cell.subtotalCostLabel.text = String(format:  "$%.2f", basket.subTotal)
        cell.discountLabel.text     = String(format: "-$%.2f", basket.discount)
        cell.deliveryLabel.text     = String(format: "$%.2f", basket.customerhandoffcharge)
        cell.taxCostLabel.text      = String(format:  "$%.2f", basket.salesTax)
        cell.totalCostLabel.text    = String(format:  "$%.2f", basket.total)
        cell.updateDeliveryLabel()
        return cell
    }
    
    // MARK:- TableView Delegate
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        //check the store supports  delivery then show the delivery option
        if CurrentStoreService.sharedInstance.currentStore!.supportsDelivery {
            switch section {
            case 0: return "Items"
            case 1: return "Store"
            case 2: return "Order Type"
            case 3:
                if BasketService.sharedBasket?.deliveryMode == deliveryMode.delivery.rawValue{
                    return "Delivery Address"
                }else if(BasketService.sharedBasket?.deliveryMode == deliveryMode.pickup.rawValue){
                    return "Pickup Date & Time"
                }
            case 4: return "Rewards & Promotions"
            case 5: return "Total"
            default: assert(false, "Unexpected section number.")
            }
            return nil
        } else {
            switch section {
            case 0: return "Items"
            case 1: return "Store"
            case 2: return "Pickup Date & Time"
            case 3: return "Rewards & Promotions"
            case 4: return "Total"
            case 5: return ""
            default: assert(false, "Unexpected section number.")
            }
            return nil
        }
    }
    
    func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 18)
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 0 {
            if let cell = tableView.cellForRow(at: indexPath) as? BasketItemTableViewCell {
                cell.showSwipe(.rightToLeft, animated: true)
            } else if (tableView.cellForRow(at: indexPath) as? BasketAddAnotherItemCell) != nil {
                dismiss()
            }
        }
        
        if indexPath.section == 1 {
            self.performSegue(withIdentifier: "StoreDetail", sender: nil)
        }
        
        //if the store supports the delivery option, the arrangements of cell
        if CurrentStoreService.sharedInstance.currentStore!.supportsDelivery && indexPath.section == 3 {
            
            if BasketService.sharedBasket?.deliveryMode == deliveryMode.delivery.rawValue && indexPath.row == 0 {
                if UserService.sharedUser == nil{
                    performSegue(withIdentifier: "GuestUserDeliveryAddressSegue", sender: self)
                }else{
                    performSegue(withIdentifier: "DeliveryAddressSegue", sender: self)
                }
            }
        } else if (!CurrentStoreService.sharedInstance.currentStore!.supportsDelivery && indexPath.section == 3) || (indexPath.section == 4) {
            if UserService.sharedUser == nil {
                if basket.products.count <= 0 {
                    presentOkAlert("Empty Basket", message: "Please add at least one product to the basket in order to use promo code.")
                    return
                }else{
                    performSegue(withIdentifier: "EnterPromotionSegue", sender: self)
                }
            } else {
                if basket.products.count <= 0 {
                    presentOkAlert("Empty Basket", message: "Please add at least one product to the basket in order to apply an offer/reward.")
                    return
                }else{
                    performSegue(withIdentifier: "SelectRewardSegue", sender: self)
                }
            }
        }
        
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    
    // MARK:- Remove actions
    
    fileprivate func removeProduct(_ index: Int) {
        if BasketService.validateBasketForRewardAndOffer(){
            self.showRewardAndOfferAlert(index)
        } else{
            // Fishbowl Event
            let product = basket.products[index]
            let itemId = "\(product.productId)"
            let itemName = "PRODUCT_NAME = \(product.name);TOTAL_QUANTITY = \(product.quantity);TOTAL_COST = \(product.totalCost)"
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent(itemId, item_name: itemName, event_name: "REMOVE_PRODUCT")
            SVProgressHUD.show(withStatus: "Updating basket...")
             SVProgressHUD.setDefaultMaskType(.clear)
            BasketService.removeProduct(basket.products[index]) { (basket, error) -> Void in
                SVProgressHUD.dismiss()
                if error != nil{
                    self.presentError(error)
                }
            }
        }
    }
    
    fileprivate func removePromotion() {
        SVProgressHUD.show(withStatus: "Updating basket...")
         SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.removePromotionCode { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            self.presentError(error)
        }
    }
    
    fileprivate func removeReward() {
        // Fishbowl event
        var rewardName = ""
        if basket.appliedRewards.count > 0{
            if let reward = basket.appliedRewards.first{
                var rewardMembershipId = ""
                var rewardReference = ""
                if let rewardMemId = reward.membershipId{
                    rewardMembershipId = String(rewardMemId)
                }
                if let rewardReferenceValue = reward.reference{
                    rewardReference = rewardReferenceValue
                }
                rewardName = "REWARD_TITLE:\(reward.name);MEMBERSHIP_ID:\(rewardMembershipId);REFERENCE:\(rewardReference))"
            }
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("", item_name: rewardName, event_name: "REMOVE_REWARD")
        }
        SVProgressHUD.show(withStatus: "Updating basket...")
         SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.removeRewards { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            self.presentError(error)
        }
    }
    
    // MARK:- BasketOrderDeliveryModeTableViewCellDelegate
    //dispatch option  changed
    func orderDeliveryModeChanged(_ orderDeliveryMode: BasketDeliveryModeTableViewCell){
        
        if BasketService.sharedBasket!.deliveryMode == deliveryMode.pickup.rawValue {
            //if basket switched to delivery only swicth to pickup
            if BasketService.sharedBasket!.deliveryAddress != nil && BasketService.sharedBasket!.deliveryAddress!.id != "" {
                SVProgressHUD.show(withStatus: "Switching basket mode...")
                 SVProgressHUD.setDefaultMaskType(.clear)
                BasketService.changeDeliveryMode(deliveryMode.pickup.rawValue, callback: { (basket, error) in
                    SVProgressHUD.dismiss()
                    if error != nil {
                        self.presentError(error)
                    }
                    self.calculateStartAndEndTimesToUseOnUI(.asap)
                    self.selectedDay = .asap
                    BasketService.sharedBasket!.selectedPickupTime = nil
                    self.pickupTimeUsed = nil
                    self.tableView.reloadData()
                })
            } else {
                self.calculateStartAndEndTimesToUseOnUI(.asap)
                self.selectedDay = .asap
                self.pickupTimeUsed = nil
                BasketService.sharedBasket!.selectedPickupTime = nil
                self.tableView.reloadData()
            }
        } else {
            self.selectedDay = .asap
            self.pickupTimeUsed = nil
            BasketService.sharedBasket!.selectedPickupTime = nil
            self.tableView.reloadData()
        }
        
    }
    
    // MARK:- BasketOrderTimeTableViewCellDelegate
    
    func orderTimeCellDidChangeSelectedDay(_ orderTimeCell: BasketOrderTimeTableViewCell) {
        self.selectedDay = orderTimeCell.selectedDay()
        if ((BasketService.sharedBasket!.deliveryMode == deliveryMode.delivery.rawValue) && (PickupDay.tomorrow == orderTimeCell.selectedDay())) {
            //show the date picker for delivery option
            prepareLaterValueForDeliveryMode()
        } else {
            //calulate the slider values
            recalculateBasketPickupTimeFromOrderTimeUI(orderTimeCell, dayChanged: true)
        }
        tableView.reloadData()
    }
    
    func orderTimeCellSliderValueDidChange(_ orderTimeCell: BasketOrderTimeTableViewCell) {
        recalculateBasketPickupTimeFromOrderTimeUI(orderTimeCell, dayChanged: false)
    }
    
    
    // MARK: Helper
    
    fileprivate func populateOrderTimeUIFromBasketPickupTime(_ orderTimeCell: BasketOrderTimeTableViewCell)
    {
        let daySelected: PickupDay
        if basket.selectedPickupTime == nil {
            pickupTimeUsed = nil
            daySelected = .asap
        } else {
            pickupTimeUsed = basket.selectedPickupTime! as Date
            if pickupTimeUsed!.isTodayInGregorianCalendar() {
                daySelected = .today
            }
            else  {
                daySelected = .tomorrow
            }
        }
        
        //Calculate the start and end time
        calculateStartAndEndTimesToUseOnUI(daySelected)
        //        selectedDay = daySelected
        
        //Adjust Pickup time according to new times.
        adjustPickupTimeUsedAccordingToStartAndEndTime()
        
        //Now snap the time to next 15 min Interval.
        snapPickupTimeUsedToNext15MinInterval()
        
        //Set up max value of slider according to new times
        setupOrderTimeCellSliderMaxValue(orderTimeCell)
        
        //Setup Slider Value according to pickup time
        setupOrderTimeCellSliderValueFromPickupTimeUsed(orderTimeCell)
        
        //Populate the Labels and Button with the models.
        populateOrderTimeCellLabelsAndButons(orderTimeCell, day: daySelected)
        
        //Update store start and end time
        reloadStoreScheduele()
    }
    
    fileprivate func recalculateBasketPickupTimeFromOrderTimeUI(_ orderTimeCell: BasketOrderTimeTableViewCell, dayChanged: Bool) {
        let daySelected = orderTimeCell.selectedDay()
        
        //Calculate the start and end time
        calculateStartAndEndTimesToUseOnUI(daySelected)
        //        selectedDay = daySelected
        
        //Set up max value of slider according to new times
        setupOrderTimeCellSliderMaxValue(orderTimeCell)
        
        if dayChanged {
            //Adjust already used time for day change. i.e same time is new day
            adjustPickupTimeUsedForDayChangeAndSetSliderValues(daySelected)
            
            //Adjust Pickup time according to new times.
            adjustPickupTimeUsedAccordingToStartAndEndTime()
            
            //Now snap the time to next 15 min Interval.
            snapPickupTimeUsedToNext15MinInterval()
            
            //Setup Slider Value according to pickup time
            setupOrderTimeCellSliderValueFromPickupTimeUsed(orderTimeCell)
        } else {
            //Snap the slider value to nearest 15 min interval
            snapSliderValueToNearest15MinInterval(orderTimeCell)
            
            //Set the pickup time property of basket model
            setupBasketPickupTimeFromOrderTimeCellSliderValue(orderTimeCell)
        }
        
        //Populate the Labels and Button with the models.
        populateOrderTimeCellLabelsAndButons(orderTimeCell, day: daySelected)
    }
    
    fileprivate func calculateStartAndEndTimesToUseOnUI(_ day: PickupDay) {
        switch day {
        case .asap:
            startDateTimeUsed = nil
            endDateTimeUsed = nil
            pickupTimeUsed = nil
            return
        case .today:
            startDateTimeUsed = restaurantStartEndToday?.start as Date?
            endDateTimeUsed = restaurantStartEndToday?.end as Date?
            
        case .tomorrow:
            if BasketService.sharedBasket!.deliveryMode == deliveryMode.delivery.rawValue {
                startDateTimeUsed = restaurantStartEndSelectedDay?.start as Date?
                endDateTimeUsed = restaurantStartEndSelectedDay?.end as Date?
            } else {
                startDateTimeUsed = restaurantStartEndTomorrow?.start as Date?
                endDateTimeUsed = restaurantStartEndTomorrow?.end as Date?
            }
            
        }
        
        if startDateTimeUsed == nil || endDateTimeUsed == nil {
            storeOpenStatusText = "Store closed"
            return
        }
        
        // Truncate order time to 11:45
        // But do so only if:
        //   endTime is in future from start and not in the same day.
        //   if in past day, then we really don't need truncation as it is invalid state. And handled ahead in code
        //   if on same day. Then we are good as it is.
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        if endDateTimeUsed!.timeIntervalSince1970 > startDateTimeUsed!.timeIntervalSince1970 && !calendar.isDate(endDateTimeUsed!, inSameDayAs: startDateTimeUsed!) {
            var comps = DateComponents()
            comps.day = startDateTimeUsed!.dayOfMonthInGregorianCalendar()
            comps.month = startDateTimeUsed!.monthOfYearInGregorianCalendar()
            comps.year = startDateTimeUsed!.yearInGregorianCalendar()
            comps.hour = 23
            comps.minute = 45
            let newEndDateTimeUsed = calendar.date(from: comps)!
            //In case start time after 23:45. We can't order on the day as 23:45 is last
            //order slot for the day. So the store closed or order not possible will appear
            endDateTimeUsed = newEndDateTimeUsed
        }else{
            let getMins:Double = Double(calendar.component(.minute, from: endDateTimeUsed!))
            if getMins.truncatingRemainder(dividingBy: 15) == 0{
                endDateTimeUsed = NSDate(timeInterval: -BasketViewController.interval, since: endDateTimeUsed!) as Date
            }else{
                let interval = -15
                let nextDiff = interval - calendar.component(.minute, from: endDateTimeUsed!) % interval
                endDateTimeUsed = calendar.date(byAdding: .minute, value: nextDiff, to: endDateTimeUsed!) ?? Date()
            }
        }
        
        // Adjust for Earliest ReadyTime and curren date-time
        let earliestReadyDateTime = basket.earliestReadyTime.dateFromOloDateTimeString()
//        let currentDateTime = Date()
        
        // Check if earliest ready time is after the startTime. Then we need to make earliest ready time the start time of UI.
        // As order can not be ready before that.
        if earliestReadyDateTime!.timeIntervalSince1970 > startDateTimeUsed!.timeIntervalSince1970 {
            startDateTimeUsed = earliestReadyDateTime as Date?
        }
        
        // Check if start time is in past. i.e before current time + 15 min in future. Then need to use current time + 15 min as the start time of UI
        // As order can only be 15min future.
//        if startDateTimeUsed!.timeIntervalSince1970 < (currentDateTime.timeIntervalSince1970 + BasketViewController.interval) {
//            startDateTimeUsed = Date(timeInterval: BasketViewController.interval, since: currentDateTime)
//        }
        
        
        // Adjust the startTime so it is at 15 mins interval
        // Time Since epoch
        var startTimeInterval = BasketViewController.interval
//        // if the delivery is selected then, get the difference value from response
        if BasketService.sharedBasket?.deliveryMode == deliveryMode.delivery.rawValue {
            var timeEstimate:Double = 0.0
            if (BasketService.sharedBasket!.leadTimeEstimateMinutes.truncatingRemainder(dividingBy: BasketViewController.intervalTime) == 0) {
                timeEstimate = BasketService.sharedBasket!.leadTimeEstimateMinutes
            } else {
                let multiples = Int(BasketService.sharedBasket!.leadTimeEstimateMinutes / BasketViewController.intervalTime) + 1
                 timeEstimate = Double(multiples) * BasketViewController.intervalTime
            }
            startTimeInterval = (timeEstimate * 60)
        }
        if day == PickupDay.today{
            let getMins:Double = Double(calendar.component(.minute, from: startDateTimeUsed!))
            if getMins.truncatingRemainder(dividingBy: 15.0) != 0{
                let interval = 15
                let nextDiff = interval - calendar.component(.minute, from: startDateTimeUsed!) % interval
                startDateTimeUsed = calendar.date(byAdding: .minute, value: nextDiff, to: startDateTimeUsed!) ?? Date()
            }
        }else{
            var secondsSinceEpoch = floor(startDateTimeUsed!.timeIntervalSince1970)
            // next fifteen min multiple
            secondsSinceEpoch +=  startTimeInterval - (secondsSinceEpoch.truncatingRemainder(dividingBy: BasketViewController.interval))
            startDateTimeUsed = Date(timeIntervalSince1970: secondsSinceEpoch)
        }
        
    
        // Ensure that we have not exceeded the store ending hours. If so, we can not order on the selected day.
        if startDateTimeUsed!.timeIntervalSince1970 > endDateTimeUsed!.timeIntervalSince1970 {
            startDateTimeUsed = nil
            endDateTimeUsed = nil
            storeOpenStatusText = "Store closed or not accepting orders"
        }
    }
    
    fileprivate func setupOrderTimeCellSliderMaxValue(_ orderTimeCell: BasketOrderTimeTableViewCell) {
        if startDateTimeUsed != nil && endDateTimeUsed != nil {
            orderTimeCell.slider.maximumValue = Float(endDateTimeUsed!.timeIntervalSinceReferenceDate - startDateTimeUsed!.timeIntervalSinceReferenceDate)
        } else {
            orderTimeCell.slider.maximumValue = 0
        }
    }
    
    fileprivate func adjustPickupTimeUsedForDayChangeAndSetSliderValues(_ daySelected: PickupDay) {
        if startDateTimeUsed == nil || endDateTimeUsed == nil || daySelected == .asap {
            pickupTimeUsed = nil
            basket.selectedPickupTime = nil
            if daySelected == .today {
                //if today cannot choose, then alert the user
                if let currentStore = CurrentStoreService.sharedInstance.currentStore {
                    self.presentOkAlert("Error", message: "\(currentStore.name) is currently closed.")
                }
            }
            return
        }
        
        var dateForDay = (daySelected == .tomorrow) ? Date(timeIntervalSinceNow: 24 * 60 * 60) : Date()
        
        //if the delivery option is selected, then get the date from the picker selected date
        if BasketService.sharedBasket!.deliveryMode == deliveryMode.delivery.rawValue {
            dateForDay = (daySelected == .tomorrow) ? pickerSelectedDate ?? Date() : Date()
        }
        
        var newSelectedTimeComps = DateComponents()
        newSelectedTimeComps.day = dateForDay.dayOfMonthInGregorianCalendar()
        newSelectedTimeComps.month = dateForDay.monthOfYearInGregorianCalendar()
        newSelectedTimeComps.year = dateForDay.yearInGregorianCalendar()
        
        //Select what time we will use for picking time on next day
        //If user has not selected anything, lets pick the earliest time
        if basket.selectedPickupTime == nil {
            newSelectedTimeComps.hour = 0
            newSelectedTimeComps.minute = 0
        } else {
            newSelectedTimeComps.hour = basket.selectedPickupTime!.hourOfDayInGregorianCalendar()
            newSelectedTimeComps.minute = basket.selectedPickupTime!.minOfHourInGregorianCalendar()
        }
        
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        let newSelectedPickupTime = calendar.date(from: newSelectedTimeComps)
        pickupTimeUsed = newSelectedPickupTime!
        
        // This is user interaction. If user had picked time we convert it to next day.
        // If user has not be select the earliest time of the day.
        basket.selectedPickupTime = newSelectedPickupTime!
    }
    
    fileprivate func snapSliderValueToNearest15MinInterval(_ orderTimeCell: BasketOrderTimeTableViewCell) {
        //Get the rounded value of slider so its int and then make NSTimeInterval for easy calculations
        let value = Double(orderTimeCell.slider.value)
        //Once int, find the nearest 15-min unit.
        let fifteenMinUnits = round(value / BasketViewController.interval)
        //New Value is fifteenMinUnits * 15
        let newValue: TimeInterval = fifteenMinUnits * BasketViewController.interval
        orderTimeCell.slider.setValue(Float(newValue), animated: false)
    }
    
    fileprivate func snapPickupTimeUsedToNext15MinInterval() {
        if pickupTimeUsed == nil {
            return
        }
        
        // Round time to nearest second
        let intervalSinceEpoch = round(pickupTimeUsed!.timeIntervalSince1970)
        // Once in seconds, find the nearest 15-min unit. Ceil So time is always in future.
        let fifteenMinUnits = ceil(intervalSinceEpoch / BasketViewController.interval)
        // New Value is fifteenMinUnits * 15
        let newValue: TimeInterval = fifteenMinUnits * BasketViewController.interval
        pickupTimeUsed = Date(timeIntervalSince1970: newValue)
    }
    
    fileprivate func setupBasketPickupTimeFromOrderTimeCellSliderValue(_ orderTimeCell: BasketOrderTimeTableViewCell) {
        if startDateTimeUsed != nil && endDateTimeUsed != nil {
            // Althoug no need to round here, but just in case.
            let sliderValue = TimeInterval(round(orderTimeCell.slider.value))
            // Selected Time as epoch
            let timeIntervalSince1970 = startDateTimeUsed!.timeIntervalSince1970 + sliderValue
            let seletedPickupTime = Date(timeIntervalSince1970: timeIntervalSince1970)
            // Only update basket time on user interaction.
            basket.selectedPickupTime = seletedPickupTime
            pickupTimeUsed = seletedPickupTime
        }
    }
    
    fileprivate func adjustPickupTimeUsedAccordingToStartAndEndTime() {
        if startDateTimeUsed != nil && endDateTimeUsed != nil && pickupTimeUsed != nil {
            let startIntervalSinceEpoch = startDateTimeUsed!.timeIntervalSince1970
            let endIntervalSinceEpoch = endDateTimeUsed!.timeIntervalSince1970
            let pickupTimeIntervalSinceEpoch = pickupTimeUsed!.timeIntervalSince1970
            // Ensure pickup time is in start-end range
            pickupTimeUsed = Date(timeIntervalSince1970: min(endIntervalSinceEpoch, max(startIntervalSinceEpoch, pickupTimeIntervalSinceEpoch)))
        }
    }
    
    fileprivate func setupOrderTimeCellSliderValueFromPickupTimeUsed(_ orderTimeCell: BasketOrderTimeTableViewCell) {
        if startDateTimeUsed != nil && endDateTimeUsed != nil && pickupTimeUsed != nil {
            let startIntervalSinceEpoch = startDateTimeUsed!.timeIntervalSince1970
            let pickupTimeIntervalSinceEpoch = pickupTimeUsed!.timeIntervalSince1970
            let sliderValue = pickupTimeIntervalSinceEpoch - startIntervalSinceEpoch
            orderTimeCell.slider.value = Float(sliderValue)
        } else {
            orderTimeCell.slider.value = 0
        }
    }
    
    fileprivate func populateOrderTimeCellLabelsAndButons(_ orderTimeCell: BasketOrderTimeTableViewCell, day: PickupDay) {
        orderTimeCell.selectDay(selectedDay)
        orderTimeCell.startTimeLabel.text = startDateTimeUsed?.timeString()
        orderTimeCell.endTimeLabel.text = endDateTimeUsed?.timeString()
        orderTimeCell.selectedTimeLabel.text = formatDisplaySelectedDateTime(pickupTimeUsed) ?? storeOpenStatusText
        
        //if store is hidden, then remove the slider
        if (pickupTimeUsed == nil && selectedDay != .asap) || (BasketService.sharedBasket!.deliveryMode == deliveryMode.delivery.rawValue && selectedDay == .tomorrow ) {
            orderTimeCell.pickupSliderViewHeight.constant = 30
            orderTimeCell.slider.isHidden = true
        } else if selectedDay == .asap {
            orderTimeCell.pickupSliderViewHeight.constant = 0
            orderTimeCell.slider.isHidden = true
        } else {
            orderTimeCell.pickupSliderViewHeight.constant = 82
            orderTimeCell.slider.isHidden = false
        }
    }
    
    fileprivate func validatePickUpTime() -> Bool {
        if basket.timeWanted == nil {
            return true // ASAP
        }
        
        let timeWantedDateTime = basket.timeWanted!.dateFromOloDateTimeString()
        
        // Earliest ready time
        let earliestReadyDateTime = basket.earliestReadyTime.dateFromOloDateTimeString()
        if earliestReadyDateTime!.timeIntervalSince1970 > timeWantedDateTime?.timeIntervalSince1970 {
            presentOkAlert("Pickup time", message: "Please select a time after \(basket.earliestReadyTime).")
            return false
        }
        
        // I am still working on time travel :)
//        let currentDateTime = Date()
//        if currentDateTime.timeIntervalSince1970 > timeWantedDateTime?.timeIntervalSince1970 {
//            presentOkAlert("Pickup time", message: "Please select a time in the future.")
//            return false
//        }
        
        return true
    }
    
    fileprivate func setupTimeWantedFromBasketPickupTime() {
        //Clear any past value
        basket.timeWanted = nil
        basket.selectedPickupTime = nil
        
        //If we have valid start and end time. The use the pick up time value.
        if startDateTimeUsed != nil && endDateTimeUsed != nil && pickupTimeUsed != nil {
            //User interaction. So pickup whats on UI
            basket.selectedPickupTime = pickupTimeUsed
            let timeWantedString = basket.selectedPickupTime!.oloDateTimeString()
            basket.timeWanted = timeWantedString
        }
    }
    
    
    // MARK: MGSwipe Delegate
    
    func swipeTableCell(_ cell: MGSwipeTableCell, tappedButtonAt index: Int, direction: MGSwipeDirection, fromExpansion: Bool) -> Bool {
        if let indexPath = tableView.indexPath(for: cell) {
            if indexPath.section == 0 && basket.products.count > 0 {
                removeProduct(indexPath.row)
                return true
            }
            
            if indexPath.section == 3 && basket.promotionCode != nil {
                removePromotion()
                return true
            }
            
            if indexPath.section == 3 && basket.appliedRewards.count > 0 {
                removeReward()
                return true
            }
        }
        return true
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
    
    // Dismiss view controller from store detail screen
    func dismissFromStoreDetailViewController(){
        (navigationController as? BasketNavigationController)?.dismissWhenRestartOrder()
    }
    
    // Dismiss view from pushnotification received
    func dismissWhenPushNotificationViewOffer(){
        (navigationController as? BasketNavigationController)?.dimissWhenPushNotificationViewOffer()
    }
    
    // Dismis view from shortcut item
    func dismissFromShortcutItem(){
        (navigationController as? BasketNavigationController)?.dimissWhenShortcutItem()
    }
    
    
    // Proceed order
    func proceedOrder(){
        
        // Fishbowl Rewards redeamd
        var rewardItemId = ""
        var rewardItemName = ""
        let rewards = basket.appliedRewards
        if rewards.count > 0{
            if let reward = rewards.first{
                if reward.rewardId != nil{
                    rewardItemId = "\(reward.rewardId!)"
                }
                
                rewardItemName = reward.name
            }
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent(rewardItemId, item_name: rewardItemName, event_name: "REWARDS_REDEEMED")
        }
        
        
        self.trackButtonPress(checkoutButton)
        //Set time and validate from server
        SVProgressHUD.show(withStatus: "Updating basket...") // Avoid two messages here
         SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.updateTimeWantedAndValidate { (error) -> Void in
            if error != nil {
                SVProgressHUD.dismiss()
                self.presentError(error)
                return
            }
            
            // Take to payment/credit card screen
            if UserService.sharedUser != nil {
                //User is logged in.
                // Try to fetch the credit cards.
                BasketService.billingSchemes { (basket, error) -> Void in
                    SVProgressHUD.dismiss()
                    if error != nil {
                        self.presentError(error)
                        //Do not return. Try to use any existing data.
                    }
                    // If credit cars count is non-zero navigate to payment method
                    //else move to enter credit card
                    if BasketService.sharedBasket!.billingSchemes != nil && BasketService.sharedBasket!.billingSchemes!.count > 0 {
                        let count = BasketService.sharedBasket!.billingSchemes!.reduce(0) { (initial, scheme) -> Int in
                            initial + scheme.accounts.count
                        }
                        if count > 0 {
                            //Segue Identifier: "SelectPaymentMethod"
                            self.performSegue(withIdentifier: "SelectPaymentMethod", sender: self)
                            return
                        }
                    }
                    //Segue Identifier2: "EnterCreditCard"
                    self.performSegue(withIdentifier: "EnterCreditCard", sender: self)
                }
                return
            }
            
            //User is not logged in, navigate to enter user info and credit card
            SVProgressHUD.dismiss()
            self.performSegue(withIdentifier: "EnterGuestUserInfo", sender: self)
        }
    }
    
    // Remove/apply an offer
    func showRewardAndOfferAlert(_ index: Int){
        self.presentConfirmationWithYesOrNo("Alert", message: "Changing basket contents will remove applied discounts. Coupon / reward will need to be re-applied manually. Proceed with changes?", buttonTitle: "Yes") { (confirmed) in
            if confirmed{
                if BasketService.validateAppliedReward(){
                    SVProgressHUD.show(withStatus: "Updating basket...")
                    SVProgressHUD.setDefaultMaskType(.clear)
                    BasketService.removeRewards({ (basket, error) in
                        if error != nil{
                            SVProgressHUD.dismiss()
                            self.presentError(error)
                        }else{
                            self.removeProduct(index)
                        }
                    })
                } else if BasketService.validateAppliedOffer(){
                    SVProgressHUD.show(withStatus: "Updating basket...")
                    SVProgressHUD.setDefaultMaskType(.clear)
                    BasketService.removePromotionCode({ (basket, error) in
                        if error != nil{
                            SVProgressHUD.dismiss()
                            self.presentError(error)
                        }else{
                            self.removeProduct(index)
                        }
                    })
                }
            } else{
                return
            }
        }
    }
    
    //MARK:-reset dispatch mode
    func resetDeliveryMode() {
        selectedDay = .asap
        self.pickupTimeUsed = nil
        calculateStartAndEndTimesToUseOnUI(.asap)
        tableView.reloadData()
    }
    
    //prepare date values for the Picker
    func prepareLaterValueForDeliveryMode() {
        deliveryLaterValue = []
        
        //calculate for the store opening time
        if pickerSelectedDate == nil {
            restaurantStartEndSelectedDay = store.storeSchedule!.startAndEndDateTimesForDate(getNthtDay(Date(), dayValue: 1))
            calculateStartAndEndTimesToUseOnUI(.tomorrow)
            pickerSelectedDate = startDateTimeUsed
        }
        
        //retain value
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "E, dd MMM"
        let retainSelectedDay   = dateFormatter.string(from: pickerSelectedDate!)
        var retainSelectedIndex = 0
        
        var nextDay = getNthtDay(Date(), dayValue: 1)
        for i in 0..<BasketService.sharedBasket!.maxNoOfDaysForLater {
            
            
            //skip the day, if it is holiday
            restaurantStartEndSelectedDay = store.storeSchedule!.startAndEndDateTimesForDate(nextDay)
            if (restaurantStartEndSelectedDay?.start?.timeIntervalSince1970 != restaurantStartEndSelectedDay?.end?.timeIntervalSince1970) && (restaurantStartEndSelectedDay?.start != nil) {
                deliveryLaterValue.insert(dateFormatter.string(from: nextDay), at: i)
                
                //retain previous selected value
                if retainSelectedDay == dateFormatter.string(from: nextDay) {
                    retainSelectedIndex = i
                }
            }
            nextDay = getNthtDay(nextDay, dayValue: 1)
            
        }
        
        prepareTimeHourForDeliveryMode()
        UIView.transition(with: datePickerView, duration: 0.5, options: .transitionCrossDissolve, animations: {
            self.datePickerView.isHidden = false
            }, completion: nil)
        picker.reloadComponent(0)
        picker.selectRow(retainSelectedIndex, inComponent: 0, animated: false)
    }
    
    //prepare Hour values for the Picker
    func prepareTimeHourForDeliveryMode() {
        deliveryLaterHourValue = []
        var retainSelectedIndex = 0
        
        //get the store opening & closing time for selected date
        restaurantStartEndSelectedDay = store.storeSchedule!.startAndEndDateTimesForDate(pickerSelectedDate!)
        
        if restaurantStartEndSelectedDay?.start != nil {
            //add the delivery awaits time
            
            //claulate the picker time (add lead estimate time ....)
            calculateStartAndEndTimesToUseOnUI(.tomorrow)
            
            //if Picker Select day is less than store opening time, Then update the time as store opening time
            if pickerSelectedDate!.timeIntervalSinceNow < startDateTimeUsed!.timeIntervalSinceNow {
                pickerSelectedDate = startDateTimeUsed
            }
            
            
            //get the Hour within the range of store oprn time
            let calendar = Calendar.current
            var date = startDateTimeUsed
            
            //retain selected value
            var i = 0
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "HH"
            
            let retainSelectedDay   = dateFormatter.string(from: pickerSelectedDate!)
            
            
            let maxHour = (calendar as NSCalendar).date(byAdding: .day, value: 1, to: (restaurantStartEndSelectedDay?.start!)! as Date, options: [])
            
            //add the hour value in array
            deliveryLaterHourValue.insert(dateFormatter.string(from: date!), at: i)
            while (date?.timeIntervalSince1970 < endDateTimeUsed?.timeIntervalSince1970) {
                i += 1
                date = (calendar as NSCalendar).date(byAdding: .minute, value: 60, to: date!, options: [])
                
                //precaution for add max 24 hrs
                if maxHour?.timeIntervalSince1970 > date?.timeIntervalSince1970 {
                    deliveryLaterHourValue.insert(dateFormatter.string(from: date!), at: i)
                    
                    //retain selected value
                    if retainSelectedDay == dateFormatter.string(from: date!) {
                        retainSelectedIndex = i
                    }
                }
                
            }
        }
        
        prepareTimeMinuteForDeliveryMode()
        picker.reloadComponent(1)
        picker.selectRow(retainSelectedIndex, inComponent: 1, animated: false)
    }
    
    //prepare Minute values for the Picker
    func prepareTimeMinuteForDeliveryMode() {
        deliveryLaterMinuteValue = []
        
        //if the previous Date retain
        let calendar = Calendar.current
        let prevDateFormatter = DateFormatter()
        prevDateFormatter.dateFormat = "dd-MM-yyyy HH:00"
        let tempDate:String = prevDateFormatter.string(from: pickerSelectedDate!)
        let dateFormat = DateFormatter()
        dateFormat.dateFormat = "dd-MM-yyyy HH:mm"
        var date = dateFormat.date(from: "\(tempDate)")
        var retainSelectedIndex = 0
        
        //if store opening time is null, then get it from store info
        if startDateTimeUsed == nil {
            //get the store opening & closing time for selected date
            restaurantStartEndSelectedDay = store.storeSchedule!.startAndEndDateTimesForDate(pickerSelectedDate!)
            calculateStartAndEndTimesToUseOnUI(.tomorrow)
        }
        
        //Check the slected time is after the store opening time
        if pickerSelectedDate?.timeIntervalSince1970 < startDateTimeUsed?.timeIntervalSince1970 || date?.timeIntervalSince1970 < startDateTimeUsed?.timeIntervalSince1970 {
            date = startDateTimeUsed
        }
        
        //get the valid minutes from the Store opening hours
        var i = 0
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "mm"
        let retainSelectedDay = dateFormatter.string(from: pickerSelectedDate!)
        
        var comp = (calendar as NSCalendar).components([.hour, .minute], from: date!)
        var minute = comp.minute
        
        //Check for the Store closing buffer time (interval time)
        let maxMinute = 60 - Int(BasketService.sharedBasket!.intervalTime)
        repeat {
            if (i > 0 && minute == 0) {
                break
            }
            deliveryLaterMinuteValue.insert(dateFormatter.string(from: date!), at: i)
            
            //retain selected value
            if retainSelectedDay == dateFormatter.string(from: date!) {
                retainSelectedIndex = i
            }
            
            //add the next interval b/w the hour
            i += 1
            date = (calendar as NSCalendar).date(byAdding: .minute, value: Int(BasketService.sharedBasket!.intervalTime), to: date!, options: [])
            
            comp = (calendar as NSCalendar).components([.hour, .minute], from: date!)
            minute = comp.minute

        }while (maxMinute >= minute! && date!.timeIntervalSince1970 <= endDateTimeUsed?.timeIntervalSince1970)
        
        picker.reloadComponent(2)
        picker.selectRow(retainSelectedIndex, inComponent: 2, animated: false)
    }
    
    //get the nth day value from passing day count
    func getNthtDay(_ date:Date, dayValue: Int) -> Date {
        return (Calendar.current as NSCalendar).date(byAdding: .day,
                                                             value: dayValue, to: date, options: [])!
    }
    
    //return the selected date string
    func formatDisplaySelectedDateTime(_ date:Date?) -> String? {
        if date == nil {
            return nil
        }
        let dateFormatter = DateFormatter()
        //show date for 
        if basket.deliveryMode == deliveryMode.delivery.rawValue && selectedDay == PickupDay.tomorrow {
            dateFormatter.dateFormat = "E, dd MMM h:mm a"
        } else {
            dateFormatter.dateFormat = "h:mm a"
        }
        return dateFormatter.string(from: date!)
    }
    
    //MARK:- Picker View
    //MARK: - Picker view methods
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 3;
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int{
        if component == 0 {
            return deliveryLaterValue.count;
        } else if component == 1 {
            return deliveryLaterHourValue.count;
        } else  {
            return deliveryLaterMinuteValue.count;
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if component == 0 {
            return deliveryLaterValue[row];
        } else if component == 1 {
            return deliveryLaterHourValue[row];
        } else  {
            return deliveryLaterMinuteValue[row];
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        let userSelectedDay = getNthtDay(Date(), dayValue: row+1)
        let yearFormatter = DateFormatter()
        yearFormatter.dateFormat = "yyyy"
        let year = yearFormatter.string(from: userSelectedDay)
        
        //precaution for Picker view Wrong Input
        pickerView.selectRow(row, inComponent: component, animated: false)
        
        //precation for array index
        if deliveryLaterValue.count > 0 && deliveryLaterHourValue.count > 0 && deliveryLaterMinuteValue.count > 0 {
            
            //Date is selected
            if component == 0 {
                
                //update user selected date
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "E, dd MMM yyyy HH mm"
                let dateString:String = "\(deliveryLaterValue[pickerView.selectedRow(inComponent: 0)]) \(year) \(deliveryLaterHourValue[pickerView.selectedRow(inComponent: 1)]) \(deliveryLaterMinuteValue[pickerView.selectedRow(inComponent: 2)])"
                
                print(dateString)
                pickerSelectedDate = dateFormatter.date(from: dateString)
                
                //calculate the store opening & closing stime for the user selected date
                restaurantStartEndSelectedDay = store.storeSchedule!.startAndEndDateTimesForDate(pickerSelectedDate!)
                calculateStartAndEndTimesToUseOnUI(.tomorrow)
                
                pickerSelectedDate = startDateTimeUsed
                
                //claculate the Store Opening hours & minutes
                prepareTimeHourForDeliveryMode()
                
                //reset the hour & minute components in picker view
                pickerView.reloadComponent(1)
                pickerView.selectRow(0, inComponent: 1, animated: true)
                pickerView.selectRow(0, inComponent: 2, animated: true)
                
                //Hour is selected
            } else if component == 1 {
                
                //update user selected date & Hour
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "E, dd MMM yyyy HH mm"
                let dateString:String = "\(deliveryLaterValue[pickerView.selectedRow(inComponent: 0)]) \(year) \(deliveryLaterHourValue[pickerView.selectedRow(inComponent: 1)]) 00"
                pickerSelectedDate = dateFormatter.date(from: dateString)
                
                prepareTimeMinuteForDeliveryMode()
                
                //reset the  minute components in picker view
                pickerView.selectRow(0, inComponent: 2, animated: true)
            } else if component == 2 {
                //update user selected date & Hour
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "E, dd MMM yyyy HH mm"
                let dateString:String = "\(deliveryLaterValue[pickerView.selectedRow(inComponent: 0)]) \(year) \(deliveryLaterHourValue[pickerView.selectedRow(inComponent: 1)]) \(deliveryLaterMinuteValue[pickerView.selectedRow(inComponent: 2)])"
                pickerSelectedDate = dateFormatter.date(from: dateString)
            }
        }
        pickerView.reloadComponent(2)
        
    }
    
    //adjust pickerView Width
    func pickerView(_ pickerView: UIPickerView, widthForComponent component: Int) -> CGFloat {
        //resize the width for day selection
        let width:CGFloat = pickerView.frame.width / 4
        
        //day component 2 times the normal width
        if component == 0{
            return (2 * width)
        } else {
            return width
        }
    }
    
}
