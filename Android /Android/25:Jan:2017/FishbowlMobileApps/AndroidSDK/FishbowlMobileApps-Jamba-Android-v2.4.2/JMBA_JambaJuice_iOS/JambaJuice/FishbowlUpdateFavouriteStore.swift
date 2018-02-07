//
//  FishbowlUpdateFavouriteStore.swift
//  JambaJuice
//
//  Created by VT016 on 09/02/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation

public struct FishbowlUpdateFavouriteStore {
    
    var memberid: String            = Bundle.main.bundleIdentifier ?? ""
    var storeCode: String           = ""
    
    public init(memberid:String, storeCode:String){
        self.memberid = memberid
        self.storeCode = storeCode
    }
    
    public func serializeAsJSONDictionary() -> FishbowlJSONDictionary{
        var jsonDict = FishbowlJSONDictionary()
        jsonDict["memberid"]  = memberid as AnyObject?
        jsonDict["storeCode"] = storeCode as AnyObject?
        return jsonDict
    }
}
