
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

enum StoreAlertType:Int{
    case kFavouriteStoreEmpty = 0
    case kFavouriteStoreNotOrderAhead = 1
}

private var instance: SignedInMainViewController?

class SignedInMainViewController: UIViewController {
    
    @IBOutlet weak var logoImageView: UIImageView!
    @IBOutlet weak var topMarginWithLogoConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var profileImageView: UIButton!
    @IBOutlet weak var welcomeLabel: UILabel!
    
    @IBOutlet weak var lblNoRewards: UILabel!
    
    @IBOutlet weak var lblRecentorders: UILabel!
    @IBOutlet weak var profileView: UIView!
    
    @IBOutlet weak var ordersHeadingLabel: UILabel!
    @IBOutlet weak var ordersBadgeLabel: UILabel!
    @IBOutlet weak var orderProductNameLabel: UILabel!
    @IBOutlet weak var orderProductModifiersLabel: UILabel!
    
    
    @IBOutlet weak var rewardImgView: UIImageView!
    @IBOutlet weak var rewardImageView: UIImageView!
    @IBOutlet weak var storeName: UILabel!
    @IBOutlet weak var storeAddress: UILabel!
    @IBOutlet weak var rewardsTextView: UILabel!
    @IBOutlet weak var MaxRewards: UILabel!
    @IBOutlet weak var maxRewardsText: UILabel!
    
    @IBOutlet weak var scrlDashboard: UIScrollView!
    
    @IBOutlet weak var lblOfferText: UILabel!
    
    @IBOutlet weak var lblRewardsText: UILabel!
    @IBOutlet weak var lblOffers: UILabel!
    
    @IBOutlet weak var vwInAppoffer: UIView!
    
    @IBOutlet weak var vwRewards: UIView!
    //    @IBOutlet weak var menuSlogan: UILabel!
    
    //    @IBOutlet weak var welcomeTextRecentOrder1: UILabel!
    //    @IBOutlet weak var welcomeTextRecentOrder2: UILabel!
    //    @IBOutlet weak var arrow: UIImageView!
    //    @IBOutlet weak var recentOrder: UILabel!
    
    @IBOutlet weak var withoutRecentOrder: UIView!
    @IBOutlet weak var recentOrder: UIView!
    
    @IBOutlet weak var orderProductName: UILabel!
    @IBOutlet weak var orderProductDesc: UILabel!
    @IBOutlet weak var orderDesc: UILabel!
    @IBOutlet weak var orderPriceLabel: UILabel!
    
    //   @IBOutlet weak var orderProductName2: UILabel!
    //  @IBOutlet weak var orderProductDesc2: UILabel!
    //  @IBOutlet weak var orderPriceLabel2: UILabel!
    
    //  @IBOutlet weak var recentOrderViewConstraint: NSLayoutConstraint!;
    //  @IBOutlet weak var secondRecentProductView: UIView!;
    
    //for store name width constraint
    @IBOutlet weak var storeNameView: UIView!
    // @IBOutlet weak var storeNameWidth: NSLayoutConstraint!;
    // @IBOutlet weak var storeAddressWidth: NSLayoutConstraint!;
    
    private var rewardSummary: RewardSummary?
    private var offerSummary: ClpOfferSummary?
    
