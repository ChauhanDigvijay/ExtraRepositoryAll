//
//  FishbowlStoreList.swift
//  JambaJuice
//
//  Created by VT010 on 2/16/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import SwiftyJSON

public struct FishbowlStoreList{
    
    var storeList:[FishbowlStore]?
    var message:String
    var successFlag:Bool?
    public init(json:JSON){
        self.message = json["message"].string!
        self.successFlag = json["successFlag"].bool
        self.storeList = json["stores"].arrayValue.map { store in FishbowlStore(json: store) }
    }
}
