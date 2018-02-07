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
        return bounds == CGRectMake(0, 0, 320, 480) ||
               bounds == CGRectMake(0, 0, 640, 960)
    }
    
    func is4inch() -> Bool {
        return bounds == CGRectMake(0, 0, 320,  568) ||
               bounds == CGRectMake(0, 0, 640, 1136)
    }
    
    func is47inch() -> Bool {
        return bounds == CGRectMake(0, 0, 375,  667) ||
               bounds == CGRectMake(0, 0, 750, 1334)
    }
    
    func is55inch() -> Bool {
        return bounds == CGRectMake(0, 0,  414,  736) ||
               bounds == CGRectMake(0, 0, 1080, 1920) ||
               bounds == CGRectMake(0, 0, 1125, 2001) ||
               bounds == CGRectMake(0, 0, 1242, 2208)
    }
    
}
