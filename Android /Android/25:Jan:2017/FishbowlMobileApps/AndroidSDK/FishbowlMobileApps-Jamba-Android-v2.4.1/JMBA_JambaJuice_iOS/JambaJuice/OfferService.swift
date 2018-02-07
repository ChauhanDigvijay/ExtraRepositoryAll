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

//protocol OfferServiceDelegate: class {
//    func showOffersList()
//}
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
        var validatedOffers = FishbowlOfferSummary()
        validatedOffers.type = offers.type
        for offer in offers.offerList {
            //Check for Promocode is available
            if offer.promotionCode != "" && offer.promotionCode != "0" {
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                let offerEndDate = dateFormatter.date(from: offer.offerValidity)
                let today = Date().timeIntervalSince1970    //today
                
                //if the offer is not expired then add the offer
                if offerEndDate != nil && offerEndDate!.timeIntervalSince1970 > today {
                    validatedOffers.offerList.append(offer)
                    
                    //if the offer never expires, then add the offer
                } else if offerEndDate == nil {
                    validatedOffers.offerList.append(offer)
                }
            }
        }
        return validatedOffers
    }
}
