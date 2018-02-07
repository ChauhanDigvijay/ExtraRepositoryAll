//
//  ClpOffer.swift
//  JambaJuice
//
//  Created by Joe Jayabalan on 2/21/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct ClpOffer {
    
    public var type: String
    public var offerTitle: String
    public var desc: String
    public var quantity: Int
    public var id : Int
    public var channalId : Int
    public var passURL : String
    public var offerValidity : String
    public var storeAddress : String
    public var pmPromotionID : String
    public var isPMoffer : Bool



    
    public init(json: JSON) {
        type       = json["type"].stringValue
        offerTitle = json["campaignTitle"].stringValue
        desc       = json["campaignDescription"].stringValue
        quantity   = json["quantity"].intValue
        quantity    = json["quantity"].intValue
        id        =  json["campaignId"].intValue
        channalId    =  json["channelID"].intValue
        passURL    =  json["offerURL"].stringValue
        offerValidity = json["validityEndDateTime"].stringValue
        storeAddress = json["storeAddress"].stringValue
        pmPromotionID = json["promotionID"].stringValue
        isPMoffer =     json["isPMOffer"].boolValue



    }
    
}
