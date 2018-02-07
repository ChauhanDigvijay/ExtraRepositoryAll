//
//  PreferredStoreViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class PreferredStoreViewController: UIViewController {

    var store: Store!
    
    override func viewDidLoad() {
        // Notification for preferred store changes
         NotificationCenter.default.addObserver(self, selector: #selector(PreferredStoreViewController.userPreferredStoreChanged), name: NSNotification.Name(rawValue: JambaNotification.PreferredStoreChanged.rawValue), object: nil)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "EmbeddedSegueToTableViewController" {
            let tableViewController = segue.destination as! StoreDetailTableViewController
            tableViewController.store = store
        }
        if segue.identifier == "StoreLocatorSegue"{
            let nc = segue.destination as! UINavigationController
            let storeLocatorViewController = nc.viewControllers[0] as! StoreLocatorViewController
            storeLocatorViewController.changePreferredStoreProfileScreen = true
        }
    }
    
    // User preferred store changed dismiss screen
    func userPreferredStoreChanged(){
        self.dismiss(animated: false) {
            self.popToRootViewController()
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }

}
