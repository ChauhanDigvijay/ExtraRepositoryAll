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
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "EmbeddedSegueToTableViewController" {
            let tableViewController = segue.destination as! StoreDetailTableViewController
            tableViewController.store = store
        }
    }


    // MARK: User Actions
    
    @IBAction func startNewOrder(_ sender: UIButton) {
        trackButtonPress(sender)
        sender.isEnabled = false
        OrderStarter.startOrder(store, fromViewController: self) { success in
            sender.isEnabled = true
            if success {
                self.dismissModalController()
            }
        }
    }
    
}
