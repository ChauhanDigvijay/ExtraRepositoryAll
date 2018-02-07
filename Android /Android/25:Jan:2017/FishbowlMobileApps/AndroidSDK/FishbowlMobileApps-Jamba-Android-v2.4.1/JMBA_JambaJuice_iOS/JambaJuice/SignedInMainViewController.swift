
//
//  SignedInMainViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 18/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import OloSDK
import CoreLocation
import Fabric
import Crashlytics
import QuartzCore
import SwiftyJSON
import Foundation



enum StoreAlertType:Int{
    case kFavouriteStoreEmpty = 0
    case kFavouriteStoreNotOrderAhead = 1
}

private var instance: SignedInMainViewController?

class SignedInMainViewController: UIViewController{
    @IBOutlet weak var profileImageView: UIButton!
    @IBOutlet weak var welcomeLabel: UILabel!
    @IBOutlet weak var profileView: UIView!
    @IBOutlet weak var rewardImageView: UIImageView!
    @IBOutlet weak var storeName: UILabel!
    @IBOutlet weak var storeAddress: UILabel!
    @IBOutlet weak var rewardsTextView: UILabel!
    @IBOutlet weak var MaxRewards: UILabel!
    @IBOutlet weak var giftCardCount: UILabel!
    @IBOutlet weak var giftCardCountBadge: UIImageView!
    @IBOutlet weak var scrlDashboard: UIScrollView!
    @IBOutlet weak var offersLoadingLabel: UILabel!
    @IBOutlet weak var vwInAppoffer: UIView!
    @IBOutlet weak var withoutRecentOrder: UIView!
    @IBOutlet weak var recentOrder: UIView!
    @IBOutlet weak var orderProductName: UILabel!
    @IBOutlet weak var orderProductDesc: UILabel!
    @IBOutlet weak var orderDesc: UILabel!
    @IBOutlet weak var orderPriceLabel: UILabel!
    @IBOutlet weak var offersBadgeCount: UILabel!
    @IBOutlet weak var offersImageView: UIImageView!
    @IBOutlet weak var offersCountView: UIView!
    
    fileprivate var rewardSummary: RewardSummary?
    fileprivate var offerSummary: FishbowlOfferSummary?
    
    // Reward completion
    var rewardAndOfferCount:String = ""
    
    var storeAlertType:StoreAlertType = .kFavouriteStoreEmpty
    // var launchScreen:Bool = false
    
    class func sharedInstance() -> SignedInMainViewController {
        if instance == nil {
            instance = SignedInMainViewController() // Should never be called
        }
        return instance!
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Call Fishbowl api calls and offfers and incomm gift card count
        giftCardAndOffersEventsCall()
        // Call for rewards and offers
        updateGiftCardCount()
        // Call Current Store changed
        currentStoreChanged()
        updateScreen()
        loadData()
        addStatusBarAndBasketFlag()
        addSwipeUpRecognizer()
        loadUI()
        initateNotifications()
    }
    
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        trackScreenView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        view.layoutIfNeeded()
        // Progress bar needs to be adjusted after screen has been presented (for autolayout)
        // Load data only after progress bar has been updated
        //This is also needed for periodic fetch when the Home Screen Appears again
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    @IBAction func openOrderHistory(_ sender: UITapGestureRecognizer) {
        performSegue(withIdentifier: "OrderHistorySegue", sender: nil)
    }
    
    
    func swipeUp(_ sender: UIGestureRecognizer) {
        // HomeViewController.sharedInstance().openLastScreen()
    }
    
    //    func loggedInStateUpdated(_ notification: Notification) {
    //        updateScreen()//Update Everything in UI, if needed
    //        loadData()//Make call to fetch all needed data if needed
    //    }
    
    func userNameUpdated(_ notification: Notification) {
        updateUserName()
    }
    
    func userProfileImageUpdated(_ notification: Notification) {
        updateUserProfileImage()
    }
    
    func orderStateChanged(_ notification: Notification) {
        //We just need to update recen order labels in case of no recent orders
        updateRecentOrders()
    }
    
