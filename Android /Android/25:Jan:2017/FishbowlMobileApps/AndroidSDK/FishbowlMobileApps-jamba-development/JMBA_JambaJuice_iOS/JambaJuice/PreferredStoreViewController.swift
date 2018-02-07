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
         NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(PreferredStoreViewController.userPreferredStoreChanged), name: JambaNotification.PreferredStoreChanged.rawValue, object: nil)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "EmbeddedSegueToTableViewController" {
            let tableViewController = segue.destinationViewController as! StoreDetailTableViewController
            tableViewController.store = store
        }
        if segue.identifier == "StoreLocatorSegue"{
            let nc = segue.destinationViewController as! UINavigationController
            let storeLocatorViewController = nc.viewControllers[0] as! StoreLocatorViewController
            storeLocatorViewController.changePreferredStoreProfileScreen = true
        }
    }
    
    // User preferred store changed dismiss screen
    func userPreferredStoreChanged(){
        self.dismissViewControllerAnimated(false) {
            self.popToRootViewController()
        }
    }
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }

}
