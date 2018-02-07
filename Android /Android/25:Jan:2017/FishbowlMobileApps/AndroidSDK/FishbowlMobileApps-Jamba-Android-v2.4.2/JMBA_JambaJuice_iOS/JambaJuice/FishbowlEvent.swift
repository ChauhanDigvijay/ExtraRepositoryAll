//
//  FishbowlEvent.swift
//  JambaJuice
//
//  Created by VT010 on 2/8/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation

public typealias FishbowlJSONDictionary = [String:Any]

public struct FishbowlEvent{
    var item_id:String?
    var item_name:String?
    var event_name:String?
    var action:String?
    var memberid:Int64?
    var lat:String
    var long:String
    var deviceType:String?
    var tenentid:String?
    var device_os_var:String?
    var event_source_id:String?
    
    public init(item_id:String?, item_name:String?, event_name:String?){
        self.item_id = item_id
        self.item_name = item_name
        self.event_name = event_name
        
        let defaults = UserDefaults.standard
        let userId = defaults.string(forKey: "fishbowl_customer_id") ?? ""
        var intMemberId:Int64 = 0
        if let intUserId = Int64(userId){
            intMemberId = intUserId
        }
        self.action = "AppEvent"
        self.memberid = intMemberId
        self.lat = userLocationLat
        self.long = userLocationLong
        self.deviceType = UIDevice.current.model
        self.tenentid = "1173"
        self.device_os_var = UIDevice.current.systemVersion
        self.event_source_id = FishbowlApiClassService.sharedInstance.getDeviceId()
    }
    
    public func serializeAsJSONDictionary() -> FishbowlJSONDictionary{
      var jsonDict = FishbowlJSONDictionary()
        jsonDict["item_id"]  = item_id as AnyObject?
        jsonDict["item_name"] = item_name as AnyObject?
        jsonDict["event_name"] = event_name as AnyObject?
        jsonDict["action"] = action as AnyObject?
        if memberid != nil{
            jsonDict["memberid"] = NSNumber(value: memberid! as Int64)
        }
        jsonDict["lat"] = lat as AnyObject?
        jsonDict["lon"] = long as AnyObject?
        jsonDict["device_type"] = deviceType as AnyObject?
        jsonDict["tenantid"] = tenentid as AnyObject?
        jsonDict["device_os_ver"] = device_os_var as AnyObject?
        jsonDict["event_source_id"] = event_source_id as AnyObject?
        return jsonDict
    }
}
