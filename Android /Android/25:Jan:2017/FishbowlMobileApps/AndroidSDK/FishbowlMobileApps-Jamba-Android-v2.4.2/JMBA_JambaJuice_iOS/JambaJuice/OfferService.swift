//
//  OfferService.swift
//  JambaJuice
//
//  Created by vThink Technologies on 05/01/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//


import Foundation
import UIKit
import SVProgressHUD
import SwiftyJSON

class OfferService:NSObject {
    
    enum onLineInstore:String {
        case instoreonline = "1"
        case onlineonly = "2"
        case instoreonly = "3"
    }
   // weak var delegate: OfferServiceDelegate?
    fileprivate(set) var offersSummary:FishbowlOfferSummary?
    static let sharedInstance=OfferService();
    
    func updateOffers(_ offer:FishbowlOfferSummary) {
        //filter
        self.offersSummary = self.validateOffers(offer)

    }
    
    func clearOfferList() {
        self.offersSummary = nil
    }
    
    //filter the offers (Validity of offers end & promocode is not empty)
    func validateOffers(_ offers:FishbowlOfferSummary) -> FishbowlOfferSummary {
        // Sort offers by ascending order
        let offerslist = offers.offerList
        var validatedOffers = FishbowlOfferSummary()
        validatedOffers.type = offers.type
        for offer in offerslist {
            //Check for Promocode is available and duplicate offers
            if offer.promotionCode != "" && offer.promotionCode != "0" && offer.pmPromotionID != "0" && offer.pmPromotionID != "" {
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                let offerEndDate = dateFormatter.date(from: offer.offerValidity)
                let today = Date().timeIntervalSince1970    //today
                
                //if the offer is not expired then add the offer and if the offer does not have validity add it
                if (offerEndDate != nil && offerEndDate!.timeIntervalSince1970 > today) || offerEndDate == nil{
                    if let index = validatedOffers.offerList.index(where: {$0.pmPromotionID == offer.pmPromotionID}){
                        if validatedOffers.offerList[index].offerId > offer.offerId{
                              validatedOffers.offerList[index] = offer
                        }
                        
                    }else{
                        validatedOffers.offerList.append(offer)
                    }
                }
            }
        }
       return validatedOffers
    }
}
