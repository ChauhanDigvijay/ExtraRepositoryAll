//
//  UpSellProductsService.swift
//  JambaJuice
//
//  Created by VT010 on 10/23/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import Parse

typealias UpSellProductsImagesCallback = ([UpSellProductsImages]) -> Void
typealias UpsellProductImagesAndConfigCompleteCallback = ([UpSellProductsImages],UpSellConfig) -> Void
typealias upsellConfigCallback = (UpSellConfig) -> Void



class UpSellProductsService {
    
    static let sharedInstance = UpSellProductsService()
    var upSellProductImages:[UpSellProductsImages] = []
    var upSellConfig:UpSellConfig = UpSellConfig.init()
    
    // Featured products from local cache
    func getUpSellProductsImages(callback: @escaping UpsellProductImagesAndConfigCompleteCallback) {
        UIApplication.inBackground {
            self.loadUpSellConfigAndUpSellProductsImagesFromRemote()
                self.loadUpSellProductsImages({ (images) in
                    self.upSellProductImages = images
                })
                self.loadUpsellConfig({ (config) in
                    self.upSellConfig = config
                })
            
            UIApplication.inMainThread {
                return callback(self.upSellProductImages, self.upSellConfig)
            }
        }
        
    }
    func loadUpSellConfigAndUpSellProductsIfNeeded(){
        // Check if it is time to download products and save to local data store
        if let lastPullDate = SettingsManager.setting(.LastUpSellProductsUpdate) as? Date {
            if abs(lastPullDate.timeIntervalSinceNow) < Constants.parseRemoteSyncPeriod {
                return
            }
        }
        
        // Load products from remote
        loadUpSellConfigAndUpSellProductsImagesFromRemote()
    }
    
    func loadUpSellConfigAndUpSellProductsImagesFromRemote() {
        do {
            // UpSell Products Images
            log.verbose("Downloading upsellproductsimages from Parse.com ...")
            let query = PFQuery(className: UpSellProductsImages.parseClassName)
            let upSellProductsImages = try query.findObjects()
            let _ = try? PFObject.unpinAllObjects(withName: UpSellProductsImages.parseClassName) // Ignore if it fails
            log.verbose("Downloaded \(upSellProductsImages.count) upsellproductsimages from Parse.com")
            try PFObject.pinAll(upSellProductsImages, withName: UpSellProductsImages.parseClassName)
            
            // UpSell Products Images Config
            log.verbose("Downloading products from Parse.com ...")
            let cofigquery = PFQuery(className: UpSellConfig.parseClassName)
            let uspellConfig = try cofigquery.findObjects()
            let _ = try? PFObject.unpinAllObjects(withName: UpSellConfig.parseClassName) // Ignore if it fails
            log.verbose("Downloaded \(uspellConfig.count) upsellproductsimages from Parse.com")
            try PFObject.pinAll(uspellConfig, withName: UpSellConfig.parseClassName)
            
            SettingsManager.setSetting(.LastProductUpdate, value: Date() as AnyObject)
        } catch {
            log.error("Failed to load products from remote")
        }
    }
    
    func loadUpSellProductsImages(_ callback: @escaping UpSellProductsImagesCallback){
        let query = PFQuery(className: UpSellProductsImages.parseClassName)
        query.fromLocalDatastore()
        do {
            let productImages = try query.findObjects()
            UIApplication.inMainThread {
                callback(productImages.map { UpSellProductsImages(parseObject: $0) })
            }
        } catch {
            UIApplication.inMainThread {
                callback([]) // Treat as no upsell products
            }
        }
    }
    
    func loadUpsellConfig(_ callback: @escaping upsellConfigCallback){
        let query = PFQuery(className: UpSellConfig.parseClassName)
        query.fromLocalDatastore()
        do {
            let result = try query.findObjects()
            UIApplication.inMainThread {
                if result.count > 0 {
                    callback(UpSellConfig(parseObject:result[0]))
                    
                }else{
                    callback(UpSellConfig.init())
                }
            }
        } catch {
            UIApplication.inMainThread {
                callback(UpSellConfig.init())
            }
        }
    }
    
}
