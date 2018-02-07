//
//  AppConstants.swift
//  LoyaltyIpadApp
//
//  Created by Puneet  on 11/21/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit


var strURL : String = ""
var strClientId : String = ""
var strClientSecret : String = ""
var strTanentId : String = ""



public class ApiHeaders
{
    
   public static let sharedInstance = ApiHeaders()
   private init() {}
    
}

    public func AppPointingURL( PointingServer: String) -> String {
        
         strURL = PointingServer
        
        return strURL
       
    }
    
    public func AppPointingClientID(clientId: String) -> String {
        
        strClientId = clientId
            return strClientId

    }
    
    public func AppPointingClientSecret(clientSecret: String) -> String {
        
        strClientSecret = clientSecret
        return strClientSecret
    }
    
    public func AppPointingletTanentID(tanentId : String) -> String {
        
        return tanentId
        
    }



