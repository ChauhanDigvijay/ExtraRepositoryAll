//
//  PushNotificationResponse.swift
//  JambaJuice
//
//  Created by VT010 on 3/14/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct PushNotificationResponse{
    public var aps:PushApsResponse?
    public var clpnid:Int?
    public var custid:String?
    public var mt:String?
    public var ntype:String?
    public var offerid:String?
    public var promoCode:String?
    
    public init(json:JSON){
        aps = PushApsResponse(json: json["aps"])
        clpnid = json["clpnid"].int
        custid = json["custid"].string
        mt = json["mt"].string
        ntype = json["ntype"].string
        offerid = json["offerid"].string
        promoCode = json["pc"].string
    }
    
}
