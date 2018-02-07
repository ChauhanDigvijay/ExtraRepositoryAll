//
//  OloFaveService.swift
//  Olo
//
//  Created by Taha Samad on 06/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

public typealias OloFaveCallback = (favorites: [OloFave], error: NSError?) -> Void


public class OloFaveService {
    
    public class func faves(callback: OloFaveCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            let error = OloUtils.error("User is not authenticated.", code: 0)
            callback(favorites: [], error: error)
            return
        }

        OloService.get("/users/\(authToken!)/faves", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(favorites: [], error: error)
                return
            }
            let faves = response["faves"].arrayValue.map { item in OloFave(json: item) }
            callback(favorites: faves, error: error)
        }
    }
    
    public class func createFave(basketId: String, description: String, callback: OloFaveCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            let error = OloUtils.error("User is not authenticated.", code: 0)
            callback(favorites: [], error: error)
            return
        }
        let parameters = ["basketid": basketId, "description": description]
        OloService.post("/users/\(authToken!)/faves", parameters: parameters) { (response, error) -> Void in
            if error != nil {
                callback(favorites: [], error: error)
                return
            }
            let faves = response["faves"].arrayValue.map { item in OloFave(json: item) }
            callback(favorites: faves, error: error)
        }
    }
    
    public class func deleteFave(faveId: Int64, callback: OloErrorCallback) {
        let authToken = OloSessionService.authToken
        if authToken == nil {
            let error = OloUtils.error("User is not authenticated.", code: 0)
            callback(error: error)
            return
        }
        OloService.delete("/users/\(authToken!)/faves/\(faveId)", parameters: nil) { (response, error) -> Void in
            callback(error: error)
        }
    }
    
}
