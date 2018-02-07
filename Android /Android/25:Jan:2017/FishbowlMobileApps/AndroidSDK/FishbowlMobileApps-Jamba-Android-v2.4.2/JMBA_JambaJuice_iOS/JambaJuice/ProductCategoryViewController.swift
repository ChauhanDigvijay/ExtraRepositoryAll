//
//  ProductCategoryViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//
//  Displays an entire product category
//  User can swipe left and right to navigate to the
//  previous and next categories, respectively.

import UIKit
import CoreLocation
import AlamofireImage

class ProductCategoryViewController: ScrollableModalViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
    @IBOutlet weak var heroView: UIView!
    @IBOutlet weak var categoryImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var taglineLabel: UILabel!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var storeAddressLabel: UILabel!
    
    //for store name width constraint
    //    @IBOutlet weak var storeNameView: UIView!
    //    @IBOutlet weak var storeNameWidth: NSLayoutConstraint!;
    //    @IBOutlet weak var storeAddressWidth: NSLayoutConstraint!;
    
    var productCategories: ProductCategoryList = []
    var currentCategoryIndex = 0
    fileprivate var category: ProductCategory {
        get {
            return productCategories[currentCategoryIndex]
        }
    }
    
    fileprivate var products: ProductList = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        scrollViewBackgroundColor = UIColor.clear // UIColor(hex: Constants.jambaVeryLightGrayColor)
        
        navigationController?.isNavigationBarHidden = true
        configureNavigationBar(.lightBlue)
        title = category.name.capitalized
        
        nameLabel.text = category.name.capitalized
        taglineLabel.text = category.desc
        if let url = URL(string: category.imageUrl) {
            categoryImageView.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.categoryImageView.image = image.value
            })
        }
        
        // Store Header text
        storeNameLabel.text=CurrentStoreService.sharedInstance.currentStore?.name
        storeAddressLabel.text=CurrentStoreService.sharedInstance.currentStore?.address
        
        //        let width = UIScreen.mainScreen().bounds.width;
        //        storeNameWidth.constant = width - 180;
        //        storeAddressWidth.constant = width - 180;
        
        loadCurrentCategory()
        
        // Adjust collection view item size to fit two columns
        collectionView.collectionViewLayout.invalidateLayout()
        if #available(iOS 11.0, *) {
            scrollView?.contentInsetAdjustmentBehavior = .never
        }
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        AnalyticsService.trackEvent("view", action: "category_view", label: category.name)
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
    
    fileprivate func loadCurrentCategory() {
        
        self.products=category.products
        
        self.updateScreen()
        /* log.verbose("Current Category: \(self.currentCategoryIndex)")
         ProductService.productsForCategory(category) { (products, error) in
         UIApplication.inMainThread {
         self.products = products
         self.updateScreen()
         }
         }*/
    }
    
    override internal func updateScreen() {
        super.updateScreen()
        collectionView.reloadData()
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
    
    
    //    override internal func heightForScrollViewContent() -> CGFloat {
    //        let rows = ceil(CGFloat(products.count) / 2)
    //        let collectionViewHeight = rows * (collectionCellSize().height + 1) // +1 for margin between cells
    //        return collectionViewHeight + heroView.frame.size.height
    //    }
    //
    //    private func collectionCellSize() -> CGSize {
    //        let width = (collectionView.frame.size.width - 1) / 2 // 2 columns
    //        let height = width / Constants.productThumbAspectRatio
    //        return CGSizeMake(width, height)
    //    }
    
    override internal func heightForScrollViewContent() -> CGFloat {
        let rows = ceil(CGFloat(products.count) / 2)
        let width = (UIScreen.main.bounds.width - 1) / 2
        let height = width / Constants.productThumbAspectRatio
        let collectionViewHeight = rows * (height + 1) // +1 for margin between cells
        return collectionViewHeight + heroView.frame.size.height
    }
    
    fileprivate func collectionCellSize() -> CGSize {
        let width = (UIScreen.main.bounds.width - 1) / 2 // 2 columns
        let height = width / Constants.productThumbAspectRatio
        return CGSize(width: width, height: height)
    }
    
    // Swipe right, load previous category
    @IBAction func prevCategory(_ sender: UIGestureRecognizer) {
        trackGesture(sender)
        if currentCategoryIndex <= 0 {
            return
        }
        
        // Check if we need to inject previous category controller
        if navigationController?.viewControllers.count == 1 {
            let vc = UIViewController.instantiate("ProductCategoryViewController", storyboard: "Main") as! ProductCategoryViewController
            vc.productCategories = productCategories
            vc.currentCategoryIndex = currentCategoryIndex - 1
            var controllers = navigationController!.viewControllers
            controllers.insert(vc, at: controllers.count - 1)
            navigationController?.setViewControllers(controllers, animated: false)
        }
        
        _ = navigationController?.popViewController(animated: true)
        trackScreenEvent("swipe_prev_category", label: category.name)
    }
    
    // Swipe left, load next category
    @IBAction func nextCategory(_ sender: UIGestureRecognizer) {
        trackGesture(sender)
        if currentCategoryIndex >= productCategories.count - 1 {
            return
        }
        
        // Add next category
        let vc = UIViewController.instantiate("ProductCategoryViewController", storyboard: "Main") as! ProductCategoryViewController
        vc.currentCategoryIndex = currentCategoryIndex + 1
        vc.productCategories = productCategories
        navigationController?.pushViewController(vc, animated: true)
        trackScreenEvent("swipe_next_category", label: category.name)
    }
    
    // MARK: UICollectionViewDatasource
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return collectionCellSize()
    }
    
    
    // MARK: - Collection View data source
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return products.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ProductCollectionViewCell", for: indexPath) as! ProductCollectionViewCell
        cell.productIndex = indexPath.row
        cell.update(products[indexPath.row])
        return cell
    }
    
    
    // MARK: ScrollViewDelegate
    
    override func scrollViewDidScroll(_ scrollView: UIScrollView) {
        super.scrollViewDidScroll(scrollView)
        
        // Show navigation bar when scrolling down the list
        if scrollView.contentOffset.y > Constants.navigationBarHiddenThreshold {
            navigationController?.isNavigationBarHidden = false
        } else {
            navigationController?.isNavigationBarHidden = true
        }
    }
    
    
    // MARK: Navigation
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ProductDetailSegue" {
            let index = (sender as! ProductCollectionViewCell).productIndex
            let nc = segue.destination as! UINavigationController
            let vc = nc.viewControllers[0] as! ProductDetailViewController
            vc.productList = products
            vc.currentProductIndex = index
            
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
