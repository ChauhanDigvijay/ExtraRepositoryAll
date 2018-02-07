//
//  Feedback.swift
//  JambaJuice
//
//  Created by Taha Samad on 8/24/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

class Feedback {
    
    static let parseClassName = "Feedback"
    
    var emailAddress: String?
    var phoneNumber: String?
    var feedbackString = ""
    var feedbackType = FeedbackType.General
    
    func serializeAsParseObject() -> PFObject {
        let parseObject = PFObject(className: Feedback.parseClassName)
        parseObject["emailAddress"] = emailAddress ?? ""
        parseObject["phoneNumber"]  = phoneNumber ?? ""
        parseObject["feedbackType"] = feedbackType.rawValue
        parseObject["feedback"]     = feedbackString
        parseObject["OS"]           = "iOS"
        let osVersion               = NSProcessInfo.processInfo().operatingSystemVersion
        parseObject["OSVersion"]    = "\(osVersion.majorVersion).\(osVersion.minorVersion).\(osVersion.patchVersion)"
        parseObject["appVersion"]   = "\(UIApplication.versionNumber()).\(UIApplication.buildNumber())"
        return parseObject
    }
    
}
