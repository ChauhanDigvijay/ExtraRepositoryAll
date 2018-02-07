//
//  SelectableButton.swift
//  JambaJuice
//
//  Created by Taha Samad on 26/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import Spring

// TODO: Move this to HDK
open class SelectableButton: UIButton {

    @IBInspectable var selectedBackgroundColor: UIColor? = UIColor.clear {
        didSet {
            updateColor()
        }
    }
    
    @IBInspectable var normalBackgroundColor: UIColor? = UIColor.clear {
        didSet {
            updateColor()
        }
    }
    
    override open func awakeFromNib() {
        super.awakeFromNib()
        updateColor()
    }
    
    override open var isSelected: Bool {
        didSet {
            updateColor()
        }
    }
    
    func updateColor() {
        backgroundColor = isSelected ? selectedBackgroundColor : normalBackgroundColor
    }

}
