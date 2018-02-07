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


enum PickupDay {
    case ASAP
    case Today
    case Tomorrow
}

enum SeeMore : Int {
    case kSeeMoreNone = -1
    case kSeeMoreExpand = 0
    case kSeeMoreCollapse = 1
}



private typealias BasketViewControllerAnimationCompleteCallback = () -> Void


class BasketViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, BasketOrderTimeTableViewCellDelegate, MGSwipeTableCellDelegate {
    
    static let interval: NSTimeInterval = (15 * 60)
    
    @IBOutlet weak var checkoutCostLabel: UILabel!
    @IBOutlet weak var checkoutButton: UIButton!
    @IBOutlet weak var tableView: UITableView!
    
    // Restaurant open / close times (nil is closed)
    private var restaurantStartEndToday: TimeRange?
    private var restaurantStartEndTomorrow: TimeRange?
    
    // Times displayed on UI
    private var startDateTimeUsed: NSDate?
    private var endDateTimeUsed: NSDate?
    private var pickupTimeUsed: NSDate?  // will be nil for ASAP orders
    private var storeOpenStatusText: String = ""
    
    var arrayProductName : NSMutableArray = []
    var arrayProductID : NSMutableArray = []
    var productIDStr : NSString!
    
    private var basket: Basket {
        get {
            return BasketService.sharedBasket!
        }
    }
    
    private var store: Store {
        get {
            return BasketService.sharedBasket!.store
        }
    }
    
    var seeMoreArray = [Int]()
    
