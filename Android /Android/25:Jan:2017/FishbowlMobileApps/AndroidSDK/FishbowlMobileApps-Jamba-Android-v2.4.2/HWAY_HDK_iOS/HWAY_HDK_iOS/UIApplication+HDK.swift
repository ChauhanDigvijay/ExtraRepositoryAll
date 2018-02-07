//
//  UIApplication+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 5/6/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import UIKit

extension UIApplication {
    
    /// Return the short bundle version string (usually application version)
    public class func versionNumber() -> String {
        return Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as! String
    }
    
    /// Return the bundle version string (usually the application build number)
    public class func buildNumber() -> String {
        return Bundle.main.object(forInfoDictionaryKey: "CFBundleVersion") as! String
    }
    
    /// Execute a given block in the main thread
    public class func inMainThread(_ block: @escaping () -> Void) {
        DispatchQueue.main.async {
            block()
        }
    }
    
    /// Execute a given block in background
    public class func inBackground(_ block: @escaping () -> Void) {
        DispatchQueue.global(qos: DispatchQoS.QoSClass.default).async {
            block()
        }
    }

    /// Execute a given block in the main thread after a given delay
    public class func afterDelay(_ interval: TimeInterval, block: @escaping () -> Void) {
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double(Int64(interval * Double(NSEC_PER_SEC))) / Double(NSEC_PER_SEC)) {
            block()
        }
    }
    
    /// Execute a given block in background after a given delay
    public class func afterDelayInBackground(_ interval: TimeInterval, block: @escaping () -> Void) {
        DispatchQueue.global(qos: DispatchQoS.QoSClass.default).asyncAfter(deadline: DispatchTime.now() + Double(Int64(interval * Double(NSEC_PER_SEC))) / Double(NSEC_PER_SEC)) {
                block()
        }
    }
    
}

/// Global: Assert callee is running in main thread
public func assertMainThread() {
    assert(Thread.current.isMainThread, "Is main thread.")
}
