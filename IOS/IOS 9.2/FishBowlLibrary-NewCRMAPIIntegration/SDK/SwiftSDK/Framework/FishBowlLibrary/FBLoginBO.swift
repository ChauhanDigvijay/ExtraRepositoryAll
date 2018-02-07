//
//  FBLoginBO.swift
//  FishBowlLibrary
//
//  Created by Puneet  on 8/31/17.
//  Copyright © 2017 Fishbowl. All rights reserved.
//

import Foundation

public protocol ResponseBO {
    
}

public struct FBLoginBO : ResponseBO {
    
    public  var userName : String = ""
    public  var password : String = ""
    public  var deviceId : String = ""
    public  var oldPassword : String = ""
    
    public init()
    {
        
    }
    
    
}

public struct FBDeviceBO : ResponseBO {
    
    public  var appId : String = ""
    public  var deviceOSVersion : String = ""
    public  var memberId : String = ""
    public  var pushToken : String = ""
    
    
    public init()
    {
        
    }
    
    
}

public struct OfferBO : ResponseBO {
    
    public var campaignId : CLong = 0
    public var campaignTitle : String = ""
    public var campaignDescription : String = ""
    public var validityEndDateTime : String = ""
    public var promotionId : CLong = 0
    public var channelTypeId : CLong = 0
    public var campaignType : CLong = 0
    public var promotionCode : String = ""
    public var couponURL : String = ""
    public var arrStoreRestriction : NSMutableArray = []
    public var offerId : CLong = 0
    public var loyaltyPoints : CLong = 0

    
    
    
    public init()
    {
        
    }
    
}


public struct StoreBO : ResponseBO {
    
    public var storeName : String = ""
    public var address : String = ""
    public var latitude : Double = 0.0
    public var longitude : Double = 0.0
    public var channelTypeId : CLong = 0
    public var storeId : CLong = 0
    public var storeNumber : String = ""
    public var city : String = ""
    public var arrStoreHourList : NSMutableArray = []
    public var state : String = ""
    public var zip : String = ""
    public var country : String = ""
    public var phone : String = ""
    public var geoFenceCorrFactor : CLong = 0

    
    public init()
    {
        
    }
    
}


public struct SearchStoreBO : ResponseBO {
    
    public var storeName : String = ""
    public var description : String = ""
    public var address : String = ""
    public var latitude : Double = 0.0
    public var longitude : Double = 0.0
    public var channelTypeId : CLong = 0
    public var storeId : CLong = 0
    public var storeNumber : String = ""
    public var city : String = ""
    public var arrStoreHourList : NSMutableArray = []
    public var state : String = ""
    public var zip : String = ""
    public var country : String = ""
    public var phone : String = ""
    public var geoFenceCorrFactor : CLong = 0
    public var email : String = ""
    public var distance : CLong = 0
    public var storeImage : String = ""

    
    public init()
    {
        
    }
    
}

public struct MenuCategoryBO : ResponseBO {
    
    public var productCategoryId : CLong = 0
    public var productCategoryName : String = ""
    public var productCategoryDescription : String = ""
    public var productCategoryImageUrl : String = ""
    public var productCategoryThumbImageUrl : String = ""
    public var active : Bool = false
    

}

public struct FBMenuCategoryBO : ResponseBO {

    public var successFlag : Bool = false
    public var message : String = ""
    public var arrProductCategoryList : NSMutableArray = []
    
    
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        var arrProductCategory : NSArray  = []
        
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        
        
        if let message = myModel["productCategoryList"] as? NSArray {
            print("arrProductCategoryList inside Model is \(message)")
            
            arrProductCategory = message
            print("arrProductCategoryList is \(arrProductCategoryList)")
            var arrTempCategory = [[String:AnyObject]]()
            arrTempCategory = arrProductCategory as! [[String:AnyObject]]
            print("arrProductCategoryList is \(arrTempCategory)")
            
            for  i in (0..<arrProductCategory.count) {
                
                var objMenu =  MenuCategoryBO()
                
                let dicOffer : Dictionary = arrTempCategory[i] as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["productCategoryId"] as? CLong {
                    objMenu.productCategoryId = message
                } else {
                    objMenu.productCategoryId = 0
                }
                if let message = dicOffer["productCategoryName"] as? String {
                    objMenu.productCategoryName = message
                } else {
                    objMenu.productCategoryName = ""
                }
                if let message = dicOffer["productCategoryDescription"] as? String {
                    objMenu.productCategoryDescription = message
                } else {
                    objMenu.productCategoryDescription = ""
                }
                if let message = dicOffer["productCategoryImageUrl"] as? String {
                    objMenu.productCategoryImageUrl = message
                } else {
                    objMenu.productCategoryImageUrl = ""
                }
                if let message = dicOffer["productCategoryThumbImageUrl"] as? String {
                    objMenu.productCategoryThumbImageUrl = message
                } else {
                    objMenu.productCategoryThumbImageUrl = ""
                }
                if let message = dicOffer["active"] as? Bool {
                    objMenu.active = message
                } else {
                    objMenu.active = false
                }
                
                if(objMenu.active == true)
                {
                    arrProductCategoryList.add(objMenu)
                }
                
            }
        }
        
    }
   
}


public struct MenuSubCategoryBO : ResponseBO {
    
    public var productSubCategoryId : CLong = 0
    public var productSubCategoryName : String = ""
    public var productSubCategoryDescription : String = ""
    public var productSubCategoryImageUrl : String = ""
    public var productSubCategoryThumbImageUrl : String = ""
    public var active : Bool = false

    
}

public struct FBMenuSubCategoryBO : ResponseBO {
    
    public var successFlag : Bool = false
    public var message : String = ""
    public var arrProductSubCategoryList : NSMutableArray = []
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        var arrProductSubCategory : NSArray  = []
        
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        
        
        if let message = myModel["productSubCategoryList"] as? NSArray {
            print("arrProductSubCategory inside Model is \(message)")
            
            arrProductSubCategory = message
            print("arrProductSubCategory is \(arrProductSubCategory)")
            var arrTempSubCategory = [[String:AnyObject]]()
            arrTempSubCategory = arrProductSubCategory as! [[String:AnyObject]]
            print("arrProductSubCategoryList is \(arrTempSubCategory)")
            
            for  i in (0..<arrProductSubCategory.count) {
                
                var objMenu =  MenuSubCategoryBO()
                
                let dicOffer : Dictionary = arrTempSubCategory[i] as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["productSubCategoryId"] as? CLong {
                    objMenu.productSubCategoryId = message
                } else {
                    objMenu.productSubCategoryId = 0
                }
                if let message = dicOffer["productSubCategoryName"] as? String {
                    objMenu.productSubCategoryName = message
                } else {
                    objMenu.productSubCategoryName = ""
                }
                if let message = dicOffer["productSubCategoryDescription"] as? String {
                    objMenu.productSubCategoryDescription = message
                } else {
                    objMenu.productSubCategoryDescription = ""
                }
                if let message = dicOffer["productSubCategoryImageUrl"] as? String {
                    objMenu.productSubCategoryImageUrl = message
                } else {
                    objMenu.productSubCategoryImageUrl = ""
                }
                if let message = dicOffer["productSubCategoryThumbImageUrl"] as? String {
                    objMenu.productSubCategoryThumbImageUrl = message
                } else {
                    objMenu.productSubCategoryThumbImageUrl = ""
                }
                if let message = dicOffer["active"] as? Bool {
                    objMenu.active = message
                } else {
                    objMenu.active = false
                }
                
                if(objMenu.active == true)
                {
                    arrProductSubCategoryList.add(objMenu)
                }
                
            }
        }
        
    }
    
}


public struct MenuProductListBO : ResponseBO {
    
    public var productId : CLong = 0
    public var productName : String = ""
    public var productLongDescription : String = ""
    public var productShortDescription : String = ""
    public var productImageUrl : String = ""
    public var productThumbImageUrl : String = ""
    public var productBasePrice : CLong = 0
    public var productNutritionDescription : String = ""
    public var active : Bool = false
    

    
}

public struct FBMenuProductListBO : ResponseBO {
    
    public var successFlag : Bool = false
    public var message : String = ""
    public var arrProductList : NSMutableArray = []
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        var arrProduct : NSArray  = []
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        
        
        if let message = myModel["productList"] as? NSArray {
            print("productList inside Model is \(message)")
            
            arrProduct = message
            print("arrProductSubCategory is \(arrProduct)")
            var arrTempProduct = [[String:AnyObject]]()
            arrTempProduct = arrProduct as! [[String:AnyObject]]
            print("arrProductSubCategoryList is \(arrTempProduct)")
            
            for  i in (0..<arrProduct.count) {
                
                var objMenu =  MenuProductListBO()

                let dicOffer : Dictionary = arrTempProduct[i] as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["id"] as? CLong {
                    objMenu.productId = message
                } else {
                    objMenu.productId = 0
                }
                if let message = dicOffer["productName"] as? String {
                    objMenu.productName = message
                } else {
                    objMenu.productName = ""
                }
                if let message = dicOffer["productLongDescription"] as? String {
                    objMenu.productLongDescription = message
                } else {
                    objMenu.productLongDescription = ""
                }
                if let message = dicOffer["productShortDescription"] as? String {
                    objMenu.productShortDescription = message
                } else {
                    objMenu.productShortDescription = ""
                }
                if let message = dicOffer["productImageUrl"] as? String {
                    objMenu.productImageUrl = message
                } else {
                    objMenu.productImageUrl = ""
                }
                if let message = dicOffer["productThumbImageUrl"] as? String {
                    objMenu.productThumbImageUrl = message
                } else {
                    objMenu.productThumbImageUrl = ""
                }
                if let message = dicOffer["productBasePrice"] as? CLong {
                    objMenu.productBasePrice = message
                } else {
                    objMenu.productBasePrice = 0
                }
                if let message = dicOffer["productNutritionDescription"] as? String {
                    objMenu.productNutritionDescription = message
                } else {
                    objMenu.productNutritionDescription = ""
                }
                
                
                if let message = dicOffer["active"] as? Bool {
                    objMenu.active = message
                } else {
                    objMenu.active = false
                }
                
                if(objMenu.active == true)
                {
                    arrProductList.add(objMenu)
                }
                
            }
        }
        
    }
    
}


public struct FBMenuProductAttributeBO : ResponseBO {
    
    public var successFlag : Bool = false
    public var message : String = ""
    public var productNutrients : NSMutableArray = []
    public var productIngredients : NSMutableArray = []
    public var productSize : NSMutableArray = []
    public var productModifier : NSMutableArray = []
    public var productOption : NSMutableArray = []
    public var productAllergens : NSMutableArray = []
    public var productBasePrice : Double = 0.0
    public var minWeightRange : CLong = 0
    public var maxWeightRange : CLong = 0

    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        
        
        if let dicOffer = myModel["productDetails"] as? Dictionary<String,AnyObject> {
            print("productList inside Model is \(dicOffer)")
            
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["productNutrientList"] as? NSMutableArray {
                    self.productNutrients = message
                } else {
                    self.productNutrients = []
                }
                if let message = dicOffer["productIngredients"] as? NSMutableArray {
                    self.productIngredients = message
                } else {
                    self.productIngredients = []
                }
                if let message = dicOffer["productSizeList"] as? NSMutableArray {
                    self.productSize = message
                } else {
                    self.productSize = []
                }
                if let message = dicOffer["productModifierList"] as? NSMutableArray {
                    self.productModifier = message
                } else {
                    self.productModifier = []
                }
                if let message = dicOffer["productOptionList"] as? NSMutableArray {
                    self.productOption = message
                } else {
                    self.productOption = []
                }
                if let message = dicOffer["productAllergensList"] as? NSMutableArray {
                    self.productAllergens = message
                } else {
                    self.productAllergens = []
                }
                if let message = dicOffer["productPrice"] as? Double {
                    self.productBasePrice = message
                } else {
                    self.productBasePrice = 0.0
                }
                if let message = dicOffer["minWeightRange"] as? CLong {
                    self.minWeightRange = message
                } else {
                    self.minWeightRange = 0
                }
                
                
                if let message = dicOffer["maxWeightRange"] as? CLong {
                    self.maxWeightRange = message
                } else {
                    self.maxWeightRange = 0
                }
            }
        }
    }



public struct signupRuleListBO : ResponseBO {
    
    public  var criteriaName : String = ""
    public  var criteriaValue : CLong = 0
    public  var description : String = ""
    public  var ruleId : CLong = 0
    public  var rewardRule : Bool = false
    public  var pointRule : Bool = false

}


public struct FBsignupRuleListBO : ResponseBO {
    
    public var arrSignupRuleList : NSMutableArray = []
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSArray else {
            return nil
        }
        
        
        var arrSignupRule : NSArray  = []
        
        print("jsondata inside Model is \(json)")
        
            arrSignupRule = myModel
      
