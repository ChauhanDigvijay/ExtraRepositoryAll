//
//  StatusBarStyleManager.swift
//  JambaJuice
//
//  Created by Taha Samad on 7/6/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

struct StatusBarStyleManager {
    
    fileprivate static var requestStack: [StatusBarStyleRequest] = []
    
    static func pushStyle(_ style: UIStatusBarStyle, viewController: UIViewController) {
        pruneStack(nil)
        pruneStack(viewController)
        requestStack.append(StatusBarStyleRequest(viewController: viewController, requestedStatusBarStyle: style))
        updateStatusBarStyle()
    }
    
    static func popStyle(_ viewController: UIViewController) {
        pruneStack(viewController)
        updateStatusBarStyle()
    }
    
    fileprivate static func updateStatusBarStyle() {
        UIApplication.shared.statusBarStyle = requestStack.last?.requestedStatusBarStyle ?? .default
    }
    
    fileprivate static func pruneStack(_ viewController: UIViewController?) {
        requestStack = requestStack.filter {
            return $0.viewController != nil && (viewController == nil || $0.viewController != viewController)
        }
    }
    
}
