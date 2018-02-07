//
//  InCommCard.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/7/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommCard {

    public var balance: Double
    public var barcodeImageUrl: String
    public var barcodeValue: String
    public var brandId: String
    public var brandName: String
    public var cardId: Int32
    public var cardNumber: String
    public var cardName: String
    public var imageUrl: String
    public var initialBalance: Double
    public var isTestMode: Bool

    public var messageFrom: String
    public var messageText: String
    public var messageTo: String
    public var termsAndConditions: String
    public var thumbnailImageUrl: String
    public var usageInstructions: String
    //
    public var autoReloadId: Int32?
    public var balanceDate: NSDate?//TODO: balanceDate is nil
    public var cardPin: String?//TODO: cardPin/pin is null
    public var expirationDate: NSDate?
    public var externalCertificateData: String?
    public var lastModifiedDate: NSDate?
    
    public init(json: JSON) {
        balance                  = json["Balance"].doubleValue
        barcodeImageUrl          = json["BarcodeImageUrl"].stringValue
        barcodeValue             = json["BarcodeValue"].stringValue
        brandId                  = json["BrandId"].stringValue
        brandName                = json["BrandName"].stringValue
        cardId                   = json["CardId"].int32Value
        cardNumber               = json["CardNumber"].stringValue
        cardName                 = json["CardName"].stringValue
        imageUrl                 = json["ImageUrl"].stringValue
        initialBalance           = json["InitialBalance"].doubleValue
        isTestMode               = json["IsTestMode"].boolValue
        messageFrom              = json["MessageFrom"].stringValue
        messageText              = json["MessageText"].stringValue
        messageTo                = json["MessageTo"].stringValue
        termsAndConditions        = json["TermsAndConditions"].stringValue
        thumbnailImageUrl        = json["ThumbnailImageUrl"].stringValue
        usageInstructions        = json["UsageInstructions"].stringValue
        //
        autoReloadId             = json["AutoReloadId"].int32
        balanceDate              = json["BalanceDate"].string?.dateFromInCommFormatString()
        cardPin                  = json["CardPin"].string
        expirationDate           = json["ExpirationDate"].string?.dateFromInCommFormatString()
        externalCertificateData  = json["ExternalCertificateData"].string
        lastModifiedDate         = json["LastModifiedDate"].stringValue.dateFromInCommFormatString()
    }
}
