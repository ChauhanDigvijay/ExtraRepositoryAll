//
//  StoreConfigurationViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/9/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class StoreConfigurationViewController: UIViewController {

    @IBOutlet var storeCodeButton: UIButton!
    @IBOutlet var storeNameLabel: UILabel!
    @IBOutlet var storeAddressLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        storeCodeButton.setTitle("", forState: .Normal)
        updateStoreInformation()

        if let storeCode = SettingsManager.setting(.StoreCode) as? String {
            loadStore(storeCode) {
                let skipConfiguration = SettingsManager.setting(.SkipStoreConfiguration) as? Bool ?? true
                if skipConfiguration {
                    self.performSegueWithIdentifier("SetupComplete", sender: nil)
                }
            }
        }
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    private func loadStore(storeCode: String, complete: (Void -> Void)? = nil) {
        storeCodeButton.setTitle(storeCode, forState: .Normal)
        if storeCode.trim().isEmpty {
            return // Nothing to do
        }
        SVProgressHUD.showWithStatus("Loading Store \(storeCode)...", maskType: .Black)
        StoreService.sharedInstance.loadStoreWithCode(storeCode) { (store, error) -> Void in
            SVProgressHUD.dismiss()
            self.presentError(error)
            self.updateStoreInformation()
            if error == nil {
                complete?()
            }
        }
    }

    private func updateStoreInformation() {
        let store = StoreService.sharedInstance.currentStore
        storeNameLabel.text = store?.name
        storeAddressLabel.text = store?.address ?? "Please enter the four-digit code of the current store"
        if store != nil {
            storeCodeButton.setTitle(store!.storeCode, forState: .Normal)
        }
    }


    // MARK: - Navigation

    @IBAction func selectStore(sender: UIButton) {
        trackButtonPressWithName("SelectStore")
        performSegueWithIdentifier("NumberPad", sender: nil)
    }

    @IBAction func finishConfiguration(sender: UIButton) {
        trackButtonPress(sender)
        guard let store = StoreService.sharedInstance.currentStore else {
            presentOkAlert("Error", message: "Please select a valid store.")
            return
        }
        if store.supportsOrderAhead == false {
            presentOkAlert("Error", message: "The selected store does not support Order Ahead. Please select a different store.")
            return
        }

        SettingsManager.setSetting(.StoreCode, value: store.storeCode)
        performSegueWithIdentifier("SetupComplete", sender: nil)
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "NumberPad" {
            let vc = segue.destinationViewController as? NumberPadViewController
            vc?.title = "Enter Store Code"
            vc?.maxLength = 4
            vc?.initialValue = StoreService.sharedInstance.currentStore?.storeCode ?? "0000"
            vc?.completeCallback = { enteredNumber in
                if let storeCode = enteredNumber {
                    self.loadStore(storeCode)
                } else {
                    self.updateStoreInformation()
                }
            }
        }
    }

}
