//
//  CardAutoReloadDetails.swift
//  JambaGiftCard
//
//  Created by vThink on 9/5/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import InCommSDK

class CardAutoReloadDetails{
    
    var userGiftCard        :InCommUserGiftCard?
    var autoReloadDetails   :InCommAutoReloadSavable?
    
    init(userGiftCard:InCommUserGiftCard?,autoReloadDetails:InCommAutoReloadSavable?){
        self.userGiftCard = userGiftCard
        self.autoReloadDetails = autoReloadDetails
    }
    
    func setAutoReloadDetails(autoReloadDetails:InCommAutoReloadSavable?, error: NSError?){
        self.autoReloadDetails = autoReloadDetails
    }
    
}
