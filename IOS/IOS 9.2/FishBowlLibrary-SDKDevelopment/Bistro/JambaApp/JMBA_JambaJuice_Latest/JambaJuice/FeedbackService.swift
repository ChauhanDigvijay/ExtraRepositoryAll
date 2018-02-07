//
//  FeedbackService.swift
//  JambaJuice
//
//  Created by Taha Samad on 8/25/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

typealias FeedbackSubmissionCallback = (error: NSError?) -> Void

class FeedbackService {
    
    class func submitFeedback(feedback: Feedback, callback: FeedbackSubmissionCallback) {
        let feedbackParseObject = feedback.serializeAsParseObject()
        feedbackParseObject.saveInBackgroundWithBlock { (succeeded, error) -> Void in
            if !succeeded.boolValue || error != nil {
                clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")

                callback(error: NSError(description: "Unexpected error occured while submitting feedback."))
                return
            }
            callback(error: nil)
        }
    }
    
}