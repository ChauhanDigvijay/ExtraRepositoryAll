//
//  SubmitInCommOrderIdDetails.swift
//  JambaJuice
//
//  Created by VT010 on 11/14/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

public struct SubmitInCommOrderIdDetails{
    public var customerId:String
    
    public init(customerId: String){
      self.customerId = customerId
    }
    
    public func serializeAsJSONDictionary() -> ClpJSONDictionary{
        var jsonDict = ClpJSONDictionary()
        jsonDict["customerId"] = customerId
        return jsonDict
    }
}
