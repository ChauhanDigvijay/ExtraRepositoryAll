//
//  ProductMenuViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/15/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import AlamofireImage
import CoreLocation
import SVProgressHUD


class ProductMenuViewController: ScrollableModalViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, RecentOrdersCarouselViewControllerDelegate {
    
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
    
    
    fileprivate var recentlyOrderedProducts: ProductList = []
    fileprivate var featuredProducts: ProductList = []
    fileprivate var adsList: AdProductsList = AdClass.init()
    fileprivate var productCategoryId = ""
    
    let noRecordDefaultHeight:CGFloat = 0
    let downArrowNormalTopSpace:CGFloat = 15
    let downArrowWithotAdTopSpace:CGFloat = 35
    
    fileprivate weak var recentOrdersCarouselViewController: RecentOrdersCarouselViewController?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        topMarginConstraint.constant = Constants.homeScreenNavigationMenuHeight
        scrollViewBackgroundColor = collectionView.backgroundColor!
        recentOrdersView.backgroundColor = collectionView.backgroundColor!
        
        loadFeaturedProducts()
        
        storeNameLabel.text=CurrentStoreService.sharedInstance.currentStore?.name
        storeAddressLabel.text=CurrentStoreService.sharedInstance.currentStore?.address
        
        // Adjust collection view item size to fit two columns
        collectionView.collectionViewLayout.invalidateLayout()
        //Add observer
        NotificationCenter.default.addObserver(self, selector: #selector(ProductMenuViewController.recentlyOrderedProductsUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.RecentlyOrderedProductsUpdated.rawValue), object: nil)
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        
    }
    

    func myMethod(_ controller:UIView)
    {
        
        // need to check
        performSegue(withIdentifier: "OfferDetail", sender: self)
        
    }
    
    
    deinit {
        NotificationCenter.default.removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    @objc func recentlyOrderedProductsUpdated(_ notification: Notification) {
        loadRecentlyOrderedProducts()
    }
    
    func recentOrdersCarouselViewControllerCloseMenu() {
        NonSignedInViewController.sharedInstance().closeModalScreen()
    }
    
    fileprivate func loadFeaturedProducts() {
        self.featuredProducts = CurrentStoreService.sharedInstance.storeBasedFeatureProducts;
        self.loadRecentlyOrderedProducts()
    }
    
    fileprivate func loadRecentlyOrderedProducts() {
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
    
    override func viewDidAppear(_ animated: Bool) {
        
        super.viewDidAppear(animated)
        trackScreenView()
    }
    

    override internal func updateScreen() {
        super.updateScreen()
        //if there is no ad & recent product is available, then Hide Ad section
        if adsList.adsDetailList.count == 0 {
            recentOrdersHeightConstraint.constant = noRecordDefaultHeight
            downArrowTopSpaceConstraint.constant = downArrowWithotAdTopSpace
        } else {
            let width = UIScreen.main.bounds.width
            recentOrdersHeightConstraint.constant = width * Constants.adImageAspectRatio
            
            downArrowTopSpaceConstraint.constant = downArrowNormalTopSpace
            self.view.layoutIfNeeded()
            recentOrdersCarouselViewController?.updateRecentlyOrderedProducts(adsList)
        }
        
        collectionView.reloadData()
        updateRecentOrder()
        if featuredProducts.count == 0 {
            featureProductNoContent.isHidden = false
        } else {
            featureProductNoContent.isHidden = true
        }
    }
    
    override internal func heightForScrollViewContent() -> CGFloat {
        //particular product thumb image height
        let width = UIScreen.main.bounds.width
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
    
    
    fileprivate func collectionCellSize() -> CGSize {
        let width = (UIScreen.main.bounds.width - 1) / 2
        let height = width / Constants.productThumbAspectRatio
        return CGSize(width: width, height: height)
    }
    
    // MARK: - Collection View data source
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return featuredProducts.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "FeaturedProductCollectionViewCell", for: indexPath) as! FeaturedProductCollectionViewCell
        cell.productIndex = indexPath.row
        cell.update(featuredProducts[indexPath.row])
        return cell
    }
    
    
    // MARK: - Collection View layout delegate
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return collectionCellSize()
    }
    
    
    // MARK: - Scroll View delegate
    
    override func scrollViewDidScroll(_ scrollView: UIScrollView) {
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
    }
    