        print("arrOffer is \(arrSignupRule)")
        var tempArrSignupRule = [[String:AnyObject]]()
        tempArrSignupRule = arrSignupRule as! [[String:AnyObject]]
        print("arrOffer is \(tempArrSignupRule)")
        
        
        for  i in (0..<arrSignupRule.count) {
            
            var objRule =  signupRuleListBO()
            
            let dicOffer : Dictionary = tempArrSignupRule[i] as Dictionary<String,AnyObject>
            print("dicOffer is \(dicOffer)")
            
            if let message = dicOffer["criteriaName"] as? String {
                objRule.criteriaName = message
            } else {
                objRule.criteriaName = ""
            }
            
            if let message = dicOffer["criteriaValue"] as? CLong {
                objRule.criteriaValue = message
            } else {
                objRule.criteriaValue = 0
            }
            if let message = dicOffer["description"] as? String {
                objRule.description = message
            } else {
                objRule.description = ""
            }
            
            if let message = dicOffer["id"] as? CLong {
                objRule.ruleId = message
            } else {
                objRule.ruleId = 0
            }
            if let message = dicOffer["rewardRule"] as? Bool {
                objRule.rewardRule = message
            } else {
                objRule.rewardRule =  false
            }
            if let message = dicOffer["pointRule"] as? Bool {
                objRule.pointRule = message
            } else {
                objRule.pointRule =  false
            }
         
            print("objOffers inside Model is \(objRule)")
            
            arrSignupRuleList.add(objRule)
            
            
        }
        
    }

}


public struct FBOfferBO : ResponseBO {
    
    public  var memberId : String = ""
    public  var offerId : String = ""
    public  var claimPoints : String = ""
    public var successFlag : Bool = false
    public var message : String = ""
    public var arrOffers : NSMutableArray = []

    public init()
    {
        
    }
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        
        var arrOffer : NSArray  = []

        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        if let message = myModel["inAppOfferList"] as? NSArray {
            print("arrOffer inside Model is \(message)")

            arrOffer = message
        } else {
            
        }
        
        
        print("arrOffer is \(arrOffer)")
        var arrOffer1 = [[String:AnyObject]]()
        arrOffer1 = arrOffer as! [[String:AnyObject]]
        print("arrOffer is \(arrOffer1)")

        
        for  i in (0..<arrOffer.count) {
            
            var objOffers =  OfferBO()
            
            let dicOffer : Dictionary = arrOffer1[i] as Dictionary<String,AnyObject>
            print("dicOffer is \(dicOffer)")
            
            if let message = dicOffer["campaignId"] as? CLong {
                objOffers.campaignId = message
            } else {
                objOffers.campaignId = 0
            }
            
            if let message = dicOffer["campaignTitle"] as? String {
                objOffers.campaignTitle = message
            } else {
                objOffers.campaignTitle = ""
            }
            if let message = dicOffer["campaignDescription"] as? String {
                objOffers.campaignDescription = message
            } else {
                objOffers.campaignDescription = ""
            }
            
            if let message = dicOffer["validityEndDateTime"] as? String {
                objOffers.validityEndDateTime = message
            } else {
                objOffers.validityEndDateTime = ""
            }
            if let message = dicOffer["promotionID"] as? CLong {
                objOffers.promotionId = message
            } else {
                objOffers.promotionId =  0
            }
            if let message = dicOffer["channelTypeID"] as? CLong {
                objOffers.channelTypeId = message
            } else {
                objOffers.channelTypeId =  0
            }
            if let message = dicOffer["campaignType"] as? CLong {
                objOffers.campaignType = message
            } else {
                objOffers.campaignType =  0
            }
            if let message = dicOffer["loyaltyPoints"] as? CLong {
                objOffers.loyaltyPoints = message
            } else {
                objOffers.loyaltyPoints =  0
            }
            
            if let message = dicOffer["promotionCode"] as? String {
                objOffers.promotionCode = message
            } else {
                objOffers.promotionCode = ""
            }
            if let message = dicOffer["couponURL"] as? String {
                objOffers.couponURL = message
            } else {
                objOffers.couponURL = ""
            }
            if let message = dicOffer["storeRestriction"] as? NSMutableArray {
                objOffers.arrStoreRestriction = message
            } else {
                
            }
            
            print("objOffers inside Model is \(objOffers)")

            
            arrOffers.add(objOffers)
            
            
        }
        
    }
    
    
}


public struct StateBO : ResponseBO {
    
    public var stateId : CLong = 0
    public var stateName : String = ""
    public var stateCode : String = ""
    public var countryCode : String = ""
    
    
    public init()
    {
        
    }
    
}


public struct FBStateBO : ResponseBO {
    
    var successFlag : Bool = false
    var message : String = ""
    public var arrState : NSMutableArray = []
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        var arrStates : NSArray  = []
        
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        
        
        if let message = myModel["stateList"] as? NSArray {
            print("arrState inside Model is \(message)")
            
            arrStates = message
            print("arrState is \(arrStates)")
            var arrTempState = [[String:AnyObject]]()
            arrTempState = arrStates as! [[String:AnyObject]]
            print("arrState is \(arrTempState)")
            
            for  i in (0..<arrStates.count) {
                
                var objState =  StateBO()
                
                let dicOffer : Dictionary = arrTempState[i] as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["stateID"] as? CLong {
                    objState.stateId = message
                } else {
                    objState.stateId = 0
                }
                if let message = dicOffer["stateName"] as? String {
                    objState.stateName = message
                } else {
                    objState.stateName = ""
                }
                if let message = dicOffer["stateCode"] as? String {
                    objState.stateCode = message
                } else {
                    objState.stateCode = ""
                }
                if let message = dicOffer["countryCode"] as? String {
                    objState.countryCode = message
                } else {
                    objState.countryCode = ""
                }
                
                arrState.add(objState)

            }
        }
        
    }
    
    
    
    
}

public struct FBStoreBO : ResponseBO {
    
    public  var query : String = ""
    public  var memberId : String = ""
    public  var storeCode : String = ""
    var radius : String = "1000"
    var count : String = "100"
    var successFlag : Bool = false
    var message : String = ""
    public var arrStores : NSMutableArray = []
    public var objStoreDetail : SearchStoreBO = SearchStoreBO()
    
    public init()
    {
        
    }
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        
        var arrstore : NSArray  = []
        
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        if let message = myModel["stores"] as? NSArray {
            print("arrstore inside Model is \(message)")
            
            arrstore = message
            print("arrstore is \(arrstore)")
            var arrTempStore = [[String:AnyObject]]()
            arrTempStore = arrstore as! [[String:AnyObject]]
            print("arrstore is \(arrTempStore)")
            
            for  i in (0..<arrstore.count) {
                
                var objStore =  StoreBO()
                
                let dicOffer : Dictionary = arrTempStore[i] as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["storeName"] as? String {
                    objStore.storeName = message
                } else {
                    objStore.storeName = ""
                }
                
                if let message = dicOffer["address"] as? String {
                    objStore.address = message
                } else {
                    objStore.address = ""
                }
                if let message = dicOffer["storeNumber"] as? String {
                    objStore.storeNumber = message
                } else {
                    objStore.storeNumber = ""
                }
                
                if let message = dicOffer["city"] as? String {
                    objStore.city = message
                } else {
                    objStore.city = ""
                }
                if let message = dicOffer["latitude"] as? Double {
                    objStore.latitude = message
                } else {
                    objStore.latitude =  0
                }
                if let message = dicOffer["longitude"] as? Double {
                    objStore.longitude = message
                } else {
                    objStore.longitude =  0
                }
                if let message = dicOffer["storeID"] as? CLong {
                    objStore.storeId = message
                } else {
                    objStore.storeId = 0
                }
                if let message = dicOffer["state"] as? String {
                    objStore.state = message
                } else {
                    objStore.state = ""
                }
                if let message = dicOffer["zip"] as? String {
                    objStore.zip = message
                } else {
                    objStore.zip = ""
                }
                if let message = dicOffer["country"] as? String {
                    objStore.country = message
                } else {
                    objStore.country = ""
                }
                if let message = dicOffer["phone"] as? String {
                    objStore.phone = message
                } else {
                    objStore.phone = ""
                }
                if let message = dicOffer["geoFenceCorrFactor"] as? CLong {
                    objStore.geoFenceCorrFactor = message
                } else {
                    objStore.geoFenceCorrFactor = 0
                }
                if let message = dicOffer["storeHourList"] as? NSMutableArray {
                    objStore.arrStoreHourList = message
                } else {
                    
                }
                
                print("objOffers inside Model is \(objStore)")
                
                arrStores.add(objStore)
                
            }
            
            
            
        } else {
            
        }
        
        if let message = myModel["storeList"] as? NSArray {
            print("arrstore inside Model is \(message)")
            
            arrstore = message
            print("arrstore is \(arrstore)")
            var arrTempStore = [[String:AnyObject]]()
            arrTempStore = arrstore as! [[String:AnyObject]]
            print("arrstore is \(arrTempStore)")
            
            
            for  i in (0..<arrstore.count) {
                
                var objStore =  SearchStoreBO()
                
                let dicOffer : Dictionary = arrTempStore[i] as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["storeName"] as? String {
                    objStore.storeName = message
                } else {
                    objStore.storeName = ""
                }
                if let message = dicOffer["description"] as? String {
                    objStore.description = message
                } else {
                    objStore.description = ""
                }
                
                if let message = dicOffer["address"] as? String {
                    objStore.address = message
                } else {
                    objStore.address = ""
                }
                if let message = dicOffer["number"] as? String {
                    objStore.storeNumber = message
                } else {
                    objStore.storeNumber = ""
                }
                
                if let message = dicOffer["city"] as? String {
                    objStore.city = message
                } else {
                    objStore.city = ""
                }
                if let message = dicOffer["email"] as? String {
                    objStore.email = message
                } else {
                    objStore.email = ""
                }
                if let message = dicOffer["latitude"] as? Double {
                    objStore.latitude = message
                } else {
                    objStore.latitude =  0
                }
                if let message = dicOffer["longitude"] as? Double {
                    objStore.longitude = message
                } else {
                    objStore.longitude =  0
                }
                if let message = dicOffer["storeId"] as? CLong {
                    objStore.storeId = message
                } else {
                    objStore.storeId = 0
                }
                if let message = dicOffer["state"] as? String {
                    objStore.state = message
                } else {
                    objStore.state = ""
                }
                if let message = dicOffer["zipCode"] as? String {
                    objStore.zip = message
                } else {
                    objStore.zip = ""
                }
                if let message = dicOffer["country"] as? String {
                    objStore.country = message
                } else {
                    objStore.country = ""
                }
                if let message = dicOffer["phone"] as? String {
                    objStore.phone = message
                } else {
                    objStore.phone = ""
                }
                if let message = dicOffer["geoFenceCorrFactor"] as? CLong {
                    objStore.geoFenceCorrFactor = message
                } else {
                    objStore.geoFenceCorrFactor = 0
                }
                if let message = dicOffer["distance"] as? CLong {
                    objStore.distance = message
                } else {
                    objStore.distance = 0
                }
                if let message = dicOffer["storeImage"] as? String {
                    objStore.storeImage = message
                } else {
                    objStore.storeImage = ""
                }
                if let message = dicOffer["storeHoursList"] as? NSMutableArray {
                    objStore.arrStoreHourList = message
                } else {
                    
                }
                
                print("objOffers inside Model is \(objStore)")
                
                arrStores.add(objStore)
                
            }

            
            
        } else {
            
        }
        
        
        if let message = myModel["mobileStores"] as? Dictionary<String,AnyObject> {
            print("message inside Model is \(message)")
            
                var objStore =  SearchStoreBO()
                
                let dicOffer : Dictionary = message as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["storeName"] as? String {
                    objStore.storeName = message
                } else {
                    objStore.storeName = ""
                }
                if let message = dicOffer["description"] as? String {
                    objStore.description = message
                } else {
                    objStore.description = ""
                }
                
                if let message = dicOffer["address"] as? String {
                    objStore.address = message
                } else {
                    objStore.address = ""
                }
                if let message = dicOffer["storeNumber"] as? String {
                    objStore.storeNumber = message
                } else {
                    objStore.storeNumber = ""
                }
                
                if let message = dicOffer["city"] as? String {
                    objStore.city = message
                } else {
                    objStore.city = ""
                }
                if let message = dicOffer["primaryContactEmail"] as? String {
                    objStore.email = message
                } else {
                    objStore.email = ""
                }
                if let message = dicOffer["latitude"] as? Double {
                    objStore.latitude = message
                } else {
                    objStore.latitude =  0
                }
                if let message = dicOffer["longitude"] as? Double {
                    objStore.longitude = message
                } else {
                    objStore.longitude =  0
                }
                if let message = dicOffer["storeID"] as? CLong {
                    objStore.storeId = message
                } else {
                    objStore.storeId = 0
                }
                if let message = dicOffer["state"] as? String {
                    objStore.state = message
                } else {
                    objStore.state = ""
                }
                if let message = dicOffer["zip"] as? String {
                    objStore.zip = message
                } else {
                    objStore.zip = ""
                }
                if let message = dicOffer["country"] as? String {
                    objStore.country = message
                } else {
                    objStore.country = ""
                }
                if let message = dicOffer["phone"] as? String {
                    objStore.phone = message
                } else {
                    objStore.phone = ""
                }
                if let message = dicOffer["geoFenceCorrFactor"] as? CLong {
                    objStore.geoFenceCorrFactor = message
                } else {
                    objStore.geoFenceCorrFactor = 0
                }
                if let message = dicOffer["distance"] as? CLong {
                    objStore.distance = message
                } else {
                    objStore.distance = 0
                }
                if let message = dicOffer["storeImage"] as? String {
                    objStore.storeImage = message
                } else {
                    objStore.storeImage = ""
                }
                if let message = dicOffer["storeHourList"] as? NSMutableArray {
                    objStore.arrStoreHourList = message
                } else {
                    
                }
                
                print("objOffers inside Model is \(objStore)")
            
            objStoreDetail = objStore
            
            
        } else {
            
        }
        
        
    }
    
}

