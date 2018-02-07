//
//  InCommOrderStatusDetails.Swift
//  JambaJuice
//
//  Created by VT010 on 11/14/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

public struct InCommOrderStatusDetails{
    enum OrderStatus:Int{
        case started = 1
        case cancelled = 2
        case completed = 3
        case failed = 4
    }
    
    public var inCommOrderId:String?
    public var orderStatus:Int?
    public var customerId:String?
    public var errorReason:String?
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary{
        var jsonDict = InCommJSONDictionary()
        jsonDict["inCommOrderId"] = inCommOrderId as AnyObject?
        jsonDict["orderStatus"] = orderStatus as AnyObject?
        jsonDict["errorReason"] = errorReason as AnyObject?
        jsonDict["customerId"] = customerId as AnyObject?
        return jsonDict
    }
}
