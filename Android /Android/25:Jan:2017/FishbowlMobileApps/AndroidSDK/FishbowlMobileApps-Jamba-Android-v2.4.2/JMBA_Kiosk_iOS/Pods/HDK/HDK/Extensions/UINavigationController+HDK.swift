//
//  UINavigationController+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 5/30/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import UIKit

extension UINavigationController {

    /// Pop last view controller from the navigation stack, with animation and completion block
    /// Use CATransaction to provide a completion block when the animation is complete
    public func popViewController(completion: () -> Void) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        popViewControllerAnimated(true)
        CATransaction.commit()
    }

    /// Push a view controller into the navigation stack, with animation and completion block
    /// Use CATransaction to provide a completion block when the animation is complete
    public func pushViewController(viewController: UIViewController, completion: () -> Void) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        pushViewController(viewController, animated: true)
        CATransaction.commit()
    }
    
}
