//
//  AppHelpers.swift
//  JambaJuice
//
//  Created by Taha Samad on 05/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK

class CallManager {
    
    /// Makes a phone call to a given number. Delegates on application to identify if call can be made
    class func call(phoneNumber: String, presentingViewController viewController: UIViewController) {
        if let telURL = NSURL(string: "tel://\(phoneNumber.stringByRemovingNonNumericCharacters())") {
            // Check if device can call to this number
            log.verbose("\(telURL)")
            if !UIApplication.sharedApplication().canOpenURL(telURL) {
                viewController.presentOkAlert("Phone Call Unavailable", message: "This device cannot make phone calls or phone number is not supported.")
                return
            }
            
            // Prompt the user for confirmation to make the phone call
            viewController.presentConfirmation("Call Store", message: "Please confirm you would like to call this number: \(phoneNumber.prettyFormattedPhoneNumber())", buttonTitle: "Call") { confirmed in
                if confirmed {
                    UIApplication.sharedApplication().openURL(telURL)
                }
            }
        } else {
            viewController.presentOkAlert("Unsupported Number", message: "This device cannot make phone calls to this number: \(phoneNumber)")
        }
    }
    
}
