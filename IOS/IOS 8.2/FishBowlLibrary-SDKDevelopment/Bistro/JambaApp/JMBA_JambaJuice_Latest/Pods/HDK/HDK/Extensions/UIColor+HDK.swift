//
//  UIColor+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 5/6/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import UIKit

extension UIColor {

    public convenience init(hex: Int) {
        let red   = CGFloat((hex & 0xFF0000) >> 16) / 255.0
        let green = CGFloat((hex & 0x000FF00) >> 8) / 255.0
        let blue  = CGFloat((hex & 0x0000FF)) / 255.0
        self.init(red: red, green: green, blue: blue, alpha: 1.0)
    }
    
}