    private weak var timer: NSTimer?
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.Orange)
        tableView.estimatedRowHeight = 70
        tableView.rowHeight = UITableViewAutomaticDimension
        loadStoreScheduleIfNeeded()
        updateCheckoutCostLabel()
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(BasketViewController.sharedBasketUpdated(_:)), name: JambaNotification.SharedBasketUpdated.rawValue, object: nil)
        
        // Dismiss when guest request reorder
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(BasketViewController.dismissFromStoreDetailViewController), name: JambaNotification.ReStartOrderForGuest.rawValue, object: nil)
        
        // Dismiss when user request reorder
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(BasketViewController.dismissFromStoreDetailViewController), name: JambaNotification.ReStartOrderForUser.rawValue, object: nil)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        //We want the timer to be active if view is on-screen
        startTimer()
        // Refresh with promotions and rewards
        tableView.reloadData()
    }
    
    override func viewDidDisappear(animated: Bool) {
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
                    cost = cost + (choice.cost * Double(product.quantity))
                }
            }
        }
        return cost
    }
    
    
    
    @IBAction func checkout(sender: UIButton) {
        // Validate Basket locally
        if validateBasketForCheckout() == false {
            return
        }
        
        // Confirmation alert with change store, cancel and proceed choices
        let alertController = UIAlertController(title: "Checkout Confirmation", message: "Please confirm your order will be placed at the \(basket.store.name) store.", preferredStyle: .ActionSheet)
        
        let proceedAction = UIAlertAction(title: "Proceed", style: .Default, handler: {
            action in
            self.proceedOrder()
        })
        alertController.addAction(proceedAction)
        
        let changeStoreAction = UIAlertAction(title: "Change Store", style: .Default, handler: {
            action in
            self.performSegueWithIdentifier("StoreLocatorSegue", sender: self)
        })
        alertController.addAction(changeStoreAction)
        
        var cancelAction = UIAlertAction()
        
        // For iPad Support validation
        if UIDevice.currentDevice().userInterfaceIdiom == .Pad{
            alertController.popoverPresentationController?.sourceView = sender
            alertController.popoverPresentationController?.sourceRect = sender.bounds
            cancelAction = UIAlertAction(title: "Cancel", style: .Default, handler: {
                action in
                return
            })
        }
            
        else{
            cancelAction = UIAlertAction(title: "Cancel", style: .Cancel, handler: {
                action in
                return
            })
        }
        
        alertController.addAction(cancelAction)
        
        self.presentViewController(alertController, animated: true, completion: nil)
    }
    
    private func validateBasketForCheckout() -> Bool {
        let basket = BasketService.sharedBasket!
        if basket.products.count <= 0 {
            presentOkAlert("Empty Basket", message: "Please add at least one product to the basket in order to check out.")
            return false
        }
        
        //Set time wanted in basket.
        setupTimeWantedFromBasketPickupTime()
        if !validatePickUpTime() {
            return false
        }
        
        return true
    }
    
    // MARK: Notifications
    
    func sharedBasketUpdated(notification: NSNotification) {
        //Check from baster service since we force unwrap.
        if BasketService.sharedBasket != nil {
            updateCheckoutCostLabel()
            tableView.reloadData()
        }
    }
    
    // MARK: Navigation
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "StoreDetail" {
            (segue.destinationViewController as! StoreViewController).store = basket.store
        }
        else if segue.identifier == "EnterCreditCard"
        {
            if let offerId = BasketService.sharedBasket?.offerId
            {
                (segue.destinationViewController as! EnterCreditCardViewController).appliedOfferId =  offerId }
            
        }
            
        else if segue.identifier == "SelectPaymentMethod"
        {
            if let offerId = BasketService.sharedBasket?.offerId
            {
                (segue.destinationViewController as! PaymentMethodViewController).appliedOfferId =  offerId }
        }
            
        else if segue.identifier == "StoreLocatorSegue"{
            let nc = segue.destinationViewController as! UINavigationController
            let vc = nc.viewControllers[0] as! StoreLocatorViewController
            vc.storeSearchTypeOrderAhead = true
            vc.basketTransfer = true
        }
    }
    
    @IBAction func dismiss(sender: UIButton) {
        trackButtonPressWithName("Back")
        dismiss()
    }
    
    @IBAction func deleteBasket(sender: UIButton) {
        trackButtonPressWithName("DeleteBasket")
        presentConfirmation("Empty Basket", message: "Emptying your basket will cancel the current order. Continue?", buttonTitle: "Empty") { confirmed in
            if confirmed {
                SVProgressHUD.showWithStatus("Deleting Basket...", maskType: .Clear)
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
    
    // MARK: Private
    
    private func dismiss() {
        (navigationController as? BasketNavigationController)?.dismiss()
    }
    
    private func startTimer() {
        stopTimer()
        let now = NSDate()
        //Add interval
        let fifteenMinFromNow = now.timeIntervalSince1970 + BasketViewController.interval
        //
        let roundedOff = (fifteenMinFromNow - (fifteenMinFromNow % BasketViewController.interval)) + 1 //1 second after the 15min mark
        let timer = NSTimer(fireDate: NSDate(timeIntervalSince1970: roundedOff), interval: BasketViewController.interval, target: self, selector: #selector(BasketViewController.intervalUpdate), userInfo: nil, repeats: true)
        NSRunLoop.currentRunLoop().addTimer(timer, forMode: NSDefaultRunLoopMode)
        self.timer = timer
    }
    
    private func stopTimer() {
        timer?.invalidate()
        timer = nil
    }
    
    // MARK: - NSTimer Callback
    
    func intervalUpdate() {
        tableView.reloadSections(NSIndexSet(index: 2), withRowAnimation: .Automatic)
    }
    
    private func loadStoreScheduleIfNeeded() {
        if store.storeSchedule == nil || !store.storeSchedule!.hasTimingsForTodayAndTomorrow() {
            loadStoreSchedule()
        } else {
            populateStartAndEndDateTimes()
            tableView.reloadSections(NSIndexSet(index: 2), withRowAnimation: .Automatic)
        }
    }
    
    private func loadStoreSchedule() {
        StoreService.storeSchedule(store.restaurantId!) { (schedule, error) in
            assertMainThread()
            if error != nil {
                self.presentError(error)
                return
            }
            self.store.storeSchedule = schedule
            self.populateStartAndEndDateTimes()
            self.tableView.reloadSections(NSIndexSet(index: 2), withRowAnimation: .Automatic)
        }
    }
    
    private func populateStartAndEndDateTimes() {
        restaurantStartEndToday = store.storeSchedule!.startAndEndDateTimesForToday()
        restaurantStartEndTomorrow = store.storeSchedule!.startAndEndDateTimesForTomorrow()
    }
    
    
    // MARK: Table View DataSource
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 5
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //Items
        if section == 0 {
            return max(basket.products.count, 1)
        }
            //Store,Time,Rewards,Total
        else {
            return 1
        }
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        //Store
        if indexPath.section == 1 {
            let cell = tableView.dequeueReusableCellWithIdentifier("BasketStoreTableViewCell", forIndexPath: indexPath) as! BasketStoreTableViewCell
            cell.nameAndAddrLabel.text = "\(basket.store.name)\n\(basket.store.address)"
            return cell
        }
        
        //Items
        if indexPath.section == 0 {
            if basket.products.count == 0 {
                return tableView.dequeueReusableCellWithIdentifier("BasketEmptyTableViewCell")!
            }
            
            let cell = tableView.dequeueReusableCellWithIdentifier("BasketBasicTableViewCell", forIndexPath: indexPath) as! BasketItemTableViewCell
            
            if (seeMoreArray.count == indexPath.row) {
                cell.update(basket.products[indexPath.row],state: 0)
                let noOfLines = BasketItemTableViewCell.lines(BasketItemTableViewCell.descriptionLabelWidth,label: cell.descriptionLabel)
                if (noOfLines > BasketItemTableViewCell.defaultNoOfLines) {
                    seeMoreArray.insert(SeeMore.kSeeMoreCollapse.rawValue, atIndex: indexPath.row)
                } else {
                    seeMoreArray.insert(SeeMore.kSeeMoreNone.rawValue, atIndex: indexPath.row)
                }
            }
            
            
            isIDContain = true
            
            if(!arrayProductName.containsObject(basket.products[indexPath.row].name))
            {
                arrayProductName.addObject(basket.products[indexPath.row].name)
                productName = arrayProductName.componentsJoinedByString(",")
            }
            
            if(!arrayProductID.containsObject(String(basket.products[indexPath.row].id)))
            {
                arrayProductID.addObject(String(basket.products[indexPath.row].id))
                productIDStr = arrayProductID.componentsJoinedByString(",")
            }
            
            
            
            
            
            cell.update(basket.products[indexPath.row],state: seeMoreArray[indexPath.row])
            cell.seeMoreButton.tag = indexPath.row
            cell.delegate = self
            //            cell.frame.size.height = 150;
            return cell
        }
        
        
        // Time
        if indexPath.section == 2 {
            let cell = tableView.dequeueReusableCellWithIdentifier("BasketOrderTimeTableViewCell", forIndexPath: indexPath) as! BasketOrderTimeTableViewCell
            cell.delegate = self
            populateOrderTimeUIFromBasketPickupTime(cell)
            return cell
        }
        
        // Rewards Or Promotion
        if indexPath.section == 3 {
            let cell = tableView.dequeueReusableCellWithIdentifier("SelectRewardTableViewCell", forIndexPath: indexPath) as! BasketNavigationTableViewCell
            cell.updateForBasket(basket)
            cell.delegate = self
            return cell
        }
        
        // Total
        let cell = tableView.dequeueReusableCellWithIdentifier("BasketTotalTableViewCell", forIndexPath: indexPath) as! BasketTotalTableViewCell
        cell.subtotalCostLabel.text = String(format:  "$%.2f", basket.subTotal)
        cell.discountLabel.text     = String(format: "-$%.2f", basket.discount)
        cell.taxCostLabel.text      = String(format:  "$%.2f", basket.salesTax)
        cell.totalCostLabel.text    = String(format:  "$%.2f", basket.total)
        return cell
    }
    
    // MARK: - TableView Delegate
    
    func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        
        switch section {
        case 0: return "Items"
        case 1: return "Store"
        case 2: return "Pickup Date & Time"
        case 3: return "Rewards & Promotions"
        case 4: return "Total"
        default: assert(false, "Unexpected section number.")
        }
        return nil
    }
    
    func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont(name: Constants.archerMedium, size: 18)
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if indexPath.section == 0 && basket.products.count > 0 {
            let cell = tableView.cellForRowAtIndexPath(indexPath) as! BasketItemTableViewCell
            cell.showSwipe(.RightToLeft, animated: true)
        }
        if indexPath.section == 3 {
            if UserService.sharedUser == nil {
                performSegueWithIdentifier("EnterPromotionSegue", sender: self)
            } else {
                performSegueWithIdentifier("SelectRewardSegue", sender: self)
            }
        }
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
    
    // MARK: Remove actions
    
    private func removeProduct(index: Int) {
        SVProgressHUD.showWithStatus("Updating basket...", maskType: .Clear)
        BasketService.removeProduct(basket.products[index]) { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            
            if error == nil
            {
                if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                {
                    clpAnalyticsService.sharedInstance.clpTrackScreenView("REMOVE_PRODUCT");
                    clpAnalyticsService.sharedInstance.clpTrackScreenView("REMOVE_BOOST");
                }
            }
            else
            {
                self.presentError(error)
            }
        }
    }
    
    private func removePromotion() {
        SVProgressHUD.showWithStatus("Updating basket...", maskType: .Clear)
        BasketService.removePromotionCode { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            self.presentError(error)
        }
    }
    
    private func removeReward() {
        SVProgressHUD.showWithStatus("Updating basket...", maskType: .Clear)
        BasketService.removeRewards { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            self.presentError(error)
        }
    }
    
    
    
    // MARK: BasketOrderTimeTableViewCellDelegate
    
    func orderTimeCellDidChangeSelectedDay(orderTimeCell: BasketOrderTimeTableViewCell) {
        recalculateBasketPickupTimeFromOrderTimeUI(orderTimeCell, dayChanged: true)
        tableView.reloadSections(NSIndexSet(index: 2), withRowAnimation: .None)
    }
    
    func orderTimeCellSliderValueDidChange(orderTimeCell: BasketOrderTimeTableViewCell) {
        recalculateBasketPickupTimeFromOrderTimeUI(orderTimeCell, dayChanged: false)
    }
    
    
    // MARK: Helper
    
    private func populateOrderTimeUIFromBasketPickupTime(orderTimeCell: BasketOrderTimeTableViewCell)
    {
        let daySelected: PickupDay
        if basket.selectedPickupTime == nil {
            pickupTimeUsed = nil
            daySelected = .ASAP
        } else {
            pickupTimeUsed = basket.selectedPickupTime!
            if pickupTimeUsed!.isTodayInGregorianCalendar() {
                daySelected = .Today
            }
            else if pickupTimeUsed!.isTomorrowInGregorianCalendar() {
                daySelected = .Tomorrow
            }
            else {
                pickupTimeUsed = nil
                daySelected = .ASAP
            }
        }
        
        //Calculate the start and end time
        calculateStartAndEndTimesToUseOnUI(daySelected)
        
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
    }
    
    private func recalculateBasketPickupTimeFromOrderTimeUI(orderTimeCell: BasketOrderTimeTableViewCell, dayChanged: Bool) {
        let daySelected = orderTimeCell.selectedDay()
        
        //Calculate the start and end time
        calculateStartAndEndTimesToUseOnUI(daySelected)
        
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
    
    private func calculateStartAndEndTimesToUseOnUI(day: PickupDay) {
        switch day {
        case .ASAP:
            startDateTimeUsed = nil
            endDateTimeUsed = nil
            pickupTimeUsed = nil
            return
        case .Today:
            startDateTimeUsed = restaurantStartEndToday?.start
            endDateTimeUsed = restaurantStartEndToday?.end
        case .Tomorrow:
            startDateTimeUsed = restaurantStartEndTomorrow?.start
            endDateTimeUsed = restaurantStartEndTomorrow?.end
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
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        if endDateTimeUsed!.timeIntervalSince1970 > startDateTimeUsed!.timeIntervalSince1970 && !calendar.isDate(endDateTimeUsed!, inSameDayAsDate: startDateTimeUsed!) {
            let comps = NSDateComponents()
            comps.day = startDateTimeUsed!.dayOfMonthInGregorianCalendar()
            comps.month = startDateTimeUsed!.monthOfYearInGregorianCalendar()
            comps.year = startDateTimeUsed!.yearInGregorianCalendar()
            comps.hour = 23
            comps.minute = 45
            let newEndDateTimeUsed = calendar.dateFromComponents(comps)!
            //In case start time after 23:45. We can't order on the day as 23:45 is last
            //order slot for the day. So the store closed or order not possible will appear
            endDateTimeUsed = newEndDateTimeUsed
        }
        
        // Adjust for Earliest ReadyTime and curren date-time
        let earliestReadyDateTime = basket.earliestReadyTime.dateFromOloDateTimeString()
        let currentDateTime = NSDate()
        
        // Check if earliest ready time is after the startTime. Then we need to make earliest ready time the start time of UI.
        // As order can not be ready before that.
        if earliestReadyDateTime!.timeIntervalSince1970 > startDateTimeUsed!.timeIntervalSince1970 {
            startDateTimeUsed = earliestReadyDateTime
        }
        
        // Check if start time is in past. i.e before current time + 15 min in future. Then need to use current time + 15 min as the start time of UI
        // As order can only be 15min future.
        if startDateTimeUsed!.timeIntervalSince1970 < (currentDateTime.timeIntervalSince1970 + BasketViewController.interval) {
            startDateTimeUsed = NSDate(timeInterval: BasketViewController.interval, sinceDate: currentDateTime)
        }
        
        // Adjust the startTime so it is at 15 mins interval
        // Time Since epoch
        var secondsSinceEpoch = floor(startDateTimeUsed!.timeIntervalSince1970)
        // next fifteen min multiple
        secondsSinceEpoch +=  BasketViewController.interval - (secondsSinceEpoch % BasketViewController.interval)
        startDateTimeUsed = NSDate(timeIntervalSince1970: secondsSinceEpoch)
        
        // Ensure that we have not exceeded the store ending hours. If so, we can not order on the selected day.
        if startDateTimeUsed!.timeIntervalSince1970 > endDateTimeUsed!.timeIntervalSince1970 {
            startDateTimeUsed = nil
            endDateTimeUsed = nil
            storeOpenStatusText = "Store closed or not accepting orders"
        }
    }
    
    private func setupOrderTimeCellSliderMaxValue(orderTimeCell: BasketOrderTimeTableViewCell) {
        if startDateTimeUsed != nil && endDateTimeUsed != nil {
            orderTimeCell.slider.maximumValue = Float(endDateTimeUsed!.timeIntervalSinceReferenceDate - startDateTimeUsed!.timeIntervalSinceReferenceDate)
        } else {
            orderTimeCell.slider.maximumValue = 0
        }
    }
    
    private func adjustPickupTimeUsedForDayChangeAndSetSliderValues(daySelected: PickupDay) {
        if startDateTimeUsed == nil || endDateTimeUsed == nil || daySelected == .ASAP {
            pickupTimeUsed = nil
            basket.selectedPickupTime = nil
            return
        }
        
        let dateForDay = (daySelected == .Tomorrow) ? NSDate(timeIntervalSinceNow: 24 * 60 * 60) : NSDate()
        
        let newSelectedTimeComps = NSDateComponents()
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
        
        let calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
        let newSelectedPickupTime = calendar.dateFromComponents(newSelectedTimeComps)
        pickupTimeUsed = newSelectedPickupTime!
        
        // This is user interaction. If user had picked time we convert it to next day.
        // If user has not be select the earliest time of the day.
        basket.selectedPickupTime = newSelectedPickupTime!
    }
    
    private func snapSliderValueToNearest15MinInterval(orderTimeCell: BasketOrderTimeTableViewCell) {
        //Get the rounded value of slider so its int and then make NSTimeInterval for easy calculations
        let value = Double(orderTimeCell.slider.value)
        //Once int, find the nearest 15-min unit.
        let fifteenMinUnits = round(value / BasketViewController.interval)
        //New Value is fifteenMinUnits * 15
        let newValue: NSTimeInterval = fifteenMinUnits * BasketViewController.interval
        orderTimeCell.slider.setValue(Float(newValue), animated: false)
    }
    
    private func snapPickupTimeUsedToNext15MinInterval() {
        if pickupTimeUsed == nil {
            return
        }
        
        // Round time to nearest second
        let intervalSinceEpoch = round(pickupTimeUsed!.timeIntervalSince1970)
        // Once in seconds, find the nearest 15-min unit. Ceil So time is always in future.
        let fifteenMinUnits = ceil(intervalSinceEpoch / BasketViewController.interval)
        // New Value is fifteenMinUnits * 15
        let newValue: NSTimeInterval = fifteenMinUnits * BasketViewController.interval
        pickupTimeUsed = NSDate(timeIntervalSince1970: newValue)
    }
    
    private func setupBasketPickupTimeFromOrderTimeCellSliderValue(orderTimeCell: BasketOrderTimeTableViewCell) {
        if startDateTimeUsed != nil && endDateTimeUsed != nil {
            // Althoug no need to round here, but just in case.
            let sliderValue = NSTimeInterval(round(orderTimeCell.slider.value))
            // Selected Time as epoch
            let timeIntervalSince1970 = startDateTimeUsed!.timeIntervalSince1970 + sliderValue
            let seletedPickupTime = NSDate(timeIntervalSince1970: timeIntervalSince1970)
            // Only update basket time on user interaction.
            basket.selectedPickupTime = seletedPickupTime
            pickupTimeUsed = seletedPickupTime
        }
    }
    
    private func adjustPickupTimeUsedAccordingToStartAndEndTime() {
        if startDateTimeUsed != nil && endDateTimeUsed != nil && pickupTimeUsed != nil {
            let startIntervalSinceEpoch = startDateTimeUsed!.timeIntervalSince1970
            let endIntervalSinceEpoch = endDateTimeUsed!.timeIntervalSince1970
            let pickupTimeIntervalSinceEpoch = pickupTimeUsed!.timeIntervalSince1970
            // Ensure pickup time is in start-end range
            pickupTimeUsed = NSDate(timeIntervalSince1970: min(endIntervalSinceEpoch, max(startIntervalSinceEpoch, pickupTimeIntervalSinceEpoch)))
        }
    }
    
    private func setupOrderTimeCellSliderValueFromPickupTimeUsed(orderTimeCell: BasketOrderTimeTableViewCell) {
        if startDateTimeUsed != nil && endDateTimeUsed != nil && pickupTimeUsed != nil {
            let startIntervalSinceEpoch = startDateTimeUsed!.timeIntervalSince1970
            let pickupTimeIntervalSinceEpoch = pickupTimeUsed!.timeIntervalSince1970
            let sliderValue = pickupTimeIntervalSinceEpoch - startIntervalSinceEpoch
            orderTimeCell.slider.value = Float(sliderValue)
        } else {
            orderTimeCell.slider.value = 0
        }
    }
    
    private func populateOrderTimeCellLabelsAndButons(orderTimeCell: BasketOrderTimeTableViewCell, day: PickupDay) {
        orderTimeCell.selectDay(day)
        orderTimeCell.startTimeLabel.text = startDateTimeUsed?.timeString()
        orderTimeCell.endTimeLabel.text = endDateTimeUsed?.timeString()
        orderTimeCell.selectedTimeLabel.text = pickupTimeUsed?.timeString() ?? storeOpenStatusText
    }
    
    private func validatePickUpTime() -> Bool {
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
        let currentDateTime = NSDate()
        if currentDateTime.timeIntervalSince1970 > timeWantedDateTime?.timeIntervalSince1970 {
            presentOkAlert("Pickup time", message: "Please select a time in the future.")
            return false
        }
        
        return true
    }
    
    private func setupTimeWantedFromBasketPickupTime() {
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
    
    func swipeTableCell(cell: MGSwipeTableCell!, tappedButtonAtIndex index: Int, direction: MGSwipeDirection, fromExpansion: Bool) -> Bool {
        if let indexPath = tableView.indexPathForCell(cell) {
            if indexPath.section == 0 && basket.products.count > 0 {
                
                productName =  basket.products[index].name
                productID =   basket.products[index].id
                isAppEvent = true
                
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
    @IBAction func expandCollapseDescription(sender: UIButton) {
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
    
    
    // Proceed order
    func proceedOrder(){
        self.trackButtonPress(checkoutButton)
        
        TotalModifierCost =  self.getCustomiseItCostInBasket()
        
        //Set time and validate from server
        SVProgressHUD.showWithStatus("Updating basket...", maskType: .Clear) // Avoid two messages here
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
                        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                        {
                            clpAnalyticsService.sharedInstance.clpTrackScreenView("CHECKOUT");
                            clpAnalyticsService.sharedInstance.clpTrackScreenView("PICK_UP_TIME");
                        }
                        
                        if count > 0 {
                            //Segue Identifier: "SelectPaymentMethod"
                            self.performSegueWithIdentifier("SelectPaymentMethod", sender: self)
                            return
                        }
                    }
                    //Segue Identifier2: "EnterCreditCard"
                    self.performSegueWithIdentifier("EnterCreditCard", sender: self)
                }
                return
            }
            
            //User is not logged in, navigate to enter user info and credit card
            SVProgressHUD.dismiss()
            self.performSegueWithIdentifier("EnterGuestUserInfo", sender: self)
        }
    }
    
}
