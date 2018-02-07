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
class SelectableButton: DesignableButton {

    @IBInspectable var selectedBackgroundColor: UIColor? = UIColor.clearColor() {
        didSet {
            updateColor()
        }
    }
    
    @IBInspectable var normalBackgroundColor: UIColor? = UIColor.clearColor() {
        didSet {
            updateColor()
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        updateColor()
    }
    
    override var selected: Bool {
        didSet {
            updateColor()
        }
    }
    
    func updateColor() {
        backgroundColor = selected ? selectedBackgroundColor : normalBackgroundColor
    }

}
