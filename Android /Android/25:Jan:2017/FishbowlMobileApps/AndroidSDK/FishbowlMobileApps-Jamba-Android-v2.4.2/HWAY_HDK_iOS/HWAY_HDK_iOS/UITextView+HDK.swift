//
//  UITextView+HDK.swift
//  Pods
//
//  Created by Taha Samad on 9/15/15.
//
//

import UIKit

extension UITextView {
    
    public func shouldChangeInRange(_ range: NSRange, replacementString string: String, maxAllowedLength: Int) -> Bool {
        // http://stackoverflow.com/questions/433337/set-the-maximum-character-length-of-a-uitextfield
        // Prevent crashing undo bug – see note below.
        let textCount = text?.characters.count ?? 0
        if range.length + range.location >  textCount {
            return false
        }
        let newLength = textCount + string.characters.count - range.length
        return newLength <= maxAllowedLength
    }
    
}
