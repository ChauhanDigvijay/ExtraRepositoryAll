//
//  GradientView.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/16/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

@IBDesignable
class GradientView: UIView {

    @IBInspectable var topColor: UIColor? {
        didSet {
            configureView()
        }
    }
    @IBInspectable var bottomColor: UIColor? {
        didSet {
            configureView()
        }
    }

    override class func layerClass() -> AnyClass {
        return CAGradientLayer.self
    }

    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)!
        configureView()
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
        configureView()
    }

    override func tintColorDidChange() {
        super.tintColorDidChange()
        configureView()
    }

    func configureView() {
        guard let layer = self.layer as? CAGradientLayer else {
            return
        }
        let locations = [0.0, 1.0]
        layer.locations = locations
        let color1 = topColor ?? self.tintColor as UIColor
        let color2 = bottomColor ?? UIColor.blackColor() as UIColor
        layer.colors = [color1.CGColor, color2.CGColor]
    }

}
