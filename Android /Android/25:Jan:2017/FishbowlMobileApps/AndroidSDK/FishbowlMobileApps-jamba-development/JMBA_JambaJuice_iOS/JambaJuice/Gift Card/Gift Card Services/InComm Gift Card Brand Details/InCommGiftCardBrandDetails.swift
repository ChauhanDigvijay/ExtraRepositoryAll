//
//  InCommGiftCardBrandDetails.swift
//  JambaGiftCard
//
//  Created by vThink on 8/26/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import InCommSDK

typealias InCommBrandDetailsCallback = (brand: InCommBrand?, error: NSError?) -> Void

class InCommGiftCardBrandDetails{
    static let sharedInstance = InCommGiftCardBrandDetails()
    
    let brandID = Configuration.sharedConfiguration.InCommBrandId
    
    private(set) var brand:InCommBrand?
    
    
    func loadBrandDetails(callback: InCommBrandDetailsCallback){
        if brand != nil{
            return callback(brand: brand, error: nil)
        }
        InCommBrandsService.brandWithId(brandID) { (brand, error) in
            if error != nil{
                // When user credentails invalid repeat process
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser {
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommBrandsService.brandWithId(self.brandID, callback: { (brand, error) in
                                if error != nil{
                                    return callback(brand: nil, error: error)
                                }
                                else{
                                    self.reSetBrandDetails(brand!)
                                    return callback(brand: brand, error: nil)
                                }
                            })
                        }
                        else{
                            return callback(brand: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(brand: nil, error: error)
                }
                //
            }
                
            else{
                self.reSetBrandDetails(brand!)
                return callback(brand:brand, error:nil)
            }
        }
    }
    
    func reSetBrandDetails(brand: InCommBrand){
        self.brand = brand
    }
    
    func removeBrandDetails(){
        brand = nil
    }
}
