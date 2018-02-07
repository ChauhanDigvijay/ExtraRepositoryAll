//
//  InCommCardsService.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/10/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public typealias InCommCardCallback = (card: InCommCard?, error: NSError?) -> Void
public typealias InCommCardBalanceCallback = (cardBalance: Double?, cardBalanceDate: String?, error: NSError?) -> Void
public typealias InCommVoidCardCallback = (error: NSError?) -> Void

public class InCommCardsService {

    public class func card(brandCode: String, cardNumber: String, pin: String? = nil, getLatestBalance: Bool = true, callback: InCommCardCallback) {
        var parameters = InCommJSONDictionary()
        parameters["pin"] = pin
        parameters["getLatestBalance"] = getLatestBalance ? "true" : "false"
        InCommService.get("/Cards/\(brandCode)/\(cardNumber)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(card: nil, error: error)
                return
            }
            let card = InCommCard(json: response)
            callback(card: card, error: nil)
        }
    }

    public class func card(cardId: Int32, getLatestBalance: Bool = true, callback: InCommCardCallback) {
        var parameters = InCommJSONDictionary()
        parameters["getLatestBalance"] = getLatestBalance ? "true" : "false"
        InCommService.get("/Cards/\(cardId)", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(card: nil, error: error)
                return
            }
            let card = InCommCard(json: response)
            callback(card: card, error: nil)
        }
    }
    
}
