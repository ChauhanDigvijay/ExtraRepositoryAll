//
//  InCommOrderRecipientDetails.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/17/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import SwiftyJSON

public struct InCommOrderRecipientDetails {
    public var firstName: String
    public var lastName: String
    public var items: [InCommOrderItem]
    //
    public var city: String?
    public var company: String?
    public var country: String?
    public var deliverOn: NSDate?
    public var deliveryLanguage: String?
    public var disableEmailDelivery: Bool?
    public var emailAddress: String?
    public var mobilePhoneNumber: String?
    public var phoneNumber: String?
    public var stateProvince: String?
    public var streetAddress1: String?
    public var streetAddress2: String?
    public var zipPostalCode: String?
    public var shippingMethod: InCommShippingMethod?
    
    public init(firstName: String, lastName: String, emailAddress: String, items: [InCommOrderItem]) {
        self.firstName = firstName
        self.lastName  = lastName
        self.emailAddress = emailAddress
        self.items = items
    }
    
    public func serializeAsJSONDictionary() -> InCommJSONDictionary {
        var jsonDict                     = InCommJSONDictionary()
        jsonDict["FirstName"]            = firstName
        jsonDict["LastName"]             = lastName
        jsonDict["Items"]                = items.map { $0.serializeAsJSONDictionary() }
        //
        jsonDict["City"]                 = city
        jsonDict["Company"]              = company
        jsonDict["Country"]              = country
        jsonDict["DeliverOn"]            = deliverOn?.InCommDateTimeFormatString()
        jsonDict["DeliveryLanguage"]     = deliveryLanguage
        jsonDict["DisableEmailDelivery"] = disableEmailDelivery
        jsonDict["EmailAddress"]         = emailAddress
        jsonDict["MobilePhoneNumber"]    = mobilePhoneNumber
        jsonDict["PhoneNumber"]          = phoneNumber
        jsonDict["StateProvince"]        = stateProvince
        jsonDict["StreetAddress1"]       = streetAddress1
        jsonDict["StreetAddress2"]       = streetAddress2
        jsonDict["ZipPostalCode"]        = zipPostalCode
        jsonDict["ShippingMethod"]       = shippingMethod != nil ? shippingMethod!.rawValue : nil
        return jsonDict
    }
}
