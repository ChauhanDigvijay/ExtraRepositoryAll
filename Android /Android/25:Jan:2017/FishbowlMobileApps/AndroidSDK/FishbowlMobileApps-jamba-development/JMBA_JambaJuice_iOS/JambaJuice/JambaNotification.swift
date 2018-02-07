//
//  JambaNotifications.swift
//  JambaJuice
//
//  Created by Taha Samad on 18/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

enum JambaNotification: String {
    
    case LoggedInStateChanged = "JambaLoggedInStateChanged"
    
    case UserFirstNameChanged = "JambaUserFirstNameChanged"
    case UserEmailAddressChanged = "JambaUserEmailAddressChanged"
    case UserPhoneNumberChanged = "JambaUserPhoneNumberChanged"
    case UserProfileImageChanged = "JambaUserProfileImageChanged"
    
    case SharedBasketUpdated = "SharedBasketUpdated"
    case BasketNCDismissed = "BasketNCDismissed"
    
    case OrderPlaced = "OrderPlaced"
    case OrderStarted = "OrderStarted"
    
    case RecentOrdersUpdated = "RecentOrdersUpdated"
    case RecentlyOrderedProductsUpdated = "RecentlyOrderedProductsUpdated"
    
    // CurrentStoreChanged
    case CurrentStoreChanged = "CurrentStoreChanged"
    
    // RestartOrder and Basket Transferred for guest
    case ReStartOrderForGuest = "ReStartOrderForGuest"
    case BasketTransferredForGuest = "BasketTransferredForGuest"
    
    // RestartOrder and Basket Transferred for user
    case ReStartOrderForUser = "ReStartOrderForUser"
    case BasketTransferredForUser = "BasketTransferredForUser"
    
    case CancelStoreChange = "CancelStoreChange"
    
    case JambaGiftCardCountRefresh = "JambaGiftCardCountRefresh"
    
    // Preferred store change from profile screen notification
    case PreferredStoreChanged = "PreferredStoreChanged"
    
}
