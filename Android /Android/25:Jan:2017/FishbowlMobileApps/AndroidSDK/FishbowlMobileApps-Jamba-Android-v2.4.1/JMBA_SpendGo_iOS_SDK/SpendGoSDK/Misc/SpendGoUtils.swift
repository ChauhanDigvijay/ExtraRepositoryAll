//
//  SpendGoUtils.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 11/05/2015.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import UIKit

public let SpendGoErrorTechnicalDescriptionKey = "TechnicalDescription"

public enum SpendGoErrorDomain: String {
    case GeneralError = "SpendGoError"
    case ServiceErrorFromJSONBody = "SpendGoServiceErrorFromJSONBody"
    case ServiceErrorFromHTTPStatusCode = "SpendGoServiceErrorFromHTTPStatusCode"
}

class SpendGoUtils {
  

    class func error(_ description: String, code: Int, technicalDescription: String? = nil, domain: String? = nil) -> NSError {
        var userInfo = [
            NSLocalizedDescriptionKey: description
        ]
        //surendra
        

//        clpAnalyticsService.sharedInstance.receiveRemoteNotification(userInfo);

        if technicalDescription != nil {
            userInfo[SpendGoErrorTechnicalDescriptionKey] = technicalDescription
        }
        else {
            userInfo[SpendGoErrorTechnicalDescriptionKey] = description
        }
        let errorDomain = domain != nil ? domain! : SpendGoErrorDomain.GeneralError.rawValue
        return NSError(domain: errorDomain, code: code, userInfo: userInfo)
    }
    
    class func error(_ description: String) -> NSError {
        return error(description, code: -1)
    }
    
}
