//
//  SearchProductViewController.swift
//  JambaJuice
//
//  Created by VT010 on 10/17/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit

class SearchProductViewController: UIViewController,UITableViewDelegate, UITableViewDataSource,UISearchBarDelegate{
    
    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var searchBar:UISearchBar!
    @IBOutlet weak var scrollView:UIScrollView!
    
    @IBOutlet weak var topMarginConstraint: NSLayoutConstraint!
    @IBOutlet weak var jambaLogo: UIImageView!
    @IBOutlet weak var downArrow: UIImageView!
    
    var topMarginMinimum: CGFloat = 20
    var searchProductResults: SearchResultProducts = []
    var selectedProductIndex = 0
    
    override func viewDidLoad() {
        searchProduct()
        super.viewDidLoad()
        
        topMarginConstraint.constant = Constants.homeScreenNavigationMenuHeight
        trackScreenView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        trackScreenView()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if searchProductResults.count == 0{
            return 2
        }
        else{
            return searchProductResults.count + 1
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if searchProductResults.count == 0{
            if indexPath.row == 0{
                 return tableView.dequeueReusableCell(withIdentifier: "SearchProductNoResultsTableViewCell")!
            }
            else{
                 return tableView.dequeueReusableCell(withIdentifier: "ParticipantStoresTableViewCell")!
            }
        }
        else{
            if indexPath.row < searchProductResults.count{
                let cell = tableView.dequeueReusableCell(withIdentifier: "ProductTableViewCell") as! ProductTableViewCell
                cell.setData(searchProductResults[indexPath.row])
                return cell
                
            }
            else{
              return tableView.dequeueReusableCell(withIdentifier: "ParticipantStoresTableViewCell")!
            }
        }
    }
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        view.layoutIfNeeded()
        self.jambaLogo.isHidden=true;
        self.downArrow.isHidden=true;
        topMarginConstraint.constant = topMarginMinimum
        UIView.animate(withDuration: 0.25, animations: {
            self.view.layoutIfNeeded()
        }) 
        searchBar.showsCancelButton = true
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchBar.showsCancelButton = false
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        if (searchBar.text != nil) {
            searchBar.resignFirstResponder()
            AnalyticsService.trackEvent("search", action: "product_search", label: searchBar.text)
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("", item_name: searchBar.text!.lowercased(), event_name: "MENU_SEARCH")
            searchProduct()
        }
    }
    
//    func searchBarBookmarkButtonClicked(searchBar: UISearchBar) {
//        searchBar.resignFirstResponder()
//        searchBar.text = ""
//        searchProduct()
//        
//    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
        searchProduct()
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        if searchBar.text == "" {
            searchProduct()
        }
    }
    
    func searchProduct(){
        searchProductResults = []
        let productFamily = CurrentStoreService.sharedInstance.productTree
        for family in productFamily{
            for category in family.categories{
                for product in category.products{
                    if searchBar.text != nil && searchBar.text?.isEmpty == false{
                        if product.name.lowercased().contains(searchBar.text!.lowercased()){
                            let searchProductResult = ProductWithCategory.init(product: product, category: category)
                            searchProductResults.append(searchProductResult)
                        }
                    }else{
                        let searchProductResult = ProductWithCategory.init(product: product, category: category)
                        searchProductResults.append(searchProductResult)
                    }
                }
            }
        }
        UIView.animate(withDuration: 0.25, animations: {
            self.tableView.alpha = 0.0
        }, completion: { (finished) in
            self.tableView.reloadData()
            self.tableView.alpha = 1.0
        }) 
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if searchProductResults.count == 0{
            if indexPath.row == 0{
                return 90
            }
            else{
                return 120
            }
        }
        else{
            if indexPath.row == searchProductResults.count{
                return 120
            }
            else{
                return 100
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if tableView.cellForRow(at: indexPath) as? ProductTableViewCell != nil{
            selectedProductIndex = indexPath.row
           self.performSegue(withIdentifier: "ProductDetailViewSegue", sender: self)
        }
    
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ProductDetailViewSegue"{
            var productList:[Product] = []
            for productResult in searchProductResults{
                productList.append(productResult.product)
            }
            let nc = segue.destination as! UINavigationController
            let vc = nc.viewControllers[0]  as! ProductDetailViewController
            vc.currentProductIndex = selectedProductIndex
            vc.productList = productList
            vc.screenPresented = false
        }
    }
    // MARK: - ScrollView delegate
    
     func scrollViewDidScroll(_ scrollView: UIScrollView) {
       // super.scrollViewDidScroll(scrollView)
        
        hideShowHeaderIcons()
        
        // Maximize screen when user scrolls to the bottom
        if scrollView.contentOffset.y > 0 && topMarginConstraint.constant > topMarginMinimum {
            topMarginConstraint.constant = max(topMarginMinimum, topMarginConstraint.constant - scrollView.contentOffset.y)
            scrollView.contentOffset.y = 0
        }
            // Snap back to open state
        else if scrollView.contentOffset.y < 0 && topMarginConstraint.constant < Constants.homeScreenNavigationMenuHeight {
            topMarginConstraint.constant = min(Constants.homeScreenNavigationMenuHeight, topMarginConstraint.constant - scrollView.contentOffset.y)
            scrollView.contentOffset.y = 0
        }
    }
    
   func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y < Constants.scrollViewThresholdToDismissModal {
            trackScreenEvent("close_screen", label: "drag_down")
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
        if topMarginConstraint.constant == topMarginMinimum || topMarginConstraint.constant == Constants.homeScreenNavigationMenuHeight {
            return
        }
        
        log.verbose("Before snap animation")
        log.verbose("TopConstraint: \(self.topMarginConstraint.constant)")
        log.verbose("ScrollY: \(self.scrollView.contentOffset.y)")
        
        // Snap scroll view to closest edge (open or close)
        view.layoutIfNeeded()
        topMarginConstraint.constant = closestEnd(topMarginConstraint.constant, lowEnd: topMarginMinimum, highEnd: Constants.homeScreenNavigationMenuHeight)
        
        
        hideShowHeaderIcons()
        UIView.animate(withDuration: 0.25, animations: {
            
            self.view.layoutIfNeeded()
            log.verbose("After snap animation")
            log.verbose("TopConstraint: \(self.topMarginConstraint.constant)")
            log.verbose("ScrollY: \(self.scrollView.contentOffset.y)")
        }) 
    }
    
    @IBAction func close() {
        dismiss(animated: true, completion: nil)
    }
    
    //MARK: show/hide the header icon
    func hideShowHeaderIcons() {
        if (topMarginMinimum + 20 > topMarginConstraint.constant) {
            jambaLogo.isHidden=true;
            downArrow.isHidden=true;
        }
        else if (topMarginConstraint.constant > topMarginMinimum) {
            jambaLogo.isHidden=false;
            downArrow.isHidden=false;
        }
    }
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}
