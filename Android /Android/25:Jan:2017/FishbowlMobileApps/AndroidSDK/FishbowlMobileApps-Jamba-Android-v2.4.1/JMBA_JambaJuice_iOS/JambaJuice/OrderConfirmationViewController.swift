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
    fileprivate static var currentInstance: OrderConfirmationViewController?
    
    fileprivate var orderStatus: OrderStatus! {
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
    @IBOutlet weak var getDirectionViewHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var orderStatusView: UIView!
    
    @IBOutlet weak var orderStatusViewHeighConstraints: NSLayoutConstraint!

    override func viewDidLoad() {
        super.viewDidLoad()
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        OrderConfirmationViewController.currentInstance = self
        loadUI()
        
//        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
//        {
//        
//        isAppEvent = true;
//        productName = String(format: "$%.2f", TotalModifierCost)
//        clpAnalyticsService.sharedInstance.clpTrackScreenView("CheckoutWithModifiers");
//        }

    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    private func loadUI() {
        if UserService.sharedUser != nil{
            becomeJambaInsiderView.isHidden = true
        }else{
            orderStatusView.isHidden = true
            orderStatusViewHeighConstraints.constant = 0
        }
        if orderStatus.deliveryMode == deliveryMode.pickup.rawValue{
            if orderStatus.readyTime != nil {
                let labelTimeText = orderStatus.readyTime!.timeString()
                let tomorrowString = orderStatus.readyTime!.isTomorrowInGregorianCalendar() ? "tomorrow" : "today"
                readyTimeLabel.text = "Your order will be ready at \(labelTimeText) \(tomorrowString). Head straight to the pick up counter in store and Skip the Line®!"
            } else {
                log.warning("No pickup time on order confirmation!!")
                readyTimeLabel.text = "When your order is ready, head straight to the pick up counter in store and Skip the Line®!"
            }
        }else if orderStatus.deliveryMode == deliveryMode.delivery.rawValue{
            getDirectionsView.isHidden = true
            getDirectionViewHeightConstraint.constant = 0
            readyTimeLabel.text = "Your order was placed at \(orderStatus.timePlaced!.timeString()) today. Your order's estimated delivery time is \(Int(BasketService.sharedBasket!.leadTimeEstimateMinutes)) mins."
        }
        
        if orderStatus.store == nil {
            log.error("Store missing from order status")
            nameAndAddressLabel.text = ""
            getDirectionsView.isHidden = true
            callStoreView.isHidden = true
            return
        }

        nameAndAddressLabel.text = "\(orderStatus.store!.name)\n\(orderStatus.store!.addressAndDistance)"
        phoneNumberLabel.text = orderStatus.store!.phone.prettyFormattedPhoneNumber()
        
        // If phone is empty or null, hide Call button
        if orderStatus.store!.phone.trim().isEmpty || orderStatus.store!.phone == "null" {
            callStoreView.isHidden = true
        }
    }

    fileprivate func close() {
        view.removeFromSuperview()
        OrderConfirmationViewController.currentInstance = nil
        BasketService.lastOrderStatus = nil
    }
    
    @IBAction func dismiss(_ sender: UIButton) {
        trackButtonPress(sender)
        close()
    }

    @IBAction func getDirections(_ sender: AnyObject) {
        let location = CLLocation(latitude: orderStatus.store!.latitude, longitude: orderStatus.store!.longitude)
        MapsManager.openMapsWithDirectionsToLocation(location, name: orderStatus.store!.name)
        AnalyticsService.trackEvent("stores", action: "get_directions", label: orderStatus.store!.name)
    }
    
    @IBAction func makeCall(_ sender: AnyObject) {
        CallManager.call(orderStatus.store!.phone, presentingViewController: self)
        AnalyticsService.trackEvent("stores", action: "call_store", label: orderStatus.store!.name)
    }

    @IBAction func becomeJambaInsider(_ sender: AnyObject) {
        close()
        NonSignedInViewController.sharedInstance().openSignUpScreen();

    }
    
    @IBAction func navigateToOrderStatus(_ sender: AnyObject) {
        trackGesture(sender as! UIGestureRecognizer)
        view.removeFromSuperview()
        OrderConfirmationViewController.currentInstance = nil
        SignedInMainViewController.sharedInstance().closeModalScreen(){
            SignedInMainViewController.sharedInstance().showRecentOrder(showOrderDetail: true)
            BasketService.lastOrderStatus = nil
        }
        
    }
}
