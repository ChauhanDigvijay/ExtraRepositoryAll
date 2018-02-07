//
//  StatusBarStyleManager.swift
//  JambaJuice
//
//  Created by Taha Samad on 7/6/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

struct StatusBarStyleManager {
    
    private static var requestStack: [StatusBarStyleRequest] = []
    
    static func pushStyle(style: UIStatusBarStyle, viewController: UIViewController) {
        pruneStack(nil)
        pruneStack(viewController)
        requestStack.append(StatusBarStyleRequest(viewController: viewController, requestedStatusBarStyle: style))
        updateStatusBarStyle()
    }
    
    static func popStyle(viewController: UIViewController) {
        pruneStack(viewController)
        updateStatusBarStyle()
    }
    
    private static func updateStatusBarStyle() {
        UIApplication.sharedApplication().statusBarStyle = requestStack.last?.requestedStatusBarStyle ?? .Default
    }
    
    private static func pruneStack(viewController: UIViewController?) {
        requestStack = requestStack.filter {
            return $0.viewController != nil && (viewController == nil || $0.viewController != viewController)
        }
    }
    
}
