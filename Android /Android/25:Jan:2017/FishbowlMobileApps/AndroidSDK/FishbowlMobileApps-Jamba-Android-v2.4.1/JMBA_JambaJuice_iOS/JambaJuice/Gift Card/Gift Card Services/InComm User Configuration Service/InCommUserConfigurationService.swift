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

typealias InCommUserIdCallback = (_ inCommUserId: Int32?, _ error: NSError?) -> Void
typealias InCommUserConfigurationCallback = (_ inCommUserStatus:Bool, _ error:NSError?) -> Void
typealias InCommUserStatusCallback = (_ status:Bool) -> Void
typealias InCommAuthTokenCallback = (_ inCommAuthToken:InCommAuthToken?, _ error: NSError?) -> Void



class InCommUserConfigurationService{
    
    static let sharedInstance = InCommUserConfigurationService()
    var generalError = NSError(domain: "InComm Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Unable to proceed please try again"])
//    var generalError = NSError.init(coder: "Unable to proceed please try again")
    
    fileprivate(set) var inCommUserId: Int32?
    
    //MARK: InCommServiceConfiguration
    func inCommServiceConfiguration(_ callback:@escaping InCommUserConfigurationCallback){
        if UserService.sharedUser?.id == nil || UserService.sharedUser?.spendgoAuthToken == nil{
            return callback(false,generalError)
        }
        else{
            let inCommSubmitAuthToken = SubmitInCommAuthToken.init(spendGoKey: Configuration.sharedConfiguration.SpendGoAPIKey,spendGoAuthorizationToken: (UserService.sharedUser?.spendgoAuthToken)!, spendGoId: (UserService.sharedUser?.id)!)
            
            getInCommAuthToken(inCommSubmitAuthToken, callback: { (inCommAuthToken, error) in
                if error != nil{
                    return callback(false,error)
                }
                else{
                    let configuration = Configuration.sharedConfiguration
                    InCommService.configurationForService(configuration.InCommBaseURL, clientId: configuration.InCommClientId, apiKey: inCommAuthToken!.inCommToken, testMode: configuration.InCommTestMode, printLog: configuration.InCommPrintLog)
                    GiftCardCreationService.sharedInstance.setInCommAuthToken(inCommAuthToken!.inCommToken)
                    return callback(true,nil)
                }
            })
        }
        
    }
    
    //MARK: Get inComm user id
    func getInCommUserId(_ callback: @escaping InCommUserIdCallback){
        
        if inCommUserId != nil{
            return callback(inCommUserId, nil)
        }
        
        if UserService.sharedUser?.id == nil{
            return callback(nil, generalError)
        }
        InCommUserService.getInCommUserAccessToken { (inCommUserAccessToken, error) in
            if error != nil{
                // When credentials invalid
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    self.inCommServiceConfiguration({ (inCommUserStatus, error) in
                        if inCommUserStatus{
                            InCommUserService.getInCommUserAccessToken({ (inCommUserAccessToken, error) in
                                if error != nil{
                                    return callback(nil, error)
                                }
                                else{
                                    self.resetUserID((inCommUserAccessToken?.userID)!)
                                    // Update user profile
                                    self.updateInCommProfileDetails()
                                    return callback(inCommUserAccessToken?.userID, nil)
                                }
                            })
                        }
                        else{
                            return callback(nil, self.generalError)
                        }
                    })
                    
                }
                else{
                    return callback(nil, error)
                }
                //
            }
            else{
                self.resetUserID((inCommUserAccessToken?.userID)!)
                // Update user profile
                self.updateInCommProfileDetails()
                return callback(inCommUserAccessToken?.userID, nil)
            }
        }
    }
    
    //MARK: InComm reset user id
    func resetUserID(_ inCommUserId: Int32){
        self.inCommUserId = inCommUserId
    }
    
    //MARK: InComm user validation
    func inCommUserValidation(_ callback:@escaping InCommUserStatusCallback){
        if inCommUserId != nil{
            return callback(true)
        }
        self.inCommServiceConfiguration { (inCommUserStatus,error) in
            if inCommUserStatus{
                self.getInCommUserId{ (inCommUserId, error) in
                    if error != nil{
                        return callback(false)
                    } else {
                        InCommGiftCardBrandDetails.sharedInstance.loadBrandDetails{ (brand, error) in
                            if error != nil{
                                return callback(false)
                            } else {
                                return callback(true)
                            }
                        }
                    }
                }
            }
            else{
                return callback(false)
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
    
    func getInCommAuthToken(_ submitInCommAuthToken:SubmitInCommAuthToken,callback:@escaping InCommAuthTokenCallback){
    
        let fish = FishbowlIncommService()
        
        fish.getIncommAuthtoken { (incommAuthoken, error) in
            return callback(incommAuthoken, error)
        }
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
                            InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
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
