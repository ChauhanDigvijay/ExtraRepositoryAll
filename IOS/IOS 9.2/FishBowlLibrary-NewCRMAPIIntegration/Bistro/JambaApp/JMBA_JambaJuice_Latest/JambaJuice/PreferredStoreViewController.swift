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

}
