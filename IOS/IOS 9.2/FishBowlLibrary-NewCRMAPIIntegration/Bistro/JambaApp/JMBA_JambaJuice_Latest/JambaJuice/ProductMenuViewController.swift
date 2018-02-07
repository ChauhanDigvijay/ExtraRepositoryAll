//
//  ProductMenuViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/15/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import Haneke
import CoreLocation
import SVProgressHUD


class ProductMenuViewController: ScrollableModalViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, RecentOrdersCarouselViewControllerDelegate,ProtocolDelegate {
    
    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var storeAddressLabel: UILabel!
    
    @IBOutlet weak var topMarginConstraint: NSLayoutConstraint!
    @IBOutlet weak var recentOrdersHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var recentOrdersView: UIView!
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var storeNameView: UIView!
    @IBOutlet weak var storeNameWidth: NSLayoutConstraint!;
    @IBOutlet weak var storeAddressWidth: NSLayoutConstraint!;
    
    private var recentlyOrderedProducts: ProductList = []
    private var originalFeatureProducts: ProductList = []
    private var featuredProducts: ProductList = []

    private weak var recentOrdersCarouselViewController: RecentOrdersCarouselViewController?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ProductMenuViewController.updateStuff), name: "appDidBecomeActive", object: nil)
        
        
        topMarginConstraint.constant = Constants.homeScreenNavigationMenuHeight
        scrollViewBackgroundColor = collectionView.backgroundColor!
        recentOrdersView.backgroundColor = collectionView.backgroundColor!
        
        loadFeaturedProducts()
        
        storeNameLabel.text=CurrentStoreService.sharedInstance.currentStore?.name
        storeAddressLabel.text=CurrentStoreService.sharedInstance.currentStore?.address
        
        let width = UIScreen.mainScreen().bounds.width;
        storeNameWidth.constant = width - 180;
        storeAddressWidth.constant = width - 180;
        
        // Adjust collection view item size to fit two columns
        collectionView.collectionViewLayout.invalidateLayout()
        //Add observer
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ProductMenuViewController.recentlyOrderedProductsUpdated(_:)), name: JambaNotification.RecentlyOrderedProductsUpdated.rawValue, object: nil)
        StatusBarStyleManager.pushStyle(.Default, viewController: self)

        
    }
    
    
    override func viewWillAppear(animated: Bool) {
        
        self.updateStuff()
    }
    
    
    func updateStuff()
    {
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
     
            if appdelegate!.isPushOpen == true
            {
                appdelegate!.isPushOpen = false
                UIApplication.sharedApplication().statusBarHidden = true
                let vwoffer = OfferView()
                vwoffer.frame = CGRectMake(0, -130, self.view.frame.size.width, 100)
                vwoffer.tag = 1001
                vwoffer.delegate = self
                vwoffer.addCustomView()
                self.view.addSubview(vwoffer)
                
                UIView.animateWithDuration(0.4, animations: {() -> Void in
                    vwoffer.frame = CGRectMake(0, 0, self.view.frame.size.width, 100)
                })
            }
                
            else
            {
                UIApplication.sharedApplication().statusBarHidden = false
                
            }
    }
    
    func myMethod(controller:UIView)
    {
        
        // need to check
        performSegueWithIdentifier("OfferDetail", sender: self)

    }
    
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    func recentlyOrderedProductsUpdated(notification: NSNotification) {
        loadRecentlyOrderedProducts()
    }
    
    func recentOrdersCarouselViewControllerCloseMenu() {
         NonSignedInViewController.sharedInstance().closeModalScreen()
       // HomeViewController.sharedInstance().closeModalScreen {}
    }
    
    private func loadFeaturedProducts() {
        self.originalFeatureProducts=CurrentStoreService.sharedInstance.storeBasedFeatureProducts;
        self.updateDataProperties()
        self.loadRecentlyOrderedProducts()
     /*   ProductService.featuredProducts { (products, error) in
            UIApplication.inMainThread {
                if error != nil {
                    log.error("Error: no products loaded!")
                    return
                }
                
                self.originalFeatureProducts = products
                self.updateDataProperties()
                self.loadRecentlyOrderedProducts()
            }
        } */
        
    }
    
    private func loadRecentlyOrderedProducts() {
        ProductService.recentlyOrderedProducts { (products, error) -> Void in
            UIApplication.inMainThread {
                if error != nil {
                    log.error("Error: no products loaded!")
                } else {
                    self.recentlyOrderedProducts = products
                    self.updateDataProperties()
                }
                
                self.updateScreen()
            }
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
        {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("MENU_CLICK");
        }

    }
    
    override func viewDidDisappear(animated: Bool) {
        super.viewDidDisappear(animated)
        if UIApplication.sharedApplication().statusBarHidden.boolValue == true
        {
            UIApplication.sharedApplication().statusBarHidden = false
        }

//        updateStatusBarStyleForMaximumContrast()
    }
    
    private func updateDataProperties() {
        if originalFeatureProducts.count > 0 && recentlyOrderedProducts.count == 0 {
            let count = min(2, originalFeatureProducts.count)
            recentlyOrderedProducts = Array(originalFeatureProducts[0..<count])
            featuredProducts = Array(originalFeatureProducts[count..<originalFeatureProducts.count])
        }else {
            if originalFeatureProducts.count > 0 {
                featuredProducts = originalFeatureProducts
            }
        }
    }
    
    override internal func updateScreen() {
        super.updateScreen()
        recentOrdersCarouselViewController?.updateRecentlyOrderedProducts(recentlyOrderedProducts)
        collectionView.reloadData()
    }

    override internal func heightForScrollViewContent() -> CGFloat {
        let collectionViewHeight = ceil(CGFloat(featuredProducts.count) / 2) * (collectionCellSize().height + 1) // +1 for margin between cells
        return collectionViewHeight + recentOrdersView.frame.size.height + 40 // 40 for "Featured Products"
    }

    private func collectionCellSize() -> CGSize {
//        let width = (collectionView.frame.size.width - 1) / 2 // 2 columns
        let width = (UIScreen.mainScreen().bounds.width - 1) / 2
        let height = width / Constants.productThumbAspectRatio
        return CGSizeMake(width, height)
    }

    // MARK: - Collection View data source
    
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return featuredProducts.count
    }
    
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier("FeaturedProductCollectionViewCell", forIndexPath: indexPath) as! FeaturedProductCollectionViewCell
        cell.productIndex = indexPath.row
        cell.update(featuredProducts[indexPath.row])
        return cell
    }
    
    
    // MARK: - Collection View layout delegate
    
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAtIndexPath indexPath: NSIndexPath) -> CGSize {
        return collectionCellSize()
    }
    
    
    // MARK: - Scroll View delegate
    
    override func scrollViewDidScroll(scrollView: UIScrollView) {
        super.scrollViewDidScroll(scrollView)

        // Maximize screen when user scrolls to the bottom
        if scrollView.contentOffset.y > 0 && topMarginConstraint.constant > 0 {
            topMarginConstraint.constant = max(0, topMarginConstraint.constant - scrollView.contentOffset.y)
            scrollView.contentOffset.y = 0
        }
        // Snap back to open state
        else if scrollView.contentOffset.y < 0 && topMarginConstraint.constant < Constants.homeScreenNavigationMenuHeight {
            topMarginConstraint.constant = min(Constants.homeScreenNavigationMenuHeight, topMarginConstraint.constant - scrollView.contentOffset.y)
            scrollView.contentOffset.y = 0
        }

        updateStatusBar()
    }

    override func scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y < Constants.scrollViewThresholdToDismissModal {
            trackScreenEvent("close_screen", label: "drag_down")
            if(UserService.sharedUser==nil){
                NonSignedInViewController.sharedInstance().closeModalScreen()
            } else {
                SignedInMainViewController.sharedInstance().closeModalScreen()
            }
            
//            dismissModalController()


//            NonSignedInViewController.sharedInstance().closeModalScreen()
          //  HomeViewController.sharedInstance().closeModalScreen {}
            return
        }

        if decelerate == false {
            snapScrollView()
        }

        updateStatusBar()
    }

    func scrollViewDidEndDecelerating(scrollView: UIScrollView) {
        snapScrollView()
    }
    
    /// Return the closest end to the given point
    private func closestEnd(point: CGFloat, lowEnd: CGFloat, highEnd: CGFloat) -> CGFloat {
        return abs(point - lowEnd) < abs(point - highEnd) ? lowEnd : highEnd
    }
    
    /// Animate screen if needed. End position should be always close or open, but not in between
    private func snapScrollView() {
        if topMarginConstraint.constant == 0 || topMarginConstraint.constant == Constants.homeScreenNavigationMenuHeight {
            return
        }

        log.verbose("Before snap animation")
        log.verbose("TopConstraint: \(self.topMarginConstraint.constant)")
        log.verbose("ScrollY: \(self.scrollView.contentOffset.y)")

        // Snap scroll view to closest edge (open or close)
        view.layoutIfNeeded()
        topMarginConstraint.constant = closestEnd(topMarginConstraint.constant, lowEnd: 0, highEnd: Constants.homeScreenNavigationMenuHeight)
        UIView.animateWithDuration(0.25) {
            self.view.layoutIfNeeded()
            log.verbose("After snap animation")
            log.verbose("TopConstraint: \(self.topMarginConstraint.constant)")
            log.verbose("ScrollY: \(self.scrollView.contentOffset.y)")
            self.updateStatusBar()
        }
    }
    
    // Adjust status bar color depending on the scroll position
    private func updateStatusBar() {
        if topMarginConstraint.constant > 0 || scrollView.contentOffset.y < -UIApplication.sharedApplication().statusBarFrame.size.height {
            StatusBarStyleManager.pushStyle(.Default, viewController: self)
        } else {
            StatusBarStyleManager.pushStyle(.LightContent, viewController: self)
        }
    }
    
    
    // MARK: - Navigation
    
    @IBAction func closeScreen() {
        trackButtonPressWithName("Menu")
        if(UserService.sharedUser==nil){
            NonSignedInViewController.sharedInstance().closeModalScreen()
        } else {
            SignedInMainViewController.sharedInstance().closeModalScreen()
        }
//        dismissModalController()

    }

    @IBAction func openStoresScreen() {
        trackButtonPressWithName("Stores")
       // HomeViewController.sharedInstance().openStoresScreen()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ProductDetailSegue" {
            let productList: ProductList!
            let index: Int!
            if let rocVC = sender as? RecentOrdersCarouselViewController {
                productList = recentlyOrderedProducts
                index = rocVC.currentIndex
            }
            else {
                productList = featuredProducts
                index = (sender as! FeaturedProductCollectionViewCell).productIndex
            }
            let nc = segue.destinationViewController as! UINavigationController
            let vc = nc.viewControllers[0] as! ProductDetailViewController
            vc.productList = productList
            vc.currentProductIndex = index
            
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {

            let product = productList[index]
            if product.featured {
                trackScreenEvent("tap_featured_product", label: product.name)
                productID   = product.chainProductId;
                productName = product.name;
            } else {
                trackScreenEvent("tap_recent_ordered_product", label: product.name)
                productID   = product.chainProductId;
                productName = product.name;
            }
            
            isAppEvent = true;
            clpAnalyticsService.sharedInstance.clpTrackScreenView("FEATURE_PRODUCT_CLICK");
            }
            
        }
        else if segue.identifier == "RecentOrdersSegue" {
            recentOrdersCarouselViewController = segue.destinationViewController as? RecentOrdersCarouselViewController
            recentOrdersCarouselViewController?.delegate = self
        }
    }
    
    func recentOrdersCarouselViewControllerDidGetTapped(vc: RecentOrdersCarouselViewController) {
        self.performSegueWithIdentifier("ProductDetailSegue", sender: vc)
    }
    
    @IBAction func getDirection(){
        if let store=CurrentStoreService.sharedInstance.currentStore{
            let location = CLLocation(latitude: store.latitude, longitude: store.longitude)
            MapsManager.openMapsWithDirectionsToLocation(location, name: store.name)
        }
    }
   
}
