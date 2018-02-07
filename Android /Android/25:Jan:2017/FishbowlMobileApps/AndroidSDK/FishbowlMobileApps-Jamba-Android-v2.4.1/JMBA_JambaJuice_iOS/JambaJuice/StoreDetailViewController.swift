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
            preferredStoreButton.isHidden = true
            preferredStoreHeightConstraint.constant = 0
        }
        
        // Store button hidden when store does not support order ahead
        if store.supportsOrderAhead == false {
            startOrderButton.isHidden = true
            startOrderButtonHeightConstraint.constant = 0
        }
        
        // Store search type OrderAhead for select store request
        if(storeSearchTypeOrderAhead){
            startOrderButton.setTitle("Select Store", for: UIControlState())
            preferredStoreButton.isHidden = true
            preferredStoreHeightConstraint.constant = 0
        }
        
        // Change preferred store from profile screen
        if changePreferredStoreProfileScreen{
            preferredStoreButton.isHidden = false
            preferredStoreHeightConstraint.constant = 50
            startOrderButton.isHidden = true
            startOrderButtonHeightConstraint.constant = 0
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        StatusBarStyleManager.pushStyle(.default, viewController: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "EmbeddedSegueToTableViewController" {
            let tableViewController = segue.destination as! StoreDetailTableViewController
            tableViewController.store = store
        }
    }
    
    @IBAction func makePreferredStore() {
        if UserService.sharedUser == nil {
            return
        }
        SVProgressHUD.show(withStatus: "Saving changes...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.updateFavoriteStore(store) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            // CurrentStoreService.sharedInstance.resetStore(self.store)
            
            if self.changePreferredStoreProfileScreen{
                  NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.PreferredStoreChanged.rawValue), object: nil);
                return
            }
            
            NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.CurrentStoreChanged.rawValue), object: nil);
            self.presentOkAlert("Preferred Store", message: "\(self.store.name) has been saved as your preferred store")
        }
    }
    
    @IBAction func startOrder(_ sender: UIButton) {
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("\(self.store.storeCode)", item_name: "\(self.store.name)", event_name: "START_NEW_ORDER")
        trackButtonPress(sender)
        // Check change store from basket or basket store detail screen
        if(basketTransfer){
            // Validate rewards and offers applied for the bakset
            if BasketService.validateBasketForRewardAndOffer(){
                self.presentConfirmationWithYesOrNo("Alert", message: "Changing basket contents will remove applied discounts. Coupon / reward will need to be re-applied manually. Proceed with changes?", buttonTitle: "Yes") { (confirmed) in
                    if confirmed{
                        if BasketService.validateAppliedReward(){
                            SVProgressHUD.show(withStatus: "Basket Transferring...")
                            SVProgressHUD.setDefaultMaskType(.clear)
                            BasketService.removeRewards({ (basket, error) in
                                SVProgressHUD.dismiss()
                                if error != nil{
                                    self.presentError(error)
                                }else{
                                    // Basket transfer validation between change store and current store
                                   self.basketTransferValidation()
                                }
                            })
                        } else if BasketService.validateAppliedOffer(){
                            SVProgressHUD.show(withStatus: "Basket Transferring...")
                            SVProgressHUD.setDefaultMaskType(.clear)
                            BasketService.removePromotionCode({ (basket, error) in
                                SVProgressHUD.dismiss()
                                if error != nil{
                                    self.presentError(error)
                                }else{
                                    // Basket transfer validation between change store and current store
                                    self.basketTransferValidation()
                                }
                            })
                        }
                    } else{
                        return
                    }
                }
            } else{
                // Basket transfer validation between change store and current store
                self.basketTransferValidation()
            }
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
                        NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.CurrentStoreChanged.rawValue), object: nil);
                        SignedInMainViewController.sharedInstance().openProductMenuScreen()
                    }
                }
                else{
                    if(UserService.sharedUser==nil){
                        
                        NonSignedInViewController.sharedInstance().openOrderScreen()
                    }
                    else{
                        NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.CurrentStoreChanged.rawValue), object: nil);
                        SignedInMainViewController.sharedInstance().openOrderScreen()
                    }
                }
                
            }
        }
    }
    
    // Start an order for user selected store
    func startOrder(){
        SVProgressHUD.show(withStatus: "Starting new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        CurrentStoreService.sharedInstance.startNewOrder(self.store) { (status, error) in
            SVProgressHUD.dismiss()
            if((status == "Success") && (error == nil)){
                self.goToMenuScreen()
            }
            else{
                self.presentOkAlert("Error", message:"Please try again")
            }
        }
    }
    
    // Basket transfer vaildation and basket transfer
    func basketTransferValidation(){
        // Precaution for restaurant id nil
        if self.store == nil || self.store.restaurantId == nil || BasketService.sharedBasket == nil || BasketService.sharedBasket!.id == ""{
            self.presentError(NSError.init(description: Constants.genericErrorMessage))
            return
        }
        // When old basket is empty
        if BasketService.sharedBasket!.products.count == 0{
            startReorder()
            return
        }
        SVProgressHUD.show(withStatus: "Basket Transferring...")
        SVProgressHUD.setDefaultMaskType(.clear)
        OloBasketService.basketTransfer(self.store.restaurantId!, basketId: BasketService.sharedBasket!.id, callback: { (basketTransfer, error) in
            if basketTransfer?.basket == nil || basketTransfer?.basket.products.count == 0 ||
                basketTransfer?.itemsnottransferred.count != 0 || error != nil {
                SVProgressHUD.dismiss()
                self.presentStorechangeConfirmation("Restart Order", message: "Not all the products in your basket are available in the new store. Do you want to empty your basket and start a new order, or do you can to keep existing basket and revert store selection?", buttonTitle: "Start New Order", callback: { (confirmed) in
                    if confirmed{
                        self.startReorder()
                    }
                    else{
                        // Cancel store change
                        self.dismiss(animated: true, completion: {
                            NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.CancelStoreChange.rawValue), object: nil)
                            
                        })
                    }
                })
            } else if basketTransfer?.basket.total != BasketService.sharedBasket?.total{
                SVProgressHUD.dismiss()
                self.presentConfirmation("Alert", message: "The price of one or more items in your basket has changed. The new total is: $\(basketTransfer!.basket.total)", buttonTitle: "Continue", callback: { (confirmed) in
                    if confirmed{
                          SVProgressHUD.show(withStatus: "Basket Transferring...")
                        SVProgressHUD.setDefaultMaskType(.clear)
                         self.basketTransferComplete(basketTransfer!)
                    }
                    else{
                        // Cancel store change
                        return
                    }
                })
            }
            else{
                 self.basketTransferComplete(basketTransfer!)
            }
        })
    }
    
    // Store change confirmation alert
    func presentStorechangeConfirmation(_ title: String, message: String, buttonTitle: String, callback: @escaping (_ confirmed: Bool) -> Void) {
        let okAction = UIAlertAction(title: buttonTitle, style: .default) { action in
            callback(true)
        }
        let cancelAction = UIAlertAction(title: "Cancel Store Change", style: .cancel) { action in
            callback(false)
        }
        presentAlert(title, message: message, actions: okAction, cancelAction)
    }
    
    // Store Detail to menu screen
    func redirectToMenuScreen(){
        if (UserService.sharedUser == nil){
            self.performSegue(withIdentifier: "NonSignedSegue", sender: self)
        }
        else{
            self.performSegue(withIdentifier: "SignedSegue", sender: self)
        }
    }
    
    // Start Re-Order
    
    func startReorder(){
        SVProgressHUD.show(withStatus: "Starting re-order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        CurrentStoreService.sharedInstance.startNewOrder(self.store, callback: { (status, error) in
            SVProgressHUD.dismiss()
            if error == nil && status == "Success" {
                self.dismiss(animated: true, completion: {
                    
                    if(UserService.sharedUser == nil){
                        NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.ReStartOrderForGuest.rawValue), object: nil)
                    }
                    else{
                        NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.ReStartOrderForUser.rawValue), object: nil)
                    }
                    
                })
            }
            else{
                self.presentError(NSError.init(description: Constants.genericErrorMessage))
            }
        })
    }
    
    func basketTransferComplete(_ basketTransfer:OloBasketTransfer){
        var tempStoreMenuBasket = StoreMenuBasket()
        // Add reference to store in new basket
        tempStoreMenuBasket.oloBasket = basketTransfer.basket
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
            self.dismiss(animated: true, completion: {
                if(UserService.sharedUser == nil){
                    NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.BasketTransferredForGuest.rawValue), object: nil)
                }
                else{
                    NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.BasketTransferredForUser.rawValue), object: nil)
                }
            })
            
        })
    }
}
