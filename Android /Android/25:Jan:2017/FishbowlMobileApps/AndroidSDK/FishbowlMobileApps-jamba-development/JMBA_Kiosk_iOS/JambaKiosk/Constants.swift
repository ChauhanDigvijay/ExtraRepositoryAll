//
//  Constants.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/8/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import CoreGraphics

struct Constants {

    static let jambaPrivacyUrl = "https://www.jambajuice.com/company-info/privacy"
    static let jambaTermsUrl = "https://www.jambajuice.com/company-info/legal"
    static let spendGoPrivacyUrl = "https://jamba.spendgo.com/index.html#/privacy"
    static let spendGoTermsUrl = "https://jamba.spendgo.com/index.html#/terms"
    static let oloPrivacyUrl = "http://order.jambajuice.com/help/privacypolicy"
    static let oloTermsUrl = "http://order.jambajuice.com/help/useragreement"

    // Brand color constants
    static let jambaOrangeColor        = 0xF8981D   // Bright orange        Main accent color

    static let jambaGarnetColor        = 0xC60C46   // Redish / garnet      Main call to action
    static let jambaDarkGarnetColor    = 0xA3103C   // Darker garnet

    static let jambaGreenColor         = 0x61C401   // Bright green         Large copy on welcome screens
    static let jambaDarkGreenColor     = 0x018301   // Dark green           Smaller copy on welcome screens

    static let jambaLightBlueColor     = 0x99ccf5   // Light blue
    static let jambaBlueberryColor     = 0x476FB3   // Blueberry blue

    static let jambaGrayColor          = 0xbfbfbf   // Gray                 Inactive state
    static let jambaDarkGrayColor      = 0x5e5e5e   // Dark gray            Titles, Active state
    static let jambaLightGrayColor     = 0xfafafa   // Light gray           Background


    // Fonts
    static let archerMedium = "Archer-Medium"
    static let archerBold = "Archer-Bold"
    static let nexaRustSansBlack = "NexaRustSans-Black"
    static let ocrAStandard = "OCRAStd"

    static let maxCreditCardExpiryYears = 100            // At max the credit card should expire this many years from current year
    static let parseRemoteSyncPeriod: NSTimeInterval = 24 * 60 * 60         // 24 hr

    // UI constants
    static let productThumbAspectRatio: CGFloat = 1.2621951219512195        // Aspect ratio for product thumbnails (621/492)


    // Image Names
    static let locationIconActive = "location-icon"
    static let locationIconInactive = "location-icon-gray"
    static let userProfileAvatars = [
        "apple":      0xc0d630,
        "blueberry":  0xcb2847,
        "orange":     0xcde3f4,
        "strawberry": 0xf29e44
    ]

    // Limits
    static let oloItemLimit: Int64 = 10

    static let cvvInputLimit = 4
    static let creditCardNumberInputLimit = 25 // Allow users to enter spaces and dashes
    static let expirationDateInputLimit = 7 //05/2015
    static let zipCodeInputLimit = 5
    static let phoneNumberInputLimit = 20 // Allow users to enter phone with spaces or other symbols, it is cleaned up on submit
    static let dobInputLimit = 12 // 05/01/2015 or Dec 25, 2015

    static let oloEmailAddressLimit = 64
    static let oloFirstAndLastNameLimit = 36

}
