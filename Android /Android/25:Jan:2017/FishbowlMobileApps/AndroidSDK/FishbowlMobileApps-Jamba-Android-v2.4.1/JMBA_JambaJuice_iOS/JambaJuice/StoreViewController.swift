//
//  StoreViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/26/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SVProgressHUD

class StoreViewController: UIViewController {
    
    var store: Store!
    
    var myOrderViewController:Bool = false

    @IBOutlet weak var changeStore:UIButton!
    @IBOutlet weak var makePreferredStore: UIButton!
    @IBOutlet weak var changeStoreButtonHeight: NSLayoutConstraint!
    
    @IBOutlet weak var makePreferredStoreButtonHeight: NSLayoutConstraint!
    
    override func viewDidAppear(_ animated: Bool) {
        // Notification for basket transfer and cancel store change action
        NotificationCenter.default.addObserver(self, selector: #selector(StoreViewController.dismissView), name: NSNotification.Name(rawValue: JambaNotification.BasketTransferredForGuest.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(StoreViewController.dismissView), name: NSNotification.Name(rawValue: JambaNotification.BasketTransferredForUser.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(StoreViewController.dismissView), name: NSNotification.Name(rawValue: JambaNotification.CancelStoreChange.rawValue), object: nil)
        
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func viewDidLoad() {
        self.navigationItem.title = store!.name
        if myOrderViewController == true{
            self.changeStore.isHidden = true
            self.changeStoreButtonHeight.constant = 0
            self.validateMakePreferredStoreButton()
        }else{
            self.changeStore.isHidden = false
            self.changeStoreButtonHeight.constant = 50
            makePreferredStore.isHidden = true
            makePreferredStoreButtonHeight.constant = 0
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "EmbeddedSegueToTableViewController" {
            let tableViewController = segue.destination as! StoreDetailTableViewController
            tableViewController.store = store
        }
            // Navigate to store locator screen
        else if segue.identifier == "StoreLocatorSegue" {
            let nc = segue.destination as! UINavigationController
            let vc = nc.viewControllers[0] as! StoreLocatorViewController
            vc.basketTransfer = true
            vc.storeSearchTypeOrderAhead = true
        }
    }
    
    @IBAction func changeStore(_ sender:UIButton){
        self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
    }
    @IBAction func changePreferredStore(_ sender: UIButton) {
        if UserService.sharedUser == nil {
            return
        }
        SVProgressHUD.show(withStatus: "Saving changes...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.updateFavoriteStore(store) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }else{
                self.presentOkAlert("Preferred Store", message: "\(self.store.name) has been saved as your preferred store", callback: {
                    self.validateMakePreferredStoreButton()
                })
            }
        }
    }
    
    
    // Dismiss basket transfer and change store for re order
    func dismissView(){
        self.popViewController()
    }
    
    /// Only called when app closes
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    // Make preferred store button would be hidden if store equals favourite store.
    func validateMakePreferredStoreButton(){
        if UserService.sharedUser!.favoriteStore == store{
            makePreferredStore.isHidden = true
            makePreferredStoreButtonHeight.constant = 0
        }else{
            makePreferredStore.isHidden = false
            makePreferredStoreButtonHeight.constant = 50
        }
    }
    
}
