//
//  ShowOfferStoreViewController.swift
//  JambaJuice
//
//  Created by VT016 on 21/02/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
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


class ShowOfferStoreViewController:UIViewController {
    
    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var searchBar:UISearchBar!
    @IBOutlet weak var tableViewTopConstraint:NSLayoutConstraint!
    
    var offerDetail:FishbowlOffer?
    var offerDetailArray:[FishbowlOfferStore]        = []
    var offerDetailTableArray:[FishbowlOfferStore]   = []
    
    override func viewDidLoad() {
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        
        //show/hide searchbar
        if offerDetail != nil && offerDetail?.storeRestriction.count > 10 {
            tableViewTopConstraint.constant = 44    //show search bar
        } else {
            tableViewTopConstraint.constant = 0     //hide search bar
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if offerDetail != nil && offerDetail?.storeRestriction.count > 0 {
            getOloRestaurent()
        }
    }
    
    
    //get olo restaurent current store service instance (or) get from Api & update in instance
    func getOloRestaurent() {
        if CurrentStoreService.sharedInstance.oloRestaurentList.count == 0 {
            SVProgressHUD.show(withStatus: "Getting Store Information...")
            SVProgressHUD.setDefaultMaskType(.clear)
            CurrentStoreService.sharedInstance.getOloRestaurentsFromApi({ (error) in
                SVProgressHUD.dismiss()
                if error != nil {
                    self.presentConfirmation("Error", message:"Unable to get store information, please try again.", buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.getOloRestaurent()
                            return
                        }else{
                            return
                        }
                    })
                } else {
                    self.mapStoreDataWithStoreRestriction()
                }
            })
        } else {
            mapStoreDataWithStoreRestriction()
        }
        
    }
    
    //map olo store info with the corresponding store code
    func mapStoreDataWithStoreRestriction() {
        offerDetailArray = []
        for i in 0..<offerDetail!.storeRestriction.count {
            var storeAddress = ""
            var storeName = ""
            if let storeDetails = FishbowlApiClassService.sharedInstance.getOloStoreDetails(offerDetail!.storeRestriction[i].storecode ?? "", oloRestaurent: CurrentStoreService.sharedInstance.oloRestaurentList) {
                
                storeName = (storeDetails.name as NSString).replacingOccurrences(of: "Jamba Juice ", with: "")
                storeAddress = "\(storeDetails.city) \(storeDetails.state)"
            }
            let offerStore:FishbowlOfferStore = FishbowlOfferStore(storename: storeName, displayname: offerDetail!.storeRestriction[i].displayname, storecode: offerDetail!.storeRestriction[i].storecode, storeid: offerDetail!.storeRestriction[i].storeid, storeAddress: storeAddress)
            offerDetailArray.append(offerStore)
        }
        offerDetailTableArray = offerDetailArray
        tableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return offerDetailTableArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAtIndexPath indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ShowOfferStoreTableViewCell") as! ShowOfferStoreTableViewCell
        cell.storeName.text = offerDetailTableArray[indexPath.row].storename
        cell.storeAddress.text = offerDetailTableArray[indexPath.row].storeAddress
        return cell
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchBar.showsCancelButton = false
        tableView.reloadData()
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        if (searchBar.text != nil) {
            searchBar.resignFirstResponder()
            offerDetailTableArray = offerDetailArray.filter({ ($0.storename!.lowercased().contains(searchBar.text!.lowercased()))})
            tableView.reloadData()
        }
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
        offerDetailTableArray = offerDetailArray
        tableView.reloadData()
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        if searchBar.text == "" {
            offerDetailTableArray = offerDetailArray
            tableView.reloadData()
        }
    }
    
}
