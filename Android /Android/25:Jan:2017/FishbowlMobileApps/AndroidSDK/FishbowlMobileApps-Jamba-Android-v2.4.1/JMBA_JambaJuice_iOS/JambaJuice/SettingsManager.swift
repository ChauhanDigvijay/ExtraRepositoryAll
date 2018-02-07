//
//  SettingsManager.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/15/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//
//  Wrapper class for user settings, might be stored in local database later on.
//  Currently using NSUserDefaults


import Foundation

enum SettingKey: String {
    case LastProductUpdate        = "JambaJuice::LastProductUpdate"
    case LastSearch               = "JambaJuice::LastLocationSearched"
    case UserOnboarded            = "JambaJuice::UserOnboarded"
    case UserOnboardedNationwide  = "JambaJuice::UserOnboardedNationwide"
    case FirstTimeLaunch          = "JambaJuice::FirstTimeLaunch"
    case FirstTimePush            = "JambaJuice::FirstTimePush"
    case FirstTimeLaunchForUser   = "JambaJuice::FirstTimeLaunchForUser"
}

class SettingsManager: NSObject {
   
    class func setting(_ key: String) -> AnyObject? {
        UserDefaults.standard.synchronize()
        return UserDefaults.standard.object(forKey: key) as AnyObject?
    }

    class func setSetting(_ key: String, value: AnyObject) {
        UserDefaults.standard.set(value, forKey: key)
        UserDefaults.standard.synchronize()
    }

    class func deleteSetting(_ key: String) {
        UserDefaults.standard.removeObject(forKey: key)
        UserDefaults.standard.synchronize()
    }
    
    // MARK: Helpers
    
    class func setting(_ key: SettingKey) -> AnyObject? {
        return setting(key.rawValue)
    }
    
    class func setSetting(_ key: SettingKey, value: AnyObject) {
        setSetting(key.rawValue, value: value)
    }
    
    class func deleteSetting(_ key: SettingKey) {
        deleteSetting(key.rawValue)
    }
    
}
