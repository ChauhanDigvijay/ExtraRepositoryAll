//
//  PaddingTextField.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 02/12/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit
class PaddingTextField: UITextField {
    
    @IBInspectable var paddingLeft: CGFloat = 0
    @IBInspectable var paddingRight: CGFloat = 0
    
    override func textRectForBounds(bounds: CGRect) -> CGRect {
        return CGRectMake(bounds.origin.x + paddingLeft, bounds.origin.y,
                          bounds.size.width - paddingLeft - paddingRight, bounds.size.height+10);
    }
    
    override func editingRectForBounds(bounds: CGRect) -> CGRect {
        return textRectForBounds(bounds)
    }}
