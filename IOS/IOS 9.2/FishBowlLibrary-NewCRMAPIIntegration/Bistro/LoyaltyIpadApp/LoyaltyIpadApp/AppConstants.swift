//
//  AppConstants.swift
//  LoyaltyIpadApp
//
//  Created by Puneet  on 11/21/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

let intPointingServer: Int = 7
struct URL {
    static let QA3 = 1
    static let STG3 = 2
    static let DEMO3 = 3
    static let DEV3 = 4
    static let ProdHBH = 5
    static let STGSeaI = 6
    static let ProdSeaI = 7
    
    
    
    static func AppPointingURL(PointingServer: Int) -> String {
        switch PointingServer {
            
        case URL.QA3:
            return "https://qa-jamba.fishbowlcloud.com/clpapi"
        case URL.STG3:
            return "https://stg-hbh.fishbowlcloud.com/clpapi"
        case URL.DEMO3:
            return "https://demo-jamba.fishbowlcloud.com/"
        case URL.DEV3:
            return "https://dev-jamba.fishbowlcloud.com/clpapi"
        case URL.ProdHBH:
            return "https://hbh.fishbowlcloud.com/clpapi"
        case URL.STGSeaI:
            return "https://stg-seaisland.fishbowlcloud.com/clpapi"
        case URL.ProdSeaI:
            return "https://seaisland.fishbowlcloud.com/clpapi/"
        default:
            return "https://qa-jamba.fishbowlcloud.com/"
        }
    }
    static func AppPointingClientID(PointingServer: Int) -> String {
        
        switch PointingServer {
        case URL.QA3:
            return "201969E1BFD242E189FE7B6297B1B5A6"
        case URL.STG3:
            return "201969E1BFD242E189FE7B6297B1B5A3"
        case URL.DEMO3:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.DEV3:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.ProdHBH:
            return "201969E1BFD242E189FE7B6297B1B540"
        case URL.STGSeaI:
            return "201969E1BFD242E189FE7B6297B1B5B2"
        case URL.ProdSeaI:
            return "201969E1BFD242E189FE7B6297B1B590"
        default:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        }
        
    }
    
    static func AppPointingClientSecret(PointingServer: Int) -> String {
        
        switch PointingServer {
        case URL.QA3:
            return "C65A0DC0F28C469FB7376F972DEFBCB7"
        case URL.STG3:
            return "C65A0DC0F28C469FB7376F972DEFBCB3"
        case URL.DEMO3:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.DEV3:
            return "C65A0DC0F28C469FB7376F972DEFBCB8"
        case URL.ProdHBH:
            return "C65A0DC0F28C469FB7376F972DEFBC40"
        case URL.STGSeaI:
            return "C65A0DC0F28C469FB7376F972DEFBCA2"
        case URL.ProdSeaI:
            return "C65A0DC0F28C469FB7376F972DEFBC90"
        default:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        }
        
    }
    
}


