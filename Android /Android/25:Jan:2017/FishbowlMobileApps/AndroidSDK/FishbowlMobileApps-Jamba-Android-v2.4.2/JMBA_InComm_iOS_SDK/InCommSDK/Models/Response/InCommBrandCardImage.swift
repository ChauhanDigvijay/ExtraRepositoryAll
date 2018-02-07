//
//  InCommBrandCardImage.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommBrandCardImage {
    
    public var imageCode: String
    public var customerCharge: Double
    public var imageFileName: String
    public var imageUrl: String
    public var isCustomUpload: Bool
    public var sortOrder: Int32
    public var thumbnailImageUrl: String
    public var imageType: InCommBrandCardImageType
    
    public init(json: JSON) {
        imageCode         = json["ImageCode"].stringValue
        customerCharge    = json["CustomerCharge"].doubleValue
        imageFileName     = json["ImageFileName"].stringValue
        imageUrl          = json["ImageUrl"].stringValue
        isCustomUpload    = json["IsCustomUpload"].boolValue
        sortOrder         = json["SortOrder"].int32Value
        thumbnailImageUrl = json["ThumbnailImageUrl"].stringValue
        imageType         = InCommBrandCardImageType(rawValue: json["ImageType"].stringValue)!
    }
}