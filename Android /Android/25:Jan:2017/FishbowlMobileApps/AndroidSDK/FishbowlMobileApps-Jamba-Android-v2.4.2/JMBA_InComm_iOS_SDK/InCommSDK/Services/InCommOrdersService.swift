//
//  InCommOrdersService.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/10/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public typealias InCommOrderCallback = (_ order: InCommOrder?, _ error: NSError?) -> Void
public typealias InCommPromoOrderCallback = (_ error: NSError?) -> Void

open class InCommOrdersService {

    open class func submitOrder(_ submitOrder: InCommSubmitOrder, callback: @escaping InCommOrderCallback) {
        let parameters = submitOrder.serializeAsJSONDictionary()
        InCommService.post("/Orders", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let order = InCommOrder(json: response)
            callback(order, nil)
        }
    }
    
    open class func submitReloadOrder(_ reloadOrder: InCommReloadOrder, callback: @escaping InCommOrderCallback) {
        InCommService.post("/Orders/SubmitReload", parameters: reloadOrder.serializeAsJSONDictionary()) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let order = InCommOrder(json: response)
            callback(order, nil)
        }
    }
    
    open class func order(_ id: String, callback: @escaping InCommOrderCallback) {
        InCommService.get("/Orders/\(id)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let order = InCommOrder(json: response)
            callback(order, nil)
        }
    }
    
    open class func submitPromoOrder(_ submitOrder: InCommSubmitOrder, callback: @escaping InCommPromoOrderCallback) {
        let parameters = submitOrder.serializeAsJSONDictionary()
        InCommService.post("/Orders", parameters: parameters) { (response, error) -> Void in
            return callback(error)
        }
    }
    
    
}
