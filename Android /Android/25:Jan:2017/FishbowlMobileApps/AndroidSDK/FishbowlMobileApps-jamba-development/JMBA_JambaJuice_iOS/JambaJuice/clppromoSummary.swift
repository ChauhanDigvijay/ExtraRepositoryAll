//
//  clppromoSummary.swift
//  JambaJuice
//
//  Created by Puneet  on 3/21/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct clppromoSummary {
    
    public var type: String
    public var promoCode : String
    public var StatusCode : Int

    
    public init(json: JSON) {
        
        type     = json["successFlag"].stringValue
        StatusCode     = json["statuscode"].intValue

        if (type == "false")
        {
           promoCode = ""
        }
        else
        {
            
            promoCode = json["promoCode"].stringValue
        }
    }
    
}
