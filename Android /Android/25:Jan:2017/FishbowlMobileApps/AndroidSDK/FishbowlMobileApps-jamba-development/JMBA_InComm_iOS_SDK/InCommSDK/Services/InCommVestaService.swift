//
//  InCommVestaService.swift
//  InCommSDK
//
//  Created by Taha Samad on 9/17/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public typealias InCommVestaWebSession = (vestaOrgId: String, vestaWebSessionId: String)

public typealias InCommVestaWebSessionCallback = (vestaWebSession: InCommVestaWebSession?, error: NSError?) -> Void

public class InCommVestaService {
    public class func vestaWebSession(callback: InCommVestaWebSessionCallback) {
        InCommService.get("/VestaWebSessionId", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(vestaWebSession: nil, error: error)
                return
            }
            let orgId = response["VestaOrgId"].stringValue
            let webSessionId = response["VestaWebSessionId"].stringValue
            let vestaWebSession = (vestaOrgId: orgId, vestaWebSessionId: webSessionId)
            callback(vestaWebSession: vestaWebSession, error: nil)
        }
    }
}