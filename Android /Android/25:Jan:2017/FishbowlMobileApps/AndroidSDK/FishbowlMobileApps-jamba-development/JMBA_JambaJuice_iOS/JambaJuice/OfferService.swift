//
//  OfferService.swift
//  JambaJuice
//
//  Created by VT016 on 05/01/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//


import Foundation
import UIKit
import SVProgressHUD
import SwiftyJSON

protocol OfferServiceDelegate: class {
    func showOffersList()
}
class OfferService:NSObject {
    
    weak var delegate: OfferServiceDelegate?
    private(set) var offersSummary:ClpOfferSummary?
    
    static let sharedInstance=OfferService();
    
    func getOffersSummary() {
//        ClpApiClassService.sharedInstance.getOffers()
        updateOffers(ClpOfferSummary())
    }
    
    func updateOffers(offer:ClpOfferSummary) {
        self.offersSummary = offer
        delegate?.showOffersList()
    }
    
    func clearOfferList() {
        self.offersSummary = nil
    }
}
