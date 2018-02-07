//
//  PushApsResponse.swift
//  JambaJuice
//
//  Created by VT010 on 3/14/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON


public struct PushApsResponse{
    public var alert:PushAlertResponse?
    public init(json:JSON){
        alert = PushAlertResponse(json: json["alert"])
    }
}
