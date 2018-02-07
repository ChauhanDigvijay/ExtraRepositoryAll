//
//  FishbowlMobileAppEvent.swift
//  JambaJuice
//
//  Created by VT010 on 2/8/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation

public struct FishbowlEvents{
    public var mobileAppEvents:[FishbowlEvent]
    
    public init(mobileAppEvents:[FishbowlEvent]){
        self.mobileAppEvents = mobileAppEvents
    }
    
    public func serializeJSONDictionary() -> FishbowlJSONDictionary{
        var jsonDict = FishbowlJSONDictionary()
        jsonDict["mobileAppEvent"] = mobileAppEvents.map { event in event.serializeAsJSONDictionary() }
        return jsonDict
    }
}
