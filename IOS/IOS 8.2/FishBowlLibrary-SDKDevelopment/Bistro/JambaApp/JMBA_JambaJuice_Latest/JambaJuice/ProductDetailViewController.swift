//
//  ProductDetailViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Haneke
import SVProgressHUD
import CoreLocation
import OloSDK
import SwiftyJSON

class ProductDetailViewController: ScrollableModalViewController, ProductDetailAvailableViewControllerDelegate, SelectStoreViewControllerDelegate {
    
    @IBOutlet weak var closeButton: UIButton!
    @IBOutlet weak var productImageView: UIImageView!
    @IBOutlet weak var productNameLabel: UILabel!
    @IBOutlet weak var productIngredientsLabel: UILabel!
    @IBOutlet weak var orderedAgoLabel: UILabel!
    
    @IBOutlet weak var productAvailableContainer: UIView!
    @IBOutlet weak var productNotAvailableView: UIView!
    @IBOutlet weak var productNotAvailableLabel: UILabel!
    
    @IBOutlet weak var addToCartView: UIView!
    @IBOutlet weak var cartTotalAmountLabel: UILabel!
    @IBOutlet weak var startOrderButton: UIButton!
    @IBOutlet weak var selectDifferentStore: UIButton!
    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var storeAddressLabel: UILabel!
    
    //for store name width constraint
    @IBOutlet weak var storeNameView: UIView!
    @IBOutlet weak var storeNameWidth: NSLayoutConstraint!;
    @IBOutlet weak var storeAddressWidth: NSLayoutConstraint!;
    
    var productList: ProductList = []
    var currentProductIndex = 0
    private var product: Product {
        get {
            return productList[currentProductIndex]
        }
    }
    
    private var productAvailableViewController: ProductDetailAvailableViewController?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.navigationBarHidden = true
        configureNavigationBar(.LightBlue)
        StatusBarStyleManager.pushStyle(.Default, viewController: self)
        title = product.name
        
        productNameLabel.text = product.name
        productIngredientsLabel.text = product.ingredients.lowercaseString
        if let url = NSURL(string: product.imageUrl) {
            productImageView.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
        }
        
