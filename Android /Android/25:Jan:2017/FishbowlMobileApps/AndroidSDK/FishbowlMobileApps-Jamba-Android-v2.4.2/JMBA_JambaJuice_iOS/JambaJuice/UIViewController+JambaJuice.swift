//
//  UIViewController+JambaJuice.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/3/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

enum NavigationBarStyle {
    case lightBlue
    case orange
    case red
}

extension UIViewController {
    
    /// Class name as string (with module namespace removed)
    var className: String {
        return NSStringFromClass(type(of: self)).components(separatedBy: ".").last ?? "UIViewController"
    }
    
    func configureNavigationBar(_ style: NavigationBarStyle) {
        let font = UIFont.init(name: Constants.archerMedium, size: 20)!
        let navigationBar = navigationController?.navigationBar
        
        switch style {
            case .lightBlue:
                navigationBar?.isTranslucent = true
                navigationBar?.backgroundColor = UIColor(hex: Constants.jambaLightBlueColor)
                navigationBar?.barTintColor = nil
                navigationBar?.tintColor = UIColor(hex: Constants.jambaGrayColor)
                navigationBar?.titleTextAttributes = [NSAttributedStringKey.font: font, NSAttributedStringKey.foregroundColor: UIColor(hex: Constants.jambaDarkGrayColor)]
                StatusBarStyleManager.pushStyle(.default, viewController: self)

            case .orange:
                navigationBar?.isTranslucent = true
                 navigationBar?.backgroundColor = nil
                navigationBar?.barTintColor = UIColor(hex: Constants.jambaOrangeColor)
                navigationBar?.tintColor = UIColor.white
                navigationBar?.titleTextAttributes = [NSAttributedStringKey.font: font, NSAttributedStringKey.foregroundColor: UIColor.white]
                StatusBarStyleManager.pushStyle(.lightContent, viewController: self)
        
        case .red:
//        navigationBar?.translucent = true
        navigationBar?.barTintColor = UIColor(hex: Constants.jambaRedColor)

        navigationBar?.titleTextAttributes = [NSAttributedStringKey.font: font, NSAttributedStringKey.foregroundColor: UIColor.white]
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        }

    }

    /// Track screen view
    func trackScreenView() {
        AnalyticsService.trackScreenView(className)
    }

    /// Track UX event on current screen
    func trackScreenEvent(_ action: String, label: String) {
        AnalyticsService.trackEvent("ux", action: action, label: "\(className)::\(label)", screenName: className)
    }
    
    /// Track button press on screen
    func trackButtonPress(_ button: UIButton) {
        let title = button.titleLabel?.text ?? "[BUTTON HAS NO TITLE]"
        trackScreenEvent("button_press", label: title)
    }
    
    /// Track button press on screen
    func trackButtonPressWithName(_ buttonName: String) {
        trackScreenEvent("button_press", label: buttonName)
    }

    /// Track keyboard return button
    func trackKeyboardReturn() {
        trackScreenEvent("button_press", label: "KeyboardReturn")
    }
    
    /// Track gesture
    func trackGesture(_ gestureRecognizer: UIGestureRecognizer) {
        trackScreenEvent("gesture", label: NSStringFromClass(type(of: gestureRecognizer)))
    }

}
