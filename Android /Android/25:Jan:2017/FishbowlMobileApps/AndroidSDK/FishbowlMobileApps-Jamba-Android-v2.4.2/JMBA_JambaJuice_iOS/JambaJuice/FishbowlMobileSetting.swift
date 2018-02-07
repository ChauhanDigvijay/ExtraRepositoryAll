//
//  FishbowlMobileSetting.swift
//  JambaJuice
//
//  Created by VT010 on 2/24/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON


public struct FishbowlMobileSetting{
   var status:Bool?
   var tenantId:String?
   var settingGroup:String?
   var settingName:String?
   var settingId:String?
   var settingValue:String?
    
    public init(json:JSON){
        status = json["status"].bool
        tenantId = json["tenantId"].string
        settingGroup = json["settingGroup"].string
        settingName = json["settingName"].string
        settingId = json["settingId"].string
        settingValue = json["settingValue"].string
    }
}
