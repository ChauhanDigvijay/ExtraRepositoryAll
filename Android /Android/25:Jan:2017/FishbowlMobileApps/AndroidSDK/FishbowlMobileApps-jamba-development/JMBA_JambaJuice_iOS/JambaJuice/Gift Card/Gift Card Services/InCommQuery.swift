//
//  InCommQuery.swift
//  JambaJuice
//
//  Created by VT010 on 10/24/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct InCommQuery{
    public var amount:Double
    public var messageTo:String
    public var inCommInput: SubmitInCommAuthToken
    public var inCommToken:String
    
    public init(amount:Double, messageTo:String, inCommInput:SubmitInCommAuthToken, inCommToken:String){
        self.amount = amount
        self.messageTo = messageTo
        self.inCommInput = inCommInput
        self.inCommToken = inCommToken
    }
    public func serializeAsJSONDictionary() -> ClpJSONDictionary{
        var jsonDict = ClpJSONDictionary()
        jsonDict["amount"] = amount
        jsonDict["messageTo"] = messageTo
        jsonDict["incommInput"] =  inCommInput.serializeAsJSONDictionary()
        jsonDict["incommToken"] = inCommToken
      return jsonDict
    }
}