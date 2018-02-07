//
//  FishbowlDigitalEvent.swift
//  JambaJuice
//
//  Created by VT010 on 2/24/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct FishbowlDigitalEvent{
    var eventTypeName:String?
    var eventID:String?
    
    public init(json:JSON){
        eventTypeName = json["eventTypeName"].string
        eventID = json["eventID"].string
    }
}
