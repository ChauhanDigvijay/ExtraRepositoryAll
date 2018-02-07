//
//  ProductService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/12/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//


import UIKit
import Parse

private let productEntityName = "Product"
private let productCategoryEntityName = "ProductCategory"
private let productFamilyEntityName = "ProductFamily"


class ProductService {

    // Force downloading products from remote
    class func forceLoadProductsInBackground() {
        UIApplication.inBackground {
            loadProductsFromRemote()
        }
    }

    // Load entire product tree
    // Sync method: Call from background thread
    class func loadProductTree(chainProductIds: [Int64]) -> ProductFamilyList {
        loadProductsIfNeeded()
        return productFamiliesWithCategories(chainProductIds)
    }

    // Load all product families
    // Sync method: Call from background thread
    private class func productFamiliesWithCategories(chainProductIds: [Int64]) -> ProductFamilyList {
        let query = PFQuery(className: productFamilyEntityName)
        query.orderByAscending("order")
        query.fromLocalDatastore()
        if let objects = try? query.findObjects() {
            let families: ProductFamilyList = objects.map {
                var family = ProductFamily(parseObject: $0)
                family.categories = self.categoriesForFamilyWithProducts(family, chainProductIds: chainProductIds)
                return family
            }
            return families.filter { $0.categories.count > 0 }
        }
        return []
    }

    // Product categories for a family
    // Sync method: Call from background thread
    private class func categoriesForFamilyWithProducts(family: ProductFamily, chainProductIds: [Int64]) -> ProductCategoryList {
        let query = PFQuery(className: productCategoryEntityName)
        query.whereKey("family", equalTo: PFObject(withoutDataWithClassName: productFamilyEntityName, objectId: family.productFamilyId))
        query.orderByAscending("order")
        query.fromLocalDatastore()
        if let objects = try? query.findObjects() {
            let categories: ProductCategoryList = objects.map {
                var category = ProductCategory(parseObject: $0)
                category.products = self.productsForCategory(category, chainProductIds: chainProductIds)
                return category
            }
            return categories.filter { $0.products.count > 0 }
        }
        return []
    }

    // Products for a category
    // Sync method: Call from background thread
    private class func productsForCategory(category: ProductCategory, chainProductIds: [Int64]) -> ProductList {
        let query = PFQuery(className: productEntityName)
        query.whereKey("category", equalTo: PFObject(withoutDataWithClassName: productCategoryEntityName, objectId: category.productCategoryId))
        query.orderByAscending("order")
        query.fromLocalDatastore()
        if let objects = try? query.findObjects() {
            let products = objects.map { Product(parseObject: $0) }
            return products.filter { chainProductIds.contains($0.chainProductId) }
        }
        return []
    }

    // Sync method: Call from background thread
    private class func loadProductsIfNeeded() {
        // Check if it is time to download products and save to local data store
        if let lastPullDate = SettingsManager.setting(.LastProductUpdate) as? NSDate {
            if abs(lastPullDate.timeIntervalSinceNow) < Constants.parseRemoteSyncPeriod {
                return
            }
        }

        // Load products from remote
        loadProductsFromRemote()
    }

    // Download products from Parse.com and store in Parse local datastore
    // Sync method: Call from background thread
    private class func loadProductsFromRemote() {
        do {
            log.verbose("Downloading products from Parse.com ...")
            let query = PFQuery(className: productEntityName)
            query.whereKey("published", equalTo: true)
            query.includeKey("category")
            query.includeKey("family")
            let products = try query.findObjects()
            let _ = try? PFObject.unpinAllObjectsWithName(productEntityName) // Ignore if it fails
            log.verbose("Downloaded \(products.count) products from Parse.com")
            try PFObject.pinAll(products, withName: productEntityName)
            SettingsManager.setSetting(.LastProductUpdate, value: NSDate())
        } catch {
            log.error("Failed to load products from remote")
        }
    }

}
