//
//  FishbowlUpdateUserDevice.swift
//  JambaJuice
//
//  Created by VT016 on 09/02/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation

public struct FishbowlUpdateUserDevice{
    
    var appId: String               = Bundle.main.bundleIdentifier ?? ""
    var deviceId: String            = ""
    var deviceOSVersion: String     = UIDevice.current.systemVersion
    var memberid: String            = ""
    var pushToken: String           = ""
    var deviceType: String          = UIDevice.current.model
    
    public init(deviceId:String, memberid:String, pushToken:String){
        self.deviceId = deviceId
        self.memberid = memberid
        self.pushToken = pushToken
    }
    
    public func serializeAsJSONDictionary() -> FishbowlJSONDictionary{
        var jsonDict = FishbowlJSONDictionary()
        jsonDict["deviceId"]  = deviceId as AnyObject?
        jsonDict["memberid"] = memberid as AnyObject?
        jsonDict["pushToken"] = pushToken as AnyObject?
        jsonDict["appId"] = appId as AnyObject?
        jsonDict["deviceOSVersion"] = deviceOSVersion as AnyObject?
        jsonDict["deviceType"] = deviceType as AnyObject?
        return jsonDict
    }
}
