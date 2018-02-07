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
        let window = UIApplication.sharedApplication().delegate!.window!!
        //Dismiss any keyboards from prev vcs
        window.endEditing(true)
        let nc = UIViewController.instantiate("BasketNavigationController", storyboard: "Main") as! BasketNavigationController
        window.addSubview(nc.view)
        nc.animateIn {}
        return nc
    }
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    /// Slide the navigation controller view into screen
    private func animateIn(callback: BasketNavigationControllerAnimationCompleteCallback) {
        let superViewBounds = view.superview!.bounds
        view.frame = CGRectMake(superViewBounds.width, 0, superViewBounds.width, superViewBounds.height)
        UIView.animateWithDuration(Constants.basketNCAnimationTime, animations: {
            self.view.frame = superViewBounds
        }, completion: { completed in
            callback()
        })
    }

    /// Slide the navigation controller view out of screen
    private func animateOut(callback: BasketNavigationControllerAnimationCompleteCallback) {
        let superViewBounds = view.superview!.bounds
        let finalFrame = CGRectMake(superViewBounds.width, 0, superViewBounds.width, superViewBounds.height)
        UIView.animateWithDuration(Constants.basketNCAnimationTime, animations: {
            self.view.frame = finalFrame
        }, completion: { completed in
            callback()
        })
    }
    
    func dismiss() {
        animateOut {
            self.view.removeFromSuperview()
            NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.BasketNCDismissed.rawValue, object: self)
        }
    }
    
    // Dismiss when restart order
    func dismissWhenRestartOrder(){
        self.view.removeFromSuperview()
        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.BasketNCDismissed.rawValue, object: self)
    }

}
