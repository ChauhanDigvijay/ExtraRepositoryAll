//
//  FishbowlLogin.swift
//  JambaJuice
//
//  Created by VT016 on 09/02/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation

public struct FishbowlLogin {
    
    var username: String            = ""
    var deviceId: String            = ""
    
    public init(username:String, deviceId:String){
        self.username = username
        self.deviceId = deviceId
    }
    
    public func serializeAsJSONDictionary() -> FishbowlJSONDictionary{
        var jsonDict = FishbowlJSONDictionary()
        jsonDict["username"] = username as AnyObject?
        jsonDict["deviceId"]  = deviceId as AnyObject?
        return jsonDict
    }
}
