//
//  SessionExpirationService.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/16/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

// NSObject required for timer target selector
class SessionExpirationService: NSObject {

    static let sharedInstance = SessionExpirationService()

    static let secondsLeftKey = "secondsLeftKey"

    static let secondsLeftNotificatioName          = "SessionExpiration::SecondsLeft"
    static let sessionAboutToExpireNotificatioName = "SessionExpiration::SessionAboutToExpire"
    static let sessionExpiredNotificatioName       = "SessionExpiration::SessionExpired"
    static let sessionResetNotificationName        = "SessionExpiration::Reset"

    private let sessionExpirationSeconds = 60
    private let sessionFirstWarningSeconds = 30
    private let sessionLastWarningSeconds = 10

    private(set) var secondsLeft = 0
    private var timer: NSTimer?

    /// Start new kiosk session
    func startKioskSession() {
        secondsLeft = sessionExpirationSeconds
        timer?.invalidate()
        timer = NSTimer.scheduledTimerWithTimeInterval(1, target: self, selector: "timerTick:", userInfo: nil, repeats: true)
        postNotification(SessionExpirationService.sessionResetNotificationName)
    }

    /// Terminate current kiosk session without starting a new one
    func terminateKioskSession() {
        timer?.invalidate()
        timer = nil
        postNotification(SessionExpirationService.sessionExpiredNotificatioName)
    }

    /// When user activity happens, session is reset
    func trackUserActivity() {
        secondsLeft = sessionExpirationSeconds
        postNotification(SessionExpirationService.sessionResetNotificationName)
    }

    private func postNotification(name: String) {
        UIApplication.inMainThread {
            log.info("Sending session expiration notification: \(name) (seconds left: \(self.secondsLeft))")
            NSNotificationCenter.defaultCenter().postNotificationName(name, object: nil, userInfo: [SessionExpirationService.secondsLeftKey: self.secondsLeft])
        }
    }


    // MARK: Timer callback

    /// On every timer tick, check if session is about to expire or has expired already and post the corresponding notification
    /// Will post 'expired' notification repeatedly until the session is terminated
    func timerTick(timer: NSTimer) {
        secondsLeft = max(0, secondsLeft - 1)

        // Send 'senconds left' update, and continue
        if secondsLeft < sessionFirstWarningSeconds {
            postNotification(SessionExpirationService.secondsLeftNotificatioName)
        }

        // Send 'season about to expire' warning
        if secondsLeft == sessionLastWarningSeconds {
            postNotification(SessionExpirationService.sessionAboutToExpireNotificatioName)
        } else if secondsLeft == 0 {
            terminateKioskSession()
        }
    }

}
