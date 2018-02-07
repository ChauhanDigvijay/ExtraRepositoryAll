//
//  FishbowlDigitalEventList.swift
//  JambaJuice
//
//  Created by VT010 on 2/24/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct FishbowlDigitalEventList{
    var digitalEventList:[FishbowlDigitalEvent]?
    var digitalEvents:[FishbowlDigitalEvent]?
    var message:String?
    var successFlag:Bool?
    
    public init(json:JSON){
        digitalEventList = json["digitalEventList"].arrayValue.map{fishbowlDigitalEvent in FishbowlDigitalEvent(json:fishbowlDigitalEvent)}
        digitalEvents = json["digitalEvents"].arrayValue.map{fishbowlDigitalEvent in FishbowlDigitalEvent(json:fishbowlDigitalEvent)}
        message = json["message"].string
        successFlag = json["successFlag"].bool
    }
}
