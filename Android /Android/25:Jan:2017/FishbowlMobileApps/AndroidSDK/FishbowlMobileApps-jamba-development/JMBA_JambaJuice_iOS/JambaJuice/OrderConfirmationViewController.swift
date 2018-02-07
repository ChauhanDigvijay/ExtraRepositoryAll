//
//  OrderConfirmationViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 04/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import MapKit
import HDK

class OrderConfirmationViewController: UIViewController {

    // Hold current instance in memory until ready to close
    private static var currentInstance: OrderConfirmationViewController?
    
    private var orderStatus: OrderStatus! {
        get {
            return BasketService.lastOrderStatus!
        }
    }
    
    @IBOutlet weak var readyTimeLabel: UILabel!
    @IBOutlet weak var nameAndAddressLabel: UILabel!
    @IBOutlet weak var getDirectionsView: UIView!
    @IBOutlet weak var callStoreView: UIView!
    @IBOutlet weak var phoneNumberLabel: UILabel!
    @IBOutlet weak var becomeJambaInsiderView: UIView!

    override func viewDidLoad() {
        super.viewDidLoad()
        StatusBarStyleManager.pushStyle(.Default, viewController: self)
        OrderConfirmationViewController.currentInstance = self
        loadUI()
        
        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
        {
        
        isAppEvent = true;
        productName = String(format: "$%.2f", TotalModifierCost)
        clpAnalyticsService.sharedInstance.clpTrackScreenView("CheckoutWithModifiers");
        }

    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    private func loadUI() {
        becomeJambaInsiderView.hidden = UserService.sharedUser != nil
        if orderStatus.readyTime != nil {
            let labelTimeText = orderStatus.readyTime!.timeString()
            let tomorrowString = orderStatus.readyTime!.isTomorrowInGregorianCalendar() ? "tomorrow" : "today"
            readyTimeLabel.text = "Your order will be ready at \(labelTimeText) \(tomorrowString). Head straight to the pick up counter in store and Skip the Line®!"
        } else {
            log.warning("No pickup time on order confirmation!!")
            readyTimeLabel.text = "When your order is ready, head straight to the pick up counter in store and Skip the Line®!"
        }
        if orderStatus.store == nil {
            log.error("Store missing from order status")
            nameAndAddressLabel.text = ""
            getDirectionsView.hidden = true
            callStoreView.hidden = true
            return
        }

        nameAndAddressLabel.text = "\(orderStatus.store!.name)\n\(orderStatus.store!.addressAndDistance)"
        phoneNumberLabel.text = orderStatus.store!.phone.prettyFormattedPhoneNumber()
        
        // If phone is empty or null, hide Call button
        if orderStatus.store!.phone.trim().isEmpty || orderStatus.store!.phone == "null" {
            callStoreView.hidden = true
        }
    }

    private func close() {
        view.removeFromSuperview()
        OrderConfirmationViewController.currentInstance = nil
        BasketService.lastOrderStatus = nil
    }
    
    @IBAction func dismiss(sender: UIButton) {
        trackButtonPress(sender)
        close()
    }

    @IBAction func getDirections(sender: AnyObject) {
        let location = CLLocation(latitude: orderStatus.store!.latitude, longitude: orderStatus.store!.longitude)
        MapsManager.openMapsWithDirectionsToLocation(location, name: orderStatus.store!.name)
        AnalyticsService.trackEvent("stores", action: "get_directions", label: orderStatus.store!.name)
    }
    
    @IBAction func makeCall(sender: AnyObject) {
        CallManager.call(orderStatus.store!.phone, presentingViewController: self)
        AnalyticsService.trackEvent("stores", action: "call_store", label: orderStatus.store!.name)
    }

    @IBAction func becomeJambaInsider(sender: AnyObject) {
        close()
        NonSignedInViewController.sharedInstance().openSignUpScreen();

    }
    
}
