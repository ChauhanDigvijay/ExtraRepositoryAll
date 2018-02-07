//
//  NotificationService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 7/30/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation

class NotificationService {

    class func addTag(_ tag: String) {
       // UAirship.push().addTag(tag)
        AnalyticsService.trackEvent("push_notification", action: "add_tag", label: tag)
    }
    
    class func removeTag(_ tag: String) {
      //  UAirship.push().removeTag(tag)
        AnalyticsService.trackEvent("push_notification", action: "remove_tag", label: tag)
    }
    
}
