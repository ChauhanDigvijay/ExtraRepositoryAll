//
//  InCommUtils.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/6/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public let InCommErrorTechnicalDescriptionKey = "TechnicalDescription"

public enum InCommErrorDomain: String {
    case GeneralError = "InCommError"
    case ServiceErrorFromJSONBody = "InCommServiceErrorFromJSONBody"
    case ServiceErrorFromHTTPStatusCode = "InCommServiceErrorFromHTTPStatusCode"
    case ServiceErrorFromInvalidSessionToken = "InCommServiceErrorFromInvalidSessionToken"
}

class InCommUtils {
    
    class func error(description: String, code: Int, technicalDescription: String? = nil, domain: String? = nil) -> NSError {
        var userInfo = [
            NSLocalizedDescriptionKey: description
        ]
        if technicalDescription != nil {
            userInfo[InCommErrorTechnicalDescriptionKey] = technicalDescription
        }
        else {
            userInfo[InCommErrorTechnicalDescriptionKey] = description
        }
        let errorDomain = domain != nil ? domain! : InCommErrorDomain.GeneralError.rawValue
        return NSError(domain: errorDomain, code: code, userInfo: userInfo)
    }
    
}
