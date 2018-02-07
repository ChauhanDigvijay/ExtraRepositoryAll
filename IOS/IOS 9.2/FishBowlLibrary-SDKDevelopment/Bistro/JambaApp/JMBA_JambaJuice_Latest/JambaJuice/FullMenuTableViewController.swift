//
//  FullMenuTableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Haneke

class FullMenuTableViewController: UITableViewController {

    typealias FamilyOrCategory = (family: ProductFamily?, category: ProductCategory?)
    private var tableRows = Array<FamilyOrCategory>()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        navigationController?.navigationBarHidden = true
        configureNavigationBar(.LightBlue)
        StatusBarStyleManager.pushStyle(.LightContent, viewController: self)
        title = "Full Menu"
        
        let families = CurrentStoreService.sharedInstance.productTree
        
        for family in families {
            self.tableRows.append(FamilyOrCategory(family: family, category: nil))
            for category in family.categories {
                self.tableRows.append(FamilyOrCategory(family: nil, category: category))
            }
        }
        
        
        // Load families with nested categories
       /* ProductService.productFamilies { (families, error) -> Void in
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
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    private func updateScreen() {
        tableView.reloadData()
    }

    @IBAction func closeScreen(sender: AnyObject) {
        trackButtonPressWithName("swipe-down-button")
        dismissModalController()
    }
    
    // MARK: - Table view data source

    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        let familyOrCategory = tableRows[indexPath.row]
        return familyOrCategory.category == nil ? 148 : 70
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableRows.count
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
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
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
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
                newIndex += 1;
            }
        }
        return newIndex
    }

    // MARK: ScrollView delegate
    
    override func scrollViewDidScroll(scrollView: UIScrollView) {
        // Reveal previous screen only when pulling down, not when pulling up
        if scrollView.contentOffset.y > 0 {
            tableView.backgroundColor = UIColor.whiteColor()
        } else {
            tableView.backgroundColor = UIColor.clearColor()
        }
        
        // Show navigation bar when scrolling down the list
        if scrollView.contentOffset.y > Constants.navigationBarHiddenThreshold {
            navigationController?.navigationBarHidden = false
            StatusBarStyleManager.pushStyle(.Default, viewController: self)
        } else {
            navigationController?.navigationBarHidden = true
            StatusBarStyleManager.pushStyle(.LightContent, viewController: self)
        }
    }

    override func scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y < Constants.scrollViewThresholdToDismissModal {
            trackScreenEvent("close_screen", label: "drag_down")
            dismissModalController()
        }
    }

}