public struct FBEventBO : ResponseBO {
    
    public  var memberId : String = ""
    public  var lat : String = ""
    public  var eventName : String = ""
    public  var lon : String = ""
    public  var deviceOsVersion : String = ""
    public  var deviceType : String = ""
    public  var eventsSourceId : String = ""
    public  var itemId : String = ""
    public  var itemName : String = ""
    public  var latitude : Double = 0.0
    public  var longitude : Double = 0.0

    
    public init()
    {
        
    }
    
    
    
    
}

public struct FBGiftCardBO : ResponseBO {
    
    public  var key : String = ""
    public  var authorization : String = ""
    public  var spendgoId : String = ""
    public  var memberId : String = ""
    public  var inCommOrderId : String = ""
    public  var orderStatus : String = ""
    public  var errorReason : String = ""
    public  var orderDateTime : String = ""
    
    public init()
    {
        
    }
    
}


public struct RecipientsBO : ResponseBO {
    
    public  var emailAddress : String = ""
    public  var firstName : String = ""
    public  var lastName : String = ""
    public  var country : String = ""

}


public struct FBGiftCardOrderValueBO : ResponseBO {

    public  var amount : String = ""
    public  var messageTo : String = ""
    public  var key : String = ""
    public  var authorization : String = ""
    public  var spendgoId : String = ""
    public  var incommToken : String = ""
    public  var orderPaymentMethod : String = ""
    public  var purchaserEmailAddress : String = ""
    public  var purchaserFirstName : String = ""
    public  var purchaserLastName : String = ""
    public  var purchaserCountry : String = ""
    public  var purchaserSuppressReceiptEmail : Bool = false
    public  var arrRecipients : NSMutableArray = []
    
}
public struct FBRegistrationBO : ResponseBO {
    
    public var successFlag : Bool = false
    public var message : String = ""
    public var memberId : CLong = 0
    public var loyalityNo : String = ""
    public var FirstName : String = ""
    public var LastName : String = ""
    public var Password : String = ""
    public var DOB : String = ""
    public var Gender : String = ""
    public var Address : String = ""
    public var FavoriteStore : String = ""
    public var EmailOptIn : Bool = false
    public var PhoneNumber : String = ""
    public var SMSOptIn : Bool = false
    public var LoyaltyOptIn : String = ""
    public var PushOptIn : String = ""
    public var TenantId : String = ""
    public var UpdatedBy : String = ""
    public var InputSource : String = ""
    public var EmailAddress : String = ""
    public var State : String = ""
    public var City : String = ""
    public var ZipCode : String = ""
    public var StoreCode : String = ""
    public var Country : String = ""
    public var Bonus : String = ""
    public var appId : String = ""
    public  var deviceOSVersion : String = ""
    public  var deviceType : String = ""
    public  var pushToken : String = ""
    public var Custom1 : String = ""
    public var Custom2 : String = ""
    public var Custom3 : String = ""
    public var Custom4 : String = ""
    public var Custom5 : String = ""
    public var Custom6 : String = ""
    public var Custom7 : String = ""
    public var Custom8 : String = ""
    public var Custom9 : String = ""
    public var Custom10 : String = ""
    public var Custom11 : String = ""
    public var Custom12 : String = ""
    public var Custom13 : String = ""
    public var Custom14 : String = ""
    public var Custom15 : String = ""
    public var Custom16 : String = ""
    public var Custom17 : String = ""
    public var Custom18 : String = ""
    public var Custom19 : String = ""
    public var Custom20 : String = ""
    public var Custom21 : String = ""
    public var Custom22 : String = ""
    public var Custom23 : String = ""
    public var Custom24 : String = ""
    public var Custom25 : String = ""
    public var Custom26 : String = ""
    public var Custom27 : String = ""
    public var Custom28 : String = ""
    public var Custom29 : String = ""
    public var Custom30 : String = ""
    public var Custom31 : String = ""
    public var Custom32 : String = ""
    public var Custom33 : String = ""
    public var Custom34 : String = ""
    public var Custom35 : String = ""
    public var Custom36 : String = ""
    public var Custom37 : String = ""
    public var Custom38 : String = ""
    public var Custom39 : String = ""
    public var Custom40 : String = ""
    public var Custom41 : String = ""
    public var Custom42 : String = ""
    public var Custom43 : String = ""
    public var Custom44 : String = ""
    public var Custom45 : String = ""
    public var Custom46 : String = ""
    public var Custom47 : String = ""
    public var Custom48 : String = ""
    public var Custom49 : String = ""
    public var Custom50 : String = ""
    public var Custom51 : String = ""
    public var Custom52 : String = ""
    public var Custom53 : String = ""
    public var Custom54 : String = ""
    public var Custom55 : String = ""
    public var Custom56 : String = ""
    public var Custom57 : String = ""
    public var Custom58 : String = ""
    public var Custom59 : String = ""
    public var Custom60 : String = ""
    public var Custom61 : String = ""
    public var Custom62 : String = ""
    public var Custom63 : String = ""
    public var Custom64 : String = ""
    public var Custom65 : String = ""
    public var Custom66 : String = ""
    public var Custom67 : String = ""
    public var Custom68 : String = ""
    public var Custom69 : String = ""
    public var Custom70 : String = ""
    public var Custom71 : String = ""
    public var Custom72 : String = ""
    public var Custom73 : String = ""
    public var Custom74 : String = ""
    public var Custom75 : String = ""
    public var Custom76 : String = ""
    public var Custom77 : String = ""
    public var Custom78 : String = ""
    public var Custom79 : String = ""
    public var Custom80 : String = ""
    public var Custom81 : String = ""
    public var Custom82 : String = ""
    public var Custom83 : String = ""
    public var Custom84 : String = ""
    public var Custom85 : String = ""
    public var Custom86 : String = ""
    public var Custom87 : String = ""
    public var Custom88 : String = ""
    public var Custom89 : String = ""
    public var Custom90 : String = ""
    public var Custom91 : String = ""
    public var Custom92 : String = ""
    public var Custom93 : String = ""
    public var Custom94 : String = ""
    public var Custom95 : String = ""
    public var Custom96 : String = ""
    public var Custom97 : String = ""
    public var Custom98 : String = ""
    public var Custom99 : String = ""
    public var Custom100 : String = ""
    
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        
        if let strMemberId = myModel["memberid"] as? CLong {
            self.memberId = strMemberId
        } else {
            self.memberId = 0
        }
        if let message = myModel["firstName"] as? String {
            self.FirstName = message
        } else {
            self.FirstName = ""
        }
        if let message = myModel["lastName"] as? String {
            self.LastName = message
        } else {
            self.LastName = ""
        }
        if let message = myModel["emailID"] as? String {
            self.EmailAddress = message
        }
        else {
            self.EmailAddress = ""
        }
        if let message = myModel["emailOptIn"] as? Bool {
            self.EmailOptIn = message
        }
        else {
            self.EmailOptIn = false
        }
        if let message = myModel["phone"] as? String {
            self.PhoneNumber = message
        }
        else {
            self.PhoneNumber = ""
        }
        if let message = myModel["smsOptIn"] as? Bool {
            self.SMSOptIn = message
        }
        else {
            self.SMSOptIn = false
        }
        if let message = myModel["addressStreet"] as? String {
            self.Address = message
        }
        else {
            self.Address = ""
        }
        if let message = myModel["addressState"] as? String {
            self.State = message
        } else {
            self.State = ""
        }
        if let message = myModel["addressCity"] as? String {
            self.City = message
        } else {
            self.City = ""
        }
        if let message = myModel["addressZipCode"] as? String {
            self.ZipCode = message
        } else {
            self.ZipCode = ""
        }
        if let message = myModel["addressCountry"] as? String {
            self.Country = message
        } else {
            self.Country = ""
        }
        if let message = myModel["storeCode"] as? String {
            self.StoreCode = message
        } else {
            self.StoreCode = ""
        }
        if let message = myModel["gender"] as? String {
            self.Gender = message
        } else {
            self.Gender = ""
        }
        if let message = myModel["storeCode"] as? String {
            self.StoreCode = message
        } else {
            self.StoreCode = ""
        }
        if let message = myModel["favoriteStore"] as? String {
            self.FavoriteStore = message
        } else {
            self.FavoriteStore = ""
        }
        if let message = myModel["custom_1"] as? String {
            self.Custom1 = message
        } else {
            self.Custom1 = ""
        }
        if let message = myModel["custom_2"] as? String {
            self.Custom2 = message
        } else {
            self.Custom2 = ""
        }
        if let message = myModel["custom_3"] as? String {
            self.Custom3 = message
        } else {
            self.Custom3 = ""
        }
        if let message = myModel["custom_4"] as? String {
            self.Custom4 = message
        } else {
            self.Custom4 = ""
        }
        if let message = myModel["custom_5"] as? String {
            self.Custom5 = message
        } else {
            self.Custom5 = ""
        }
        if let message = myModel["custom_6"] as? String {
            self.Custom6 = message
        } else {
            self.Custom6 = ""
        }
        if let message = myModel["custom_7"] as? String {
            self.Custom7 = message
        } else {
            self.Custom7 = ""
        }
        if let message = myModel["custom_8"] as? String {
            self.Custom8 = message
        } else {
            self.Custom8 = ""
        }
        if let message = myModel["custom_9"] as? String {
            self.Custom9 = message
        } else {
            self.Custom9 = ""
        }
        if let message = myModel["custom_10"] as? String {
            self.Custom10 = message
        } else {
            self.Custom10 = ""
        }
        if let message = myModel["custom_11"] as? String {
            self.Custom11 = message
        } else {
            self.Custom11 = ""
        }
        if let message = myModel["custom_12"] as? String {
            self.Custom12 = message
        } else {
            self.Custom12 = ""
        }
        if let message = myModel["custom_13"] as? String {
            self.Custom13 = message
        } else {
            self.Custom13 = ""
        }
        if let message = myModel["custom_14"] as? String {
            self.Custom14 = message
        } else {
            self.Custom14 = ""
        }
        if let message = myModel["custom_15"] as? String {
            self.Custom15 = message
        } else {
            self.Custom15 = ""
        }
        if let message = myModel["custom_16"] as? String {
            self.Custom6 = message
        } else {
            self.Custom16 = ""
        }
        if let message = myModel["custom_17"] as? String {
            self.Custom17 = message
        } else {
            self.Custom17 = ""
        }
        if let message = myModel["custom_18"] as? String {
            self.Custom18 = message
        } else {
            self.Custom18 = ""
        }
        if let message = myModel["custom_19"] as? String {
            self.Custom19 = message
        } else {
            self.Custom19 = ""
        }
        if let message = myModel["custom_20"] as? String {
            self.Custom20 = message
        } else {
            self.Custom20 = ""
        }
        if let message = myModel["custom_21"] as? String {
            self.Custom21 = message
        } else {
            self.Custom21 = ""
        }
        if let message = myModel["custom_22"] as? String {
            self.Custom22 = message
        } else {
            self.Custom22 = ""
        }
        if let message = myModel["custom23"] as? String {
            self.Custom23 = message
        } else {
            self.Custom23 = ""
        }
        if let message = myModel["custom_24"] as? String {
            self.Custom24 = message
        } else {
            self.Custom24 = ""
        }
        if let message = myModel["custom_25"] as? String {
            self.Custom25 = message
        } else {
            self.Custom25 = ""
        }
        if let message = myModel["custom_26"] as? String {
            self.Custom26 = message
        } else {
            self.Custom26 = ""
        }
        if let message = myModel["custom_27"] as? String {
            self.Custom27 = message
        } else {
            self.Custom27 = ""
        }
        if let message = myModel["custom_28"] as? String {
            self.Custom28 = message
        } else {
            self.Custom28 = ""
        }
        if let message = myModel["custom_29"] as? String {
            self.Custom29 = message
        } else {
            self.Custom29 = ""
        }
        if let message = myModel["custom_30"] as? String {
            self.Custom30 = message
        } else {
            self.Custom30 = ""
        }
        if let message = myModel["custom_31"] as? String {
            self.Custom31 = message
        } else {
            self.Custom31 = ""
        }
        if let message = myModel["custom_32"] as? String {
            self.Custom32 = message
        } else {
            self.Custom32 = ""
        }
        if let message = myModel["custom_33"] as? String {
            self.Custom33 = message
        } else {
            self.Custom33 = ""
        }
        if let message = myModel["custom_34"] as? String {
            self.Custom34 = message
        } else {
            self.Custom34 = ""
        }
        if let message = myModel["custom_35"] as? String {
            self.Custom35 = message
        } else {
            self.Custom35 = ""
        }
        if let message = myModel["custom_36"] as? String {
            self.Custom36 = message
        } else {
            self.Custom36 = ""
        }
        if let message = myModel["custom_37"] as? String {
            self.Custom37 = message
        } else {
            self.Custom37 = ""
        }
        if let message = myModel["custom_38"] as? String {
            self.Custom38 = message
        } else {
            self.Custom38 = ""
        }
        if let message = myModel["custom_39"] as? String {
            self.Custom39 = message
        } else {
            self.Custom39 = ""
        }
        if let message = myModel["custom_40"] as? String {
            self.Custom40 = message
        } else {
            self.Custom40 = ""
        }
        if let message = myModel["custom_41"] as? String {
            self.Custom41 = message
        } else {
            self.Custom41 = ""
        }
        if let message = myModel["custom_42"] as? String {
            self.Custom42 = message
        } else {
            self.Custom42 = ""
        }
        if let message = myModel["custom_43"] as? String {
            self.Custom43 = message
        } else {
            self.Custom43 = ""
        }
        if let message = myModel["custom_44"] as? String {
            self.Custom44 = message
        } else {
            self.Custom44 = ""
        }
        if let message = myModel["custom_45"] as? String {
            self.Custom45 = message
        } else {
            self.Custom45 = ""
        }
        if let message = myModel["custom_46"] as? String {
            self.Custom46 = message
        } else {
            self.Custom46 = ""
        }
        if let message = myModel["custom_47"] as? String {
            self.Custom47 = message
        } else {
            self.Custom47 = ""
        }
        if let message = myModel["custom_48"] as? String {
            self.Custom48 = message
        } else {
            self.Custom48 = ""
        }
        if let message = myModel["custom_49"] as? String {
            self.Custom49 = message
        } else {
            self.Custom49 = ""
        }
        if let message = myModel["custom_50"] as? String {
            self.Custom50 = message
        } else {
            self.Custom50 = ""
        }
        if let message = myModel["custom_51"] as? String {
            self.Custom51 = message
        } else {
            self.Custom51 = ""
        }
        if let message = myModel["custom_52"] as? String {
            self.Custom52 = message
        } else {
            self.Custom52 = ""
        }
        if let message = myModel["custom_53"] as? String {
            self.Custom53 = message
        } else {
            self.Custom53 = ""
        }
        if let message = myModel["custom_54"] as? String {
            self.Custom54 = message
        } else {
            self.Custom54 = ""
        }
        if let message = myModel["custom_55"] as? String {
            self.Custom55 = message
        } else {
            self.Custom55 = ""
        }
        if let message = myModel["custom_56"] as? String {
            self.Custom56 = message
        } else {
            self.Custom56 = ""
        }
        if let message = myModel["custom_57"] as? String {
            self.Custom57 = message
        } else {
            self.Custom57 = ""
        }
        if let message = myModel["custom_58"] as? String {
            self.Custom58 = message
        } else {
            self.Custom58 = ""
        }
        if let message = myModel["custom_59"] as? String {
            self.Custom59 = message
        } else {
            self.Custom59 = ""
        }
        if let message = myModel["custom_60"] as? String {
            self.Custom60 = message
        } else {
            self.Custom60 = ""
        }
        if let message = myModel["custom_61"] as? String {
            self.Custom61 = message
        } else {
            self.Custom61 = ""
        }
        if let message = myModel["custom_62"] as? String {
            self.Custom62 = message
        } else {
            self.Custom62 = ""
        }
        if let message = myModel["custom_63"] as? String {
            self.Custom63 = message
        } else {
            self.Custom63 = ""
        }
        if let message = myModel["custom_64"] as? String {
            self.Custom64 = message
        } else {
            self.Custom64 = ""
        }
        if let message = myModel["custom_65"] as? String {
            self.Custom65 = message
        } else {
            self.Custom65 = ""
        }
        if let message = myModel["custom_66"] as? String {
            self.Custom66 = message
        } else {
            self.Custom66 = ""
        }
        if let message = myModel["custom_67"] as? String {
            self.Custom67 = message
        } else {
            self.Custom67 = ""
        }
        if let message = myModel["custom_68"] as? String {
            self.Custom68 = message
        } else {
            self.Custom68 = ""
        }
        if let message = myModel["custom_69"] as? String {
            self.Custom69 = message
        } else {
            self.Custom69 = ""
        }
        if let message = myModel["custom_70"] as? String {
            self.Custom70 = message
        } else {
            self.Custom70 = ""
        }
        if let message = myModel["custom_71"] as? String {
            self.Custom71 = message
        } else {
            self.Custom71 = ""
        }
        if let message = myModel["custom_72"] as? String {
            self.Custom72 = message
        } else {
            self.Custom72 = ""
        }
        if let message = myModel["custom_73"] as? String {
            self.Custom73 = message
        } else {
            self.Custom73 = ""
        }
        if let message = myModel["custom_74"] as? String {
            self.Custom74 = message
        } else {
            self.Custom74 = ""
        }
        if let message = myModel["custom_75"] as? String {
            self.Custom75 = message
        } else {
            self.Custom75 = ""
        }
        if let message = myModel["custom_76"] as? String {
            self.Custom76 = message
        } else {
            self.Custom76 = ""
        }
        if let message = myModel["custom_77"] as? String {
            self.Custom77 = message
        } else {
            self.Custom77 = ""
        }
        if let message = myModel["custom_78"] as? String {
            self.Custom78 = message
        } else {
            self.Custom78 = ""
        }
        if let message = myModel["custom_79"] as? String {
            self.Custom79 = message
        } else {
            self.Custom79 = ""
        }
        if let message = myModel["custom_80"] as? String {
            self.Custom80 = message
        } else {
            self.Custom80 = ""
        }
        if let message = myModel["custom_81"] as? String {
            self.Custom81 = message
        } else {
            self.Custom81 = ""
        }
        if let message = myModel["custom_82"] as? String {
            self.Custom82 = message
        } else {
            self.Custom82 = ""
        }
        if let message = myModel["custom_83"] as? String {
            self.Custom83 = message
        } else {
            self.Custom83 = ""
        }
        if let message = myModel["custom_84"] as? String {
            self.Custom84 = message
        } else {
            self.Custom84 = ""
        }
        if let message = myModel["custom_85"] as? String {
            self.Custom85 = message
        } else {
            self.Custom85 = ""
        }
        if let message = myModel["custom_86"] as? String {
            self.Custom86 = message
        } else {
            self.Custom86 = ""
        }
        if let message = myModel["custom_87"] as? String {
            self.Custom87 = message
        } else {
            self.Custom87 = ""
        }
        if let message = myModel["custom_88"] as? String {
            self.Custom88 = message
        } else {
            self.Custom88 = ""
        }
        if let message = myModel["custom_89"] as? String {
            self.Custom89 = message
        } else {
            self.Custom89 = ""
        }
        if let message = myModel["custom_90"] as? String {
            self.Custom90 = message
        } else {
            self.Custom90 = ""
        }
        if let message = myModel["custom_91"] as? String {
            self.Custom91 = message
        } else {
            self.Custom91 = ""
        }
        if let message = myModel["custom_92"] as? String {
            self.Custom92 = message
        } else {
            self.Custom92 = ""
        }
        if let message = myModel["custom_93"] as? String {
            self.Custom93 = message
        } else {
            self.Custom93 = ""
        }
        if let message = myModel["custom_94"] as? String {
            self.Custom94 = message
        } else {
            self.Custom94 = ""
        }
        if let message = myModel["custom_95"] as? String {
            self.Custom95 = message
        } else {
            self.Custom95 = ""
        }
        if let message = myModel["custom_96"] as? String {
            self.Custom96 = message
        } else {
            self.Custom96 = ""
        }
        if let message = myModel["custom_97"] as? String {
            self.Custom97 = message
        } else {
            self.Custom97 = ""
        }
        if let message = myModel["custom_98"] as? String {
            self.Custom98 = message
        } else {
            self.Custom98 = ""
        }
        if let message = myModel["custom_99"] as? String {
            self.Custom99 = message
        } else {
            self.Custom99 = ""
        }
        if let message = myModel["custom_100"] as? String {
            self.Custom100 = message
        } else {
            self.Custom100 = ""
        }
        
    }
    
    
    
    public init()
    {
        
    }
    
    
    
}

