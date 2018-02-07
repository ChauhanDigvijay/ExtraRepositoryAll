//
//  UIViewController+JambaJuice.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/3/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

enum NavigationBarStyle {
    case LightBlue
    case Orange
    case Red
}

extension UIViewController {
    
    /// Class name as string (with module namespace removed)
    var className: String {
        return NSStringFromClass(self.dynamicType).componentsSeparatedByString(".").last ?? "UIViewController"
    }
    
    func configureNavigationBar(style: NavigationBarStyle) {
        let font = UIFont(name: Constants.archerMedium, size: 20)!
        let navigationBar = navigationController?.navigationBar
        
        switch style {
            case .LightBlue:
                navigationBar?.translucent = true
                navigationBar?.backgroundColor = UIColor(hex: Constants.jambaLightBlueColor)
//                navigationBar?.barTintColor = UIColor(hex: Constants.jambaLightBlueColor)
                navigationBar?.tintColor = UIColor(hex: Constants.jambaGrayColor)
                navigationBar?.titleTextAttributes = [NSFontAttributeName: font, NSForegroundColorAttributeName: UIColor(hex: Constants.jambaGrayColor)]
                StatusBarStyleManager.pushStyle(.Default, viewController: self)

            case .Orange:
                navigationBar?.translucent = true
//                navigationBar?.backgroundColor = UIColor(hex: Constants.jambaOrangeColor)
                navigationBar?.barTintColor = UIColor(hex: Constants.jambaOrangeColor)
                navigationBar?.tintColor = UIColor.whiteColor()
                navigationBar?.titleTextAttributes = [NSFontAttributeName: font, NSForegroundColorAttributeName: UIColor.whiteColor()]
                StatusBarStyleManager.pushStyle(.LightContent, viewController: self)
        
        case .Red:
//        navigationBar?.translucent = true
        navigationBar?.barTintColor = UIColor(hex: Constants.jambaRedColor)

        navigationBar?.titleTextAttributes = [NSFontAttributeName: font, NSForegroundColorAttributeName: UIColor.whiteColor()]
        StatusBarStyleManager.pushStyle(.Default, viewController: self)
        }

    }

    /// Track screen view
    func trackScreenView() {
        AnalyticsService.trackScreenView(className)
    }

    /// Track UX event on current screen
    func trackScreenEvent(action: String, label: String) {
        AnalyticsService.trackEvent("ux", action: action, label: "\(className)::\(label)", screenName: className)
    }
    
    /// Track button press on screen
    func trackButtonPress(button: UIButton) {
        let title = button.titleLabel?.text ?? "[BUTTON HAS NO TITLE]"
        trackScreenEvent("button_press", label: title)
    }
    
    /// Track button press on screen
    func trackButtonPressWithName(buttonName: String) {
        trackScreenEvent("button_press", label: buttonName)
    }

    /// Track keyboard return button
    func trackKeyboardReturn() {
        trackScreenEvent("button_press", label: "KeyboardReturn")
    }
    
    /// Track gesture
    func trackGesture(gestureRecognizer: UIGestureRecognizer) {
        trackScreenEvent("gesture", label: NSStringFromClass(gestureRecognizer.dynamicType))
    }

}
