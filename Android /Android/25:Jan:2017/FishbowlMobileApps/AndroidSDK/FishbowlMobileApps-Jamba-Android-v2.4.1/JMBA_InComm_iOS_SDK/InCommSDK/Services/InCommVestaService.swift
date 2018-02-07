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

public typealias InCommVestaWebSessionCallback = (_ vestaWebSession: InCommVestaWebSession?, _ error: NSError?) -> Void

open class InCommVestaService {
    open class func vestaWebSession(_ callback: @escaping InCommVestaWebSessionCallback) {
        InCommService.get("/VestaWebSessionId", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            let orgId = response["VestaOrgId"].stringValue
            let webSessionId = response["VestaWebSessionId"].stringValue
            let vestaWebSession = (vestaOrgId: orgId, vestaWebSessionId: webSessionId)
            callback(vestaWebSession, nil)
        }
    }
}
