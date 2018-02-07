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
    @IBOutlet weak var downArrowTopSpaceConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var recentOrdersView: UIView!
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var recentOrderImage1: UIImageView!
    @IBOutlet weak var recentOrderImage2: UIImageView!
    @IBOutlet weak var recentOrderShadeImage2: UIImageView!
    @IBOutlet weak var recentOrderImageView: UIView!
    @IBOutlet weak var recentOrderImageHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var recentOrderLabel1: UILabel!
    @IBOutlet weak var recentOrderLabel2: UILabel!
    
    @IBOutlet weak var featureProductNoContent: UILabel!
    
    
    private var recentlyOrderedProducts: ProductList = []
    private var featuredProducts: ProductList = []
    private var adsList: AdProductsList = AdClass.init()
    
    let noRecordDefaultHeight:CGFloat = 0
    let downArrowNormalTopSpace:CGFloat = 15
    let downArrowWithotAdTopSpace:CGFloat = 35
    
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
    }
    
    private func loadFeaturedProducts() {
        self.featuredProducts = CurrentStoreService.sharedInstance.storeBasedFeatureProducts;
        self.loadRecentlyOrderedProducts()
    }
    
    private func loadRecentlyOrderedProducts() {
        ProductService.recentlyOrderedProductsAndAds { (products, adsList, error) in
            UIApplication.inMainThread {
                if error != nil {
                    log.error("Error: no products loaded!")
                } else {
                    self.recentlyOrderedProducts = products
                    self.adsList = adsList
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
    
    override internal func updateScreen() {
        super.updateScreen()
        //if there is no ad & recent product is available, then Hide Ad section
        if adsList.adsDetailList.count == 0 {
            recentOrdersHeightConstraint.constant = noRecordDefaultHeight
            downArrowTopSpaceConstraint.constant = downArrowWithotAdTopSpace
        } else {
            let width = UIScreen.mainScreen().bounds.width
            recentOrdersHeightConstraint.constant = width * Constants.adImageAspectRatio
            
            downArrowTopSpaceConstraint.constant = downArrowNormalTopSpace
            self.view.layoutIfNeeded()
            recentOrdersCarouselViewController?.updateRecentlyOrderedProducts(adsList)
        }
        
        collectionView.reloadData()
        updateRecentOrder()
        if featuredProducts.count == 0 {
            featureProductNoContent.hidden = false
        } else {
            featureProductNoContent.hidden = true
        }
    }
    
    override internal func heightForScrollViewContent() -> CGFloat {
        //particular product thumb image height
        let width = UIScreen.mainScreen().bounds.width
        let cellHeight = ((width - 1) / 2 ) / Constants.productThumbAspectRatio
        let featuredProductsCount = featuredProducts.count > 0 ? featuredProducts.count : 1
        //for feature products
        let collectionViewHeight = featuredProducts.count == 0 ? noRecordDefaultHeight : ceil(CGFloat(featuredProductsCount) / 2) * (cellHeight + 1) // +1 for margin between cells
        //for recent orders
        let recentOrderListHeight = recentlyOrderedProducts.count == 0 ? noRecordDefaultHeight : (cellHeight + 40) // "recent order header & recent order image view height"
        //Ads height
        let adsImageViewHeight = adsList.adsDetailList.count == 0 ? noRecordDefaultHeight : (width * Constants.adImageAspectRatio)
        return collectionViewHeight + recentOrderListHeight + adsImageViewHeight + 40 // 40 for "Featured & recentProducts"
    }
    
    
    private func collectionCellSize() -> CGSize {
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
    }
    
    @IBAction func openStoresScreen() {
        trackButtonPressWithName("Stores")
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ProductDetailSegue" {
            let productList: ProductList!
            let index: Int!
            
            let nc = segue.destinationViewController as! UINavigationController
            let vc = nc.viewControllers[0] as! ProductDetailViewController
            var isAd = false
            
            if let rocVC = sender as? RecentOrdersCarouselViewController {
                isAd = true
                productList = recentlyOrderedProducts
                index = rocVC.currentIndex
                vc.productList = ProductService.getParticularProduct(adsList.adsDetailList[index].productId)
                
                vc.currentProductIndex = 0
            } else if let imageView = sender as? UIImageView {
                productList = recentlyOrderedProducts
                index = imageView.tag
                vc.productList = productList
                vc.currentProductIndex = index
            } else {
                productList = featuredProducts
                index = (sender as! FeaturedProductCollectionViewCell).productIndex
                vc.productList = productList
                vc.currentProductIndex = index
            }
            
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true && !isAd)
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
        } else if segue.identifier == "AdDetailSegue" {
            let nc = segue.destinationViewController as! UINavigationController
            let vc = nc.viewControllers[0] as! WebViewController
        
            if let rocVC = sender as? RecentOrdersCarouselViewController {
                let index = rocVC.currentIndex
                vc.linkToLoad = adsList.adsDetailList[index].producturl
                vc.isAdPresented = true
                vc.title = adsList.adsDetailList[index].adName
            }
        }
    }
    
    func recentOrdersCarouselViewControllerDidGetTapped(vc: RecentOrdersCarouselViewController) {
        let index = vc.currentIndex
        let product = ProductService.getParticularProduct(adsList.adsDetailList[index].productId)
        
        if adsList.adsDetailList[index].productId != 0 {
            //if the product is not available in the store, then don't show Product detail
            if product.count == 0 || product[0].storeMenuProduct == nil {
                return
            }
            self.performSegueWithIdentifier("ProductDetailSegue", sender: vc)
        } else if adsList.adsDetailList[index].producturl != "" {
            //check the string is a valid url
            if NSURL(string: adsList.adsDetailList[index].producturl)  != nil {
                self.performSegueWithIdentifier("AdDetailSegue", sender: vc)
            }
        }
        
    }
    
    @IBAction func getDirection(){
        if let store=CurrentStoreService.sharedInstance.currentStore{
            let location = CLLocation(latitude: store.latitude, longitude: store.longitude)
            MapsManager.openMapsWithDirectionsToLocation(location, name: store.name)
        }
    }
    
    func updateRecentOrder() {
        //if recent order is not available hide the view
        if recentlyOrderedProducts.count == 0 {
            recentOrderImageView.hidden = true
            recentOrderImageHeightConstraint.constant = noRecordDefaultHeight
            self.view.layoutIfNeeded()
        }
        //if recent order count is min 1 then show the first recent order
        if recentlyOrderedProducts.count >= 1 {
            recentOrderImageHeightConstraint.constant = (((UIScreen.mainScreen().bounds.width - 1) / 2 ) / Constants.productThumbAspectRatio) + 40 //40 for recent order header
            self.view.layoutIfNeeded()
            recentOrderImageView.hidden = false
            recentOrderLabel1.hidden = false
            recentOrderImage1.hidden = false
            recentOrderLabel2.hidden = true
            recentOrderImage2.hidden = true
            recentOrderLabel1.text = recentlyOrderedProducts[0].name
            recentOrderShadeImage2.hidden = true
            if let url = NSURL(string: recentlyOrderedProducts[0].thumbImageUrl) {
                recentOrderImage1.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
            }
        }
        //if recent order count is 2 then show the second recent order
        if recentlyOrderedProducts.count >= 2 {
            if let url = NSURL(string: recentlyOrderedProducts[1].thumbImageUrl) {
                recentOrderImage2.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
            }
            recentOrderLabel2.hidden = false
            recentOrderImage2.hidden = false
            recentOrderShadeImage2.hidden = false
            recentOrderLabel2.text = recentlyOrderedProducts[1].name
        }
    }
    
    //show product list when tapping recent order
    @IBAction func showProductList(sender:UITapGestureRecognizer) {
        self.performSegueWithIdentifier("ProductDetailSegue", sender: sender.view)
    }
    
}