    var storeAlertType:StoreAlertType = .kFavouriteStoreEmpty
    
    
    class func sharedInstance() -> SignedInMainViewController {
        
        
        if instance == nil {
            instance = SignedInMainViewController() // Should never be called
        }
        return instance!
    }
    
    
    func pushToOfferScreen()
    {
        
        performSegueWithIdentifier("RewardsOffers", sender: self)
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        instance = self
        scrlDashboard.bounces = true
        
        let defaults=NSUserDefaults.standardUserDefaults();
        if defaults.boolForKey("isInAPPOffer") == true
        {
            
            vwInAppoffer.hidden = false
            vwRewards.hidden = true
        }
        
        else
        {
            vwInAppoffer.hidden = true
            vwRewards.hidden = false
        }

        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.updateStuff), name: "appDidBecomeActive", object: nil)
        
        
        
        //        let appdelgate = UIApplication.sharedApplication().delegate as! AppDelegate
        //        self.view .addSubview(appdelgate.aiv)
        
        //        self.view?.bringSubviewToFront(appdelgate.window!)
        
        offerSummary = ClpOfferSummary(json: nil)
        
        rewardImageView.layer.shadowColor = UIColor.blackColor().CGColor
        rewardImageView.layer.shadowOffset = CGSizeMake(0, 1)
        rewardImageView.layer.shadowOpacity = 0.3
        rewardImageView.layer.shadowRadius = 1.0
        rewardImageView.clipsToBounds = false
        
        rewardImgView.layer.shadowColor = UIColor.blackColor().CGColor
        rewardImgView.layer.shadowOffset = CGSizeMake(0, 1)
        rewardImgView.layer.shadowOpacity = 0.3
        rewardImgView.layer.shadowRadius = 1.0
        rewardImgView.clipsToBounds = false
        
        
        
        profileView.layer.shadowColor = UIColor.blackColor().CGColor
        profileView.layer.shadowOffset = CGSizeMake(0, 1)
        profileView.layer.shadowOpacity = 0.3
        profileView.layer.shadowRadius = 2.0
        profileView.clipsToBounds = false
        
        lblNoRewards.layer.cornerRadius = 5.0;
        lblNoRewards.clipsToBounds = true
        
        // Attach basket controller to main window
        BasketFlagViewController.sharedInstance.attachToMainWindow()
        StatusBarStyleManager.pushStyle(.Default, viewController: self)
        
        profileView.layer.borderColor = UIColor( red: 255.0/255.0, green: 165.0/255.0, blue:0.0/255.0, alpha: 1.0 ).CGColor
        
        profileView.layer.shadowColor = UIColor.blackColor().CGColor
        
        // Call Current Store changed
        currentStoreChanged();
        
        // Listen for notifications on user profile
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.loggedInStateUpdated(_:)), name: JambaNotification.LoggedInStateChanged.rawValue, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.userNameUpdated(_:)), name: JambaNotification.UserFirstNameChanged.rawValue, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.userProfileImageUpdated(_:)), name: JambaNotification.UserProfileImageChanged.rawValue, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.orderPlaced(_:)), name: JambaNotification.OrderPlaced.rawValue, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.orderStateChanged(_:)), name: JambaNotification.SharedBasketUpdated.rawValue, object: nil)
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.currentStoreChanged), name: JambaNotification.CurrentStoreChanged.rawValue, object: nil)
        
        // Start reorder notification when change store from basket and basket store detail screen
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.startReorder), name: JambaNotification.ReStartOrderForUser.rawValue, object: nil)
        
        // Basket transferred notification when change store from basket and store detail screen
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(SignedInMainViewController.basketTransferred), name: JambaNotification.BasketTransferredForUser.rawValue, object: nil)
        
        
        // Add swipe up gesture recognizer to all subviews with user interaction enabled
        addRecognizer(view)
        view.walkSubViews { view in
            if view.userInteractionEnabled {
                self.addRecognizer(view)
            }
        }
        updateScreen()
        
    }
    
    
    func updateStuff()
    {
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        
        print("push reveive updateStuff");

        
        if appdelegate?.isPushOpen == true
        {
            self.startOrderButtonClicked()
            print("push reveive updateStuff isPushOpen");

            
        }
    }
    
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        updateStuff()
        self.getoffers()

        
    }
    
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        view.layoutIfNeeded()
        
        
        
        updateScreen()
        
        
        // Progress bar needs to be adjusted after screen has been presented (for autolayout)
        // Load data only after progress bar has been updated
        //This is also needed for periodic fetch when the Home Screen Appears again
        loadData()
        
    }
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    func swipeUp(sender: UIGestureRecognizer) {
        // HomeViewController.sharedInstance().openLastScreen()
    }
    
    func loggedInStateUpdated(notification: NSNotification) {
        updateScreen()//Update Everything in UI, if needed
        loadData()//Make call to fetch all needed data if needed
    }
    
    func userNameUpdated(notification: NSNotification) {
        updateUserName()
    }
    
    func userProfileImageUpdated(notification: NSNotification) {
        // updateUserProfileImage()
    }
    
    func orderStateChanged(notification: NSNotification) {
        //We just need to update recen order labels in case of no recent orders
        updateRecentOrders()
    }
    
    func orderPlaced(notification: NSNotification) {
        //While fetch is being made, we need to update the recent order labels incase
        //of 0 recent orders
        updateRecentOrders()
        //Fetch and update all data
        loadData()
    }
    
    // MARK: Navigation
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "RewardsOffers" {
            trackButtonPressWithName("Rewards")
            let nc = segue.destinationViewController as! UINavigationController
            let vc = nc.viewControllers.first as! MyRewardsTableViewController
            vc.rewardSummary = rewardSummary
            vc.OfferSummary = self.offerSummary
            vc.isOffers = false
            
            
        }
        else if segue.identifier == "OffersSegue"
        {
            trackButtonPressWithName("Offers")
            let nc = segue.destinationViewController as! UINavigationController
            let vc = nc.viewControllers.first as! MyRewardsTableViewController
            vc.rewardSummary = rewardSummary
            vc.OfferSummary = self.offerSummary
            vc.isOffers = true
            
        }
        else if segue.identifier == "OrderHistorySegue" {
            trackButtonPressWithName("Order History")
        }
        else if segue.identifier == "UserProfileSegue" {
            trackButtonPressWithName("User Profile")
        }
        else if segue.identifier == "StoreLocatorSegue"{
            let nc = segue.destinationViewController as! UINavigationController
            let storeLocatorViewController = nc.viewControllers[0] as! StoreLocatorViewController
            storeLocatorViewController.storeSearchTypeOrderAhead = true
            if storeAlertType == .kFavouriteStoreEmpty{
                storeLocatorViewController.alertType = .kFavouriteStoreEmpty
            }
            else{
                storeLocatorViewController.alertType = .kFavouriteStoreNotOrderAhead
            }
        }
    }
    
    override func shouldPerformSegueWithIdentifier(identifier: String?, sender: AnyObject?) -> Bool {
        if identifier == "OrderHistorySegue" {
            let recentOrderCount = UserService.sharedUser?.recentOrders?.count ?? 0
            if  recentOrderCount == 0 {
                if BasketService.sharedBasket != nil {
                    BasketFlagViewController.sharedInstance.openBasketVC()
                } else {
                    //  NonSignedInViewController.sharedInstance().startNewOrder()
                    // HomeViewController.sharedInstance().openOrderScreen()
                }
                return false
            }
        }
        
        return true
    }
    
    
    // MARK: Private helpers
    private func updateScreen() {
        if UserService.sharedUser == nil {
            // User logged out, probably
            return
        }
        updateUserName()
        updateUserProfileImage()
        updateRewards()
        updateRecentOrders()
    }
    
    func loadData() {
        if UserService.sharedUser == nil {
            // User logged out, probably
            return
        }
        loadRewards()
        loadOrderHistory()
    }
    
    
    private func updateUserName() {
        if UserService.sharedUser?.firstName != nil && UserService.sharedUser!.firstName!.isEmpty == false {
            welcomeLabel.text = UserService.sharedUser!.firstName!
        } else {
            welcomeLabel.text = "Welcome!"
        }
    }
    
    private func updateUserProfileImage() {
        
        profileImageView .setImage(UIImage(named: UserService.sharedUser!.profileImageName), forState: UIControlState.Normal)
        
    }
    
    private func loadRewards() {
        RewardService.loadRewards { (rewards, error) -> Void in
            if error != nil {
                log.warning("WARNING: \(error?.localizedDescription)")
                return
            }
            self.rewardSummary = rewards
            self.updateRewards()
        }
        
        //7143548
    }
    
    func getoffers()
    {
        //Newmemberid
        let defaults = NSUserDefaults.standardUserDefaults()
        if let customerId = defaults.stringForKey("Newmemberid")
        {
            print(customerId)
            ClpOfferService.getClpOffers(customerId){ (offers : ClpOfferSummary?, error) -> Void in
                if error != nil {
                    log.warning("WARNING: \(error?.localizedDescription)")
                    return
                }
                print("joe** response is here \(offers)")
                self.offerSummary = offers
                if let offerCount = self.offerSummary?.offerList.count
                {
                    if offerCount > 0
                    {
                        self.lblOffers.text = "You have \(offerCount) exciting offers"
                    }
                    else
                    {
                        self.lblOffers.text = "You have no offers available"
                        self.lblOfferText.text = "You do not have any offers for now and will soon be getting exciting offers"
                    }
                }
            }
            
        }
        
    }
    
    private func updateRewards() {
        log.verbose("Rewards: \(self.rewardSummary)")
        
        
        if let rewards = rewardSummary {
            log.verbose("pts \(rewards.points) pts");
            // rewardsTextView.text = ("\(rewards.rewardCount)/35")
            rewardsTextView.text=("\(rewards.points)")
            lblRewardsText.text = ("\(rewards.points)")
            MaxRewards.text = "/\(rewards.threshold)"
            maxRewardsText.text = "/\(rewards.threshold)"
            lblNoRewards.text = "\(rewards.rewardCount)"
            
            if (rewards.points == 0) {
                rewardImageView.image = UIImage(named: "empty-jamba-cup.png")
                rewardImgView.image = UIImage(named: "empty-jamba-cup.png")
            } else if (rewards.points < 5) {
                rewardImageView.image = UIImage(named: "1-jamba-cup.png")
                rewardImgView.image = UIImage(named: "1-jamba-cup.png")
                
            } else if (rewards.points < 10) {
                rewardImageView.image = UIImage(named: "5-jamba-cup.png")
                rewardImgView.image = UIImage(named: "5-jamba-cup.png")
                
            } else if (rewards.points < 15) {
                rewardImageView.image = UIImage(named: "10-jamba-cup.png")
                rewardImgView.image = UIImage(named: "10-jamba-cup.png")
                
            } else if (rewards.points < 20) {
                rewardImageView.image = UIImage(named: "15-jamba-cup.png")
                rewardImgView.image = UIImage(named: "15-jamba-cup.png")
                
            } else if (rewards.points < 25) {
                rewardImageView.image = UIImage(named: "20-jamba-cup.png")
                rewardImgView.image = UIImage(named: "20-jamba-cup.png")
                
            } else if (rewards.points < 30) {
                rewardImageView.image = UIImage(named: "25-jamba-cup.png")
                rewardImgView.image = UIImage(named: "25-jamba-cup.png")
                
            } else if (rewards.points < 35) {
                rewardImageView.image = UIImage(named: "30-jamba-cup.png")
                rewardImgView.image = UIImage(named: "30-jamba-cup.png")
                
            } else {
                rewardImageView.image = UIImage(named: "35-jamba-cup.png")
                rewardImgView.image = UIImage(named: "35-jamba-cup.png")
                
            }
        } else {
            rewardImageView.image = UIImage(named: "empty-jamba-cup.png")
            rewardImgView.image = UIImage(named: "empty-jamba-cup.png")
            
            rewardsTextView.text = ("0")
            lblRewardsText.text = "0"
        }
        
    }
    
    
    @IBAction func randomProgress() {
        //updateProgress(random() % 36, maxPoints: 35, animated: true)
    }
    
    private func loadOrderHistory() {
        UserService.recentOrders { (error) -> Void in
            if error != nil {
                log.warning("WARNING: \(error?.localizedDescription)")
                return
            }
            self.updateRecentOrders()
        }
    }
    
    private func updateRecentOrders() {
        if UserService.sharedUser == nil {//user is logged out. Just for safety.
            return
        }
        if (UserService.sharedUser!.recentOrders == nil || UserService.sharedUser?.recentOrders!.count==0)  {
            //            if (true) {
            //            welcomeTextRecentOrder1.hidden=false;
            //            welcomeTextRecentOrder2.hidden=false;
            //            arrow.hidden=false;
            //            recentOrder.hidden=true;
            //            orderProductName.hidden=true;
            //            orderProductDesc.hidden=true;
            //            orderPriceLabel.hidden=true;
            withoutRecentOrder.hidden=false;
            recentOrder.hidden=true;
        } else  {
            //            welcomeTextRecentOrder1.hidden=true;
            //            welcomeTextRecentOrder2.hidden=true;
            //            arrow.hidden=true;
            //            recentOrder.hidden=false;
            //            orderProductName.hidden=false;
            //            orderProductDesc.hidden=false;
            //            orderPriceLabel.hidden=false;
            withoutRecentOrder.hidden=true;
            recentOrder.hidden=false;
            
            //secondRecentProductView.hidden=true;
            //recentOrderViewConstraint.constant=120
            let orderDetails = UserService.sharedUser!.recentOrders![0];
            if let orders : String = String((UserService.sharedUser?.recentOrders!.count)!)
            {
                lblRecentorders.text = orders
            }
            orderProductName.text = orderDetails.commaSepratedNameOfProducts()
            if (orderDetails.timePlaced != nil) {
                orderProductDesc.text = "Ordered " + orderDetails.timePlaced!.currentDateIfDateInFuture().timeAgoWithDayAsMinUnit()
                orderDesc.text = "";
            } else {
                orderProductDesc.text = ""
                orderDesc.text = ""
                
            }
            let cost = String(format: "%.2f", orderDetails.total)
            orderPriceLabel.text = "$" + cost;
            /* if (UserService.sharedUser?.recentOrders?.count>1) {
             secondRecentProductView.hidden=false;
             recentOrderViewConstraint.constant=180
             let orderDetails = UserService.sharedUser!.recentOrders![1];
             orderProductName2.text = orderDetails.commaSepratedNameOfProducts()
             if (orderDetails.timePlaced != nil) {
             orderProductDesc2.text = "Ordered " + orderDetails.timePlaced!.currentDateIfDateInFuture().timeAgoWithDayAsMinUnit()
             } else {
             orderProductDesc.text = ""
             }
             let cost = String(format: "%.2f", orderDetails.total)
             orderPriceLabel2.text = "$" + cost;
             }*/
        }
    }
    
    private func addRecognizer(view: UIView) {
        let swipeUp = UISwipeGestureRecognizer(target: self, action: #selector(SignedInMainViewController.swipeUp(_:)))
        swipeUp.direction = .Up
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
                self.performSegueWithIdentifier("StoreLocatorSegue", sender: self)
            }
            else{
                if favoriteStore!.supportsOrderAhead == false {
                    // When favourite store not support order Ahead
                    storeAlertType = .kFavouriteStoreNotOrderAhead
                    self.performSegueWithIdentifier("StoreLocatorSegue", sender: self)
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
                goToMenuScreen()
            }
            else{
                // Start an order for the selected store. Get menu and create baskset
                startOrder()
            }
        }
    }
    
    func goToMenuScreen() {
        ProductService.recentlyOrderedProducts { (products, error) -> Void in
            UIApplication.inMainThread {
                var hasRecentOrderedProducts = true
                if error != nil || products.count == 0 {
                    hasRecentOrderedProducts = false
                }
                if (CurrentStoreService.sharedInstance.storeBasedFeatureProducts.count>0 || hasRecentOrderedProducts){
                    self.performSegueWithIdentifier("ProductMenuSegue", sender: self)
                }
                else{
                    self.performSegueWithIdentifier("FullMenuSegue", sender: self)
                }
            }
        }
    }
    
    /// Open Menu Landign screen, closing other menu screens if needed
    func openOrderScreen() {
        closeModalScreen() {
            self.performSegueWithIdentifier("FullMenuSegue", sender: self)
        }
    }
    
    func openProductMenuScreen() {
        closeModalScreen() {
            self.performSegueWithIdentifier("ProductMenuSegue", sender: self)
        }
    }
    /// Closes a main navigation screen, with completion callback
    func closeModalScreen(complete: (() -> Void)? = nil) {
        if presentedViewController == nil {
            complete?()
            return
        }
        
        dismissViewControllerAnimated(true) { () -> Void in
            complete?()
        }
    }
    
    
    func closeModalScreenToDashboard(complete: (() -> Void)? = nil) {
        if presentedViewController == nil {
            complete?()
            return
        }
        
        dismissViewControllerAnimated(false) { () -> Void in
            
            if let controller = self.presentingViewController
            {
                
                controller.modalTransitionStyle = .CrossDissolve
                self.presentingViewController!.view.alpha = 0.0
                UIView.animateWithDuration(0.5, animations: {() -> Void in
                    self.presentingViewController!.view.alpha = 1.0
                })
                
                complete?()
                
            }
        }
    }
    
    
    func closeAfterSignout() {
        self.dismissViewControllerAnimated(true, completion: {() -> Void in
            NonSignedInViewController.sharedInstance().closeModalScreen()
        })
    }
    
    //location of the store in the map
    @IBAction func getDirection(){
        if let store=CurrentStoreService.sharedInstance.currentStore{
            let location = CLLocation(latitude: store.latitude, longitude: store.longitude)
            MapsManager.openMapsWithDirectionsToLocation(location, name: store.name)
        }
    }
    
    @IBAction func showRecentOrder(){
        self.performSegueWithIdentifier("orderHistorySegue", sender: self)
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
        SVProgressHUD.showWithStatus("Starting new order...", maskType: .Clear)
        self.validateSupportOrderAhead { (error) in
            if error == nil{
                CurrentStoreService.sharedInstance.startNewOrder(CurrentStoreService.sharedInstance.currentStore!) { (status, error) in
                    SVProgressHUD.dismiss()
                    if((status == "Success") && (error == nil)){
                        self.goToMenuScreen()
                    }
                    else{
                        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
                        appdelegate!.isPushOpen = false
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
    func validateSupportOrderAhead(callback: (error:NSError?) -> ()){
        if CurrentStoreService.sharedInstance.currentStore!.supportsOrderAhead == false{
            if let currentStore = CurrentStoreService.sharedInstance.currentStore{
                CurrentStoreService.sharedInstance.oloRestauarantDetailsForStoreCode(currentStore, callback: { (store, error) in
                    if error != nil{
                        callback(error: error)
                        return
                    }
                    else{
                        CurrentStoreService.sharedInstance.resetStore(currentStore)
                        callback(error: nil)
                        return
                    }
                    
                })
            }
        }
        else{
            callback(error: nil)
            return
        }
    }
}
