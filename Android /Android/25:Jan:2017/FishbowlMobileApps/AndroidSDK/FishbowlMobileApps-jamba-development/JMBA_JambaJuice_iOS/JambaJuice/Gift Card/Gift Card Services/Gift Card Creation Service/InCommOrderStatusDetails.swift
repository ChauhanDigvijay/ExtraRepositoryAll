//
//  InCommOrderStatusDetails.Swift
//  JambaJuice
//
//  Created by VT010 on 11/14/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

public struct InCommOrderStatusDetails{
    
    enum OrderStatus:Int{
        case STARTED = 1
        case CANCELLED = 2
        case COMPLETED = 3
        case FAILED = 4
    }
    
    public var inCommOrderId:String?
    public var orderStatus:Int?
    public var customerId:String?
    public var errorReason:String?
    
    public func serializeAsJSONDictionary() -> ClpJSONDictionary{
        var jsonDict = ClpJSONDictionary()
        jsonDict["inCommOrderId"] = inCommOrderId
        jsonDict["orderStatus"] = orderStatus
        jsonDict["errorReason"] = errorReason
        jsonDict["customerId"] = customerId
        return jsonDict
    }
}
