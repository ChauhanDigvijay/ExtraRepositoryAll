//
//  Array+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 12/19/14.
//  Copyright (c) 2014 hathway. All rights reserved.
//

import Foundation

public extension Array {
    
    /// Returns an item by index
    public func getAt(_ index: Int) -> Element? {
        if count > index {
            return self[index]
        }
        return nil
    }

    /// Return a random item from the array.
    public func random() -> Element? {
        if count < 1 {
            return nil
        }
        let index = Int(arc4random_uniform(UInt32(count)))
        return self[index]
    }
    
}
