//
//  NSError+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 12/15/14.
//  Copyright (c) 2014 hathway. All rights reserved.
//

import Foundation

private var _applicationErrorDomain = "com.wearehathway.hdk"

extension NSError {
    
    /// Configure application error domain
    public class var applicationErrorDomain: String {
        get {
        return _applicationErrorDomain
        }
        set {
            _applicationErrorDomain = applicationErrorDomain
        }
    }
    
    /// Create a new NSError with default domain and code, no failure reason
    /// - parameter description: Error description
    /// - returns: An initialized NSError instance
    public convenience init(description: String) {
        let userInfo = [
            NSLocalizedDescriptionKey: description
        ]
        self.init(domain: NSError.applicationErrorDomain, code: -1, userInfo: userInfo)
    }
    
    /// Create a new NSError with default domain, no failure reason
    /// - parameter description: Error description
    /// - parameter code: Error code
    /// - returns: An initialized NSError instance
    public convenience init(description: String, code: Int) {
        let userInfo = [
            NSLocalizedDescriptionKey: description
        ]
        self.init(domain: NSError.applicationErrorDomain, code: code, userInfo: userInfo)
    }
    
    /// Create a new NSError with default code
    /// - parameter description: Error description
    /// - parameter failureReason: Error failure reason message
    /// - parameter domain: Custom error domain
    /// - returns: An initialized NSError instance
    public convenience init(description: String, failureReason: String, domain: String) {
        let userInfo = [
            NSLocalizedDescriptionKey: description,
            NSLocalizedFailureReasonErrorKey: failureReason
        ]
        self.init(domain: domain, code: -1, userInfo: userInfo)
    }
    
    /// Create a new NSError
    /// - parameter description: Error description
    /// - parameter failureReason: Error failure reason message
    /// - parameter domain: Custom error domain
    /// - parameter code: Error code
    /// - returns: An initialized NSError instance
    public convenience init(description: String, failureReason: String, domain: String, code: Int) {
        let userInfo = [
            NSLocalizedDescriptionKey: description,
            NSLocalizedFailureReasonErrorKey: failureReason
        ]
        self.init(domain: domain, code: code, userInfo: userInfo)
    }
    
    /// Predefined error: Not Implemented
    /// Usage: NSError.NotImplemented
    public class var NotImplemented: NSError {
        get {
            return NSError(description: "Not implemented")
        }
    }
    
}
