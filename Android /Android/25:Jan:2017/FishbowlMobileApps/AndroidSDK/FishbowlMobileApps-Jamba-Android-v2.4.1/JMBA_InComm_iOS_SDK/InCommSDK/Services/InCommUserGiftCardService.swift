//
//  IncommUserCardService.swift
//  InCommSDK
//
//  Created by vThink on 6/10/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation

public typealias UserGiftCardCallback = (_ userGiftCard: InCommUserGiftCard?, _ error: NSError?) -> Void
public typealias UserGiftCardsCallback = (_ userGiftCards: [InCommUserGiftCard], _ error: NSError?) -> Void
public typealias AutoReloadCallback = (_ autoReloadSavable: InCommAutoReloadSavable?, _ error: NSError?) -> Void
public typealias EmptyResponseCallback = (_ error: NSError?) -> Void
public typealias GiftCardBalanceCallback = (_ giftCardBalance: InCommGiftCardBalance?, _ error: NSError?) -> Void
public typealias GiftCardTransactionHistoryCallback = (_ giftCardTransactionHistory: InCommGiftCardTransactionHistory?, _ error: NSError?) -> Void

open class InCommUserGiftCardService {
    
    // MARK: AssociateGiftCard
    open class func associateGiftCard(_ userId:Int32!, brandId: String!, cardNumber: String!, cardPin: String!, callback: @escaping UserGiftCardCallback) {
        var parameters = InCommJSONDictionary()
        parameters["BrandId"] = brandId as AnyObject?
        parameters["CardNumber"] = cardNumber as AnyObject?
        parameters["CardPin"] = cardPin as AnyObject?
        
        InCommService.post("/Users/\(userId!)/Cards", parameters: parameters) { (response, error)  -> Void in
            
            if error != nil {
                callback(nil, error)
                return
            }
            let userCard = InCommUserGiftCard(json: response)
            callback(userCard, nil)
        }
    }
    
    // MARK: AssociateGiftCard by cardToken
    open class func associateGiftCard(_ userId:Int32!,cardToken:String!, callback: @escaping UserGiftCardCallback) {
        var parameters = InCommJSONDictionary()
        parameters["CardToken"] = cardToken as AnyObject?
        
        InCommService.post("/Users/\(userId!)/Cards", parameters: parameters) { (response, error)  -> Void in
            
            if error != nil {
                callback(nil, error)
                return
            }
            let userCard = InCommUserGiftCard(json: response)
            callback(userCard, nil)
        }
    }
    
    // MARK: GetGiftCards
    open class func getGiftCards(_ userId: Int32!, callback: @escaping UserGiftCardsCallback){
        InCommService.get("/Users/\(userId!)/Cards") { (response, error) in
            if error != nil {
                callback([], error)
                return
            }
            let userCards = response.arrayValue.map { InCommUserGiftCard(json: $0) }
            callback(userCards.reversed(), nil)
        }
    }
    
    // MARK: GetGiftCard
    open class func getGiftCard(_ userId: Int32!, cardId: Int32!, callback: @escaping UserGiftCardCallback){
        InCommService.get("/Users/\(userId!)/Cards/\(cardId!)") { (response, error) in
            if error != nil {
                callback(nil, error)
                return
            }
            callback(InCommUserGiftCard(json: response), nil)
        }
    }
    
    // MARK: CreateAutoReload
    open class func createAutoReload(_ userId: Int32!, cardId: Int32!, autoReload:InCommAutoReload, callback: @escaping AutoReloadCallback){
        
        InCommService.post("/Users/\(userId!)/Cards/\(cardId!)/AutoReloads",parameters: autoReload.serializeAsJSONDictionary() ){ (response, error) in
            
            if error != nil {
                callback(nil, error)
                return
            }
            callback(InCommAutoReloadSavable(json: response), nil)
        }
    }
    
    // MARK: DeleteGiftCard
    open class func deleteGiftCard(_ userId: Int32!, cardId: Int32!, callback: @escaping EmptyResponseCallback){
        InCommService.delete("/Users/\(userId!)/Cards/\(cardId!)") { (response, error) -> Void in
            if error != nil{
                callback(error)
                return
            }
            callback(nil)
        }
    }
    
    // MARK: GetAutoReload
    open class func getAutoReload(_ userId: Int32!, cardId: Int32!, autoReloadId: Int32!, callback:@escaping AutoReloadCallback){
        InCommService.get("/Users/\(userId!)/Cards/\(cardId!)/AutoReloads/\(autoReloadId!)"){(response, error) -> Void in
            if error != nil{
                callback(nil, error)
                return
            }
            callback(InCommAutoReloadSavable(json: response), nil)
        }
    }
    
    // MARK: GetBalance
    open class func getGiftCardBalance(_ userId: Int32!, cardId: Int32!, callback: @escaping GiftCardBalanceCallback){
        InCommService.get("/Users/\(userId!)/Cards/\(cardId!)/GetBalance"){(response, error) -> Void in
            if error != nil{
                callback(nil, error)
                return
            }
            callback(InCommGiftCardBalance(json: response), nil)
        }
    }
    
    // MARK: GetTransactionHistory
    open class func getGiftCardTransactionHistory(_ userId: Int32!, cardId: Int32!, callback: @escaping GiftCardTransactionHistoryCallback){
        InCommService.get("/Users/\(userId!)/Cards/\(cardId!)/GetTransactionHistory"){(response, error) -> Void in
            if error != nil{
                callback(nil, error)
                return
            }
            callback(InCommGiftCardTransactionHistory(json: response), nil)
            return
        }
    }
    
    // MARK: DeleteAutoReload
    open class func deleteAutoReload(_ userId: Int32!, cardId: Int32!, autoReloadId: Int32!, callback: @escaping EmptyResponseCallback){
        InCommService.delete("/Users/\(userId!)/Cards/\(cardId!)/AutoReloads/\(autoReloadId!)"){ (response, error) -> Void in
            if error != nil{
                callback(error)
                return
            }
            callback(nil)
        }
    }
    
    // MARK: UpdateAutoReloadStatus
    open class func updateAutoReloadStatus(_ userId: Int32!, cardId: Int32!, autoReloadId: Int32!, active: Bool!, callback: @escaping EmptyResponseCallback){
        //        let parameters = InCommJSONDictionary()
        InCommService.put("/Users/\(userId!)/Cards/\(cardId!)/AutoReloads/\(autoReloadId!)/UpdateState?active=\(active!)", parameters: nil){ (response, error) -> Void in
            if error != nil{
                callback(error)
                return
            }
            callback(nil)
        }
    }
    
    // MARK: UpdateUserGiftCardName
    open class func updateUserGiftCardName(_ userId: Int32!, cardId: Int32!, cardName: String!, callback:@escaping UserGiftCardCallback){
        var parameters = InCommJSONDictionary()
        parameters["CardName"] = cardName as AnyObject?
        InCommService.put("/Users/\(userId!)/Cards/\(cardId!)", parameters: parameters){ (response, error) -> Void in
            if error != nil{
                callback(nil, error)
                return
            }
            callback(InCommUserGiftCard(json: response), nil )
        }
    }
    
}
