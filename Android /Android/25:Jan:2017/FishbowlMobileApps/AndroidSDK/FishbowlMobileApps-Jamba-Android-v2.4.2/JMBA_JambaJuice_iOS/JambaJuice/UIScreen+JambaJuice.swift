//
//  UIScreen+JambaJuice.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 7/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//
//  Screen sizes from http://www.paintcodeapp.com/news/ultimate-guide-to-iphone-resolutions

import Foundation

extension UIScreen {

    func is35inch() -> Bool {
        return bounds == CGRect(x: 0, y: 0, width: 320, height: 480) ||
               bounds == CGRect(x: 0, y: 0, width: 640, height: 960)
    }
    
    func is4inch() -> Bool {
        return bounds == CGRect(x: 0, y: 0, width: 320,  height: 568) ||
               bounds == CGRect(x: 0, y: 0, width: 640, height: 1136)
    }
    
    func is47inch() -> Bool {
        return bounds == CGRect(x: 0, y: 0, width: 375,  height: 667) ||
               bounds == CGRect(x: 0, y: 0, width: 750, height: 1334)
    }
    
    func is55inch() -> Bool {
        return bounds == CGRect(x: 0, y: 0,  width: 414,  height: 736) ||
               bounds == CGRect(x: 0, y: 0, width: 1080, height: 1920) ||
               bounds == CGRect(x: 0, y: 0, width: 1125, height: 2001) ||
               bounds == CGRect(x: 0, y: 0, width: 1242, height: 2208)
    }
    
}
