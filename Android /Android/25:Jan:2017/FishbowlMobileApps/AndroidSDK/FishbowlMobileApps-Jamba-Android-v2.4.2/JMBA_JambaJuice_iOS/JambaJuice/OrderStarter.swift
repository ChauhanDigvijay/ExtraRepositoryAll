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
    
    static func startOrder(_ store: Store, fromViewController: UIViewController, callback: @escaping (_ success: Bool) -> Void) {
        // Check if store supports order ahead
        if store.supportsOrderAhead == false {
            fromViewController.presentOkAlert("Oops", message: "This store does not support order ahead. Please choose a different store.")
            callback(false)
            return
        }
        
        // Send request to start new order
        SVProgressHUD.show(withStatus: "Starting new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        StoreService.startNewOrder(store) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                fromViewController.presentError(error)
                callback(false)
                return
            }
            callback(true)
            AnalyticsService.trackEvent("stores", action: "start_new_order", label: store.name)
        }
    }
    
}