    override func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
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
        
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        snapScrollView()
    }
    
    /// Return the closest end to the given point
    fileprivate func closestEnd(_ point: CGFloat, lowEnd: CGFloat, highEnd: CGFloat) -> CGFloat {
        return abs(point - lowEnd) < abs(point - highEnd) ? lowEnd : highEnd
    }
    
    /// Animate screen if needed. End position should be always close or open, but not in between
    fileprivate func snapScrollView() {
        if topMarginConstraint.constant == 0 || topMarginConstraint.constant == Constants.homeScreenNavigationMenuHeight {
            return
        }
        
        log.verbose("Before snap animation")
        log.verbose("TopConstraint: \(self.topMarginConstraint.constant)")
        log.verbose("ScrollY: \(self.scrollView.contentOffset.y)")
        
        // Snap scroll view to closest edge (open or close)
        view.layoutIfNeeded()
        topMarginConstraint.constant = closestEnd(topMarginConstraint.constant, lowEnd: 0, highEnd: Constants.homeScreenNavigationMenuHeight)
        UIView.animate(withDuration: 0.25, animations: {
            self.view.layoutIfNeeded()
            log.verbose("After snap animation")
            log.verbose("TopConstraint: \(self.topMarginConstraint.constant)")
            log.verbose("ScrollY: \(self.scrollView.contentOffset.y)")
        }) 
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ProductDetailSegue" {
            let productList: ProductList!
            let index: Int!
            
            let nc = segue.destination as! UINavigationController
            let vc = nc.viewControllers[0] as! ProductDetailViewController
            
            if let rocVC = sender as? RecentOrdersCarouselViewController {
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
                let product = vc.productList[vc.currentProductIndex]
                
                var itemId = ""
                var itemName = ""
                
                if let storeMenuProduct = product.storeMenuProduct{
                    itemId = "\(storeMenuProduct.productId)"
                    itemName = "\(product.name)"
                }
                FishbowlApiClassService.sharedInstance.submitMobileAppEvent(itemId, item_name: itemName, event_name: "FEATURE_PRODUCT_CLICK")
            }
        }
        else if segue.identifier == "RecentOrdersSegue" {
            recentOrdersCarouselViewController = segue.destination as? RecentOrdersCarouselViewController
            recentOrdersCarouselViewController?.delegate = self
        } else if segue.identifier == "AdDetailSegue" {
            let nc = segue.destination as! UINavigationController
            let vc = nc.viewControllers[0] as! WebViewController
        
            if let rocVC = sender as? RecentOrdersCarouselViewController {
                let index = rocVC.currentIndex
                vc.linkToLoad = adsList.adsDetailList[index].producturl
                vc.isAdPresented = true
                vc.title = adsList.adsDetailList[index].adName
            }
        } else if segue.identifier == "ProductCategorySegue"{
            let vc = segue.destination as! ProductCategoryViewController
            vc.currentCategoryIndex = 0
            vc.productCategories = CurrentStoreService.sharedInstance.getCategoryList(productCategoryId) // method for get product category list
        }
    }
    
    func recentOrdersCarouselViewControllerDidGetTapped(_ vc: RecentOrdersCarouselViewController) {
        let index = vc.currentIndex
        let product = ProductService.getParticularProduct(adsList.adsDetailList[index].productId)
        
        if adsList.adsDetailList[index].productId != 0 {
            //if the product is not available in the store, then don't show Product detail
            if product.count == 0 || product[0].storeMenuProduct == nil {
                return
            }
            self.performSegue(withIdentifier: "ProductDetailSegue", sender: vc)
        } else if adsList.adsDetailList[index].producturl != "" {
            //check the string is a valid url
            if URL(string: adsList.adsDetailList[index].producturl)  != nil {
                self.performSegue(withIdentifier: "AdDetailSegue", sender: vc)
            }
        } else if adsList.adsDetailList[index].categoryId != "" {
            productCategoryId = adsList.adsDetailList[index].categoryId
            let categoryList=CurrentStoreService.sharedInstance.getCategoryList(productCategoryId) // method for get product category list
            if categoryList.count > 0{
                self.performSegue(withIdentifier: "ProductCategorySegue", sender: vc)
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
            recentOrderImageView.isHidden = true
            recentOrderImageHeightConstraint.constant = noRecordDefaultHeight
            self.view.layoutIfNeeded()
        }
        //if recent order count is min 1 then show the first recent order
        if recentlyOrderedProducts.count >= 1 {
            recentOrderImageHeightConstraint.constant = (((UIScreen.main.bounds.width - 1) / 2 ) / Constants.productThumbAspectRatio) + 40 //40 for recent order header
            self.view.layoutIfNeeded()
            recentOrderImageView.isHidden = false
            recentOrderLabel1.isHidden = false
            recentOrderImage1.isHidden = false
            recentOrderLabel2.isHidden = true
            recentOrderImage2.isHidden = true
            recentOrderLabel1.text = recentlyOrderedProducts[0].name
            recentOrderShadeImage2.isHidden = true
            if let url = URL(string: recentlyOrderedProducts[0].thumbImageUrl) {
                recentOrderImage1.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                    self.recentOrderImage1.image = image.value
                })
            }
        }
        //if recent order count is 2 then show the second recent order
        if recentlyOrderedProducts.count >= 2 {
            if let url = URL(string: recentlyOrderedProducts[1].thumbImageUrl) {
                recentOrderImage2.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                    self.recentOrderImage2.image = image.value
                })
            }
            recentOrderLabel2.isHidden = false
            recentOrderImage2.isHidden = false
            recentOrderShadeImage2.isHidden = false
            recentOrderLabel2.text = recentlyOrderedProducts[1].name
        }
    }
    
    //show product list when tapping recent order
    @IBAction func showProductList(_ sender:UITapGestureRecognizer) {
        self.performSegue(withIdentifier: "ProductDetailSegue", sender: sender.view)
    }
    
}
