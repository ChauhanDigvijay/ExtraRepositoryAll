//
//  PreloadMenuViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/24/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class PreloadMenuViewController: UIViewController {

    @IBOutlet var statusLabel: UILabel!

    var store: Store!

    override func viewDidLoad() {
        super.viewDidLoad()
        loadStoreMenu()
    }

    private func loadStoreMenu() {
        statusLabel.text = "Loading store menu..."
        StoreService.sharedInstance.loadStoreMenu { error in
            if error != nil {
                self.presentError(error)
                return
            }
            guard let store = StoreService.sharedInstance.currentStore else {
                self.presentError(NSError(description: "Could not load store"))
                return
            }
            if store.isAvailable == false {
                self.presentError(NSError(description: "Store is not available for Order Ahead."))
                return
            }
            if store.storeMenu?.count ?? 0 == 0 {
                self.presentError(NSError(description: "Store has no products."))
                return
            }
            self.loadProductModifiers()
        }
    }

    private func loadProductModifiers() {
        statusLabel.text = "Loading store menu modifiers..."
        StoreService.sharedInstance.loadModifiersForAllProducts {
            UIApplication.inMainThread {
                self.performSegueWithIdentifier("StoreReadySegue", sender: nil)
            }
        }
    }

    private func presentError(error: NSError) {
        self.presentError(error, callback: {
            self.popToRootViewController()
        })
    }

}
