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
        return NSBundle.mainBundle().objectForInfoDictionaryKey("CFBundleShortVersionString") as! String
    }
    
    /// Return the bundle version string (usually the application build number)
    public class func buildNumber() -> String {
        return NSBundle.mainBundle().objectForInfoDictionaryKey("CFBundleVersion") as! String
    }
    
    /// Execute a given block in the main thread
    public class func inMainThread(block: () -> Void) {
        dispatch_async(dispatch_get_main_queue()) {
            block()
        }
    }
    
    /// Execute a given block in background
    public class func inBackground(block: () -> Void) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            block()
        }
    }

    /// Execute a given block in the main thread after a given delay
    public class func afterDelay(interval: NSTimeInterval, block: () -> Void) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW,
            Int64(interval * Double(NSEC_PER_SEC))), dispatch_get_main_queue()) {
            block()
        }
    }
    
    /// Execute a given block in background after a given delay
    public class func afterDelayInBackground(interval: NSTimeInterval, block: () -> Void) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW,
            Int64(interval * Double(NSEC_PER_SEC))), dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
                block()
        }
    }
    
}

/// Global: Assert callee is running in main thread
public func assertMainThread() {
    assert(NSThread.currentThread().isMainThread, "Is main thread.")
}
