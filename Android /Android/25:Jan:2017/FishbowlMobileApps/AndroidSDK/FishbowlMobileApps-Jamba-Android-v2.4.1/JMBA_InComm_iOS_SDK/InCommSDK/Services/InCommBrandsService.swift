//
//  InCommBrandsService.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/17/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public typealias InCommBrandsCallback = (_ brands: [InCommBrand], _ error: NSError?) -> Void
public typealias InCommBrandCallback = (_ brand: InCommBrand?, _ error: NSError?) -> Void

open class InCommBrandsService {
    
    open class func brands(_ callback: @escaping InCommBrandsCallback) {
        InCommService.get("/Brands", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback([], error)
                return
            }
            let brands = response.arrayValue.map { InCommBrand(json: $0) }
            callback(brands, nil)
        }
    }
    
    open class func brandWithId(_ id: String, callback: @escaping InCommBrandCallback) {
        InCommService.get("/Brands/\(id)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(nil, error)
                return
            }
            
            let brand =  preValidation(InCommBrand(json: response))
            if brand == nil{
                callback(nil, InCommUtils.error("Unexpected error encountered during the request.", code: 0, domain: InCommErrorDomain.GeneralError.rawValue))
                return
            }
            else{
                callback(brand, nil)
                return
            }
        }
    }
    
    open class func preValidation(_ brand:InCommBrand!) -> InCommBrand?{
        var inCommBrand = brand
        if inCommBrand?.billingStates.count == 0 || inCommBrand?.billingCountries.count == 0 || inCommBrand?.quantities.count == 0 || inCommBrand?.variableAmountDenominationMaximumValue == nil || inCommBrand?.variableAmountDenominationMinimumValue == nil || inCommBrand?.cardImages.count == 0 || inCommBrand?.creditCardTypesAndImages.count == 0 {
            inCommBrand = nil
            return inCommBrand
        }
        return inCommBrand
    }
}
