//
//  InCommCardsService.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/10/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public typealias InCommCardCallback = (_ card: InCommCard?, _ error: NSError?) -> Void
public typealias InCommCardBalanceCallback = (_ cardBalance: Double?, _ cardBalanceDate: String?, _ error: NSError?) -> Void
public typealias InCommVoidCardCallback = (_ error: NSError?) -> Void

open class InCommCardsService {

    open class func card(_ brandCode: String, cardNumber: String, pin: String? = nil, getLatestBalance: Bool = true, callback: @escaping InCommCardCallback) {
        var parameters = InCommJSONDictionary()
        parameters["pin"] = pin as AnyObject?
        parameters["getLatestBalance"] = getLatestBalance ? "true" : "false" as AnyObject?
        InCommService.get("/Cards/\(brandCode)/\(cardNumber)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let card = InCommCard(json: response)
            callback(card, nil)
        }
    }

    open class func card(_ cardId: Int32, getLatestBalance: Bool = true, callback: @escaping InCommCardCallback) {
        var parameters = InCommJSONDictionary()
        parameters["getLatestBalance"] = getLatestBalance ? "true" : "false" as AnyObject?
        InCommService.get("/Cards/\(cardId)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let card = InCommCard(json: response)
            callback(card, nil)
        }
    }
    
}
