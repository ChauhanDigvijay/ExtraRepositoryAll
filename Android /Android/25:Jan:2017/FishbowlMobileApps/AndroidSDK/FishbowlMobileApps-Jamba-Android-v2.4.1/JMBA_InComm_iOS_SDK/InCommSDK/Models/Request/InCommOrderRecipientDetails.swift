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
    public var deliverOn: Date?
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
        jsonDict["FirstName"]            = firstName as AnyObject?
        jsonDict["LastName"]             = lastName as AnyObject?
        jsonDict["Items"]                = items.map { $0.serializeAsJSONDictionary() }
        //
        jsonDict["City"]                 = city as AnyObject?
        jsonDict["Company"]              = company as AnyObject?
        jsonDict["Country"]              = country as AnyObject?
        jsonDict["DeliverOn"]            = deliverOn?.InCommDateTimeFormatString() as AnyObject?
        jsonDict["DeliveryLanguage"]     = deliveryLanguage as AnyObject?
        // Default Value as false
        jsonDict["DisableEmailDelivery"] = false
        //
        jsonDict["EmailAddress"]         = emailAddress as AnyObject?
        jsonDict["MobilePhoneNumber"]    = mobilePhoneNumber as AnyObject?
        jsonDict["PhoneNumber"]          = phoneNumber as AnyObject?
        jsonDict["StateProvince"]        = stateProvince as AnyObject?
        jsonDict["StreetAddress1"]       = streetAddress1 as AnyObject?
        jsonDict["StreetAddress2"]       = streetAddress2 as AnyObject?
        jsonDict["ZipPostalCode"]        = zipPostalCode as AnyObject?
        jsonDict["ShippingMethod"]       = shippingMethod != nil ? shippingMethod!.rawValue : nil
        return jsonDict
    }
}
