//
//  FishbowlMobileSettingResponse.swift
//  JambaJuice
//
//  Created by VT010 on 2/24/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct FishbowlMobileSettingResponse{
    var message:String?
    var digitalEventList:FishbowlDigitalEventList?
    var successFlag:Bool?
    var mobSettingList:FishbowlMobileSettingsList?
    
    public init(json: JSON){
        message = json["message"].string
        digitalEventList = FishbowlDigitalEventList(json:json["digitalEventList"])
        successFlag = json["successFlag"].bool
        mobSettingList = FishbowlMobileSettingsList(json: json["mobSettingList"])
    }
    
}
