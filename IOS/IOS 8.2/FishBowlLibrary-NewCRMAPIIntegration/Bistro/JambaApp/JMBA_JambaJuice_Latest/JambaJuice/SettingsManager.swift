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
}

class SettingsManager: NSObject {
   
    class func setting(key: String) -> AnyObject? {
        NSUserDefaults.standardUserDefaults().synchronize()
        return NSUserDefaults.standardUserDefaults().objectForKey(key)
    }

    class func setSetting(key: String, value: AnyObject) {
        NSUserDefaults.standardUserDefaults().setObject(value, forKey: key)
        NSUserDefaults.standardUserDefaults().synchronize()
    }

    class func deleteSetting(key: String) {
        NSUserDefaults.standardUserDefaults().removeObjectForKey(key)
        NSUserDefaults.standardUserDefaults().synchronize()
    }
    
    // MARK: Helpers
    
    class func setting(key: SettingKey) -> AnyObject? {
        return setting(key.rawValue)
    }
    
    class func setSetting(key: SettingKey, value: AnyObject) {
        setSetting(key.rawValue, value: value)
    }
    
    class func deleteSetting(key: SettingKey) {
        deleteSetting(key.rawValue)
    }
    
}
