//
//  OloMenuService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

public typealias OloMenuCallback = (_ categories: [OloCategory], _ error: NSError?) -> Void
public typealias OloModifierCallback = (_ modifiers: [OloModifier], _ error: NSError?) -> Void

open class OloMenuService {
 
    /// Retrieve the entire menu from a participating restaurant
    ///
    /// - parameter callback:    Returns a list of OloProduct objects or error if failed
    open class func restaurantMenu(_ restaurant: OloRestaurant, callback: @escaping OloMenuCallback) {
        restaurantMenu(restaurant.id, callback: callback)
    }
    
    open class func restaurantMenu(_ restaurantId: Int64, callback: @escaping OloMenuCallback) {
        OloService.get("/restaurants/\(restaurantId)/menu") { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }

            let categories = response["categories"].arrayValue.map { item in OloCategory(json: item) }
            callback(categories, nil)
        }
    }
    
    open class func productModifiers(_ product: OloProduct, callback: @escaping OloModifierCallback) {
        productModifiers(product.id, callback: callback)
    }
    
    open class func productModifiers(_ productId: Int64, callback: @escaping OloModifierCallback) {
        OloService.get("/products/\(productId)/modifiers"){ (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            
            let modifiers = response["optiongroups"].arrayValue.map { item in OloModifier(json: item) }
            callback(modifiers, nil)
        }
    }
    
    open class func productOptions(_ product: OloProduct, callback: @escaping OloModifierCallback) {
        let productId = product.id
        OloService.get("/products/\(productId)/options"){ (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            
            let modifiers = response["optiongroups"].arrayValue.map { item in OloModifier(json: item) }
            callback(modifiers, nil)
        }
    }

}
