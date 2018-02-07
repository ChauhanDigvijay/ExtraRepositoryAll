//
//  FullMenuTableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import AlamofireImage

class FullMenuTableViewController: UITableViewController {

    typealias FamilyOrCategory = (family: ProductFamily?, category: ProductCategory?)
    fileprivate var tableRows = Array<FamilyOrCategory>()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        navigationController?.isNavigationBarHidden = true
        configureNavigationBar(.lightBlue)
        
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

    override func viewDidAppear(_ animated: Bool) {
        StatusBarStyleManager.pushStyle(.lightContent, viewController: self)
        super.viewDidAppear(animated)
        trackScreenView()
    }

    fileprivate func updateScreen() {
        tableView.reloadData()
    }

    @IBAction func closeScreen(_ sender: AnyObject) {
        trackButtonPressWithName("swipe-down-button")
        dismissModalController()
    }
    
    // MARK: - Table view data source

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let familyOrCategory = tableRows[indexPath.row]
        return familyOrCategory.category == nil ? 148 : 70
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableRows.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let familyOrCategory = tableRows[indexPath.row]
        if let family = familyOrCategory.family {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ProductFamilyTableViewCell", for: indexPath) as! ProductFamilyTableViewCell
            cell.closeButton.isHidden = indexPath.row > 0
            cell.nameLabel.text = family.name.capitalized
            if let url = URL(string: family.imageUrl) {
                cell.backgroundImageView.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: DispatchQueue.main, imageTransition: UIImageView.ImageTransition.noTransition, runImageTransitionIfCached: false, completion: nil)
            }
            return cell
        }
        else if let category = familyOrCategory.category {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ProductCategoryTableViewCell", for: indexPath) as! ProductCategoryTableViewCell
            cell.nameLabel.text = category.name
            cell.taglineLabel.text = category.tagline
            cell.indexPath = indexPath
            cell.separatorLineView.isHidden = indexPath.row >= (tableRows.count-1) || tableRows[indexPath.row + 1].family != nil
            return cell
        }
        assert(false, "Should not happen")
        return UITableViewCell()
    }

    
    // MARK: Table View delegate
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    
    // MARK: Navigation
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "CategoryDetailSegue" {
            let indexPath = (sender as! ProductCategoryTableViewCell).indexPath!
            let nc = segue.destination as! UINavigationController
            let vc = nc.viewControllers[0] as! ProductCategoryViewController
            vc.productCategories = categoryList()
            vc.currentCategoryIndex = currentIndexFromIndexPath(indexPath.row)
        }
    }

    // Return list of all product categories
    fileprivate func categoryList() -> ProductCategoryList {
        return tableRows.filter { $0.category != nil }.map { $0.category! }
    }
    
    // Calculate the index of the current category from the Index Path
    fileprivate func currentIndexFromIndexPath(_ index: Int) -> Int {
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
    
    override func scrollViewDidScroll(_ scrollView: UIScrollView) {
        // Reveal previous screen only when pulling down, not when pulling up
        if scrollView.contentOffset.y > 0 {
            tableView.backgroundColor = UIColor.white
        } else {
            tableView.backgroundColor = UIColor.clear
        }
        
        // Show navigation bar when scrolling down the list
        if scrollView.contentOffset.y > Constants.navigationBarHiddenThreshold {
            navigationController?.isNavigationBarHidden = false
            StatusBarStyleManager.pushStyle(.default, viewController: self)
        } else {
            navigationController?.isNavigationBarHidden = true
            StatusBarStyleManager.pushStyle(.lightContent, viewController: self)
        }
    }

    override func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y < Constants.scrollViewThresholdToDismissModal {
            trackScreenEvent("close_screen", label: "drag_down")
            dismissModalController()
        }
    }

}