public struct EventRespnoseBO : ResponseBO {

    public lazy var  successFlag : Bool = false
    public lazy var  message : String = ""


}

public struct FBEventRespnoseBO : ResponseBO {
    
    public var arrResponse : NSMutableArray = []
    
    public init?(json:Any) {
        guard let myModel = json as? NSArray else {
            return nil
        }
        
        
        var arrResponseType : NSArray  = []
        
        print("jsondata inside Model is \(json)")
        
        arrResponseType = myModel
        
        print("arrOffer is \(arrResponseType)")
        var tempArrResponseType = [[String:AnyObject]]()
        tempArrResponseType = arrResponseType as! [[String:AnyObject]]
        print("arrOffer is \(tempArrResponseType)")
        
        
        for  i in (0..<arrResponseType.count) {
            
            var objEvent =  EventRespnoseBO()
            
            let dicOffer : Dictionary = tempArrResponseType[i] as Dictionary<String,AnyObject>
            print("dicOffer is \(dicOffer)")
            
            if let message = dicOffer["successFlag"] as? Bool {
                objEvent.successFlag = message
            } else {
                objEvent.successFlag = false
            }
            
            if let message = dicOffer["message"] as? String {
                objEvent.message = message
            } else {
                objEvent.message = ""
            }
            print("objOffers inside Model is \(objEvent)")
            
            arrResponse.add(objEvent)
            
            
        }
        
    }

}

public struct digitalEventListBO : ResponseBO
{
    
    public lazy var  eventId : CLong = 0
    public lazy var  eventTypeName : String = ""

}

public struct MobileSettingsListBO : ResponseBO
{
    
    public lazy var  tenantId : CLong = 0
    public lazy var  settingName : String = ""
    public lazy var  settingValue : String = ""
    public lazy var  status : Bool = false

    
}

public struct FBMobileSettingsBO : ResponseBO {

    public lazy var  successFlag : Bool = false
    public lazy var  message : String = ""
    public lazy var  arrDigitalEventList : NSMutableArray = []
    public lazy var  arrMobileSettingsList : NSMutableArray = []

    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        var arrDigitalEvent : NSArray  = []
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        
        if let dict = myModel["digitalEventList"] as? Dictionary<String,AnyObject> {
        if let message = dict["digitalEventList"] as? NSArray {
            print("arrState inside Model is \(message)")
            
            arrDigitalEvent = message
            print("arrState is \(arrDigitalEvent)")
            var arrTempDigitalEvent = [[String:AnyObject]]()
            arrTempDigitalEvent = arrDigitalEvent as! [[String:AnyObject]]
            print("arrState is \(arrTempDigitalEvent)")
            
            for  i in (0..<arrDigitalEvent.count) {
                
                var objEvent =  digitalEventListBO()
                
                let dicOffer : Dictionary = arrTempDigitalEvent[i] as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["eventID"] as? CLong {
                    objEvent.eventId = message
                } else {
                    objEvent.eventId = 0
                }
                if let message = dicOffer["eventTypeName"] as? String {
                    objEvent.eventTypeName = message
                } else {
                    objEvent.eventTypeName = ""
                }
                
                arrDigitalEventList.add(objEvent)
                
            }
        }
    }
        
        var arrMobileSettings : NSArray  = []

        
        if let dict = myModel["mobSettingList"] as? Dictionary<String,AnyObject>{
            if let message = dict["mobileSettings"] as? NSArray {
                print("arrState inside Model is \(message)")
                
                arrMobileSettings = message
                print("arrState is \(arrMobileSettings)")
                var arrTempMobileSettings = [[String:AnyObject]]()
                arrTempMobileSettings = arrMobileSettings as! [[String:AnyObject]]
                print("arrState is \(arrTempMobileSettings)")
                
                for  i in (0..<arrMobileSettings.count) {
                    
                    var objMobileSettings =  MobileSettingsListBO()
                    
                    let dicOffer : Dictionary = arrTempMobileSettings[i] as Dictionary<String,AnyObject>
                    print("dicOffer is \(dicOffer)")
                    
                    if let message = dicOffer["tenantId"] as? CLong {
                        objMobileSettings.tenantId = message
                    } else {
                        objMobileSettings.tenantId = 0
                    }
                    if let message = dicOffer["settingName"] as? String {
                        objMobileSettings.settingName = message
                    } else {
                        objMobileSettings.settingName = ""
                    }
                    if let message = dicOffer["settingValue"] as? String {
                        objMobileSettings.settingValue = message
                    }
                    else {
                        objMobileSettings.settingValue = ""
                    }
                    if let message = dicOffer["status"] as? Bool {
                        objMobileSettings.status = message
                    } else {
                        objMobileSettings.status = false
                    }
                    
                    arrMobileSettingsList.add(objMobileSettings)
                    
                }
            }
        }

        
    }
    
}

public struct ThemeSettingsBO : ResponseBO {
    
    public lazy var  name : String = ""
    public lazy var  displayName : String = ""
    public lazy var  confirmationText : String = ""
    public lazy var  displaySeq : CLong = 0
    public lazy var  required : Bool = false
    public lazy var  maximumLength : CLong = 0
    public lazy var  editable : Bool = false
    public lazy var  databaseName : String = ""
    public lazy var  customField : Bool = false
    public lazy var  displayType : CLong = 0
    public lazy var  profileFieldType : String = ""
    public lazy var  dataType : String = ""
    public lazy var  optionList : NSArray = []
    public lazy var  defaultValue : String = ""


}

public struct ThemeCreativeSettingsBO : ResponseBO {
    
