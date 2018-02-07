//
//  OloMenuService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

public typealias OloMenuCallback = (categories: [OloCategory], error: NSError?) -> Void
public typealias OloModifierCallback = (modifiers: [OloModifier], error: NSError?) -> Void

public class OloMenuService {
 
    /// Retrieve the entire menu from a participating restaurant
    ///
    /// - parameter callback:    Returns a list of OloProduct objects or error if failed
    public class func restaurantMenu(restaurant: OloRestaurant, callback: OloMenuCallback) {
        restaurantMenu(restaurant.id, callback: callback)
    }
    
    public class func restaurantMenu(restaurantId: Int64, callback: OloMenuCallback) {
        OloService.get("/restaurants/\(restaurantId)/menu") { (response, error) -> Void in
            if error != nil {
                callback(categories: [], error: error)
                return
            }

            let categories = response["categories"].arrayValue.map { item in OloCategory(json: item) }
            callback(categories: categories, error: nil)
        }
    }
    
    public class func productModifiers(product: OloProduct, callback: OloModifierCallback) {
        productModifiers(product.id, callback: callback)
    }
    
    public class func productModifiers(productId: Int64, callback: OloModifierCallback) {
        OloService.get("/products/\(productId)/modifiers"){ (response, error) -> Void in
            if error != nil {
                callback(modifiers: [], error: error)
                return
            }
            
            let modifiers = response["optiongroups"].arrayValue.map { item in OloModifier(json: item) }
            callback(modifiers: modifiers, error: nil)
        }
    }
    
    public class func productOptions(product: OloProduct, callback: OloModifierCallback) {
        let productId = product.id
        OloService.get("/products/\(productId)/options"){ (response, error) -> Void in
            if error != nil {
                callback(modifiers: [], error: error)
                return
            }
            
            let modifiers = response["optiongroups"].arrayValue.map { item in OloModifier(json: item) }
            callback(modifiers: modifiers, error: nil)
        }
    }

}
