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
    class func trackUserAccess(user: User, action: String) {
        let auditUser = AuditUser(user: user, action: action)
        saveAuditRecord(auditUser.serializeAsParseObject())
    }

    // Track orders submitted from the app
    class func trackOrder(basket: Basket, orderStatus: OrderStatus) {
        let auditOrder = AuditOrder(basket: basket, orderStatus: orderStatus, user: UserService.sharedUser)
        saveAuditRecord(auditOrder.serializeAsParseObject())
    }

    private class func saveAuditRecord(parseObject: PFObject) {
        let osVersion = NSProcessInfo.processInfo().operatingSystemVersion
        parseObject["device_name"] = UIDevice.currentDevice().name
        parseObject["device_model"] = UIDevice.currentDevice().model
        parseObject["device_os_version"] = "\(osVersion.majorVersion).\(osVersion.minorVersion).\(osVersion.patchVersion)"
        parseObject["application_version"] = UIApplication.versionNumber()
        parseObject["application_build"] = UIApplication.buildNumber()
        parseObject.saveEventually()
    }
}
