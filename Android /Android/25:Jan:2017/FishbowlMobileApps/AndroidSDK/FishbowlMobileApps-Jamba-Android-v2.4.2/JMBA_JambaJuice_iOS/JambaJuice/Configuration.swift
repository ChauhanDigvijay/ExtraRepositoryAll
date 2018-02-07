//
//  Configuration.swift
//  JambaJuice
//
//  Created by Taha Samad on 14/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation

struct Configuration {

    static let sharedConfiguration = Configuration()

    fileprivate(set) var OloAPIKey: String
    fileprivate(set) var OloBaseURL: String
    fileprivate(set) var SpendGoAPIKey: String
    fileprivate(set) var SpendGoBaseURL: String
    fileprivate(set) var SpendGoSigningKey: String
    fileprivate(set) var ParseBaseURL: String
    fileprivate(set) var ParseApplicationID: String
    fileprivate(set) var ParseClientKey: String
    fileprivate(set) var GoogleAnalyticsAccountID: String
    fileprivate(set) var InstabugAPIToken: String?

    fileprivate(set) var AppsFlyerDevKey: String

    fileprivate(set) var InCommBaseURL:String
    fileprivate(set) var InCommClientId:String
    fileprivate(set) var InCommBrandId:String
    fileprivate(set) var InCommPrintLog:Bool = false
    fileprivate(set) var InCommTestMode:Bool = false
    fileprivate(set) var ProductionBuild:Bool

    fileprivate(set) var ShowDemoVendor: Bool = false
    fileprivate(set) var FBEvents:Bool        = false

     //Production/Live credentials
    private init() {
        OloAPIKey                   = "2Qs7wYoevR3elHBXLw3CTMmGExJfhesu"
        OloBaseURL                  = "https://ordering.api.olo.com/v1.1"
        SpendGoAPIKey               = "jambamobile"
        SpendGoBaseURL              = "https://my.spendgo.com/mobile/gen/jamba/v1"
        SpendGoSigningKey           = "onc6fyGnYszZklhCGyu9jUWAQn+53sck1qTAbQna9uk="
        ParseBaseURL                = "https://parse.fishbowlcloud.com:1338/production/jamba/"
        ParseApplicationID          = "ZDoQmWb85Uif38XWJAnVlAmVYiDRnQfGfwbOlNkd"
        ParseClientKey              = "FK4KAbsmaELUR7q3JjkFVeeRqk99tuCmpicY1Qw7"
        GoogleAnalyticsAccountID    = "UA-61764891-4"
        InstabugAPIToken            = nil
        AppsFlyerDevKey             = "UZ4zswHekZwWK46Mr2G9jV"

        // InComm Configuration
        InCommBaseURL               = "https://api.giftango.com/enterpriseapi/v1"
        InCommClientId              = "jambajuicemobileapplication"
        InCommBrandId               = "jambajuice"
        InCommPrintLog              = false
        InCommTestMode              = false

        ShowDemoVendor              = false
        ProductionBuild             = true
        FBEvents                    = true
        FishbowlApiClassService.sharedInstance.fbInitialization(pointingServer: PointingServer.Production.rawValue)
    }


    // Sandbox credentials
//    fileprivate init() {
//        OloAPIKey                   = "GXRRA3G0NEJtE4HLYrIQHmCxG2idGEcUTkK9V7vwanK1C_YgAPt9BFfQurlFhGjz"
//        OloBaseURL                  = "https://ordering.api.olosandbox.com/v1.1"
//        SpendGoAPIKey               = "jambamobile"
//        SpendGoBaseURL              = "https://my.skuped.com/mobile/gen/jamba/v1"
//        SpendGoSigningKey           = "ZLT4end4MY6TYj28GXVzAyE62SW0uKuXLHQLqu8gja0="
//        ParseBaseURL                = "http://jamba.clpqa.com:1338/sandbox/jamba/"
//        ParseApplicationID          = "gEHxHYrbkPJKNVi4P1UakDMKtqdIrjzZt4Xlmp4m"
//        ParseClientKey              = "IHeUZNjkusqGoItXI2VR8enRO4onSfk5BFVPIXue"
//        GoogleAnalyticsAccountID    = "UA-61764891-4"
//        InstabugAPIToken            = nil
//        AppsFlyerDevKey             = "UZ4zswHekZwWK46Mr2G9jV"
//
//        //         InComm Configuration
//
//        InCommBaseURL               = "https://api.giftango.com/enterpriseapi/v1"
//        InCommClientId              = "jambajuicemobileapplicationtest"
//        InCommBrandId               = "711763"
//        InCommPrintLog              = true
//        InCommTestMode              = true
//
//        ShowDemoVendor              = true
//        ProductionBuild             = false
//        FBEvents                    = true
//
//        FishbowlApiClassService.sharedInstance.fbInitialization(pointingServer: PointingServer.QA.rawValue)
//   }
 }
