//
//  FullMenuTableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Haneke
import CoreLocation




class FullMenuViewController: UIViewController,UITableViewDelegate,UITableViewDataSource,ProtocolDelegate {
    
    typealias FamilyOrCategory = (family: ProductFamily?, category: ProductCategory?)
    private var tableRows = Array<FamilyOrCategory>()
    @IBOutlet var tableView : UITableView?;
    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var storeAddressLabel: UILabel!
    //    @IBOutlet var mapIconImageView: UIImageView?;
    
    
    //for store name width constraint
    //    @IBOutlet weak var storeNameView: UIView!
    //    @IBOutlet weak var storeNameWidth: NSLayoutConstraint!;
    //    @IBOutlet weak var storeAddressWidth: NSLayoutConstraint!;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(FullMenuViewController.updateStuff), name: "appDidBecomeActive", object: nil)
        
        
        navigationController?.navigationBarHidden = true
        configureNavigationBar(.LightBlue)
        
        StatusBarStyleManager.pushStyle(.Default, viewController: self)
        //        StatusBarStyleManager.pushStyle(.Default, viewController: self)
        title = "Full Menu"
        
        storeNameLabel.text=CurrentStoreService.sharedInstance.currentStore?.name
        storeAddressLabel.text=CurrentStoreService.sharedInstance.currentStore?.address
        
        //        let width = UIScreen.mainScreen().bounds.width;
        //        storeNameWidth.constant = width - 180;
        //        storeAddressWidth.constant = width - 180;
        
        
        let families = CurrentStoreService.sharedInstance.productTree
        
        for family in families {
            self.tableRows.append(FamilyOrCategory(family: family, category: nil))
            for category in family.categories {
                self.tableRows.append(FamilyOrCategory(family: nil, category: category))
            }
        }
        
        self.updateScreen()
        
        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
        {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("CATEGORY_CLICK");
        }
        
        
        
        
        // Load families with nested categories
        /*      ProductService.productFamilies { (families, error) -> Void in
         UIApplication.inMainThread {
         if error != nil {
         log.error(error?.localizedDescription)
         return
         }
         log.verbose("\(families)")
         
         // Flatten product families and categories into a single array
         for family in families {
         self.tableRows.append(FamilyOrCategory(family: family, category: nil))
         for category in family.categories {
         self.tableRows.append(FamilyOrCategory(family: nil, category: category))
         }
         }
         self.updateScreen()
         }
         }*/
    }
    
    
    
    override func viewWillAppear(animated: Bool) {
        
        self.updateStuff()
        
    }
    
    
    func updateStuff()
    {
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        
        if appdelegate?.isPushOpen == true
        {
            if let tmpView : UIView = self.view.viewWithTag(1001)! as UIView
            {
                tmpView.removeFromSuperview()
                
            }
            
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
                print("open Normal Menu")
                UIApplication.sharedApplication().statusBarHidden = false
                
            }
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
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    private func updateScreen() {
        tableView!.reloadData()
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
    
    // MARK: - Table view data source
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        let familyOrCategory = tableRows[indexPath.row]
        return familyOrCategory.category == nil ? 148 : 70
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableRows.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let familyOrCategory = tableRows[indexPath.row]
        if let family = familyOrCategory.family {
            let cell = tableView.dequeueReusableCellWithIdentifier("ProductFamilyTableViewCell", forIndexPath: indexPath) as! ProductFamilyTableViewCell
            cell.closeButton.hidden = indexPath.row > 0
            cell.nameLabel.text = family.name.capitalizedString
            if let url = NSURL(string: family.imageUrl) {
                cell.backgroundImageView.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
            }
            return cell
        }
        else if let category = familyOrCategory.category {
            let cell = tableView.dequeueReusableCellWithIdentifier("ProductCategoryTableViewCell", forIndexPath: indexPath) as! ProductCategoryTableViewCell
            cell.nameLabel.text = category.name
            cell.taglineLabel.text = category.tagline
            cell.indexPath = indexPath
            cell.separatorLineView.hidden = indexPath.row >= (tableRows.count-1) || tableRows[indexPath.row + 1].family != nil
            return cell
        }
        assert(false, "Should not happen")
        return UITableViewCell()
    }
    
    
    // MARK: Table View delegate
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
    
    // MARK: Navigation
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "CategoryDetailSegue" {
            let indexPath = (sender as! ProductCategoryTableViewCell).indexPath!
            let nc = segue.destinationViewController as! UINavigationController
            let vc = nc.viewControllers[0] as! ProductCategoryViewController
            vc.productCategories = categoryList()
            vc.currentCategoryIndex = currentIndexFromIndexPath(indexPath.row)
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
                let familyOrCategory = tableRows[indexPath.row]
                let category = familyOrCategory.category
                productName = category?.name
                productID = Int64((category?.id)!)
                isAppEvent = true
                clpAnalyticsService.sharedInstance.clpTrackScreenView("SUB_CATEGORY_CLICK");
            }
            
        }
    }
    
    // Return list of all product categories
    private func categoryList() -> ProductCategoryList {
        return tableRows.filter { $0.category != nil }.map { $0.category! }
    }
    
    // Calculate the index of the current category from the Index Path
    private func currentIndexFromIndexPath(index: Int) -> Int {
        var newIndex = -1
        if index < 0 || index > tableRows.count - 1 {
            return newIndex
        }
        for i in 0...index {
            let row = tableRows[i]
            if row.category != nil {
                newIndex += 1
            }
        }
        return newIndex
    }
    
    // MARK: ScrollView delegate
    
    func scrollViewDidScroll(scrollView: UIScrollView) {
        
        tableView!.backgroundColor = UIColor.clearColor()
        
        // Reveal previous screen only when pulling down, not when pulling up
        //        if scrollView.contentOffset.y > 0 {
        //            tableView!.backgroundColor = UIColor.clearColor()
        //        } else {
        //            tableView!.backgroundColor = UIColor.clearColor()
        //        }
        
        //        StatusBarStyleManager.pushStyle(.Default, viewController: self)
        //
        //        // Show navigation bar when scrolling down the list
        if scrollView.contentOffset.y > Constants.navigationBarHiddenThreshold {
            navigationController?.navigationBarHidden = false
            //            StatusBarStyleManager.pushStyle(.Default, viewController: self)
        } else {
            navigationController?.navigationBarHidden = true
            //            StatusBarStyleManager.pushStyle(.LightContent, viewController: self)
        }
    }
    
    func scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y < Constants.scrollViewThresholdToDismissModal {
            trackScreenEvent("close_screen", label: "drag_down")
            dismissModalController()
        }
    }
    
    
    
    
    @IBAction func getDirection(){
        if let store=CurrentStoreService.sharedInstance.currentStore{
            let location = CLLocation(latitude: store.latitude, longitude: store.longitude)
            MapsManager.openMapsWithDirectionsToLocation(location, name: store.name)
        }
    }
    
}
