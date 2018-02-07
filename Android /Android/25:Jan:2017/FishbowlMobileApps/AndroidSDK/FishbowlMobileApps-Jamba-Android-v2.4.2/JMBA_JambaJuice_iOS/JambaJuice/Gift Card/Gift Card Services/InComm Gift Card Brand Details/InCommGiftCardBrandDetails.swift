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
typealias CreditCardImageCallback = (_ imageURL:String?, _ imageName:String?) -> Void

enum CreditCardTypeStringValue :String {
    case AmericanExpress = "AMEX"
    case Visa = "VISA"
    case MasterCard = "MasterCard"
    case Discover = "Discover"
}

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
    
    // get credit card Type
    
    func getCreditCardType(_ cardNumber:String) -> String{
        var selectedCardType = ""
        switch cardNumber {
        case CreditCardType.AmericanExpress.rawValue:
            selectedCardType = "AMEX"
        case CreditCardType.Visa.rawValue:
            selectedCardType = "VISA"
        case CreditCardType.MasterCard.rawValue:
            selectedCardType = "MasterCard"
        case CreditCardType.Discover.rawValue:
            selectedCardType = "Discover"
        default:
            // Set visa type as default
            selectedCardType = "VISA"
        }
        for cardType in brand!.creditCardTypesAndImages {
            if cardType.creditCardType.lowercased() == selectedCardType.lowercased() {
                return cardType.creditCardType
            }
        }
        // Default creditcard type
        return "VISA"
    }
    // get credit card Type image or image url
    func getCreditCardTypeImageNameOrImageURL(_ cardNumber:String, callback:CreditCardImageCallback) {
        var imageURL:String? = ""
        var imageName:String? = ""
        var selectedCardType = ""
        switch cardNumber {
        case CreditCardType.AmericanExpress.rawValue:
            selectedCardType = "AMEX"
        case CreditCardType.Visa.rawValue:
            selectedCardType = "VISA"
        case CreditCardType.MasterCard.rawValue:
            selectedCardType = "MasterCard"
        case CreditCardType.Discover.rawValue:
            selectedCardType = "Discover"
        default:
            selectedCardType = "VISA"
        }
        if let brand = InCommGiftCardBrandDetails.sharedInstance.brand{
            for cardType in brand.creditCardTypesAndImages {
                if cardType.creditCardType.lowercased() == selectedCardType.lowercased() {
                    imageURL = cardType.thumbnailImageUrl
                    break;
                }
            }
        }
        switch cardNumber {
        case CreditCardType.AmericanExpress.rawValue:
            imageName = "american_exp_card"
        case CreditCardType.Visa.rawValue:
            imageName = "visa_card"
        case CreditCardType.MasterCard.rawValue:
            imageName = "master_card_card"
        case CreditCardType.Discover.rawValue:
            imageName = "discover_card"
        default:
            imageName = "visa_card"
        }
        
        return callback(imageURL,imageName)
    }
    
    // ImageName or ImageURL for creditCardType
    func getCreditCardTypeImageNameOrImageURLForCreditCardType(_ creditCardType:String, callback:CreditCardImageCallback){
        var imageURL:String? = ""
        var imageName:String? = ""
        if let brand = InCommGiftCardBrandDetails.sharedInstance.brand{
            for cardType in brand.creditCardTypesAndImages {
                if cardType.creditCardType.lowercased() == creditCardType.lowercased() {
                    imageURL = cardType.thumbnailImageUrl
                    break;
                }
            }
        }
        switch creditCardType {
        case CreditCardTypeStringValue.AmericanExpress.rawValue:
            imageName = "american_exp_card"
        case CreditCardTypeStringValue.Visa.rawValue:
            imageName = "visa_card"
        case CreditCardTypeStringValue.MasterCard.rawValue:
            imageName = "master_card_card"
        case CreditCardTypeStringValue.Discover.rawValue:
            imageName = "discover_card"
        default:
            imageName = "visa_card"
        }
        
        return callback(imageURL,imageName)
    }
}
