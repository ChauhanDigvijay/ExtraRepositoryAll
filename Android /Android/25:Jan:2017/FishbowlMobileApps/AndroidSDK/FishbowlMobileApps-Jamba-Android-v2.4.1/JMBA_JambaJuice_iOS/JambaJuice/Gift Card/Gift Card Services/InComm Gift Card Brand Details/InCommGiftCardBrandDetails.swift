//
//  InCommGiftCardBrandDetails.swift
//  JambaGiftCard
//
//  Created by vThink on 8/26/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import InCommSDK

typealias InCommBrandDetailsCallback = (_ brand: InCommBrand?, _ error: NSError?) -> Void

class InCommGiftCardBrandDetails{
    static let sharedInstance = InCommGiftCardBrandDetails()
    
    let brandID = Configuration.sharedConfiguration.InCommBrandId
    
    fileprivate(set) var brand:InCommBrand?
    
    
    func loadBrandDetails(_ callback:@escaping InCommBrandDetailsCallback){
        if brand != nil{
            return callback(brand, nil)
        }
        else if GiftCardCreationService.sharedInstance.inCommAuthToken  != nil{
            self.getBrandDetails({ (brand, error) in
                return callback(brand, error)
            })
        }else{
            InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration{ (inCommUserStatus, inCommError) in
                if !inCommUserStatus{
                    return callback(nil, inCommError)
                }else{
                    self.getBrandDetails({ (brand, error) in
                        return callback(brand, error)
                    })
                }
                
            }
        }
    /*    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration { (inCommUserStatus) in
            if !inCommUserStatus{
                return callback(nil, GiftCardAppConstants.generalError)
            }else{
                InCommBrandsService.brandWithId(self.brandID) { (brand, error) in
                    if error != nil{
                        // When user credentails invalid repeat process
                        if error!.code == GiftCardAppConstants.errorCodeInvalidUser {
                            InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                                if inCommUserStatus{
                                    InCommBrandsService.brandWithId(self.brandID, callback: { (brand, error) in
                                        if error != nil{
                                            return callback(nil, error)
                                        }
                                        else{
                                            self.reSetBrandDetails(brand!)
                                            return callback(brand, nil)
                                        }
                                    })
                                }
                                else{
                                    return callback(nil, GiftCardAppConstants.generalError)
                                }
                            })
                        }
                        else{
                            return callback(nil, error)
                        }
                        //
                    }
                        
                    else{
                        self.reSetBrandDetails(brand!)
                        return callback(brand, nil)
                    }
                }
            }
        } */
    }
    
    func reSetBrandDetails(_ brand: InCommBrand){
        self.brand = brand
    }
    
    func removeBrandDetails(){
        brand = nil
    }
    func getBrandDetails(_ callback:@escaping InCommBrandDetailsCallback){
        InCommBrandsService.brandWithId(self.brandID) { (brand, error) in
            if error != nil{
                // When user credentails invalid repeat process
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser {
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommBrandsService.brandWithId(self.brandID, callback: { (brand, error) in
                                if error != nil{
                                    return callback(nil, error)
                                }
                                else{
                                    self.reSetBrandDetails(brand!)
                                    return callback(brand, nil)
                                }
                            })
                        }
                        else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil, error)
                }
                //
            }
                
            else{
                self.reSetBrandDetails(brand!)
                return callback(brand, nil)
            }
        }
    }
}
