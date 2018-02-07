//
//  FeedbackType.swift
//  JambaJuice
//
//  Created by Taha Samad on 8/24/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

enum FeedbackType: String {
    case General = "General"
    case BugOrIssue = "Bug / Issue"
    case NewFeatureRequest = "New Feature Request"
    
    init?(index: UInt) {
        switch index {
        case 0:
            self = .General
        case 1:
            self = .BugOrIssue
        case 2:
            self = .NewFeatureRequest
        default:
            assert(false, "Unexpected index in enum init")
            return nil
        }
    }
}
