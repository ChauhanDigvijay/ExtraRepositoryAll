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
        PFObject.unpinAllObjectsInBackgroundWithName(User.parseClassName)
    }
    
    class func persistUserSession(user: User) {
        UIApplication.inBackground {
            do {
                // Unpin any existing sessions, ignore if none present
                _ = try? PFObject.unpinAllObjectsWithName(User.parseClassName)
                
                // Save new user session
                let userSession = user.serializeAsParseObject()
                try userSession.pinWithName(User.parseClassName)
                
                // Save profile image on Settings
                SettingsManager.setSetting("\(user.emailAddress)::profileImage", value: user.profileImageName)
            } catch {
                // TODO: Ignore? next time user tries to run the app they will be logged out
            }
        }
    }
    
    class func loadUserSession(callback: (user: User?) -> Void) {
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
                    user.profileImageName = SettingsManager.setting("\(user.emailAddress)::profileImage") as? String ?? "apple"
                    callback(user: user)
                } else {
                    callback(user: nil)
                }
            } catch {
                callback(user: nil)
            }
        }
    }
    
}
