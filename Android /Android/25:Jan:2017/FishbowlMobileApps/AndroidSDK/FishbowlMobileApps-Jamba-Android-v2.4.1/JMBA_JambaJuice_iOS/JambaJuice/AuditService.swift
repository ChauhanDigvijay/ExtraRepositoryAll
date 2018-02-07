//
//  AuditService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 8/31/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

class AuditService {

    // Track user access to the app
    class func trackUserAccess(_ user: User, action: String) {
        let auditUser = AuditUser(user: user, action: action)
        saveAuditRecord(auditUser.serializeAsParseObject())
    }
    
    // Track orders submitted from the app
    class func trackOrder(_ basket: Basket, orderStatus: OrderStatus) {
        let auditOrder = AuditOrder(basket: basket, orderStatus: orderStatus, user: UserService.sharedUser)
        saveAuditRecord(auditOrder.serializeAsParseObject())
    }

    fileprivate class func saveAuditRecord(_ parseObject: PFObject) {
        let osVersion = ProcessInfo.processInfo.operatingSystemVersion
        parseObject["device_name"] = UIDevice.current.name
        parseObject["device_model"] = UIDevice.current.model
        parseObject["device_os_version"] = "\(osVersion.majorVersion).\(osVersion.minorVersion).\(osVersion.patchVersion)"
        parseObject["application_version"] = UIApplication.versionNumber()
        parseObject["application_build"] = UIApplication.buildNumber()
        parseObject.saveEventually()
    }
}
