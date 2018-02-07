//
//  FeedbackService.swift
//  JambaJuice
//
//  Created by Taha Samad on 8/25/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

typealias FeedbackSubmissionCallback = (_ error: NSError?) -> Void

class FeedbackService {
    
    class func submitFeedback(_ feedback: Feedback, callback: @escaping FeedbackSubmissionCallback) {
        let feedbackParseObject = feedback.serializeAsParseObject()
        feedbackParseObject.saveInBackground { (succeeded, error) -> Void in
            if !succeeded || error != nil {
                //clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
//                callback(NSError(coder: "Unexpected error occured while submitting feedback."))
                callback(NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Unexpected error occured while submitting feedback."]))
                return
            }
            callback(nil)
        }
    }
    
}