    func orderPlaced(_ notification: Notification) {
        //While fetch is being made, we need to update the recent order labels incase
        //of 0 recent orders
        updateRecentOrders()
        //Fetch and update all data
        loadData()
    }
    
    // MARK: Navigation
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "OffersSegue"
        {
            trackButtonPressWithName("Offers")
            let nc = segue.destination as! UINavigationController
            let vc = nc.viewControllers.first as! MyRewardsTableViewController
            vc.rewardSummary = RewardService.sharedInstance.rewardSummary
            vc.OfferSummary = OfferService.sharedInstance.offersSummary
            vc.refreshOffer = false
            if sender != nil && sender! is Bool{
                if sender! as! Bool == true{
                    vc.refreshOffer = true
                }
            }
            
            NSLog("%@", sender.debugDescription)
        }
        else if segue.identifier == "OrderHistorySegue" {
            trackButtonPressWithName("Order History")
            if (sender != nil){
                let nc = segue.destination as! UINavigationController
                let vc = nc.viewControllers.first as! MyOrdersTableViewController
                vc.showOrderDetail()
            }
        }
        else if segue.identifier == "UserProfileSegue" {
            trackButtonPressWithName("User Profile")
        }
        else if segue.identifier == "StoreLocatorSegue"{
            let nc = segue.destination as! UINavigationController
            let storeLocatorViewController = nc.viewControllers[0] as! StoreLocatorViewController
            storeLocatorViewController.storeSearchTypeOrderAhead = true
            if let user = UserService.sharedUser {
                if let favStore = user.favoriteStore {
                    if !favStore.supportsOrderAhead {
                        storeLocatorViewController.storeSearchTypeOrderAhead = false
                    }
                }
            }
            if storeAlertType == .kFavouriteStoreEmpty{
                storeLocatorViewController.alertType = .kFavouriteStoreEmpty
            }
            else{
                storeLocatorViewController.alertType = .kFavouriteStoreNotOrderAhead
            }
        }
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String?, sender: Any?) -> Bool {
        if identifier == "OrderHistorySegue" {
            let recentOrderCount = UserService.sharedUser?.recentOrders?.count ?? 0
            if  recentOrderCount == 0 {
                if BasketService.sharedBasket != nil {
                    BasketFlagViewController.sharedInstance.openBasketVC()
                } else {
                }
                return false
            }
        }
        
        return true
    }
    
    
    // MARK: Private helpers
    fileprivate func updateScreen() {
        if UserService.sharedUser == nil {
            // User logged out, probably
            return
        }
        updateUserName()
        updateUserProfileImage()
        updateRecentOrders()
        
    }
    
    func loadData() {
        if UserService.sharedUser == nil {
            // User logged out, probably
            return
        }
        loadOrderHistory()
        loadRecentOrders()
    }
    
    
    fileprivate func updateUserName() {
        if UserService.sharedUser?.firstName != nil && UserService.sharedUser!.firstName!.isEmpty == false {
            welcomeLabel.text = UserService.sharedUser!.firstName!
        } else {
            welcomeLabel.text = "Welcome!"
        }
    }
    
    fileprivate func updateUserProfileImage() {
        profileImageView .setImage(UIImage(named: UserService.sharedUser!.profileImageName), for: UIControlState())
    }
    
    
    fileprivate func updateRewards() {
        log.verbose("Rewards: \(String(describing: self.rewardSummary))")
        if let rewards = rewardSummary {
            log.verbose("pts \(rewards.points) pts");
            rewardsTextView.text = String(format: "%d", rewards.points)
            rewardsTextView.isHidden = false
            MaxRewards.text = "/\(rewards.threshold)"
            
            if (rewards.points == 0) {
                rewardImageView.image = UIImage(named: "empty-jamba-cup.png")
            } else if (rewards.points < 5) {
                rewardImageView.image = UIImage(named: "1-jamba-cup.png")
            } else if (rewards.points < 10) {
                rewardImageView.image = UIImage(named: "5-jamba-cup.png")
            } else if (rewards.points < 15) {
                rewardImageView.image = UIImage(named: "10-jamba-cup.png")
            } else if (rewards.points < 20) {
                rewardImageView.image = UIImage(named: "15-jamba-cup.png")
            } else if (rewards.points < 25) {
                rewardImageView.image = UIImage(named: "20-jamba-cup.png")
            } else if (rewards.points < 30) {
                rewardImageView.image = UIImage(named: "25-jamba-cup.png")
            } else if (rewards.points < 35) {
                rewardImageView.image = UIImage(named: "30-jamba-cup.png")
            } else {
                rewardImageView.image = UIImage(named: "35-jamba-cup.png")
            }
        } else {
            rewardImageView.image = UIImage(named: "empty-jamba-cup.png")
            rewardsTextView.text = "0"
            rewardsTextView.isHidden = false
        }
        
    }
    
    fileprivate func loadOrderHistory() {
        UserService.recentOrders { (error) -> Void in
            if error != nil {
                log.warning("WARNING: \(String(describing: error?.localizedDescription))")
                return
            }
            self.updateRecentOrders()
        }
    }
    
    //load favourite Orders
    fileprivate func loadRecentOrders() {
        UserService.favouriteOrders { (error) in
            if error != nil {
                log.warning("WARNING: \(String(describing: error?.localizedDescription))")
                return
            }
        }
    }
    
    fileprivate func updateRecentOrders() {
        if UserService.sharedUser == nil {//user is logged out. Just for safety.
            return
        }
        if (UserService.sharedUser!.recentOrders == nil || UserService.sharedUser?.recentOrders!.count==0)  {
            withoutRecentOrder.isHidden=false;
            recentOrder.isHidden=true;
        } else  {
            withoutRecentOrder.isHidden=true;
            recentOrder.isHidden=false;
            let orderDetails = UserService.sharedUser!.recentOrders![0];
            orderProductName.text = orderDetails.truncateProductNames()
            if (orderDetails.timePlaced != nil) {
                
                let orderedAgo = orderDetails.timePlaced!.currentDateIfDateInFuture().timeAgoWithDayAsMinUnit()
                
                orderProductDesc.text = ("Ordered \(orderedAgo)")
                orderDesc.text = "";
            } else {
                orderProductDesc.text = ""
                orderDesc.text = ""
            }
            let cost = String(format: "%.2f", orderDetails.total)
            orderPriceLabel.text = "$" + cost;
        }
    }
    fileprivate func addRecognizer(_ view: UIView) {
        let swipeUp = UISwipeGestureRecognizer(target: self, action: #selector(SignedInMainViewController.swipeUp(_:)))
        swipeUp.direction = .up
        view.addGestureRecognizer(swipeUp)
    }
    @IBAction func startOrderButtonClicked(){
        // Track Event for order now button clicked
        trackButtonPressWithName("OrderNow")
        // Validate current store is nil or not
        if CurrentStoreService.sharedInstance.currentStore == nil {
            let favoriteStore = UserService.sharedUser?.favoriteStore
            if(favoriteStore == nil){
                storeAlertType = .kFavouriteStoreEmpty
                self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
            }
            else{
                if favoriteStore!.supportsOrderAhead == false {
                    // When favourite store not support order Ahead
                    storeAlertType = .kFavouriteStoreNotOrderAhead
                    self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
                }
                else{
                    CurrentStoreService.sharedInstance.resetStore(favoriteStore!)
                    startOrder()
                }
            }
        }
        else{
            // When current store is not nil
            // Validate store menu present or not
            if(CurrentStoreService.sharedInstance.currentStore!.storeMenu != nil){
                // Fishbowl Event
                FishbowlApiClassService.sharedInstance.submitMobileAppEvent("MENU_CLICK")
                goToMenuScreen()
            }
            else{
                // Start an order for the selected store. Get menu and create baskset
                startOrder()
            }
        }
    }
    
    func goToMenuScreen() {
        //if "recentproducts" (or) "feature products" (or) ads are available then show feature product screen otherwise show full screen
        ProductService.recentlyOrderedProductsAndAds { (products, adsList, error) in
            UIApplication.inMainThread {
                var hasRecentOrderedProducts = true
                if error != nil || (products.count == 0 && adsList.adsDetailList.count == 0) {
                    hasRecentOrderedProducts = false
                }
                if (CurrentStoreService.sharedInstance.storeBasedFeatureProducts.count>0 || hasRecentOrderedProducts){
                    self.performSegue(withIdentifier: "ProductMenuSegue", sender: self)
                }
                else{
                    self.performSegue(withIdentifier: "FullMenuSegue", sender: self)
                }
            }
        }
    }
    
    /// Open Menu Landign screen, closing other menu screens if needed
    func openOrderScreen() {
        closeModalScreen() {
            self.performSegue(withIdentifier: "FullMenuSegue", sender: self)
        }
    }
    
    func openProductMenuScreen() {
        closeModalScreen() {
            self.performSegue(withIdentifier: "ProductMenuSegue", sender: self)
        }
    }
    /// Closes a main navigation screen, with completion callback
    func closeModalScreen(_ complete: (() -> Void)? = nil) {
        if presentedViewController == nil {
            complete?()
            return
        }
        dismiss(animated: true) { () -> Void in
            complete?()
        }
    }
    
    
    func closeModalScreenToDashboard(_ complete: (() -> Void)? = nil) {
        if presentedViewController == nil {
            complete?()
            return
        }
        
        dismiss(animated: false) { () -> Void in
            if let controller = self.presentingViewController
            {
                controller.modalTransitionStyle = .crossDissolve
                self.presentingViewController!.view.alpha = 0.0
                UIView.animate(withDuration: 0.5, animations: {() -> Void in
                    self.presentingViewController!.view.alpha = 1.0
                })
                complete?()
            }
        }
    }
    
    
    //MARK: - Search product screen
    // Search a product based on current store
    @IBAction func searchProduct(){
        if CurrentStoreService.sharedInstance.currentStore == nil {
            let favoriteStore = UserService.sharedUser?.favoriteStore
            if(favoriteStore == nil){
                storeAlertType = .kFavouriteStoreEmpty
                self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
            }
            else{
                if favoriteStore!.supportsOrderAhead == false {
                    // When favourite store not support order Ahead
                    storeAlertType = .kFavouriteStoreNotOrderAhead
                    self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
                }
                else{
                    CurrentStoreService.sharedInstance.resetStore(favoriteStore!)
                    searchProductForCurrentMenu()
                }
            }
        }
        else if CurrentStoreService.sharedInstance.currentStore!.storeMenu == nil{
            searchProductForCurrentMenu()
        }
        else{
            self.performSegue(withIdentifier: "SearchProductSegue", sender: self)
        }
    }
    
    //MARK: - Search product for menu
    // Retrieve menu for current store when store menu is nil
    func searchProductForCurrentMenu(){
        SVProgressHUD.show(withStatus: "Retrieving Menu...")
        SVProgressHUD.setDefaultMaskType(.clear)
        self.validateSupportOrderAhead { (error) in
            if error == nil{
                CurrentStoreService.sharedInstance.startNewOrder(CurrentStoreService.sharedInstance.currentStore!) { (status, error) in
                    SVProgressHUD.dismiss()
                    if((status == "Success") && (error == nil)){
                        self.performSegue(withIdentifier: "SearchProductSegue", sender: self)
                    }
                    else{
                        self.presentOkAlert("Error", message:Constants.genericErrorMessage)
                    }
                }
            }
            else{
                self.presentOkAlert("Error", message: (error?.localizedDescription)!)
            }
        }
        
    }
    
    
    //location of the store in the map
    @IBAction func getDirection(){
        if let store=CurrentStoreService.sharedInstance.currentStore{
            let location = CLLocation(latitude: store.latitude, longitude: store.longitude)
            MapsManager.openMapsWithDirectionsToLocation(location, name: store.name)
        }
    }
    
    func showRecentOrder(showOrderDetail: Bool){
        performSegue(withIdentifier: "OrderHistorySegue", sender: showOrderDetail)
    }
    
    
    // Current Store Changed
    func currentStoreChanged(){
        if(CurrentStoreService.sharedInstance.currentStore == nil){
            storeName.text = "Store not saved?"
            storeAddress.text = "Tap to find your nearest store"
        }
        else{
            storeName.text=CurrentStoreService.sharedInstance.currentStore?.name
            storeAddress.text=CurrentStoreService.sharedInstance.currentStore?.address
        }
    }
    
    // Start order for the selected store
    func startOrder(){
        // Fishbowl Event
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("START_NEW_ORDER")
        SVProgressHUD.show(withStatus: "Starting new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        self.validateSupportOrderAhead { (error) in
            if error == nil{
                CurrentStoreService.sharedInstance.startNewOrder(CurrentStoreService.sharedInstance.currentStore!) { (status, error) in
                    SVProgressHUD.dismiss()
                    if((status == "Success") && (error == nil)){
                        self.goToMenuScreen()
                    }
                    else{
                        self.presentOkAlert("Error", message:Constants.genericErrorMessage)
                    }
                    
                }
            }
            else{
                self.presentOkAlert("Error", message: (error?.localizedDescription)!)
            }
        }
    }
    
    // Re-order
    func startReorder() {
        closeModalScreen() {
            self.goToMenuScreen()
        }
    }
    
    // Basket Successfully transferred
    func basketTransferred(){
        closeModalScreen()
    }
    
    // Validate current store support order ahead or not
    func validateSupportOrderAhead(_ callback: @escaping (_ error:NSError?) -> ()){
        if CurrentStoreService.sharedInstance.currentStore!.supportsOrderAhead == false{
            if let currentStore = CurrentStoreService.sharedInstance.currentStore{
                CurrentStoreService.sharedInstance.oloRestauarantDetailsForStoreCode(currentStore, callback: { (store, error) in
                    if error != nil{
                        callback(error)
                        return
                    }
                    else{
                        CurrentStoreService.sharedInstance.resetStore(currentStore)
                        callback(nil)
                        return
                    }
                    
                })
            }
        }
        else{
            callback(nil)
            return
        }
    }
    
    func getFilteredOfferCount(_ rewardSummary :RewardSummary) -> Int {
        let filteredRewards = rewardSummary.rewards.filter { $0.type == "offer" || $0.type == "reward" }
        return filteredRewards.count
    }
    
    
    //MARK: Refresh gift card count API call will not happen
    func updateGiftCardCount(){
        if let inCommUserGiftCards = GiftCardCreationService.sharedInstance.inCommUserGiftCards{
            giftCardCount.text = String(inCommUserGiftCards.count)
            showHideGiftCardCount(cardCount: inCommUserGiftCards.count)
        }else{
            showHideGiftCardCount(cardCount: 0)
        }
        
    }
    
    //MARK: Load inComm user gift cards API call will happen
    func loadInCommUserGiftCards(){
        SVProgressHUD.show(withStatus: "Please wait...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService
            .sharedInstance.getUserGiftCardsCount{ (userGiftCards) in
                SVProgressHUD.dismiss()
                UIApplication.inMainThread {
                    if let inCommUserGiftCards = GiftCardCreationService.sharedInstance.inCommUserGiftCards{
                        self.giftCardCount.text = String(inCommUserGiftCards.count)
                        self.showHideGiftCardCount(cardCount: inCommUserGiftCards.count)
                    }
                }
        }
    }
    
    //MARK: show/Hide gift card count label based on gift card count
    func showHideGiftCardCount(cardCount:Int) {
        if (cardCount > 0) {
            giftCardCountBadge.isHidden = false
            giftCardCount.isHidden = false
        } else {
            giftCardCountBadge.isHidden = true
            giftCardCount.isHidden = true
        }
    }
    
    
    //MARK: Navigate to gift card screen
    @IBAction func showGiftCardScreen(){
        // Validate user
        guard let user = UserService.sharedUser else{
            return
        }
        
        var message = ""
        // Validate user first name and last name
        if (user.firstName ==  nil || (user.firstName?.isEmpty)!) && (user.lastName == nil || (user.lastName?.isEmpty)!){
            message = "Please take a moment to update your profile information (first and last name) before purchasing a Jamba Card."
            self.presentProfileInformationAlert(message: message)
            
        } else if user.firstName == nil || user.firstName!.isEmpty{
            message = "Please take a moment to update your profile information (first name) before purchasing a Jamba Card."
            self.presentProfileInformationAlert(message: message)
            
        } else if user.lastName == nil || user.lastName!.isEmpty{
            message = "Please take a moment to update your profile information (last name) before purchasing a Jamba Card."
            self.presentProfileInformationAlert(message: message)
        } else {
            if InCommGiftCardBrandDetails.sharedInstance.brand == nil{
                SVProgressHUD.show(withStatus: "Please wait...")
                SVProgressHUD.setDefaultMaskType(.clear)
                FishbowlApiClassService.sharedInstance.validateFishbowlCredentials{ (status) in
                    if !status{
                        SVProgressHUD.dismiss()
                        return
                    }else{
                        InCommGiftCardBrandDetails.sharedInstance.loadBrandDetails { (brand, error) in
                            SVProgressHUD.dismiss()
                            if error != nil{
                                if error!.code == -1009 && error!.localizedDescription == "The Internet connection appears to be offline."{
                                    self.presentError(error)
                                }
                                return
                            }else{
                                self.showGiftCardScreenToUser()
                            }
                        }
                    }
                }
            }else{
                self.showGiftCardScreenToUser()
            }
        }
    }
    
    //MARK: Profile Information Alert
    func presentProfileInformationAlert(message:String){
        self.presentConfirmation("Attention", message: message, buttonTitle: "Update Profile") { (confirmed) in
            if confirmed{
                self.performSegue(withIdentifier: "Setting", sender: self)
            }
        }
    }
    
    
    // Refresh rewards and offers
    func rewardsAndOffers(){
        offersLoadingLabel.text = "Rewards & Offers"
        if OfferService.sharedInstance.offersSummary != nil {
            self.offerSummary = OfferService.sharedInstance.offersSummary
            let offers = OfferService.sharedInstance.offersSummary?.offerList ?? []
            
            if offers.count > 0
            {
                var totalCount = 0
                var rewardCount = "0"
                totalCount = offers.count
                rewardCount = String(format: "%d", offers.count)
                
                if let rewards = rewardSummary {
                    let filteredCount = self.getFilteredOfferCount(rewards)
                    if filteredCount > 0 {
                        rewardCount = String(format: "%d", filteredCount + totalCount )
                    }
                }
                //new screen changes
                self.offersBadgeCount.text = rewardCount
                self.offersImageView.isHidden = false
                self.offersCountView.isHidden = false
                //   NotificationCenter.default.post(name: Notification.Name(JambaNotification.ReloadRewardsAndOfferList.rawValue), object: nil)
                return
            }
        }
        
        var rewardCount = "0"
        if let rewards = rewardSummary {
            let filteredCount = self.getFilteredOfferCount(rewards)
            if filteredCount > 0 {
                rewardCount = String(format: "%d", filteredCount )
            }
        }
        self.offersBadgeCount.text = rewardCount
        self.offersImageView.isHidden = false
        self.offersCountView.isHidden = false
    }
    
    
    // Update rewards and offers
    func updateRewardsAndOffers(){
        self.rewardSummary = RewardService.sharedInstance.rewardSummary
        self.offerSummary = OfferService.sharedInstance.offersSummary
        self.updateRewards()
        self.rewardsAndOffers()
    }
    
    
    // Pushnotification
    func openRewardandOffers() {
        let appdelegate = UIApplication.shared.delegate as! AppDelegate
        let topViewController = self.getVisibleViewControllerFrom(appdelegate.window!.rootViewController)
        appdelegate.currentShortCutItem = JambaNotification.None
        if(topViewController is MyRewardsTableViewController){
            // Trigger notification center when view will appear
            NotificationCenter.default.post(name: Notification.Name(JambaNotification.RefreshMyRewardViewWillAppear.rawValue), object: nil)
            return
        }
        else{
            closeModalScreen({
                self.performSegue(withIdentifier: "OffersSegue", sender: true)
            })
        }
    }
    
    // ShortCutItme Methods
    func openStoreLoctionForShortCutItem(){
        let appdelegate = (UIApplication.shared.delegate) as! AppDelegate
        appdelegate.currentShortCutItem = JambaNotification.None
        closeModalScreen(){
            self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
        }
    }
    
    func openOrderMenuForShortCutItem(){
        let appdelegate = (UIApplication.shared.delegate) as! AppDelegate
        appdelegate.currentShortCutItem = JambaNotification.None
        closeModalScreen(){
            self.startOrderButtonClicked()
        }
    }
    func onLineOfferOrder(){
        closeModalScreen() {
            if BasketService.sharedBasket == nil || BasketService.sharedBasket?.id == "" {
                self.startOrderButtonClicked()
            } else {
                self.goToMenuScreen()
            }
        }
    }
    func getVisibleViewControllerFrom(_ vc: UIViewController?) -> UIViewController? {
        if let nc = vc as? UINavigationController {
            return self.getVisibleViewControllerFrom(nc.visibleViewController)
        } else {
            if let pvc = vc?.presentedViewController {
                return self.getVisibleViewControllerFrom(pvc)
            } else {
                return vc
            }
        }
    }
    
    func deinitNotificationCenter() {
        NotificationCenter.default.removeObserver(self)
    }
    
    func fbAndSpendGoRewardsAndOffers(){
        UIApplication.inMainThread {
            self.offersLoadingLabel.text = "Loading ..."
        }
        let group = DispatchGroup()
        group.enter()
        RewardService.loadRewards { (rewards, error) -> Void in
            if error != nil {
                group.leave()
                return
            }
            RewardService.sharedInstance.updateRewardSummary(rewards)
            group.leave()
        }
        group.enter()
        // Fishbowl Credential validation
        FishbowlApiClassService.sharedInstance.validateFishbowlCredentials { (status) in
            if !status{
                group.leave()
                return
            }else{
                FishbowlApiClassService.sharedInstance.getFishbowlOffer(callback: { (response, error) in
                    if error != nil{
                        group.leave()
                        return
                    }
                    let offers = FishbowlOfferSummary(json: JSON(response!))
                    OfferService.sharedInstance.updateOffers(offers);
                    group.leave()
                })
            }
        }
        group.notify(queue: .main) {
            UIApplication.inMainThread {
                self.updateRewardsAndOffers()
            }
        }
    }
    
    func loadUI(){
        instance = self
        scrlDashboard.bounces = true
        vwInAppoffer.isHidden = false
        self.offersBadgeCount.text = "0"
        self.offersImageView.isHidden = false
        self.offersCountView.isHidden = false
        
        // Reward points
        rewardImageView.image = UIImage(named: "empty-jamba-cup.png")
        rewardsTextView.text = "0"
        rewardsTextView.isHidden = false
        
        rewardImageView.layer.shadowColor = UIColor.black.cgColor
        rewardImageView.layer.shadowOffset = CGSize(width: 0, height: 1)
        rewardImageView.layer.shadowOpacity = 0.3
        rewardImageView.layer.shadowRadius = 1.0
        rewardImageView.clipsToBounds = false
        
        profileView.layer.shadowColor = UIColor.black.cgColor
        profileView.layer.shadowOffset = CGSize(width: 0, height: 1)
        profileView.layer.shadowOpacity = 0.3
        profileView.layer.shadowRadius = 2.0
        profileView.clipsToBounds = false
        profileView.layer.borderColor = UIColor( red: 255.0/255.0, green: 165.0/255.0, blue:0.0/255.0, alpha: 1.0 ).cgColor
    }
    
    func initateNotifications(){
        
        // Listen for notifications on user profile
        // NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.loggedInStateUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.LoggedInStateChanged.rawValue), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.userNameUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.UserFirstNameChanged.rawValue), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.userProfileImageUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.UserProfileImageChanged.rawValue), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.orderPlaced(_:)), name: NSNotification.Name(rawValue: JambaNotification.OrderPlaced.rawValue), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.orderStateChanged(_:)), name: NSNotification.Name(rawValue: JambaNotification.SharedBasketUpdated.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.currentStoreChanged), name: NSNotification.Name(rawValue: JambaNotification.CurrentStoreChanged.rawValue), object: nil)
        
        // Start reorder notification when change store from basket and basket store detail screen
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.startReorder), name: NSNotification.Name(rawValue: JambaNotification.ReStartOrderForUser.rawValue), object: nil)
        
        // Basket transferred notification when change store from basket and store detail screen
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.basketTransferred), name: NSNotification.Name(rawValue: JambaNotification.BasketTransferredForUser.rawValue), object: nil)
        
        // Refresh rewards and offers
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.updateRewardsAndOffers), name: NSNotification.Name(rawValue: JambaNotification.RefreshRewardsAndOffers.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.openRewardandOffers), name: NSNotification.Name(rawValue: JambaNotification.OpenRewardsAndOfferDetail.rawValue), object: nil)
        
        
        // Shorcut iteme notification
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.openOrderMenuForShortCutItem), name: NSNotification.Name(rawValue: JambaNotification.OrderMenuWhenSignedIn.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.openStoreLoctionForShortCutItem), name: NSNotification.Name(rawValue: JambaNotification.LocationSearchWhenSignedIn.rawValue), object: nil)
        
        // Refresh user gift card count
        NotificationCenter.default.addObserver(self, selector: #selector(SignedInMainViewController.updateGiftCardCount), name: NSNotification.Name(rawValue: JambaNotification.JambaGiftCardCountRefresh.rawValue), object: nil)
        
        // Speciffic for shortcut item
        let appdelegate = (UIApplication.shared.delegate) as! AppDelegate
        if appdelegate.currentShortCutItem == JambaNotification.OrderMenuWhenSignedIn {
            openOrderMenuForShortCutItem()
        }else if appdelegate.currentShortCutItem == JambaNotification.LocationSearchWhenSignedIn{
            openStoreLoctionForShortCutItem()
        }else if appdelegate.currentShortCutItem == JambaNotification.OpenRewardsAndOfferDetail{
            openRewardandOffers()
        }
    }
    
    func addSwipeUpRecognizer(){
        // Add swipe up gesture recognizer to all subviews with user interaction enabled
        addRecognizer(view)
        view.walkSubViews { view in
            if view.isUserInteractionEnabled {
                self.addRecognizer(view)
            }
        }
    }
    
    func addStatusBarAndBasketFlag(){
        // Attach basket controller to main window
        BasketFlagViewController.sharedInstance.attachToMainWindow()
        StatusBarStyleManager.pushStyle(.default, viewController: self)
    }
    
    // Show gift card or not
    func showGiftCardScreenToUser(){
        let storyBoard: UIStoryboard  = UIStoryboard(name: "GiftCardMain", bundle: nil)
        let vc =  storyBoard.instantiateViewController(withIdentifier: "JambaCardsViewController") as! JambaCardsViewController
        let nv: UINavigationController = UINavigationController.init(rootViewController: vc)
        nv.isNavigationBarHidden = true
        self.present(nv, animated: true, completion: nil)
    }
    func giftCardAndOffersEventsCall(){
        SVProgressHUD.show(withStatus: "Please wait...")
        SVProgressHUD.setDefaultMaskType(.clear)
        FishbowlApiClassService.sharedInstance.fishbowlLogin {
            //SVProgressHUD.dismiss()
            self.fbAndSpendGoRewardsAndOffers()
            self.loadInCommUserGiftCards()
            FishbowlApiClassService.sharedInstance.getAllStores()
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("APP_OPEN")
            if UserDefaults.standard.string(forKey: "pushToken") == nil{
                FishbowlApiClassService.sharedInstance.submitMobileAppEvent("LOGIN")
                UIApplication.inMainThread {
                    let appDelegate = UIApplication.shared.delegate as! AppDelegate
                    appDelegate.registerPushNotification()
                }
            }
        }
    }
}