        // TODO: If user is logged in, check if product was ordered recently
        orderedAgoLabel.hidden = true
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ScrollableModalViewController.updateScreen), name: JambaNotification.SharedBasketUpdated.rawValue, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ScrollableModalViewController.updateScreen), name: JambaNotification.OrderStarted.rawValue, object: nil)
        
        // Store Header text
        storeNameLabel.text=CurrentStoreService.sharedInstance.currentStore?.name
        storeAddressLabel.text=CurrentStoreService.sharedInstance.currentStore?.address
        
        let width = UIScreen.mainScreen().bounds.width;
        storeNameWidth.constant = width - 180;
        storeAddressWidth.constant = width - 180;
        
    }
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ProductAvailableSegue" {
            productAvailableViewController = segue.destinationViewController as? ProductDetailAvailableViewController
            productAvailableViewController?.product = productList[currentProductIndex]
            productAvailableViewController?.delegate = self
        }
        else if segue.identifier == "SelectPickupLocationSegue" {
            let vc = segue.destinationViewController as! SelectStoreViewController
            vc.delegate = self
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        AnalyticsService.trackEvent("view", action: "product_view", label: product.name)
        updateNavBarAccordingToScrollOffset()
    }
    
    override func didReceiveMemoryWarning() {
        // Remove previous view controlers from stack
        if let viewControllers = navigationController?.viewControllers {
            if viewControllers.last == self && viewControllers.count > 1 {
                navigationController?.setViewControllers([self], animated: false)
            }
        }
        
        super.didReceiveMemoryWarning()
    }
    
    override internal func heightForScrollViewContent() -> CGFloat {
        log.verbose("Error in height")
        if product.storeMenuProduct != nil {
            let height = productAvailableViewController!.contentHeight() + 300 + 80// 300 for hero area + 80 for store selection header
            log.verbose("New scroll view content height: \(height)")
            return height
        } else {
            return UIScreen.mainScreen().bounds.height
        }
    }
    
    override internal func updateScreen() {
        super.updateScreen()
        productAvailableViewController?.updateScreen()
        closeButton.setTitle("", forState: .Normal)
        
        // If an order has not been started
        if BasketService.sharedBasket == nil {
            productAvailableContainer.hidden = true
            productNotAvailableView.hidden = false
            productNotAvailableLabel.text = "Choose a store to\nstart your order"
            addToCartView.hidden = true
            startOrderButton.hidden = false
            selectDifferentStore.hidden = true
        }
            // If order is started, and:
            // If product is available at current selected store
        else if product.storeMenuProduct != nil {
            productAvailableContainer.hidden = false
            productNotAvailableView.hidden = true
            addToCartView.hidden = false
            startOrderButton.hidden = true
            selectDifferentStore.hidden = true
            loadModifiersIfNeeded()
        }
            // Order has started but product is not available
        else {
            productAvailableContainer.hidden = true
            productNotAvailableView.hidden = false
            productNotAvailableLabel.text = "This product is not available at the selected store"
            addToCartView.hidden = true
            startOrderButton.hidden = true
            selectDifferentStore.hidden = false
        }
    }
    
    //    private func loadModifiersIfNeeded() {
    //        if product.storeMenuProduct == nil || product.storeMenuProduct!.hasPopulatedModifiers {
    //            // Nothing to do
    //            return
    //        }
    //
    //        log.verbose("Loading product modifiers: \(self.product.name)")
    //        SVProgressHUD.showWithMaskType(.Clear) // No message
    //        StoreService.modifiersForProduct(product) { (modifiers, error) in
    //            SVProgressHUD.dismiss()
    //            if error != nil {
    //                self.presentError(error)
    //                return
    //            }
    //            self.product.storeMenuProduct?.setModifiers(modifiers)
    //            self.productAvailableViewController?.updateScreen()
    //            self.updateScreen() // Adjust screen size
    //        }
    //    }
    
    private func loadModifiersIfNeeded() {
        if product.storeMenuProduct == nil || product.storeMenuProduct!.hasPopulatedModifiers {
            // Nothing to do
            return
        }
        
        // MARK: - Remove me
//        if (self.product.name.lowercaseString.containsString("orange c-booster") ) {
//            // remove below code
//            // hardcode data - add more
//            do {
//                let jsonfilepath = NSBundle.mainBundle().pathForResource("document", ofType: "json")
//                let jsonstr = try String(contentsOfFile: jsonfilepath!, encoding: NSUTF8StringEncoding)
//                let jsondata = jsonstr.dataUsingEncoding(NSUTF8StringEncoding)
//                guard let jsonObject = try? NSJSONSerialization.JSONObjectWithData(jsondata!, options: NSJSONReadingOptions()) else {
//                    return
//                }
//                let json = JSON(jsonObject)
//                let modifiers = json.arrayValue.map { item in OloModifier(json: item) }
//                let smodifiers = modifiers.map { StoreMenuProductModifier(oloModifier: $0) }
//                
//                self.product.storeMenuProduct?.setModifiers(smodifiers)
//                self.productAvailableViewController?.updateScreen()
//                self.updateScreen() // Adjust screen size
//                
//            }catch {
//                
//            }
//            return
//            //
//        }
        // MARK: -
        
        log.verbose("Loading product modifiers: \(self.product.name)")
        SVProgressHUD.showWithMaskType(.Clear) // No message
        StoreService.modifiersForProduct(product) { (modifiers, error) in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            //StoreMenuProductModifier
            self.product.storeMenuProduct?.setModifiers(modifiers)
            self.productAvailableViewController?.updateScreen()
            self.updateScreen() // Adjust screen size
        }
    }
    
    @IBAction func closeScreen(sender: AnyObject) {
        trackButtonPressWithName("swipe-down-button")
        dismissModalController()
    }
    
    
    @IBAction func closeScreenToDashboard(sender: AnyObject)
    {
        
        trackButtonPressWithName("Menu")
        if(UserService.sharedUser==nil){
           // NonSignedInViewController.sharedInstance().closeModalScreen()
        } else {
            SignedInMainViewController.sharedInstance().closeModalScreenToDashboard()
            
        }
    }
    
    
    
    // MARK: ProductAvailableViewControllerDelegate
    
    func productDetailAvailableTableViewContentChanged() {
        updateScreen()
    }
    
    func costUpdated(cost: Double) {
        cartTotalAmountLabel.text = String(format: "$%.2f", cost)
    }
    
    func keyboardDidShow() {
        let bottomOffset = CGPointMake(0, scrollView.contentSize.height - scrollView.bounds.size.height)
        scrollView.setContentOffset(bottomOffset, animated: true)
    }
    
    private func updateNavBarAccordingToScrollOffset() {
        // Show navigation bar when scrolling down the list
        if scrollView.contentOffset.y > Constants.navigationBarHiddenThreshold {
            navigationController?.navigationBarHidden = false
            //            StatusBarStyleManager.pushStyle(.Default, viewController: self)
        } else {
            navigationController?.navigationBarHidden = true
            //            StatusBarStyleManager.pushStyle(.LightContent, viewController: self)
        }
    }
    
    // MARK: ScrollViewDelegate
    
    override func scrollViewDidScroll(scrollView: UIScrollView) {
        super.scrollViewDidScroll(scrollView)
        updateNavBarAccordingToScrollOffset()
    }
    
    
    // MARK: Navigation
    
    // Swipe right, load previous
    @IBAction func prevProduct(sender: UIGestureRecognizer) {
        trackGesture(sender)
        if currentProductIndex <= 0 {
            return
        }
        
        // Check if we need to inject previous view controller
        if navigationController?.viewControllers.count == 1 {
            let vc = UIViewController.instantiate("ProductDetailViewController") as! ProductDetailViewController
            vc.productList = productList
            vc.currentProductIndex = currentProductIndex - 1
            var controllers = navigationController!.viewControllers
            controllers.insert(vc, atIndex: controllers.count - 1)
            navigationController?.setViewControllers(controllers, animated: false)
        }
        
        navigationController?.popViewControllerAnimated(true)
        trackScreenEvent("swipe_prev_product", label: product.name)
    }
    
    // Swipe left, load next
    @IBAction func nextProduct(sender: UIGestureRecognizer) {
        trackGesture(sender)
        if currentProductIndex >= productList.count - 1 {
            return
        }
        
        // Add next view controller
        let vc = UIViewController.instantiate("ProductDetailViewController") as! ProductDetailViewController
        vc.productList = productList
        vc.currentProductIndex = currentProductIndex + 1
        navigationController?.pushViewController(vc, animated: true)
        trackScreenEvent("swipe_next_product", label: product.name)
    }
    
    
    // MARK: User actions
    
    @IBAction func startNewOrder(sender: UIButton) {
        trackButtonPress(sender)
        
        // Check if a basket already exists and has at least one product
        if BasketService.sharedBasket?.products.count > 0 {
            // Ask user for confirmation to wipe out current basket
            // If user confirms, wipe basket and start new order
            presentConfirmation("Choose Another Store", message: "Choosing a different store will empty the basket and cancel the current order. Continue?", buttonTitle: "Choose Different Store") { confirmed in
                if confirmed {
                    BasketService.deleteAndCreateNewBasket(nil)                    
                    self.startNewOrder(sender)
                }
            }
            // Do not start new order
            return
        }
        
        // Check if user is logged in and his preferred store supports order ahead
        if UserService.sharedUser?.favoriteStore?.restaurantId != nil {
            performSegueWithIdentifier("SelectPickupLocationSegue", sender: self)
        } else {
            performSegueWithIdentifier("SearchPickUpStoreSegue", sender: self)
        }
    }
    
    @IBAction func selectDifferentStore(sender: UIButton) {
        trackButtonPress(sender)
        startNewOrder(sender)
    }
    
    /// Add current product with selected options to basket
    /// Requirements: User must have started an order and basket must exist
    @IBAction func addToBasket(sender: UIButton) {
        trackButtonPress(sender)
        let existingQuantity = BasketService.itemsInBasket()
        //If we already have 10 items in basket
        if existingQuantity >= 10 {
            presentOkAlert("Too Many Items", message: "The maximum number of items allowed per order is 10")
            return
        }
        //If we trying to add 0 items to bakset.
        let userChoice = productAvailableViewController!.userChoice
        if userChoice.quantity == 0 {
            presentOkAlert("Invalid Quantity", message: "Please add at least one product")
            return
        }
        if validateUserChoice() == false {
            return
        }
        
        let quantity = Int64(userChoice.quantity)
        var optionsAsInt64Ids = Set<Int64>()
        for (_, seletedOptionIdsForModifierID) in userChoice.selectedOptionIdsForModifierId {
            optionsAsInt64Ids.unionInPlace(seletedOptionIdsForModifierID)
        }
        var options: [String] = []
        for optionAsInt64Id in optionsAsInt64Ids {
            options.append("\(optionAsInt64Id)")
        }
        if userChoice.selectedTypeOptionId != nil {
            options.append("\(userChoice.selectedTypeOptionId!)")
        }
        if userChoice.selectedSizeOptionId != nil {
            options.append("\(userChoice.selectedSizeOptionId!)")
        }
        
        for option in userChoice.selectedCustomizeOptionIds {
            let optionId = option
            options.append("\(optionId)")
        }
        
        
        
        
        log.verbose("Make server call to add product to basket")
        // Per UX direction, no need to show loading indicator here
        sender.enabled = false
        BasketService.addProduct(product, quantity: quantity, options: options, specialInstructions: userChoice.specialInstructions) { (basket, error) in
            sender.enabled = true
            if error != nil {
                self.presentError(error)
                return
            }
            
            log.verbose("Product has been added to basket")
            
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
            
            productName = self.product.name
            productID = self.product.storeMenuProduct?.productId
            isAppEvent = true
            clpAnalyticsService.sharedInstance.clpTrackScreenView("ADD_PRODUCT");
            }

            //            self.closeButton.setTitle("Continue Shopping", forState: .Normal)
            self.dismissModalController()
        }
    }
    
    private func validateUserChoice() -> Bool {
        var errorString = ""
        if product.storeMenuProduct != nil {
            for productModifier in product.storeMenuProduct!.productModifiers {
                var subtract = 0
                if productModifier.optionIdForUnnestedOption != nil {
                    subtract = 1
                }
                let maxSelects = productModifier.maxSelects
                let minSelects = productModifier.minSelects
                let userChoice = productAvailableViewController!.userChoice
                let selectedOptionIdsForModifierId = userChoice.selectedOptionIdsForModifierId[productModifier.id]
                if selectedOptionIdsForModifierId != nil {
                    if maxSelects != nil {
                        if Int64(selectedOptionIdsForModifierId!.count - subtract) > maxSelects! {
                            errorString += "Please select up to \(productModifier.maxSelects!) options from the group \(productModifier.name)"
                        }
                    }
                    if minSelects != nil {
                        if Int64(selectedOptionIdsForModifierId!.count - subtract) < minSelects! {
                            errorString += "Please select at least \(productModifier.minSelects!) options from the group \(productModifier.name)"
                        }
                    }
                }
            }
        }
        if errorString.length > 0 {
            presentOkAlert("Invalid Options", message: errorString)
            return false
        }
        return true
    }
    
    // MARK: SelectStoreViewControllerDelegate
    
    func selectStoreViewControllerUseDifferentStore() {
        performSegueWithIdentifier("SearchPickUpStoreSegue", sender: self)
    }
    
    func selectStoreViewControllerUsePreferredStore() {
        if let store = UserService.sharedUser?.favoriteStore {
            self.refetchStoreToUpdateOloRestaurantPropertiesAndStartOrder(store)
        }
    }
    
    func refetchStoreToUpdateOloRestaurantPropertiesAndStartOrder(store: Store) {
        if store.storeCode == nil{
            self.presentError(NSError(description: "Unexpected error occured while processing your request."))
            return   
        }
        SVProgressHUD.showWithStatus("Starting new order...", maskType: .Clear)
        log.verbose("Fetching Olo Restaurant info on start order from preferred store")
        StoreService.storeByStoreCode(store.storeCode!, callback: { (stores, location, error) -> Void in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            if stores.count != 1 {
                self.presentError(NSError(description: "Unexpected error occured while processing your request."))
                return
            }
            store.updateOloRestaurantProperties(stores.first!)
            self.startNewOrder(store)
        })
    }
    
    func startNewOrder(store: Store) {
        OrderStarter.startOrder(store, fromViewController: self) { success in
            // Nothing to do
        }
    }
    
    //location of the store in the map
    @IBAction func getDirection(){
        if let store=CurrentStoreService.sharedInstance.currentStore{
            let location = CLLocation(latitude: store.latitude, longitude: store.longitude)
            MapsManager.openMapsWithDirectionsToLocation(location, name: store.name)
        }
    }
    
}
