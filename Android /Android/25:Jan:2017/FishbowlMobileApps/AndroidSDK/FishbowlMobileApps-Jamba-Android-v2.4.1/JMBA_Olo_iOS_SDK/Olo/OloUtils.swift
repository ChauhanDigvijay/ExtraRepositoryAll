//
//  OloUtils.swift
//  Olo
//
//  Created by Taha Samad on 4/28/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation

public let OloErrorTechnicalDescriptionKey = "TechnicalDescription"

public enum OloErrorDomain: String {
    case GeneralError = "OloError"
    case ServiceErrorFromJSONBody = "OloServiceErrorFromJSONBody"
    case ServiceErrorFromHTTPStatusCode = "OloServiceErrorFromHTTPStatusCode"
}

class OloUtils {

    class func error(_ description: String, code: Int, technicalDescription: String? = nil, domain: String? = nil) -> NSError {
        var userInfo = [
            NSLocalizedDescriptionKey: description
        ]
        if technicalDescription != nil {
            userInfo[OloErrorTechnicalDescriptionKey] = technicalDescription
        }
        else {
            userInfo[OloErrorTechnicalDescriptionKey] = description
        }
        let errorDomain = domain != nil ? domain! : OloErrorDomain.GeneralError.rawValue
        return NSError(domain: errorDomain, code: code, userInfo: userInfo)
    }

}
