//
//  SelectPickUpStoreViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/5/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class SelectPickUpStoreViewController: UIViewController {
    
    var store: Store!

    override func viewDidLoad() {
        super.viewDidLoad()
        title = store.name
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
    }


    // MARK: User Actions
    
    @IBAction func startNewOrder(sender: UIButton) {
        trackButtonPress(sender)
        sender.enabled = false
        OrderStarter.startOrder(store, fromViewController: self) { success in
            sender.enabled = true
            if success {
                self.dismissModalController()
            }
        }
    }
    
}
