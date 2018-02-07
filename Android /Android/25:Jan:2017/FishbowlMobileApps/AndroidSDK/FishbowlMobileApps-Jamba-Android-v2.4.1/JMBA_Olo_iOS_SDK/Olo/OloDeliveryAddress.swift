//
//  OloDeliveryAddress.swift
//  Olo
//
//  Created by VT016 on 09/03/17.
//  Copyright © 2017 Hathway, Inc. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct OloDeliveryAddress {
    
    public var id                   : String
    public var building             : String
    public var streetaddress        : String
    public var city                 : String
    public var zipcode              : String
    public var phonenumber          : String
    public var specialinstructions  : String
    
    public init(id: String, building:String, streetaddress: String, city: String, zipcode:String, phonenumber: String, specialinstructions:String) {
        self.id                  = id
        self.building            = building
        self.streetaddress       = streetaddress
        self.city                = city
        self.zipcode             = zipcode
        self.phonenumber         = phonenumber
        self.specialinstructions = specialinstructions
    }
    
    public init(json: JSON) {
        id                  = json["id"].stringValue
        building            = json["building"].stringValue
        streetaddress       = json["streetaddress"].stringValue
        city                = json["city"].stringValue
        zipcode             = json["zipcode"].stringValue
        phonenumber         = json["phonenumber"].stringValue
        specialinstructions = json["specialinstructions"].stringValue
    }
    
    public func serializeAsJSONDictionary() -> OloJSONDictionary {
        var jsonDict = OloJSONDictionary()
        jsonDict["id"]                  = id as AnyObject?
        jsonDict["building"]            = building as AnyObject?
        jsonDict["streetaddress"]       = streetaddress as AnyObject?
        jsonDict["city"]                = city as AnyObject?
        jsonDict["zipcode"]             = zipcode as AnyObject?
        jsonDict["phonenumber"]         = phonenumber as AnyObject?
        jsonDict["specialinstructions"] = specialinstructions as AnyObject?
        return jsonDict
    }
    
}
