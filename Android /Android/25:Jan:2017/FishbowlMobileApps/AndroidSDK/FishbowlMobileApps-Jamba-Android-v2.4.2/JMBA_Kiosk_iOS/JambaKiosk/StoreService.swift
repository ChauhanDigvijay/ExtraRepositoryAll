//
//  StoreService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/25/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK
import HDK

typealias StoreCallback = (store: Store?, error: NSError?) -> Void
typealias StoreProductModifiersCallback = (modifiers: [StoreMenuProductModifier], error: NSError?) -> Void
typealias StoreErrorCallback = (error: NSError?) -> Void

class StoreService {

    static let sharedInstance = StoreService()

    private(set) var currentStore: Store?

    /// Load a store by Jamba Juice store code
    func loadStoreWithCode(storeCode: String, callback: StoreCallback) {
        OloRestaurantService.restaurant(storeCode) { (oloRestaurant, error) in
            if error != nil {
                callback(store: nil, error: error)
                return
            }
            self.currentStore = Store(oloRestaurant: oloRestaurant!)
            callback(store: self.currentStore, error: error)
//            self.loadStoreMenu { error in
//            }
        }
    }

    /// Retrieve product menu for the current store
    func loadStoreMenu(callback: StoreErrorCallback) {
        guard let restaurantId = currentStore?.restaurantId else {
            return
        }
        OloMenuService.restaurantMenu(restaurantId) { (oloCategories, error) in
            if error != nil {
                callback(error: error)
                return
            }
            var storeMenu: StoreMenu = [:]
            for oloCategory in oloCategories {
                for oloProduct in oloCategory.products {
                    let storeMenuProduct = StoreMenuProduct(productId: oloProduct.id, chainProductId: oloProduct.chainProductId, cost: oloProduct.cost)
                    storeMenu[oloProduct.chainProductId] = storeMenuProduct
                }
            }
            self.currentStore?.storeMenu = storeMenu
            callback(error: nil)
        }
    }

    func loadModifiersForAllProducts(complete: Void -> Void) {
        guard let storeMenu = currentStore?.storeMenu else {
            log.error("Store Menu not found")
            complete()
            return
        }
        let group = dispatch_group_create()
        for (_, menuProduct) in storeMenu {
            log.verbose("Loading modifiers for product: \(menuProduct.productId)")
            dispatch_group_enter(group)
            loadModifiersForProduct(menuProduct.productId) { modifiers, error in
                menuProduct.setModifiers(modifiers)
                dispatch_group_leave(group)
            }
        }
        // Wait for all product modifiers to load
        dispatch_group_notify(group, dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0)) {
            complete()
        }
    }

    /// Retrieve modifiers for a given product
    func loadModifiersForProduct(productId: Int64, callback: StoreProductModifiersCallback) {
        OloMenuService.productModifiers(productId) { (oloModifiers, error) -> Void in
            if error != nil {
                callback(modifiers: [], error: error)
                return
            }
            let modifiers = oloModifiers.map { StoreMenuProductModifier(oloModifier: $0) }
            callback(modifiers: modifiers, error: nil)
        }
    }

}
