//
//  OrderStarter.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/23/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import SVProgressHUD

struct OrderStarter {
    
    static func startOrder(store: Store, fromViewController: UIViewController, callback: (success: Bool) -> Void) {
        // Check if store supports order ahead
        if store.supportsOrderAhead == false {
            fromViewController.presentOkAlert("Oops", message: "This store does not support order ahead. Please choose a different store.")
            callback(success: false)
            return
        }
        
        // Send request to start new order
        SVProgressHUD.showWithStatus("Starting new order...", maskType: .Clear)
        StoreService.startNewOrder(store) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                fromViewController.presentError(error)
                callback(success: false)
                return
            }
            callback(success: true)
            AnalyticsService.trackEvent("stores", action: "start_new_order", label: store.name)
            if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
            {
                productName = store.name;
                productID = Int64(store.id!)
                isAppEvent = true
                clpAnalyticsService.sharedInstance.clpTrackScreenView("START_NEW_ORDER");
            }
            
        }
    }
    
}
