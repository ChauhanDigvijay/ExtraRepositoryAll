//
//  InCommUserGiftCard.swift
//  InCommSDK
//
//  Created by vThink on 6/10/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//


import Foundation
import SwiftyJSON

public struct InCommUserGiftCard {

    public var userId:Int32?
    public var cardNumber: String
    public var cardPin: String?//TODO: cardPin/pin is null
    public var externalCertificateData: String?
    public var brandId: String
    public var cardId: Int32
    public var brandName: String
    public var cardName: String
    public var imageUrl: String
    public var thumbnailImageUrl: String
    public var barcodeValue: String
    public var barcodeImageUrl: String
    public var expirationDate: NSDate?
    public var initialBalance: Double
    public var balance: Double
    public var balanceDate: NSDate?//TODO: balanceDate is nil
    public var messageFrom: String
    public var messageText: String
    public var messageTo: String
    public var termsAndConditions: String
    public var usageInstructions: String
    public var autoReloadId: Int32?
    public var isTestMode: Bool
    public var lastModifiedDate: NSDate?
    public var refreshTime: NSDate?
    
    public init(json: JSON) {
        userId                   = json["UserId"].int32
        cardNumber               = json["CardNumber"].stringValue
        cardPin                  = json["CardPin"].string
        externalCertificateData  = json["ExternalCertificateData"].string
        brandId                  = json["BrandId"].stringValue
        cardId                   = json["CardId"].int32Value
        brandName                = json["BrandName"].stringValue
        cardName                 = json["CardName"].stringValue
        imageUrl                 = json["ImageUrl"].stringValue
        thumbnailImageUrl        = json["ThumbnailImageUrl"].stringValue
        barcodeValue             = json["BarcodeValue"].stringValue
        barcodeImageUrl          = json["BarcodeImageUrl"].stringValue
        expirationDate           = json["ExpirationDate"].string?.dateFromInCommFormatString()
        initialBalance           = json["InitialBalance"].doubleValue
        balance                  = json["Balance"].doubleValue
        balanceDate              = json["BalanceDate"].string?.dateFromInCommFormatString()
        messageFrom              = json["MessageFrom"].stringValue
        messageText              = json["MessageText"].stringValue
        messageTo                = json["MessageTo"].stringValue
        termsAndConditions       = json["TermsAndConditions"].stringValue
        usageInstructions        = json["UsageInstructions"].stringValue
        autoReloadId             = json["AutoReloadId"].int32
        isTestMode               = json["IsTestMode"].boolValue
        lastModifiedDate         = json["LastModifiedDate"].stringValue.dateFromInCommFormatString()
        refreshTime              = NSDate()
    }
}