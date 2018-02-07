//
//  FishbowlIncommService.swift
//  JambaJuice
//
//  Created by VT010 on 5/4/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import FishBowlLibrary

class FishbowlIncommService{
    var submitPromoOrderInput:InCommPromoOrder?
    func getIncommAuthtoken(_ callback:@escaping InCommAuthTokenCallback){
        
        let inCommSubmitAuthToken = SubmitInCommAuthToken.init(spendGoKey: Configuration.sharedConfiguration.SpendGoAPIKey,spendGoAuthorizationToken: (UserService.sharedUser?.spendgoAuthToken)!, spendGoId: (UserService.sharedUser?.id)!)
        
        let objectdataManager = DataManager()
        
        objectdataManager.InCommTokenAPI(dictBody: inCommSubmitAuthToken.serializeAsJSONDictionary()
            as [String : AnyObject]) { (response, error) in
                if error != nil{
                    if error == DataManagerError.TokenExpiration{
                       FishbowlApiClassService.sharedInstance.getLoggedInUserAccessToken{
                        let objectdataManager = DataManager()
                        objectdataManager.InCommTokenAPI(dictBody: inCommSubmitAuthToken.serializeAsJSONDictionary()
                            as [String : AnyObject]) { (response, error) in
                                self.handleIncommAuthTokenResponse(response,error: error, callback: callback)
                        }
                        }
                    }else{
                        
                        self.handleIncommAuthTokenResponse(response,error: error, callback: callback)
                    }
                }else{
                    self.handleIncommAuthTokenResponse(response,error: error, callback: callback)                }
        }
    }
    
    func handleIncommAuthTokenResponse(_ response: AnyObject?,error:DataManagerError?, callback: InCommAuthTokenCallback){
        if response == nil && error == nil{
            callback(nil,NSError.init(description: "Unexpected Error"))
        }else if response == nil && error != nil{
            if error == DataManagerError.NOInternetConnection{
                callback(nil,NSError.init(description: "The Internet connection appears to be offline.", code: -1009))
            }else{
                callback(nil,NSError.init(description: "Unexpected Error"))
            }
        }
        else{
            let successFlag = response!["successFlag"] as? Bool
            let incommToken = response!["inCommToken"] as? String
            if successFlag != nil && successFlag! && incommToken != nil{
                let inCommAuthtoken = InCommAuthToken.init(successFlag: successFlag!, inCommToken: incommToken!)
                callback(inCommAuthtoken, nil)
            }else{
                callback(nil,NSError.init(description: "Unexpected Error"))
            }
        }
    }
    
    func getIncommOrderId(_ callback:@escaping InCommOrderIdCallback){
        
        guard let spendGoUserId = UserService.sharedUser?.id else{
            return
        }
        
        
        let objectDataManager = DataManager()
        
        let inCommUserIdDetails = SubmitInCommOrderIdDetails.init(customerId: spendGoUserId)
        
        objectDataManager.InCommOrderIDAPI(dictBody: inCommUserIdDetails.serializeAsJSONDictionary() as [String : AnyObject]) { (response, error) in
            if error != nil{
                if error == DataManagerError.TokenExpiration{
                    FishbowlApiClassService.sharedInstance.getLoggedInUserAccessToken {
                        let objectDataManager = DataManager()
                        objectDataManager.InCommOrderIDAPI(dictBody:  inCommUserIdDetails.serializeAsJSONDictionary() as [String : AnyObject]){ (response, error) in
                            self.handleIncommOrderIdResponse(response, callback: callback)
                        }
                    }
                }else{
                    
                    self.handleIncommOrderIdResponse(response, callback: callback)
                }
            }else{
                self.handleIncommOrderIdResponse(response, callback: callback)
            }
        }
        
    }
    
    func handleIncommOrderIdResponse(_ response: AnyObject?, callback: InCommOrderIdCallback){
        
        if response == nil{
            callback(nil, GiftCardAppConstants.generalError)
        }else{
            if let inCommOrderId = response!["inCommOrderId"]{
                if let inCommOrderIdString = inCommOrderId as? String{
                    callback(inCommOrderIdString, nil)
                }else{
                    callback(nil,GiftCardAppConstants.generalError)
                }
            }
            else{
                callback(nil, GiftCardAppConstants.generalError)
            }
        }
    }
    
    func submitPromoOrder(_ incommPromoOrder:InCommPromoOrder){
        let objectManager = DataManager()
        objectManager.OrderValueAPI(dictBody: incommPromoOrder.serializeAsJSONDictionary() as [String : AnyObject]) { (response, error) in
            if error != nil{
                if error == DataManagerError.TokenExpiration{
                    let objectManager = DataManager()
                    FishbowlApiClassService.sharedInstance.getLoggedInUserAccessToken {
                        objectManager.OrderValueAPI(dictBody: incommPromoOrder.serializeAsJSONDictionary() as [String : AnyObject]) { (response, error) in
                            self.handleSubmitPromoOrderResponse(response)
                        }
                    }
                }else{
                    self.handleSubmitPromoOrderResponse(response)
                }
            }else{
                self.handleSubmitPromoOrderResponse(response)
            }
        }
    }
    
    func handleSubmitPromoOrderResponse(_ response: AnyObject?){
        if response != nil {
            let code = response?["code"] as? Int
            if code != nil && code == GiftCardAppConstants.errorCodeInvalidUser{
                InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                    if inCommUserStatus{
                        if self.submitPromoOrderInput != nil{
                            self.submitPromoOrder(self.submitPromoOrderInput!)
                        }
                    }
                })
            }
        }
    }
    
    func updateTransactionDetails(_ incommOrderStatusDetails: InCommOrderStatusDetails){
        let objectManager = DataManager()
        objectManager.updateIncommTransactionAPI(dictBody: incommOrderStatusDetails.serializeAsJSONDictionary() as [String : AnyObject]) { (result, error) in
            if  error != nil{
                if error == DataManagerError.TokenExpiration{
                    let objectManager = DataManager()
                    FishbowlApiClassService.sharedInstance.getLoggedInUserAccessToken {
                        objectManager.updateIncommTransactionAPI(dictBody: incommOrderStatusDetails.serializeAsJSONDictionary() as [String : AnyObject]) { (result, error) in
                            self.handleUpdateTransactionDetails(result)
                        }
                    }
                }else{
                    self.handleUpdateTransactionDetails(result)
                }
            } else{
                self.handleUpdateTransactionDetails(result)
            }
        }
    }
    
    func handleUpdateTransactionDetails(_ response:AnyObject?){
        if response == nil{
            print("failed")
        }else{
            print("success")
        }
    }
}
