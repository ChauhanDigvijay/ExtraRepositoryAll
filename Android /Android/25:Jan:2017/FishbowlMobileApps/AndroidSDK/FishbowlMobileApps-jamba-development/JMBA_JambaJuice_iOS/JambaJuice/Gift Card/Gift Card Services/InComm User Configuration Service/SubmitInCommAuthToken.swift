//
//  ClpInCommAuthTokenRequest.swift
//  JambaJuice
//
//  Created by VT010 on 10/22/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct SubmitInCommAuthToken{
    public var spendGoKey:String
    public var spendGoAuthorizationToken:String
    public var spendGoId:String
    
    
    public init(spendGoKey:String, spendGoAuthorizationToken:String, spendGoId:String){
        self.spendGoKey = spendGoKey
        self.spendGoAuthorizationToken = spendGoAuthorizationToken
        self.spendGoId = spendGoId
    }
    
    public func serializeAsJSONDictionary() -> ClpJSONDictionary{
        var jsonDict = ClpJSONDictionary()
        jsonDict["key"] = spendGoKey
        jsonDict["authorization"] = spendGoAuthorizationToken
        jsonDict["spendgoId"] = spendGoId
        return jsonDict
    }
}