    public lazy var  settingId : CLong = 0
    public lazy var  configGroup : String = ""
    public lazy var  configName : String = ""
    public lazy var  configValue : String = ""
    public lazy var  configDisplayLabel : String = ""
    public lazy var  description : String = ""
    public lazy var  active : Bool = false
    public lazy var  mandatory : Bool = false
    public lazy var  configDisplaySeq : CLong = 0
    
}


public struct FBThemeSettingsBO : ResponseBO {
    
    public lazy var  successFlag : Bool = false
    public lazy var  message : String = ""
    public lazy var  arrRegistrationFields : NSMutableArray = []
    public lazy var  arrThemeCreativeSettings : NSMutableArray = []
    public lazy var  arrGeneralSettings : NSMutableArray = []
    public lazy var  arrLoginSettings : NSMutableArray = []
    public lazy var  arrRegistrationSettings : NSMutableArray = []
    public lazy var themeName : String = ""
    public lazy var themeId : CLong = 0
    public lazy var themeDescription : String = ""


    
    
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        var arrRegistration : NSArray  = []
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.message = message
        } else {
            self.message = ""
        }
        
        if let dictBody = myModel["themeDetails"] as? Dictionary<String,AnyObject>{

        
        if let message = dictBody["id"] as? CLong {
            self.themeId = message
        } else {
            self.themeId = 0
        }
        if let message = dictBody["themeName"] as? String {
            self.themeName = message
        } else {
            self.themeName = ""
        }
        if let message = dictBody["themeDescription"] as? String {
            self.themeDescription = message
        } else {
            self.themeDescription = ""
        }
        
        
        if let arrProfileField = dictBody["profilefield"] as? NSArray{
            
        for  i in (0..<arrProfileField.count) {
            
            let dict : Dictionary = arrProfileField[i] as! Dictionary<String,AnyObject>

            let strname : String = dict["pageName"] as! String

            if case strname = "Registration"
            {
            if let message = dict["fields"] as? NSArray {

                print("arrState inside Model is \(message)")
                
                arrRegistration = message
                print("arrState is \(arrRegistration)")
                var arrTempRegistration = [[String:AnyObject]]()
                arrTempRegistration = arrRegistration as! [[String:AnyObject]]
                print("arrState is \(arrTempRegistration)")
                
                for  i in (0..<arrRegistration.count) {
                    
                    var objTheme =  ThemeSettingsBO()
                    
                    let dicOffer : Dictionary = arrTempRegistration[i] as Dictionary<String,AnyObject>
                    print("dicOffer is \(dicOffer)")
                    
                    if let message = dicOffer["name"] as? String {
                        objTheme.name = message
                    } else {
                        objTheme.name = ""
                    }
                    if let message = dicOffer["displayName"] as? String {
                        objTheme.displayName = message
                    } else {
                        objTheme.displayName = ""
                    }
                    if let message = dicOffer["confirmationText"] as? String {
                        objTheme.confirmationText = message
                    } else {
                        objTheme.confirmationText = ""
                    }
                    if let message = dicOffer["displaySeq"] as? CLong {
                        objTheme.displaySeq = message
                    } else {
                        objTheme.displaySeq = 0
                    }
                    if let message = dicOffer["required"] as? Bool {
                        objTheme.required = message
                    } else {
                        objTheme.required = false
                    }
                    if let message = dicOffer["maximumLength"] as? CLong {
                        objTheme.maximumLength = message
                    } else {
                        objTheme.maximumLength = 0
                    }
                    if let message = dicOffer["editable"] as? Bool {
                        objTheme.editable = message
                    } else {
                        objTheme.editable = false
                    }
                    if let message = dicOffer["databaseName"] as? String {
                        objTheme.databaseName = message
                    } else {
                        objTheme.databaseName = ""
                    }
                    if let message = dicOffer["customField"] as? Bool {
                        objTheme.customField = message
                    } else {
                        objTheme.customField = false
                    }
                    if let message = dicOffer["displayType"] as? CLong {
                        objTheme.displayType = message
                    } else {
                        objTheme.displayType = 0
                    }
                    if let message = dicOffer["profileFieldType"] as? String {
                        objTheme.profileFieldType = message
                    } else {
                        objTheme.profileFieldType = ""
                    }
                    if let message = dicOffer["dataType"] as? String {
                        objTheme.dataType = message
                    } else {
                        objTheme.dataType = ""
                    }
                    if let message = dicOffer["optionList"] as? NSArray {
                        objTheme.optionList = message
                    } else {
                        objTheme.optionList = []
                    }
                    if let message = dicOffer["defaultValue"] as? String {
                        objTheme.defaultValue = message
                    } else {
                        objTheme.defaultValue = ""
                    }
                    
                    arrRegistrationFields.add(objTheme)
                 
                }
                }
                }
        
            }
        }
        
        var arrGeneralSetting : NSArray  = []

        
        if let arrProfileField = dictBody["themeConfigSettings"] as? NSArray{
            
            for  i in (0..<arrProfileField.count) {
                
                let dict : Dictionary = arrProfileField[i] as! Dictionary<String,AnyObject>
                
                let strname : String = dict["pageName"] as! String
                
                if case strname = "General"
                {
                    if let message = dict["themeCreativeSettings"] as? NSArray {
                        
                        print("arrState inside Model is \(message)")
                        
                        arrGeneralSetting = message
                        print("arrGeneralSetting is \(arrGeneralSetting)")
                        var arrTempGeneralSetting = [[String:AnyObject]]()
                        arrTempGeneralSetting = arrGeneralSetting as! [[String:AnyObject]]
                        print("arrGeneralSetting is \(arrTempGeneralSetting)")
                        
                        for  i in (0..<arrGeneralSetting.count) {
                            
                            var objCreativeTheme =  ThemeCreativeSettingsBO()
                            
                            let dicOffer : Dictionary = arrTempGeneralSetting[i] as Dictionary<String,AnyObject>
                            print("dicOffer is \(dicOffer)")
                            
                            if let message = dicOffer["id"] as? CLong {
                                objCreativeTheme.settingId = message
                            } else {
                                objCreativeTheme.settingId = 0
                            }
                            if let message = dicOffer["configGroup"] as? String {
                                objCreativeTheme.configGroup = message
                            } else {
                                objCreativeTheme.configGroup = ""
                            }
                            if let message = dicOffer["configName"] as? String {
                                objCreativeTheme.configName = message
                            } else {
                                objCreativeTheme.configName = ""
                            }
                            if let message = dicOffer["configValue"] as? String {
                                objCreativeTheme.configValue = message
                            } else {
                                objCreativeTheme.configValue = ""
                            }
                            if let message = dicOffer["configDisplayLabel"] as? String {
                                objCreativeTheme.configDisplayLabel = message
                            } else {
                                objCreativeTheme.configDisplayLabel = ""
                            }
                            if let message = dicOffer["description"] as? String {
                                objCreativeTheme.description = message
                            } else {
                                objCreativeTheme.description = ""
                            }
                            if let message = dicOffer["active"] as? Bool {
                                objCreativeTheme.active = message
                            } else {
                                objCreativeTheme.active = false
                            }
                            if let message = dicOffer["mandatory"] as? Bool {
                                objCreativeTheme.mandatory = message
                            } else {
                                objCreativeTheme.mandatory = false
                            }
                            if let message = dicOffer["configDisplaySeq"] as? CLong {
                                objCreativeTheme.configDisplaySeq = message
                            } else {
                                objCreativeTheme.configDisplaySeq = 0
                            }
                            
                            arrGeneralSettings.add(objCreativeTheme)
                            
                        }
                    }
                }
                
                
                arrGeneralSetting = []
                
                if case strname = "Login"
                {
                    if let message = dict["themeCreativeSettings"] as? NSArray {
                        
                        print("arrState inside Model is \(message)")
                        
                        arrGeneralSetting = message
                        print("arrGeneralSetting is \(arrGeneralSetting)")
                        var arrTempGeneralSetting = [[String:AnyObject]]()
                        arrTempGeneralSetting = arrGeneralSetting as! [[String:AnyObject]]
                        print("arrGeneralSetting is \(arrTempGeneralSetting)")
                        
                        for  i in (0..<arrGeneralSetting.count) {
                            
                            var objCreativeTheme =  ThemeCreativeSettingsBO()
                            
                            let dicOffer : Dictionary = arrTempGeneralSetting[i] as Dictionary<String,AnyObject>
                            print("dicOffer is \(dicOffer)")
                            
                            if let message = dicOffer["id"] as? CLong {
                                objCreativeTheme.settingId = message
                            } else {
                                objCreativeTheme.settingId = 0
                            }
                            if let message = dicOffer["configGroup"] as? String {
                                objCreativeTheme.configGroup = message
                            } else {
                                objCreativeTheme.configGroup = ""
                            }
                            if let message = dicOffer["configName"] as? String {
                                objCreativeTheme.configName = message
                            } else {
                                objCreativeTheme.configName = ""
                            }
                            if let message = dicOffer["configValue"] as? String {
                                objCreativeTheme.configValue = message
                            } else {
                                objCreativeTheme.configValue = ""
                            }
                            if let message = dicOffer["configDisplayLabel"] as? String {
                                objCreativeTheme.configDisplayLabel = message
                            } else {
                                objCreativeTheme.configDisplayLabel = ""
                            }
                            if let message = dicOffer["description"] as? String {
                                objCreativeTheme.description = message
                            } else {
                                objCreativeTheme.description = ""
                            }
                            if let message = dicOffer["active"] as? Bool {
                                objCreativeTheme.active = message
                            } else {
                                objCreativeTheme.active = false
                            }
                            if let message = dicOffer["mandatory"] as? Bool {
                                objCreativeTheme.mandatory = message
                            } else {
                                objCreativeTheme.mandatory = false
                            }
                            if let message = dicOffer["configDisplaySeq"] as? CLong {
                                objCreativeTheme.configDisplaySeq = message
                            } else {
                                objCreativeTheme.configDisplaySeq = 0
                            }
                            
                            arrLoginSettings.add(objCreativeTheme)
                            
                        }
                    }
                }
                
                arrGeneralSetting = []
                
                if case strname = "Registration"
                {
                    if let message = dict["themeCreativeSettings"] as? NSArray {
                        
                        print("arrState inside Model is \(message)")
                        
                        arrGeneralSetting = message
                        print("arrGeneralSetting is \(arrGeneralSetting)")
                        var arrTempGeneralSetting = [[String:AnyObject]]()
                        arrTempGeneralSetting = arrGeneralSetting as! [[String:AnyObject]]
                        print("arrGeneralSetting is \(arrTempGeneralSetting)")
                        
                        for  i in (0..<arrGeneralSetting.count) {
                            
                            var objCreativeTheme =  ThemeCreativeSettingsBO()
                            
                            let dicOffer : Dictionary = arrTempGeneralSetting[i] as Dictionary<String,AnyObject>
                            print("dicOffer is \(dicOffer)")
                            
                            if let message = dicOffer["id"] as? CLong {
                                objCreativeTheme.settingId = message
                            } else {
                                objCreativeTheme.settingId = 0
                            }
                            if let message = dicOffer["configGroup"] as? String {
                                objCreativeTheme.configGroup = message
                            } else {
                                objCreativeTheme.configGroup = ""
                            }
                            if let message = dicOffer["configName"] as? String {
                                objCreativeTheme.configName = message
                            } else {
                                objCreativeTheme.configName = ""
                            }
                            if let message = dicOffer["configValue"] as? String {
                                objCreativeTheme.configValue = message
                            } else {
                                objCreativeTheme.configValue = ""
                            }
                            if let message = dicOffer["configDisplayLabel"] as? String {
                                objCreativeTheme.configDisplayLabel = message
                            } else {
                                objCreativeTheme.configDisplayLabel = ""
                            }
                            if let message = dicOffer["description"] as? String {
                                objCreativeTheme.description = message
                            } else {
                                objCreativeTheme.description = ""
                            }
                            if let message = dicOffer["active"] as? Bool {
                                objCreativeTheme.active = message
                            } else {
                                objCreativeTheme.active = false
                            }
                            if let message = dicOffer["mandatory"] as? Bool {
                                objCreativeTheme.mandatory = message
                            } else {
                                objCreativeTheme.mandatory = false
                            }
                            if let message = dicOffer["configDisplaySeq"] as? CLong {
                                objCreativeTheme.configDisplaySeq = message
                            } else {
                                objCreativeTheme.configDisplaySeq = 0
                            }
                            
                            arrRegistrationSettings.add(objCreativeTheme)
                            
                        }
                    }
                }

                
            }
        }
            
        }

        
    }
}

public struct FBGiftCardRespnoseBO : ResponseBO {

    public lazy var  successFlag : Bool = false
    public lazy var  message : String = ""
    public lazy var  inCommToken : String = ""
    public lazy var  response : String = ""
    public lazy var  type : String = ""
    public lazy var  inCommOrderId : String = ""


    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        print("jsondata inside Model is \(json)")
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let strMessage = myModel["message"] as? String {
            self.message = strMessage
        } else {
            self.message = ""
        }
        
        if let strMessage = myModel["type"] as? String {
            self.type = strMessage
        } else {
            self.type = ""
        }
        
        if self.successFlag == true
        {
            
            if let inCommTokenValue = myModel["inCommToken"] as? String {
                self.inCommToken = inCommTokenValue
            } else {
                self.inCommToken = ""
            }
            
            if let inCommTokenValue = myModel["inCommOrderId"] as? String {
                self.inCommOrderId = inCommTokenValue
            } else {
                self.inCommOrderId = ""
            }
            
            if let responseValue = myModel["response"] as? String {
                self.response = responseValue
            } else {
                self.response = ""
            }
        }
        
        
    }
    
}

