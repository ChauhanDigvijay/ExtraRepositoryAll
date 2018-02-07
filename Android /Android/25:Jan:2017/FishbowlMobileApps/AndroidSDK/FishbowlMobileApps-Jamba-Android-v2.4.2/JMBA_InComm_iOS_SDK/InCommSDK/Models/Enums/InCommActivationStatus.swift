//
//  InCommActivationStatus.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public enum InCommActivationStatus: String {
    case Activated = "Activated"
    case Cancelled = "Cancelled"
    case Delayed = "Delayed"
    case Error = "Error"
    case Pending = "Pending"
    case PendingBulk = "PendingBulk"
    case Processing = "Processing"
}