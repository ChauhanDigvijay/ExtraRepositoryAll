//
//  UIView+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 12/21/14.
//  Copyright (c) 2014 hathway. All rights reserved.
//

import UIKit

extension UIView {

    /// Circlifies the view by setting border radious to half of the width
    /// Assumes view is square for the resulting mask to be a circle
    public func circlify() {
        roundify(frame.width / 2.0)
    }
    
    /// Set the corner radius for the view
    /// - parameter radius: Radius to apply to each corner
    public func roundify(radius: CGFloat) {
        layer.cornerRadius = radius
        clipsToBounds = true
    }
    
    /// Adds a stroke line to the view with the given width and color
    /// - parameter color: Color for the stroke line
    /// - parameter width: Width in pixels for the stroke line
    public func stroke(color: UIColor, width: CGFloat) {
        layer.borderWidth = width
        layer.borderColor = color.CGColor
    }

    /// Recursively walk all subviews
    public func walkSubViews(visit: (view: UIView) -> Void) {
        let viewList = subviews
        for view in viewList {
            visit(view: view)
            view.walkSubViews(visit)
        }
    }

}