public struct registrationFieldsBO : ResponseBO
{
    public lazy var  field : String = ""
    public lazy var  mandatory : Bool = false
    public lazy var  visible : Bool = false

}

public struct themeFontUrlBO : ResponseBO
{
    public lazy var  fontUrl : String = ""
    public lazy var  fontName : String = ""
    public lazy var  fontId : CLong = 0
    public lazy var  fontDescription : String = ""

}


public struct UnSubscribeFieldsBO : ResponseBO
{
    public lazy var  title : String = ""
    public lazy var  titleDescription : String = ""
    public lazy var  question : String = ""
    public lazy var  optionYesText : String = ""
    public lazy var  optionNoText : String = ""
    public lazy var  disclaimer : String = ""
    
}
//180042452255
public struct FBLoyaltySettingsBO : ResponseBO {
    
    public lazy var  successFlag : Bool = false
    public lazy var  message : String = ""
    public lazy var  companyLogoImageUrl : String = ""
    public lazy var  companyName : String = ""
    public lazy var  favoriteIconUrl : String = ""
    public lazy var  defaultStoreId : CLong = 0
    public lazy var  loginHeaderImageUrl : String = ""
    public lazy var  loginHeaderHtmlUrl : String = ""
    public lazy var  loginLeftSideHtmlUrl : String = ""
    public lazy var  loginRightTopImageUrl : String = ""
    public lazy var  loginRightTopHtmlUrl : String = ""
    public lazy var  loginFooterImageUrl : String = ""
    public lazy var  loginFooterHtmlUrl : String = ""
    public lazy var  loginBackgroundImageUrl : String = ""
    public lazy var  loginBackgroundHtmlUrl : String = ""
    public lazy var  signUpBackgroundImageUrl : String = ""
    public lazy var  signUpBackgroundHtmlUrl : String = ""
    public lazy var  checkInButtonColor : String = ""
    public lazy var  minRewardPoints : CLong = 0
    public lazy var  maxRewardPoints : CLong = 0
    public lazy var  arrRegistrationFields : NSMutableArray = []
    public lazy var  privacyContentUrl : String = ""
    public lazy var  termsAndConditionsUrl : String = ""
    public lazy var  themeFontName : String = ""
    public lazy var  arrThemeFontUrl : NSMutableArray = []
    public lazy var  themeFontSize : String = ""
    public lazy var  themeFontColor : String = ""
    public lazy var  unSubscribeBackgroundImageUrl : String = ""
    public lazy var  loginCreativeImageUrl : String = ""
    public lazy var  signUpCreativeImageUrl : String = ""
    public lazy var  splashScreenUrl : String = ""
    public lazy var  passwordEnable : Bool = false
    public lazy var  faq : String = ""
    public lazy var  objUnsubscribe : UnSubscribeFieldsBO = UnSubscribeFieldsBO()
    
    
    public init?(json:Any) {
            guard let myModel = json as? NSDictionary else {
                return nil
            }
            
            print("jsondata inside Model is \(json)")
            
            if let isSuccessFlag = myModel["successFlag"] as? Bool {
                self.successFlag = isSuccessFlag
            } else {
                self.successFlag = false
            }
            
            if let strMessage = myModel["message"] as? String {
                self.message = strMessage
            } else {
                self.message = ""
            }
            
            if self.successFlag == true
            {
                
                if let accessTokenValue = myModel["companyLogoImageUrl"] as? String {
                    self.companyLogoImageUrl = accessTokenValue
                } else {
                    self.companyLogoImageUrl = ""
                }
            }
            
            if let accessTokenValue = myModel["companyName"] as? String {
                self.companyName = accessTokenValue
            } else {
                self.companyName = ""
            }
            if let accessTokenValue = myModel["favoriteIconUrl"] as? String {
                self.favoriteIconUrl = accessTokenValue
            } else {
                self.favoriteIconUrl = ""
            }
            if let accessTokenValue = myModel["defaultStoreId"] as? CLong {
                self.defaultStoreId = accessTokenValue
            } else {
                self.defaultStoreId = 0
            }
            if let accessTokenValue = myModel["loginHeaderImageUrl"] as? String {
                self.loginHeaderImageUrl = accessTokenValue
            } else {
                self.loginHeaderImageUrl = ""
            }
            if let accessTokenValue = myModel["loginHeaderHtmlUrl"] as? String {
                self.loginHeaderHtmlUrl = accessTokenValue
            } else {
                self.loginHeaderHtmlUrl = ""
            }
            if let accessTokenValue = myModel["loginLeftSideHtmlUrl"] as? String {
                self.loginLeftSideHtmlUrl = accessTokenValue
            } else {
                self.loginLeftSideHtmlUrl = ""
            }
            if let accessTokenValue = myModel["loginRightTopImageUrl"] as? String {
                self.loginRightTopImageUrl = accessTokenValue
            } else {
                self.loginRightTopImageUrl = ""
            }
            if let accessTokenValue = myModel["loginRightTopHtmlUrl"] as? String {
                self.loginRightTopHtmlUrl = accessTokenValue
            } else {
                self.loginRightTopHtmlUrl = ""
            }
            if let accessTokenValue = myModel["loginFooterImageUrl"] as? String {
                self.loginFooterImageUrl = accessTokenValue
            } else {
                self.loginFooterImageUrl = ""
            }
            if let accessTokenValue = myModel["loginFooterHtmlUrl"] as? String {
                self.loginFooterHtmlUrl = accessTokenValue
            } else {
                self.loginFooterHtmlUrl = ""
            }
            if let accessTokenValue = myModel["loginBackgroundImageUrl"] as? String {
                self.loginBackgroundImageUrl = accessTokenValue
            } else {
                self.loginBackgroundImageUrl = ""
            }
            if let accessTokenValue = myModel["loginBackgroundHtmlUrl"] as? String {
                self.loginBackgroundHtmlUrl = accessTokenValue
            } else {
                self.loginBackgroundHtmlUrl = ""
            }
            if let accessTokenValue = myModel["signUpBackgroundImageUrl"] as? String {
                self.signUpBackgroundImageUrl = accessTokenValue
            } else {
                self.signUpBackgroundImageUrl = ""
            }
            if let accessTokenValue = myModel["signUpBackgroundHtmlUrl"] as? String {
                self.signUpBackgroundHtmlUrl = accessTokenValue
            } else {
                self.signUpBackgroundHtmlUrl = ""
            }
            if let accessTokenValue = myModel["checkInButtonColor"] as? String {
                self.checkInButtonColor = accessTokenValue
            } else {
                self.checkInButtonColor = ""
            }
            if let accessTokenValue = myModel["minRewardPoints"] as? CLong {
                self.minRewardPoints = accessTokenValue
            } else {
                self.minRewardPoints = 0
            }
            if let accessTokenValue = myModel["maxRewardPoints"] as? CLong {
                self.maxRewardPoints = accessTokenValue
            } else {
                self.maxRewardPoints = 0
            }
            if let accessTokenValue = myModel["registrationFields"] as? NSArray {
    
                var arrRegistrationField : NSArray  = []

                    print("arrRegistrationFields inside Model is \(message)")
                    
                    arrRegistrationField = accessTokenValue
                    print("arrRegistrationFields is \(arrRegistrationField)")
                    var arrTempRegistrationField = [[String:AnyObject]]()
                    arrTempRegistrationField = arrRegistrationField as! [[String:AnyObject]]
                    print("arrState is \(arrTempRegistrationField)")
                    
                    for  i in (0..<arrRegistrationField.count) {
                        
                        var objRegistrationField =  registrationFieldsBO()
                        
                        let dicOffer : Dictionary = arrTempRegistrationField[i] as Dictionary<String,AnyObject>
                        print("dicOffer is \(dicOffer)")
                        
                        if let message = dicOffer["field"] as? String {
                            objRegistrationField.field = message
                        } else {
                            objRegistrationField.field = ""
                        }
                        if let message = dicOffer["mandatory"] as? Bool {
                            objRegistrationField.mandatory = message
                        } else {
                            objRegistrationField.mandatory = false
                        }
                        if let message = dicOffer["visible"] as? Bool {
                            objRegistrationField.visible = message
                        } else {
                            objRegistrationField.visible = false
                        }
                        
                        arrRegistrationFields.add(objRegistrationField)
                        
                    }
                }

            if let accessTokenValue = myModel["privacyContentUrl"] as? String {
                self.privacyContentUrl = accessTokenValue
            } else {
                self.privacyContentUrl = ""
            }
            if let accessTokenValue = myModel["termsAndConditionsUrl"] as? String {
                self.termsAndConditionsUrl = accessTokenValue
            } else {
                self.termsAndConditionsUrl = ""
            }
            if let accessTokenValue = myModel["themeFontName"] as? String {
                self.themeFontName = accessTokenValue
            } else {
                self.themeFontName = ""
            }
        if let accessTokenValue = myModel["themeFontUrl"] as? NSArray {
            
            var arrThemeFont : NSArray  = []
            
            print("arrRegistrationFields inside Model is \(message)")
            
            arrThemeFont = accessTokenValue
            print("arrThemeFont is \(arrThemeFont)")
            var arrTempThemeFont = [[String:AnyObject]]()
            arrTempThemeFont = arrThemeFont as! [[String:AnyObject]]
            print("arrTempThemeFont is \(arrTempThemeFont)")
            
            for  i in (0..<arrThemeFont.count) {
                
                var objthemeFontUrl =  themeFontUrlBO()
                
                let dicOffer : Dictionary = arrTempThemeFont[i] as Dictionary<String,AnyObject>
                print("dicOffer is \(dicOffer)")
                
                if let message = dicOffer["fontUrl"] as? String {
                    objthemeFontUrl.fontUrl = message
                } else {
                    objthemeFontUrl.fontUrl = ""
                }
                if let message = dicOffer["fontName"] as? String {
                    objthemeFontUrl.fontName = message
                } else {
                    objthemeFontUrl.fontName = ""
                }
                if let message = dicOffer["fontId"] as? CLong {
                    objthemeFontUrl.fontId = message
                } else {
                    objthemeFontUrl.fontId = 0
                }
                if let message = dicOffer["fontDescription"] as? String {
                    objthemeFontUrl.fontDescription = message
                } else {
                    objthemeFontUrl.fontDescription = ""
                }
                
                
                arrThemeFontUrl.add(objthemeFontUrl)
                
            }
        }
            if let accessTokenValue = myModel["themeFontSize"] as? String {
                self.themeFontSize = accessTokenValue
            } else {
                self.themeFontSize = ""
            }
            if let accessTokenValue = myModel["themeFontColor"] as? String {
                self.themeFontColor = accessTokenValue
            } else {
                self.themeFontColor = ""
            }
            if let accessTokenValue = myModel["unSubscribeBackgroundImageUrl"] as? String {
                self.unSubscribeBackgroundImageUrl = accessTokenValue
            } else {
                self.unSubscribeBackgroundImageUrl = ""
            }
            if let accessTokenValue = myModel["loginCreativeImageUrl"] as? String {
                self.loginCreativeImageUrl = accessTokenValue
            } else {
                self.loginCreativeImageUrl = ""
            }
            if let accessTokenValue = myModel["signUpCreativeImageUrl"] as? String {
                self.signUpCreativeImageUrl = accessTokenValue
            } else {
                self.signUpCreativeImageUrl = ""
            }
            if let accessTokenValue = myModel["splashScreenUrl"] as? String {
                self.splashScreenUrl = accessTokenValue
            } else {
                self.splashScreenUrl = ""
            }
            if let accessTokenValue = myModel["passwordEnable"] as? Bool {
                self.passwordEnable = accessTokenValue
            } else {
                self.passwordEnable = false
            }
            if let accessTokenValue = myModel["faq"] as? String {
                self.faq = accessTokenValue
            } else {
                self.faq = ""
            }
            if let dictUnSubscribeFields = myModel["unSubscribeFields"] as? Dictionary<String,AnyObject> {
               
                if let accessTokenValue = dictUnSubscribeFields["title"] as? String {
                    self.objUnsubscribe.title = accessTokenValue
                } else {
                    self.objUnsubscribe.title = ""
                }
                if let accessTokenValue = dictUnSubscribeFields["titleDescription"] as? String {
                    self.objUnsubscribe.titleDescription = accessTokenValue
                } else {
                    self.objUnsubscribe.titleDescription = ""
                }
                if let accessTokenValue = dictUnSubscribeFields["question"] as? String {
                    self.objUnsubscribe.question = accessTokenValue
                } else {
                    self.objUnsubscribe.question = ""
                }
                if let accessTokenValue = dictUnSubscribeFields["optionYesText"] as? String {
                    self.objUnsubscribe.optionYesText = accessTokenValue
                } else {
                    self.objUnsubscribe.optionYesText = ""
                }
                if let accessTokenValue = dictUnSubscribeFields["optionNoText"] as? String {
                    self.objUnsubscribe.optionNoText = accessTokenValue
                } else {
                    self.objUnsubscribe.optionNoText = ""
                }
                if let accessTokenValue = dictUnSubscribeFields["disclaimer"] as? String {
                    self.objUnsubscribe.disclaimer = accessTokenValue
                } else {
                    self.objUnsubscribe.disclaimer = ""
                }
                
                
            }
        
    }
            
    }

