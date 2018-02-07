//
//  InCommUserConfigurationService.swift
//  JambaGiftCard
//
//  Created by vThink on 8/26/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import InCommSDK
import SpendGoSDK

typealias InCommUserIdCallback = (inCommUserId: Int32?, error: NSError?) -> Void
typealias InCommUserConfigurationCallback = (inCommUserStatus:Bool) -> Void
typealias InCommUserStatusCallback = (status:Bool) -> Void
typealias InCommAuthTokenCallback = (inCommAuthToken:InCommAuthToken?, error: NSError?) -> Void



class InCommUserConfigurationService{
    
    static let sharedInstance = InCommUserConfigurationService()
    
    var generalError = NSError.init(description: "Unable to proceed please try again")
    
    private(set) var inCommUserId: Int32?
    
    //MARK: InCommServiceConfiguration
    func inCommServiceConfiguration(callback:InCommUserConfigurationCallback){
        if UserService.sharedUser?.id == nil || UserService.sharedUser?.spendgoAuthToken == nil{
            return callback(inCommUserStatus: false)
        }
        else{
            let inCommSubmitAuthToken = SubmitInCommAuthToken.init(spendGoKey: Configuration.sharedConfiguration.SpendGoAPIKey,spendGoAuthorizationToken: (UserService.sharedUser?.spendgoAuthToken)!, spendGoId: (UserService.sharedUser?.id)!)
            
            getInCommAuthToken(inCommSubmitAuthToken, callback: { (inCommAuthToken, error) in
                if error != nil{
                    return callback(inCommUserStatus: false)
                }
                else{
                    let configuration = Configuration.sharedConfiguration
                    InCommService.configurationForService(configuration.InCommBaseURL, clientId: configuration.InCommClientId, apiKey: inCommAuthToken!.inCommToken, testMode: configuration.InCommTestMode, printLog: configuration.InCommPrintLog)
                    GiftCardCreationService.sharedInstance.setInCommAuthToken(inCommAuthToken!.inCommToken)
                    return callback(inCommUserStatus: true)
                }
            })
        }
        
    }
    
    //MARK: Get inComm user id
    func getInCommUserId(callback: InCommUserIdCallback){
        
        if inCommUserId != nil{
            return callback(inCommUserId: inCommUserId, error: nil)
        }
        
        if UserService.sharedUser?.id == nil{
            return callback(inCommUserId: nil, error: generalError)
        }
        InCommUserService.getInCommUserAccessToken { (inCommUserAccessToken, error) in
            if error != nil{
                // When credentials invalid
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    self.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserService.getInCommUserAccessToken({ (inCommUserAccessToken, error) in
                                if error != nil{
                                    return callback(inCommUserId: nil, error: error)
                                }
                                else{
                                    self.resetUserID((inCommUserAccessToken?.userID)!)
                                    // Update user profile
                                    self.updateInCommProfileDetails()
                                    return callback(inCommUserId: inCommUserAccessToken?.userID, error: nil)
                                }
                            })
                        }
                        else{
                            return callback(inCommUserId: nil, error: self.generalError)
                        }
                    })
                    
                }
                else{
                    return callback(inCommUserId: nil, error: error)
                }
                //
            }
            else{
                self.resetUserID((inCommUserAccessToken?.userID)!)
                // Update user profile
                self.updateInCommProfileDetails()
                return callback(inCommUserId: inCommUserAccessToken?.userID, error: nil)
            }
        }
    }
    
    //MARK: InComm reset user id
    func resetUserID(inCommUserId: Int32){
        self.inCommUserId = inCommUserId
    }
    
    //MARK: InComm user validation
    func inCommUserValidation(callback:InCommUserStatusCallback){
        if inCommUserId != nil{
            return callback(status: true)
        }
        self.inCommServiceConfiguration { (inCommUserStatus) in
            if inCommUserStatus{
                self.getInCommUserId{ (inCommUserId, error) in
                    if error != nil{
                        return callback(status: false)
                    } else {
                        InCommGiftCardBrandDetails.sharedInstance.loadBrandDetails{ (brand, error) in
                            if error != nil{
                                return callback(status: false)
                            } else {
                                return callback(status: true)
                            }
                        }
                    }
                }
            }
            else{
                return callback(status: false)
            }
        }
        
    }
    
    //MARK: Logout inComm user
    func logoutInCommUser(){
        inCommUserId = nil
        GiftCardCreationService.sharedInstance.resetInCommAuthToken()
        InCommGiftCardBrandDetails.sharedInstance.removeBrandDetails()
        GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
        GiftCardCreationService.sharedInstance.resetInCommUserGiftCards()
    }
    
    func getInCommAuthToken(submitInCommAuthToken:SubmitInCommAuthToken,callback:InCommAuthTokenCallback){
        //        clpAnalyticsService.sharedInstance.clpsdkobj?.getInCommAuthToken(submitInCommAuthToken.serializeAsJSONDictionary()) { (result, error) -> Void in
        //            if error != nil{
        //                return callback(inCommAuthToken: nil, error: error)
        //            }
        //            else{
        //                if result != nil{
        //                    let successFlag = result["successFlag"]
        //                    let incommToken = result["inCommToken"]
        //                    if successFlag != nil && successFlag!.boolValue && incommToken != nil{
        //                        return callback(inCommAuthToken: InCommAuthToken.init(responseData: result), error:nil)
        //                    }
        //                    else{
        //                        return callback(inCommAuthToken: nil, error: GiftCardAppConstants.generalError)
        //                    }
        //                }
        //                else{
        //                    return callback(inCommAuthToken: nil, error: GiftCardAppConstants.generalError)
        //                }
        //
        //            }
        //        }
    }
    
    //MARK: Update inComm profile details
    func updateInCommProfileDetails(){
        if UserService.sharedUser == nil {
            return
        }
        
        if inCommUserId == nil{
            self.getInCommUserId({ (inCommUserId, error) in
                if error != nil {
                    return
                } else if inCommUserId != nil {
                    self.updateInCommProfileDetailsWithIncommId()
                }
            })
        } else {
            updateInCommProfileDetailsWithIncommId()
        }
        
        
    }
    
    //MARK: Update inComm profile details with incomm Id
    func updateInCommProfileDetailsWithIncommId() {
        var userFirstName       = ""
        var userLastName        = ""
        var userEmailAddress    = ""
        
        if let userDetail = UserService.sharedUser {
            if  let firstName = userDetail.firstName {
                userFirstName = firstName
            }
            
            if let lastName = userDetail.lastName{
                userLastName = lastName
            }
            
            if let emailAddress = userDetail.emailAddress{
                userEmailAddress = emailAddress
            }
            
            if self.inCommUserId != nil {
                let inCommUserProfileDetails = InCommUserProfileDetails.init(firstName: userFirstName, lastName: userLastName, emailAddress: userEmailAddress, id: inCommUserId!)
                
                InCommUserService.updateUserProfileDetails(inCommUserId!, userProfileDetails: inCommUserProfileDetails) { (error) in
                    if let inCommError = error{
                        if inCommError.code == GiftCardAppConstants.errorCodeInvalidUser{
                            InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                                if inCommUserStatus{
                                    if self.inCommUserId != nil {
                                        InCommUserService.updateUserProfileDetails(self.inCommUserId!, userProfileDetails: inCommUserProfileDetails, callback: { (error) in
                                        })
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}
