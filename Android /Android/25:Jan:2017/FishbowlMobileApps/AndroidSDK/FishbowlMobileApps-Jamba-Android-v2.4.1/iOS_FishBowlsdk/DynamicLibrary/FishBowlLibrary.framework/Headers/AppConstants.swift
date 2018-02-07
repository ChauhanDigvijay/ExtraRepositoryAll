//
//  AppConstants.swift
//  LoyaltyIpadApp
//
//  Created by Puneet  on 17/07/17.
//  Copyright © 2017 Fishbowl. All rights reserved.
//

import UIKit


public var strBaseURL : String = ""
public var strClientId : String = ""
public var strClientSecret : String = ""
public var strTanentId : String = ""
public var strSpendgoBaseUrl : String = ""
public let applicationType = "mobilesdk"
public let contentType = "application/json"


public class AppConstant
{
    
   public static let sharedInstance = AppConstant()
   private init() {}
    

    public func AppPointingURL( PointingServer: String)  {
        
         strBaseURL = PointingServer
    }
    
    public func AppPointingClientID(clientId: String) {
        
        strClientId = clientId

    }
    
    public func AppPointingClientSecret(clientSecret: String) {
        
        strClientSecret = clientSecret
    }
    
    public func AppPointingTanentID(tanentId : String){
        
        strTanentId = tanentId
        
    }

    public func AppPointingSpendgoBaseUrl(spendgoBaseUrl : String) {
    
    strSpendgoBaseUrl = spendgoBaseUrl
}

   
}