public struct emailCampaignBO : ResponseBO {

    public lazy var  campaignId : CLong = 0
    public lazy var  offerId : CLong = 0
    public lazy var  channels : String = ""
    public lazy var  campaignText : String = ""
    public lazy var  campaignName : String = ""
    public lazy var  campaignType : String = ""
    public lazy var  templateId : CLong = 0

}

public struct smsCampaignBO : ResponseBO {
    
    public lazy var  campaignId : CLong = 0
    public lazy var  offerId : CLong = 0
    public lazy var  channels : String = ""
    public lazy var  campaignText : String = ""
    public lazy var  campaignName : String = ""
    public lazy var  campaignType : String = ""
    public lazy var  templateId : CLong = 0
    
}

public struct pushCampaignBO : ResponseBO {
    
    public lazy var  campaignId : CLong = 0
    public lazy var  offerId : CLong = 0
    public lazy var  channels : String = ""
    public lazy var  campaignText : String = ""
    public lazy var  campaignName : String = ""
    public lazy var  campaignType : String = ""
    public lazy var  templateId : CLong = 0
    
}
public struct pullCampaignBO : ResponseBO {
    
    public lazy var  campaignId : CLong = 0
    public lazy var  offerId : CLong = 0
    public lazy var  channels : String = ""
    public lazy var  campaignText : String = ""
    public lazy var  campaignName : String = ""
    public lazy var  campaignType : String = ""
    public lazy var  templateId : CLong = 0
    
}


public struct FishbowlPromotionBO : ResponseBO {
    
    public lazy var  fishbowlPromotionId : CLong = 0
    public lazy var  name : String = ""
    public lazy var  description : String = ""
    public lazy var  redemptions : CLong = 0
    public lazy var  status : String = ""
    public lazy var  type : String = ""
    public lazy var  expirationDate : String = ""
    public lazy var  endDate : String = ""
    public lazy var  publicDescription : String = ""
    public lazy var  publicName : String = ""
    public lazy var  offerTypeId : CLong = 0
    public lazy var  channelId : CLong = 0
    public lazy var  storeRestriction : NSArray = []

}

public struct RewardOfferBO : ResponseBO {
    
    public lazy var  successFlag : Bool = false
    public lazy var  message : String = ""
    public lazy var arrLoyaltyPointRedemptionList : NSMutableArray = []
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        print("jsondata inside Model is \(json)")
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let strMessage = myModel["message"] as? String {
            self.message = strMessage
        } else {
            self.message = ""
        }
        
        var arrLoyaltyPointRedemption : NSArray = []
        
