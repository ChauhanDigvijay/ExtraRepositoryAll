//
//  FishbowlOffer.swift
//  JambaJuice
//
//  Created by Joe Jayabalan on 2/21/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct FishbowlOffer {
    
    public var type: String
    public var offerTitle: String
    public var desc: String
    public var notificationContent:String
    public var quantity: Int
    public var id : Int
    public var channalId : Int
    public var passURL : String
    public var offerValidity : String
    public var storeAddress : String
    public var pmPromotionID : String
    public var isPMoffer : Bool
    public var promotionCode:String
    public var storeRestriction:[FishbowlOfferStore]
    public var onlineInStore:String
    public var offerId:Int
    
    
    
    
    public init(json: JSON) {
        type       = json["type"].stringValue
        offerTitle = json["campaignTitle"].stringValue
        
        // CampaignDescription data
        let descContent = json["campaignDescription"].stringValue
        let descContentPromoCodeRemoval = descContent.components(separatedBy: "Promo Code:")
        desc = descContentPromoCodeRemoval[0]
        
        // Notification content promocode removal
        let noteContent = json["notificationContent"].stringValue
        let promoCodeRemoval = noteContent.components(separatedBy: "Promo Code:")
        notificationContent = promoCodeRemoval[0]
        
        quantity   = json["quantity"].intValue
        quantity    = json["quantity"].intValue
        id        =  json["campaignId"].intValue
        channalId    =  json["channelID"].intValue
        passURL    =  json["offerURL"].stringValue
        offerValidity = json["validityEndDateTime"].stringValue
        storeAddress = json["storeAddress"].stringValue
        pmPromotionID = json["promotionID"].stringValue
        isPMoffer =     json["isPMOffer"].boolValue
        promotionCode = json["promotionCode"].stringValue
        storeRestriction = json["storeRestriction"].arrayValue.map{store in FishbowlOfferStore(json: store)}
        onlineInStore = json["onlineinStore"].stringValue
        offerId      = json["id"].intValue
    }
    
}
