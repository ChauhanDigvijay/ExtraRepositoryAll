//
//  BasketNavigationController.swift
//  JambaJuice
//
//  Created by Taha Samad on 04/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

private typealias BasketNavigationControllerAnimationCompleteCallback = () -> Void

class BasketNavigationController: UINavigationController {
    
    class func showBasketScreen() -> BasketNavigationController {
        let window = UIApplication.shared.delegate!.window!!
        //Dismiss any keyboards from prev vcs
        window.endEditing(true)
        let nc = UIViewController.instantiate("BasketNavigationController", storyboard: "Main") as! BasketNavigationController
        window.addSubview(nc.view)
        nc.animateIn {}
        return nc
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    /// Slide the navigation controller view into screen
    fileprivate func animateIn(_ callback: @escaping BasketNavigationControllerAnimationCompleteCallback) {
        let superViewBounds = view.superview!.bounds
        view.frame = CGRect(x: superViewBounds.width, y: 0, width: superViewBounds.width, height: superViewBounds.height)
        UIView.animate(withDuration: Constants.basketNCAnimationTime, animations: {
            self.view.frame = superViewBounds
        }, completion: { completed in
            callback()
        })
    }

    /// Slide the navigation controller view out of screen
    fileprivate func animateOut(_ callback: @escaping BasketNavigationControllerAnimationCompleteCallback) {
        let superViewBounds = view.superview!.bounds
        let finalFrame = CGRect(x: superViewBounds.width, y: 0, width: superViewBounds.width, height: superViewBounds.height)
        UIView.animate(withDuration: Constants.basketNCAnimationTime, animations: {
            self.view.frame = finalFrame
        }, completion: { completed in
            callback()
        })
    }
    
    func dismiss() {
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        animateOut {
            self.view.removeFromSuperview()
            NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.BasketNCDismissed.rawValue), object: self)
        }
    }
    
    // Dismiss when restart order
    func dismissWhenRestartOrder(){
        self.view.removeFromSuperview()
        NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.BasketNCDismissed.rawValue), object: self)
    }
    
    
    func dimissWhenPushNotificationViewOffer(){
        self.view.removeFromSuperview()
        NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.BasketNCDismissed.rawValue), object: self)
    }
    
    func dimissWhenShortcutItem(){
        self.view.removeFromSuperview()
        NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.BasketNCDismissed.rawValue), object: self)
    }

}
