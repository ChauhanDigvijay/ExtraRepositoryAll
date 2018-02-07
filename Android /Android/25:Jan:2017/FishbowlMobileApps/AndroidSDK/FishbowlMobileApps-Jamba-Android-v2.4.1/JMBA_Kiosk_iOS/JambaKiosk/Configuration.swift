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

    private(set) var oloAPIKey: String
    private(set) var oloBaseURL: String
    private(set) var spendGoAPIKey: String
    private(set) var spendGoBaseURL: String
    private(set) var spendGoSigningKey: String
    private(set) var parseApplicationID: String
    private(set) var parseClientKey: String
    private(set) var googleAnalyticsAccountID: String
    private(set) var instabugAPIToken: String?

    private init() {
        let path = NSBundle.mainBundle().pathForResource("Config", ofType: "plist")
        let infoDict = NSDictionary(contentsOfFile: path!)!

        oloAPIKey = infoDict["OloAPIKey"] as? String ?? ""
        oloBaseURL = infoDict["OloBaseURL"] as? String ?? ""

        spendGoBaseURL = infoDict["SpendGoBaseURL"] as? String ?? ""
        spendGoAPIKey = infoDict["SpendGoAPIKey"] as? String ?? ""
        spendGoSigningKey = infoDict["SpendGoSigningKey"] as? String ?? ""

        parseApplicationID = infoDict["ParseApplicationID"] as? String ?? ""
        parseClientKey = infoDict["ParseClientKey"] as? String ?? ""

        googleAnalyticsAccountID = infoDict["GoogleAnalyticsAccountID"] as? String ?? ""

        instabugAPIToken = infoDict["InstabugAPIToken"] as? String // Optional, will be empty on production
    }

}
