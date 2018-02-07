//
//  OloOrderService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

open class OloOrderService {
    
    open class func getOrederAddress(_ orderGuid: String, callback: @escaping OloBasketSavedDeliveryAddressListCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            callback([], OloUtils.error("User is not authenticated.", code: 0))
            return
        }
        
        OloService.get("/orders/\(orderGuid)/deliverystatus?authtoken=\(authToken!)") { (response, error) -> Void in
            if error != nil {
                callback([], error)
            } else {
                let address = OloSavedDeliverAddressList(json: response)
                callback(address.savedDeliverAddress, error)
            }
        }
    }
   
}
