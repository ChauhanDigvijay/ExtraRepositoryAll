//
//  User.swift
//  JambaJuice
//
//  Created by Taha Samad on 07/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK
import SpendGoSDK
import Parse
import HDK

class User: Equatable {
    
    static let parseClassName = "User"
    
    var id: String
    var emailAddress: String?
    var phoneNumber: String
    var smsOptIn: Bool
    var emailOptIn: Bool
    var pushOptIn: Bool
    var firstName: String?
    var lastName: String?
    var dateOfBirth: NSDate?
    var profileImageName: String
    
    // Favorite store (should always be set, but do not trust SpendGo API)
    var favoriteStore: Store?
    
    // Current Store Maintain in session
    var currentStore: Store?
    
    // Store both SpendGo and Olo Auth Tokens (Sprint 7)
    var spendgoAuthToken: String?
    var oloAuthToken: String?
    
    // Recent Orders
    var recentOrders: [OrderStatus]? {
        didSet {
            NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.RecentOrdersUpdated.rawValue, object: nil)
        }
    }
    
    init(spendGoUser: SpendGoUser, spendGoAuthToken: String) {
        id               = spendGoUser.id!
        spendgoAuthToken = spendGoAuthToken
        phoneNumber      = spendGoUser.phoneNumber
        smsOptIn         = spendGoUser.smsOptIn
        pushOptIn        = true
        emailAddress     = spendGoUser.emailAddress
        emailOptIn       = spendGoUser.emailOptIn ?? false
        firstName        = spendGoUser.firstName
        lastName         = spendGoUser.lastName
        dateOfBirth      = spendGoUser.dateOfBirth?.dateFromISOString()
        profileImageName = Array(Constants.userProfileAvatars.keys).random()!
        
        // SpendGo user from API should always have store, but double check (see JMBAIO-809)
        if let spendGoStore = spendGoUser.favoriteStore {
            favoriteStore = Store(spendGoStore: spendGoStore)
        }
    }
    
    init(parseObject: PFObject) {
        // Auth Token Data
        spendgoAuthToken = parseObject["spendGoAuthToken"] as? String
        oloAuthToken     = parseObject["oloAuthToken"] as? String
        
        // User Data
        id               = parseObject["id"] as! String
        emailAddress     = parseObject["emailAddress"] as? String
        phoneNumber      = parseObject["phone"] as! String
        smsOptIn         = parseObject["smsOptIn"] as! Bool
        pushOptIn        = true
        emailOptIn       = parseObject["emailOptIn"] as! Bool
        firstName        = parseObject["firstName"] as? String
        lastName         = parseObject["lastName"] as? String
        dateOfBirth      = parseObject["dateOfBirth"] as? NSDate
        profileImageName = parseObject["profileImageName"] as? String ?? "apple" // Default
        
        // Favorite Store Data
        if let store = parseObject["favoriteStore"] as? PFObject {
            favoriteStore = Store(parseObject: store)
        }
        
        // Current Store Data
        if let store = parseObject["currentStore"] as? PFObject {
            currentStore = Store(parseObject: store)
        }
    }
    func serializeAsParseObject() -> PFObject {
        let parseObject = PFObject(className: User.parseClassName)
        
        // User Data
        parseObject["id"]               = id
        parseObject["emailAddress"]     = emailAddress ?? NSNull()
        parseObject["phone"]            = phoneNumber
        parseObject["smsOptIn"]         = smsOptIn
        parseObject["emailOptIn"]       = emailOptIn
        parseObject["firstName"]        = firstName ?? NSNull()
        parseObject["lastName"]         = lastName ?? NSNull()
        parseObject["dateOfBirth"]      = dateOfBirth ?? NSNull()
        parseObject["profileImageName"] = profileImageName
        parseObject["favoriteStore"]    = favoriteStore?.serializeAsParseObject() ?? NSNull()
        
        // Current Store Data
        parseObject["currentStore"] = currentStore?.serializeAsParseObject() ?? NSNull()
        
        // Save token in session
        parseObject["spendGoAuthToken"] = spendgoAuthToken ?? NSNull()
        parseObject["oloAuthToken"]     = oloAuthToken ?? NSNull()
        
        
        
        return parseObject
    }
    
    init (user: User) {
        id               = user.id
        phoneNumber      = user.phoneNumber
        smsOptIn         = user.smsOptIn
        pushOptIn        = user.pushOptIn
        emailAddress     = user.emailAddress
        emailOptIn       = user.emailOptIn
        firstName        = user.firstName
        lastName         = user.lastName
        dateOfBirth      = user.dateOfBirth
        profileImageName = user.profileImageName
        favoriteStore    = user.favoriteStore
        spendgoAuthToken = user.spendgoAuthToken
        oloAuthToken     = user.oloAuthToken
        recentOrders     = user.recentOrders
        
        // Add current Store
        currentStore  = user.currentStore
    }
    
    func updateUserFromSpendGoUser(spendGoUser: SpendGoUser) {
        id               = spendGoUser.id!
        phoneNumber      = spendGoUser.phoneNumber
        smsOptIn         = spendGoUser.smsOptIn
        emailAddress     = spendGoUser.emailAddress
        emailOptIn       = spendGoUser.emailOptIn ?? false
        firstName        = spendGoUser.firstName
        lastName         = spendGoUser.lastName
        dateOfBirth      = spendGoUser.dateOfBirth?.dateFromISOString()
        
        //save existing store
        let existingStore = favoriteStore
        //Try to assign the new store
        if spendGoUser.favoriteStore != nil {
            favoriteStore = Store(spendGoStore: spendGoUser.favoriteStore!)
            //Our new store is same as exisiting store
            if existingStore != nil && existingStore!.id == favoriteStore!.id {
                favoriteStore!.updateOloRestaurantProperties(existingStore!)
            }
        }
        else {
            favoriteStore = nil
        }
    }
    
}

// MARK: Equatable

func == (lhs: User, rhs: User) -> Bool {
    return lhs.phoneNumber == rhs.phoneNumber
}
