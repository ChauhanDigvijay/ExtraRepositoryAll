//
//  InCommCreditCardTypeAndImage.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommCreditCardTypeAndImage {

    public var thumbnailImageUrl: String
    public var creditCardType: String
    
    public init(json: JSON) {
        thumbnailImageUrl = json["ThumbnailImageUrl"].stringValue
        creditCardType    = json["CreditCardType"].stringValue
    }
}