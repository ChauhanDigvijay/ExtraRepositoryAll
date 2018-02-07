//
//  SelectStoreViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/5/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol SelectStoreViewControllerDelegate: class {
    func selectStoreViewControllerUseDifferentStore()
    func selectStoreViewControllerUsePreferredStore()
}

class SelectStoreViewController: UIViewController {

    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var storeAddressLabel: UILabel!
    
    weak var delegate: SelectStoreViewControllerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        storeNameLabel.text = UserService.sharedUser!.favoriteStore?.name
        storeAddressLabel.text = UserService.sharedUser!.favoriteStore?.addressAndDistance
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    @IBAction func useDifferentStore(_ sender: AnyObject) {
        dismissModalController()
        delegate?.selectStoreViewControllerUseDifferentStore()
    }
    
    @IBAction func usePreferredStore(_ sender: AnyObject) {
        dismissModalController()
        delegate?.selectStoreViewControllerUsePreferredStore()
    }
    
}
