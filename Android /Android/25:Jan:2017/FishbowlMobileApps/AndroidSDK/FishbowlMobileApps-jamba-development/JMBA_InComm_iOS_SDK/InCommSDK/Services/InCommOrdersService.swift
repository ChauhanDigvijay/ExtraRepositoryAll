//
//  InCommOrdersService.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/10/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public typealias InCommOrderCallback = (order: InCommOrder?, error: NSError?) -> Void
public typealias InCommPromoOrderCallback = (error: NSError?) -> Void

public class InCommOrdersService {

    public class func submitOrder(submitOrder: InCommSubmitOrder, callback: InCommOrderCallback) {
        let parameters = submitOrder.serializeAsJSONDictionary()
        InCommService.post("/Orders", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(order: nil, error: error)
                return
            }
            let order = InCommOrder(json: response)
            callback(order: order, error: nil)
        }
    }
    
    public class func submitReloadOrder(reloadOrder: InCommReloadOrder, callback: InCommOrderCallback) {
        InCommService.post("/Orders/SubmitReload", parameters: reloadOrder.serializeAsJSONDictionary()) { (response, error) -> Void in
            if error != nil {
                callback(order: nil, error: error)
                return
            }
            let order = InCommOrder(json: response)
            callback(order: order, error: nil)
        }
    }
    
    public class func order(id: String, callback: InCommOrderCallback) {
        InCommService.get("/Orders/\(id)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(order: nil, error: error)
                return
            }
            let order = InCommOrder(json: response)
            callback(order: order, error: nil)
        }
    }
    
    public class func submitPromoOrder(submitOrder: InCommSubmitOrder, callback: InCommPromoOrderCallback) {
        let parameters = submitOrder.serializeAsJSONDictionary()
        InCommService.post("/Orders", parameters: parameters) { (response, error) -> Void in
            return callback(error: error)
        }
    }
    
    
}