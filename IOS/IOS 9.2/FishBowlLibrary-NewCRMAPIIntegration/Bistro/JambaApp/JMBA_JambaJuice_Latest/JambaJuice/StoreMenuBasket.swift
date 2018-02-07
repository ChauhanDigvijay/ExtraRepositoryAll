//
//  StoreMenuBasket.swift
//  JambaJuice
//
//  Created by VT010 on 8/2/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import OloSDK

struct StoreMenuBasket {
    var store:Store?
    var productTree:ProductFamilyList = []
    var storeBasedFeatureProducts:ProductList = []
    var oloBasket:OloBasket?
    
    init(){
        store = nil
        productTree = []
        storeBasedFeatureProducts = []
        oloBasket = nil
    }
}
