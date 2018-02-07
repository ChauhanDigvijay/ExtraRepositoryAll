//
//  IncommUserCardService.swift
//  InCommSDK
//
//  Created by vThink on 6/10/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation

public typealias UserGiftCardCallback = (userGiftCard: InCommUserGiftCard?, error: NSError?) -> Void
public typealias UserGiftCardsCallback = (userGiftCards: [InCommUserGiftCard], error: NSError?) -> Void
public typealias AutoReloadCallback = (autoReloadSavable: InCommAutoReloadSavable?, error: NSError?) -> Void
public typealias EmptyResponseCallback = (error: NSError?) -> Void
public typealias GiftCardBalanceCallback = (giftCardBalance: InCommGiftCardBalance?, error: NSError?) -> Void
public typealias GiftCardTransactionHistoryCallback = (giftCardTransactionHistory: InCommGiftCardTransactionHistory?, error: NSError?) -> Void

public class InCommUserGiftCardService {
    
    // MARK: AssociateGiftCard
    public class func associateGiftCard(userId:Int32!, brandId: String!, cardNumber: String!, cardPin: String!, callback: UserGiftCardCallback) {
        var parameters = InCommJSONDictionary()
        parameters["BrandId"] = brandId
        parameters["CardNumber"] = cardNumber
        parameters["CardPin"] = cardPin
        
        InCommService.post("/Users/\(userId)/Cards", parameters: parameters) { (response, error)  -> Void in
            
            if error != nil {
                callback(userGiftCard: nil, error: error)
                return
            }
            let userCard = InCommUserGiftCard(json: response)
            callback(userGiftCard: userCard, error: nil)
        }
    }
    
    // MARK: AssociateGiftCard by cardToken
    public class func associateGiftCard(userId:Int32!,cardToken:String!, callback: UserGiftCardCallback) {
        var parameters = InCommJSONDictionary()
        parameters["CardToken"] = cardToken
        
        InCommService.post("/Users/\(userId)/Cards", parameters: parameters) { (response, error)  -> Void in
            
            if error != nil {
                callback(userGiftCard: nil, error: error)
                return
            }
            let userCard = InCommUserGiftCard(json: response)
            callback(userGiftCard: userCard, error: nil)
        }
    }
    
    // MARK: GetGiftCards
    public class func getGiftCards(userId: Int32!, callback: UserGiftCardsCallback){
        InCommService.get("/Users/\(userId)/Cards") { (response, error) in
            if error != nil {
                callback(userGiftCards: [], error: error)
                return
            }
            let userCards = response.arrayValue.map { InCommUserGiftCard(json: $0) }
            callback(userGiftCards: userCards.reverse(), error: nil)
        }
    }
    
    // MARK: GetGiftCard
    public class func getGiftCard(userId: Int32!, cardId: Int32!, callback: UserGiftCardCallback){
        InCommService.get("/Users/\(userId)/Cards/\(cardId)") { (response, error) in
            if error != nil {
                callback(userGiftCard: nil, error: error)
                return
            }
            callback(userGiftCard: InCommUserGiftCard(json: response), error: nil)
        }
    }
    
    // MARK: CreateAutoReload
    public class func createAutoReload(userId: Int32!, cardId: Int32!, autoReload:InCommAutoReload, callback: AutoReloadCallback){
        
        InCommService.post("/Users/\(userId)/Cards/\(cardId)/AutoReloads",parameters: autoReload.serializeAsJSONDictionary() ){ (response, error) in
            
            if error != nil {
                callback(autoReloadSavable: nil, error: error)
                return
            }
            callback(autoReloadSavable: InCommAutoReloadSavable(json: response), error: nil)
        }
    }
    
    // MARK: DeleteGiftCard
    public class func deleteGiftCard(userId: Int32!, cardId: Int32!, callback: EmptyResponseCallback){
        InCommService.delete("/Users/\(userId)/Cards/\(cardId)") { (response, error) -> Void in
            if error != nil{
                callback(error: error)
                return
            }
            callback(error: nil)
        }
    }
    
    // MARK: GetAutoReload
    public class func getAutoReload(userId: Int32!, cardId: Int32!, autoReloadId: Int32!, callback:AutoReloadCallback){
        InCommService.get("/Users/\(userId)/Cards/\(cardId)/AutoReloads/\(autoReloadId)"){(response, error) -> Void in
            if error != nil{
                callback(autoReloadSavable: nil, error: error)
                return
            }
            callback(autoReloadSavable: InCommAutoReloadSavable(json: response), error: nil)
        }
    }
    
    // MARK: GetBalance
    public class func getGiftCardBalance(userId: Int32!, cardId: Int32!, callback: GiftCardBalanceCallback){
        InCommService.get("/Users/\(userId)/Cards/\(cardId)/GetBalance"){(response, error) -> Void in
            if error != nil{
                callback(giftCardBalance: nil, error: error)
                return
            }
            callback(giftCardBalance: InCommGiftCardBalance(json: response), error: nil)
        }
    }
    
    // MARK: GetTransactionHistory
    public class func getGiftCardTransactionHistory(userId: Int32!, cardId: Int32!, callback: GiftCardTransactionHistoryCallback){
        InCommService.get("/Users/\(userId)/Cards/\(cardId)/GetTransactionHistory"){(response, error) -> Void in
            if error != nil{
                callback(giftCardTransactionHistory: nil, error: error)
                return
            }
            callback(giftCardTransactionHistory: InCommGiftCardTransactionHistory(json: response), error: nil)
            return
        }
    }
    
    // MARK: DeleteAutoReload
    public class func deleteAutoReload(userId: Int32!, cardId: Int32!, autoReloadId: Int32!, callback: EmptyResponseCallback){
        InCommService.delete("/Users/\(userId)/Cards/\(cardId)/AutoReloads/\(autoReloadId)"){ (response, error) -> Void in
            if error != nil{
                callback(error: error)
                return
            }
            callback(error: nil)
        }
    }
    
    // MARK: UpdateAutoReloadStatus
    public class func updateAutoReloadStatus(userId: Int32!, cardId: Int32!, autoReloadId: Int32!, active: Bool!, callback: EmptyResponseCallback){
        //        let parameters = InCommJSONDictionary()
        InCommService.put("/Users/\(userId)/Cards/\(cardId)/AutoReloads/\(autoReloadId)/UpdateState?active=\(active)", parameters: nil){ (response, error) -> Void in
            if error != nil{
                callback(error: error)
                return
            }
            callback(error: nil)
        }
    }
    
    // MARK: UpdateUserGiftCardName
    public class func updateUserGiftCardName(userId: Int32!, cardId: Int32!, cardName: String!, callback:UserGiftCardCallback){
        var parameters = InCommJSONDictionary()
        parameters["CardName"] = cardName
        InCommService.put("/Users/\(userId)/Cards/\(cardId)", parameters: parameters){ (response, error) -> Void in
            if error != nil{
                callback(userGiftCard: nil, error: error)
                return
            }
            callback(userGiftCard: InCommUserGiftCard(json: response), error: nil )
        }
    }
    
}