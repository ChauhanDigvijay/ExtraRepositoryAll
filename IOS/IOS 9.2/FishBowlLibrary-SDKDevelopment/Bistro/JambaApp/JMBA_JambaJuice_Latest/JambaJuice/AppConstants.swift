//
//  AppConstants.swift
//  JambaJuice
//
//  Created by Puneet  on 3/31/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//


import UIKit

let intPointingServer: Int = 7

class AppConstants {
    
    static let CLPheaderKey = "91225258ddb5c8503dce33719c5deda7"
    
    
struct URL {
    static let QA = 1
    static let DEMO = 2
    static let Sales = 3
    static let Staging = 4
    static let ProductionTest = 5
    static let ProductionJamba = 6
    static let QA3 = 7
    static let DEV3 = 8
    static let STG3 = 9


    
    static func AppPointingURL(PointingServer: Int) -> String {
        switch PointingServer {
        case URL.QA:
            return "http://jamba.clpqa.com/"
        case URL.DEMO:
            return "http://demo.clpdemo.com/"
        case URL.Sales:
            return "http://salesfb.clpdemo.com/"
        case URL.Staging:
            return "http://jamba.clpstaging.com/"
        case URL.ProductionTest:
            return "https://test.clyptechs.com/"
        case URL.ProductionJamba:
            return "https://Jamba.clyptechs.com/"
        case URL.QA3:
            return "https://qa-jamba.fishbowlcloud.com/"
        case URL.DEV3:
            return "https://dev-jamba.fishbowlcloud.com/"
        case URL.STG3:
            return "https://stg-jamba.fishbowlcloud.com/"
            
        default:
            return "http://jamba.clpqa.com/"
        }
    }
    
    
    
    static func AppPointingClientID(PointingServer: Int) -> String {
        
        switch PointingServer {
        case URL.QA:
            return "201969E1BFD242E189FE7B6297B1B5A6"
        case URL.DEMO:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.Sales:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.Staging:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.ProductionTest:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.ProductionJamba:
            return "201969E1BFD242E189FE7B6297B1B5A5"
//        case URL.JuneRelease:
//            return "201969E1BFD242E189FE7B6297B1B5A5"
//        case URL.JuneStagingRelease:
//            return "201969E1BFD242E189FE7B6297B1B5A5"
//        case URL.JuneDemoRelease:
//            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.QA3:
            return "201969E1BFD242E189FE7B6297B1B5A6"
        case URL.DEV3:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        case URL.STG3:
            return "201969E1BFD242E189FE7B6297B1B5A9"
        default:
            return "201969E1BFD242E189FE7B6297B1B5A5"
        }
        
    }
   
    }

}