        if let message = myModel["loyaltyPointRedemptionList"] as? NSArray {
            print("arrloyaltyPointRedemptionList inside Model is \(message)")
            
            arrLoyaltyPointRedemption = message
            print("arrProductSubCategory is \(arrLoyaltyPointRedemption)")
            var arrTempLoyaltyPointRedemption = [[String:AnyObject]]()
            arrTempLoyaltyPointRedemption = arrLoyaltyPointRedemption as! [[String:AnyObject]]
            print("arrProductSubCategoryList is \(arrTempLoyaltyPointRedemption)")
            
            for  i in (0..<arrLoyaltyPointRedemption.count) {
                
                var objLoyaltyPoints =  FBLoyaltyPointsBO()
                
                let myModel : Dictionary = arrLoyaltyPointRedemption[i] as! Dictionary<String,AnyObject>
                print("dicOffer is \(myModel)")
                
                
                if let isSuccessFlag = myModel["successFlag"] as? Bool {
                    objLoyaltyPoints.successFlag = isSuccessFlag
                } else {
                    objLoyaltyPoints.successFlag = false
                }
                
                if let strMessage = myModel["message"] as? String {
                    objLoyaltyPoints.message = strMessage
                } else {
                    objLoyaltyPoints.message = ""
                }
                
                
                if let accessTokenValue = myModel["emailCampaignId"] as? CLong {
                    objLoyaltyPoints.emailCampaignId = accessTokenValue
                } else {
                    objLoyaltyPoints.emailCampaignId = 0
                }
                if let accessTokenValue = myModel["smsCampaignId"] as? CLong {
                    objLoyaltyPoints.smsCampaignId = accessTokenValue
                } else {
                    objLoyaltyPoints.smsCampaignId = 0
                }
                if let accessTokenValue = myModel["pushCampaignId"] as? CLong {
                    objLoyaltyPoints.pushCampaignId = accessTokenValue
                } else {
                    objLoyaltyPoints.pushCampaignId = 0
                }
                if let accessTokenValue = myModel["pullCampaignId"] as? CLong {
                    objLoyaltyPoints.pullCampaignId = accessTokenValue
                } else {
                    objLoyaltyPoints.pullCampaignId = 0
                }
                
                if let snapshotValue = myModel["emailCampaign"] as? Dictionary<String,AnyObject> {
                    
                    if let strLoyaltyNo =  snapshotValue["campaignId"] as? CLong {
                        objLoyaltyPoints.objEmailCampaign.campaignId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objEmailCampaign.campaignId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["offerId"] as? CLong {
                        objLoyaltyPoints.objEmailCampaign.offerId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objEmailCampaign.offerId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["channels"] as? String {
                        objLoyaltyPoints.objEmailCampaign.channels = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objEmailCampaign.channels = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignText"] as? String {
                        objLoyaltyPoints.objEmailCampaign.campaignText = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objEmailCampaign.campaignText = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignName"] as? String {
                        objLoyaltyPoints.objEmailCampaign.campaignName = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objEmailCampaign.campaignName = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignType"] as? String {
                        objLoyaltyPoints.objEmailCampaign.campaignType = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objEmailCampaign.campaignType = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["templateId"] as? CLong {
                        objLoyaltyPoints.objEmailCampaign.templateId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objEmailCampaign.templateId = 0
                    }
                }
                
                
                if let snapshotValue = myModel["smsCampaign"] as? Dictionary<String,AnyObject> {
                    
                    if let strLoyaltyNo =  snapshotValue["campaignId"] as? CLong {
                        objLoyaltyPoints.objSmsCampaign.campaignId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objSmsCampaign.campaignId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["offerId"] as? CLong {
                        objLoyaltyPoints.objSmsCampaign.offerId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objSmsCampaign.offerId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["channels"] as? String {
                        objLoyaltyPoints.objSmsCampaign.channels = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objSmsCampaign.channels = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignText"] as? String {
                        objLoyaltyPoints.objSmsCampaign.campaignText = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objSmsCampaign.campaignText = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignName"] as? String {
                        objLoyaltyPoints.objSmsCampaign.campaignName = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objSmsCampaign.campaignName = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignType"] as? String {
                        objLoyaltyPoints.objSmsCampaign.campaignType = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objSmsCampaign.campaignType = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["templateId"] as? CLong {
                        objLoyaltyPoints.objSmsCampaign.templateId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objSmsCampaign.templateId = 0
                    }
                }
                
                
                if let snapshotValue = myModel["pushCampaign"] as? Dictionary<String,AnyObject> {
                    
                    if let strLoyaltyNo =  snapshotValue["campaignId"] as? CLong {
                        objLoyaltyPoints.objPushCampaign.campaignId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPushCampaign.campaignId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["offerId"] as? CLong {
                        objLoyaltyPoints.objPushCampaign.offerId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPushCampaign.offerId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["channels"] as? String {
                        objLoyaltyPoints.objPushCampaign.channels = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPushCampaign.channels = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignText"] as? String {
                        objLoyaltyPoints.objPushCampaign.campaignText = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPushCampaign.campaignText = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignName"] as? String {
                        objLoyaltyPoints.objPushCampaign.campaignName = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPushCampaign.campaignName = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignType"] as? String {
                        objLoyaltyPoints.objPushCampaign.campaignType = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPushCampaign.campaignType = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["templateId"] as? CLong {
                        objLoyaltyPoints.objPushCampaign.templateId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPushCampaign.templateId = 0
                    }
                }
                
                
                if let snapshotValue = myModel["pullCampaign"] as? Dictionary<String,AnyObject> {
                    
                    if let strLoyaltyNo =  snapshotValue["campaignId"] as? CLong {
                        objLoyaltyPoints.objPullCampaign.campaignId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPullCampaign.campaignId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["offerId"] as? CLong {
                        objLoyaltyPoints.objPullCampaign.offerId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPullCampaign.offerId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["channels"] as? String {
                        objLoyaltyPoints.objPullCampaign.channels = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPullCampaign.channels = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignText"] as? String {
                        objLoyaltyPoints.objPullCampaign.campaignText = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPullCampaign.campaignText = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignName"] as? String {
                        objLoyaltyPoints.objPullCampaign.campaignName = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPullCampaign.campaignName = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["campaignType"] as? String {
                        objLoyaltyPoints.objPullCampaign.campaignType = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPullCampaign.campaignType = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["templateId"] as? CLong {
                        objLoyaltyPoints.objPullCampaign.templateId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objPullCampaign.templateId = 0
                    }
                }
                
                
                if let accessTokenValue = myModel["loyaltyPoints"] as? CLong {
                    objLoyaltyPoints.loyaltyPoints = accessTokenValue
                } else {
                    objLoyaltyPoints.loyaltyPoints = 0
                }
                
                if let accessTokenValue = myModel["pointsToNextReward"] as? CLong {
                    objLoyaltyPoints.pointsToNextReward = accessTokenValue
                } else {
                    objLoyaltyPoints.pointsToNextReward = 0
                }
                
                if let accessTokenValue = myModel["earnedPoints"] as? CLong {
                    objLoyaltyPoints.earnedPoints = accessTokenValue
                } else {
                    objLoyaltyPoints.earnedPoints = 0
                }
                if let accessTokenValue = myModel["lifeTimePoints"] as? CLong {
                    objLoyaltyPoints.lifeTimePoints = accessTokenValue
                } else {
                    objLoyaltyPoints.lifeTimePoints = 0
                }
                
                if let accessTokenValue = myModel["memberId"] as? CLong {
                    objLoyaltyPoints.memberId = accessTokenValue
                } else {
                    objLoyaltyPoints.memberId = 0
                }
                
                if let accessTokenValue = myModel["criteriaStartDate"] as? String {
                    objLoyaltyPoints.criteriaStartDate = accessTokenValue
                } else {
                    objLoyaltyPoints.criteriaStartDate = ""
                }
                
                if let accessTokenValue = myModel["lastEarnedPointsDate"] as? String {
                    objLoyaltyPoints.lastEarnedPointsDate = accessTokenValue
                } else {
                    objLoyaltyPoints.lastEarnedPointsDate = ""
                }
                
                if let accessTokenValue = myModel["enabled"] as? Bool {
                    objLoyaltyPoints.enabled = accessTokenValue
                } else {
                    objLoyaltyPoints.enabled = false
                }
                
                if let accessTokenValue = myModel["offerId"] as? CLong {
                    objLoyaltyPoints.offerId = accessTokenValue
                } else {
                    objLoyaltyPoints.offerId = 0
                }
                
                
                
                if let snapshotValue = myModel["fishBowlPromotion"] as? Dictionary<String,AnyObject> {
                    
                    if let strLoyaltyNo =  snapshotValue["id"] as? CLong {
                        objLoyaltyPoints.objFishbowlPromotion.fishbowlPromotionId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.fishbowlPromotionId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["name"] as? String {
                        objLoyaltyPoints.objFishbowlPromotion.name = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.name = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["description"] as? String {
                        objLoyaltyPoints.objFishbowlPromotion.description = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.description = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["redemptions"] as? CLong {
                        objLoyaltyPoints.objFishbowlPromotion.redemptions = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.redemptions = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["status"] as? String {
                        objLoyaltyPoints.objFishbowlPromotion.status = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.status = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["type"] as? String {
                        objLoyaltyPoints.objFishbowlPromotion.type = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.type = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["expirationdate"] as? String {
                        objLoyaltyPoints.objFishbowlPromotion.expirationDate = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.expirationDate = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["end_date"] as? String {
                        objLoyaltyPoints.objFishbowlPromotion.endDate = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.endDate = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["publicdescription"] as? String {
                        objLoyaltyPoints.objFishbowlPromotion.publicDescription = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.publicDescription = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["publicname"] as? String {
                        objLoyaltyPoints.objFishbowlPromotion.publicName = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.publicName = ""
                    }
                    if let strLoyaltyNo =  snapshotValue["offertypeid"] as? CLong {
                        objLoyaltyPoints.objFishbowlPromotion.offerTypeId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.offerTypeId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["channelid"] as? CLong {
                        objLoyaltyPoints.objFishbowlPromotion.channelId = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.channelId = 0
                    }
                    if let strLoyaltyNo =  snapshotValue["storerestriction"] as? NSArray {
                        objLoyaltyPoints.objFishbowlPromotion.storeRestriction = strLoyaltyNo
                    } else {
                        objLoyaltyPoints.objFishbowlPromotion.storeRestriction = []
                    }
                    
                }
                
                arrLoyaltyPointRedemptionList.add(objLoyaltyPoints)
                
            }
                
            }
        }
}


public struct FBLoyaltyPointsBO : ResponseBO {
    
    public lazy var  successFlag : Bool = false
    public lazy var  message : String = ""
    public lazy var  emailCampaignId : CLong = 0
    public lazy var  smsCampaignId : CLong = 0
    public lazy var  pushCampaignId : CLong = 0
    public lazy var  pullCampaignId : CLong = 0
    public lazy var  objEmailCampaign : emailCampaignBO = emailCampaignBO()
    public lazy var  objSmsCampaign : smsCampaignBO = smsCampaignBO()
    public lazy var  objPushCampaign : pushCampaignBO = pushCampaignBO()
    public lazy var  objPullCampaign : pullCampaignBO = pullCampaignBO()

    
    
    public lazy var  loyaltyPoints : CLong = 0
    public lazy var  pointsToNextReward : CLong = 0
    public lazy var  earnedPoints : CLong = 0
    public lazy var  lifeTimePoints : CLong = 0
    public lazy var  memberId : CLong = 0
    public lazy var  criteriaStartDate : String = ""
    public lazy var  lastEarnedPointsDate : String = ""
    public lazy var  enabled : Bool = false
    public lazy var  offerId : CLong = 0
    public lazy var  objFishbowlPromotion : FishbowlPromotionBO = FishbowlPromotionBO()
    
    public init()
    {
        
    }
    
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        print("jsondata inside Model is \(json)")
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let strMessage = myModel["message"] as? String {
            self.message = strMessage
        } else {
            self.message = ""
        }
        
   
        if let accessTokenValue = myModel["emailCampaignId"] as? CLong {
            self.emailCampaignId = accessTokenValue
        } else {
            self.emailCampaignId = 0
        }
        if let accessTokenValue = myModel["smsCampaignId"] as? CLong {
            self.smsCampaignId = accessTokenValue
        } else {
            self.smsCampaignId = 0
        }
        if let accessTokenValue = myModel["pushCampaignId"] as? CLong {
            self.pushCampaignId = accessTokenValue
        } else {
            self.pushCampaignId = 0
        }
        if let accessTokenValue = myModel["pullCampaignId"] as? CLong {
            self.pullCampaignId = accessTokenValue
        } else {
            self.pullCampaignId = 0
        }
        
        if let snapshotValue = myModel["emailCampaign"] as? Dictionary<String,AnyObject> {
            
            if let strLoyaltyNo =  snapshotValue["campaignId"] as? CLong {
                self.objEmailCampaign.campaignId = strLoyaltyNo
            } else {
                self.objEmailCampaign.campaignId = 0
            }
            if let strLoyaltyNo =  snapshotValue["offerId"] as? CLong {
                self.objEmailCampaign.offerId = strLoyaltyNo
            } else {
                self.objEmailCampaign.offerId = 0
            }
            if let strLoyaltyNo =  snapshotValue["channels"] as? String {
                self.objEmailCampaign.channels = strLoyaltyNo
            } else {
                self.objEmailCampaign.channels = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignText"] as? String {
                self.objEmailCampaign.campaignText = strLoyaltyNo
            } else {
                self.objEmailCampaign.campaignText = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignName"] as? String {
                self.objEmailCampaign.campaignName = strLoyaltyNo
            } else {
                self.objEmailCampaign.campaignName = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignType"] as? String {
                self.objEmailCampaign.campaignType = strLoyaltyNo
            } else {
                self.objEmailCampaign.campaignType = ""
            }
            if let strLoyaltyNo =  snapshotValue["templateId"] as? CLong {
                self.objEmailCampaign.templateId = strLoyaltyNo
            } else {
                self.objEmailCampaign.templateId = 0
            }
        }
        
        
        if let snapshotValue = myModel["smsCampaign"] as? Dictionary<String,AnyObject> {
            
            if let strLoyaltyNo =  snapshotValue["campaignId"] as? CLong {
                self.objSmsCampaign.campaignId = strLoyaltyNo
            } else {
                self.objSmsCampaign.campaignId = 0
            }
            if let strLoyaltyNo =  snapshotValue["offerId"] as? CLong {
                self.objSmsCampaign.offerId = strLoyaltyNo
            } else {
                self.objSmsCampaign.offerId = 0
            }
            if let strLoyaltyNo =  snapshotValue["channels"] as? String {
                self.objSmsCampaign.channels = strLoyaltyNo
            } else {
                self.objSmsCampaign.channels = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignText"] as? String {
                self.objSmsCampaign.campaignText = strLoyaltyNo
            } else {
                self.objSmsCampaign.campaignText = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignName"] as? String {
                self.objSmsCampaign.campaignName = strLoyaltyNo
            } else {
                self.objSmsCampaign.campaignName = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignType"] as? String {
                self.objSmsCampaign.campaignType = strLoyaltyNo
            } else {
                self.objSmsCampaign.campaignType = ""
            }
            if let strLoyaltyNo =  snapshotValue["templateId"] as? CLong {
                self.objSmsCampaign.templateId = strLoyaltyNo
            } else {
                self.objSmsCampaign.templateId = 0
            }
        }
        
        
        if let snapshotValue = myModel["pushCampaign"] as? Dictionary<String,AnyObject> {
            
            if let strLoyaltyNo =  snapshotValue["campaignId"] as? CLong {
                self.objPushCampaign.campaignId = strLoyaltyNo
            } else {
                self.objPushCampaign.campaignId = 0
            }
            if let strLoyaltyNo =  snapshotValue["offerId"] as? CLong {
                self.objPushCampaign.offerId = strLoyaltyNo
            } else {
                self.objPushCampaign.offerId = 0
            }
            if let strLoyaltyNo =  snapshotValue["channels"] as? String {
                self.objPushCampaign.channels = strLoyaltyNo
            } else {
                self.objPushCampaign.channels = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignText"] as? String {
                self.objPushCampaign.campaignText = strLoyaltyNo
            } else {
                self.objPushCampaign.campaignText = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignName"] as? String {
                self.objPushCampaign.campaignName = strLoyaltyNo
            } else {
                self.objPushCampaign.campaignName = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignType"] as? String {
                self.objPushCampaign.campaignType = strLoyaltyNo
            } else {
                self.objPushCampaign.campaignType = ""
            }
            if let strLoyaltyNo =  snapshotValue["templateId"] as? CLong {
                self.objPushCampaign.templateId = strLoyaltyNo
            } else {
                self.objPushCampaign.templateId = 0
            }
        }
        
        
        if let snapshotValue = myModel["pullCampaign"] as? Dictionary<String,AnyObject> {
            
            if let strLoyaltyNo =  snapshotValue["campaignId"] as? CLong {
                self.objPullCampaign.campaignId = strLoyaltyNo
            } else {
                self.objPullCampaign.campaignId = 0
            }
            if let strLoyaltyNo =  snapshotValue["offerId"] as? CLong {
                self.objPullCampaign.offerId = strLoyaltyNo
            } else {
                self.objPullCampaign.offerId = 0
            }
            if let strLoyaltyNo =  snapshotValue["channels"] as? String {
                self.objPullCampaign.channels = strLoyaltyNo
            } else {
                self.objPullCampaign.channels = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignText"] as? String {
                self.objPullCampaign.campaignText = strLoyaltyNo
            } else {
                self.objPullCampaign.campaignText = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignName"] as? String {
                self.objPullCampaign.campaignName = strLoyaltyNo
            } else {
                self.objPullCampaign.campaignName = ""
            }
            if let strLoyaltyNo =  snapshotValue["campaignType"] as? String {
                self.objPullCampaign.campaignType = strLoyaltyNo
            } else {
                self.objPullCampaign.campaignType = ""
            }
            if let strLoyaltyNo =  snapshotValue["templateId"] as? CLong {
                self.objPullCampaign.templateId = strLoyaltyNo
            } else {
                self.objPullCampaign.templateId = 0
            }
        }
        
        
        if let accessTokenValue = myModel["loyaltyPoints"] as? CLong {
            self.loyaltyPoints = accessTokenValue
        } else {
            self.loyaltyPoints = 0
        }
        
        if let accessTokenValue = myModel["pointsToNextReward"] as? CLong {
            self.pointsToNextReward = accessTokenValue
        } else {
            self.pointsToNextReward = 0
        }
        
        if let accessTokenValue = myModel["earnedPoints"] as? CLong {
            self.earnedPoints = accessTokenValue
        } else {
            self.earnedPoints = 0
        }
        if let accessTokenValue = myModel["lifeTimePoints"] as? CLong {
            self.lifeTimePoints = accessTokenValue
        } else {
            self.lifeTimePoints = 0
        }
        
        if let accessTokenValue = myModel["memberId"] as? CLong {
            self.memberId = accessTokenValue
        } else {
            self.memberId = 0
        }
        
        if let accessTokenValue = myModel["criteriaStartDate"] as? String {
            self.criteriaStartDate = accessTokenValue
        } else {
            self.criteriaStartDate = ""
        }
        
        if let accessTokenValue = myModel["lastEarnedPointsDate"] as? String {
            self.lastEarnedPointsDate = accessTokenValue
        } else {
            self.lastEarnedPointsDate = ""
        }
        
        if let accessTokenValue = myModel["enabled"] as? Bool {
            self.enabled = accessTokenValue
        } else {
            self.enabled = false
        }
        
        if let accessTokenValue = myModel["offerId"] as? CLong {
            self.offerId = accessTokenValue
        } else {
            self.offerId = 0
        }
        
        
        
        if let snapshotValue = myModel["fishBowlPromotion"] as? Dictionary<String,AnyObject> {
            
            if let strLoyaltyNo =  snapshotValue["id"] as? CLong {
                self.objFishbowlPromotion.fishbowlPromotionId = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.fishbowlPromotionId = 0
            }
            if let strLoyaltyNo =  snapshotValue["name"] as? String {
                self.objFishbowlPromotion.name = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.name = ""
            }
            if let strLoyaltyNo =  snapshotValue["description"] as? String {
                self.objFishbowlPromotion.description = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.description = ""
            }
            if let strLoyaltyNo =  snapshotValue["redemptions"] as? CLong {
                self.objFishbowlPromotion.redemptions = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.redemptions = 0
            }
            if let strLoyaltyNo =  snapshotValue["status"] as? String {
                self.objFishbowlPromotion.status = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.status = ""
            }
            if let strLoyaltyNo =  snapshotValue["type"] as? String {
                self.objFishbowlPromotion.type = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.type = ""
            }
            if let strLoyaltyNo =  snapshotValue["expirationdate"] as? String {
                self.objFishbowlPromotion.expirationDate = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.expirationDate = ""
            }
            if let strLoyaltyNo =  snapshotValue["end_date"] as? String {
                self.objFishbowlPromotion.endDate = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.endDate = ""
            }
            if let strLoyaltyNo =  snapshotValue["publicdescription"] as? String {
                self.objFishbowlPromotion.publicDescription = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.publicDescription = ""
            }
            if let strLoyaltyNo =  snapshotValue["publicname"] as? String {
                self.objFishbowlPromotion.publicName = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.publicName = ""
            }
            if let strLoyaltyNo =  snapshotValue["offertypeid"] as? CLong {
                self.objFishbowlPromotion.offerTypeId = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.offerTypeId = 0
            }
            if let strLoyaltyNo =  snapshotValue["channelid"] as? CLong {
                self.objFishbowlPromotion.channelId = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.channelId = 0
            }
            if let strLoyaltyNo =  snapshotValue["storerestriction"] as? NSArray {
                self.objFishbowlPromotion.storeRestriction = strLoyaltyNo
            } else {
                self.objFishbowlPromotion.storeRestriction = []
            }
            
        }
        
        
        
    }
    
}


public struct FBRespnoseBO : ResponseBO {
    
    
    public lazy var  accessToken : String = ""
    public lazy var  successFlag : Bool = false
    public lazy var  message : String = ""
    public lazy var  loyaltyNumber : String = ""
    public lazy var  memberId : CLong = 0
    public lazy var  inCommToken : String = ""
    public lazy var  response : String = ""

    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        print("jsondata inside Model is \(json)")
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let strMessage = myModel["message"] as? String {
            self.message = strMessage
        } else {
            self.message = ""
        }
        
        if self.successFlag == true
        {
            
            if let accessTokenValue = myModel["accessToken"] as? String {
                self.accessToken = accessTokenValue
            } else {
                self.accessToken = ""
            }
        }
        
        if let snapshotValue = myModel["loyalty"] as? Dictionary<String,AnyObject> {
            
            if let strLoyaltyNo =  snapshotValue["loyaltyno"] as? String {
                print("loyaltyNumber is \(loyaltyNumber)")
                
                self.loyaltyNumber = strLoyaltyNo
            } else {
                self.loyaltyNumber = ""
            }
        }
        
        
        if let strMemberId = myModel["memberid"] as? CLong {
            self.memberId = strMemberId
        } else {
            self.memberId = 0
        }
        
        
    }
}

public struct ErrorBO : ResponseBO {
    
    public lazy var statusCode : String = ""
    public lazy var errorMessage : String = ""
    public lazy var successFlag : Bool = false
    
    
    public init?(json:Any) {
        guard let myModel = json as? NSDictionary else {
            return nil
        }
        
        
        print("jsondata inside Model is \(json)")
        
        
        if let isSuccessFlag = myModel["successFlag"] as? Bool {
            self.successFlag = isSuccessFlag
        } else {
            self.successFlag = false
        }
        
        if let message = myModel["message"] as? String {
            self.errorMessage = message
        } else {
            self.errorMessage = "Unknown Error from Fishbowl server"
        }
        
    }
    
}




