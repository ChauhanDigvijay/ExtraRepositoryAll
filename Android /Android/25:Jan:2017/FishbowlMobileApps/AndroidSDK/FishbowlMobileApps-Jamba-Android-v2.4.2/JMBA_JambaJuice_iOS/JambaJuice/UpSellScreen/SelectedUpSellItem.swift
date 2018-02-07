//
//  SelectedUpSellItem.swift
//  JambaJuice
//
//  Created by VT010 on 10/23/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

public struct SelecttedUpSellItem{
    public var id: Int64
    public var name: String
    public var cost: String
    public var shortdescription: String
    public var minquantity: Int
    public var maxquantity: Int
    public var selectedQuantity:Int
    public var selection:Bool
    
    public init(oloUpSellItem:OloUpSellItem){
        id = oloUpSellItem.id
        name = oloUpSellItem.name
        cost = oloUpSellItem.cost
        shortdescription = oloUpSellItem.shortdescription
        minquantity = oloUpSellItem.minquantity
        maxquantity = oloUpSellItem.maxquantity
        selectedQuantity = oloUpSellItem.minquantity
        selection   = false
    }
    
    public mutating func updateQuantity(value:String){
        self.selectedQuantity = Int(value)!
    }
    public mutating func updateSelection(selection:Bool){
        self.selection = selection
    }
}
