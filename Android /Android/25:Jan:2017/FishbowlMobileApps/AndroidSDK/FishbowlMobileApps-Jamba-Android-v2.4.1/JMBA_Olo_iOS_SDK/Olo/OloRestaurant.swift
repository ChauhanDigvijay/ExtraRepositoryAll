//
//  OloRestaurant.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/20/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

// OLO restaurant response (sample restaurant)
//
//    {
//        advanceonly = 1;
//        advanceorderdays = 7;
//        candeliver = 0;
//        canpickup = 1;
//        city = Salinas;
//        crossstreet = "";
//        customfields = "<null>";
//        deliveryarea = "";
//        deliveryfee = 0;
//        distance = 0;
//        extref = 1182;
//        hasolopass = 0;
//        id = 9517;
//        isavailable = 1;
//        latitude = "36.6592";
//        longitude = "-121.659";
//        metadata = "<null>";
//        minimumdeliveryorder = 0;
//        mobileurl = "http://jamba.ololitestaging.com/menu/jamba-juice-south-salinas-plaza-shopping-center-1182/";
//        name = "Jamba Juice South Salinas Plaza Shopping Center";
//        slug = "jamba-juice-south-salinas-plaza-shopping-center-1182";
//        state = CA;
//        storename = "Jamba Juice";
//        streetaddress = "1126 S. Main Street";
//        supportedcardtypes = "American Express/Discover/MasterCard/Visa";
//        supportscoupons = 1;
//        supportsfeedback = 1;
//        supportsgrouporders = 0;
//        supportsguestordering = 1;
//        supportsloyalty = 0;
//        supportsmanualfire = 0;
//        supportsonlineordering = 1;
//        supportsspecialinstructions = 1;
//        supportstip = 0;
//        telephone = "(555) 555-5555";
//        url = "http://jamba.olostaging.com/menu/jamba-juice-south-salinas-plaza-shopping-center-1182/";
//        zip = 93901;
//    }


import UIKit
import SwiftyJSON

public struct OloRestaurant {
   
    public var id: Int64
    public var name: String
    public var storeName: String
    public var telephone: String
    public var streetAddress: String
    public var crossStreet: String
    public var city: String
    public var state: String
    public var zip: String
    public var latitude: Double
    public var longitude: Double
    public var url: String
    public var mobileUrl: String
    public var distance: Double
    public var storeCode: String
    public var advanceOnly: Bool
    public var advanceOrderDays: Int
    public var supportsCoupons: Bool
    public var supportsDelivery: Bool
    public var supportedCardTypes: [String]
    public var hasOloPass: Bool
    public var customFields: [OloRestaurantCustomField]
    public var isAvailable: Bool
    public var supportsOnlineOrdering: Bool
    public var supportsSpecialInstructions: Bool
    public var supportedTimeModes:[String]
    public var deliveryFees: Double
    public var deliveryArea:String
    public var deliveryDistance:String
   
    public init(json: JSON) {
        id                 = json["id"].int64Value
        name               = json["name"].stringValue
        storeName          = json["storename"].stringValue
        telephone          = json["telephone"].stringValue
        streetAddress      = json["streetaddress"].stringValue
        crossStreet        = json["crossstreet"].stringValue
        city               = json["city"].stringValue
        state              = json["state"].stringValue
        zip                = json["zip"].stringValue
        latitude           = json["latitude"].doubleValue
        longitude          = json["longitude"].doubleValue
        url                = json["url"].stringValue
        mobileUrl          = json["mobileurl"].stringValue
        distance           = json["distance"].doubleValue
        storeCode          = json["extref"].stringValue
        advanceOnly        = json["advanceonly"].boolValue
        advanceOrderDays   = json["advanceorderdays"].intValue
        supportsCoupons    = json["supportscoupons"].boolValue
        supportsDelivery   = json["supportsdispatch"].boolValue
        supportedCardTypes = json["supportedcardtypes"].stringValue.components(separatedBy: "/")
        hasOloPass         = json["hasolopass"].boolValue
        customFields       = json["customfields"].arrayValue.map { item in OloRestaurantCustomField(json: item) }
        isAvailable        = json["isavailable"].boolValue
        supportsOnlineOrdering = json["supportsonlineordering"].boolValue
        supportsSpecialInstructions = json["supportsspecialinstructions"].boolValue
        supportedTimeModes = json["supportedtimemodes"].map{ $0.1.stringValue }
        deliveryFees       = json["deliveryfee"].doubleValue
        deliveryArea       = json["deliveryarea"].stringValue
        deliveryDistance   = json["distance"].stringValue
    }

}
