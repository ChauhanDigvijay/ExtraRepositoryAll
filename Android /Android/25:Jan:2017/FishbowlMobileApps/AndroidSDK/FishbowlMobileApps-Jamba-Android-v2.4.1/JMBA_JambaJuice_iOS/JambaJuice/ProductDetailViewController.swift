

//
//  ProductDetailViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import AlamofireImage
import SVProgressHUD
import CoreLocation
import OloSDK
import SwiftyJSON
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


class ProductDetailViewController: ScrollableModalViewController, ProductDetailAvailableViewControllerDelegate, SelectStoreViewControllerDelegate, ProductDetailPickerViewControllerDelegate {
    
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
    //    @IBOutlet weak var storeNameView: UIView!
    
    @IBOutlet weak var pickerView: UIView!
    
    var productList: ProductList = []
    var currentProductIndex = 0
    fileprivate var product: Product {
        get {
            return productList[currentProductIndex]
        }
    }
    
    fileprivate var productAvailableViewController: ProductDetailAvailableViewController?
    fileprivate var productDetailPickerViewController: ProductDetailPickerViewController?
    var screenPresented : Bool = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.isNavigationBarHidden = true
        configureNavigationBar(.lightBlue)
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        title = product.name
        
        productNameLabel.text = product.name
        productIngredientsLabel.text = product.ingredients.lowercased()
        if let url = URL(string: product.imageUrl) {
            productImageView.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.productImageView.image = image.value
            })
        }
        
        // TODO: If user is logged in, check if product was ordered recently
        orderedAgoLabel.isHidden = true
        
        NotificationCenter.default.addObserver(self, selector: #selector(ScrollableModalViewController.updateScreen), name: NSNotification.Name(rawValue: JambaNotification.SharedBasketUpdated.rawValue), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ScrollableModalViewController.updateScreen), name: NSNotification.Name(rawValue: JambaNotification.OrderStarted.rawValue), object: nil)
        
        // Store Header text
        storeNameLabel.text=CurrentStoreService.sharedInstance.currentStore?.name
        storeAddressLabel.text=CurrentStoreService.sharedInstance.currentStore?.address
        
        //        let width = UIScreen.mainScreen().bounds.width;
        //        storeNameWidth.constant = width - 180;
        //        storeAddressWidth.constant = width - 180;
        
        pickerView.isHidden = true
        
        if #available(iOS 11.0, *) {
            scrollView?.contentInsetAdjustmentBehavior = .never
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ProductAvailableSegue" {
            productAvailableViewController = segue.destination as? ProductDetailAvailableViewController
            productAvailableViewController?.product = productList[currentProductIndex]
            productAvailableViewController?.delegate = self
        }
        else if segue.identifier == "SelectPickupLocationSegue" {
            let vc = segue.destination as! SelectStoreViewController
            vc.delegate = self
        } else if segue.identifier == "ProductDetailPickerVCSegue" {
            productDetailPickerViewController = segue.destination as? ProductDetailPickerViewController
            productDetailPickerViewController?.delegate = self
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        var itemName = ""
        var itemId = ""
        if !product.name.isEmpty {
            itemName = product.name
            if let storeMenuProduct = product.storeMenuProduct{
                itemId = "\(storeMenuProduct.productId)"
            }
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent(itemId, item_name: itemName, event_name: "PRODUCT_CLICK")
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
            return UIScreen.main.bounds.height
        }
    }
    
    override internal func updateScreen() {
        super.updateScreen()
        productAvailableViewController?.updateScreen()
        closeButton.setTitle("", for: UIControlState())
        
        // If an order has not been started
        if BasketService.sharedBasket == nil {
            productAvailableContainer.isHidden = true
            productNotAvailableView.isHidden = false
            productNotAvailableLabel.text = "Choose a store to\nstart your order"
            addToCartView.isHidden = true
            startOrderButton.isHidden = false
            selectDifferentStore.isHidden = true
        }
            // If order is started, and:
            // If product is available at current selected store
        else if product.storeMenuProduct != nil {
            productAvailableContainer.isHidden = false
            productNotAvailableView.isHidden = true
            addToCartView.isHidden = false
            startOrderButton.isHidden = true
            selectDifferentStore.isHidden = true
            loadModifiersIfNeeded()
        }
            // Order has started but product is not available
        else {
            productAvailableContainer.isHidden = true
            productNotAvailableView.isHidden = false
            productNotAvailableLabel.text = "This product is not available at the selected store"
            addToCartView.isHidden = true
            startOrderButton.isHidden = true
            selectDifferentStore.isHidden = false
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
    
    fileprivate func loadModifiersIfNeeded() {
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
        SVProgressHUD.show(withStatus: "")
        SVProgressHUD.setDefaultMaskType(.clear)
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
    
    @IBAction func closeScreen(_ sender: AnyObject) {
        trackButtonPressWithName("swipe-down-button")
        dismissModalController()
    }
    
    
    @IBAction func closeScreenToDashboard(_ sender: AnyObject)
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
    
    func costUpdated(_ cost: Double) {
        cartTotalAmountLabel.text = String(format: "$%.2f", cost)
    }
    
    func keyboardDidShow() {
        let bottomOffset = CGPoint(x: 0, y: scrollView.contentSize.height - scrollView.bounds.size.height)
        scrollView.setContentOffset(bottomOffset, animated: true)
    }
    
    fileprivate func updateNavBarAccordingToScrollOffset() {
        // Show navigation bar when scrolling down the list
        if scrollView.contentOffset.y > Constants.navigationBarHiddenThreshold {
            navigationController?.isNavigationBarHidden = false
            //            StatusBarStyleManager.pushStyle(.Default, viewController: self)
        } else {
            navigationController?.isNavigationBarHidden = true
            //            StatusBarStyleManager.pushStyle(.LightContent, viewController: self)
        }
    }
    
    // MARK: ScrollViewDelegate
    
    override func scrollViewDidScroll(_ scrollView: UIScrollView) {
        super.scrollViewDidScroll(scrollView)
        updateNavBarAccordingToScrollOffset()
    }
    
    
    // MARK: Navigation
    
    // Swipe right, load previous
    @IBAction func prevProduct(_ sender: UIGestureRecognizer) {
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
            controllers.insert(vc, at: controllers.count - 1)
            navigationController?.setViewControllers(controllers, animated: false)
        }
        
        _ = navigationController?.popViewController(animated: true)
        trackScreenEvent("swipe_prev_product", label: product.name)
    }
    
    // Swipe left, load next
    @IBAction func nextProduct(_ sender: UIGestureRecognizer) {
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
    
    @IBAction func startNewOrder(_ sender: UIButton) {
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
            performSegue(withIdentifier: "SelectPickupLocationSegue", sender: self)
        } else {
            performSegue(withIdentifier: "SearchPickUpStoreSegue", sender: self)
        }
    }
    
    @IBAction func selectDifferentStore(_ sender: UIButton) {
        trackButtonPress(sender)
        startNewOrder(sender)
    }
    
    /// Add current product with selected options to basket
    /// Requirements: User must have started an order and basket must exist
    @IBAction func addToBasket(_ sender: UIButton) {
        // Validate rewards and offers
        if BasketService.validateBasketForRewardAndOffer(){
            showRewardAndOfferAlert(sender)
        }else{
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
        var optionsWithQuantitys:[OloProductChoiceQuantity] = []
        for (_, seletedOptionIdsForModifierID) in userChoice.selectedOptionIdsForModifierId {
            for optionId in seletedOptionIdsForModifierID {
                var optionsWithQuantity = OloProductChoiceQuantity()
                optionsWithQuantity.choiceId = optionId.choiceId
                optionsWithQuantity.quantity = optionId.quantity
                optionsWithQuantitys.append(optionsWithQuantity)
            }
        }
        if userChoice.selectedTypeOptionId != nil {
            var optionsWithQuantity = OloProductChoiceQuantity()
            optionsWithQuantity.choiceId = userChoice.selectedTypeOptionId!
            optionsWithQuantity.quantity = 1
            optionsWithQuantitys.append(optionsWithQuantity)
        }
        if userChoice.selectedSizeOptionId != nil {
            var optionsWithQuantity = OloProductChoiceQuantity()
            optionsWithQuantity.choiceId = userChoice.selectedSizeOptionId!
            optionsWithQuantity.quantity = 1
            optionsWithQuantitys.append(optionsWithQuantity)
        }
        
        for option in userChoice.selectedCustomizeOptionIds {
            var optionsWithQuantity = OloProductChoiceQuantity()
            optionsWithQuantity.choiceId = option
            optionsWithQuantity.quantity = 1
            optionsWithQuantitys.append(optionsWithQuantity)
        }
        
        // Fishbowl event
        var itemId = ""
        var itemName = ""
        var cost = ""
        cost = cartTotalAmountLabel.text!
        // removing first character '$'
        let updatedCost = String(cost.characters.dropFirst())
        if let storeProduct = product.storeMenuProduct{
            itemId = "\(storeProduct.productId)"
            itemName = "PRODUCT_NAME = \(product.name);TOTAL_QUANTITY = \(quantity);TOTAL_COST = \(updatedCost)"
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent(itemId, item_name: itemName, event_name: "ADD_PRODUCT")
        log.verbose("Make server call to add product to basket")
        // Per UX direction, no need to show loading indicator here
        sender.isEnabled = false
        BasketService.addProductWithOptionQuantity(product, quantity: quantity, options: optionsWithQuantitys, specialInstructions: userChoice.specialInstructions) { (basket, error) in
            sender.isEnabled = true
            if error != nil {
                self.presentError(error)
                return
            }
            
            //            self.closeButton.setTitle("Continue Shopping", forState: .Normal)
            self.dismissModalController()
        }
        }
    }
    
    fileprivate func validateUserChoice() -> Bool {
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
                
                //check for min & max choices in a modifiers
                if selectedOptionIdsForModifierId != nil {
                    if maxSelects != nil {
                        if Int64(selectedOptionIdsForModifierId!.count - subtract) > maxSelects! {
                            errorString += "Please select up to \(productModifier.maxSelects!) options from the group '\(productModifier.name)'"
                        }
                    }
                    //if minselect greather than 0, then validate
                    if minSelects != nil && minSelects != 0 {
                        if Int64(selectedOptionIdsForModifierId!.count - subtract) < minSelects! {
                            errorString += "Please select at least \(productModifier.minSelects!) options from the group '\(productModifier.name)'"
                        }
                    }
                    
                //check for mandatory
                } else if productModifier.mandatory {
                    presentOkAlert("Error", message: "Please make a selection for '\(productModifier.name)'")
                    return false
                }
            }
        }
        if errorString.length > 0 {
            presentOkAlert("Invalid Options", message: errorString)
            return false
        }
        return true
    }
    
//    fileprivate func validateUserChoiceMandatory(modifiers:[StoreMenuProductModifier]) -> Bool {
//        for modifier in modifiers {
//            
//            if modifier.mandatory {
//                let userChoice = productAvailableViewController!.userChoice
//                if userChoice.selectedOptionIdsForModifierId[modifier.i]
//            }
//        }
//        return true
//    }
    
    // MARK: SelectStoreViewControllerDelegate
    
    func selectStoreViewControllerUseDifferentStore() {
        performSegue(withIdentifier: "SearchPickUpStoreSegue", sender: self)
    }
    
    func selectStoreViewControllerUsePreferredStore() {
        if let store = UserService.sharedUser?.favoriteStore {
            self.refetchStoreToUpdateOloRestaurantPropertiesAndStartOrder(store)
        }
    }
    
    func refetchStoreToUpdateOloRestaurantPropertiesAndStartOrder(_ store: Store) {
        SVProgressHUD.show(withStatus: "Starting new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        log.verbose("Fetching Olo Restaurant info on start order from preferred store")
        StoreService.storeByStoreCode(store.storeCode, callback: { (stores, location, error) -> Void in
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
    
    func startNewOrder(_ store: Store) {
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
    
    @IBAction func closePickerView(){
        productDetailPickerViewController?.closePicker()
    }
    
    //MARK:- Picker View delegates
    func pickerValueChanged(_ value:String,index:Int) {
        productAvailableViewController?.saveProductModifierQuantity(value)
    }
    func closepickerScreen() {
        pickerView.isHidden = true
    }
    
    //show picker only if maxAggregateQuantity value is present
    // if picker is showing, then validate the exact Maximum to show in picker
    func showProductQuantityPickerView(_ selectedValue:String, parentModifier: StoreMenuProductModifier, option: StoreMenuProductModifierOption) {
        
        //if supportsChoiceQuantities is false or nil then do not show the product modifier choice's picker
        if parentModifier.supportsChoiceQuantities == nil || !parentModifier.supportsChoiceQuantities! {
            return
        }
        
        if var maxValue = parentModifier.maxAggregateQuantity {
            if parentModifier.maxChoiceQuantity != nil {
                if parentModifier.maxChoiceQuantity! <= maxValue {
                    maxValue = parentModifier.maxChoiceQuantity!
                }
            }
            var totalQuanitySelected = productAvailableViewController?.userChoice.getTotalQuantityWithOptionId(parentModifier.id, optionId: option.id)
            
            let unNestedOptionIdCount = productAvailableViewController?.getUnNestedOptionId(modifier: parentModifier)
            
            totalQuanitySelected! =  totalQuanitySelected! == 0 ? 0:(totalQuanitySelected! - unNestedOptionIdCount!)
            
            if (Int(maxValue) - totalQuanitySelected!) <= 1 {
                return
            }
            
            productDetailPickerViewController?.setMaxQuantity(Int(maxValue) - totalQuanitySelected!)
            productDetailPickerViewController?.picker.reloadAllComponents()
            pickerView.isHidden = false
            productDetailPickerViewController?.selectedValue1 = selectedValue
            productDetailPickerViewController?.retainSelectedValue()
            productDetailPickerViewController?.pickerHeaderTitleLabel.text = "Select Quantity"
            productDetailPickerViewController?.showPicker()
        }
    }
    
    //MARK: show product search screen
    @IBAction func showProductSearchScreen(){
        if screenPresented {
            performSegue(withIdentifier: "ShowProductSearchVC", sender: self)
        } else {
            dismissModalController()
        }
    }

    
    // Show the alert
    func showRewardAndOfferAlert(_ sender: UIButton){
        self.presentConfirmationWithYesOrNo("Alert", message: "Changing basket contents will remove applied discounts. Coupon / reward will need to be re-applied manually. Proceed with changes?", buttonTitle: "Yes") { (confirmed) in
            if confirmed{
                if BasketService.validateAppliedReward(){
                    sender.isEnabled = false
                    BasketService.removeRewards({ (basket, error) in
                    sender.isEnabled = true
                        if error != nil{
                            self.presentError(error)
                        }else{
                            self.addToBasket(sender)
                        }
                    })
                } else if BasketService.validateAppliedOffer(){
                    sender.isEnabled = false
                    BasketService.removePromotionCode({ (basket, error) in
                        sender.isEnabled = true
                        if error != nil{
                            self.presentError(error)
                        }else{
                           self.addToBasket(sender)
                        }
                    })
                }
            } else{
                return
            }
        }
    }
}
