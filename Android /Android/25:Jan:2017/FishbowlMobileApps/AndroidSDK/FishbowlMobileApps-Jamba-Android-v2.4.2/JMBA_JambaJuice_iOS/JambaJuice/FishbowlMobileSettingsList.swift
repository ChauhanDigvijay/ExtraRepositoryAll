//
//  FishbowlMobileSettingsList.swift
//  JambaJuice
//
//  Created by VT010 on 2/24/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct FishbowlMobileSettingsList{
    var message:String?
    var mobileSettings:[FishbowlMobileSetting]
    var successFlag: Bool?
    
    public init(json:JSON){
        message = json["message"].string
        mobileSettings = json["mobileSettings"].arrayValue.map { mobileSetting in FishbowlMobileSetting(json: mobileSetting) }
        successFlag = json["successFlag"].bool
    }
}
