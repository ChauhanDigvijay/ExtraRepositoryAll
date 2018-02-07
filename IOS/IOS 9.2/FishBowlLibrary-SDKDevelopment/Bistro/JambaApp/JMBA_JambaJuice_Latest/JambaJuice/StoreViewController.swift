//
//  StoreViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/26/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation

class StoreViewController: UIViewController {
    
    var store: Store!
    
    override func viewDidAppear(animated: Bool) {
        // Notification for basket transfer and cancel store change action
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(StoreViewController.dismiss), name: JambaNotification.BasketTransferredForGuest.rawValue, object: nil)
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(StoreViewController.dismiss), name: JambaNotification.BasketTransferredForUser.rawValue, object: nil)
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(StoreViewController.dismiss), name: JambaNotification.CancelStoreChange.rawValue, object: nil)
        
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "EmbeddedSegueToTableViewController" {
            let tableViewController = segue.destinationViewController as! StoreDetailTableViewController
            tableViewController.store = store
        }
            // Navigate to store locator screen
        else if segue.identifier == "StoreLocatorSegue" {
            let nc = segue.destinationViewController as! UINavigationController
            let vc = nc.viewControllers[0] as! StoreLocatorViewController
            vc.basketTransfer = true
            vc.storeSearchTypeOrderAhead = true
        }
    }
    
    @IBAction func changeStore(sender:UIButton){
        self.performSegueWithIdentifier("StoreLocatorSegue", sender: self)
    }
    
    // Dismiss basket transfer and change store for re order
    func dismiss(){
        self.popViewController()
    }
    
    /// Only called when app closes
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    
    
}
