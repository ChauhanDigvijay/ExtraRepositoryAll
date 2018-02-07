//
//  SessionManager.swift
//  JambaJuice
//
//  Created by Taha Samad on 22/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Parse
import OloSDK

class SessionPersistenceManager: NSObject {

    class func clearUserSession() {
        // Unpin any existing sessions
        PFObject.unpinAllObjectsInBackground(withName: User.parseClassName)
    }
    
    class func persistUserSession(_ user: User) {
        UIApplication.inBackground {
            do {
                // Unpin any existing sessions, ignore if none present
                _ = PFObject.unpinAllObjectsInBackground(withName: User.parseClassName)
                // Save new user session
                let userSession = user.serializeAsParseObject()
                try userSession.pin(withName: User.parseClassName)
                // Save profile image on Settings
                SettingsManager.setSetting("\(String(describing: user.emailAddress))::profileImage", value: user.profileImageName as AnyObject)
            } catch {
                // TODO: Ignore? next time user tries to run the app they will be logged out
            }
        }
    }
    
    class func loadUserSession(_ callback: @escaping (_ user: User?) -> Void) {
        UIApplication.inBackground {
            let query = PFQuery(className: User.parseClassName)
            query.fromLocalDatastore()
            query.includeKey("favoriteStore")
            query.includeKey("currentStore")
            do {
                let sessions = try query.findObjects()
                if let userSession = sessions.first {
                    let user = User(parseObject: userSession)
                    // Load profile image from Settings
                    user.profileImageName = SettingsManager.setting("\(String(describing: user.emailAddress))::profileImage") as? String ?? "apple"
                    callback(user)
                } else {
                    callback(nil)
                }
            } catch {
                callback(nil)
            }
        }
    }
    
}
