//
//  AppConstants.swift
//  JambaJuice
//
//  Created by Puneet  on 3/31/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//


import UIKit

let intPointingServer: Int = 6

class AppConstants {
    
    static let CLPheaderKey = "91225258ddb5c8503dce33719c5deda7"
    static let jambaClientKey = "201969E1BFD242E189FE7B6297B1B5A6"
    
    
struct URL {
    static let QA = 1
    static let DEMO = 2
    static let Sales = 3
    static let Staging = 4
    static let ProductionTest = 5
    static let ProductionJamba = 6
    
    static func AppPointingURL(PointingServer: Int) -> String {
        switch PointingServer {
        case URL.QA:
//            return "http://jamba.clpqa.com/"
            return "https://qa-jamba.fishbowlcloud.com/"
        case URL.DEMO:
            return "http://demo.clpdemo.com/"
        case URL.Sales:
            return "http://salesfb.clpdemo.com/"
        case URL.Staging:
//            return "http://jamba.clpstaging.com/"
            return "https://stg-jamba.fishbowlcloud.com/"
        case URL.ProductionTest:
            return "https://test.clyptechs.com/"
        case URL.ProductionJamba:
            return "https://Jamba.clyptechs.com/"
            
        default:
            return "http://jamba.clpqa.com/"
        }
    }
}

}
