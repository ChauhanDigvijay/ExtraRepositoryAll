//
//  InCommSubmittedOrderItemGiftCard.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/18/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommSubmittedOrderItemGiftCard {

    public var amount: Double
    public var barcodeImageUrl: String
    public var barcodeValue: String
    public var brandName: String
    public var giftCardId: Int32
    public var giftCardImageUrl: String
    public var giftCardNumber: String
    public var giftCardStatus: InCommActivationStatus
    public var giftCardUrl: String
    public var token: String
    //
    public var brandId: String?//TODO: BrandId is null
    public var externalCertificateData: String?
    public var pin: String?//TODO: pin/cardPin is null

    public init(json: JSON) {
        amount                  = json["Amount"].doubleValue
        barcodeImageUrl         = json["BarcodeImageUrl"].stringValue
        barcodeValue            = json["BarcodeValue"].stringValue
        brandName               = json["BrandName"].stringValue
        giftCardId              = json["GiftCardId"].int32Value
        giftCardImageUrl        = json["GiftCardImageUrl"].stringValue
        giftCardNumber          = json["GiftCardNumber"].stringValue
        giftCardStatus          = InCommActivationStatus(rawValue: json["GiftCardStatus"].stringValue)!
        giftCardUrl             = json["GiftCardUrl"].stringValue
        token                   = json["Token"].stringValue
        //
        brandId                 = json["BrandId"].string
        externalCertificateData = json["ExternalCertificateData"].string
        pin                     = json["Pin"].string
    }

}