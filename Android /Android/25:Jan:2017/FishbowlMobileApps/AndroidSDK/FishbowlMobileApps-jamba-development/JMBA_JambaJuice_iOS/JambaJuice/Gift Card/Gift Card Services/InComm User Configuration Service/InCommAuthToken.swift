//
//  ClpInCommAuthTokenResponse.swift
//  JambaJuice
//
//  Created by VT010 on 10/22/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public typealias InCommDictionary = [NSObject : AnyObject]

public struct InCommAuthToken{
    public var successFlag:Bool
    public var inCommToken:String
    
    public init(successFlag:Bool,inCommToken:String){
        self.successFlag = successFlag
        self.inCommToken = inCommToken
    }
    
    public init(responseData:InCommDictionary!){
        successFlag = responseData["successFlag"]! as? Bool ?? false
        inCommToken = responseData["inCommToken"]! as! String
    }
    
}
