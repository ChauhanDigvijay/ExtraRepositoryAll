//
//  UIViewController+JambaJuice.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/9/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

extension UIViewController {

    /// Class name as string (with module namespace removed)
    var className: String {
        return NSStringFromClass(self.dynamicType).componentsSeparatedByString(".").last ?? "UIViewController"
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
