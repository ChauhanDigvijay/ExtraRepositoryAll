//
//  InCommOrder.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/7/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommOrder {

    //NOTE: THIS CLASS ONLY INCLUDES NEEDED PROPS. FOR FULL LIST SEE DOCUMENTATION
    public var id: String
    public var result: InCommOrderStatus
    //
    public var resultDescription: String?
    public var userId: String?
    public var cardBalance: Double?
    public var cardBalanceDate: NSDate?
    public var submittedOrderItemGiftCards: [InCommSubmittedOrderItemGiftCard]?

    public init(json: JSON) {
        id                          = json["Id"].stringValue
        result                      = InCommOrderStatus(rawValue: json["Result"].stringValue)!
        //
        resultDescription           = json["ResultDescription"].string
        userId                      = json["UserId"].string
        cardBalance                 = json["CardBalance"].double
        cardBalanceDate             = json["CardBalanceDate"].string?.dateFromInCommFormatString()
        submittedOrderItemGiftCards = json["SubmittedOrderItemGiftCards"].array?.map { InCommSubmittedOrderItemGiftCard(json: $0) }
    }
}
