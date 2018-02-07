//
//  AnalyticsService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 7/3/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import AppsFlyerLib

class AnalyticsService {
    
    /// Track screen view
    class func trackScreenView(_ name: String) {
        GAI.sharedInstance().defaultTracker.set(kGAIScreenName, value: name)
        let builder = GAIDictionaryBuilder.createScreenView()
        GAI.sharedInstance().defaultTracker.send(builder?.build() as NSDictionary? as? [AnyHashable: Any] ?? [:])
    }
    
    /// Track a single event
    class func trackEvent(_ category: String, action: String, label: String? = nil, value: Int? = nil, screenName: String? = nil) {
        // Google Analytics
        if screenName != nil {
            GAI.sharedInstance().defaultTracker.set(kGAIScreenName, value: screenName!)
        }
        let builder = GAIDictionaryBuilder.createEvent(withCategory: category, action: action, label: label, value: value as NSNumber!)
        GAI.sharedInstance().defaultTracker.send(builder?.build() as NSDictionary? as? [AnyHashable: Any] ?? [:])
        
        // AppsFlyer
        AppsFlyerTracker.shared().trackEvent(action, withValues: ["category": category, "label": label ?? "", "value": value ?? 0, "screenName": screenName ?? ""])
    }
    
    /// Track purchase
    class func trackPurchase(_ orderStatus: OrderStatus, products: [BasketProduct]) {
        // Send transaction
        let payload = GAIDictionaryBuilder.createTransaction(withId: orderStatus.orderRef, affiliation: orderStatus.vendorExtRef, revenue: orderStatus.total as NSNumber!, tax: orderStatus.salesTax as NSNumber!, shipping: 0, currencyCode: "USD").build() as NSDictionary? as? [AnyHashable: Any] ?? [:]
        GAI.sharedInstance().defaultTracker.send(payload)
        
        // Send basket items
        for product in products {
            // Google Analytics
            let payload = GAIDictionaryBuilder.createItem(withTransactionId: orderStatus.orderRef, name: product.name, sku: String(product.productId), category: "Product", price: product.totalCost as NSNumber!, quantity: NSNumber(value: product.quantity as Int), currencyCode: "USD").build() as NSDictionary? as? [AnyHashable: Any] ?? [:]
            GAI.sharedInstance().defaultTracker.send(payload)
            
            // AppsFlyer
            AppsFlyerTracker.shared().trackEvent(AFEventPurchase, withValues: [AFEventParamRevenue: product.totalCost, AFEventParamContentType: "Product", AFEventParamDescription: product.name, AFEventParamContentId: String(product.productId), AFEventParamQuantity: String(product.quantity), AFEventParamCurrency: "USD"])
        }
    }
    
    /// Track application openURL

    @available(iOS 9.0, *)
    class func trackOpenURL(_ url: URL,options: [UIApplicationOpenURLOptionsKey : Any] = [:] ) {
        AppsFlyerTracker.shared().handleOpen(url, options: options)
        trackEvent("open_url", action: url.absoluteString , label: options[UIApplicationOpenURLOptionsKey.sourceApplication] as? String)
    }

    // Below iOS 9.0
    class func trackOpenURL(_ url: URL, sourceApplication: String?, annotation: AnyObject?) {
        trackEvent("open_url", action: url.absoluteString , label: sourceApplication)
    }
    
    /// Track user login
    class func trackUserLogin() {
        trackEvent("user_account", action: "login")
        AppsFlyerTracker.shared().trackEvent(AFEventLogin, withValues: [:])
    }
    
    /// Track user registration
    class func trackUserRegistration() {
        trackEvent("user_account", action: "signup")
        AppsFlyerTracker.shared().trackEvent(AFEventCompleteRegistration, withValues: [:])        
    }
    
    /// Set/clear the user ID
    class func setUserId(_ userId: String?) {
        // Google Analytics
        GAI.sharedInstance().defaultTracker.set("&uid", value: userId)
        GAI.sharedInstance().defaultTracker.set("user_authenticated", value: userId == nil ? "no" : "yes")
        GAI.sharedInstance().defaultTracker.set(GAIFields.customDimension(for: 1), value: userId == nil ? "no" : "yes")
        
        // AppsFlyer
        AppsFlyerTracker.shared().customerUserID = userId
    }
    
    /// Send any pending
    class func sendPending() {
        GAI.sharedInstance().dispatch()
    }
    
}
