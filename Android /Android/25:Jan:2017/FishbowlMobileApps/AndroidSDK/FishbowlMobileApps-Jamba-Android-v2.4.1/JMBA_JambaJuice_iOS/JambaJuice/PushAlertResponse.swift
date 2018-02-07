//
//  PushAlertResponse.swift
//  JambaJuice
//
//  Created by VT010 on 6/16/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct PushAlertResponse{
    public var body:String?
    public init(json:JSON){
       let notifcationdata = json["body"].string
        // removing promo code if appended 
        let delimiter = "Promo Code: "
        let contentdata = notifcationdata?.components(separatedBy: delimiter)
        body = contentdata?[0]
    }
}
