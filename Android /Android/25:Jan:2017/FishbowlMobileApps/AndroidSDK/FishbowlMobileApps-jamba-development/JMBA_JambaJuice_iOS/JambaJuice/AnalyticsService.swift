//
//  AnalyticsService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 7/3/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import AppsFlyer

class AnalyticsService {
    
    /// Track screen view
    class func trackScreenView(name: String) {
        GAI.sharedInstance().defaultTracker.set(kGAIScreenName, value: name)
        let builder = GAIDictionaryBuilder.createScreenView()
        GAI.sharedInstance().defaultTracker.send(builder.build() as [NSObject : AnyObject])
    }
    
    /// Track a single event
    class func trackEvent(category: String, action: String, label: String? = nil, value: Int? = nil, screenName: String? = nil) {
        // Google Analytics
        if screenName != nil {
            GAI.sharedInstance().defaultTracker.set(kGAIScreenName, value: screenName!)
        }
        let builder = GAIDictionaryBuilder.createEventWithCategory(category, action: action, label: label, value: value)
        GAI.sharedInstance().defaultTracker.send(builder.build() as [NSObject : AnyObject])
        
        // AppsFlyer
        AppsFlyerTracker.sharedTracker().trackEvent(action, withValues: ["category": category, "label": label ?? "", "value": value ?? 0, "screenName": screenName ?? ""])
    }
    
    /// Track purchase
    class func trackPurchase(orderStatus: OrderStatus, products: [BasketProduct]) {
        // Send transaction
        let payload = GAIDictionaryBuilder.createTransactionWithId(orderStatus.orderRef, affiliation: orderStatus.vendorExtRef, revenue: orderStatus.total, tax: orderStatus.salesTax, shipping: 0, currencyCode: "USD").build() as [NSObject:AnyObject]
        GAI.sharedInstance().defaultTracker.send(payload)
        
        // Send basket items
        for product in products {
            // Google Analytics
            let payload = GAIDictionaryBuilder.createItemWithTransactionId(orderStatus.orderRef, name: product.name, sku: String(product.productId), category: "Product", price: product.totalCost, quantity: NSNumber(long: product.quantity), currencyCode: "USD").build() as [NSObject:AnyObject]
            GAI.sharedInstance().defaultTracker.send(payload)
            
            // AppsFlyer
            AppsFlyerTracker.sharedTracker().trackEvent(AFEventPurchase, withValues: [AFEventParamRevenue: product.totalCost, AFEventParamContentType: "Product", AFEventParamDescription: product.name, AFEventParamContentId: String(product.productId), AFEventParamQuantity: String(product.quantity), AFEventParamCurrency: "USD"])
        }
    }
    
    /// Track application openURL
    class func trackOpenURL(url: NSURL, sourceApplication: String?, annotation: AnyObject?) {
        AppsFlyerTracker.sharedTracker().handleOpenURL(url, sourceApplication: sourceApplication ?? "", withAnnotation: annotation)
        trackEvent("open_url", action: url.absoluteString ?? "", label: sourceApplication)
    }
    
    /// Track user login
    class func trackUserLogin() {
        trackEvent("user_account", action: "login")
        AppsFlyerTracker.sharedTracker().trackEvent(AFEventLogin, withValues: [:])
        // clpAnalyticsService.sharedInstance.clpTrackScreenView("SignedIn")
    }
    
    /// Track user registration
    class func trackUserRegistration() {
        trackEvent("user_account", action: "signup")
        AppsFlyerTracker.sharedTracker().trackEvent(AFEventCompleteRegistration, withValues: [:])
        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
        {
            clpAnalyticsService.sharedInstance.clpTrackScreenView("SIGN_UP_COMPLETE");
        }
        
    }
    
    /// Set/clear the user ID
    class func setUserId(userId: String?) {
        // Google Analytics
        GAI.sharedInstance().defaultTracker.set("&uid", value: userId)
        GAI.sharedInstance().defaultTracker.set("user_authenticated", value: userId == nil ? "no" : "yes")
        GAI.sharedInstance().defaultTracker.set(GAIFields.customDimensionForIndex(1), value: userId == nil ? "no" : "yes")
        
        // AppsFlyer
        AppsFlyerTracker.sharedTracker().customerUserID = userId
    }
    
    /// Send any pending
    class func sendPending() {
        GAI.sharedInstance().dispatch()
    }
    
}
