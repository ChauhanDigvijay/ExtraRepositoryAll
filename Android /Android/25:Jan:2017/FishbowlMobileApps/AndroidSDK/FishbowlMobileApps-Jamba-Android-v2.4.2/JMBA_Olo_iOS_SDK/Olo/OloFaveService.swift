//
//  OloFaveService.swift
//  Olo
//
//  Created by Taha Samad on 06/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

public typealias OloFaveCallback = (_ favorites: [OloFave], _ error: NSError?) -> Void


open class OloFaveService {
    
    open class func faves(_ callback: @escaping OloFaveCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            let error = OloUtils.error("User is not authenticated.", code: 0)
            callback([], error)
            return
        }

        OloService.get("/users/\(authToken!)/faves", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let faves = response["faves"].arrayValue.map { item in OloFave(json: item) }
            callback(faves, error)
        }
    }
    
    open class func createFave(_ basketId: String, description: String, callback: @escaping OloFaveCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            let error = OloUtils.error("User is not authenticated.", code: 0)
            callback([], error)
            return
        }
        let parameters = ["basketid": basketId, "description": description]
        OloService.post("/users/\(authToken!)/faves", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let faves = response["faves"].arrayValue.map { item in OloFave(json: item) }
            callback(faves, error)
        }
    }
    
    open class func deleteFave(_ faveId: Int64, callback: @escaping OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            let error = OloUtils.error("User is not authenticated.", code: 0)
            callback(error)
            return
        }
        OloService.delete("/users/\(authToken!)/faves/\(faveId)", parameters: nil) { (response, error) -> Void in
            callback(error)
        }
    }
    
}
