//
//  ClpOfferService.swift
//  JambaJuice
//
//  Created by Joe Jayabalan on 2/18/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import XCGLogger
import SwiftyJSON

typealias ClpOfferServiceCallback = (offer: ClpOfferSummary?, error: NSError?) -> Void
typealias ClpPromoServiceCallback = (promocode: clppromoSummary?, error: NSError?) -> Void
typealias clpOfferRedeemedCallback = (offerRedeemStatus: String?, error: NSError?) -> Void
typealias ClpOfferCallback = (offerjson : JSON, error : NSError?) -> Void



class ClpOfferService{
    
    class func getClpOffers(customerId : String, callback: ClpOfferServiceCallback){

        let parameters = [
            "CLP-API-KEY": String(clpAnalyticsService.sharedInstance.clpsdkobj?.authToken)
        ]
    //    customerId = "2477387680";
        
        ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getoffers/\(customerId)/100",parameters: nil){(response: JSON, error: NSError?)-> Void in
        
//        ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getofferforcustomer/\(customerId)/0",parameters: parameters){(response: JSON, error: NSError?)-> Void in

            if(error != nil){
                XCGLogger.info("error response")
                
                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                
                return
            }
             XCGLogger.info(response.description)
                        
            // security implementation code enabled            
            let jsonobject: JSON = response
            let StatusCode : Int = jsonobject["statuscode"].intValue
            if StatusCode == 401
            {
                clpAnalyticsService.sharedInstance.clpsdkobj?.getRefrshedToken()
                
                ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getoffers/\(customerId)/100",parameters: nil){(response: JSON, error: NSError?)-> Void in
                    
                    if(error != nil){
                        XCGLogger.info("error response")
                        
                        clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                        
                        return
                    }
                    else
                    {
                        callback(offer: ClpOfferSummary(json: response), error: nil)

                    }

                }
                
            }
            
            callback(offer: ClpOfferSummary(json: response), error: nil)

        }
        
        
    }
    
    class func getClpOffer(offerId : String, callback: ClpOfferServiceCallback){
        
        let parameters = [
            "CLP-API-KEY": AppConstants.CLPheaderKey
        ]
        
        ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getOfferByOfferId/\(offerId)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
            
            if(error != nil){
                XCGLogger.info("error response")
                
                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                
                return
            }
            XCGLogger.info(response.description)
            
            
            
            // security implementation code enabled
            let jsonobject: JSON = response
            let StatusCode : Int = jsonobject["statuscode"].intValue
            if StatusCode == 401
            {
                clpAnalyticsService.sharedInstance.clpsdkobj?.getRefrshedToken()
                
                ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getOfferByOfferId/\(offerId)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
                    
                    if(error != nil){
                        XCGLogger.info("error response")
                        
                        clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                        
                        return
                    }
                    
                }
                
            }
            
            
            callback(offer: ClpOfferSummary(json: response), error: nil)
            
        }
        
        
    }
    
    
    class func getPassByteArray(customerId : String, offerId : String, callback: ClpPromoServiceCallback){
        
        let parameters = [
            "CLP-API-KEY": AppConstants.CLPheaderKey  //12448704/1577
        ]
//        http://jamba.clpqa.com:8080/Clyptechs/clpapi/mobile/getPass/123/1548

        
        ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getPass/\(customerId)/\(offerId)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
            
            if(error != nil){
                XCGLogger.info("error response")
                
                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")

                
                return
            }
            XCGLogger.info(response.description)
            
            
            // security implementation code enabled
            let jsonobject: JSON = response
            let StatusCode : Int = jsonobject["statuscode"].intValue
            if StatusCode == 401
            {
                clpAnalyticsService.sharedInstance.clpsdkobj?.getRefrshedToken()
                
                ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getPass/\(customerId)/\(offerId)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
                    
                    if(error != nil){
                        XCGLogger.info("error response")
                        
                        clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                        
                        return
                    }
                    
                }
                
            }
            
            
            callback(promocode: clppromoSummary(json: response), error: nil)
            
        }
        
        
    }
    
    
    class func getPromoCode(customerId : String, offerId : String, isPMIntegrated : Bool ,callback: ClpPromoServiceCallback){
        
        let parameters = [
            "CLP-API-KEY": AppConstants.CLPheaderKey  //12448704/1577
        ]
        
        ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getPromo/\(customerId)/\(offerId)/\(isPMIntegrated)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
            
            if(error != nil){
                XCGLogger.info("error response")
                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")

                
                return
            }
            XCGLogger.info(response.description)
            
            
            // security implementation code enabled
            let jsonobject: JSON = response
            let StatusCode : Int = jsonobject["statuscode"].intValue
            if StatusCode == 401
            {
                clpAnalyticsService.sharedInstance.clpsdkobj?.getRefrshedToken()
                
                ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getPromo/\(customerId)/\(offerId)/\(isPMIntegrated)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
                    
                    if(error != nil){
                        XCGLogger.info("error response")
                        
                        clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                        
                        return
                    }
                    
                }
                
            }
       
            callback(promocode: clppromoSummary(json: response), error: nil)
            
        }
        
        
    }
    
    
    class func redeemedOffer(customerId : String, offerId : String,callback: clpOfferRedeemedCallback){
        
        let parameters = [
            "CLP-API-KEY": AppConstants.CLPheaderKey  //12448704/1577
        ]
        
        ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/redeemed/\(customerId)/\(offerId)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
            
            if(error != nil){
                XCGLogger.info("error response")
                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                
                
                return
            }
            XCGLogger.info(response.description)
            
            
            // security implementation code enabled
            let jsonobject: JSON = response
            let StatusCode : Int = jsonobject["statuscode"].intValue
            if StatusCode == 401
            {
                clpAnalyticsService.sharedInstance.clpsdkobj?.getRefrshedToken()
                
                ClpService.get("\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/redeemed/\(customerId)/\(offerId)",parameters: parameters){(response: JSON, error: NSError?)-> Void in
                    
                    if(error != nil){
                        XCGLogger.info("error response")
                        
                        clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                        
                        return
                    }
                    
                }
                
            }
            
            callback(offerRedeemStatus: "200", error: nil)
            
        }
        
        
    }

}