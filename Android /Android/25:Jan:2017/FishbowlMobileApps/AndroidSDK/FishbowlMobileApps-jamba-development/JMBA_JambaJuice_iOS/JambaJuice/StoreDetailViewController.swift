//
//  StoreDetailViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/30/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import OloSDK

class StoreDetailViewController: UIViewController {
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var startOrderButton: UIButton!
    @IBOutlet weak var startOrderButtonHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var preferredStoreButton: UIButton!
    @IBOutlet weak var preferredStoreHeightConstraint: NSLayoutConstraint!
    
    var store: Store!
    
    // Validate store search type for order ahead support store or not
    var storeSearchTypeOrderAhead:Bool = false
    
    // Validate basket transfer request or not
    var basketTransfer:Bool = false
    
    // Validate change preferred store screen
    var changePreferredStoreProfileScreen:Bool = false
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        titleLabel.text = store.name
        
        if UserService.sharedUser == nil || UserService.sharedUser!.favoriteStore == store {
            preferredStoreButton.hidden = true
            preferredStoreHeightConstraint.constant = 0
        }
        
        // Store button hidden when store does not support order ahead
        if store.supportsOrderAhead == false {
            startOrderButton.hidden = true
            startOrderButtonHeightConstraint.constant = 0
        }
        
        // Store search type OrderAhead for select store request
        if(storeSearchTypeOrderAhead){
            startOrderButton.setTitle("Select Store", forState: UIControlState.Normal)
            preferredStoreButton.hidden = true
            preferredStoreHeightConstraint.constant = 0
        }
        
        // Change preferred store from profile screen
        if changePreferredStoreProfileScreen{
            preferredStoreButton.hidden = false
            preferredStoreHeightConstraint.constant = 50
            startOrderButton.hidden = true
            startOrderButtonHeightConstraint.constant = 0
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "EmbeddedSegueToTableViewController" {
            let tableViewController = segue.destinationViewController as! StoreDetailTableViewController
            tableViewController.store = store
        }
    }
    
    @IBAction func makePreferredStore() {
        if UserService.sharedUser == nil {
            return
        }
        SVProgressHUD.showWithStatus("Saving changes...", maskType: .Clear)
        UserService.updateFavoriteStore(store) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            // CurrentStoreService.sharedInstance.resetStore(self.store)
            
            if self.changePreferredStoreProfileScreen{
                  NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.PreferredStoreChanged.rawValue, object: nil);
                return
            }
            
            NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.CurrentStoreChanged.rawValue, object: nil);
            self.presentOkAlert("Preferred Store", message: "\(self.store.name) has been saved as your preferred store")
            // clpAnalyticsService.sharedInstance.clpTrackScreenView("FavoriteStoreSelected")
        }
    }
    
    @IBAction func startOrder(sender: UIButton) {
        trackButtonPress(sender)
        // Check change store from basket or basket store detail screen
        if(basketTransfer){
            // Basket transfer validation between change store and current store
            basketTransferValidation()
        }
        else{
            if(BasketService.sharedBasket?.products.count>0){
                self.presentConfirmation("Start New Order", message: "Starting a new order will empty the basket and cancel your current order. Continue?", buttonTitle: "Start New Order") { confirmed in
                    if confirmed {
                        self.startOrder()
                    }
                }
            }
            else{
                startOrder()
            }
        }
    }
    
    //if "recentproducts" (or) "feature products" (or) ads are available then show feature product screen otherwise show full screen
    func goToMenuScreen() {
        ProductService.recentlyOrderedProductsAndAds { (products, adsList, error) in
            UIApplication.inMainThread {
                var hasRecentOrderedProducts = true
                if error != nil || (products.count == 0 && adsList.adsDetailList.count == 0) {
                    hasRecentOrderedProducts = false
                }
                
                if( CurrentStoreService.sharedInstance.storeBasedFeatureProducts.count>0 || hasRecentOrderedProducts){
                    if(UserService.sharedUser==nil){
                        NonSignedInViewController.sharedInstance().openProductMenuScreen()
                    }
                    else{
                        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.CurrentStoreChanged.rawValue, object: nil);
                        SignedInMainViewController.sharedInstance().openProductMenuScreen()
                    }
                }
                else{
                    if(UserService.sharedUser==nil){
                        
                        NonSignedInViewController.sharedInstance().openOrderScreen()
                    }
                    else{
                        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.CurrentStoreChanged.rawValue, object: nil);
                        SignedInMainViewController.sharedInstance().openOrderScreen()
                    }
                }
                
            }
        }
    }
    
    // Start an order for user selected store
    func startOrder(){
        SVProgressHUD.showWithStatus("Starting new order...", maskType: .Clear)
        CurrentStoreService.sharedInstance.startNewOrder(self.store) { (status, error) in
            SVProgressHUD.dismiss()
            if((status == "Success") && (error == nil)){
                productName = self.store.name;
                productID = Int64(self.store.id!)
                isAppEvent = true
                clpAnalyticsService.sharedInstance.clpTrackScreenView("START_NEW_ORDER")
                self.goToMenuScreen()
            }
            else{
                self.presentOkAlert("Error", message:"Please try again")
            }
        }
    }
    
    // Basket transfer vaildation and basket transfer
    func basketTransferValidation(){
        // When old basket is empty
        if BasketService.sharedBasket!.products.count == 0{
            startReorder()
            return
        }
        // Precaution for restaurant id nil
        if self.store.restaurantId == nil || BasketService.sharedBasket?.id == nil{
            self.presentError(NSError.init(description: Constants.genericErrorMessage))
            return
        }
        SVProgressHUD.showWithStatus("Basket Transferring...", maskType: .Clear)
        var tempStoreMenuBasket = StoreMenuBasket()
        OloBasketService.basketTransfer(self.store.restaurantId!, basketId: (BasketService.sharedBasket?.id)!, callback: { (basketTransfer, error) in
            
            if basketTransfer?.basket == nil || basketTransfer?.basket.products.count == 0 ||
                basketTransfer?.itemsnottransferred.count != 0 || error != nil {
                SVProgressHUD.dismiss()
                self.presentStorechangeConfirmation("Restart Order", message: "Not all the products in your basket are available in the new store. Do you want to empty your basket and start a new order, or do you can to keep existing basket and revert store selection?", buttonTitle: "Start New Order", callback: { (confirmed) in
                    if confirmed{
                        self.startReorder()
                    }
                    else{
                        // Cancel store change
                        self.dismissViewControllerAnimated(true, completion: {
                            NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.CancelStoreChange.rawValue, object: nil)
                            
                        })
                    }
                })
            }
            else{
                
                // Add reference to store in new basket
                tempStoreMenuBasket.oloBasket = basketTransfer?.basket
                
                CurrentStoreService.sharedInstance.menuForStore(self.store, callback: { (storeMenuBasket, error) in
                    SVProgressHUD.dismiss()
                    if error != nil{
                        return self.presentError(error)
                    }
                    if storeMenuBasket == nil{
                        return self.presentError(NSError.init(description: Constants.genericErrorMessage))
                    }
                    tempStoreMenuBasket.store = self.store
                    tempStoreMenuBasket.storeBasedFeatureProducts = storeMenuBasket!.storeBasedFeatureProducts
                    tempStoreMenuBasket.productTree = storeMenuBasket!.productTree
                    CurrentStoreService.sharedInstance.currentStoreMenuBasketUpdation(tempStoreMenuBasket)
                    self.dismissViewControllerAnimated(true, completion: {
                        if(UserService.sharedUser == nil){
                            NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.BasketTransferredForGuest.rawValue, object: nil)
                        }
                        else{
                            NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.BasketTransferredForUser.rawValue, object: nil)
                        }
                    })
                    
                })
            }
        })
    }
    
    // Store change confirmation alert
    func presentStorechangeConfirmation(title: String, message: String, buttonTitle: String, callback: (confirmed: Bool) -> Void) {
        let okAction = UIAlertAction(title: buttonTitle, style: .Default) { action in
            callback(confirmed: true)
        }
        let cancelAction = UIAlertAction(title: "Cancel Store Change", style: .Cancel) { action in
            callback(confirmed: false)
        }
        presentAlert(title, message: message, actions: okAction, cancelAction)
    }
    
    // Store Detail to menu screen
    func redirectToMenuScreen(){
        if (UserService.sharedUser == nil){
            self.performSegueWithIdentifier("NonSignedSegue", sender: self)
        }
        else{
            self.performSegueWithIdentifier("SignedSegue", sender: self)
        }
    }
    
    // Start Re-Order
    
    func startReorder(){
        SVProgressHUD.showWithStatus("Starting re-order...", maskType: .Clear)
        CurrentStoreService.sharedInstance.startNewOrder(self.store, callback: { (status, error) in
            SVProgressHUD.dismiss()
            if error == nil && status == "Success" {
                self.dismissViewControllerAnimated(true, completion: {
                    
                    if(UserService.sharedUser == nil){
                        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.ReStartOrderForGuest.rawValue, object: nil)
                    }
                    else{
                        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.ReStartOrderForUser.rawValue, object: nil)
                    }
                    
                })
            }
            else{
                self.presentError(NSError.init(description: Constants.genericErrorMessage))
            }
        })
    }
}
