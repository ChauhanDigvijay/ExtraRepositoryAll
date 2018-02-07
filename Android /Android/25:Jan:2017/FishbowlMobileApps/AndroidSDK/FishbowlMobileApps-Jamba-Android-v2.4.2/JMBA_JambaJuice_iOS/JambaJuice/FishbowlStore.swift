//
//  FishbowlStore.swift
//  JambaJuice
//
//  Created by VT010 on 2/16/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON
public struct FishbowlStore{
    var address: String?
    var city: String?
    var contactPerson: String?
    var country: String?
    var email: String?
    var geoFenceCorrFactor: String?
    var latitude: String?
    var longitude: String?
    var mailingAddress1: String?
    var mobile: String?
    var phone: String?
    var primaryContactEmail: String?
    var secondaryContact: String?
    var state: String?
    var storeHourList: String?
    var storeID: Int?
    var storeMobile: String?
    var storeName: String?
    var storeNumber:String?
    var tenantId:String?
    var zip:String?
    
    public init(json:JSON){
        
        self.address = json["address"].string
        self.city = json["city"].string
        self.contactPerson = json["contactPerson"].string
        self.country = json["country"].string
        self.email = json["email"].string
        self.geoFenceCorrFactor = json["geoFenceCorrFactor"].string
        self.latitude = json["latitude"].string
        self.longitude = json["longitude"].string
        self.mailingAddress1 = json["mailingAddress1"].string
        self.mobile = json["mobile"].string
        self.phone = json["phone"].string
        self.primaryContactEmail = json["primaryContactEmail"].string
        self.secondaryContact = json["secondaryContact"].string
        self.state = json["state"].string
        self.storeHourList = json["storeHourList"].string
        self.storeID = json["storeID"].int
        if json["storeNumber"].string == nil{
            self.storeNumber = "0"
        }else{
            self.storeNumber = json["storeNumber"].string
        }
        self.storeMobile = json["storeMobile"].string
        self.storeName = json["storeName"].string
        self.tenantId = json["tenantId"].string
        self.zip = json["zip"].string
    }
}

