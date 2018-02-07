//
//  ClpOfferSummary.swift
//  JambaJuice
//
//  Created by Joe Jayabalan on 2/21/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct ClpOfferSummary {
    
    public var type: String
    public var offerList: [ClpOffer]
    
    public init(json: JSON) {
      //  type     = json["type"].stringValue
     //   offerList  = json["offer"].arrayValue.map { item in ClpOffer(json: item)
        
            
            type     = json["type"].stringValue
            offerList  = json["inAppOfferList"].arrayValue.map { item in ClpOffer(json: item) 
        
        }
    }
    
}


public struct ClpOffers {
    
    public var type: String
    public var offerList: [ClpOffer]
    
    public init(json: JSON) {
        type     = json["type"].stringValue
        offerList  = json["offer"].arrayValue.map { item in ClpOffer(json: item) }
    }
    
}

