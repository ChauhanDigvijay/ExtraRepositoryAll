//
//  InCommPromoOrder.swift
//  JambaJuice
//
//  Created by VT010 on 10/24/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//


import SwiftyJSON

public struct InCommPromoOrder{
   
    public var query:InCommQuery
    public var result:InCommResult
    
    public init(query: InCommQuery, result: InCommResult){
        self.query = query
        self.result = result
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary{
        var jsonDict = InCommJSONDictionary()
        jsonDict["query"]  = query.serializeAsJSONDictionary() as AnyObject?
        jsonDict["result"] = result.serializeAsJSONDictionary() as AnyObject?
        return jsonDict
    }

}
