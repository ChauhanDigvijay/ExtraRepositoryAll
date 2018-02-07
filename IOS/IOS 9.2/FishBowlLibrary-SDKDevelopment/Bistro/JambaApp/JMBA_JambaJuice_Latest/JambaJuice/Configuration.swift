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

    private(set) var OloAPIKey: String
    private(set) var OloBaseURL: String
    private(set) var SpendGoAPIKey: String
    private(set) var SpendGoBaseURL: String
    private(set) var SpendGoSigningKey: String
    private(set) var ParseApplicationID: String
    private(set) var ParseClientKey: String
    private(set) var GoogleAnalyticsAccountID: String
    private(set) var InstabugAPIToken: String?

    private(set) var AppsFlyerDevKey: String
    
  /*  // Production/Live credentials
    private init() {
        OloAPIKey                   = "2Qs7wYoevR3elHBXLw3CTMmGExJfhesu"
        OloBaseURL                  = "https://api.olo.com/v1.1"
        SpendGoAPIKey               = "jambamobile"
        SpendGoBaseURL              = "https://my.spendgo.com/mobile/gen/jamba/v1"
        SpendGoSigningKey           = "onc6fyGnYszZklhCGyu9jUWAQn+53sck1qTAbQna9uk="
        ParseApplicationID          = "ZDoQmWb85Uif38XWJAnVlAmVYiDRnQfGfwbOlNkd"
        ParseClientKey              = "FK4KAbsmaELUR7q3JjkFVeeRqk99tuCmpicY1Qw7"
        GoogleAnalyticsAccountID    = "UA-61764891-4"
        InstabugAPIToken            = nil
        AppsFlyerDevKey             = "UZ4zswHekZwWK46Mr2G9jV"
    } */
    
    // Sandbox credentials
    private init() {
        OloAPIKey                   = "GXRRA3G0NEJtE4HLYrIQHmCxG2idGEcUTkK9V7vwanK1C_YgAPt9BFfQurlFhGjz"
        OloBaseURL                  = "https://api.olosandbox.com/v1.1"
        SpendGoAPIKey               = "jambamobile"
        SpendGoBaseURL              = "https://my.skuped.com/mobile/gen/jamba/v1"
        SpendGoSigningKey           = "ZLT4end4MY6TYj28GXVzAyE62SW0uKuXLHQLqu8gja0="
        ParseApplicationID          = "ZDoQmWb85Uif38XWJAnVlAmVYiDRnQfGfwbOlNkd"
        ParseClientKey              = "FK4KAbsmaELUR7q3JjkFVeeRqk99tuCmpicY1Qw7"
        GoogleAnalyticsAccountID    = "UA-61764891-4"
        InstabugAPIToken            = nil
        AppsFlyerDevKey             = "UZ4zswHekZwWK46Mr2G9jV"
    }

}
