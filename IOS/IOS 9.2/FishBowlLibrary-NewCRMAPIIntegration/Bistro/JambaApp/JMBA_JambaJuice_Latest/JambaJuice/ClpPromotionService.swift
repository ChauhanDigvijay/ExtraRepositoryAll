//
//  ClpPromotionService.swift
//  JambaJuice
//
//  Created by HARSH on 03/03/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import XCGLogger
import SwiftyJSON

typealias ClpPromotionServiceCallback = (offer: ClpOfferSummary?, error: NSError?) -> Void


class ClpPromotionService{
    
    class func getPromoCode(customerId : String,offerId : String, callback: ClpPromotionServiceCallback){
        
        let parameters = [
            "CLP-API-KEY": "91225258ddb5c8503dce33719c5deda7"
        ]
        
        ClpService.get("http://jamba.clpqa.com/clpapi/mobile/getPromo/\(customerId)/\(offerId)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
            
            
            if(error != nil){
                XCGLogger.info("error response")
                return
            }
            XCGLogger.info(response.description)
            //callback(offer: ClpOfferSummary(json: response), error: nil)
            
        }
        
        
    }
}