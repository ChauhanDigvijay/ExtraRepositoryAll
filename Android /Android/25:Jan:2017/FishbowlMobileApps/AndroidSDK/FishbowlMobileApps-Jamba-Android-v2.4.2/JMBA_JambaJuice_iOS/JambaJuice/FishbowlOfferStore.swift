//
//  FishbowlStoreRestriction.swift
//  JambaJuice
//
//  Created by VT010 on 2/21/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct FishbowlOfferStore{
    var  displayname: String?
    var  storecode: String?
    var  storeid: String?
    var  storename: String?
    var  storeAddress: String?
    
    public init(json:JSON){
        displayname = json["displayname"].stringValue
        storecode = json["storecode"].stringValue
        storeid = json["storeid"].stringValue
        storename = json["storename"].stringValue
        storeAddress = ""
    }
    
    public init(storename:String?, displayname:String?, storecode:String?, storeid:String?, storeAddress: String?) {
        self.storename = storename
        self.displayname = displayname
        self.storecode = storecode
        self.storeid = storeid
        self.storeAddress = storeAddress
    }
}
