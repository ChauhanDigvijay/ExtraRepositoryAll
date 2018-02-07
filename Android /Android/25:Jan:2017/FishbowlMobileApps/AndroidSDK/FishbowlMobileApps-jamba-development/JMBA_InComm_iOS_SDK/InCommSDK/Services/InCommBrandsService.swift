//
//  InCommBrandsService.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/17/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation

public typealias InCommBrandsCallback = (brands: [InCommBrand], error: NSError?) -> Void
public typealias InCommBrandCallback = (brand: InCommBrand?, error: NSError?) -> Void

public class InCommBrandsService {
    
    public class func brands(callback: InCommBrandsCallback) {
        InCommService.get("/Brands", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(brands: [], error: error)
                return
            }
            let brands = response.arrayValue.map { InCommBrand(json: $0) }
            callback(brands: brands, error: nil)
        }
    }
    
    public class func brandWithId(id: String, callback: InCommBrandCallback) {
        InCommService.get("/Brands/\(id)", parameters: nil) { (response, error) -> Void in
            if error != nil {
                callback(brand: nil, error: error)
                return
            }
            
            let brand =  preValidation(InCommBrand(json: response))
            if brand == nil{
                callback(brand: nil, error:  InCommUtils.error("Unexpected error encountered during the request.", code: 0, domain: InCommErrorDomain.GeneralError.rawValue))
                return
            }
            else{
                callback(brand: brand, error: nil)
                return
            }
        }
    }
    
    public class func preValidation(brand:InCommBrand!) -> InCommBrand?{
        var inCommBrand = brand
        if inCommBrand.billingStates.count == 0 || inCommBrand.billingCountries.count == 0 || inCommBrand.quantities.count == 0 || inCommBrand.variableAmountDenominationMaximumValue == nil || inCommBrand.variableAmountDenominationMinimumValue == nil || inCommBrand.cardImages.count == 0 || inCommBrand.creditCardTypesAndImages.count == 0 {
            inCommBrand = nil
            return inCommBrand
        }
        return inCommBrand
    }
}
